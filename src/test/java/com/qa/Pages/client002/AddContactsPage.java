package com.qa.Pages.client002;

import WebSynchronization.DriverActionsFactory;
import com.google.gson.JsonObject;
import com.qa.Base.BaseClass;
import com.qa.component.WEB.DriverActionables.DriverActions;
import com.qa.component.WEB.WEBReportLogging.WEBReportLogging;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.sql.Driver;

import static com.qa.component.WEB.WEBReportLogging.WEBReportLogging.*;

public class AddContactsPage extends BaseClass {
    DriverActions driverActions;
    WEBReportLogging webReportLogging;
    public AddContactsPage(){
        PageFactory.initElements(getDriver(),this);
    }
    @FindBy(xpath = "//input[@id='firstName']")
    WebElement firstNameTextField;
    @FindBy(xpath = "//input[@id='lastName']")
    WebElement lastNameTextField;
    @FindBy(xpath = "//button[@id='submit']")
    WebElement submitButton;
    public ContactListPage addContact(JsonObject testData){
        driverActions = new DriverActions();
        webReportLogging = new WEBReportLogging();
        try {
            DriverActionsFactory.getInstance().enterText(getDriver(),firstNameTextField,testData.get("firstName").getAsString(),timeOutSeconds,pollingInterval);
            DriverActionsFactory.getInstance().enterText(getDriver(),lastNameTextField,testData.get("lastName").getAsString(),timeOutSeconds,pollingInterval);
            DriverActionsFactory.getInstance().clickOnWebElement(getDriver(),submitButton,timeOutSeconds,pollingInterval);
            logWebExecutionStepIntoExtentReport("New contact created with | FIRSTNAME | : "
                    +testData.get("firstName").getAsString() + " | LASTNAME | "+ testData.get("lastName").getAsString());
        } catch (Exception e) {
            logWebExecutionStepIntoExtentReport("Unable to create new contact , Please check exception: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return new ContactListPage();
    }
}
