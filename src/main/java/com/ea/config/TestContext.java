package com.ea.config;

import io.cucumber.java.Scenario;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

@ToString
@Data
public class TestContext {
    @Getter(lazy = true)
    private static final TestContext instance = new TestContext();

    private Configuration configuration;
    private String screenshotPath;
    private String environment;
    private String mode;
    private Scenario scenario;

    public void setEnvironment(String environment) {
        if (environment == null) {
            environment = "local";
        }
        this.environment = environment;
    }

    public void reset() {
        screenshotPath = null;
        scenario = null;
    }
}