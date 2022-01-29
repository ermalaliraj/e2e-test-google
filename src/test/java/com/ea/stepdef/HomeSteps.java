package com.ea.stepdef;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import com.ea.config.DriverFactory;
import com.ea.util.Util;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

import static com.ea.util.CommonKeyboard.clickEscButton;
import static com.ea.util.ElementUtil.elementClick;
import static com.ea.util.ElementUtil.elementChangeValue;
import static com.ea.util.ElementUtil.getElements;
import static com.ea.util.ElementUtil.startApp;
import static com.ea.util.ElementUtil.waitForElementTobePresent;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
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
        startApp(DriverFactory.getInstance().getWebDriver(), appName);
        log.debug("Navigated to {} application", appName);
    }

    @Then("Accept cookie browser")
    public void acceptCookie() {
        WebElement element = waitForElementTobePresent(driver, XPATH_BUTTON_OK_COOKIE);
        elementClick(driver, element);
    }

    @Then("Home page is displayed")
    public void homePageDisplayed() {
        WebElement element = waitForElementTobePresent(driver, XPATH_LOGO);
        assertNotNull(element, "Element " + XPATH_LOGO + " not found ");
    }

    @When("Insert {string} in the search input")
    public void search(String searchValue) {
        WebElement element = waitForElementTobePresent(driver, XPATH_INPUT_SEARCH);
        elementChangeValue(element, searchValue);
    }

    @Then("Click search button")
    public void searchButton() {
        Util.wait(10);
        clickEscButton(driver);
        WebElement element = waitForElementTobePresent(driver, XPATH_BUTTON_SEARCH);
        elementClick(driver, element);
    }

    @Then("Search result will be generated with {int} rows")
    public void validateSearch(int rows) {
        List<WebElement> elements = getElements(driver, XPATH_SEARCH_RESULT);
        assertNotNull(elements, "Elements " + XPATH_SEARCH_RESULT + " not found ");
        assertTrue(elements.size() > rows);

        elements = getElements(driver, XPATH_SEARCH_PAGINATOR);
        assertNotNull(elements, "Elements " + XPATH_SEARCH_PAGINATOR + " not found ");
        assertEquals(elements.size(), 12);
    }

    @Then("Close the browser")
    public void closeTheBrowser() {
        DriverFactory.getInstance().getWebDriver().quit();
    }

}