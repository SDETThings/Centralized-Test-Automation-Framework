package com.qa.CompositeFunctions.API;

import com.aventstack.extentreports.Status;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.qa.Base.BaseClass;
import com.qa.Step.API.PetStoreTestCaseSteps;
import com.qa.Utils.APIReportLogging;
import com.qa.Utils.FrameworkComponentKeys;
import com.qa.component.API.URIConstruction.ApplicationEndpoints;
import com.qa.component.API.URIConstruction.Constants;
import dataUtils.MasterDataUtils;
import io.restassured.http.Method;
import io.restassured.response.Response;
import org.testng.Assert;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PetStoreCompositeFunctions extends BaseClass {
    MasterDataUtils masterDataUtils;
    PetStoreTestCaseSteps petStoreTestCaseSteps;
    APIReportLogging apiReportLogging;
    ApplicationEndpoints endpoints;
    Map<String ,String> endpointReplacements;
    public synchronized Response getRestfullBookerAuthenticationToken(String testCaseId,String client,String env,JsonObject completeMasterData,String endpointConstant,Map<String,JsonElement> payloadList) throws IOException, IllegalAccessException {
        masterDataUtils = new MasterDataUtils();
        endpointReplacements = new ConcurrentHashMap<>();
        Response response=null;
        //Read master data for request payload and response body
        JsonObject requestPayloadFieldsToModify = masterDataUtils.accessApiRequestData(testCaseId, FrameworkComponentKeys.API.getComponentAsString(), completeMasterData, endpointConstant);
        JsonObject expectedResponsePayloadFieldsToValidate = masterDataUtils.accessApiResponsetData(testCaseId, FrameworkComponentKeys.API.getComponentAsString(), completeMasterData, endpointConstant);
        if (requestPayloadFieldsToModify != null && expectedResponsePayloadFieldsToValidate != null) {
            apiReportLogging = new APIReportLogging();
            apiReportLogging.logStartOfEndpointExecutionIntoExtentReport(getEnvironmentBaseURL(env,client,Constants.APIM,endpointConstant) + retrieveApplicationEndpoint(Constants.GET_RESTFULL_BOOKER_AUTH_TOKEN));
            endpoints = new ApplicationEndpoints();
            petStoreTestCaseSteps = new PetStoreTestCaseSteps();
            JsonObject requestPayload = petStoreTestCaseSteps.IcallBuildRequestPayload(client,endpointConstant,requestPayloadFieldsToModify);
            storeTestDataJsonIntoListForDatabaseConsumption(payloadList,requestPayload, getEnvironmentBaseURL(env,client,Constants.APIM,endpointConstant) + retrieveApplicationEndpoint(Constants.GET_RESTFULL_BOOKER_AUTH_TOKEN));
            response = petStoreTestCaseSteps.IcallExecuteEndpoint(Method.POST,requestPayload,client,env,endpointConstant,endpointReplacements);
            Assert.assertEquals(response.getStatusCode(), 200);
            apiReportLogging.logStatusCodeIntoExtentReport(Status.PASS, "Response status code for API call is as expected :", response.getStatusCode());
            Map<String, JsonElement> mismatches =petStoreTestCaseSteps.IcallResponseValidation(response, expectedResponsePayloadFieldsToValidate);
            Assert.assertEquals(mismatches.size(), 0);
            apiReportLogging.logMessageStringIntoExtentReport(Status.PASS, "There is no mismatch between expected and actual json response for endpoint");
        }
        return response;
    }
    public synchronized Response placePetOrder(String testCaseId,String client,String env,JsonObject completeMasterData,String endpointConstant,Map<String,JsonElement> payloadList,String authToken) throws IOException, IllegalAccessException {
        masterDataUtils = new MasterDataUtils();
        endpointReplacements = new ConcurrentHashMap<>();
        Response response=null;
        //Read master data for request payload and response body
        JsonObject requestPayloadFieldsToModify = masterDataUtils.accessApiRequestData(testCaseId, FrameworkComponentKeys.API.getComponentAsString(), completeMasterData, endpointConstant);
        JsonObject expectedResponsePayloadFieldsToValidate = masterDataUtils.accessApiResponsetData(testCaseId, FrameworkComponentKeys.API.getComponentAsString(), completeMasterData, endpointConstant);
        if (requestPayloadFieldsToModify != null && expectedResponsePayloadFieldsToValidate != null) {
            apiReportLogging = new APIReportLogging();
            apiReportLogging.logStartOfEndpointExecutionIntoExtentReport(getEnvironmentBaseURL(env,client,Constants.APIM,endpointConstant) + retrieveApplicationEndpoint(endpointConstant));
            endpoints = new ApplicationEndpoints();
            petStoreTestCaseSteps = new PetStoreTestCaseSteps();
            JsonObject requestPayload = petStoreTestCaseSteps.IcallBuildRequestPayload(client,endpointConstant,requestPayloadFieldsToModify);
            storeTestDataJsonIntoListForDatabaseConsumption(payloadList,requestPayload, getEnvironmentBaseURL(env,client,Constants.APIM,endpointConstant) + retrieveApplicationEndpoint(endpointConstant));
            response = petStoreTestCaseSteps.IcallExecuteEndpoint(Method.POST,requestPayload,client,env,endpointConstant,endpointReplacements,authToken);
            Assert.assertEquals(response.getStatusCode(), 200);
            apiReportLogging.logStatusCodeIntoExtentReport(Status.PASS, "Response status code for API call is as expected :", response.getStatusCode());
            Map<String, JsonElement> mismatches =petStoreTestCaseSteps.IcallResponseValidation(response, expectedResponsePayloadFieldsToValidate);
            Assert.assertEquals(mismatches.size(), 0);
            apiReportLogging.logMessageStringIntoExtentReport(Status.PASS, "There is no mismatch between expected and actual json response for endpoint");
        }
        return response;
    }
    public synchronized Response getPetOrder(String testCaseId,String client,String env,JsonObject completeMasterData,String endpointConstant,Map<String,JsonElement> payloadList,String authToken) throws IOException, IllegalAccessException {
        masterDataUtils = new MasterDataUtils();
        endpointReplacements = new ConcurrentHashMap<>();
        Response response=null;
        //Read master data for request payload and response body
        JsonObject requestPayloadFieldsToModify = masterDataUtils.accessApiRequestData(testCaseId, FrameworkComponentKeys.API.getComponentAsString(), completeMasterData, endpointConstant);
        JsonObject expectedResponsePayloadFieldsToValidate = masterDataUtils.accessApiResponsetData(testCaseId, FrameworkComponentKeys.API.getComponentAsString(), completeMasterData, endpointConstant);
        if (requestPayloadFieldsToModify != null && expectedResponsePayloadFieldsToValidate != null) {
            apiReportLogging = new APIReportLogging();
            apiReportLogging.logStartOfEndpointExecutionIntoExtentReport(getEnvironmentBaseURL(env,client,Constants.APIM,endpointConstant) + retrieveApplicationEndpoint(endpointConstant));
            endpoints = new ApplicationEndpoints();
            petStoreTestCaseSteps = new PetStoreTestCaseSteps();
            if(retrieveApplicationEndpoint(endpointConstant).contains("{"))
            {
                endpointReplacements.putIfAbsent("orderId","5");
            }
            response = petStoreTestCaseSteps.IcallExecuteEndpoint(Method.GET,requestPayloadFieldsToModify,client,env,endpointConstant,endpointReplacements,authToken);
            Assert.assertEquals(response.getStatusCode(), 200);
            apiReportLogging.logStatusCodeIntoExtentReport(Status.PASS, "Response status code for API call is as expected :", response.getStatusCode());
            Map<String, JsonElement> mismatches =petStoreTestCaseSteps.IcallResponseValidation(response, expectedResponsePayloadFieldsToValidate);
            Assert.assertEquals(mismatches.size(), 0);
            apiReportLogging.logMessageStringIntoExtentReport(Status.PASS, "There is no mismatch between expected and actual json response for endpoint");
        }
        return response;
    }

}
