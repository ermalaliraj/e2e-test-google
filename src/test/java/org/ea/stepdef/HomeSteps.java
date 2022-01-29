package org.ea.stepdef;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.ea.config.WebDriverFactory;
import org.ea.util.Common;
import org.ea.util.Util;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.ea.util.CommonKeyboard.clickEscButton;
import static org.ea.util.Util.*;

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
        checkElementAndClick(driver, XPATH_BUTTON_OK_COOKIE);
        checkElement(driver, XPATH_LOGO);
        log.debug("All elements found in Homepage");
    }

    @When("^Search \"([^\"]*)\"")
    public void search(String searchValue) {
        checkElementAndChangeValue(driver, XPATH_INPUT_SEARCH, searchValue);
        Util.wait(10);
        clickEscButton(driver);
        checkElementAndClick(driver, XPATH_BUTTON_SEARCH);
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

}