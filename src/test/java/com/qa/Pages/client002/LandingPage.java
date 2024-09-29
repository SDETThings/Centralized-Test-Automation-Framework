package com.qa.Pages.client002;

import com.google.gson.JsonObject;
import com.qa.Base.BaseClass;
import com.qa.component.WEB.DriverActionables.DriverActions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class LandingPage extends BaseClass {
    private static final Logger log = LogManager.getLogger(LandingPage.class);
    DriverActions driverActions;
    @FindBy(xpath = "//button[@id='signup']")
    WebElement signUpButton;
    @FindBy(xpath = "//input[@id='email']")
    WebElement emailTextBox;
    @FindBy(xpath = "//input[@id='password']")
    WebElement passwordTextBox;
    @FindBy(xpath = "//button[@id='submit']")
    WebElement submitButton;

    public LandingPage() {
        PageFactory.initElements(getDriver(), this);
    }
    public String getTitle() throws InterruptedException {
        driverActions = new DriverActions();

        return driverActions.getTitle(getDriver());
    }
    public AddUserPage clickOnSignUpButtonInLandingPage() {
        boolean hasClicked;
        driverActions = new DriverActions();
        try {
            hasClicked =  driverActions.clickOnWebElement(getDriver(),signUpButton);
            if(!hasClicked)
            {
                log.error("Could not click on signUp button");
            }
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new AddUserPage();
    }
    public ContactListPage existingUserSignIn(JsonObject testData){
        driverActions = new DriverActions();
        try {
            driverActions.enterText(getDriver(),emailTextBox,testData.get("email").getAsString());
            driverActions.enterText(getDriver(),passwordTextBox,testData.get("password").getAsString());
            driverActions.clickOnWebElement(getDriver(),submitButton);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new ContactListPage();
    }

}
