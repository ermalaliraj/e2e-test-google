package com.ea.stepdef;

import com.ea.config.WebDriverFactory;
import org.openqa.selenium.WebDriver;

public class BaseSteps {

    protected WebDriver driver = WebDriverFactory.getInstance().getWebDriver();

}
