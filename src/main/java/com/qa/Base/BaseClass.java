package com.qa.Base;

import com.qa.Utils.APIReportLogging;
import com.qa.Utils.PdfUtils;
import com.qa.Utils.ReportListeners;
import com.qa.Utils.TestListener;
import com.aventstack.extentreports.Status;
import com.google.gson.*;
import com.qa.JsonModellers.TestDataDriver;
import com.qa.component.API.HeaderConstruction.Headers;
import com.qa.component.API.URIConstruction.APIVersions;
import com.qa.component.API.URIConstruction.ApplicationEndpoints;
import com.qa.component.API.URIConstruction.EnvironmentBaseUrls;
import com.qa.component.API.URIConstruction.Constants;
import com.qa.component.API.RequestTypes.RestUtils;
import DataHandlerUtils.JsonCompareUtils;
import com.qa.component.WEB.DriverActionables.DriverActions;
import com.qa.component.WEB.PageConstants.PageConstants;
import com.qa.component.WEB.Urls.WebUrls;
import fileUtils.FileUtils;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.bonigarcia.wdm.config.DriverManagerType;
import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import DataHandlerUtils.JsonOperations;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.json.Json;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.Reporter;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
public class BaseClass {
    public static ThreadLocal<WebDriver> tl = new ThreadLocal<>();
    APIReportLogging apiReportlogging;
    public static TestDataDriver testDataDriver;
    public static Properties prop;
    public ThreadLocal<List<JsonObject>> AllpayloadUsed = new ThreadLocal<>();
    public ThreadLocal<Map<String ,JsonElement>> AllpayloadUsed1 = new ThreadLocal<>();
    public ThreadLocal<WebDriver> LocalWebDriver= new ThreadLocal<>();
    public ThreadLocal<RemoteWebDriver> LocalRemoteWebDriver= new ThreadLocal<>();
    public ThreadLocal<AndroidDriver> LocalAndroidDriver= new ThreadLocal<>();
    public ThreadLocal<IOSDriver> LocalIOSDriver= new ThreadLocal<>();
    JsonOperations jsonOperations= new JsonOperations();
    public ThreadLocal<Integer> TestIterationCount= new ThreadLocal<>();


    public synchronized String generateRandomName() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int) (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        String generatedString = buffer.toString();
        return generatedString;
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
    public synchronized String generateRandomDateOfBirthBetween(String dateStart,String dateEnd ) {
        LocalDate startDate = LocalDate.of(Integer.parseInt(dateStart.split("./")[0]),Integer.parseInt(dateStart.split("./")[1]),Integer.parseInt(dateStart.split("./")[2]));
        LocalDate endDate = LocalDate.of(Integer.parseInt(dateEnd.split("./")[0]),Integer.parseInt(dateEnd.split("./")[1]),Integer.parseInt(dateEnd.split("./")[2]));
        long statEpochDay = startDate.toEpochDay();
        long endEpochDay = endDate.toEpochDay();
        long randomEpochDay = ThreadLocalRandom.current().nextLong(statEpochDay,endEpochDay+1);
        LocalDate randomeDate = LocalDate.ofEpochDay(randomEpochDay);
        return randomeDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
    public synchronized void readJSONDriver(String JsonDriverPath) throws IOException {
        Gson gson = new Gson();
        FileReader reader = new FileReader(JsonDriverPath);
        testDataDriver = gson.fromJson(reader, TestDataDriver.class);
    }
    public synchronized TestDataDriver getRunConfigurations() {
        return testDataDriver;
    }
    public static void setProperties() {
        try {
            FileInputStream fis = new FileInputStream("./Config.properties");
            prop = new Properties();
            prop.load(fis);
        } catch (IOException var2) {
            var2.printStackTrace();
        }
    }
    public synchronized void setResults(String env, String client) {
        String testResultsFolder = ReportListeners.getCurrentTestResultsFolder();
        File file = new File(testResultsFolder);
        ITestResult result = Reporter.getCurrentTestResult();
        result.setAttribute("env",env);
        result.setAttribute("client",client);
        result.setAttribute("documentId",file.getName());
        result.setAttribute("testCaseFolder",testResultsFolder);
    }
    public synchronized void setResults(String env, String client,String uniqueIdentifier) {
        String testResultsFolder = ReportListeners.getCurrentTestResultsFolder();
        File file = new File(testResultsFolder);
        ITestResult result = Reporter.getCurrentTestResult();
        result.setAttribute("env",env);
        result.setAttribute("client",client);
        result.setAttribute("documentId",file.getName());
        result.setAttribute("testCaseFolder",testResultsFolder);
        result.setAttribute("uniqueIdentifier",uniqueIdentifier);
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
    public synchronized void
    storeTestDataJsonIntoListForDatabaseConsumption(Map<String,JsonElement> payloadList , JsonElement jsonObject,String url) {
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
                apiReportlogging.logMessageStringIntoExtentReport(Status.WARNING,"Unable to find the request payload folder for client : ||"+ client + "|| Please add the new request payload folder for the client or correct existing client id ");
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
                apiReportlogging.logMessageStringIntoExtentReport(Status.WARNING,"Client Name not found for client id || " + clientId + " ||");
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
                apiReportlogging.logMessageStringIntoExtentReport(Status.WARNING,"Master data sheet for the client : ||"+ client + "|| does not exist, Please add a new client or correct existing client id");
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
        if(endpointName.contains(Constants.GET_RESTFULL_BOOKER_AUTH_TOKEN))
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
                                apiReportlogging.logMessageStringIntoExtentReport(Status.WARNING, "Environment base url is not found for environment : ||" + env + "|| Please add the new environment or correct environment");
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
    public synchronized Response UploadEvidenceAPIRequest(String filePath) {
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
    }
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
    public synchronized Response sendAPIExecutionDetails(ITestResult iTestResult) throws ParseException, IOException {
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
/*if(prop.getProperty("RunOn").equalsIgnoreCase("Local"))
{
String runOn = "Local";
}*/
        String env = iTestResult.getAttribute("env").toString();
        JsonObject databasePayload =
                createRequestPayloadForDatabaseExecutionEntry( testCaseId,result,testData,excecutionDateTime,
                        documentId,executionLog,executionLink,client,env,browserOrOsVersion);
        Response response = hitExecutionResultsDatabaseAPI(databasePayload);
        return response;
    }
    public synchronized Response sendExecutionDetails(ITestResult iTestResult,String link, String TestData,String BrowserDetails) throws ParseException, IOException {
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
    }
    public synchronized Response sendHybridExecutionDetails(ITestResult iTestResult) throws ParseException, IOException {
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
    }
    public synchronized Response hitExecutionResultsDatabaseAPI(JsonObject jsonObject) {
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
    }
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
    public synchronized Response sendLogInformation(JsonObject jsonObject) {
        RestUtils restUtils = new RestUtils();
        String payloadBody = jsonObject.toString();
        String BaseUrl = prop.getProperty("GenesisBaseURL");
        String endpoint = "v1/getAppInsightLogAnalysisFramework";
        String completeRequestURIString = BaseUrl + endpoint;
        Response response = restUtils.requestAsync(Method.POST, Constants.CONTENTTYPE_APPLICATIONJSON,completeRequestURIString,null,null,payloadBody,null,null,null);
        return response;
    }
    public void setUniqueIdentifier(Response response,String whatKeyValueToSend, String env,String client) {
        if(response.jsonPath().get(whatKeyValueToSend)!=null)
        {
            setResults(env, client,response.jsonPath().get("id"));
        }
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
    public synchronized void afterTestApiActivities(ITestResult iTestResult) throws ParseException, IOException, InterruptedException {
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
    }
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
    public synchronized String getCurrentDate() {
        // Get the current date
        LocalDate currentDate = LocalDate.now();
        // Create a formatter for the desired format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // Format the current date
        String formattedDate = currentDate.format(formatter);

        return formattedDate;
    }
    public void launchAppliationURL(String browserName,String applicationUrl) {
        WebDriverManager.chromedriver().clearDriverCache().setup();
        if (browserName.equalsIgnoreCase("chrome")) {
           // WebDriverManager.chromedriver().clearDriverCache().setup();
            WebDriverManager.chromedriver().setup();
            tl.set(new ChromeDriver());
        }
        if (browserName.equalsIgnoreCase("firefox")) {
            WebDriverManager.firefoxdriver().setup();
            tl.set(new FirefoxDriver());
        }
        if (browserName.equalsIgnoreCase("IE")) {
            WebDriverManager.iedriver().setup();
            tl.set(new InternetExplorerDriver());
        }
        if (browserName.equalsIgnoreCase("Edge")) {
            WebDriverManager.edgedriver().setup();
            tl.set(new EdgeDriver());
        }
        getDriver().manage().deleteAllCookies();
        getDriver().manage().window().maximize();
        // browser is launched
        DriverActions.implicitWait(getDriver(), 10);
        DriverActions.pageLoadTimeOut(getDriver(), 20);
        // launch the application URL
        getDriver().get(applicationUrl);
    }
    public static synchronized WebDriver getDriver() {
        WebDriver tlDriver = tl.get();
        return tlDriver;
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
}
