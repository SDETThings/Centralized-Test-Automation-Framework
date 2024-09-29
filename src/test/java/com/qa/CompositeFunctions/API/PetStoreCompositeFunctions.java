package com.qa.CompositeFunctions.API;

import com.aventstack.extentreports.ExtentTest;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.qa.Base.BaseClass;
import com.qa.component.API.ReportLogging.APIReportLogging;
import com.qa.Utils.ReportListeners;
import com.qa.component.API.URIConstruction.Constants;
import dataUtils.MasterDataUtils;
import io.restassured.http.Method;
import io.restassured.response.Response;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Map;

public class PetStoreCompositeFunctions extends BaseClass {
    MasterDataUtils masterDataUtils;
    APIReportLogging apiReportLogging;
    @SuppressWarnings("unchecked")
    public Response AddNewPet(Method method , String testCaseId,String client,String environment,JsonObject testDataSet,String endpointIdentifier,Map<String ,JsonElement> payloadList) throws IOException, IllegalAccessException, UnrecoverableKeyException, CertificateException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        masterDataUtils = new MasterDataUtils();
        apiReportLogging = new APIReportLogging();
        ExtentTest test = ReportListeners.extentTest.get().createNode("<span style='font-weight: bold; color: #00FFFF;'>" + endpointIdentifier + "</span>");
        Response response=null;
        JsonObject requestPayloadFieldsToModify = masterDataUtils.accessApiRequestData(testCaseId, testDataSet, endpointIdentifier);
        JsonObject expectedResponsePayloadFieldsToValidate = masterDataUtils.accessApiResponseData(testCaseId, testDataSet, endpointIdentifier);
        if (requestPayloadFieldsToModify != null || expectedResponsePayloadFieldsToValidate != null) {
            JsonObject requestPayload = IcallBuildRequestPayload(client,endpointIdentifier,requestPayloadFieldsToModify);
            response = IcallExecuteEndpoint(test ,method,Constants.CONTENTTYPE_APPLICATIONJSON,Constants.APIM,requestPayload,null,null,null,client,environment,endpointIdentifier,getExpectedStatusCode(endpointIdentifier),payloadList);
            IcallResponseValidation(test,response, expectedResponsePayloadFieldsToValidate,response);
        }
        return response;
    }
    @SuppressWarnings("unchecked")
    public Response findPet(Method method , String testCaseId,String client,String environment,JsonObject testDataSet,String endpointIdentifier,Map<String ,JsonElement> payloadList,Response addNewPetResponse) throws IOException, IllegalAccessException, UnrecoverableKeyException, CertificateException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        masterDataUtils = new MasterDataUtils();
        ExtentTest test = ReportListeners.extentTest.get().createNode("<span style='font-weight: bold; color: #00FFFF;'>" + endpointIdentifier + "</span>");
        Response response=null;
        Map<String,String> replacementValues =  Map.of("petId", addNewPetResponse.jsonPath().getString("id"));
        JsonObject requestPayloadFieldsToModify = masterDataUtils.accessApiRequestData(testCaseId, testDataSet, endpointIdentifier);
        JsonObject expectedResponsePayloadFieldsToValidate = masterDataUtils.accessApiResponseData(testCaseId, testDataSet, endpointIdentifier);
        if (requestPayloadFieldsToModify != null || expectedResponsePayloadFieldsToValidate != null) {
            response = IcallExecuteEndpoint(test,method, Constants.CONTENTTYPE_APPLICATIONJSON,Constants.APIM,null,null,null,null,client,environment,endpointIdentifier,getExpectedStatusCode(endpointIdentifier),payloadList,replacementValues);
            IcallResponseValidation(test,response, expectedResponsePayloadFieldsToValidate,addNewPetResponse);
        }
        return response;
    }
}
