package com.qa.Step.WEB;

import com.google.gson.JsonObject;
import com.qa.Base.BaseClass;
import com.qa.Pages.client002.AddContactsPage;
import com.qa.Pages.client002.AddUserPage;
import com.qa.Pages.client002.ContactListPage;
import com.qa.Pages.client002.LandingPage;
import com.qa.component.WEB.WEBReportLogging.WEBReportLogging;

import static com.qa.component.WEB.WEBReportLogging.WEBReportLogging.*;

public class ContactListTestCaseSteps extends BaseClass {
    LandingPage landingPage;
    AddUserPage addUserPage;
    ContactListPage contactListPage;
    WEBReportLogging webReportLogging;

    public ContactListPage loginAndValidateSuccesfullLogin(JsonObject testData) throws InterruptedException {
        landingPage = new LandingPage();
        webReportLogging = new WEBReportLogging();
        contactListPage= landingPage.existingUserSignIn(testData);
        if(prop.getProperty("ContactListPageTitle").equalsIgnoreCase(contactListPage.validatePageTitle())){
            logWebExecutionStepIntoExtentReport("Successfully logged into application with existing credentials");
            return contactListPage;
        }else{
            logWebExecutionStepIntoExtentReport("Unable to login to application : Please check credentials");
            throw  new RuntimeException("Title did not match");
        }
    }
    public ContactListPage AddNewContact(JsonObject testData,ContactListPage contactListPage){
        AddContactsPage addContactsPage = contactListPage.NavigateToAddContactsPage();
        return addContactsPage.addContact(testData);
    }

}
