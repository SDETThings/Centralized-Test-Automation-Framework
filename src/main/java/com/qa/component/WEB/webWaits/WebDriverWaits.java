package com.qa.component.WEB.webWaits;

import com.qa.Base.BaseClass;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class WebDriverWaits extends BaseClass {
    private WebDriver driver;
    private WebDriverWait explicitWait;

    // Constructor to initialize WebDriver and WebDriverWait
    public WebDriverWaits(WebDriver driver, int timeoutInSeconds) {
        this.driver = driver;
        this.explicitWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
    }

    // Explicit wait for element to be visible
    public WebElement waitForElementToBeVisible(By locator) {
        return explicitWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    // Explicit wait for element to be clickable
    public WebElement waitForElementToBeClickable(By locator) {
        return explicitWait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    // Fluent wait method (configurable polling duration and ignoring exceptions)
    public WebElement fluentWaitForElement(final By locator, int timeoutInSeconds, int pollingInMillis) {
        Wait<WebDriver> fluentWait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(timeoutInSeconds))
                .pollingEvery(Duration.ofMillis(pollingInMillis))
                .ignoring(NoSuchElementException.class)
                .ignoring(TimeoutException.class);

        return fluentWait.until(new Function<WebDriver, WebElement>() {
            public WebElement apply(WebDriver driver) {
                return driver.findElement(locator);
            }
        });
    }

    // Wait for page title to contain a specific text
    public boolean waitForTitleContains(String titleFragment) {
        try {
            return explicitWait.until(new ExpectedCondition<Boolean>() {
                @Override
                public Boolean apply(WebDriver driver) {
                    String title = driver.getTitle();
                    return title != null && title.contains(titleFragment);
                }
            });
        } catch (TimeoutException e) {
            return false;
        }
    }

    // Fluent wait for a condition based on a custom function
    public <T> T fluentWaitForCondition(Function<WebDriver, T> condition, int timeoutInSeconds, int pollingInMillis) {
        Wait<WebDriver> fluentWait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(timeoutInSeconds))
                .pollingEvery(Duration.ofMillis(pollingInMillis))
                .ignoring(NoSuchElementException.class)
                .ignoring(TimeoutException.class);

        return fluentWait.until(condition);
    }
    public void waitForNSeconds(int secondsToWaitFor) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        jsExecutor.executeAsyncScript("window.setTimeout(arguments[arguments.length - 1],"+(secondsToWaitFor*1000)+");");

    }
}
