package com.qa.Pages.client002;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.qa.Base.BaseClass;
import com.qa.component.WEB.DriverActionables.DriverActions;
import com.qa.component.WEB.PageConstants.PageConstants;
import dataUtils.MasterDataUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class AddUserPage extends BaseClass {
    static String addUserPageConstant = PageConstants.addUserPage;
    private static final Logger log = LogManager.getLogger(LandingPage.class);
    MasterDataUtils masterDataUtils ;
    DriverActions driverActions;
    @FindBy(xpath = "//input[@id='firstName']")
    WebElement fistNameTextBox;
    @FindBy(xpath = "//input[@id='lastName']")
    WebElement lastNameTextBox;
    @FindBy(xpath = "//input[@id='email']")
    WebElement emailTextBox;
    @FindBy(xpath = "//input[@id='password']")
    WebElement passwordTextBox;
    @FindBy(xpath = "//button[@id='submit']")
    WebElement submitButton;
    @FindBy(xpath = "//button[@id='cancel']")
    WebElement cancelButton;
    public AddUserPage()
    {
        PageFactory.initElements(getDriver(), this);
    }
    public ContactListPage enterUserDetailsInSignUp(JsonArray completeTestCaseData) {
        boolean isSuccesfull;
        masterDataUtils = new MasterDataUtils();
        JsonObject pageData = masterDataUtils.accessPageWiseWenMobileData(completeTestCaseData, addUserPageConstant);
        boolean hasClicked = false;
        driverActions = new DriverActions();
        try {
            isSuccesfull = driverActions.sendKeys(getDriver(),fistNameTextBox,pageData.get("firstName").getAsString());
            if(isSuccesfull){
                driverActions.sendKeys(getDriver(),lastNameTextBox,pageData.get("lastName").getAsString());
            }
            if(isSuccesfull){
                driverActions.sendKeys(getDriver(),emailTextBox,"Example"+generateRandomName()+pageData.get("email").getAsString());
            }
            if(isSuccesfull){
                driverActions.sendKeys(getDriver(),passwordTextBox,pageData.get("password").getAsString());
            }
            if(isSuccesfull){
                hasClicked =  driverActions.clickOnWebElement(getDriver(),submitButton);
            }
            if(!hasClicked)
            {
                log.error("Could not click on submit button");
            }
        }catch (Exception e) {
            log.error("Could not perform desired operation");
            throw new RuntimeException(e);
        }
        return new ContactListPage();
    }

}
