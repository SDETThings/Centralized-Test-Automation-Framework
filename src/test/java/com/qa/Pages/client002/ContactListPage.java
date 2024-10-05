package com.qa.Pages.client002;

import WebSynchronization.DriverActionsFactory;
import com.qa.Base.BaseClass;
import com.qa.component.WEB.DriverActionables.DriverActions;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ContactListPage extends BaseClass {

    public ContactListPage() {
        PageFactory.initElements(getDriver(), this);
    }
    DriverActions driverActions;
    @FindBy(xpath = "//button[@id='add-contact']")
    WebElement addNewContactButton;

    public String validatePageTitle() throws InterruptedException {
        driverActions = new DriverActions();
        return driverActions.getTitle(getDriver());
    }
    public AddContactsPage NavigateToAddContactsPage()
    {
        driverActions = new DriverActions();
        try {
            DriverActionsFactory.getInstance().clickOnWebElement(getDriver(),addNewContactButton,timeOutSeconds,pollingInterval);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new AddContactsPage();
    }


}
