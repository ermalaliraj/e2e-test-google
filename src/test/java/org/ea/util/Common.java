package org.ea.util;

import org.ea.config.Configuration;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.Reporter;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.ea.config.WebDriverFactory.POLLING_TIME;
import static org.ea.config.WebDriverFactory.TIME_OUT;

public class Common {

    private static final Logger logger = LoggerFactory.getLogger(Common.class);

    private static final String IS_NOT_DISPLAYED = "The following element is NOT displayed: ";
    private static final String ELEMENT_NOTPRESENT = "The following element is NOT present: ";
    private static final String IS_NOT_DISPLAYED_VERIFIED = "The following element is NOT displayed and NOT verified: ";
    private static final String IS_DISPLAYED_AND_CLICKED = "The following element is displayed and clicked: ";
    private static final String DOUBLE_CLICKED = "The following element is double clicked: ";
    private static final String EXCEPTION_MESSGAE = "The exception occured in finding the following element ";
    private static final String EXCEPTION_MESSGAE_ON_FAILURE = "The exception occured during test execution";
    private static final String SCROLL_ELEMENT = "arguments[0].scrollIntoView(true);";

    private static final Configuration config = new Configuration();

    public static void startApp(WebDriver driver, String appName) {
        appName = appName.toLowerCase();
        String appUrl = config.getProperty("appUrl." + appName);
        logger.debug("{} application run on url {}", appName, appUrl);
        if (!driver.getCurrentUrl().trim().contains(appUrl)) {
            driver.get(appUrl);
        }
    }

    // Scroll to the element till the element is visible
    public static void scrollTo(WebDriver driver, By by) {
        try {
            waitForElement(driver, by);
            if (elementExists(driver, by)) {
                WebElement element = driver.findElement(by);
                ((JavascriptExecutor) driver).executeScript(SCROLL_ELEMENT, element);
                logger.info("Page is scrolled to the element {}", element);
            }
        } catch (Exception e) {
            exceptionReport(driver, by, e);
        }
    }

    public static void scrollToElement(WebDriver driver, WebElement ele) {
        try {
            ((JavascriptExecutor) driver).executeScript(SCROLL_ELEMENT, ele);
            logger.info("Page is scrolled to the element {}", ele);
        } catch (Exception e) {
            exceptionReport(driver, e);
        }
    }

/*    public static void scrollToSpecificOffset(WebDriver driver, int x, int y) {
        try {
            ((JavascriptExecutor) driver).executeScript("window.scrollBy(arguments[0], arguments[1]);", x, y);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }*/

    public static void waitForPageLoadComplete(WebDriver driver, int specifiedTimeout) {
        try {
            //driver.manage().timeouts().implicitlyWait(TIME_OUT, TimeUnit.SECONDS);
            WebDriverWait wait = new WebDriverWait(driver, specifiedTimeout);
            //Wait for Javascript to load
            ExpectedCondition<Boolean> jsLoad = driver1 -> "complete".equals(((JavascriptExecutor) driver)
                    .executeScript("return document.readyState").toString());
            wait.until(jsLoad);
        } catch (Exception e) {
            exceptionReport(driver, e);
        }
    }

    // Function to wait for element to appear
    public static void waitForElement(WebDriver driver, By element) {
        try {
            waitForPageLoadComplete(driver, TIME_OUT);
            logger.info("Waiting for {} to display", element);
            E2eUtil.wait(TIME_OUT);
            WebDriverWait wait = new WebDriverWait(driver, TIME_OUT);
            wait.until(ExpectedConditions.presenceOfElementLocated(element));
        } catch (StaleElementReferenceException staleExcption) {
            E2eUtil.wait(2 * TIME_OUT);
            WebDriverWait wait = new WebDriverWait(driver, TIME_OUT);
            wait.until(ExpectedConditions.presenceOfElementLocated(element));
            logger.info("There was a stale element exception, but waited");
        } catch (NoSuchElementException | TimeoutException e) {
            exceptionReport(driver, element, e);
        }
    }

    // Function to wait for element to appear
    public static void waitForElementClickable(WebDriver driver, By element) {
        try {
            driver.manage().timeouts().implicitlyWait(TIME_OUT, TimeUnit.SECONDS);
            WebDriverWait wait = new WebDriverWait(driver, TIME_OUT);
            wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(element)));
        } catch (StaleElementReferenceException staleException) {
            E2eUtil.wait(2000);
            WebDriverWait wait = new WebDriverWait(driver, TIME_OUT);
            wait.until(ExpectedConditions.presenceOfElementLocated(element));
            logger.info("There was a stale element exception, but waited");
        } catch (Exception otherexceptions) {
            exceptionReport(driver, element, otherexceptions);
        }
    }

    public static void elementClick(WebDriver driver, By element) {
        try {
            waitForElementClickable(driver, element);
            if (elementExists(driver, element)) {
                E2eUtil.highlightElement(driver, driver.findElement(element));
                driver.findElement(element).click();
                logger.info(IS_DISPLAYED_AND_CLICKED + element);
            } else {
                throw new NoSuchElementException(IS_NOT_DISPLAYED + element);
            }

        } catch (NoSuchElementException e) {
            exceptionReport(driver, element, e);
        } catch (StaleElementReferenceException staleExcption) {
            E2eUtil.wait(2000);
            WebElement webelement = driver.findElement(element);
            webelement.click();
            logger.info("There was a stale element exception, but clicked");
        } catch (ElementClickInterceptedException elementClickInterceptedException) {
            E2eUtil.scrollAndClick(driver, element);
        }
    }

    public static void elementClick(WebDriver driver, WebElement element) {
        E2eUtil.highlightElement(driver, element);
        element.click();
        logger.info(IS_DISPLAYED_AND_CLICKED + element);
    }

    // Function to verify the element is loaded before entering data
    public static void elementChangeValue(WebDriver driver, By by, String data) {
        try {
            if (elementExists(driver, by)) {
                WebElement element = driver.findElement(by);
                element.clear();
                element.sendKeys(data);
//                logger.info("The data entered at {}", element);
//                E2eUtil.takeSnapShot(driver, "PASS");
            } else {
                throw new NoSuchElementException(IS_NOT_DISPLAYED + by);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static void elementChangeValue(WebElement element, String data) {
        element.clear();
        element.sendKeys(data);
    }

    // Function to keep assertions on particular element.
    public static boolean verifyElement(WebDriver driver, By element) {
        waitForElement(driver, element);
        return verifyUIElement(driver, element);
    }

    public static Boolean verifyUIElement(WebDriver driver, By element) {
        try {
            scrollTo(driver, element);
            if (elementDisplays(driver, element)) {
                E2eUtil.highlightElement(driver, driver.findElement(element));
                return true;
            } else {
                Reporter.getCurrentTestResult().setStatus(ITestResult.FAILURE);
                logger.info(IS_NOT_DISPLAYED_VERIFIED + element);
                E2eUtil.takeSnapShot(driver, "FAIL");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // Element exists or not
    public static Boolean elementExists(WebDriver driver, By element) {
        return elementExistsWithOutwait(driver, element);
    }

    // Element exists or not
    public static Boolean elementExistsWithOutwait(WebDriver driver, By element) {
        // Tries 3 times in case of StaleElementReferenceException
        for (int i = 0; true; i++) {
            try {
                return driver.findElement(element).isEnabled();
            } catch (StaleElementReferenceException e) {
                if (i > 2) {
                    throw e;
                }
            } catch (NoSuchElementException e) {
                return false;
            }
        }
    }

    // Element displayed or not
    public static Boolean elementDisplays(WebDriver driver, By element) {
        try {
            return driver.findElement(element).isDisplayed();
        } catch (Exception e) {
            return false;
        }

    }

    public static void clickEsc(WebDriver driver) {
        Actions actions = new Actions(driver);
        actions.sendKeys(Keys.ESCAPE).build().perform();
    }

    public String getElementText(WebDriver driver, By by) {
        try {
            return driver.findElement(by).getText();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //Get Text of the element
    public String getElementText(WebElement element) {
        try {
            return element.getText();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //Get Element Attribute Value
    public String getElementAttributeValue(WebDriver driver, By by) {
        try {
            return driver.findElement(by).getAttribute("value");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //Get Element Attribute InnerText
    public String getElementAttributeInnerText(WebDriver driver, By by) {
        try {
            return driver.findElement(by).getAttribute("innerText");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public String getElementAttributeInnerText(WebElement ele) {
        try {
            return ele.getAttribute("innerText");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // Take screenshot if scenario fails and stop execution
    private static void exceptionReport(WebDriver driver, By element, Exception e) {
        E2eUtil.takeSnapShot(driver, "FAIL");
        Reporter.getCurrentTestResult().setStatus(ITestResult.FAILURE);
        logger.info(EXCEPTION_MESSGAE + element);
        logger.error(e.getMessage(), e);
        throw new AssertionError(EXCEPTION_MESSGAE, e);
    }

    // Take screenshot if scenario fails and stop execution
    private static void exceptionReport(WebDriver driver, Exception e) {
        E2eUtil.takeSnapShot(driver, "FAIL");
        logger.info(EXCEPTION_MESSGAE_ON_FAILURE);
        logger.error(e.getMessage(), e);
        throw new AssertionError(EXCEPTION_MESSGAE_ON_FAILURE, e);
    }

    public static Boolean verifyIsElementSelected(WebDriver driver, By by) {
        return driver.findElement(by).isSelected();
    }

    public static Boolean verifyElementIsEnabled(WebDriver driver, By element) {
        return elementExistsWithOutwait(driver, element);
    }

    public static void isElementDisabled(WebDriver driver, By element) {
        for (int i = 0; true; i++) {
            try {
                driver.findElement(element).isEnabled();
                return;
            } catch (StaleElementReferenceException e) {
                if (i > 2) {
                    throw e;
                }
            } catch (NoSuchElementException e) {
                return;
            }
        }
    }

    public static void verifyStringContainsText(WebDriver driver, By element) {
        for (int i = 0; true; i++) {
            try {
                driver.findElement(element).getText();
                return;
            } catch (StaleElementReferenceException e) {
                if (i > 2) {
                    throw e;
                }
            } catch (NoSuchElementException e) {
                return;
            }
        }
    }

    public static void doubleClick(WebDriver driver, By element) {
        try {
            waitForElementClickable(driver, element);
            if (elementExists(driver, element)) {
                Actions actions = new Actions(driver);
                WebElement elementLocator = driver.findElement(element);
                actions.moveToElement(elementLocator).doubleClick().build().perform();
                logger.info(DOUBLE_CLICKED + element);
            } else {
                E2eUtil.takeSnapShot(driver, "FAIL");
                throw new NoSuchElementException(ELEMENT_NOTPRESENT + element);
            }

        } catch (NoSuchElementException e) {
            exceptionReport(driver, element, e);
        } catch (StaleElementReferenceException staleExcption) {
            E2eUtil.wait(2000);
            Actions actions = new Actions(driver);
            WebElement elementLocator = driver.findElement(element);
            actions.doubleClick(elementLocator).perform();
            logger.info("There was a stale element exception, but clicked");
        } catch (ElementClickInterceptedException elementClickInterceptedException) {
            E2eUtil.scrollAndDoubleClick(driver, element);
        }
    }

    public static void elementActionClick(WebDriver driver, By locator) {
        WebElement element;
        Actions act;
        Wait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(TIME_OUT))
                .pollingEvery(Duration.ofSeconds(POLLING_TIME))
                .ignoring(NoSuchElementException.class);
        try {
            element = wait.until(driver1 -> driver1.findElement(locator));
        } catch (NoSuchElementException | TimeoutException e) {
            e.printStackTrace();
            throw e;
        }
        if (null != element) {
            try {
                act = new Actions(driver);
                act.moveToElement(element).click().release().build().perform();
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }
    }

    public static boolean selectTextThroughDoubleClick(WebDriver driver, By contextOfTheProposalText) {
        try {
            WebElement element = driver.findElement(contextOfTheProposalText);
            /*Integer width = element.getSize().getWidth();
            Actions act = new Actions(driver);
            act.moveByOffset(element.getLocation().getX() + width, element.getLocation().getY() + width).click();
            act.build().perform();
            act.moveByOffset(width/2,0).clickAndHold().moveByOffset(width,0).release().build().perform();*/
            Actions act = new Actions(driver);
            act.doubleClick(element).build().perform();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static void selectText(WebDriver driver, By locator) {
        try {
            WebElement element = driver.findElement(locator);
            Dimension size = element.getSize();
            int width = size.getWidth();
            Actions action = new Actions(driver);
            action.clickAndHold(element)
                    .moveToElement(element, -width / 2, 0)
                    .build().perform();
            action.release().build().perform();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static WebElement isElementPresent(WebDriver driver, By by) {
        return driver.findElement(by);
    }

    public static WebElement waitForElementTobePresent(WebDriver driver, By by) {
        Wait<WebDriver> wait = new FluentWait<>(driver)
//                .withTimeout(Duration.ofSeconds(TIME_OUT))
                .pollingEvery(Duration.ofSeconds(POLLING_TIME))
                .ignoring(Exception.class);

        return wait.until(driver1 -> driver1.findElement(by));
    }

    public static Boolean waitForElementTobeDisPlayed(WebDriver driver, By by) {
        Wait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(TIME_OUT))
                .pollingEvery(Duration.ofSeconds(POLLING_TIME))
                .ignoring(Exception.class);
        return wait.until(driver1 -> driver1.findElement(by).isDisplayed());
    }

    public static Boolean waitUnTillElementIsNotPresent(WebDriver driver, By by) {
        try {
            driver.manage().timeouts().implicitlyWait(POLLING_TIME, TimeUnit.SECONDS);
            Wait<WebDriver> wait = new FluentWait<>(driver)
                    .withTimeout(Duration.ofSeconds(TIME_OUT))
                    .pollingEvery(Duration.ofSeconds(POLLING_TIME));
            Boolean found = false;
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

    public static Boolean waitElementToBeDisplayedWithInSpecifiedTime(WebDriver driver, By by, int timeOut) {
        boolean bool = false;
        try {
            driver.manage().timeouts().implicitlyWait(timeOut, TimeUnit.SECONDS);
            WebElement ele = driver.findElement(by);
            if (null != ele) {
                bool = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.manage().timeouts().implicitlyWait(TIME_OUT, TimeUnit.SECONDS);
        }
        return bool;
    }

    public List<WebElement> getElementListForComparision(WebDriver driver, By by, int timeOut) {
        List<WebElement> elementList = new ArrayList<>();
        try {
            driver.manage().timeouts().implicitlyWait(timeOut, TimeUnit.SECONDS);
            elementList = driver.findElements(by);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.manage().timeouts().implicitlyWait(TIME_OUT, TimeUnit.SECONDS);
        }
        return elementList;
    }

    public static void setValueToElementAttribute(WebDriver driver, By by, String val) {
        try {
            WebElement ele = driver.findElement(by);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].value=arguments[1];", ele, val);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

/*    public String getElementAttributeInnerHTML(WebDriver driver, By by) {
        try {
            return driver.findElement(by).getAttribute("innerHTML");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }*/

    public static String getValueFromElementAttribute(WebDriver driver, By by, String attribute) {
        try {
            return driver.findElement(by).getAttribute(attribute);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}