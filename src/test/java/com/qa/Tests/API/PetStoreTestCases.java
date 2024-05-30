package com.qa.Tests.API;

import com.qa.Base.BaseClass;
import com.qa.CompositeFunctions.API.PetStoreCompositeFunctions;
import com.qa.Utils.APIReportLogging;
import com.qa.Utils.EnvDataProvider;
import BasicUtils.CommonMethods;
import com.qa.Step.API.PetStoreTestCaseSteps;
import com.aventstack.extentreports.Status;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.qa.component.API.URIConstruction.ApplicationEndpoints;
import com.qa.component.API.URIConstruction.Constants;
import DataHandlerUtils.JsonCompareUtils;
import dataUtils.MasterDataUtils;
import fileUtils.FileUtils;
import io.restassured.response.Response;
import DataHandlerUtils.JsonOperations;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.text.ParseException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PetStoreTestCases {

    public class AcquisitionApiTest extends BaseClass {
        PetStoreTestCaseSteps apiTestCaseSteps ;
        CommonMethods commonMethods ;
        JsonOperations jsonOperations ;
        MasterDataUtils masterDataUtils ;
        Map<String ,JsonElement> payloadList;
        JsonCompareUtils jsonCompareUtils;
        private ThreadLocal<Map<String,JsonObject>> storeTestData = new ThreadLocal<>();
        APIReportLogging apiReportLogging;
        ApplicationEndpoints endpoints;
        PetStoreCompositeFunctions compositeFunctions;

        @BeforeClass
        public void PreRequisites() throws IOException {
            setProperties();
            readJSONDriver(prop.getProperty("testDataDriverJson"));
        }
        // Parameterizing test case run configuration metadata
        @Test(description = "place order and get order in petstore application with test case metadata as user input ",dataProvider = "getRunDetails_TC0001" ,dataProviderClass = EnvDataProvider.class,enabled=false)
        public synchronized void TC0001(String testCase , String runEnv,String client) throws IOException, IllegalAccessException {
            compositeFunctions = new PetStoreCompositeFunctions();
            apiReportLogging = new APIReportLogging();
            apiTestCaseSteps = new PetStoreTestCaseSteps();
            jsonOperations = new JsonOperations();
            jsonCompareUtils = new JsonCompareUtils();
            commonMethods = new CommonMethods();
            masterDataUtils = new MasterDataUtils();
            payloadList = new ConcurrentHashMap<>();
            endpoints = new ApplicationEndpoints();
            setResults(runEnv,client);
            //Read master data from client json file
            JsonObject completeMasterData = jsonOperations.readJsonFileAndStoreInJsonObject(getMasterDataJsonPath(client));
            Response getAuthTokenResponse = compositeFunctions.getRestfullBookerAuthenticationToken(testCase,client,runEnv,completeMasterData,Constants.GET_RESTFULL_BOOKER_AUTH_TOKEN,payloadList);
            Response placePetOrderResponse = compositeFunctions.placePetOrder(testCase,client,runEnv,completeMasterData,Constants.PETSTORE_PLACE_ORDER,payloadList,getAuthTokenResponse.jsonPath().get("token").toString());
            Response getOrderResponse = compositeFunctions.getPetOrder(testCase,client,runEnv,completeMasterData,Constants.PETSTORE_GET_ORDER,payloadList,getAuthTokenResponse.jsonPath().get("token").toString());
        }
        @Test(description = "place order and get order in petstore application with multiple test case dataset as user input ",dataProvider = "getRunDetails_TC0002" ,dataProviderClass = EnvDataProvider.class,enabled=false)
        public synchronized void TC0002(JsonElement testDataSets) throws IOException, IllegalAccessException {
            String testCase = getTestCaseMetaDataBlock(Reporter.getCurrentTestResult().getMethod().getMethodName()).get(Constants.TESTCASE_FIELD).getAsString();
            String runEnv = getTestCaseMetaDataBlock(Reporter.getCurrentTestResult().getMethod().getMethodName()).get(Constants.ENVIRONMENT_FIELD).getAsString();
            String client = getTestCaseMetaDataBlock(Reporter.getCurrentTestResult().getMethod().getMethodName()).get(Constants.CLIENT_FIELD).getAsString();
            compositeFunctions = new PetStoreCompositeFunctions();
            apiReportLogging = new APIReportLogging();
            apiTestCaseSteps = new PetStoreTestCaseSteps();
            jsonOperations = new JsonOperations();
            jsonCompareUtils = new JsonCompareUtils();
            commonMethods = new CommonMethods();
            masterDataUtils = new MasterDataUtils();
            payloadList = new ConcurrentHashMap<>();
            endpoints = new ApplicationEndpoints();
            setResults(runEnv,client);
            //Read master data from client json file
            JsonObject completeMasterData = jsonOperations.readJsonFileAndStoreInJsonObject(getMasterDataJsonPath(client));
            Response getAuthTokenResponse = compositeFunctions.getRestfullBookerAuthenticationToken(testCase,client,runEnv,completeMasterData,Constants.GET_RESTFULL_BOOKER_AUTH_TOKEN,payloadList);
            Response placePetOrderResponse = compositeFunctions.placePetOrder(testCase,client,runEnv,completeMasterData,Constants.PETSTORE_PLACE_ORDER,payloadList,getAuthTokenResponse.jsonPath().get("token").toString());
            Response getOrderResponse = compositeFunctions.getPetOrder(testCase,client,runEnv,completeMasterData,Constants.PETSTORE_GET_ORDER,payloadList,getAuthTokenResponse.jsonPath().get("token").toString());
        }

        //@AfterMethod()
        public void afterTest(ITestResult iTestResult) throws ParseException, IOException,
                InterruptedException {
            int invocationCount = iTestResult.getMethod().getCurrentInvocationCount();
            if(iTestResult.getStatus()==ITestResult.SUCCESS)
            {
                afterTestApiActivities(iTestResult);
            }else if(iTestResult.getStatus()==ITestResult.FAILURE ){
                afterTestApiActivities(iTestResult);
            }
        }
    }
}
