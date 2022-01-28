package org.ea.stepdef;

import org.ea.config.WebDriverFactory;
import org.openqa.selenium.WebDriver;

public class BaseSteps {

    protected WebDriver driver = WebDriverFactory.getInstance().getWebDriver();

}
