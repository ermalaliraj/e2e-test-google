package org.ea.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.io.FileUtils;
import org.ea.config.Configuration;
import org.ea.config.TestNgParameters;
import org.openqa.selenium.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

@UtilityClass
public class E2eUtil {

    private static final Logger logger = LoggerFactory.getLogger(E2eUtil.class);

    private final Configuration config = new Configuration();

    public void scrollAndClick(WebDriver driver, By element) {
        try {
            WebElement webElement = driver.findElement(element);
            JavascriptExecutor executor = (JavascriptExecutor) driver;
            executor.executeScript("arguments[0].scrollIntoView(true);", webElement);
            executor.executeScript("arguments[0].click();", webElement);
            logger.info("Element {} clicked", webElement);
        } catch (StaleElementReferenceException ignore) {
            logger.info("Exception occured while clicking element, but trying click again");
            WebElement webElement = driver.findElement(element);
            JavascriptExecutor executor = (JavascriptExecutor) driver;
            executor.executeScript("arguments[0].scrollIntoView(true);", webElement);
            executor.executeScript("arguments[0].click();", webElement);
            logger.info("Exception occured while clicking element");
        } catch (Exception e) {
            logger.info("Exception occured while clicking element");
            throw e;
        }
    }

    public void scrollAndDoubleClick(WebDriver driver, By element) {
        try {
            WebElement webElement = driver.findElement(element);
            JavascriptExecutor executor = (JavascriptExecutor) driver;
            executor.executeScript("arguments[0].scrollIntoView(true);", webElement);
            executor.executeScript("var evt = document.createEvent('MouseEvents'); evt.initMouseEvent('dblclick',true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0,null);arguments[0].dispatchEvent(evt);", element);
            logger.info("Element {} double clicked", webElement);
            E2eUtil.takeSnapShot(driver, "PASS");
        } catch (Exception e) {
            logger.info("Exception occured while double clicking element");
            E2eUtil.takeSnapShot(driver, "FAIL");
        }
    }

    public void highlightElement(WebDriver driver, WebElement element) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        jsExecutor.executeScript("arguments[0].setAttribute('style', 'border:3px solid yellow;')", element);
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        jsExecutor.executeScript("arguments[0].setAttribute('style', 'border:;')", element);
    }

    /**
     * To take the screenshots of the application in case if validation needed at a particular screen
     * based on the configuration value provided the screenshot will be taking or else wont
     */
    public void takeSnapShot(WebDriver driver, String status) {
        if (config.getProperty("takeScreenshots.pass").contains("TRUE") && status.contains("PASS")) {
            copyScreenshot(driver);
        }
        if (config.getProperty("takeScreenshots.fail").contains("TRUE") && status.contains("FAIL")) {
            copyScreenshot(driver);
        }
    }

    /**
     * Support function to take screenshot
     */
    private void copyScreenshot(WebDriver driver) {
        TakesScreenshot scrShot = ((TakesScreenshot) driver);
        File srcFile = scrShot.getScreenshotAs(OutputType.FILE);
        String fileName = Configuration.SCREENSHOT;
        File destFile = new File(fileName);
        boolean copyScreenshots = Boolean.parseBoolean(System.getProperty("copyScreenshots"));
        if (copyScreenshots) {
            TestNgParameters.getInstance().setScreenshotPath(fileName);
        } else {
            TestNgParameters.getInstance().setScreenshotPath(destFile.getAbsolutePath());
        }
        try {
            FileUtils.copyFile(srcFile, destFile);
            logger.info("Screenshot captured at " + destFile);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * Definite wait needed at multiple place, used a function instead Thread.sleep method
     */
    public void wait(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

}