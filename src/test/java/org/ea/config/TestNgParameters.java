package org.ea.config;

import io.cucumber.java.Scenario;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

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

    @Override
    public String toString() {
        ReflectionToStringBuilder rtsb = new ReflectionToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
        rtsb.setExcludeNullValues(false);
        return rtsb.toString();
    }
}