package org.ea.testRunner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.ea.listener.SuiteListener;
import org.ea.listener.TestListener;
import org.testng.annotations.Listeners;

@CucumberOptions(
        features = {"classpath:features/E2e_Test.feature"}
        , plugin = {"pretty",
                    "html:target/cucumber/report.html",
                    "json:target/cucumber/reports.json",
                    "junit:target/junit-reports/reports.xml",
                    "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:",
                    "rerun:target/results/failed-reports/failedTestCases.txt"}
        , glue = {"org/ea/stepdef"}
        , monochrome = true
)

@Listeners({SuiteListener.class, TestListener.class})
public class E2e_TestRunner extends AbstractTestNGCucumberTests {
}