package com.qa.Tests.WEB;

import WEBHelper.WebDriverManager;
import com.google.gson.JsonElement;
import com.qa.CompositeFunctions.WEB.ContactListCompositeFunctions;
import com.qa.Pages.client002.ContactListPage;
import com.qa.DataProviders.WEBDataProvider;
import com.qa.component.WEB.PageConstants.PageConstants;
import com.qa.component.WEB.WEBReportLogging.WEBReportLogging;
import dataUtils.MasterDataUtils;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.FileNotFoundException;
import java.io.IOException;

import static com.qa.component.WEB.WEBReportLogging.WEBReportLogging.logWebExecutionStepIntoExtentReport;

public class ContactListTestCases extends ContactListCompositeFunctions {
    ContactListCompositeFunctions contactListCompositeFunctions;
    MasterDataUtils masterDataUtils;
    WEBReportLogging webReportLogging;
    private static final ThreadLocal<String> testCaseId = new ThreadLocal<>();
    private static final ThreadLocal<String> environment = new ThreadLocal<>();
    private static final ThreadLocal<String> client = new ThreadLocal<>();
    private static final ThreadLocal<String> BrowserName = new ThreadLocal<>();
    private static final ThreadLocal<String> BrowserVersion = new ThreadLocal<>();
    private static final ThreadLocal<String> OSName = new ThreadLocal<>();
    private static final ThreadLocal<String> OSVersion = new ThreadLocal<>();
    private static final ThreadLocal<String> DeviceName = new ThreadLocal<>();
    private static final ThreadLocal<String> AppVersion = new ThreadLocal<>();
    private static final ThreadLocal<String> groupName = new ThreadLocal<>();
    @BeforeClass
    public void PreRequisites() {
        setProperties();
    }
    @BeforeMethod
    public void BeforeExecutionStart(ITestResult iTestResult) {
        testCaseId.set(iTestResult.getMethod().getMethodName());
        try {
            environment.set(getTestCaseMetaDataBlock(testCaseId.get()).get("Environment").getAsString());
            client.set(getTestCaseMetaDataBlock(testCaseId.get()).get("Client").getAsString());
            BrowserName.set(getTestCaseMetaDataBlock(testCaseId.get()).get("BrowserName").getAsString());
            BrowserVersion.set(getTestCaseMetaDataBlock(testCaseId.get()).get("BrowserVersion").getAsString());
            OSName.set(getTestCaseMetaDataBlock(testCaseId.get()).get("OSName").getAsString());
            OSVersion.set(getTestCaseMetaDataBlock(testCaseId.get()).get("OSVersion").getAsString());
            DeviceName.set(getTestCaseMetaDataBlock(testCaseId.get()).get("DeviceName").getAsString());
            AppVersion.set(getTestCaseMetaDataBlock(testCaseId.get()).get("AppVersion").getAsString());
            groupName.set(getTestCaseMetaDataBlock(testCaseId.get()).get("groupName").getAsString());
            setWebDriver(WebDriverManager.getDriverProvider().setUpWebDriver(BrowserName.get(),Boolean.parseBoolean(prop.getProperty("isHeadless"))));
            OpenApplication(environment.get());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    @Test(description = "Login to Create contact website and create a contact",dataProvider = "getDataIterations" ,dataProviderClass = WEBDataProvider.class,enabled=false)
    public void CON0001(JsonElement testDataSet) throws InterruptedException {
        contactListCompositeFunctions = new ContactListCompositeFunctions();
        masterDataUtils = new MasterDataUtils();
        if (testDataSet.getAsJsonObject().get("ACTIVE").getAsString().equalsIgnoreCase("Y")) {
            ContactListPage contactListPage = ILoginAsExistingUser(getPageWiseData(testDataSet, PageConstants.landingPage));
            contactListPage = IAddNewContact(getPageWiseData(testDataSet, PageConstants.contactLstPage),contactListPage);

        }
    }
    @Test(description = "Login to Create contact website and create a contact",dataProvider = "getDataIterations" ,dataProviderClass = WEBDataProvider.class,enabled=false)
    public void CON0002(JsonElement testDataSet) throws InterruptedException {
        contactListCompositeFunctions = new ContactListCompositeFunctions();
        masterDataUtils = new MasterDataUtils();
        if (testDataSet.getAsJsonObject().get("ACTIVE").getAsString().equalsIgnoreCase("Y")) {
            ContactListPage contactListPage = ILoginAsExistingUser(getPageWiseData(testDataSet, PageConstants.landingPage));
            contactListPage = IAddNewContact(getPageWiseData(testDataSet, PageConstants.contactLstPage),contactListPage);

        }
    }
/*    @Test(description = "Login to Create contact website and create a contact",enabled=false)
    public synchronized void DockerTest() throws IOException, IllegalAccessException, InterruptedException {
        ChromeOptions options = new ChromeOptions();
       // options.addArguments()
        URL url = new URL("http://localhost:4444/wd/hub");
        driver = new RemoteWebDriver(url,options);
        driver.get("https://thinking-tester-contact-list.herokuapp.com/");
        System.out.println("Title of the page " + driver.getTitle());
        driver.quit();
    }*/
    @AfterMethod()
    public void tearDown()
    {
        WebDriverManager.getDriverProvider().quitBrowser();
    }
}
