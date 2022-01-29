package org.ea.util;

import lombok.experimental.UtilityClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

@UtilityClass
public class Util {

    private static final Logger logger = LoggerFactory.getLogger(Util.class);

    public static WebElement checkElement(WebDriver driver, By xpathButton) {
        WebElement element = Common.waitForElementTobePresent(driver, xpathButton);
        Assert.assertNotNull(element, "Element " + xpathButton + " not found ");
        return element;
    }

    public static void checkElementAndClick(WebDriver driver, By xPath) {
        WebElement element = checkElement(driver, xPath);
        Common.elementClick(driver, element);
    }

    public static void checkElementAndChangeValue(WebDriver driver, By xPath, String newValue) {
        WebElement element = checkElement(driver, xPath);
        Common.elementChangeValue(element, newValue);
    }

    public static void wait(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (Exception e) {
            logger.error("Exception occurred while waiting", e);
        }
    }

}