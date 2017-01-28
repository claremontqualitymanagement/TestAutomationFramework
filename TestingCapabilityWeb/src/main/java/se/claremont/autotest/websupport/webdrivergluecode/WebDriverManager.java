package se.claremont.autotest.websupport.webdrivergluecode;

import io.github.bonigarcia.wdm.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.MarionetteDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import se.claremont.autotest.common.logging.LogLevel;
import se.claremont.autotest.common.support.SupportMethods;
import se.claremont.autotest.common.support.Utils;
import se.claremont.autotest.common.testcase.TestCase;
import se.claremont.autotest.common.testrun.Settings;
import se.claremont.autotest.common.testrun.TestRun;
import se.claremont.autotest.filetestingsupport.FileTester;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Settings and management of WebDriver browser interfaces.
 *
 * Created by jordam on 2016-09-13.
 */
public class WebDriverManager {
    private TestCase testCase;

    public WebDriverManager(TestCase testCase){
        this.testCase = testCase;
    }

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
     * @return Appropriate implementation of WebDriver interface
     */
    public WebDriver initializeWebDriver(WebBrowserType webBrowserType){
        testCase.log(LogLevel.DEBUG, "Attempting to initializeIfNotInitialized WebDriver for web browser type '" +  webBrowserType.toString() + "'.");
        WebDriver driver = null;
        if(testCase == null) { testCase = new TestCase(null, "dummy");}
        switch (webBrowserType) {
            case CHROME:
                driver = new ChromeBrowser(testCase).setup();
                break;
            case INTERNET_EXPLORER:
                driver = new InternetExplorerBrowser(testCase).setup();
                break;
            case OPERA:
                driver = new OperaBrowser(testCase).setup();
                break;
            case EDGE:
                driver = new EdgeBrowser(testCase).setup();
                break;
            case MARIONETTE:
                driver = new MarionetteBrowser(testCase).setup();
                break;
            case FIREFOX:
                driver = new FirefoxBrowser(testCase).setup();
                break;
            case REMOTE_CHROME:
                testCase.log(LogLevel.INFO, "Initializing remote Chrome driver - not yet implemented.");
                //driver = new RemoteWebDriver()
                break;
            case PHANTOMJS:
                driver = new PhantomJsBrowser(testCase).setup();
                break;
            default:
                testCase.log(LogLevel.INFO, "Initializing Firefox driver (default driver) '" + System.getProperty("webdriver.firefox.bin") + "'.");
                driver = new FirefoxDriver();
                break;

        }
        if(driver == null){
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not initializeIfNotInitialized driver for browser '" + webBrowserType.toString() + "'.");
            logIdentifiedLocalBrowsersFromFileScan();
        }else{
            testCase.log(LogLevel.INFO, "Driver for browser '" + webBrowserType.toString() + "' achieved.");
        }
        return driver;

    }

    private void logIdentifiedLocalBrowsersFromFileScan(){
        List<String> fileNamesToFind = new ArrayList<>();
        fileNamesToFind.add("firefox.exe");
        fileNamesToFind.add("iexplore.exe");
        fileNamesToFind.add("chrome.exe");
        fileNamesToFind.add("phantomjs.exe");
        testCase.log(LogLevel.EXECUTED, "Scanning machine hard drive for browsers named '" + String.join("', '", fileNamesToFind) + "'.");
        List<File> browsers = scanComputerForBrowsersAndRegisterTheirLocations(fileNamesToFind);
        List<String> browserPaths = new ArrayList<>();
        for(File browser : browsers){
            browserPaths.add(browser.getAbsolutePath());
        }
        testCase.logDifferentlyToTextLogAndHtmlLog(LogLevel.INFO, "Identified local browsers on execution machine:" + SupportMethods.LF + "'" + String.join("'" + SupportMethods.LF + "'", browserPaths) + "'",
                "Identified local browsers on execution machine:<br>" + SupportMethods.LF + "'" + String.join("'<br>" + SupportMethods.LF + "'", browserPaths) + "'<br>");
    }

    private static List<File> scanComputerForBrowsersAndRegisterTheirLocations(List<String> fileNames){
        List<File> matchingFiles = null;
        if( Utils.getInstance().amIWindowsOS() )
            matchingFiles = FileTester.searchForSpecificFiles(new File("C:\\"), fileNames);
        else if( Utils.getInstance().amIMacOS() || Utils.getInstance().amILinuxOS() )
            matchingFiles = FileTester.searchForSpecificFiles(new File("/"), fileNames);
        return matchingFiles;
    }

    abstract class Browser{
        final TestCase testCase;
        Browser(TestCase testCase){
            this.testCase = testCase;
        }

        @SuppressWarnings("unused")
        public WebDriver setup(){
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Browser is an abstract class and the setup of this is not intended.");
            return null;
        }
    }

    class ChromeBrowser extends Browser{

        ChromeBrowser(TestCase testCase){
            super(testCase);
        }

        public @Override WebDriver setup(){
            WebDriver driver = null;
            testCase.log(LogLevel.DEBUG, "Attempting to initializeIfNotInitialized Chrome driver.");
            try {
                long startTime = System.currentTimeMillis();
                ChromeDriverManager.getInstance().setup();
                ChromeDriverService service =
                        new ChromeDriverService.Builder().withWhitelistedIps("127.0.0.1").withSilent(true).build();
                driver = new ChromeDriver(service);
                testCase.log(LogLevel.EXECUTED, "Creating a Chrome session took " + (System.currentTimeMillis() - startTime) + " milliseconds.");
            } catch (Exception e) {
                testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not initializeIfNotInitialized Chrome driver. Error message: " + e.getMessage());
            }
            return driver;
        }

    }

    class FirefoxBrowser extends Browser{
        FirefoxBrowser(TestCase testCase){
            super(testCase);
        }

        public @Override WebDriver setup(){
            WebDriver driver = null;
            //System.setProperty("webdriver.firefox.bin", TestRun.settings.getValue(Settings.SettingParameters.FIREFOX_PATH_TO_BROWSER_EXE));
            testCase.log(LogLevel.INFO, "Initializing Firefox driver '" + System.getProperty("webdriver.firefox.bin") + "'.");
            try {
                driver = new FirefoxDriver();
            } catch (Exception e) {
                testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not initializeIfNotInitialized Firefox driver. " +
                        "Expected to find Firefox driver at '" + TestRun.settings.getValue(Settings.SettingParameters.FIREFOX_PATH_TO_BROWSER_EXE) + "' as stated by the 'firefoxPathToBrowserExe' parameter in settings.");
                logIdentifiedLocalBrowsersFromFileScan();
            }
            return driver;
        }
    }

    class InternetExplorerBrowser extends Browser{
        InternetExplorerBrowser(TestCase testCase){ super(testCase);}

        public @Override WebDriver setup(){
            WebDriver driver = null;
            testCase.log(LogLevel.DEBUG, "Attempting to get hold of drivers for Internet Explorer.");
            try {
                long startTime = System.currentTimeMillis();
                InternetExplorerDriverManager.getInstance().setup();
                driver = new InternetExplorerDriver();
                testCase.log(LogLevel.EXECUTED, "Creating an Internet Explorer session took " + (System.currentTimeMillis() - startTime) + " milliseconds.");
            } catch (Exception e) {
                testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not start Internet Explorer driver. Error message: " + e.getMessage());
            }
            return driver;
        }
    }

    class EdgeBrowser extends Browser{
        EdgeBrowser(TestCase testCase) {super(testCase);}

        public WebDriver setup(){
            WebDriver driver = null;
            testCase.log(LogLevel.DEBUG, "Attempting to get hold of drivers for the Edge browser.");
            try {
                long startTime = System.currentTimeMillis();
                EdgeDriverManager.getInstance().setup();
                driver = new EdgeDriver();
                testCase.log(LogLevel.EXECUTED, "Creating an Edge browser session took " + (System.currentTimeMillis() - startTime) + " milliseconds.");
            } catch (Exception e) {
                testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not start Edge browser driver. Error message: " + e.getMessage());
            }
            return driver;
        }
    }

    class OperaBrowser extends Browser{
        OperaBrowser(TestCase testCase) { super(testCase);}

        public WebDriver setup(){
            WebDriver driver = null;
            testCase.log(LogLevel.DEBUG, "Attempting to get hold of drivers for Internet Explorer.");
            try {
                long startTime = System.currentTimeMillis();
                OperaDriverManager.getInstance().setup();
                driver = new OperaDriver();
                testCase.log(LogLevel.EXECUTED, "Creating an Opera session took " + (System.currentTimeMillis() - startTime) + " milliseconds.");
            } catch (Exception e) {
                testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not start Opera driver. Error message: " + e.getMessage());
            }
            return driver;

        }
    }

    class MarionetteBrowser extends Browser{
        MarionetteBrowser (TestCase testCase) { super(testCase);}

        public WebDriver setup(){
            WebDriver driver = null;
            testCase.log(LogLevel.DEBUG, "Attempting to get hold of drivers for Marionette.");
            try {
                long startTime = System.currentTimeMillis();
                MarionetteDriverManager.getInstance().setup();
                //noinspection deprecation
                driver = new MarionetteDriver();
                testCase.log(LogLevel.EXECUTED, "Creating a Marionette session took " + (System.currentTimeMillis() - startTime) + " milliseconds.");
            } catch (Exception e) {
                testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not start Marionette driver. Error message: " + e.getMessage());
            }
            return driver;
        }
    }

    class PhantomJsBrowser  extends Browser{
        PhantomJsBrowser(TestCase testCase) { super(testCase);}

        public WebDriver setup(){
            WebDriver driver = null;
            testCase.log(LogLevel.DEBUG, "Attempting to get hold of drivers for PhantomJS.");
            long startTime = System.currentTimeMillis();
            try {
                PhantomJsDriverManager.getInstance().setup();
                driver = new PhantomJSDriver();
            } catch (Exception e) {
                testCase.log(LogLevel.INFO, "Could not start PhantomJS driver through WebDriverManager libraries. Error message: " + e.getMessage() + SupportMethods.LF + "Attempting disk scan for phantomjs.exe.");
                try{
                    System.setProperty("phantomjs.binary.path", TestRun.settings.getValue(Settings.SettingParameters.PHANTOMJS_PATH_TO_EXE));
                    driver = new PhantomJSDriver();
                    testCase.log(LogLevel.EXECUTED, "Creating a PhantomJS session took " + (System.currentTimeMillis() - startTime) + " milliseconds.");
                }catch (Exception ex){
                    testCase.log(LogLevel.INFO, "Could not load PhantomJS driver from Settings variable '" + Settings.SettingParameters.PHANTOMJS_PATH_TO_EXE.toString() + "', stating path '" + TestRun.settings.getValue(Settings.SettingParameters.PHANTOMJS_PATH_TO_EXE) + "'. Initiating a disk scan for file.");
                }
            }
            if(driver != null){
                testCase.log(LogLevel.EXECUTED, "Got hold of PhantomJS driver after " + (System.currentTimeMillis() - startTime) + " milliseconds.");
            } else {
                testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not get hold of PhantomJS driver.");
            }
            return driver;
        }
    }
}
