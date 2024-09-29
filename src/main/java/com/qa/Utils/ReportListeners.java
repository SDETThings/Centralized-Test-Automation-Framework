package com.qa.Utils;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.markuputils.Markup;
import com.google.gson.JsonElement;
import com.qa.Base.BaseClass;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fileUtils.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.json.Json;
import org.testng.*;
import ReportUtils.ExtentReport;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
public class ReportListeners extends BaseClass implements ITestListener , IInvokedMethodListener {
    public static ExtentTest test;
    // ExtentReports extent;
    private static final Logger log = LogManager.getLogger(ReportListeners.class);
    public static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
    public static ThreadLocal<String> currentTestResultsFolder = new ThreadLocal<>();
    public static ThreadLocal<ExtentReports> extent = new ThreadLocal<>();
    public Map<String, Boolean> testCaseConfig;
    ExtentReport extentReport = new ExtentReport();
    FileUtils fileUtils = new FileUtils();
    ExtentReport exReport = new ExtentReport();
    @Override
    public synchronized void onStart(ITestContext iTestContext) {
    }
   @Override
    public synchronized void onTestStart(ITestResult iTestResult) {
       String testName = iTestResult.getName();
       JsonElement dataProvider = (JsonElement) iTestResult.getParameters()[0];
       String fileName = extentReport.getReportNameWithTimeStamp(testName,"yyyyMMddHHmmss");
       String fullReportPath = fileUtils.createTestCaseResultsFolder(prop.getProperty("TestCaseResultPath"), testName, "yyyyMMddHHmmss");
       currentTestResultsFolder.set(fullReportPath);
        extent.set( exReport.BuildExtentReport(currentTestResultsFolder.get()+"/"+fileName,"GENESIS_AUTOMATION","GENESIS_AUTOMATION_REPORT","dd/MM/yyyy hh:mm:ss"));
        ExtentTest test = extent.get().createTest(testName,"<b>Description</b> : "+"<b><font color='yellow'>"+dataProvider.getAsJsonObject().get("DESCRIPTION").getAsString()+"</font></b>");
        extentTest.set(test);
       // extentTest.get().log(Status.INFO, testName +" : "+"|| started for iteration - "+ dataProvider.getAsJsonObject().get("ITERATION_NUMBER").getAsString());
        log.info(testName + " : STARTED");
    }
    public synchronized static String getCurrentTestResultsFolder()
    {
        return currentTestResultsFolder.get();
    }
    @Override
    public void onTestSuccess(ITestResult iTestResult) {
        try {
            String testName = iTestResult.getName();
            //extentTest.get().log(Status.PASS, testName + " passed");
            //String resultsFolder = iTestResult.getAttribute("testCaseFolder").toString();
            String resultsFolder ="./src/test/resources/TestCaseResults/";
            extent.get().flush();
            extent.remove();
            extentTest.remove();
            currentTestResultsFolder.remove();
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public synchronized void onTestFailure(ITestResult iTestResult) {
        String testName = iTestResult.getName();
        JsonObject logPayload;
       /* if(iTestResult.getAttribute("documentId")!=null && iTestResult.getAttribute("uniqueIdentifier")!=null ) {
            try {
                logPayload = createRequestPayloadForLogEntry(iTestResult.getAttribute("documentId").toString(), iTestResult.getAttribute("uniqueIdentifier").toString());
                //sendLogInformation(logPayload);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }*/
        // Capture screenshot on failure
        String screenshotPath = takeScreenshot(getDriver(), iTestResult.getName(),currentTestResultsFolder.get());
        // Attach the screenshot to the Extent Report
        extentTest.get().log(Status.FAIL, "Test Failed: " + iTestResult.getName() + " - Screenshot: " + screenshotPath);
        //ReportListeners.extentTest.get().log(Status.FAIL, (Markup) extentTest.get().addScreenCaptureFromPath(screenshotPath));
        extent.get().flush();
        extent.remove();
        extentTest.remove();
        currentTestResultsFolder.remove();
    }
    @Override
    public synchronized void onTestSkipped(ITestResult iTestResult) {
        String testName = iTestResult.getName();
        ReportListeners.extentTest.get().log(Status.INFO, iTestResult.getThrowable());
        ReportListeners.extentTest.get().log(Status.SKIP, testName + " got skipped");
        extent.get().flush();
    }
    @Override
    public synchronized void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
    }
    @Override
    public synchronized void onFinish(ITestContext iTestContext) {
    }
    @Override
    public synchronized void beforeInvocation(IInvokedMethod iInvokedMethod, ITestResult iTestResult) {
    }
    @Override
    public synchronized void afterInvocation(IInvokedMethod iInvokedMethod, ITestResult iTestResult) {
    }
}
