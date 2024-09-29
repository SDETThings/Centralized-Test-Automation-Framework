package com.qa.CompositeFunctions.WEB;

import com.google.gson.JsonObject;
import com.qa.Base.BaseClass;
import com.qa.Pages.client002.ContactListPage;
import com.qa.Step.WEB.ContactListTestCaseSteps;

public class ContactListCompositeFunctions extends BaseClass {
    ContactListTestCaseSteps contactListTestCaseSteps;
    public ContactListPage ILoginAsExistingUser(JsonObject testData) throws InterruptedException {
        contactListTestCaseSteps =new ContactListTestCaseSteps();
        return contactListTestCaseSteps.loginAndValidateSuccesfullLogin(testData);
    }
    public ContactListPage IAddNewContact(JsonObject testData, ContactListPage contactListPage){
        contactListTestCaseSteps =new ContactListTestCaseSteps();
        return contactListTestCaseSteps.AddNewContact(testData,contactListPage);
    }

}
