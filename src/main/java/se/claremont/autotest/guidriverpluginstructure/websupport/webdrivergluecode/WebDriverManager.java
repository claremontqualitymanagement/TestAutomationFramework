package se.claremont.autotest.guidriverpluginstructure.websupport.webdrivergluecode;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import se.claremont.autotest.common.LogLevel;
import se.claremont.autotest.common.TestCase;
import se.claremont.autotest.common.TestRun;
import se.claremont.autotest.support.SupportMethods;

/**
 * Settings and management of WebDriver browser interfaces.
 *
 * Created by jordam on 2016-09-13.
 */
public class WebDriverManager {

    /**
     * Supported browser types for web based test execution via Selenium WebDriver.
     */
    @SuppressWarnings("unused")
    public enum WebBrowserType{
        CHROME,
        FIREFOX,
        INTERNET_EXPLORER,
        REMOTE_CHROME,
        REMOTE_FIREFOX,
        REMOTE_INTERNET_EXPLORER
    }

    /**
     * Tries to get a grip of appropriate Browser to return for interaction
     *
     * @param webBrowserType The enum WebBrowserType, stating what browser to try to use
     * @param testCase Current test case. For logging purposes.
     * @return Appropriate implementation of WebDriver interface
     */
    public static WebDriver initializeWebDriver(WebBrowserType webBrowserType, TestCase testCase){
        WebDriver driver;
        System.setProperty("webdriver.chrome.driver", TestRun.settings.getValueForProperty("chromeDriverPathToExe"));
        System.setProperty("webdriver.firefox.bin", TestRun.settings.getValueForProperty("firefoxPathToBrowserExe"));
        switch (webBrowserType){
            case CHROME:
                testCase.log(LogLevel.INFO, "Initializing Chrome driver '" + System.getProperty("webdriver.chrome.driver") + "'.");
                driver = new ChromeDriver();
                break;
            case FIREFOX:
                testCase.log(LogLevel.INFO, "Initializing Firefox driver '" + System.getProperty("webdriver.firefox.bin") + "'.");
                driver = new FirefoxDriver();
                break;
            case REMOTE_CHROME:
                testCase.log(LogLevel.INFO, "Initializing remote Chrome driver.");

                //driver = new RemoteWebDriver()
            default:
                testCase.log(LogLevel.INFO, "Initializing Firefox driver (default driver) '" + System.getProperty("webdriver.firefox.bin") + "'.");
                driver = new FirefoxDriver();
                break;
        }
        if(driver.getWindowHandle() == null){
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not initialize driver '" + SupportMethods.enumCapitalNameToFriendlyString(webBrowserType.toString()) + "'.");
        }
        return driver;
    }
}
