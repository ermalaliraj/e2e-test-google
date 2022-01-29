package com.ea.config;

import io.cucumber.java.Scenario;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

@ToString
@Data
public class TestNgParameters {
    @Getter(lazy = true)
    private static final TestNgParameters instance = new TestNgParameters();
    private String screenshotPath;
    private String environment;
    private String browser;
    private String mode;
    private Scenario scenario;

    private TestNgParameters(){
        environment = "local";
    }

    public void reset() {
        screenshotPath = null;
        scenario = null;
    }
}