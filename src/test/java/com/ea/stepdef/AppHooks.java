package com.ea.stepdef;

import com.ea.config.TestNgParameters;
import com.ea.config.WebDriverFactory;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.Reporter;

import java.io.File;
import java.io.IOException;

import static com.ea.util.ElementUtil.takeScreenshot;

/**
 * @Before and @After each Step
 */
public class AppHooks {

    private static final Logger log = LoggerFactory.getLogger(AppHooks.class);

    @Before
    public void startScenario(Scenario scenario) {
        log.debug("Start Scenario");
        TestNgParameters.getInstance().setScenario(scenario);
        WebDriverFactory.getInstance().setWebDriver();
    }

    @After
    public void closeScenario() throws IOException {
        if (TestNgParameters.getInstance().getScenario().isFailed()) {
            log.debug("Closing Scenario with FAILURES");
            Reporter.getCurrentTestResult().setStatus(ITestResult.FAILURE);
            takeScreenshot(WebDriverFactory.getInstance().getWebDriver());
            byte[] bytes = FileUtils.readFileToByteArray(new File(TestNgParameters.getInstance().getScreenshotPath()));
            TestNgParameters.getInstance().getScenario().attach(bytes, "image/png", "ErrorScreenshot");
            if (null != WebDriverFactory.getInstance().getWebDriver()) {
                WebDriverFactory.getInstance().getWebDriver().quit();
                TestNgParameters.getInstance().getScenario().log("Close Browser");
            }
        } else {
            log.debug("Closing Scenario with SUCCESS");
            if (null != WebDriverFactory.getInstance().getWebDriver()) {
                WebDriverFactory.getInstance().getWebDriver().quit();
                TestNgParameters.getInstance().getScenario().log("Close Browser");
            }

        }
        TestNgParameters.getInstance().reset();
    }
}