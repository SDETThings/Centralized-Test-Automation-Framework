package com.qa.component.WEB.DriverActionables;

import com.qa.Base.BaseClass;
import com.qa.component.WEB.webWaits.WebDriverWaits;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class DriverActions extends BaseClass {
    WebDriverWaits waits;
    private static final Logger log = LogManager.getLogger(DriverActions.class);

    public boolean clickOnWebElement(WebDriver driver, WebElement ele) throws Exception {
        boolean flag = false;
        try
        {
            if(ele.isDisplayed())
            {
                ele.click();
                flag = true;
            }else {
                log.error("Cannot find given web element : " + ele);
            }
        }catch (Exception e)
        {
            throw new Exception();
        }
        return flag;
    }
    public boolean sendKeys(WebDriver driver, WebElement ele,String text) throws Exception {
        boolean flag = false;
        try
        {
            if(ele.isDisplayed())
            {
                ele.sendKeys(text);
                flag = true;
            }else {
                log.error("Cannot find given web element : " + ele);
            }
        }catch (Exception e)
        {
            log.error("Cannot enter text : " + text + " in text box : " + ele);
            throw new Exception();
        }
        return flag;
    }
    public boolean enterText(WebDriver driver, WebElement ele,String text) throws Exception {
        boolean flag = false;
        try
        {
            if(ele.isDisplayed())
            {
                ele.sendKeys(text);
                flag = true;
            }else {
                log.error("Cannot find given web element : " + ele);
            }
        }catch (Exception e)
        {
            log.error("Cannot enter text : " + text + " in text box : " + ele);
            throw new Exception();
        }
        return flag;
    }
    public String getTitle(WebDriver driver) throws InterruptedException {
        String text = null;
        waits = new WebDriverWaits(getDriver(),10);
        waits.waitForNSeconds(5);
        text = driver.getTitle();
        if (text!=null) {
            log.debug("Title of the page is: \"" + text + "\"");
        }else{
            throw  new RuntimeException("Title of the page is null ");
        }
        return text;
    }
    public static void scrollByVisibilityOfElement(WebDriver driver, WebElement ele) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView();", ele);

    }
    public void click(WebDriver driver, WebElement ele) throws InterruptedException {
        fluentWait(driver,ele,10,2);
        ele.click();
    }
    public static boolean findElement(WebDriver driver, WebElement ele) {
        fluentWait(driver,ele,10,2);
        boolean flag = false;
        try {
            ele.isDisplayed();
            flag = true;
        } catch (Exception e) {
            flag = false;
        } finally {
            if (flag) {
                System.out.println("Successfully Found element");

            } else {
                System.out.println("Unable to locate element at: "+ele);
            }
        }
        return flag;
    }
    public static boolean elementExist(WebDriver driver, By by) {
        try{
            driver.findElement(by);
            return true;
        }catch(Exception e){
            return false;
        }
    }
    public static boolean isDisplayed(WebDriver driver, WebElement ele) {
        boolean flag = false;
        flag = findElement(driver, ele);
        if (flag) {
            flag = ele.isDisplayed();
            if (flag) {
                System.out.println("The element is Displayed");
            } else {
                System.out.println("The element is not Displayed");
            }
        } else {
            System.out.println("Not displayed ");
        }
        return flag;
    }
    public static boolean isSelected(WebDriver driver, WebElement ele) {
        boolean flag = false;
        flag = findElement(driver, ele);
        if (flag) {
            flag = ele.isSelected();
            if (flag) {
                System.out.println("The element is Selected");
            } else {
                System.out.println("The element is not Selected");
            }
        } else {
            System.out.println("Not selected ");
        }
        return flag;
    }
    public static boolean isEnabled(WebDriver driver, WebElement ele) {
        fluentWait(driver,ele,10,2);
        boolean flag;
        flag = findElement(driver, ele);
        if (flag) {
            flag = ele.isEnabled();
            if (flag) {
                System.out.println("The element is Enabled");
            } else {
                System.out.println("The element is not Enabled: "+ele);
            }
        }
        return flag;
    }
    public static boolean type(WebElement ele, String text) {
        boolean flag = false;
        try {
            flag = ele.isDisplayed();
            ele.clear();
            ele.sendKeys(text);
            // logger.info("Entered text :"+text);
            flag = true;
        } catch (Exception e) {
            System.out.println("Location Not found");
            flag = false;
        } finally {
            if (flag) {
                System.out.println("Successfully entered value");
            } else {
                System.out.println("Unable to enter value");
            }

        }
        return flag;
    }
    public static boolean selectBySendkeys(String value, WebElement ele) {
        boolean flag = false;
        try {
            ele.sendKeys(value);
            flag = true;
            return true;
        } catch (Exception e) {

            return false;
        } finally {
            if (flag) {
                System.out.println("Select value from the DropDown");
            } else {
                System.out.println("Not Selected value from the DropDown");
                // throw new ElementNotFoundException("", "", "")
            }
        }
    }
    public static boolean selectByIndex(WebElement element, int index) {
        boolean flag = false;
        try {
            Select s = new Select(element);
            s.selectByIndex(index);
            flag = true;
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            if (flag) {
                System.out.println("Option selected by Index");
            } else {
                System.out.println("Option not selected by Index");
            }
        }
    }
    public static boolean selectByValue(WebElement element, String value) {
        boolean flag = false;
        try {
            Select s = new Select(element);
            s.selectByValue(value);
            flag = true;
            return true;
        } catch (Exception e) {

            return false;
        } finally {
            if (flag) {
                System.out.println("Option selected by Value");
            } else {
                System.out.println("Option not selected by Value");
            }
        }
    }
    public static boolean selectByVisibleText(String visibletext, WebElement ele) {
        boolean flag = false;
        try {
            Select s = new Select(ele);
            s.selectByVisibleText(visibletext);
            flag = true;
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            if (flag) {
                System.out.println("Option selected by VisibleText");
            } else {
                System.out.println("Option not selected by VisibleText");
            }
        }
    }
    public static boolean JSClick(WebDriver driver, WebElement ele) {
        boolean flag = false;
        try {
            // WebElement element = driver.findElement(locator);
            JavascriptExecutor executor = (JavascriptExecutor) driver;
            executor.executeScript("arguments[0].click();", ele);
            // driver.executeAsyncScript("arguments[0].click();", element);

            flag = true;

        }

        catch (Exception e) {
            throw e;

        } finally {
            if (flag) {
                System.out.println("Click Action is performed");
            } else if (!flag) {
                System.out.println("Click Action is not performed");
            }
        }
        return flag;
    }
    public static boolean switchToFrameById(WebDriver driver, String idValue) {
        boolean flag = false;
        try {
            driver.switchTo().frame(idValue);
            flag = true;
            return true;
        } catch (Exception e) {

            e.printStackTrace();
            return false;
        } finally {
            if (flag) {
                System.out.println("Frame with Id \"" + idValue + "\" is selected");
            } else {
                System.out.println("Frame with Id \"" + idValue + "\" is not selected");
            }
        }
    }
    public static boolean switchToFrameByName(WebDriver driver, String nameValue) {
        boolean flag = false;
        try {
            driver.switchTo().frame(nameValue);
            flag = true;
            return true;
        } catch (Exception e) {

            return false;
        } finally {
            if (flag) {
                System.out.println("Frame with Name \"" + nameValue + "\" is selected");
            } else if (!flag) {
                System.out.println("Frame with Name \"" + nameValue + "\" is not selected");
            }
        }
    }
    public static boolean switchToDefaultFrame(WebDriver driver) {
        boolean flag = false;
        try {
            driver.switchTo().defaultContent();
            flag = true;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (flag) {
                // SuccessReport("SelectFrame ","Frame with Name is selected");
            } else if (!flag) {
                // failureReport("SelectFrame ","The Frame is not selected");
            }
        }
    }
    public static void mouseOverElement(WebDriver driver, WebElement element) {
        boolean flag = false;
        try {
            new Actions(driver).moveToElement(element).build().perform();
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (flag) {
                System.out.println(" MouserOver Action is performed on ");
            } else {
                System.out.println("MouseOver action is not performed on");
            }
        }
    }
    public static boolean moveToElement(WebDriver driver, WebElement ele) {
        boolean flag = false;
        try {
            // WebElement element = driver.findElement(locator);
            JavascriptExecutor executor = (JavascriptExecutor) driver;
            executor.executeScript("arguments[0].scrollIntoView(true);", ele);
            Actions actions = new Actions(driver);
            // actions.moveToElement(driver.findElement(locator)).build().perform();
            actions.moveToElement(ele).build().perform();
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }
    public static boolean mouseover(WebDriver driver, WebElement ele) {
        boolean flag = false;
        try {
            new Actions(driver).moveToElement(ele).build().perform();
            flag = true;
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            /*
             * if (flag) {
             * SuccessReport("MouseOver ","MouserOver Action is performed on \""+locatorName
             * +"\""); } else {
             * failureReport("MouseOver","MouseOver action is not performed on \""
             * +locatorName+"\""); }
             */
        }
    }
    public static boolean draggable(WebDriver driver, WebElement source, int x, int y) {
        boolean flag = false;
        try {
            new Actions(driver).dragAndDropBy(source, x, y).build().perform();
            Thread.sleep(5000);
            flag = true;
            return true;

        } catch (Exception e) {

            return false;

        } finally {
            if (flag) {
                System.out.println("Draggable Action is performed on \"" + source + "\"");
            } else if (!flag) {
                System.out.println("Draggable action is not performed on \"" + source + "\"");
            }
        }
    }
    public static boolean draganddrop(WebDriver driver, WebElement source, WebElement target) {
        boolean flag = false;
        try {
            new Actions(driver).dragAndDrop(source, target).perform();
            flag = true;
            return true;
        } catch (Exception e) {

            return false;
        } finally {
            if (flag) {
                System.out.println("DragAndDrop Action is performed");
            } else if (!flag) {
                System.out.println("DragAndDrop Action is not performed");
            }
        }
    }
    public static boolean slider(WebDriver driver, WebElement ele, int x, int y) {
        boolean flag = false;
        try {
            // new Actions(driver).dragAndDropBy(dragitem, 400, 1).build()
            // .perform();
            new Actions(driver).dragAndDropBy(ele, x, y).build().perform();// 150,0
            Thread.sleep(5000);
            flag = true;
            return true;
        } catch (Exception e) {

            return false;
        } finally {
            if (flag) {
                System.out.println("Slider Action is performed");
            } else {
                System.out.println("Slider Action is not performed");
            }
        }
    }
    public static boolean rightclick(WebDriver driver, WebElement ele) {
        boolean flag = false;
        try {
            Actions clicker = new Actions(driver);
            clicker.contextClick(ele).perform();
            flag = true;
            return true;
            // driver.findElement(by1).sendKeys(Keys.DOWN);
        } catch (Exception e) {

            return false;
        } finally {
            if (flag) {
                System.out.println("RightClick Action is performed");
            } else {
                System.out.println("RightClick Action is not performed");
            }
        }
    }
    public static boolean switchWindowByTitle(WebDriver driver, String windowTitle, int count) {
        boolean flag = false;
        try {
            Set<String> windowList = driver.getWindowHandles();

            String[] array = windowList.toArray(new String[0]);

            driver.switchTo().window(array[count - 1]);

            if (driver.getTitle().contains(windowTitle)) {
                flag = true;
            } else {
                flag = false;
            }
            return flag;
        } catch (Exception e) {
            // flag = true;
            return false;
        } finally {
            if (flag) {
                System.out.println("Navigated to the window with title");
            } else {
                System.out.println("The Window with title is not Selected");
            }
        }
    }
    public static boolean switchToNewWindow(WebDriver driver) {
        boolean flag = false;
        try {

            Set<String> s = driver.getWindowHandles();
            Object popup[] = s.toArray();
            driver.switchTo().window(popup[1].toString());
            flag = true;
            return flag;
        } catch (Exception e) {
            flag = false;
            return flag;
        } finally {
            if (flag) {
                System.out.println("Window is Navigated with title");
            } else {
                System.out.println("The Window with title: is not Selected");
            }
        }
    }
    public static boolean switchToOldWindow(WebDriver driver) {
        boolean flag = false;
        try {

            Set<String> s = driver.getWindowHandles();
            Object popup[] = s.toArray();
            driver.switchTo().window(popup[0].toString());
            flag = true;
            return flag;
        } catch (Exception e) {
            flag = false;
            return flag;
        } finally {
            if (flag) {
                System.out.println("Focus navigated to the window with title");
            } else {
                System.out.println("The Window with title: is not Selected");
            }
        }
    }
    public static int getColumncount(WebElement row) {
        List<WebElement> columns = row.findElements(By.tagName("td"));
        int a = columns.size();
        System.out.println(columns.size());
        for (WebElement column : columns) {
            System.out.print(column.getText());
            System.out.print("|");
        }
        return a;
    }
    public static int getRowCount(WebElement table) {
        List<WebElement> rows = table.findElements(By.tagName("tr"));
        int a = rows.size() - 1;
        return a;
    }
    public static boolean Alert(WebDriver driver) {
        boolean presentFlag = false;
        Alert alert = null;

        try {
            // Check the presence of alert
            alert = driver.switchTo().alert();
            // if present consume the alert
            alert.accept();
            presentFlag = true;
        } catch (NoAlertPresentException ex) {
            // Alert present; set the flag

            // Alert not present
            ex.printStackTrace();
        } finally {
            if (!presentFlag) {
                System.out.println("The Alert is handled successfully");
            } else {
                System.out.println("There was no alert to handle");
            }
        }

        return presentFlag;
    }
    public static boolean launchUrl(WebDriver driver, String url) {
        boolean flag = false;
        try {
            driver.navigate().to(url);
            flag = true;
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            if (flag) {
                System.out.println("Successfully launched \"" + url + "\"");
            } else {
                System.out.println("Failed to launch \"" + url + "\"");
            }
        }
    }
    public static boolean isAlertPresent(WebDriver driver) {
        try {
            driver.switchTo().alert();
            return true;
        } // try
        catch (NoAlertPresentException Ex) {
            return false;
        } // catch
    }
    public static String getCurrentURL(WebDriver driver) {
        boolean flag = false;

        String text = driver.getCurrentUrl();
        if (flag) {
            System.out.println("Current URL is: \"" + text + "\"");
        }
        return text;
    }
    public static boolean click1(WebElement locator, String locatorName) {
        boolean flag = false;
        try {
            locator.click();
            flag = true;
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            if (flag) {
                System.out.println("Able to click on \"" + locatorName + "\"");
            } else {
                System.out.println("Click Unable to click on \"" + locatorName + "\"");
            }
        }

    }
    public static Boolean fluentWait(WebDriver driver, WebElement element, int totsec, int pollsec) {
        Wait<WebDriver> wait = null;
        try {
            wait = new FluentWait<WebDriver>((WebDriver) driver).withTimeout(Duration.ofSeconds(totsec))
                    .pollingEvery(Duration.ofSeconds(pollsec)).ignoring(Exception.class);
            wait.until(ExpectedConditions.visibilityOf(element));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public static void implicitWait(WebDriver driver, int timeOut) {
        driver.manage().timeouts().implicitlyWait(timeOut, TimeUnit.SECONDS);
    }
    public static void pageLoadTimeOut(WebDriver driver, int timeOut) {
        driver.manage().timeouts().pageLoadTimeout(timeOut, TimeUnit.SECONDS);
    }
    public static String screenShot(WebDriver driver, String filename, String path) {
        String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
        TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
        File source = takesScreenshot.getScreenshotAs(OutputType.FILE);
        String destination = path + "/" + filename + "_" + dateName + ".png";
        try {
            FileUtils.copyFile(source, new File(destination));
        } catch (Exception e) {
            e.getMessage();
        }
        // This new path for jenkins
        //String newImageString = "./src/test/resources/Screenshot" + filename
        //+ "_" + dateName + ".png";

        return destination;
    }
}
