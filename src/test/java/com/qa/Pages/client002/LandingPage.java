package com.qa.Pages.client002;

import com.qa.Base.BaseClass;
import com.qa.component.WEB.DriverActionables.DriverActions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LandingPage extends BaseClass {
    private static final Logger log = LogManager.getLogger(LandingPage.class);
    DriverActions driverActions;
    @FindBy(xpath = "//button[@id='signup']")
    WebElement signUpButton;

    public LandingPage()
    {
        PageFactory.initElements(getDriver(), this);
    }
    public String getTitle()
    {
        driverActions = new DriverActions();

        return driverActions.getTitle(getDriver());
    }
    public AddUserPage clickOnSignUpButtonInLandingPage()
    {
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

}
