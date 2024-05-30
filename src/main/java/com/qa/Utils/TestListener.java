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

public class TestListener extends BaseClass implements Filter , IAnnotationTransformer {
    private static final Logger log = LogManager.getLogger(TestListener.class);
    @Override
    public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext filterContext) {
        Response response = filterContext.next(requestSpec, responseSpec);
        if (requestSpec.getMethod().equalsIgnoreCase("POST") && requestSpec.getBody() !=
                null && !(response.getBody().asString().length()>1024)) {
            log.info("\n\n Method => " + requestSpec.getMethod() +
                    "\n URI => " + requestSpec.getURI() +
                    "\n Request Headers => " + requestSpec.getHeaders() +
                    "\n Request Body => " + requestSpec.getBody().toString() +
                    "\n Response Body => " + response.getBody().prettyPrint());
        }else if (requestSpec.getMethod().equalsIgnoreCase("POST") &&
                requestSpec.getBody() == null && !(response.getBody().asString().length()>1024)) {
            log.info("\n\n Method => " + requestSpec.getMethod() +
                    "\n URI => " + requestSpec.getURI() +
                    "\n Request Headers => " + requestSpec.getHeaders() +
                    "\n Response Body => " + response.getBody().prettyPrint());
        }else if (requestSpec.getMethod().equalsIgnoreCase("GET") && !
                (response.getBody().asString().length()>1024)) {
            log.info("\n\n Method => " + requestSpec.getMethod() +
                    "\n URI => " + requestSpec.getURI() +
                    "\n Request Headers => " + requestSpec.getHeaders() +
                    "\n Response Body => " + response.getBody().prettyPrint());
        }else if (requestSpec.getMethod().equalsIgnoreCase("GET") &&
                response.getBody().asString().length()>1024) {
            log.info("\n\n Method => " + requestSpec.getMethod() +
                    "\n URI => " + requestSpec.getURI() +
                    "\n Request Headers => " + requestSpec.getHeaders()+
                    "\n Response Body => " + "{ \n Response is too large to print in console \n}");
        }else if (requestSpec.getMethod().equalsIgnoreCase("GET") &&
                (response.getContentType().contains("text/html") ||
                        response.getContentType().contains("application/pdf"))) {
            log.info("\n\n Method => " + requestSpec.getMethod() +
                            "\n URI => " + requestSpec.getURI() +
                            "\n Request Headers => " + requestSpec.getHeaders() +
                            "\n Response Body => " + "{ \n Response is a large html or pdf file too large for console \n}");
    }else if (requestSpec.getMethod().equalsIgnoreCase("PUT") && !
            (response.getBody().asString().length()>1024)) {
        log.info("\n\n Method => " + requestSpec.getMethod() +
                "\n URI => " + requestSpec.getURI() +
                "\n Request Headers => " + requestSpec.getHeaders() +
                "\n Request Body => " + requestSpec.getBody().toString() +
                "\n Response Body => " + response.getBody().prettyPrint());
    }else if (requestSpec.getMethod().equalsIgnoreCase("PATCH") && !
            (response.getBody().asString().length()>1024)) {
        log.info("\n\n Method => " + requestSpec.getMethod() +
                "\n URI => " + requestSpec.getURI() +
                "\n Request Headers => " + requestSpec.getHeaders() +
                "\n Request Body => " + requestSpec.getBody().toString() +
                "\n Response Body => " + response.getBody().prettyPrint());
    }
log.info("************************************************************************");
log.info("************************************************************************");
return response;
}
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
                iTestAnnotation.setRetryAnalyzer(RetryAnalyzer.class);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
