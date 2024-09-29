package com.qa.component.WEB.WEBReportLogging;

import DataHandlerUtils.JsonOperations;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.google.gson.*;
import com.qa.Base.BaseClass;
import com.qa.Utils.ReportListeners;
import io.restassured.http.Method;
import io.restassured.response.Response;

import java.util.Map;

public class WEBReportLogging extends BaseClass {
    Gson gson ;
    JsonOperations jsonOperations;
    public static void logWebExecutionStepIntoExtentReport( String message ){
        synchronized (WEBReportLogging.class)
        {
            ReportListeners.extentTest.get().log(Status.INFO, message);
        }
    }
    public void logWebExecutionScreenshotIntoExtentReport(){
        synchronized (WEBReportLogging.class)
        {
            ReportListeners.extentTest.get().log(Status.INFO,  takeScreenshot(getDriver(), "", prop.getProperty("tempScreenShotFolder")));
        }
    }
    public static String getJavaScriptFunction() {
        return "<script>"
                + "function toggleVisibility(button) {"
                + "  var detailsDiv = button.nextElementSibling;"
                + "  if (detailsDiv.style.display === 'none') {"
                + "    detailsDiv.style.display = 'block';"
                + "    button.innerHTML = '- ' + button.innerHTML.substring(2);"
                + "  } else {"
                + "    detailsDiv.style.display = 'none';"
                + "    button.innerHTML = '+ ' + button.innerHTML.substring(2);"
                + "  }"
                + "}"
                + "</script>";
    }
}