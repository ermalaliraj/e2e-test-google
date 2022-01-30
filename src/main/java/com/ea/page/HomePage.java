package com.ea.page;

import com.ea.util.Util;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

import static com.ea.util.KeyboardUtil.clickEscButton;
import static com.ea.util.ElementUtil.*;

public class HomePage extends BasePage {

    public static final By XPATH_BUTTON_OK_COOKIE = By.xpath("//button[@id='L2AGLb']");
    public static final By XPATH_LOGO = By.xpath("//img[@class='lnXdpd']");
    public static final By XPATH_INPUT_SEARCH = By.xpath("//input");
    public static final By XPATH_BUTTON_SEARCH = By.xpath("(//input[@value='Cerca con Google'])[2]");
    public static final By XPATH_SEARCH_RESULT = By.xpath("//div[@class='g tF2Cxc']");
    public static final By XPATH_SEARCH_PAGINATOR = By.xpath("//table[@class='AaVjTc']/tbody/tr/td");

    public void acceptCookie() {
        WebElement element = waitForElementTobePresent(driver, XPATH_BUTTON_OK_COOKIE);
        elementClick(driver, element);
    }

    public WebElement homePageDisplayed() {
        WebElement element = waitForElementTobePresent(driver, XPATH_LOGO);
        return element;
    }

    public void search(String searchValue) {
        WebElement element = waitForElementTobePresent(driver, XPATH_INPUT_SEARCH);
        elementChangeValue(element, searchValue);
    }

    public void searchButton() {
        Util.wait(10);
        clickEscButton(driver);
        WebElement element = waitForElementTobePresent(driver, XPATH_BUTTON_SEARCH);
        elementClick(driver, element);
    }

    public List<WebElement> getSearchResult() {
        List<WebElement> elements = getElements(driver, XPATH_SEARCH_RESULT);
        return elements;
    }

    public List<WebElement> getSearchPaginator() {
        List<WebElement> elements = getElements(driver, XPATH_SEARCH_PAGINATOR);
        return elements;
    }
}