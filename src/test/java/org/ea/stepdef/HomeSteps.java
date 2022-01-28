package org.ea.stepdef;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.ea.util.Common;
import org.ea.config.WebDriverFactory;
import org.ea.util.E2eUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

public class HomeSteps extends BaseSteps {

    private static final Logger log = LoggerFactory.getLogger(HomeSteps.class);

    public static final By XPATH_BUTTON_OK_COOKIE = By.xpath("//button[@id='L2AGLb']");
    public static final By XPATH_LOGO = By.xpath("//img[@class='lnXdpd']");
    public static final By XPATH_INPUT_SEARCH = By.xpath("//input");
    public static final By XPATH_BUTTON_SEARCH = By.xpath("(//input[@value='Cerca con Google'])[2]");

    @Given("^navigate to \"([^\"]*)\" application")
    public void invokeApp(String appName) {
        Common.startApp(WebDriverFactory.getInstance().getWebDriver(), appName);
        log.debug("Navigated to {} application", appName);
    }

    @Then("Home page is displayed")
    public void homePageDisplayed() {
        checkAndClickElement(driver, XPATH_BUTTON_OK_COOKIE);
        checkForElement(driver, XPATH_LOGO);
        log.debug("All elements found in Homepage");
    }

    @When("^Search \"([^\"]*)\"")
    public void search(String searchValue) {
        checkAndPopulateElement(driver, XPATH_INPUT_SEARCH, searchValue);
        E2eUtil.wait(10);
        Common.clickEsc(driver);
        checkAndClickElement(driver, XPATH_BUTTON_SEARCH);
        log.debug("All elements found in Homepage");
    }

    @Then("Validate search result")
    public void validateSearch() {
        log.debug("validateSearch");
    }

    @Then("Close the browser")
    public void closeTheBrowser() {
        WebDriverFactory.getInstance().getWebDriver().quit();
    }

    private void checkAndClickElement(WebDriver driver, By xPath) {
        WebElement element = checkForElement(driver, xPath);
        Common.elementClick(driver, element);
    }

    private WebElement checkForElement(WebDriver driver, By xpathButton) {
        WebElement element = Common.waitForElementTobePresent(driver, xpathButton);
        Assert.assertNotNull(element, "Element " + xpathButton + " not found ");
        return element;
    }

    private void checkAndPopulateElement(WebDriver driver, By xPath, String newValue) {
        WebElement element = checkForElement(driver, xPath);
        Common.elementChangeValue(element, newValue);
    }

}