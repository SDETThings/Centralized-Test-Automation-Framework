package com.qa.Utils;
import com.qa.Base.BaseClass;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fileUtils.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
   /* @Override
    public synchronized  void onTestStart(ITestResult iTestResult) {
        // String fileName = extentReport.getReportNameWithTimeStamp("yyyyMMdd");
        String testName = iTestResult.getName();
        if(iTestResult.getMethod().getCurrentInvocationCount()<1) {
            String fileName = extentReport.getReportNameWithTimeStamp("yyyyMMdd");
            String fullReportPath = fileUtils.createTestCaseResultsFolder(prop.getProperty("TestCaseResultPath"), testName,"yyyyMMddHHmmss");
            currentTestResultsFolder.set(fullReportPath);
            extent.set( exReport.BuildExtentReport(fullReportPath+"/"+fileName,"GENESIS_AUTOMATION","GENESIS_AUTOMATION_REPORT","dd/MM/yyyy hh:mm:ss"));
            ExtentTest test = extent.get().createTest(testName);
            extentTest.set(test);
            extentTest.get().log(Status.INFO, testName + " : STARTED");
            log.info(testName + " : STARTED");
        }else {
            File parentDirectory = new File(prop.getProperty("TestCaseResultPath"));
            File[] testDirectories = parentDirectory.listFiles((dir,name)->name.startsWith(testName+"_"));
            if(testDirectories!=null && testDirectories.length>0)
            {
                Arrays.sort(testDirectories, Comparator.comparingLong(File::lastModified).reversed());
                File latestDirectory = testDirectories[0];
                latestDirectory.getPath();
                //String relativePath = parentDirectory.toURI().relativize(latestDirectory.toURI()).getPath();
                currentTestResultsFolder.set(latestDirectory.getPath().toString());
            }
        }
        *//*extent.set( exReport.BuildExtentReport(fullReportPath+"/"+fileName,"GENESIS_AUTOMATION","GENESIS_AUTOMATION_REPORT","dd/MM/yyyy hh:mm:ss"));
        ExtentTest test = extent.get().createTest(testName);
        extentTest.set(test);
        extentTest.get().log(Status.INFO, iTestResult.getName() + " : STARTED");
        log.info(iTestResult.getName() + " : STARTED");*//*
}*/
   @Override
    public synchronized void onTestStart(ITestResult iTestResult) {
       String testName = iTestResult.getName();
       int invocationCount = iTestResult.getMethod().getCurrentInvocationCount();
       int interationCount = invocationCount+1;
       String fileName = extentReport.getReportNameWithTimeStamp(testName,"yyyyMMddHHmmss");
       String fullReportPath ;
       if(invocationCount<1) {
            fullReportPath = fileUtils.createTestCaseResultsFolder(prop.getProperty("TestCaseResultPath"), testName, "yyyyMMddHHmmss");
            currentTestResultsFolder.set(fullReportPath);
        }
       else{
           File parentDirectory = new File(prop.getProperty("TestCaseResultPath"));
           File[] testDirectories = parentDirectory.listFiles((dir,name)->name.startsWith(testName+"_"));
           if(testDirectories!=null && testDirectories.length>0)
           {
               Arrays.sort(testDirectories, Comparator.comparingLong(File::lastModified).reversed());
               File latestDirectory = testDirectories[0];
               latestDirectory.getPath();
               //String relativePath = parentDirectory.toURI().relativize(latestDirectory.toURI()).getPath();
               currentTestResultsFolder.set(latestDirectory.getPath().toString());
           }
       }
        extent.set( exReport.BuildExtentReport(currentTestResultsFolder.get()+"/"+fileName,"GENESIS_AUTOMATION","GENESIS_AUTOMATION_REPORT","dd/MM/yyyy hh:mm:ss"));
        ExtentTest test = extent.get().createTest(testName);
        extentTest.set(test);
        extentTest.get().log(Status.INFO, testName + " : STARTED - Iteration: "+ interationCount);
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
            extentTest.get().log(Status.PASS, testName + " passed");
            String resultsFolder = iTestResult.getAttribute("testCaseFolder").toString();
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
        ReportListeners.extentTest.get().log(Status.FAIL, iTestResult.getThrowable());
        ReportListeners.extentTest.get().log(Status.FAIL, testName + " got failed");
        JsonObject logPayload;
        if(iTestResult.getAttribute("documentId")!=null &&
                iTestResult.getAttribute("uniqueIdentifier")!=null ) {
            try {
                logPayload = createRequestPayloadForLogEntry(iTestResult.getAttribute("documentId").toString(), iTestResult.getAttribute("uniqueIdentifier").toString());
                //sendLogInformation(logPayload);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
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
