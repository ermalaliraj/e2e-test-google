package com.ea.testRunner;

import com.ea.listener.SuiteListener;
import com.ea.listener.TestListener;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.Listeners;

@CucumberOptions(
        features = {"classpath:features/Search.feature"}
        , plugin = {"pretty",
                    "html:target/cucumber/report.html",
                    "json:target/cucumber/reports.json",
                    "junit:target/junit-reports/reports.xml",
                    "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:",
                    "rerun:target/results/failed-reports/failedTestCases.txt"}
        , glue = {"com/ea/stepdef"}
        , monochrome = true
)

@Listeners({SuiteListener.class, TestListener.class})
public class E2e_TestRunner extends AbstractTestNGCucumberTests {
}