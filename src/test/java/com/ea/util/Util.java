package com.ea.util;

import lombok.experimental.UtilityClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.ea.util.ElementUtil.elementChangeValue;
import static com.ea.util.ElementUtil.elementClick;
import static com.ea.util.ElementUtil.getElements;
import static com.ea.util.ElementUtil.waitForElementTobePresent;

import static org.testng.Assert.assertNotNull;

@UtilityClass
public class Util {

    private static final Logger logger = LoggerFactory.getLogger(Util.class);

    public static WebElement checkElement(WebDriver driver, By xpathButton) {
        WebElement element = waitForElementTobePresent(driver, xpathButton);
        assertNotNull(element, "Element " + xpathButton + " not found ");
        return element;
    }

    public static void checkElementAndClick(WebDriver driver, By xPath) {
        WebElement element = checkElement(driver, xPath);
        elementClick(driver, element);
    }

    public static void checkElementAndChangeValue(WebDriver driver, By xPath, String newValue) {
        WebElement element = checkElement(driver, xPath);
        elementChangeValue(element, newValue);
    }

    public static List<WebElement> checkElements(WebDriver driver, By xPath) {
        List<WebElement> elements = getElements(driver, xPath);
        assertNotNull(elements, "Elements " + xPath + " not found ");
        return elements;
    }

    public static void wait(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (Exception e) {
            logger.error("Exception occurred while waiting", e);
        }
    }

}