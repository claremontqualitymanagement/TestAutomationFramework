package se.claremont.autotest.guidriverpluginstructure.websupport.webdrivergluecode;

import io.github.bonigarcia.wdm.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.MarionetteDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import se.claremont.autotest.common.*;
import se.claremont.autotest.filetestingsupport.FileTester;
import se.claremont.autotest.support.SupportMethods;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
        PHANTOMJS,
        MARIONETTE,
        EDGE,
        OPERA,
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
        WebDriver driver = null;
        if(testCase == null) { testCase = new TestCase(null, "dummy");}
        switch (webBrowserType) {
            case CHROME:
                testCase.log(LogLevel.DEBUG, "Attempting to initialize Chrome driver.");
                try {
                    long startTime = System.currentTimeMillis();
                    ChromeDriverManager.getInstance().setup();
                    driver = new ChromeDriver();
                    testCase.log(LogLevel.EXECUTED, "Creating a Chrome session took " + (System.currentTimeMillis() - startTime) + " milliseconds.");
                } catch (Exception e) {
                    testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not initialize Chrome driver. Error message: " + e.getMessage());
                }
                break;
            case INTERNET_EXPLORER:
                testCase.log(LogLevel.DEBUG, "Attempting to get hold of drivers for Internet Explorer.");
                try {
                    long startTime = System.currentTimeMillis();
                    InternetExplorerDriverManager.getInstance().setup();
                    driver = new InternetExplorerDriver();
                    testCase.log(LogLevel.EXECUTED, "Creating an Internet Explorer session took " + (System.currentTimeMillis() - startTime) + " milliseconds.");
                } catch (Exception e) {
                    testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not start Internet Explorer driver. Error message: " + e.getMessage());
                }
                break;
            case OPERA:
                testCase.log(LogLevel.DEBUG, "Attempting to get hold of drivers for Internet Explorer.");
                try {
                    long startTime = System.currentTimeMillis();
                    OperaDriverManager.getInstance().setup();
                    driver = new OperaDriver();
                    testCase.log(LogLevel.EXECUTED, "Creating an Opera session took " + (System.currentTimeMillis() - startTime) + " milliseconds.");
                } catch (Exception e) {
                    testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not start Opera driver. Error message: " + e.getMessage());
                }
                break;
            case EDGE:
                testCase.log(LogLevel.DEBUG, "Attempting to get hold of drivers for the Edge browser.");
                try {
                    long startTime = System.currentTimeMillis();
                    EdgeDriverManager.getInstance().setup();
                    driver = new EdgeDriver();
                    testCase.log(LogLevel.EXECUTED, "Creating an Edge browser session took " + (System.currentTimeMillis() - startTime) + " milliseconds.");
                } catch (Exception e) {
                    testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not start Edge browser driver. Error message: " + e.getMessage());
                }
                break;
            case MARIONETTE:
                testCase.log(LogLevel.DEBUG, "Attempting to get hold of drivers for Marionette.");
                try {
                    long startTime = System.currentTimeMillis();
                    MarionetteDriverManager.getInstance().setup();
                    driver = new MarionetteDriver();
                    testCase.log(LogLevel.EXECUTED, "Creating a Marionette session took " + (System.currentTimeMillis() - startTime) + " milliseconds.");
                } catch (Exception e) {
                    testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not start Marionette driver. Error message: " + e.getMessage());
                }
                break;
            case FIREFOX:
                scanComputerForBrowsersAndRegisterTheirLocations();
                System.setProperty("webdriver.firefox.bin", TestRun.settings.getValue(Settings.SettingParameters.FIREFOX_PATH_TO_BROWSER_EXE));
                testCase.log(LogLevel.INFO, "Initializing Firefox driver '" + System.getProperty("webdriver.firefox.bin") + "'.");
                try {
                    driver = new FirefoxDriver();
                } catch (Exception e) {
                    testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not initialize driver '" + SupportMethods.enumCapitalNameToFriendlyString(webBrowserType.toString()) + "'. " +
                            "Expected to find Firefox driver at '" + TestRun.settings.getValue(Settings.SettingParameters.FIREFOX_PATH_TO_BROWSER_EXE) + "' as stated by the 'firefoxPathToBrowserExe' parameter in settings.");
                }
                break;
            case REMOTE_CHROME:
                testCase.log(LogLevel.INFO, "Initializing remote Chrome driver.");
                //driver = new RemoteWebDriver()
                break;
            case PHANTOMJS:
                testCase.log(LogLevel.DEBUG, "Attempting to get hold of drivers for PhantomJS.");
                long startTime = System.currentTimeMillis();
                try {
                    //PhantomJsDriverManager.getInstance().setup();
                    driver = new PhantomJSDriver();
                } catch (Exception e) {
                    testCase.log(LogLevel.INFO, "Could not start PhantomJS driver through WebDriverManager libraries. Error message: " + e.getMessage() + SupportMethods.LF + "Attempting harddisk scan for phantomjs.exe.");
                    try{
                        System.setProperty("phantomjs.binary.path", CliTestRunner.testRun.settings.getValue(Settings.SettingParameters.PHANTOMJS_PATH_TO_EXE));
                        driver = new PhantomJSDriver();
                        testCase.log(LogLevel.EXECUTED, "Creating a PhantomJS session took " + (System.currentTimeMillis() - startTime) + " milliseconds.");
                    }catch (Exception ex){
                        testCase.log(LogLevel.INFO, "Could not load PhantomJS driver from Settings variable '" + Settings.SettingParameters.PHANTOMJS_PATH_TO_EXE.toString() + "', stating path '" + CliTestRunner.testRun.settings.getValue(Settings.SettingParameters.PHANTOMJS_PATH_TO_EXE) + "'. Initiating a harddisk scan for file.");
                        for(File browser : scanComputerForBrowsersAndRegisterTheirLocations()){
                            if(browser.getName().equals("PhantomJS.exe")){
                                CliTestRunner.testRun.settings.setValue(Settings.SettingParameters.PHANTOMJS_PATH_TO_EXE, browser.getAbsolutePath());
                                System.setProperty("phantomjs.binary.path", browser.getAbsolutePath());
                                testCase.log(LogLevel.DEBUG, "Found PhantomJS.exe at '" + browser.getAbsolutePath() + "'.");
                                driver = new PhantomJSDriver();
                                break;
                            }
                        }
                    }
                }
                if(driver != null){
                    testCase.log(LogLevel.EXECUTED, "Got hold of PhantomJS driver after " + (System.currentTimeMillis() - startTime) + " milliseconds.");
                } else {
                    testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not get hold of PhantomJS driver.");
                }
                break;
            default:
                testCase.log(LogLevel.INFO, "Initializing Firefox driver (default driver) '" + System.getProperty("webdriver.firefox.bin") + "'.");
                driver = new FirefoxDriver();
                break;

        }
        return driver;

    }

    public static List<File> scanComputerForBrowsersAndRegisterTheirLocations(){
        HashMap<String, String> browsers = new HashMap<>();
        List<String> fileNamesToFind = new ArrayList<>();
        fileNamesToFind.add("firefox.exe");
        fileNamesToFind.add("iexplore.exe");
        fileNamesToFind.add("chrome.exe");
        fileNamesToFind.add("phantomjs.exe");
        List<File> matchingFiles = FileTester.searchForSpecificFiles(new File("C:\\Program Files (x86)\\"), fileNamesToFind);
        return matchingFiles;
    }
}
