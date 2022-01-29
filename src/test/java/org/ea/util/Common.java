package org.ea.util;

import org.apache.commons.io.FileUtils;
import org.ea.config.Configuration;
import org.ea.config.TestNgParameters;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.Reporter;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.ea.config.WebDriverFactory.POLLING_TIME;
import static org.ea.config.WebDriverFactory.TIME_OUT;

public class Common {

    private static final Logger logger = LoggerFactory.getLogger(Common.class);
    private static final Configuration config = new Configuration();

    public static void startApp(WebDriver driver, String appName) {
        String appUrl = config.getProperty("appUrl." + appName.toLowerCase());
        logger.debug("Connecting to {} application on url {}", appName, appUrl);
        driver.get(appUrl);
    }

    public static void scrollToOffset(WebDriver driver, int x, int y) {
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("window.scrollBy(arguments[0], arguments[1]);", x, y);
    }

    public static WebElement scrollTo(WebDriver driver, By by) {
        WebElement element = null;
        waitForElement(driver, by);
        if (elementExists(driver, by)) {
            element = driver.findElement(by);
            JavascriptExecutor executor = (JavascriptExecutor) driver;
            executor.executeScript("arguments[0].scrollIntoView(true);", element);
            logger.info("Page is scrolled to the element {}", element);
        }
        return element;
    }

    public static void scrollTo(WebDriver driver, WebElement element) {
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].scrollIntoView(true);", element);
        logger.info("Page is scrolled to the element {}", element);
    }

    public static void scrollAndClick(WebDriver driver, By by) {
        WebElement element = driver.findElement(by);
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].scrollIntoView(true);", element);
        executor.executeScript("arguments[0].click();", element);
        logger.info("Element {} clicked", element);
    }

    public static void scrollAndDoubleClick(WebDriver driver, By element) {
        WebElement webElement = driver.findElement(element);
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].scrollIntoView(true);", webElement);
        executor.executeScript("var evt = document.createEvent('MouseEvents'); evt.initMouseEvent('dblclick',true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0,null);arguments[0].dispatchEvent(evt);", element);
        logger.info("Element {} double clicked", webElement);
    }

    public static void highlightElement(WebDriver driver, WebElement element) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        jsExecutor.executeScript("arguments[0].setAttribute('style', 'border:3px solid yellow;')", element);
        Util.wait(2000);
        jsExecutor.executeScript("arguments[0].setAttribute('style', 'border:;')", element);
    }

    public static void waitForElementAndHighlight(WebDriver driver, By by) {
        WebElement element = scrollTo(driver, by);
        highlightElement(driver, element);
    }

    public static void waitForPageLoadComplete(WebDriver driver, int timeout) {
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        ExpectedCondition<Boolean> jsLoad = driver1 -> "complete".equals(((JavascriptExecutor) driver)
                .executeScript("return document.readyState").toString());
        wait.until(jsLoad);
    }

    public static void waitForElement(WebDriver driver, By by) {
        waitForPageLoadComplete(driver, TIME_OUT);
        WebDriverWait wait = new WebDriverWait(driver, TIME_OUT);
        wait.until(ExpectedConditions.presenceOfElementLocated(by));
    }

    public static WebElement waitForElementClickable(WebDriver driver, By by) {
        driver.manage().timeouts().implicitlyWait(TIME_OUT, TimeUnit.SECONDS);
        WebDriverWait wait = new WebDriverWait(driver, TIME_OUT);
        return wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(by)));
    }

    public static WebElement waitForElementTobePresent(WebDriver driver, By by) {
        Wait<WebDriver> wait = new FluentWait<>(driver)
//                .withTimeout(Duration.ofSeconds(TIME_OUT))
                .pollingEvery(Duration.ofSeconds(POLLING_TIME))
                .ignoring(Exception.class);
        return wait.until(driver1 -> driver1.findElement(by));
    }

    public static boolean waitForElementToBeDisplayed(WebDriver driver, By by) {
        Wait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(TIME_OUT))
                .pollingEvery(Duration.ofSeconds(POLLING_TIME))
                .ignoring(Exception.class);
        return wait.until(driver1 -> driver1.findElement(by).isDisplayed());
    }

    public static boolean waitForElementToNotBeDisplayed(WebDriver driver, By by) {
        try {
            driver.manage().timeouts().implicitlyWait(POLLING_TIME, TimeUnit.SECONDS);
            Wait<WebDriver> wait = new FluentWait<>(driver)
                    .withTimeout(Duration.ofSeconds(TIME_OUT))
                    .pollingEvery(Duration.ofSeconds(POLLING_TIME));
            boolean found = false;
            long totalTime = 0;
            long startTime;
            long endTime;
            while (!found && (totalTime / 1000) < TIME_OUT) {
                startTime = System.currentTimeMillis();
                found = wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
                endTime = System.currentTimeMillis();
                totalTime = totalTime + (endTime - startTime);
            }
            return found;
        } catch (Exception e) {
            return false;
        } finally {
            driver.manage().timeouts().implicitlyWait(TIME_OUT, TimeUnit.SECONDS);
        }
    }

    public static boolean waitElementToBeDisplayedWithInSpecifiedTime(WebDriver driver, By by, int timeOut) {
        boolean bool = false;
        try {
            driver.manage().timeouts().implicitlyWait(timeOut, TimeUnit.SECONDS);
            WebElement element = driver.findElement(by);
            if (null != element) {
                bool = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.manage().timeouts().implicitlyWait(TIME_OUT, TimeUnit.SECONDS);
        }
        return bool;
    }

    public static void waitForElementAndClick(WebDriver driver, By by) {
        WebElement element = waitForElementClickable(driver, by);
        highlightElement(driver, element);
        element.click();
        logger.debug("The following element is displayed and clicked: {}", element);
    }

    public static void elementClick(WebDriver driver, WebElement element) {
        highlightElement(driver, element);
        element.click();
        logger.info("The following element is displayed and clicked: {}", element);
    }

    public static void waitForElementAndDoubleClick(WebDriver driver, By by) {
        WebElement element = waitForElementClickable(driver, by);
        Actions actions = new Actions(driver);
        actions.moveToElement(element).doubleClick().build().perform();
        logger.info("The following element is double clicked: {}", by);
    }

    public static void elementActionClick(WebDriver driver, By by) {
        Actions act;
        Wait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(TIME_OUT))
                .pollingEvery(Duration.ofSeconds(POLLING_TIME))
                .ignoring(NoSuchElementException.class);
        WebElement element = wait.until(driver1 -> driver1.findElement(by));
        if (null != element) {
            act = new Actions(driver);
            act.moveToElement(element).click().release().build().perform();
        }
    }

    public static void elementChangeValue(WebDriver driver, By by, String data) {
        WebElement element = driver.findElement(by);
        element.clear();
        element.sendKeys(data);
    }

    public static void elementChangeValue(WebElement element, String value) {
        element.clear();
        element.sendKeys(value);
    }

    public static void setValueToElement(WebDriver driver, By by, String val) {
        WebElement element = driver.findElement(by);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].value=arguments[1];", element, val);
    }

    public static boolean elementExists(WebDriver driver, By by) {
        return elementExistsWithoutWait(driver, by);
    }

    public static boolean elementExistsWithoutWait(WebDriver driver, By by) {
        for (int i = 0; true; i++) {
            try {
                return driver.findElement(by).isEnabled();
            } catch (StaleElementReferenceException e) {
                if (i > 2) { // Tries 3 times in case of StaleElementReferenceException
                    throw e;
                }
            } catch (NoSuchElementException e) {
                return false;
            }
        }
    }

    public String getElementText(WebDriver driver, By by) {
        WebElement element = driver.findElement(by);
        return getElementText(element);
    }

    public String getElementText(WebElement element) {
        return element.getText();
    }

    public String getElementAttribute(WebDriver driver, By by, String attrName) {
        WebElement element = driver.findElement(by);
        return getElementAttribute(element, attrName);
    }

    public String getElementAttribute(WebElement element, String attrName) {
        return element.getAttribute(attrName);
    }

    public String getElementValue(WebElement element) {
        return getElementAttribute(element, "value");
    }

    public String getElementInnerText(WebDriver driver, By by) {
        WebElement element = driver.findElement(by);
        return getElementInnerText(element);
    }

    public String getElementInnerText(WebElement element) {
        return getElementAttribute(element, "innerText");
    }

    public static boolean verifyElementIsSelected(WebDriver driver, By by) {
        WebElement element = driver.findElement(by);
        return element.isSelected();
    }

    public static boolean verifyElementIsSelected(WebElement element) {
        return element.isSelected();
    }

    public static boolean verifyElementIsEnabled(WebDriver driver, By by) {
        return elementExistsWithoutWait(driver, by);
    }

    public static void selectText(WebDriver driver, By by) {
        WebElement element = driver.findElement(by);
        Dimension size = element.getSize();
        int width = size.getWidth();
        Actions action = new Actions(driver);
        action.clickAndHold(element)
                .moveToElement(element, -width / 2, 0)
                .build().perform();
        action.release().build().perform();
    }

    public List<WebElement> getElements(WebDriver driver, By by, int timeOut) {
        List<WebElement> elementList;
        try {
            driver.manage().timeouts().implicitlyWait(timeOut, TimeUnit.SECONDS);
            elementList = driver.findElements(by);
        } finally {
            driver.manage().timeouts().implicitlyWait(TIME_OUT, TimeUnit.SECONDS);
        }
        return elementList;
    }

    private static void exceptionReport(WebDriver driver, By element, Exception e) {
        Reporter.getCurrentTestResult().setStatus(ITestResult.FAILURE);
        takeScreenshot(driver);
        logger.error("The exception occurred in finding the following element " + element, e);
        throw new AssertionError("The exception occurred in finding the following element " + element, e);
    }

    public static void takeScreenshot(WebDriver driver) {
        int resultStatus = Reporter.getCurrentTestResult().getStatus();
        boolean takeScreenshot = false;
        if (resultStatus == 1) {
            takeScreenshot = "true".equalsIgnoreCase(config.getProperty("takeScreenshots.pass"));
        } else if (resultStatus == 2) {
            takeScreenshot = "fail".equalsIgnoreCase(config.getProperty("takeScreenshots.fail"));
        }

        if (takeScreenshot) {
            doTakeScreenshot(driver);
        }
    }

    private static void doTakeScreenshot(WebDriver driver) {
        TakesScreenshot screenShot = ((TakesScreenshot) driver);
        File srcFile = screenShot.getScreenshotAs(OutputType.FILE);
        String fileName = Configuration.SCREENSHOT;
        File destFile = new File(fileName);
        boolean copyScreenshots = Boolean.parseBoolean(System.getProperty("copyScreenshots")); //why not in property file. actually in pom
        if (copyScreenshots) {
            TestNgParameters.getInstance().setScreenshotPath(fileName);
        } else {
            TestNgParameters.getInstance().setScreenshotPath(destFile.getAbsolutePath());
        }
        try {
            FileUtils.copyFile(srcFile, destFile);
            logger.info("Screenshot captured at " + destFile);
        } catch (IOException e) {
            logger.error("Error copying screenshot file", e);
        }
    }
}