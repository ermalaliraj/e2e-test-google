package org.ea.config;

import lombok.NoArgsConstructor;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@NoArgsConstructor
public class WebDriverFactory {

    private static final Logger log = LoggerFactory.getLogger(WebDriverFactory.class);

    public static final String DRIVER_CHROME = "chrome";
    public static final String DRIVER_FIREFOX = "firefox";
    public static final String DRIVER_IE = "internetexplorer";
    public static final String DRIVER_EDGE = "edge";
    public static final int TIME_OUT = 10;
    public static final int POLLING_TIME = 1;

    public static final String DRIVER_MODE_LOCAL = "local";
    public static final String DRIVER_MODE_REMOTE = "remote";

    public static final String DRIVER_DOWNLOAD_DIR_REMOTE = "path.remote.download";
    public static final String DRIVER_DOWNLOAD_DIR_LOCAL = "path.local.download";

    private String browser;
    private WebDriver driver;
    private static WebDriverFactory instance;
    private static Configuration config;

    public static WebDriverFactory getInstance() {
        if (instance == null) {
            instance = new WebDriverFactory();
            instance.setBrowser(TestNgParameters.getInstance().getBrowser());
            config = new Configuration();
        }
        return instance;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public WebDriver getWebDriver() {
        return driver;
    }

    public void setWebDriver() {
        final String exeMode = TestNgParameters.getInstance().getMode();
        final String gridUrl = config.getProperty("grid.url");
        final boolean remote = DRIVER_MODE_REMOTE.equals(exeMode);
        remoteDriver(browser, gridUrl, remote);
    }

    public void remoteDriver(String browser, String gridUrl, boolean remote) {
        try {
            DesiredCapabilities capability;
            if (DRIVER_FIREFOX.equals(browser)) {
                capability = DesiredCapabilities.firefox();
                if (remote) {
                    capability.setBrowserName(DRIVER_FIREFOX);
                    capability.setPlatform(Platform.ANY);
                    driver = new RemoteWebDriver(new URL(gridUrl), capability);
                    driver.manage().timeouts().implicitlyWait(TIME_OUT, TimeUnit.SECONDS);
                    driver.manage().window().maximize();
                    driver.manage().deleteAllCookies();
                }
            } else if (DRIVER_IE.equals(browser)) {
                capability = DesiredCapabilities.internetExplorer();
                if (remote) {
                    capability.setBrowserName("internet explorer");
                    capability.setPlatform(Platform.ANY);
                    driver = new RemoteWebDriver(new URL(gridUrl), capability);
                    driver.manage().timeouts().implicitlyWait(TIME_OUT, TimeUnit.SECONDS);
                    driver.manage().window().maximize();
                    driver.manage().deleteAllCookies();
                }
            } else if (DRIVER_EDGE.equals(browser)) {
                capability = DesiredCapabilities.edge();
                if (remote) {
                    capability.setBrowserName("MicrosoftEdge");
                    capability.setPlatform(Platform.ANY);
                    driver = new RemoteWebDriver(new URL(gridUrl), capability);
                    driver.manage().timeouts().implicitlyWait(TIME_OUT, TimeUnit.SECONDS);
                    driver.manage().window().maximize();
                    driver.manage().deleteAllCookies();
                }
            } else {
                capability = DesiredCapabilities.chrome();
                capability.setBrowserName(DRIVER_CHROME);
                capability.setPlatform(Platform.ANY);
                capability.setCapability("ignoreZoomSetting", true);
                ChromeOptions options = getChromeOptions(remote);
                options.merge(capability);
                if (remote) {
                    driver = new RemoteWebDriver(new URL(gridUrl), options);
                    driver.manage().timeouts().implicitlyWait(TIME_OUT, TimeUnit.SECONDS);
                    driver.manage().window().maximize();
                    driver.manage().deleteAllCookies();
                } else {
                    File configBaseFile = new File(Configuration.DRIVER_PATH);
                    String driverLocalPath = configBaseFile.getAbsolutePath();
                    System.setProperty("webdriver.chrome.driver", driverLocalPath);
                    driver = new ChromeDriver(options);
                    driver.manage().timeouts().implicitlyWait(TIME_OUT, TimeUnit.SECONDS);
                    driver.manage().window().maximize();
                }
            }
        } catch (MalformedURLException e) {
            log.error(e.getMessage(), e);
        }
    }

    private ChromeOptions getChromeOptions(boolean remote) {
        final String downloadDirectory;
        if (remote) {
            downloadDirectory = DRIVER_DOWNLOAD_DIR_REMOTE;
        } else {
            downloadDirectory = DRIVER_DOWNLOAD_DIR_LOCAL;
        }
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("enable-automation");
        options.addArguments("--disable-browser-side-navigation");
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("download.default_directory", config.getProperty(downloadDirectory));
        options.setExperimentalOption("prefs", prefs);
        options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
        return options;
    }

}
