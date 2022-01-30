package com.ea.stepdef;

import com.ea.page.HomePage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertEquals;

public class HomeSteps {

    private static final Logger log = LoggerFactory.getLogger(HomeSteps.class);

    public HomePage homePage = new HomePage();

    @Given("Navigate to {string} application")
    public void invokeApp(String appName) {
        homePage.startApp(appName);
        log.debug("Navigated to {} application", appName);
    }

    @Then("Accept cookie browser")
    public void acceptCookie() {
        homePage.acceptCookie();
    }

    @Then("Home page is displayed")
    public void homePageDisplayed() {
        homePage.homePageDisplayed();
    }

    @When("Insert {string} in the search input")
    public void search(String searchValue) {
        homePage.search(searchValue);
    }

    @Then("Click search button")
    public void searchButton() {
        homePage.searchButton();
    }

    @Then("Search result will be generated with {int} rows")
    public void checkSearchResult(int rows) {
        List<WebElement> elements = homePage.getSearchResult();
        assertNotNull(elements);
        assertTrue(elements.size() > rows);
    }

    @And("Search result will contain {int} pages in the footer")
    public void checkSearchResultPageNumbers(int pages) {
        List<WebElement> elements = homePage.getSearchPaginator();
        assertNotNull(elements);
        assertEquals(elements.size(), pages);
    }

//    @Then("Close the browser")
//    public void closeTheBrowser() {
//        homePage.closeTheBrowser();
//    }

}