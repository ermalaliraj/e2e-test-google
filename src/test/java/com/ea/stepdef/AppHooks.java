package com.ea.stepdef;

import com.ea.factory.DriverFactory;
import com.ea.config.TestContext;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.Reporter;

import static com.ea.util.ElementUtil.takeScreenshot;

/**
 * @Before and @After each Step
 */
public class AppHooks {

    private static final Logger log = LoggerFactory.getLogger(AppHooks.class);

    @Before
    public void startScenario(Scenario scenario) {
        log.debug("Start Scenario");
        TestContext.getInstance().setScenario(scenario);
        DriverFactory.getInstance().setWebDriver();
    }

    @After
    public void closeScenario(Scenario scenario) {
//        Scenario scenario TestContext.getInstance().getScenario();
        if (scenario.isFailed()) {
            log.debug("Closing Scenario with FAILURES");
            Reporter.getCurrentTestResult().setStatus(ITestResult.FAILURE);
            String screenshotName = scenario.getName().replaceAll(" ", "_");

            takeScreenshot(DriverFactory.getInstance().getWebDriver(), screenshotName);
        } else {
            log.debug("Closing Scenario with SUCCESS");
        }
        DriverFactory.getInstance().getWebDriver().quit();
        TestContext.getInstance().getScenario().log("Close Browser");
    }
}
