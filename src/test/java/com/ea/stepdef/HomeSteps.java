package com.ea.stepdef;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import com.ea.config.WebDriverFactory;
import com.ea.util.Util;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

import static com.ea.util.CommonKeyboard.clickEscButton;
import static com.ea.util.ElementUtil.startApp;
import static com.ea.util.Util.checkElement;
import static com.ea.util.Util.checkElementAndChangeValue;
import static com.ea.util.Util.checkElementAndClick;
import static com.ea.util.Util.checkElements;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class HomeSteps extends BaseSteps {

    private static final Logger log = LoggerFactory.getLogger(HomeSteps.class);

    public static final By XPATH_BUTTON_OK_COOKIE = By.xpath("//button[@id='L2AGLb']");
    public static final By XPATH_LOGO = By.xpath("//img[@class='lnXdpd']");
    public static final By XPATH_INPUT_SEARCH = By.xpath("//input");
    public static final By XPATH_BUTTON_SEARCH = By.xpath("(//input[@value='Cerca con Google'])[2]");
    public static final By XPATH_SEARCH_RESULT = By.xpath("//div[@class='g tF2Cxc']");
    public static final By XPATH_SEARCH_PAGINATOR = By.xpath("//table[@class='AaVjTc']/tbody/tr/td");

    @Given("Navigate to {string} application")
    public void invokeApp(String appName) {
        startApp(WebDriverFactory.getInstance().getWebDriver(), appName);
        log.debug("Navigated to {} application", appName);
    }

    @Then("Accept cookie browser")
    public void acceptCookie() {
        checkElementAndClick(driver, XPATH_BUTTON_OK_COOKIE);
    }

    @Then("Home page is displayed")
    public void homePageDisplayed() {
        checkElement(driver, XPATH_LOGO);
    }

    @When("Insert {string} in the search input")
    public void search(String searchValue) {
        checkElementAndChangeValue(driver, XPATH_INPUT_SEARCH, searchValue);
    }

    @Then("Click search button")
    public void searchButton() {
        Util.wait(10);
        clickEscButton(driver);
        checkElementAndClick(driver, XPATH_BUTTON_SEARCH);
    }

    @Then("Search result will be generated with {int} rows")
    public void validateSearch(int rows) {
        List<WebElement> elements = checkElements(driver, XPATH_SEARCH_RESULT);
        assertTrue(elements.size() > rows);
        elements = checkElements(driver, XPATH_SEARCH_PAGINATOR);
        assertEquals(elements.size(), 12);
    }

    @Then("Close the browser")
    public void closeTheBrowser() {
        WebDriverFactory.getInstance().getWebDriver().quit();
    }

}