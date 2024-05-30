package com.qa.Pages.client001;

import com.qa.Base.BaseClass;
import com.qa.component.WEB.DriverActionables.DriverActions;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class contactListPage extends BaseClass {
    DriverActions driverActions;
    @FindBy(xpath = "//button[@id='add-contact']")
    WebElement addNewContactButton;

    public String getTitleContactListPage()
    {
        driverActions = new DriverActions();

       return driverActions.getTitle(getDriver());
    }
}
