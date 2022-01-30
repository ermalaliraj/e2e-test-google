package com.ea.page;

import com.ea.factory.DriverFactory;
import com.ea.util.ElementUtil;
import org.openqa.selenium.WebDriver;

public class BasePage {

    protected WebDriver driver = DriverFactory.getInstance().getWebDriver();

    public void startApp(String appName) {
        ElementUtil.startApp(driver, appName);
    }

    public void closeTheBrowser() {
        driver.quit();
    }
}
