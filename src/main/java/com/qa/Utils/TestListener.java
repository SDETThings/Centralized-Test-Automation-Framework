package com.qa.Utils;

import com.qa.Base.BaseClass;
import com.google.gson.*;
import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IAnnotationTransformer;
import org.testng.ITestNGMethod;
import org.testng.annotations.ITestAnnotation;
import org.testng.annotations.Test;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class TestListener extends BaseClass implements IAnnotationTransformer {

    public synchronized void mergeTestDriverFile() throws IOException {
        setProperties();
        Gson gson = new Gson();
        JsonObject inputFiles;
        FileWriter writer = null;
        JsonArray finalFile = new JsonArray();
        File folderPath = new File(prop.getProperty("ClientTestCaseDriverFolder"));
        JsonObject masterFile = gson.fromJson(new FileReader(prop.getProperty("testDataDriverJson")),JsonObject.class);
        if(masterFile.has("TestCaseRunConfiguration"))
        {
            masterFile.remove("TestCaseRunConfiguration");
        }
        File[] files = folderPath.listFiles();
        for(File file : files)
        {
            if(!file.isDirectory())
            {
                if(file.getName().contains(".json"))
                {
                    inputFiles = gson.fromJson(new FileReader(file),JsonObject.class);
                    finalFile.addAll(inputFiles.getAsJsonArray("TestCaseRunConfiguration"));
                }
            }
        }
        masterFile.add("TestCaseRunConfiguration",finalFile);
        try {
            Gson gson1 = new GsonBuilder().setPrettyPrinting().create();
            writer = new FileWriter(prop.getProperty("testDataDriverJson"));
            gson1.toJson(masterFile, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            writer.close();
        }
    }
    public synchronized boolean shouldBeSkipped(ITestAnnotation annotation,Method methodName) throws IOException {
        mergeTestDriverFile();
        boolean isPresent = false;
        FileReader reader = null;
        try {
            reader = new FileReader(prop.getProperty("testDataDriverJson"));
            JsonObject testDriver = JsonParser.parseReader(reader).getAsJsonObject();
            JsonArray TestCaseRunConfigurationArray =
                    testDriver.get("TestCaseRunConfiguration").getAsJsonArray();
            JsonObject groupRunBlock =
                    TestCaseRunConfigurationArray.get(0).getAsJsonObject();
            String group = groupRunBlock.get("groupName").getAsString();
            if(group.equals("")) {
                for (JsonElement testCaseInfo : TestCaseRunConfigurationArray) {
                    JsonObject TestCaseRunConfigurationArrayElements =
                            testCaseInfo.getAsJsonObject();
                    String testCaseNumber =
                            TestCaseRunConfigurationArrayElements.get("testCaseId").getAsString();
                    String Environment =
                            TestCaseRunConfigurationArrayElements.get("Environment").getAsString();
                    String Client =
                            TestCaseRunConfigurationArrayElements.get("Client").getAsString();
                    if (testCaseNumber.equals(methodName.getName().toString())) {
                        if (!annotation.getEnabled()) {
                            isPresent = true;
                            break;
                        }
                    }
                }
            }else
            {
                for (JsonElement testCaseInfo : TestCaseRunConfigurationArray) {
                    JsonObject TestCaseRunConfigurationArrayElements =
                            testCaseInfo.getAsJsonObject();
                    String groupsConcatenated = "";
                    String groupName =
                            TestCaseRunConfigurationArrayElements.get("groupName").getAsString();
                    String[] groups = annotation.getGroups();
                    for(String groupValue : groups) {
                        groupsConcatenated = groupsConcatenated+","+groupValue;
                    }
                    if (groupsConcatenated.contains(groupName)) {
                        if (!annotation.getEnabled()) {
                            isPresent = true;
                            break;
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }finally
        {
            reader.close();
        }
        return isPresent;
    }
    @Override
    public void transform(ITestAnnotation iTestAnnotation, Class aClass, Constructor constructor, Method method) {
        try {
            if(shouldBeSkipped(iTestAnnotation,method))
            {
                iTestAnnotation.setEnabled(true);
                //iTestAnnotation.setRetryAnalyzer(RetryAnalyzer.class);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
