package com.ea.stepdef;

import com.ea.config.DriverFactory;
import org.openqa.selenium.WebDriver;

public class BaseSteps {

    protected WebDriver driver = DriverFactory.getInstance().getWebDriver();

}
