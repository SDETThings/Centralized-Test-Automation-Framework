package com.qa.Tests.WEB;

import BasicUtils.CommonMethods;
import DataHandlerUtils.JsonCompareUtils;
import DataHandlerUtils.JsonOperations;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.qa.Base.BaseClass;
import com.qa.CompositeFunctions.API.PetStoreCompositeFunctions;
import com.qa.CompositeFunctions.WEB.ContactListCompositeFunctions;
import com.qa.Step.API.PetStoreTestCaseSteps;
import com.qa.Utils.APIReportLogging;
import com.qa.Utils.EnvDataProvider;
import com.qa.Utils.FrameworkComponentKeys;
import com.qa.component.API.URIConstruction.ApplicationEndpoints;
import com.qa.component.API.URIConstruction.Constants;
import com.qa.component.WEB.PageConstants.PageConstants;
import dataUtils.MasterDataUtils;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.*;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class ContactListTestCases extends BaseClass {
    ContactListCompositeFunctions contactListCompositeFunctions;
    APIReportLogging apiReportLogging;
    CommonMethods commonMethods;
    JsonOperations jsonOperations;
    MasterDataUtils masterDataUtils;

    @BeforeClass
    public void PreRequisites() throws IOException {
        setProperties();
        readJSONDriver(prop.getProperty("testDataDriverJson"));
    }
    @Parameters({"browser","url"})
    @BeforeMethod()
    public void setup(String browser,String url) {
        launchAppliationURL(browser,url);
    }
    // Parameterizing test case run configuration metadata
    @Test(description = "Login to Create contact website and create a contact",dataProvider = "getRunDetails_TC0003" ,dataProviderClass = EnvDataProvider.class,enabled=false)
    public synchronized void TC0003(JsonElement testData) throws IOException, IllegalAccessException, InterruptedException {
        String testCase = getTestCaseMetaDataBlock(Reporter.getCurrentTestResult().getMethod().getMethodName()).get(Constants.TESTCASE_FIELD).getAsString();
        String runEnv = getTestCaseMetaDataBlock(Reporter.getCurrentTestResult().getMethod().getMethodName()).get(Constants.ENVIRONMENT_FIELD).getAsString();
        String client = getTestCaseMetaDataBlock(Reporter.getCurrentTestResult().getMethod().getMethodName()).get(Constants.CLIENT_FIELD).getAsString();
        setResults(runEnv,client);
        contactListCompositeFunctions = new ContactListCompositeFunctions();
        masterDataUtils = new MasterDataUtils();
        //Read master data from client json file
        JsonArray completeTestCaseData = masterDataUtils.accessWebMobileMasterData(testData.getAsJsonObject());
        Assert.assertEquals(contactListCompositeFunctions.validatePageTitle(),"Contact List App");
        contactListCompositeFunctions.SignUpAsNewUser(completeTestCaseData );
    }

}
