package com.qa.CompositeFunctions.WEB;

import com.google.gson.JsonArray;
import com.qa.Pages.client002.AddUserPage;
import com.qa.Pages.client002.ContactListPage;
import com.qa.Pages.client002.LandingPage;
import com.qa.Step.WEB.ContactListTestCaseSteps;
import org.testng.Assert;

public class ContactListCompositeFunctions {
    LandingPage landingPage;
    ContactListPage contactListPage;
    ContactListTestCaseSteps contactListTestCaseSteps;
    AddUserPage addUserPage ;

    public String validatePageTitle()
    {
        landingPage = new LandingPage();

        return landingPage.getTitle();
    }
    public synchronized void SignUpAsNewUser(JsonArray masterData) throws InterruptedException {
        landingPage = new LandingPage();
        Assert.assertEquals(validatePageTitle(),"Contact List App");
        addUserPage = landingPage.clickOnSignUpButtonInLandingPage();
        Assert.assertEquals(validatePageTitle(),"Add User");
        contactListPage = addUserPage.enterUserDetailsInSignUp(masterData);
        Thread.sleep(2000);
        Assert.assertEquals(validatePageTitle(),"My Contacts");
    }

}
