package com.qa.Utils;
import com.qa.Base.BaseClass;
import com.aventstack.extentreports.Status;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.restassured.response.Response;
import DataHandlerUtils.JsonOperations;
public class APIReportLogging extends BaseClass {
    Gson gson ;
    JsonOperations jsonOperations;
    public synchronized void logRequestPayloadIntoExtentReport(Status status ,String completeUrl, JsonObject payloadBody , String message) {
        gson = new GsonBuilder().setPrettyPrinting().create();
        ReportListeners.extentTest.get().log(status, message+" :\n<pre style ='color: green;'>"
                + completeUrl + " : \n"+ gson.toJson(payloadBody) + "\n</pre>");
    }
    public synchronized void logResponseIntoExtentReport(Status status , String completeUrl, Response response , String message) {
        jsonOperations =new JsonOperations();
        gson = new GsonBuilder().setPrettyPrinting().create();
        if(response.getBody().asString().startsWith("["))
        {
            ReportListeners.extentTest.get().log(status, message + "\n" + " :\n<pre style ='color:green;'> \n" + completeUrl + ": \n" +
            gson.toJson(jsonOperations.convertResponseToJsonArray(response)) + "\n</pre>");
        }else
        {
            ReportListeners.extentTest.get().log(status, message + "\n" + " :\n<pre style ='color:green;'> \n" + completeUrl + ": \n" +
            gson.toJson(jsonOperations.convertResponseToJsonObject(response)) + "\n</pre>");
        }
    }
    public synchronized void logComaprisonDataIntoExtentReport(Status status , JsonObject actualResponse ,JsonObject modifiedExpectedResponse) {
        gson = new GsonBuilder().setPrettyPrinting().create();
        ReportListeners.extentTest.get().log(status, "Comparison of actual response :\n<pre style ='color: green;'>\n"+ gson.toJson(actualResponse) + "\n</pre>" + " with expected " + "\n<pre style ='color: green;'>\n "+ gson.toJson(modifiedExpectedResponse) + "\n</pre>" );
    }
    public synchronized void logComaprisonDataIntoExtentReport(Status status , JsonElement actualResponse , JsonObject modifiedExpectedResponse) {
        gson = new GsonBuilder().setPrettyPrinting().create();
        ReportListeners.extentTest.get().log(status, "Comparison of actual response :\n<pre style ='color: green;'>\n"+ gson.toJson(actualResponse) + "\n</pre>" + " with expected " + "\n<pre style ='color: green;'>\n "+ gson.toJson(modifiedExpectedResponse) + "\n</pre>" );
    }
    public synchronized void logStatusCodeIntoExtentReport(Status status , String message,int statusCode) {
        gson = new GsonBuilder().setPrettyPrinting().create();
        ReportListeners.extentTest.get().log(status, message + statusCode);
    }
    public synchronized void logMessageStringIntoExtentReport(Status status , String message) {
        gson = new GsonBuilder().setPrettyPrinting().create();
        ReportListeners.extentTest.get().log(status, message);
    }
    public synchronized void logStartOfEndpointExecutionIntoExtentReport(String URI) {
        gson = new GsonBuilder().setPrettyPrinting().create();
        ReportListeners.extentTest.get().log(Status.INFO, "<mark style='background-color: #CCCC00; color: black;'><strong>Execution started for URI : "+ URI +"</strong></mark>");
    }
    public synchronized void logStartOfEndpointMessageStringIntoExtentReport(Status status , String message) {
        gson = new GsonBuilder().setPrettyPrinting().create();
        ReportListeners.extentTest.get().log(status,
                "*********************\n"+message+"\n*********************");
    }
}