package com.qa.Step.API;

import DataHandlerUtils.JsonCompareUtils;
import DataHandlerUtils.JsonOperations;
import com.aventstack.extentreports.Status;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.qa.Base.BaseClass;
import com.qa.Utils.APIReportLogging;
import com.qa.component.API.HeaderConstruction.Headers;
import com.qa.component.API.RequestTypes.RestUtils;
import com.qa.component.API.URIConstruction.APIVersions;
import com.qa.component.API.URIConstruction.ApplicationEndpoints;
import com.qa.component.API.URIConstruction.Constants;
import dataUtils.MasterDataUtils;
import io.restassured.http.Method;
import io.restassured.response.Response;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PetStoreTestCaseSteps extends BaseClass {
    MasterDataUtils masterDataUtils;
    APIReportLogging apiReportLogging;
    ApplicationEndpoints endpoints;
    APIVersions apiVersions;
    Headers headers;
    RestUtils restUtils;
    Map<String, JsonElement> frameworkEntries;
    JsonOperations jsonOperations;
    Map<String ,String> header;
    JsonCompareUtils jsonCompareUtils;

    public JsonObject IcallBuildRequestPayload(String client, String endpointConstant, JsonObject requestPayloadFieldsToModify) throws IOException {
        jsonOperations = new JsonOperations();
        masterDataUtils = new MasterDataUtils();
        frameworkEntries = new ConcurrentHashMap<>();
        JsonObject unalteredPayloadBody = jsonOperations.readJsonFileAndStoreInJsonObject(findJsonPayloadFile(client,endpointConstant));
        frameworkEntries.putAll(buildFrameworkMap(getKeysTobePopulatedThroughFramework(requestPayloadFieldsToModify)));
        if(requestPayloadFieldsToModify.size()>0)
        {
            unalteredPayloadBody = masterDataUtils.readRequestPayloadDynamicFieldsAndMerge(unalteredPayloadBody,requestPayloadFieldsToModify,frameworkEntries);
        }
        else{
            return unalteredPayloadBody;
        }
        return unalteredPayloadBody;
    }
    public Response IcallExecuteEndpoint(Method method ,JsonObject payload, String client, String env, String endpointConstant,Map<String,String> endpointReplacements,String... token) throws IllegalAccessException, IOException {
        apiReportLogging = new APIReportLogging();
        endpoints = new ApplicationEndpoints();
        restUtils = new RestUtils();
        apiVersions = new APIVersions();
        headers = new Headers();
        header = new ConcurrentHashMap<>();
        Response response;
        String BaseUrl,apiVersion,endpoint,completeURL,payloadString;
        if(!payload.isEmpty())
        {
            payloadString = payload.toString();
        }else{
            payloadString = null;
        }
        BaseUrl = getEnvironmentBaseURL(env,client, Constants.APIM,endpointConstant);
        apiVersion = apiVersions.getApiVersion(client,env,Constants.APIM,endpointConstant);
        endpoint = modifyEndpoint(retrieveApplicationEndpoint(endpointConstant),endpointReplacements);
        if(apiVersion.equalsIgnoreCase("")) {
            completeURL = BaseUrl+ endpoint;
        }
        else{
            completeURL = BaseUrl +apiVersion+ endpoint;
        }
        if(token.length>0)
        {
            header = headers.returnHeaders(client,token[0]);
        }else{
            header = headers.returnHeaders(client);
        }
        response = restUtils.requestAsync(method,Constants.CONTENTTYPE_APPLICATIONJSON,completeURL, header,null,payloadString,null,null,null);;
        apiReportLogging.logResponseIntoExtentReport(Status.INFO,completeURL,response,"Response for API call:");

        return response;
    }
    public Map<String,JsonElement> IcallResponseValidation(Response response, JsonObject expectedResponsePayloadFildsToValidate)
    {
        masterDataUtils = new MasterDataUtils();
        jsonOperations = new JsonOperations();
        jsonCompareUtils = new JsonCompareUtils();
        apiReportLogging = new APIReportLogging();
        JsonElement actualResponse = jsonOperations.convertResponseToJsonElement(response);
        JsonObject modifiedExectedResponse = masterDataUtils.modifyExpectedResponseBeforeComparing(actualResponse,expectedResponsePayloadFildsToValidate);
        Map<String,JsonElement> mismatches = jsonCompareUtils.comapreJson(modifiedExectedResponse,actualResponse);
        apiReportLogging.logComaprisonDataIntoExtentReport(Status.INFO,actualResponse,expectedResponsePayloadFildsToValidate);

        return mismatches;
    }
}
