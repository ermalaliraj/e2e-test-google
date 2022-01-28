package org.ea.stepdef;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.ea.util.Common;
import org.ea.config.WebDriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;


public class HomeSteps extends BaseSteps {

    private static final Logger logger = LoggerFactory.getLogger(HomeSteps.class);
    public static final By XPATH_BUTTON_OK_COOKIE = By.xpath("//button[@id='L2AGLb']");
    public static final By XPATH_LOGO = By.xpath("//img[@class='lnXdpd']");
    public static final By XPATH_INPUT_SEARCH = By.xpath("//input");
    public static final By XPATH_BUTTON_SEARCH = By.xpath("//input[@value='Cerca con Google']");


    @Given("^navigate to \"([^\"]*)\" application")
    public void invokeApp(String appName) {
        Common.startApp(WebDriverFactory.getInstance().getWebDriver(), appName);
        logger.debug("Navigated to {} application", appName);
    }

    @Then("Home page is displayed")
    public void homePageDisplayed() {
        WebElement element = getElement(driver, XPATH_BUTTON_OK_COOKIE);
        Common.elementClick(driver, element);

        element = getElement(driver, XPATH_LOGO);
        element = getElement(driver, XPATH_INPUT_SEARCH);
//        Assert.assertEquals(element.getText(), "Cerca con Goole", "no same");

        element = getElement(driver, XPATH_BUTTON_SEARCH);
        Common.elementClick(driver, element);
        logger.debug("All elements found in Homepage");
    }

    private WebElement getElement(WebDriver driver, By xpathButton) {
        WebElement element = Common.waitForElementTobePresent(driver, xpathButton);
        Assert.assertNotNull(element, "Element " + xpathButton + " not found ");
        return element;
    }

    @When("Search student by name")
    public void searchStudentByName() {
        logger.debug("searchStudentByName");
    }

    @Then("Empty search result")
    public void emptySearchResult() {
        logger.debug("emptySearchResult");
    }

    @Then("Close the browser")
    public void closeTheBrowser() {
//        WebDriverFactory.getInstance().getWebDriver().quit();
    }

}