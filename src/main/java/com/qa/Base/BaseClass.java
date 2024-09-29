package com.qa.Base;

import APIHelper.HeaderProvider;
import APIHelper.RequestProvider;
import APIHelper.UrlProvider;
import com.aventstack.extentreports.ExtentTest;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import com.qa.Utils.*;
import com.aventstack.extentreports.Status;
import com.google.gson.*;
import com.qa.component.API.Certificates.ClientCertificate;
import com.qa.component.API.ReportLogging.APIReportLogging;
import com.qa.component.API.URIConstruction.*;
import DataHandlerUtils.JsonCompareUtils;
import dataUtils.MasterDataUtils;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.restassured.RestAssured;
import io.restassured.config.SSLConfig;
import io.restassured.http.Method;
import io.restassured.response.Response;
import DataHandlerUtils.JsonOperations;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.Reporter;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class BaseClass {
    public static ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();
    MasterDataUtils masterDataUtils;
    APIReportLogging apiReportlogging;
    public static Properties prop;
    public ThreadLocal<List<JsonObject>> AllpayloadUsed = new ThreadLocal<>();
    public ThreadLocal<Map<String ,JsonElement>> AllpayloadUsed1 = new ThreadLocal<>();
    public ThreadLocal<WebDriver> LocalWebDriver= new ThreadLocal<>();
    public ThreadLocal<RemoteWebDriver> LocalRemoteWebDriver= new ThreadLocal<>();
    public ThreadLocal<AndroidDriver> LocalAndroidDriver= new ThreadLocal<>();
    public ThreadLocal<IOSDriver> LocalIOSDriver= new ThreadLocal<>();
    JsonOperations jsonOperations;
    public ThreadLocal<Integer> TestIterationCount= new ThreadLocal<>();
    public synchronized String generateRandomName(int nameLength) {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(nameLength);
        for (int i = 0; i < nameLength; i++) {
            int randomLimitedInt = leftLimit + (int) (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        return buffer.toString();
    }
    public synchronized String generateRandomDateOfBirthBetween(String dateStart,String dateEnd ) {
        LocalDate startDate = LocalDate.of(Integer.parseInt(dateStart.split("./")[0]),Integer.parseInt(dateStart.split("./")[1]),Integer.parseInt(dateStart.split("./")[2]));
        LocalDate endDate = LocalDate.of(Integer.parseInt(dateEnd.split("./")[0]),Integer.parseInt(dateEnd.split("./")[1]),Integer.parseInt(dateEnd.split("./")[2]));
        long statEpochDay = startDate.toEpochDay();
        long endEpochDay = endDate.toEpochDay();
        long randomEpochDay = ThreadLocalRandom.current().nextLong(statEpochDay,endEpochDay+1);
        LocalDate randomeDate = LocalDate.ofEpochDay(randomEpochDay);
        return randomeDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
    public synchronized String LaunchMobileDriver(String OsName, String deviceName, String osVersion,String AppId, String Description, String testCaseNumber) {
        String USERNAME = prop.getProperty("BSUSERNAME");
        String AUTOMATE_KEY = prop.getProperty("AUTOMATE_KEY");
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("deviceName", deviceName);
        capabilities.setCapability("osVersion", osVersion);
        capabilities.setCapability("app", AppId);
        capabilities.setCapability("browserstack.user", USERNAME);
        capabilities.setCapability("browserstack.key", AUTOMATE_KEY);
        capabilities.setCapability("unicodeKeyboard", true); // Enable Unicode keyboard
        capabilities.setCapability("resetKeyboard", true);
        capabilities.setCapability("browserstack.local", true);
        HashMap<String, Object> browserstackOptions = new HashMap<>();
        browserstackOptions.put("projectName", Description);
        browserstackOptions.put("buildName", testCaseNumber);
        capabilities.setCapability("bstack:options", browserstackOptions);
        String sessionId;
        if (OsName.equalsIgnoreCase("Android")){
            AndroidDriver driver = null;
            try {
                driver = new AndroidDriver(new URL("https://" + USERNAME + ":" +
                        AUTOMATE_KEY + "@hub-cloud.browserstack.com/wd/hub"), capabilities);
            }
            catch (Exception e)
            {
                String TestException = e.getMessage();
                System.out.println("Test Mobile Driver Exception " +TestException);
            }
            LocalAndroidDriver.set(driver);
            assert driver != null;
            sessionId = driver.getSessionId().toString();
        }
        else
        {
            IOSDriver driver = null;
            try {
                driver = new IOSDriver(new URL("https://" + USERNAME + ":" + AUTOMATE_KEY +
                        "@hub-cloud.browserstack.com/wd/hub"), capabilities);
            }
            catch (Exception e)
            {
                String TestException = e.getMessage();
                System.out.println("Test Mobile Driver Exception " +TestException);
            }
            LocalIOSDriver.set(driver);
            assert driver != null;
            sessionId = driver.getSessionId().toString();
        }
        return GetBSPropertyExecution(OsName,sessionId,USERNAME,AUTOMATE_KEY);
    }
    private RemoteWebDriver RemoteBrowser(String BrowserName, String BrowserVersion, String OSName, String OSVersion,String Description, String testCaseNumber ) throws Exception {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        String USERNAME = prop.getProperty("BSUSERNAME");
        String AUTOMATE_KEY = prop.getProperty("AUTOMATE_KEY");
        String BURL = "https://" + USERNAME + ":" + AUTOMATE_KEY + "@hubcloud.browserstack.com/wd/hub";
        capabilities.setCapability("browserName", BrowserName);
        capabilities.setCapability("browserVersion", BrowserVersion);
        HashMap<String, Object> browserstackOptions = new HashMap<String, Object>();
        browserstackOptions.put("os", OSName);
        browserstackOptions.put("osVersion", OSVersion);
        browserstackOptions.put("projectName", Description);
        browserstackOptions.put("buildName", testCaseNumber);
        capabilities.setCapability("bstack:options", browserstackOptions);
        RemoteWebDriver driver = new RemoteWebDriver(new URL(BURL), capabilities);
        return driver;
    }
    public synchronized String GetBSPropertyExecution(String OSName, String sessionId, String username , String accessKey) {
        if (OSName.equalsIgnoreCase("Windows")||OSName.equalsIgnoreCase("OS X"))
        {
            RestAssured.baseURI = String.format("https://api.browserstack.com/automate/sessions/%s.json", sessionId);
        }
        else {
            RestAssured.baseURI = String.format("https://api.browserstack.com/app-automate/sessions/%s.json", sessionId);
            RestAssured.authentication = RestAssured.preemptive().basic(username, accessKey);
        }
        // Send the request and get the response
        Response response = RestAssured.get();
        if (response.getStatusCode() == 200) {
            // Parse the JSON response and extract the public link
            String publicLink = response.getBody().jsonPath().getString("automation_session.public_url");
            System.out.println("Public Link: " + publicLink);
            return publicLink;
        }
        else {
            // Handle the error case
            System.out.println("Error: " + response.getStatusCode());
            return "Execution failed"+response.getStatusCode();
        }
    }
    public synchronized RemoteWebDriver getRemoteWebDriver(){
        return LocalRemoteWebDriver.get();
    }
    public synchronized WebDriver getWebDriver(){
        return LocalWebDriver.get();
    }
/*    public synchronized void readJSONDriver(String JsonDriverPath) throws IOException {
        Gson gson = new Gson();
        FileReader reader = new FileReader(JsonDriverPath);
        testDataDriver = gson.fromJson(reader, TestDataDriver.class);
    }*/
/*    public synchronized TestDataDriver getRunConfigurations() {
        return testDataDriver;
    }*/
    public static void setProperties() {
        try {
            FileInputStream fis = new FileInputStream("./Config.properties");
            prop = new Properties();
            prop.load(fis);
        } catch (IOException var2) {
            var2.printStackTrace();
        }
    }
    public synchronized void setResults(String testCaseId, String env, String client,JsonElement dataSet) {
        String testResultsFolder = ReportListeners.getCurrentTestResultsFolder();
        File file = new File(testResultsFolder);
        ITestResult result = Reporter.getCurrentTestResult();
        result.setAttribute("env",env);
        result.setAttribute("client",client);
        result.setAttribute("documentId",file.getName());
        result.setAttribute("testCaseFolder",testResultsFolder);
        result.setAttribute("testCaseDescription",dataSet.getAsJsonObject().get("DESCRIPTION"));
    }
    public synchronized void setResults(String env, String client,String testCaseFolderPath,String Link,String TestData,String BrowserDetails) {
        File file = new File(testCaseFolderPath);
        ITestResult result = Reporter.getCurrentTestResult();
        result.setAttribute("env",env);
        result.setAttribute("client",client);
        result.setAttribute("documentId",file.getName());
        result.setAttribute("testCaseFolder",testCaseFolderPath);
        result.setAttribute("testCaseBSLink",Link);
        result.setAttribute("testdata",TestData);
        result.setAttribute("browserDetails",BrowserDetails);
    }
    public synchronized void storeTestDataJsonIntoListForDatabaseConsumption(List<JsonObject> payloadList , JsonObject jsonObject){
        payloadList.add(jsonObject);
        AllpayloadUsed.set(payloadList);
    }
    public synchronized void storeTestDataJsonIntoListForDatabaseConsumption(List<String> payloadList , String mainframeData) {
        payloadList.add(mainframeData);
    }
    public synchronized void storeTestDataJsonIntoListForDatabaseConsumption(Map<String,JsonElement> payloadList , JsonElement jsonObject,String url) {
        payloadList.put(" \""+url+"\" "+":",jsonObject);
        AllpayloadUsed1.set(payloadList);
    }
    public synchronized String findJsonPayloadFile(String client, String fileIndicator) throws IOException {
        String requiredFileName = null;
        File folder = null;
        String folderPath = null;
        switch (getClientName(client)) {
            case "CLIENT001":
                folderPath = prop.getProperty("Client001RequestPayloadFolder");
                folder = new File(folderPath);
                break;
            case "CLIENT002":
                folderPath = prop.getProperty("Client002RequestPayloadFolder");
                folder = new File(folderPath);
                break;
            case "CLIENT003":
                folderPath =prop.getProperty("Client003RequestPayloadFolder");
                folder = new File(folderPath);
                break;
            default:
        }
        if (folder.exists() && folder.isDirectory()) {
            File[] jsonFiles = folder.listFiles((dir, name) -> name.endsWith(".json"));
            if (jsonFiles == null) {
                throw new RuntimeException("There are no .json files");
            } else {
                File[] fileList = jsonFiles;
                int numberOfJsonFiles = jsonFiles.length;
                for(int i = 0; i < numberOfJsonFiles; i++) {
                    File jsonfile = fileList[i];
                    if (jsonfile.getName().split("_")
                            [0].toLowerCase().equalsIgnoreCase(fileIndicator.toLowerCase()) ||
                            fileIndicator.toLowerCase().equalsIgnoreCase(jsonfile.getName().split("_")
                                    [0].toLowerCase())) {
                        requiredFileName = jsonfile.getName();
                        break;
                    }
                }
                requiredFileName = folderPath + requiredFileName;
                return requiredFileName;
            }
        } else {
            throw new IllegalArgumentException("Invalid folder path");
        }
    }
    public synchronized String getClientName(String clientId) {
        String clientName = null;
        switch(clientId)
        {
            case "CL001":
            {
                clientName="CLIENT001";
                break;
            }
            case "CL002":
            {
                clientName="CLIENT002";
                break;
            }
            case "CL003":
            {
                clientName="CLIENT003";
                break;
            }
            default:
        }
        return clientName;
    }
    public synchronized String getProductName(String productId) {
        String productName = null;
        switch(productId)
        {
            case "PRO01":
            {
                productName="PRODUCT001";
                break;
            }
            case "PRO02":
            {
                productName="PRODUCT002";
                break;
            }
            case "PRO03":
            {
                productName="PRODUCT003";
                break;
            }
            case "PRO04":
            {
                productName="PRODUCT004";
                break;
            }
            case "PRO05":
            {
                productName="PRODUCT005";
                break;
            }
            case "PRO06":
            {
                productName="PRODUCT006";
                break;
            }
            case "PRO07":
            {
                productName="PRODUCT007";
                break;
            }
            case "PRO08":
            {
                productName="PRODUCT008";
                break;
            }
            case "PRO09":
            {
                productName="PRODUCT009";
                break;
            }
            case "PRO10":
            {
                productName="PRODUCT010";
                break;
            }
            case "PRO11":
            {
                productName="PRODUCT011";
                break;
            }
            case "PRO12":
            {
                productName="PRODUCT012";
                break;
            }case "PRO13":
            {
                productName="PRODUCT013";
                break;
            }case "PRO14":
            {
                productName="PRODUCT014";
                break;
            }case "PRO15":
            {
                productName="PRODUCT015";
                break;
            }case "PRO16":
            {
                productName="PRODUCT016";
                break;
            }
        }
        return productName;
    }
    public String getMasterDataJsonPath(String client) {
        apiReportlogging = new APIReportLogging();
        String masterDataPath = null;
        switch(getClientName(client)) {
            case "CLIENT001":
                masterDataPath = prop.getProperty("Client001TestCaseDataJson");
                break;
            case "CLIENT002":
                masterDataPath =prop.getProperty("Client002TestCaseDataJson");
                break;
            case "CLIENT003":
                masterDataPath=prop.getProperty("Client003TestCaseDataJson");
                break;
            default:
        }
        return masterDataPath;
    }
    public synchronized Map<String, JsonElement> getbrowserStackDetails(String brandName) throws IOException {
        jsonOperations = new JsonOperations();
        Map<String, JsonElement> details = new LinkedHashMap<>();
        JsonObject browserStackDetails
                =jsonOperations.readJsonFileAndStoreInJsonObject(prop.getProperty("BrowserStackDetailsFile"));
        JsonArray brandDetails =
                browserStackDetails.get("BrandWiseBrowserStackConfigurationDetails").getAsJsonArray();
        for(int i=0;i<brandDetails.size();i++)
        {
            JsonObject element = brandDetails.get(i).getAsJsonObject();
            String brand = element.get("brandName").getAsString();
            if (brandName.equalsIgnoreCase(brand))
            {
                JsonObject detailsObject = element.get("details").getAsJsonObject();
                details.put("Eservice_url",detailsObject.get("Eservice_url"));
                details.put("Fusion_url",detailsObject.get("Fusion_url"));
                details.put("AndroidAppName",detailsObject.get("AndroidAppName"));
                details.put("iosAppName",detailsObject.get("iosAppName"));
            }
        }
        return details;
    }
    public synchronized void savePdfToFile(byte[] pdfContent,String filePath){
        try {
            Path path = Paths.get(filePath);
            Files.write(path, pdfContent);
        } catch (IOException e) {
            System.err.println("Error saving pdf "+filePath);
            throw new RuntimeException(e);
        }
    }
    public synchronized boolean pdfComapre(String actualPdf,String expectedPdf ,String resultFilePath ) throws IOException {
        PdfUtils pdfUtils = new PdfUtils();
        boolean isThereMismatches = pdfUtils.pdfComparatorFunc(actualPdf,expectedPdf,resultFilePath);
        return isThereMismatches;
    }
    public String getEnvironmentBaseURL(String env,String client,String baseUrlType, String endpointName) {
        String baseURl = null;
        if(endpointName.contains(Constants.ADD_NEW_PET))
        {
            baseURl = EnvironmentBaseUrls.RESFTULL_BOOKER_BASE_URL;
        }
        else {
            switch (env) {
                case "ENV001": {
                    if (getClientName(client).equalsIgnoreCase("CLIENT001") && baseUrlType.equalsIgnoreCase("APIM")) {
                        baseURl = EnvironmentBaseUrls.PETSTORE_BASE_URL;
                        break;
                    } else if (getClientName(client).equalsIgnoreCase("CLIENT001") && baseUrlType.equalsIgnoreCase("FUNCTION_APP")) {
                        {
                            baseURl = EnvironmentBaseUrls.PETSTORE_BASE_URL;
                            break;
                        }
                    }
                }
                    case "ENV002": {
                        if (getClientName(client).equalsIgnoreCase("CLIENT002") && baseUrlType.equalsIgnoreCase("APIM")) {
                            baseURl = EnvironmentBaseUrls.PETSTORE_BASE_URL;
                            break;
                        } else if (getClientName(client).equalsIgnoreCase("CLIENT001") && baseUrlType.equalsIgnoreCase("FUNCTION_APP")) {
                            {
                                baseURl = EnvironmentBaseUrls.PETSTORE_BASE_URL;
                                break;
                            }
                        }
                    }
                        case "ENV003": {
                            if (getClientName(client).equalsIgnoreCase("CLIENT003") && baseUrlType.equalsIgnoreCase("APIM")) {
                                baseURl = EnvironmentBaseUrls.PETSTORE_BASE_URL;
                                break;
                            } else if (getClientName(client).equalsIgnoreCase("CLIENT001") && baseUrlType.equalsIgnoreCase("FUNCTION_APP")) {
                                {
                                    baseURl = EnvironmentBaseUrls.PETSTORE_BASE_URL;
                                    break;
                                }

                            }
                        }
                            default:
            }
        }


        return baseURl;
    }
    public String getStratusEnvironmentBaseURL(String baseUrlType , String pillarType) {
        String baseURl = null;
        if ( baseUrlType.equalsIgnoreCase("APIM") && pillarType.equalsIgnoreCase("CL")) {
            baseURl = EnvironmentBaseUrls.NDT_CL_BASEURL;
        }
        return baseURl;
    }
    public String retrieveApplicationEndpoint( String endpointName) throws IllegalAccessException {
        ApplicationEndpoints applicationEndpoints= new ApplicationEndpoints();
        String envEndpointName = getObject(applicationEndpoints, endpointName);
        return envEndpointName;
    }
    public String modifyEndpoint( String endpointName,Map<String,String> replacements) throws IllegalAccessException {
        if(endpointName.contains("{") && !replacements.isEmpty())
        {
            Set<String> keys = replacements.keySet();
            for(String key : keys)
            {
                endpointName = endpointName.replace("{{"+key+"}}",replacements.get(key));
            }
        }

        return endpointName;
    }
   /* public synchronized Response UploadEvidenceAPIRequest(String filePath) {
        String BaseUrl =prop.getProperty("GenesisBaseUploadUrl");
        String endpoint = "v1/uploadTestEvidence";
        String completeRequestURIString = BaseUrl + endpoint;
        Response res = RestAssured
                .given()
                .filter(new TestListener())
                .contentType("multipart/form-data")
                .multiPart("fileUpload",new File(filePath))
                .when()
                .post(completeRequestURIString);
        return res;
    }*/
    public synchronized String getTestCaseStatus(boolean booleanStatus) {
        String status;
        if(booleanStatus)
        {
            status= "Passed";
        }else {
            status = "Failed";
        }
        return status;
    }
   /* public synchronized Response sendAPIExecutionDetails(ITestResult iTestResult) throws ParseException, IOException {
        String testData;
        String testCaseId = iTestResult.getName();
        String result = getTestCaseStatus(iTestResult.isSuccess());
        if(AllpayloadUsed1.get() !=null)
        {
            testData = AllpayloadUsed1.get().toString().replace("="," ");
            AllpayloadUsed1.remove();
        }else {
            testData = " No payload available";
        }
        String excecutionDateTime = geCurrentTimeStamp();
        String documentId = iTestResult.getAttribute("documentId").toString();
        String executionLog = "";
        String executionLink = "";
        String client = iTestResult.getAttribute("client").toString();
        String browserOrOsVersion = "";
*//*if(prop.getProperty("RunOn").equalsIgnoreCase("Local"))
{
String runOn = "Local";
}*//*
        String env = iTestResult.getAttribute("env").toString();
        JsonObject databasePayload =
                createRequestPayloadForDatabaseExecutionEntry( testCaseId,result,testData,excecutionDateTime,
                        documentId,executionLog,executionLink,client,env,browserOrOsVersion);
        Response response = hitExecutionResultsDatabaseAPI(databasePayload);
        return response;
    }*/
/*    public synchronized Response sendExecutionDetails(ITestResult iTestResult,String link, String TestData,String BrowserDetails) throws ParseException, IOException {
        String testData;
        String testCaseId = iTestResult.getName();
        String result = getTestCaseStatus(iTestResult.isSuccess());
        if(AllpayloadUsed.get() !=null)
        {
            testData = AllpayloadUsed.get().toString();
            AllpayloadUsed.remove();
        }
        if(TestData != null)
        {
            testData = TestData;
        }
        else {
            testData = " No payload available";
        }
        String excecutionDateTime = geCurrentTimeStamp();
        String documentId = iTestResult.getAttribute("documentId").toString();
        String executionLog = "";
        String executionLink = link;
        String client = iTestResult.getAttribute("client").toString();
        String browserOrOsVersion = BrowserDetails;
        String env = iTestResult.getAttribute("env").toString();
        JsonObject databasePayload =
                createRequestPayloadForDatabaseExecutionEntry( testCaseId,result,testData,excecutionDateTime,
                        documentId,executionLog,executionLink,client,env,browserOrOsVersion);
        Response response = hitExecutionResultsDatabaseAPI(databasePayload);
        return response;
    }*/
    /*public synchronized Response sendHybridExecutionDetails(ITestResult iTestResult) throws ParseException, IOException {
        String testData;
        String testCaseId = iTestResult.getName();
        String result = getTestCaseStatus(iTestResult.isSuccess());
        if(AllpayloadUsed.get() !=null)
        {
            testData = AllpayloadUsed.get().toString();
        }else {
            testData = " No payload available";
        }
        String excecutionDateTime = geCurrentTimeStamp();
        String documentId = iTestResult.getAttribute("documentId").toString();
        String executionLog = "";
        String executionLink = "";
        String client = iTestResult.getAttribute("client").toString();
        String browserOrOsVersion = "";
        String env = iTestResult.getAttribute("env").toString();
        JsonObject databasePayload =
                createRequestPayloadForDatabaseExecutionEntry( testCaseId,result,testData,excecutionDateTime,
                        documentId,executionLog,executionLink,client,env,browserOrOsVersion);
        Response response = hitExecutionResultsDatabaseAPI(databasePayload);
        return response;
    }*/
/*    public synchronized Response hitExecutionResultsDatabaseAPI(JsonObject jsonObject) {
        String payloadBody = jsonObject.toString();
        String BaseUrl = prop.getProperty("GenesisBaseURL");
        String endpoint = "v1/addTestExecution";
        String completeRequestURIString = BaseUrl + endpoint;
        Response res = RestAssured
                .given()
                .filter(new TestListener())
                .contentType("application/json")
                .body(payloadBody)
                .when()
                .post(completeRequestURIString);
        return res;
    }*/
    public synchronized JsonObject createRequestPayloadForLogEntry( String documentId, String uniqueIdentifier ) throws IOException {
        JsonObject jobj =
                jsonOperations.readJsonFileAndStoreInJsonObject(prop.getProperty("LogAnalysisRequestPayloadFolderPath"));
        jobj.addProperty("documentId",documentId);
        jobj.addProperty("appInsightsId",uniqueIdentifier);
        return jobj;
    }
    public synchronized JsonObject createRequestPayloadForDatabaseExecutionEntry( String testCaseId, String result, String testData, String excecutionDateTime, String documentId, String executionLog , String executionLink , String client, String env,String browserOrOsVersion) throws IOException {
        JsonObject jobj = jsonOperations.readJsonFileAndStoreInJsonObject(prop.getProperty("DatabaseExecutionResultsRequestPayloadFolderPath"));
        jobj.addProperty("testCaseId",testCaseId);
        jobj.addProperty("result",result);
        jobj.addProperty("testData",testData);
        jobj.addProperty("excecutionDateTime", excecutionDateTime);
        jobj.addProperty("documentId",documentId);
        jobj.addProperty("executionLog",executionLog);
        jobj.addProperty("executionLink",executionLink);
        jobj.addProperty("browserORosVersion",browserOrOsVersion);
        jobj.addProperty("clientId",client);
        jobj.addProperty("environmentId",env);
        return jobj;
    }
    public static String getObject(Object obj, String endpointName) throws IllegalAccessException {
        String variableName = null;
        Field[] fields = obj.getClass().getDeclaredFields();
        int numberOfFields = fields.length;
        for(int i = 0; i < numberOfFields; i++) {
            Field field = fields[i];
            if (field.getName().toLowerCase().contains(endpointName.toLowerCase()) ||
                    endpointName.toLowerCase().contains(field.getName().toLowerCase())) {
                field.setAccessible(true);
                variableName = (String)field.get(obj);
                break;
            }
        }
        return variableName;
    }
    public synchronized String geCurrentTimeStamp() throws ParseException {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
                dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        String foramttedTimeStamp = dateFormat.format(currentDate);
        return foramttedTimeStamp;
    }
    public synchronized void refreshFolder(String folderPath , String fileName ,int seconds) {
        File folder = new File(folderPath);
        int elapsedSecond = 0;
        while(!containsFile(folder,fileName) && elapsedSecond<seconds)
        {
            try{
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            folder= new File(folderPath);
            elapsedSecond++;
        }
        if(!containsFile(folder,fileName))
        {
            System.out.println("Timeout occured ");
        }
    }
    public synchronized boolean containsFile(File folder , String filenName) {
        File[] files = folder.listFiles();
        if (files != null)
        {
            for(File file : files)
            {
                if(file.getName().equalsIgnoreCase(filenName))
                {
                    return true;
                }
            }
        }
        return false;
    }
    public synchronized String[] getCertificateFromLocalBasedOnClient(String clientId) {
        String clientName = getClientName(clientId) ;
        String[] credentials = new String[2];
        switch(clientName)
        {
            case "BOOHOO":
                credentials[0] = prop.get("GoosePfxFilePath").toString();
                credentials[1] = prop.get("GoosePfxPassphrase").toString();
                break;
            case "LBG":
            case "NEWDAY":
                credentials[0] = prop.get("DekoPfxFilePath").toString();
                credentials[1] = prop.get("DekoPfxPassphrase").toString();
                break;
        }
        return credentials;
    }
   /* public synchronized void afterTestApiActivities(ITestResult iTestResult) throws ParseException, IOException, InterruptedException {
        FileUtils fileUtils = new FileUtils();
        String TestCaseResultsZipFolder = prop.getProperty("TestCaseResultsZipFolder");
        String resultsFolder = iTestResult.getAttribute("testCaseFolder").toString();
        String base64EncodedFolder = prop.getProperty("Base64EncodedFolder");
        String fileToBeSent = fileUtils.convertAndWriteFileToBase64(TestCaseResultsZipFolder,
                resultsFolder, base64EncodedFolder);
        Thread.sleep(2000);
// Send Execution details to DB
        Response executionDetails = sendAPIExecutionDetails(iTestResult);
        Assert.assertEquals(executionDetails.getStatusCode(), 200);
        Thread.sleep(2000);
// Send Execution file
        Response uploadEvidenceResponse = UploadEvidenceAPIRequest(fileToBeSent);
        Assert.assertEquals(uploadEvidenceResponse.getStatusCode(), 200);
    }*/
    public synchronized void setRetryCount(int count) {
        TestIterationCount.set(count);
    }
    public synchronized ArrayList<String> getKeysTobePopulatedThroughFramework(JsonObject unalteredRequestPayload) {
        JsonCompareUtils jsonCompareUtils = new JsonCompareUtils();
        ArrayList<String> keysToBePopulatedByFramework = new ArrayList<>();
        Iterator<String> keys = unalteredRequestPayload.keySet().iterator();
        while(keys.hasNext())
        {
            String key = keys.next();
            if(jsonCompareUtils.getValueFromComplexJson(unalteredRequestPayload, key, "").isJsonPrimitive())
            {
                if(jsonCompareUtils.getValueFromComplexJson(unalteredRequestPayload, key, "").getAsString().equalsIgnoreCase(""))
                {
                    keysToBePopulatedByFramework.add(key);
                }
            }
        }
        return keysToBePopulatedByFramework;
    }
    public synchronized Map<String,JsonElement> buildFrameworkMap(ArrayList<String> keysList,String guid,String lastName ,String DateOfBirth,String... userName) {
        Map<String,JsonElement> frameworkMap = new HashMap<>();
        for(String key : keysList)
        {
            if(key.equalsIgnoreCase("CustomerRef"))
            {
                frameworkMap.put("CustomerRef",JsonParser.parseString(guid));
            } else if (key.equalsIgnoreCase("LastName")) {
                frameworkMap.put("LastName",JsonParser.parseString(lastName));
            } else if (key.equalsIgnoreCase("DateOfBirth")) {
                frameworkMap.put("DateOfBirth",JsonParser.parseString(DateOfBirth));
            }else if (key.equalsIgnoreCase("ExternalRef")) {
                frameworkMap.put("ExternalRef",JsonParser.parseString(guid));
            }else if (key.equalsIgnoreCase("ApplicantReferralId")) {
                frameworkMap.put("ApplicantReferralId",JsonParser.parseString(guid));
            }else if (key.equalsIgnoreCase("MerchantId")) {
                frameworkMap.put("MerchantId",JsonParser.parseString(guid));
            }else if (key.equalsIgnoreCase("credentialsUsername")) {
                frameworkMap.put("credentialsUsername",JsonParser.parseString(userName[0]));
            }else if (key.equalsIgnoreCase("verificationId")) {
                frameworkMap.put("verificationId",JsonParser.parseString(guid));
            }else if (key.equalsIgnoreCase("sessionId")) {
                frameworkMap.put("sessionId",JsonParser.parseString(guid));
            }
        }
        return frameworkMap;
    }
    public synchronized Map<String,JsonElement> buildFrameworkMap(ArrayList<String> keysList,Response... previousResponse) {
        Map<String,JsonElement> frameworkMap = new HashMap<>();
        for(String key : keysList)
        {
            if(key.equalsIgnoreCase("shipDate"))
            {
                frameworkMap.put("shipDate",JsonParser.parseString(getCurrentDate()));
            }
        }
        return frameworkMap;
    }
    public synchronized JsonElement getFrameworkGenertedValues(String key) {
        JsonElement value=null;
        String lastPart;
        if(key.contains("."))
        {
            lastPart= key.split("\\.")[key.split("\\.").length-1];

        }else{
            lastPart = key;
        }
        if(lastPart.equalsIgnoreCase("id"))
        {
           return JsonParser.parseString(String.valueOf(generateRandomNumber(3)));
        }
        else if(lastPart.equalsIgnoreCase("lastName"))
        {
            return JsonParser.parseString(generateRandomName(10));
        }
        else{
            return value;
        }
    }
    public synchronized JsonObject getTestCaseMetaDataBlock(String testCaseNumber) throws FileNotFoundException {
        JsonObject requiredBlock = null;
        FileReader reader = new FileReader(prop.getProperty("testDataDriverJson"));
        JsonObject testDriver = JsonParser.parseReader(reader).getAsJsonObject();
        JsonArray TestCaseRunConfigurationArray = testDriver.get("TestCaseRunConfiguration").getAsJsonArray();
        for (JsonElement testCaseInfo : TestCaseRunConfigurationArray) {
            JsonObject TestCaseRunConfigurationArrayElement = testCaseInfo.getAsJsonObject();
            if (TestCaseRunConfigurationArrayElement.get("testCaseId").getAsString().equalsIgnoreCase(testCaseNumber)) {
                requiredBlock = TestCaseRunConfigurationArrayElement;
            }
        }
        return requiredBlock;
}
    public synchronized int generateRandomNumber(int size) {
        int lowerBound = (int) Math.pow(10, size - 1);
        int upperBound = (int) Math.pow(10, size) - 1;

        // Generate and return a random integer within that range
        Random random = new Random();
        return lowerBound + random.nextInt(upperBound - lowerBound + 1);
    }
    public synchronized String getCurrentDate() {
        // Get the current date
        LocalDate currentDate = LocalDate.now();
        // Create a formatter for the desired format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // Format the current date
        String formattedDate = currentDate.format(formatter);

        return formattedDate;
    }
    public static WebDriver getDriver() {
        return tlDriver.get();
    }
    public static synchronized void setWebDriver(WebDriver driver){
        tlDriver.set(driver);
    }
    public Object getPageClassObject(String client,String PageClassConstant) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, MalformedURLException {
        String pageFolderPath = null;
        Object instance = null;
        switch(getClientName(client)){
            case "CLIENT001":
                pageFolderPath= prop.getProperty("Client001_PageFolder");
                break;
            case "CLIENT002":
                pageFolderPath= prop.getProperty("Client002_PageFolder");
                break;
            case "CLIENT003":
                pageFolderPath= prop.getProperty("Client003_PageFolder");
                break;
        }
        File file = new File(pageFolderPath);
        URL[] urls = {file.toURI().toURL()};

        // Create a new class loader with the directory
        URLClassLoader classLoader = new URLClassLoader(urls);
        File[] fileList = file.listFiles();
        for(File pageFile : fileList)
        {
            String pageFileClassName = pageFile.getName();
            String className = pageFileClassName.substring(0, pageFileClassName.length() - 6);
            if(pageFileClassName.contains(PageClassConstant))
            {
                Class<?> cls = classLoader.loadClass(className);
                instance = cls.getDeclaredConstructor().newInstance();
            }
        }
        return instance;
    }
    public synchronized  JsonElement getValueFromComplexNestedPayload(JsonObject jsonObject, String path){
        String[] parts = path.split("\\.");
        JsonElement currentElement = jsonObject;

        for (String part : parts) {
            if (part.contains("[") && part.contains("]")) {
                // Handle array index
                String arrayName = part.substring(0, part.indexOf("["));
                int index = Integer.parseInt(part.substring(part.indexOf("[") + 1, part.indexOf("]")));
                JsonArray jsonArray = currentElement.getAsJsonObject().getAsJsonArray(arrayName);
                currentElement = jsonArray.get(index);
            } else {
                // Handle object key
                currentElement = currentElement.getAsJsonObject().get(part);
            }
        }

        return currentElement;
    }
    private static void updateJsonWithKeyPath(JsonObject json, String keyPath, JsonElement newValue) {
        String[] keys = keyPath.split("\\.");
        JsonObject currentObj = json;

        for (int i = 0; i < keys.length; i++) {
            String key = keys[i];

            // Check for array access
            if (key.contains("[") && key.contains("]")) {
                String arrayKey = key.substring(0, key.indexOf("["));
                int arrayIndex = Integer.parseInt(key.substring(key.indexOf("[") + 1, key.indexOf("]")));

                // Navigate to the array
                if (!currentObj.has(arrayKey)) {
                    currentObj.add(arrayKey, new JsonArray());
                }

                JsonArray array = currentObj.getAsJsonArray(arrayKey);

                // Ensure the array is large enough
                while (array.size() <= arrayIndex) {
                    array.add(new JsonObject());
                }

                currentObj = array.get(arrayIndex).getAsJsonObject();
            }
            else {
                // Normal object key
                if (i == keys.length - 1) {
                    // Last key, update the value
                    currentObj.add(key, newValue);
                } else {
                    // Create a new object if it doesn't exist
                    if (!currentObj.has(key)) {
                        currentObj.add(key, new JsonObject());
                    }
                    currentObj = currentObj.getAsJsonObject(key);
                }
            }
        }
    }
    public String getUrlReplaced(String url , Map<String, String> valuesMap) {
        String completeFinalUrl=url ;
        for (Map.Entry<String, String> entry : valuesMap.entrySet()) {
            completeFinalUrl = url.replaceAll("\\{" + entry.getKey() + "\\}", entry.getValue());
        }
        return completeFinalUrl;
    }
    public int getExpectedStatusCode(String endpointName) throws FileNotFoundException {
        Gson gson = new Gson();
        int statusCode = 0;
        JsonObject endpointDetailsFile = gson.fromJson(new FileReader(prop.getProperty("EndpointsJsonFile")), JsonObject.class);
        if(endpointDetailsFile.has("EndpointDetails")){
            for(JsonElement endpointDetails:endpointDetailsFile.getAsJsonArray("EndpointDetails")){
                if(endpointDetails.getAsJsonObject().get("EndpointName").getAsString().equalsIgnoreCase(endpointName)){
                    statusCode= endpointDetails.getAsJsonObject().get("statusCode").getAsInt();
                }
            }
        }
        return statusCode;
    }
    public JsonObject IcallBuildRequestPayload(String client, String endpointConstant, JsonObject requestPayloadFieldsToModify,Response... previousResponse) throws IOException {
        jsonOperations = new JsonOperations();
        masterDataUtils = new MasterDataUtils();
        apiReportlogging = new APIReportLogging();
        Gson gson = new Gson();
        JsonObject unalteredPayloadBody = jsonOperations.readJsonFileAndStoreInJsonObject(findJsonPayloadFile(client,endpointConstant));
        JsonObject alteredPayload = unalteredPayloadBody;
        for(Map.Entry<String,JsonElement> entry: requestPayloadFieldsToModify.entrySet())
        {
            ReadContext originalJsonMasterPayload = JsonPath.parse(alteredPayload.toString());
            Object value = originalJsonMasterPayload.read("$."+entry.getKey());
            if(value.toString().equalsIgnoreCase("{{static}}")) {
                Object obj = JsonPath.parse(alteredPayload.toString()).set("$."+entry.getKey(),entry.getValue()).json();
                String payloadString = gson.toJson(obj);
                alteredPayload = JsonParser.parseString(payloadString).getAsJsonObject();
            }
            else if(value.toString().equalsIgnoreCase("{{dynamic}}")){
                JsonElement valueToBeModified = previousResponse[0].jsonPath().get(entry.getKey());
                Object obj = JsonPath.parse(alteredPayload.toString()).set("$."+entry.getKey(),valueToBeModified).json();
                String payloadString = gson.toJson(obj);
                alteredPayload = JsonParser.parseString(payloadString).getAsJsonObject();
            }
            else if(value.toString().equalsIgnoreCase("")){
                Object obj = JsonPath.parse(alteredPayload.toString()).set("$."+entry.getKey(),getFrameworkGenertedValues(entry.getKey())).json();
                String payloadString = gson.toJson(obj);
                alteredPayload = JsonParser.parseString(payloadString).getAsJsonObject();
            }
        }
        return alteredPayload;

    }
    public Response IcallExecuteEndpoint(ExtentTest test,Method method , String contentType, String UrlType, JsonObject payload, Map<String ,String> formParams, Map<String ,String> queryParams, Map<String ,String> pathParams , String client, String env, String endpointConstant, int expectedStatusCode , Map<String ,JsonElement> payloadList, Map<String,String>... urlReplacementValues) throws IllegalAccessException, IOException, UnrecoverableKeyException, CertificateException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        apiReportlogging = new APIReportLogging();
        SSLConfig sslConfig = null;
        String payloadString;
        ClientCertificate clientCertificate;
        if(payload!=null)
        {
            payloadString = payload.toString();
            storeTestDataJsonIntoListForDatabaseConsumption(payloadList,payload,getCompleteUrl(env,client,endpointConstant,UrlType,urlReplacementValues));
            System.out.println(AllpayloadUsed1.get());
        }else{
            payloadString = null;
        }
        if(getClientName(client).equalsIgnoreCase("CLIENT002") || getClientName(client).equalsIgnoreCase("CLIENT003")){
            clientCertificate = new ClientCertificate();
            sslConfig = clientCertificate.LoadPfxCertFromLocal(prop.getProperty("ClientCertificateFilePath"),"pass");
        }
        String completeUrl = getCompleteUrl(env,client,endpointConstant,UrlType,urlReplacementValues);
        Map<String, String> headers = HeaderProvider.getProvider().getHeaders(endpointConstant, prop.getProperty("HeadersJsonFile"));
        Response response = RequestProvider.getProvider().request(method,contentType,completeUrl,headers,formParams, payloadString, queryParams, pathParams, sslConfig);
        apiReportlogging.logApiCallDetailsInExtentReport(test,Status.INFO,method,payload,completeUrl,headers,response);
        Assert.assertEquals(response.getStatusCode(),expectedStatusCode);

        return response;
    }
    public Map<String,JsonElement> IcallResponseValidation(ExtentTest test , Response actualResponse, JsonObject expectedResponsePayloadFieldsToValidate,Response... previousResponse) {
        Gson gson = new Gson();
        apiReportlogging = new APIReportLogging();
        Map<String,JsonElement> mismatches = new ConcurrentHashMap<>();
        Map<String,JsonElement> expectedModifiedMap = new ConcurrentHashMap<>();

        if(previousResponse.length>0) {
            for(Map.Entry<String, JsonElement> entry : expectedResponsePayloadFieldsToValidate.entrySet())
            {
                if(entry.getValue().getAsString().startsWith("%")){
                    JsonElement referenceValue = gson.toJsonTree(previousResponse[0].jsonPath().get(entry.getValue().getAsString().substring(1)));
                    expectedModifiedMap.put(entry.getKey(),referenceValue);
                    JsonElement actualValue = gson.toJsonTree(actualResponse.jsonPath().get(entry.getKey()));
                    if(!referenceValue.equals(actualValue))
                    {
                        mismatches.put(entry.getKey(),actualValue);
                    }
                }
                else if(entry.getValue().getAsString().equalsIgnoreCase("not null")){
                    JsonElement actualValue = gson.toJsonTree(actualResponse.jsonPath().get(entry.getKey()));
                    expectedModifiedMap.put(entry.getKey(),entry.getValue());
                    if(actualValue.isJsonNull()){
                        mismatches.put(entry.getKey(),actualValue);
                    }
                }
                else {
                    JsonElement actualValue = gson.toJsonTree(actualResponse.jsonPath().get(entry.getKey()));
                    JsonElement expectedValue = gson.toJsonTree(entry.getValue());
                    expectedModifiedMap.put(entry.getKey(),expectedValue);
                    if(!actualValue.equals(expectedValue)){
                        mismatches.put(entry.getKey(),actualValue);
                    }
                }
            }
        }
        else {
            for(Map.Entry<String, JsonElement> entry : expectedResponsePayloadFieldsToValidate.entrySet())
            {
                if(entry.getValue().getAsString().equalsIgnoreCase("not null")){
                    JsonElement actualValue = actualResponse.jsonPath().get(entry.getKey());
                    expectedModifiedMap.put(entry.getKey(),entry.getValue());
                    if(actualValue.isJsonNull()){
                        mismatches.put(entry.getKey(),actualValue);
                    }
                }
                else {
                    JsonElement actualValue = actualResponse.jsonPath().get(entry.getKey());
                    JsonElement expectedValue = entry.getValue();
                    expectedModifiedMap.put(entry.getKey(),expectedValue);
                    if(!actualValue.equals(expectedValue)){
                        mismatches.put(entry.getKey(),actualValue);
                    }
                }
            }
        }
        if(!mismatches.isEmpty())
        {
            apiReportlogging.logResponseValidationResultsToExtentReport(test,Status.INFO,actualResponse,gson.toJsonTree(expectedModifiedMap).getAsJsonObject());
            Map<String,JsonElement> mismatchedModifiedMap = mismatches.entrySet().stream()
                    .filter(entry->expectedModifiedMap.containsKey(entry.getKey()))
                    .collect(Collectors.toMap(Map.Entry::getKey,Map.Entry::getValue));
            apiReportlogging.logMismatchedValidationResultsToExtentReport(test,Status.FAIL,mismatchedModifiedMap);

        }else{
            apiReportlogging.logResponseValidationResultsToExtentReport(test,Status.INFO,actualResponse,gson.toJsonTree(expectedModifiedMap).getAsJsonObject());
            apiReportlogging.logEndpointSuccesMessageIntoExtentReport(test);
        }
        Assert.assertEquals(mismatches.size(), 0);

        return mismatches;
    }
    public String getCompleteUrl(String env,String client,String endpointConstant,String urlType,Map<String,String>... urlReplacementValues) throws FileNotFoundException {

        String completeUrl = UrlProvider.getProvider().getUrl(env,client,endpointConstant, urlType, prop.getProperty("BaseUrlsJsonFile"),prop.getProperty("EndpointsJsonFile"), prop.getProperty("ApiVersionJsonFile"));
        if(completeUrl.contains("{")|| completeUrl.contains("}") && urlReplacementValues.length>0)
        {
            completeUrl = (completeUrl.contains("{")) ? getUrlReplaced(completeUrl, urlReplacementValues[0]) : completeUrl;
        }
        return completeUrl;
    }
    public String getEnvironmentUrl(String environment){
        switch (environment){
            case "DEV" -> {
                return prop.getProperty("ContactsApplicationDEVUrl");
            }
            case "SIT" -> {
                return prop.getProperty("ContactsApplicationSITUrl");
            }
            case "UAT" -> {
                return prop.getProperty("ContactsApplicationUATUrl");
            }
            case "NFT" -> {
                return prop.getProperty("ContactsApplicationNFTUrl");
            }
            default -> {
                return null;
            }
        }
    }
    public boolean OpenUrl(String url){
        getDriver().manage().window().maximize();
        getDriver().manage().deleteAllCookies();
        getDriver().manage().window().maximize();
        getDriver().manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        getDriver().manage().timeouts().pageLoadTimeout(20,TimeUnit.SECONDS);
        getDriver().get(url);

        return getDriver().getTitle().equalsIgnoreCase(prop.getProperty("ContactsApplicationLandingPageTitle"));
    }
    public boolean OpenApplication(String environment){
        boolean flag=false;
        if(getEnvironmentUrl(environment)!=null )
        {
            if(!OpenUrl(getEnvironmentUrl(environment)))
            {
                throw new IllegalArgumentException("Unable to open application url");
            }else{
                flag=true;
            }
        }
        return flag;
    }
    public JsonObject getPageWiseData(JsonElement testDataSet,String pageIdentifier) {
        masterDataUtils = new MasterDataUtils();
        return masterDataUtils.accessPageWiseWebMobileData(testDataSet, pageIdentifier);
    }
    public String takeScreenshot(WebDriver driver, String screenshotName,String screenshotDirectory ) {
        // Capture the screenshot and define the destination file
        String clickableImage= null;
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String screenshotPath = screenshotDirectory +"/"+ screenshotName + ".png";
        File destinationPath = new File(screenshotPath);
        String filePath = destinationPath.getAbsolutePath();
        String base64Screenshot = null;
        System.out.println("------------ fielPath : "+filePath);
        // Copy the screenshot to the destination file
        try {
            FileUtils.copyFile(screenshot, destinationPath);
            BufferedImage bufferedImage = ImageIO.read(destinationPath);

            // Create a ByteArrayOutputStream to hold the image data
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            // Write the buffered image to the output stream in PNG format
            ImageIO.write(bufferedImage, "png", baos);
            // Get the byte array from the output stream
            byte[] imageBytes = baos.toByteArray();

            // Encode the byte array to Base64
            base64Screenshot = Base64.getEncoder().encodeToString(imageBytes);
            String img = "data:image/png;base64," + base64Screenshot;
            clickableImage = "<a href=\"" + img + "\" target=\"_blank\">"
                    + "<img src=\"" + img + "\" height=\"200\" width=\"200\"/></a>";
            // Use Apache Commons IO to copy file
        } catch (IOException e) {
            e.printStackTrace();
        }

        return clickableImage;
    }

}
