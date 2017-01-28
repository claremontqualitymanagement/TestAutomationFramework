package se.claremont.autotest.websupport.webdrivergluecode;

import org.openqa.selenium.*;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.claremont.autotest.common.guidriverpluginstructure.GuiDriver;
import se.claremont.autotest.common.guidriverpluginstructure.GuiElement;
import se.claremont.autotest.common.logging.LogFolder;
import se.claremont.autotest.common.logging.LogLevel;
import se.claremont.autotest.common.support.StringManagement;
import se.claremont.autotest.common.support.SupportMethods;
import se.claremont.autotest.common.testcase.TestCase;
import se.claremont.autotest.common.testrun.TestRun;
import se.claremont.autotest.dataformats.TableData;
import se.claremont.autotest.swingsupport.robotswinggluecode.RobotSwingInteractionMethods;
import se.claremont.autotest.websupport.DomElement;
import se.claremont.autotest.websupport.LinkCheck;
import se.claremont.autotest.websupport.W3CHtmlValidatorService;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Methods for interaction with web elements in a web page DOM. Utilizes Selenium WebDriver components.
 *
 * Created by jordam on 2016-08-17.
 */
@SuppressWarnings({"SameParameterValue", "WeakerAccess", "unused"})
public class WebInteractionMethods implements GuiDriver {

    private final static Logger logger = LoggerFactory.getLogger( WebInteractionMethods.class );

    @SuppressWarnings("WeakerAccess")
    public WebDriver driver;
    private final TestCase testCase;
    @SuppressWarnings("CanBeFinal")
    private int standardTimeoutInSeconds = 5;
    private class NavigationError extends Exception{}
    private class TextEnteringError extends Exception{}
    private class BrowserClosingError extends Exception{}

    /**
     * Setting up the possibility to interact with web components
     *
     * @param testCase the test case to write testCaseLog messages to
     */
    public WebInteractionMethods(TestCase testCase){
        WebDriverManager.WebBrowserType webBrowser = WebDriverManager.WebBrowserType.CHROME;
        this.testCase = testCase;
        try{
            WebDriverManager webDriverManager = new WebDriverManager(testCase);
            driver = webDriverManager.initializeWebDriver(webBrowser);
            driver.manage().window().maximize();
        }catch (Exception e){
            log(LogLevel.FRAMEWORK_ERROR, "Could not initializeIfNotInitialized driver. Error: " + e.getMessage());
            saveScreenshot(null);
            saveDesktopScreenshot();
            writeRunningProcessListDeviationsSinceTestCaseStart();
            haltFurtherExecution();
        }
    }

    /**
     * Attempts to create a WebDriver instance to use for the specified browser type
     *
     * @param testCase The test case to log to
     * @param browserType The type of browser to try to create driver instance for
     */
    public WebInteractionMethods(TestCase testCase, WebDriverManager.WebBrowserType browserType){
        this.testCase = testCase;
        try{
            WebDriverManager webDriverManager = new WebDriverManager(testCase);
            driver = webDriverManager.initializeWebDriver(browserType);
            driver.manage().window().maximize();
        }catch (Exception e){
            log(LogLevel.FRAMEWORK_ERROR, "Could not initializeIfNotInitialized driver. Error: " + e.getMessage());
            saveScreenshot(null);
            saveDesktopScreenshot();
            writeRunningProcessListDeviationsSinceTestCaseStart();
            haltFurtherExecution();
        }
    }

    public WebInteractionMethods(TestCase testCase, WebDriver driver){
        this.testCase = testCase;
        try{
            this.driver = driver;
            driver.manage().window().maximize();
        }catch (Exception e){
            log(LogLevel.FRAMEWORK_ERROR, "Could not initializeIfNotInitialized driver. Error: " + e.getMessage());
            saveScreenshot(null);
            saveDesktopScreenshot();
            writeRunningProcessListDeviationsSinceTestCaseStart();
            haltFurtherExecution();
        }
    }

    /**
     * Remote WebDriver enabled constructor. Example of usages: BrowserStack.
     *
     * @param testCase The test case to log errors to.
     * @param url url to remote web driver
     * @param desiredCapabilities The desired capabilities of the browser driver to use.
     */
    public WebInteractionMethods(TestCase testCase, URL url, DesiredCapabilities desiredCapabilities){
        this.testCase = testCase;
        try{
            driver = new RemoteWebDriver(url, desiredCapabilities);
            driver.manage().window().maximize();
        }catch (Exception e){
            log(LogLevel.FRAMEWORK_ERROR, "Could not initializeIfNotInitialized driver. Error: " + e.getMessage());
            saveScreenshot(null);
            saveDesktopScreenshot();
            writeRunningProcessListDeviationsSinceTestCaseStart();
            haltFurtherExecution();
        }
    }

    /**
     * Returns the test case.
     *
     * @return TestCase
     */
    public TestCase getTestCase(){
        return testCase;
    }

    /**
     * Pauses execution for a number of milliseconds.
     *
     * @param milliseconds The number of milliseconds to wait.
     */
    public synchronized void wait(int milliseconds){
        try {
            Thread.sleep(milliseconds);
            log(LogLevel.DEBUG, "Waiting for " + milliseconds + " milliseconds.");
        } catch (InterruptedException e) {
            log(LogLevel.EXECUTION_PROBLEM, "Could not wait the expected " + milliseconds + " milliseconds.");
        }
    }

    /**
     * Browser back button
     */
    public void goBack(){
        if(driver == null){
            log(LogLevel.EXECUTION_PROBLEM, "Driver is null.");
            haltFurtherExecution();
        }

        try{
            driver.navigate().back();
            log(LogLevel.EXECUTED, "Navigating back in browser.");

        }catch (Exception e){
            log(LogLevel.EXECUTION_PROBLEM, "Could not navigate back in browser." + e.toString());
        }
    }

    /**
     * Writes a testCaseLog post to the testCaseLog
     *
     * @param logLevel The testCaseLog level of the testCaseLog post
     * @param message The testCaseLog message string
     */
    @SuppressWarnings("WeakerAccess")
    public void log(LogLevel logLevel, String message){
        testCase.log(logLevel, message);
    }

    /**
     * Navigates to specified url
     *
     * @param url The string formed url to navigate to
     */
    public void navigate(String url){
        try {
            goToUrl(url);
            testCase.logDifferentlyToTextLogAndHtmlLog(LogLevel.EXECUTED, "Navigation performed to url '" + url + "'.", "Navigation performed to url '<a href=\"" + url + "\" target=\"_blank\">" + url + "</a>'." );
        }catch (NavigationError e){
            testCase.logDifferentlyToTextLogAndHtmlLog(LogLevel.EXECUTION_PROBLEM, "Could not navigate to url '" + url + "'.", "Could not navigate to url '<a href=\"" + url + "\" target=\"_blank\">" + url + "</a>'.");
        }
    }

    /**
     * Creates the DomElements that are easily identified on current path to draft code in output file.
     *
     * @param outputPilePath File path to output file
     */
    public void mapCurrentPage(String outputPilePath){
        if(driver == null){
            log(LogLevel.EXECUTION_PROBLEM, "Driver is null.");
            haltFurtherExecution();
        }

        WebPageCodeConstructor.ConstructWebPageCode(driver, outputPilePath);
    }

    /**
     * Checks current page for broken links and reports results to log as verifications.
     */
    public void reportBrokenLinks(){
        List<WebElement> links = driver.findElements(By.xpath("//a"));
        List<Thread> linkCheckingThreads = new ArrayList<>();
        for(WebElement link : links){
            Thread linkCheck = new Thread(new LinkCheck(testCase, link.getAttribute("href")));
            linkCheckingThreads.add(linkCheck);
            linkCheck.start();
        }

        //Code below for waiting for all threads to finish due to log timing issues
        //noinspection ForLoopReplaceableByForEach
        for(int i = 0; i < linkCheckingThreads.size(); i++)
            try {
                linkCheckingThreads.get(i).join();
            } catch (InterruptedException e) {
                log(LogLevel.FRAMEWORK_ERROR, e.getMessage());
            }
    }

    /**
     * Waits for the given element to appear. If it hasn't appeared within the standard timeout execution continues.
     *
     * @param guiElement The element to wait for.
     * @return Returns true if element successfully appears within the timeout.
     */
    public boolean waitForElementToAppear(GuiElement guiElement){
        return waitForElementToAppear(guiElement, standardTimeoutInSeconds);
    }

    /**
     * Pauses execution until given element is displayed.
     *
     * @param guiElement The element to wait for
     * @return Returns true if element appears within timeout.
     */
    public boolean waitForElementToAppear(GuiElement guiElement, int timeoutInSeconds){
        long startTime = System.currentTimeMillis();
        DomElement domElement = (DomElement) guiElement;
        WebElement element;
        boolean elementHasAppeared = false;
        while (!elementHasAppeared && (System.currentTimeMillis() - startTime) <= timeoutInSeconds * 1000){
            element = getRuntimeElementWithoutLogging(domElement);
            if(element != null && element.isDisplayed()){
                elementHasAppeared = true;
            }else{
                wait(50);
            }
        }
        log(LogLevel.DEBUG, "Waited " + (System.currentTimeMillis() - startTime) + " for element " + domElement.LogIdentification() + " to appear. " +
                "It " + Boolean.toString(elementHasAppeared).toLowerCase().replace("true", "did.").replace("false", "never did."));
        return elementHasAppeared;
    }

    /**
     * Pauses execution until given element is displayed.
     *
     * @param guiElement The element to wait for
     * @return Returns true if element appears within timeout.
     */
    public WebElement waitForElementToBeEnabled(GuiElement guiElement, int timeoutInSeconds){
        long startTime = System.currentTimeMillis();
        DomElement domElement = (DomElement) guiElement;
        WebElement element = null;
        boolean elementIsEnabled = false;
        while (!elementIsEnabled && (System.currentTimeMillis() - startTime) <= timeoutInSeconds * 1000){
            element = getRuntimeElementWithoutLogging(domElement);
            if(element != null && element.isDisplayed() && element.isEnabled()){
                log(LogLevel.DEBUG, "Waited " + (System.currentTimeMillis() - startTime) + " milliseconds for element " + domElement.LogIdentification() + " to become displayed and enabled.");
                return element;
            }else{
                wait(50);
            }
        }
        if(element == null){
            log(LogLevel.DEBUG, "Could not identify element " + domElement.LogIdentification() + ". Tried for " + (System.currentTimeMillis() - startTime) + " milliseconds.");
        } else {
            log(LogLevel.DEBUG, "Waited " + (System.currentTimeMillis() - startTime) + " milliseconds for element " + domElement.LogIdentification() + " to become displayed and enabled. " +
                    "It " + Boolean.toString(elementIsEnabled).toLowerCase().replace("true", "did").replace("false", "never did") + " become enabled.");
        }
        return null;
    }

    /**
     * Waiting for element to disappear. If element is still there after standard timeout execution continues.
     * @param guiElement The element to wait for disappearance of.
     * @return Return true if element successfully has disappeared within the timeout.
     */
    public boolean waitForElementToDisappear(GuiElement guiElement){
        return waitForElementToDisappear(guiElement, standardTimeoutInSeconds);
    }

    /**
     * Pausing execution until the given element has disappeared.
     *
     * @param guiElement The element to wait for.
     * @param timeoutInSeconds Timeout period to wait for element to disappear.
     * @return Returns true if element disappears within timeout.
     */
    public boolean waitForElementToDisappear(GuiElement guiElement, int timeoutInSeconds){
        long startTime = System.currentTimeMillis();
        DomElement domElement = (DomElement) guiElement;
        WebElement element;
        boolean elementIsDisplayed = true;
        while (elementIsDisplayed && (System.currentTimeMillis() - startTime) <= timeoutInSeconds * 1000){
            element = getRuntimeElementWithoutLogging(domElement);
            if(element == null || !element.isDisplayed()){
                elementIsDisplayed = false;
            }else{
                wait(50);
            }
        }
        log(LogLevel.DEBUG, "Waited " + (System.currentTimeMillis() - startTime) + " for element " + domElement.LogIdentification() + " to disappear. " +
                "It " + Boolean.toString(elementIsDisplayed).toLowerCase().replace("true", "never did.").replace("false", "did."));
        return !elementIsDisplayed;
    }


    /**
     * Checks current page for broken links and reports results to log as verifications.
     */
    public void reportBrokenLinksRecursive(){
        String currentDomain = currentDomain();
        List<WebElement> links = driver.findElements(By.xpath("//a"));
        List<Thread> linkCheckingThreads = new ArrayList<>();
        for(WebElement link : links){
            String href = link.getAttribute("href");
            if(SupportMethods.isRegexMatch(href, "http.*" + currentDomain + ".*") || SupportMethods.isRegexMatch(href, "./.*")){
                Thread linkCheck = new Thread(new LinkCheck(testCase, link.getAttribute("href")));
                linkCheckingThreads.add(linkCheck);
                linkCheck.start();
            }
        }

        //Code below for waiting for all threads to finish due to log timing issues
        //noinspection ForLoopReplaceableByForEach
        for(int i = 0; i < linkCheckingThreads.size(); i++)
            try {
                linkCheckingThreads.get(i).join();
            } catch (InterruptedException e) {
                log(LogLevel.FRAMEWORK_ERROR, e.getMessage());
            }
    }

    private String currentDomain(){
        String domain = driver.getCurrentUrl();
        int startPosition = domain.indexOf("://") + 3;
        domain = domain.substring(startPosition);
        if(domain.contains("/"))
            domain = domain.substring(0, domain.indexOf("/"));
        return domain;
    }

    /**
     * Reads the text from an element
     *
     * @param guiElement The element to read the current text from
     * @return the current text in the element
     */
    public String getText(GuiElement guiElement){
        long startTime = System.currentTimeMillis();
        DomElement domElement = (DomElement) guiElement;
        String text = null;
        WebElement element = null;
        boolean elementIdentified = false;
        while (text == null && (System.currentTimeMillis() - startTime) <= standardTimeoutInSeconds * 1000){
            element = getRuntimeElementWithoutLogging(domElement);
            if(element != null){
                elementIdentified = true;
                try {
                    text = element.getText();
                }catch (Exception ignored){}
            }
            wait(50);
        }
        if(text == null && !elementIdentified){
            log(LogLevel.EXECUTION_PROBLEM, "Could not retrieve text from element " + domElement.LogIdentification() + " since it could not be identified at runtime.");
            //noinspection ConstantConditions
            saveScreenshot(element);
            saveDesktopScreenshot();
            saveHtmlContentOfCurrentPage();
            writeRunningProcessListDeviationsSinceTestCaseStart();
        } else if (text == null){
            log(LogLevel.EXECUTION_PROBLEM, "The element " + domElement.LogIdentification() + " could be identified, but the text of the element could not be read.");
            saveScreenshot(element);
            saveDesktopScreenshot();
            saveHtmlContentOfCurrentPage();
            writeRunningProcessListDeviationsSinceTestCaseStart();
        } else {
            log(LogLevel.DEBUG, "Identified the text '" + text + "' from element " + domElement.LogIdentification() + ".");
        }
        return text;
    }

    /**
     * Reads the text from an element
     *
     * @param guiElement The element to read the current text from
     * @return the current text in the element
     */
    public String getTextWithoutLogging(GuiElement guiElement){
        long startTime = System.currentTimeMillis();
        DomElement domElement = (DomElement) guiElement;
        String text = null;
        WebElement element;
        while (text == null && (System.currentTimeMillis() - startTime) <= standardTimeoutInSeconds * 1000){
            element = getRuntimeElementWithoutLogging(domElement);
            if(element != null){
                try {
                    text = element.getText();
                }catch (Exception ignored){}
            } else {
                wait(50);
            }
        }
        return text;
    }

    /**
     * Checks for existence of given element. Actually waits for existence of the element during the standard timeout time
     *
     * @param element The element to check existence of
     * @return Return true if element is found within timeout period
     */
    public boolean exists(GuiElement element){
        DomElement domElement = (DomElement) element;
        return getRuntimeElementWithTimeout(domElement, standardTimeoutInSeconds) != null;
    }

    /**
     * Sending accept to popup
     */
    public void acceptAlert() {
        if(driver == null){
            log(LogLevel.EXECUTION_PROBLEM, "Driver is null.");
            haltFurtherExecution();
        }
        try {
            WebDriverWait wait = new WebDriverWait(driver, 5);
            wait.until(ExpectedConditions.alertIsPresent());
            Alert alert = driver.switchTo().alert();
            alert.accept();
            log(LogLevel.EXECUTED, "Accepted alert dialogue.");
        } catch (Exception ignored) {
            log(LogLevel.EXECUTION_PROBLEM, "Could not accept alert dialogue.");
            saveScreenshot(null);
            saveDesktopScreenshot();
            saveHtmlContentOfCurrentPage();
            writeRunningProcessListDeviationsSinceTestCaseStart();
        }
    }

    /**
     * Re-scaling the browser window to a new size.
     *
     * @param width The new width of the browser window
     * @param height The new height of the browser window
     */
    public void setBrowserWindowSize(int width, int height) {
        if(driver == null){
            log(LogLevel.EXECUTION_PROBLEM, "Driver is null.");
            haltFurtherExecution();
        }

        log(LogLevel.DEBUG, "Re-sizing the browser window from (width/height) " + driver.manage().window().getSize().width + "/" + driver.manage().window().getSize().height + " pixels to " + width + "/" + height + " pixels.");
        try{
            driver.manage().window().setSize(new Dimension(width, height));
            log(LogLevel.EXECUTED, "Re-sized browser window to width " + width + " pixels and height " + height + " pixels.");
        }catch (Exception e){
            log(LogLevel.EXECUTION_PROBLEM, "Could not re-size browser window to height " + height + " and width " + width + " pixels");
        }
    }

    /**
     * Checks current value of given attribute for the element. For example the href value of a link, the src value of an image and so forth.
     *
     * @param linkElement The element to check attribute of
     * @param attributeName The name of the attribute to check the value of
     * @param expectedAttributeValue The expected attribute of the element
     */
    public void verifyElementAttribute(GuiElement linkElement, String attributeName, String expectedAttributeValue){
        DomElement domElement = (DomElement) linkElement;
        try{
            WebElement element = getRuntimeElementWithTimeout(domElement, standardTimeoutInSeconds);
            if(element.getAttribute("href").equals(expectedAttributeValue)){
                log(LogLevel.VERIFICATION_PASSED, "Element " + domElement.LogIdentification() + " was found to have the expected attribute value of '" + expectedAttributeValue + "' for attribute '" + attributeName + "'.");
            } else {
                log(LogLevel.VERIFICATION_FAILED, "Element " + domElement.LogIdentification() + " was expected to have the value '" + expectedAttributeValue + "' for attribute '" + attributeName + "' but actually had the value of '" + element.getAttribute(attributeName) + "'.");
            }
        } catch (Exception e){
            log(LogLevel.VERIFICATION_PROBLEM, "Could not check the attribute '" + attributeName + "' of element " + domElement.LogIdentification() + " (was expected to have the value '" + expectedAttributeValue + "'." + SupportMethods.LF + e.toString() );
        }
    }

    /**
     * Checks current value of given attribute for the element towards a regular expression pattern. For example the href value of a link, the src value of an image and so forth.
     *
     * @param linkElement The element to check attribute of
     * @param attributeName The name of the attribute to check the value of
     * @param expectedAttributevalueAsRegex The expected attribute of the element, as regular expression pattern
     */
    public void verifyElementAttributeRegex(GuiElement linkElement, String attributeName, String expectedAttributevalueAsRegex){
        DomElement domElement = (DomElement) linkElement;
        try{
            WebElement element = getRuntimeElementWithTimeout(domElement, standardTimeoutInSeconds);
            if(SupportMethods.isRegexMatch(element.getAttribute("href"),expectedAttributevalueAsRegex)){
                log(LogLevel.VERIFICATION_PASSED, "Element " + domElement.LogIdentification() + " was found to have the expected attribute value of '" + expectedAttributevalueAsRegex + "' for attribute '" + attributeName + "'.");
            } else {
                log(LogLevel.VERIFICATION_FAILED, "Element " + domElement.LogIdentification() + " was expected to have the value '" + expectedAttributevalueAsRegex + "' for attribute '" + attributeName + "' but actually had the value of '" + element.getAttribute(attributeName) + "'.");
            }
        } catch (Exception e){
            log(LogLevel.VERIFICATION_PROBLEM, "Could not check the attribute '" + attributeName + "' of element " + domElement.LogIdentification() + " (was expected to have the value '" + expectedAttributevalueAsRegex + "'." + SupportMethods.LF + e.toString() );
        }
    }

    /**
     * When a test case object is created a snapshot of running processes is created. This method makes a comparison
     * of what processes that differs at the time of method execution compared to test case start.
     */
    public void writeRunningProcessListDeviationsSinceTestCaseStart(){
        testCase.writeProcessListDeviationsFromSystemStartToLog();
    }

    /**
     * Writes text to the given element.
     *
     * @param guiElement The element to write to
     * @param textToWrite The text to write
     */
    public void write(GuiElement guiElement, String textToWrite){
        DomElement domElement = (DomElement) guiElement;
        long startTime = System.currentTimeMillis();
        WebElement element = null;
        boolean success = false;
        while (!success && System.currentTimeMillis() - startTime < standardTimeoutInSeconds * 1000){
            element = getRuntimeElementWithoutLogging(domElement);
            if(element == null){
                wait(50);
                continue;
            }
            try {
                enterText(element, textToWrite, false);
                success = true;
            }catch (Exception ignored){}
        }
        if(success){
            log(LogLevel.EXECUTED, "Wrote '" + textToWrite + "' to " + domElement.LogIdentification() + ".");
        } else {
            String text = null;
            String errorMessage = null;
            boolean exist = false;
            try {
                element = getRuntimeElementWithoutLogging(domElement);
                if(element != null) {
                    exist = true;
                    text = element.getText();
                }
            }catch (Exception e){
                errorMessage = e.getMessage();
            }
            log(LogLevel.EXECUTION_PROBLEM, "Could not enter the text '" + textToWrite + "' to element " + domElement.LogIdentification() + ". Element " + Boolean.toString(exist).toLowerCase().replace("false", "could not be identified").replace("true", "was identified") + ".");
            if(text != null) {
                log(LogLevel.DEVIATION_EXTRA_INFO, "Current text of " + domElement.LogIdentification() + " = '" + text + "'.");
            } else if(exist){
                log(LogLevel.DEVIATION_EXTRA_INFO, "Could not retrieve the current text of the element " + domElement.LogIdentification() + ". Are you sure it is the correct element?");
            }
            if(errorMessage != null) log(LogLevel.DEVIATION_EXTRA_INFO, "Error message: " + errorMessage);
            saveScreenshot(element);
            saveDesktopScreenshot();
            saveHtmlContentOfCurrentPage();
            writeRunningProcessListDeviationsSinceTestCaseStart();
            haltFurtherExecution();
        }
    }


    /**
     * Writes text to the given element after the element value has been cleared.
     *
     * @param guiElement The element to write to
     * @param textToWrite The text to write
     */
    public void writeAfterClear(GuiElement guiElement, String textToWrite){
        DomElement domElement = (DomElement) guiElement;
        WebElement webElement = getRuntimeElementWithTimeout(domElement, standardTimeoutInSeconds);
        try {
            enterText(webElement, textToWrite, true);
        }catch (Exception e){
            log(LogLevel.EXECUTION_PROBLEM, "Could not enter the text '" + textToWrite + "' to element " + domElement.LogIdentification() + ". ");
            saveScreenshot(webElement);
            saveDesktopScreenshot();
            saveHtmlContentOfCurrentPage();
            writeRunningProcessListDeviationsSinceTestCaseStart();
        }
    }

    /**
     * Writes text and triggers a submit of it to an element
     *
     * @param guiElement the element
     * @param text the text to send
     */
    public void submitText(GuiElement guiElement, String text){
        DomElement domElement = (DomElement) guiElement;
        WebElement webElement = null;
        try {
            webElement = getRuntimeElementWithTimeout(domElement, standardTimeoutInSeconds);
            enterText(webElement, text, false);
            try{
                webElement.submit();
                log(LogLevel.EXECUTED, "Submitted text '" + text + "' to " + domElement.LogIdentification() + ".");
            }catch (Exception e){
                log(LogLevel.EXECUTION_PROBLEM, "Could not submit the text entered to " + domElement.LogIdentification() + ".");
                saveScreenshot(webElement);
                saveDesktopScreenshot();
                saveHtmlContentOfCurrentPage();
                writeRunningProcessListDeviationsSinceTestCaseStart();
            }
        } catch (TextEnteringError textEnteringError) {
            log(LogLevel.EXECUTION_PROBLEM, "Could not enter '" + text + "' in " + domElement.LogIdentification() + ".");
            saveScreenshot(webElement);
            saveDesktopScreenshot();
            saveHtmlContentOfCurrentPage();
            writeRunningProcessListDeviationsSinceTestCaseStart();
        }
    }

    /**
     * Saves a screenshot of the web browser content to the testCaseLog folder and writes a testCaseLog post about it.
     * Used for provide debugging information when execution or verification problems (or errors) occur.
     * @param relevantWebElementToMarkWithBorder relevantWebElementToMarkWithBorder
     */
    @SuppressWarnings("WeakerAccess")
    public void saveScreenshot(WebElement relevantWebElementToMarkWithBorder){
        if(driver == null){
            log(LogLevel.EXECUTION_PROBLEM, "Driver is null.");
            haltFurtherExecution();
        }
        JavascriptExecutor js = null;
        try {
            js = (JavascriptExecutor) driver;
            if (relevantWebElementToMarkWithBorder != null && driver instanceof JavascriptExecutor) {
                js.executeScript("arguments[0].setAttribute('style', arguments[1]);", relevantWebElementToMarkWithBorder, "color: yellow; border: 5px solid yellow;");
            }
        } catch (Exception e){
            log(LogLevel.DEBUG, "Could not highlight any element before screenshot. Error: " + e.getMessage());
        }

        String filePath = LogFolder.testRunLogFolder + testCase.testName + TestRun.fileCounter + ".png";
        logger.debug( "Saving screenshot of web browser content to '" + filePath + "'." );
        TestRun.fileCounter++;
        byte[] fileImage;
        try{
            fileImage = ((TakesScreenshot)driver).getScreenshotAs(OutputType.BYTES);
        }catch (Exception e){
            log(LogLevel.FRAMEWORK_ERROR, "Could not take screenshot. Is driver ok? " + e.toString());
            try{
                if(relevantWebElementToMarkWithBorder != null && driver instanceof JavascriptExecutor && js != null) js.executeScript("arguments[0].setAttribute('style', arguments[1]);", relevantWebElementToMarkWithBorder, "");
            }catch (Exception jsProblem){
                log(LogLevel.DEBUG, "Could not reset element highlight frame. Error: " + jsProblem.getMessage());
            }
            return;
        }
        /*
        try {
            Path file = Paths.get(filePath);
            File fileFolder = new File(filePath);
            //noinspection ResultOfMethodCallIgnored
            fileFolder.getParentFile().mkdirs();
            if(fileImage != null){
                Files.write(file, fileImage);
                log(LogLevel.DEVIATION_EXTRA_INFO, "Saved screenshot as '" + filePath + "'.");
            }
        } catch (IOException e) {
            log(LogLevel.EXECUTION_PROBLEM, "Could not save screenshot to '" + filePath + "'. " + e.toString());
            //e.printStackTrace();
        }
        */
        if(fileImage != null){
            SupportMethods.saveToFile(fileImage, filePath);
            log(LogLevel.INFO, "Saved screenshot as '" + filePath + "'.");
        } else {
            log(LogLevel.DEBUG, "Could not save screenshot to '" + filePath + "' since the image data was empty.");
        }
    }

    /**
     * Saving desktop of full desktop rather than just the web browser
     */
    public void saveDesktopScreenshot(){
        try {
            RobotSwingInteractionMethods robotSwingInteractionMethods = new RobotSwingInteractionMethods(testCase);
            robotSwingInteractionMethods.captureScreenshot();
        } catch (Exception e){
            testCase.log(LogLevel.DEBUG, "Could not take desktop screenshot: " + e.toString());
        }
    }

    /**
     * Capture image of specific element to disk. Works by cropping a full screenshot to element.
     *
     * @param domElement The element to capture an image of.
     * @param filePath The file name of the file to write the image to.
     */
    public void saveDomElementScreenshot(DomElement domElement, String filePath){
        if(driver == null){
            log(LogLevel.EXECUTION_PROBLEM, "Driver is null.");
            haltFurtherExecution();
        }

        WebElement webElement = getRuntimeElementWithTimeout(domElement, standardTimeoutInSeconds);
        if(webElement == null){
            log(LogLevel.EXECUTION_PROBLEM, "Could not identify " + domElement.LogIdentification() + " when trying to capture an image of it.");
            saveScreenshot(null);
            saveDesktopScreenshot();
            saveHtmlContentOfCurrentPage();
            writeRunningProcessListDeviationsSinceTestCaseStart();
            return;
        }
        File screen = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        int ImageWidth = webElement.getSize().getWidth();
        int ImageHeight = webElement.getSize().getHeight();
        Point point = webElement.getLocation();
        int xCoordinate = point.getX();
        int yCoordinate = point.getY();

        BufferedImage img;
        try {
            img = ImageIO.read(screen);
        } catch (IOException e) {
            log(LogLevel.EXECUTION_PROBLEM, "Could not read screenshot of full screenshot when trying to capture an image of " + domElement.LogIdentification() + ".");
            return;
        }

        //cut Image using height, width and x y coordinates parameters.
        BufferedImage destination = img.getSubimage(xCoordinate, yCoordinate, ImageWidth, ImageHeight);
        try {
            ImageIO.write(destination, "png", new File(filePath));
        } catch (IOException e) {
            log(LogLevel.EXECUTION_PROBLEM, "Could not write image of " + domElement.LogIdentification() + " to file '" + filePath + "'.");
        }
    }


    /**
     * Checks for existence of given element. Actually waits for existence of the element during the timeout time
     *
     * @param guiElement The element to check existence of
     * @return Return true if element is found within timeout period
     */
    public boolean existsWithTimeout(GuiElement guiElement, int timeOutInSeconds){
        DomElement domElement = (DomElement) guiElement;
        return getRuntimeElementWithTimeout(domElement, timeOutInSeconds) != null;
    }

    /**
     * Checks for existence of given element.
     *
     * @param domElement The element to check existence of
     * @return Return true if element is found
     */
    public boolean exists(DomElement domElement){
        return getRuntimeElementWithoutLogging(domElement) != null;
    }

    /**
     * Changing the standard timeout value for waiting for objects in the GUI for most methods.
     *
     * @param standardTimeoutInSeconds The new value for standard timeout, in seconds.
     */
    public void setStandardTimeout(int standardTimeoutInSeconds){
        log(LogLevel.DEBUG, "Resetting standard timeout from " + this.standardTimeoutInSeconds + " seconds to " + standardTimeoutInSeconds + " seconds.");
        this.standardTimeoutInSeconds = standardTimeoutInSeconds;
    }

    /**
     * In some cases you might want to click on an element with a visible text without declaring the element itself. Then use this method.
     *
     * @param visibleText The visible text of the element to find
     */
    public void clickOnElementWithTheVisibleText(String visibleText){
        if(driver == null){
            log(LogLevel.EXECUTION_PROBLEM, "Driver is null.");
            haltFurtherExecution();
        }

        List<WebElement> potentialClickObjects = driver.findElements(By.xpath("//*[.='" + visibleText + "']"));
        if(potentialClickObjects.size() == 0){
            log(LogLevel.DEBUG, "No exact match for string '" + visibleText + "' found. Trying to find elements containing the text instead.");
            potentialClickObjects = driver.findElements(By.xpath("//*[contains(text(), '" + visibleText + "')]"));
        }
        if(potentialClickObjects.size() == 1){
            if(!potentialClickObjects.get(0).isDisplayed())
                errorManagementProcedures("Attempting to click the element with the visible text '" + visibleText + "'. It exists but it is hidden from view.");
            if(!potentialClickObjects.get(0).isEnabled())
                errorManagementProcedures("Attempting to click the element with the visible text '" + visibleText + "'. It exists but it is disabled.");
            try {
                potentialClickObjects.get(0).click();
                log(LogLevel.EXECUTED, "Clicked the element with visible text '" + visibleText + "'.");
            }catch (Exception e){
                log(LogLevel.EXECUTION_PROBLEM, "Could not click the element with the visible text '" + visibleText + "'. Error message: " + e.getMessage());
            }
        }else{
            List<WebElement> trulyClickableElements = new ArrayList<>();
            for(WebElement potentialClickObject : potentialClickObjects){
                if(potentialClickObject.isEnabled() && potentialClickObject.isDisplayed()){
                    trulyClickableElements.add(potentialClickObject);
                }
            }
            log(LogLevel.DEBUG, "Found " + potentialClickObjects.size() + " elements with the text '" + visibleText + "'. Out of those " + trulyClickableElements.size() + " was enabled and not hidden.");
            if(trulyClickableElements.size() == 1){
                try{
                    trulyClickableElements.get(0).click();
                    log(LogLevel.EXECUTED, "Clicked the element with the visible text '" + visibleText + "'.");
                }catch (Exception e){
                    errorManagementProcedures("Could not click element with visible text '" + visibleText + "'. Error message: " + e.getMessage());
                }
            }else{
                errorManagementProcedures("Attempted to click element with visible text '" + visibleText + "', but several elements was found with that text.");
            }
        }
    }

    private class Waiter{
        private final TestCase testCase;
        private final LogLevel logLevel;
        private final long startTime;
        private int totalTimeInWaiting;

        public Waiter(TestCase testCase, LogLevel logLevel){
            this.logLevel = logLevel;
            this.testCase = testCase;
            this.startTime = System.currentTimeMillis();
        }

        public void wait(int milliseconds){
            try {
                Thread.sleep(milliseconds);
                totalTimeInWaiting += milliseconds;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        public void report(){
            testCase.log(logLevel, "Waited a total of " + (System.currentTimeMillis() - startTime) + " milliseconds, with " + totalTimeInWaiting + " of those in the waiting mechanism.");
        }

        public int totalTimeSpentSoFar(){
            return (int) (System.currentTimeMillis() - startTime);
        }
    }

    /**
     * Performing a click event on an element. Method keeps trying the given number of seconds.
     *
     * @param guiElement the GUI element to click
     * @param timeoutInSeconds The number of seconds to wait and try to click
     */
    public void clickEvenIfDisabled(GuiElement guiElement, int timeoutInSeconds){
        log(LogLevel.FRAMEWORK_ERROR, "The method clickEvenIfDisabled() is not yet implemented in class WebInteractionMethods. Please do.");
        throw new NotImplementedException();
    }

    /**
     * Performing a click event on an element. Method keeps trying the set standard timeout number of seconds.
     *
     * @param guiElement the GUI element to click
     */
    public void click(GuiElement guiElement){
        click(guiElement, standardTimeoutInSeconds);
    }

    /**
     * Performing a click event on an element. Method keeps trying the given number of seconds.
     *
     * @param guiElement the GUI element to click
     * @param timeoutInSeconds The number of seconds to wait and try to click
     */
    public void click(GuiElement guiElement, int timeoutInSeconds){
        DomElement element = (DomElement) guiElement;
        long startTime = System.currentTimeMillis();
        boolean clicked = false;
        String errorMessage = null;
        log(LogLevel.DEBUG, "Attempting to click on " + element.LogIdentification() + ".");
        WebElement webElement = waitForElementToBeEnabled(guiElement, timeoutInSeconds);
        if(webElement == null){
            errorManagementProcedures("Could not identify element " + element.LogIdentification() + " in the GUI.");
        } else if(!webElement.isEnabled()){
            errorManagementProcedures("Element " + element.LogIdentification() + " is not enabled. Seems unnatural to click it. If you still want this element to be clicked the clickEvenIfDisabled() method instead.");
        } else if(!webElement.isDisplayed()){
            errorManagementProcedures("Element " + element.LogIdentification() + " is not displayed. Seems unnatural to click it. If you still want this element to be clicked the clickEvenIfDisabled() method instead.");
        }
        while (!clicked && (System.currentTimeMillis() - startTime) < timeoutInSeconds *1000){
            try{
                Thread.sleep(50);
                //noinspection ConstantConditions
                webElement.click();
                clicked = true;
            } catch (Exception e){
                errorMessage = e.getMessage();
            }
        }
        if(clicked){
            log(LogLevel.EXECUTED, "Clicked the " + element.LogIdentification() + " element after " + String.valueOf(System.currentTimeMillis() - startTime) + " milliseconds.");
        } else if(errorMessage != null){
            if(errorMessage.contains("Other element would receive the click")){
                log(LogLevel.EXECUTION_PROBLEM, "It seems something is blocking the possibility to click on " + element.LogIdentification() + ". It could for example be a popup overlaying the element?");
            } else {
                testCase.logDifferentlyToTextLogAndHtmlLog(LogLevel.FRAMEWORK_ERROR, "Could not click on element " + element.LogIdentification() + ". " + errorMessage, "Could not click och element " + element.LogIdentification() + ".<br>Error:<br><br>" + errorMessage);
            }
            errorManagementProcedures("Could not click element " + element.LogIdentification() + ".");
        } else {
            errorManagementProcedures("Could not successfully click on the " + element.LogIdentification() + " element.");
        }
    }


    private boolean elementBecomeDisplayedWithinTimeout(WebElement webelement){
        if(!webelement.isDisplayed()){
            Waiter wait = new Waiter(testCase, LogLevel.DEBUG);
            while (!webelement.isDisplayed() && wait.totalTimeSpentSoFar() < standardTimeoutInSeconds *1000){
                wait.wait(50);
            }
            wait.report();
        }
        return webelement.isDisplayed();
    }

    private boolean elementBecomeEnabledWithinTimeout(WebElement webelement){
        if(!webelement.isEnabled()){
            Waiter wait = new Waiter(testCase, LogLevel.DEBUG);
            while (!webelement.isEnabled() && wait.totalTimeSpentSoFar() < standardTimeoutInSeconds *1000){
                wait.wait(50);
            }
            wait.report();
        }
        return webelement.isEnabled();
    }

    /**
     * Closes the web browser.
     */
    public void closeBrowser(){
        try{
            closeBrowserDriver();
            log(LogLevel.INFO, "Closing browser.");
        }catch (BrowserClosingError browserClosingError) {
            log(LogLevel.EXECUTION_PROBLEM, "Could not close the browser.");
        }
    }

    /**
     * Makes sure the driver is closed
     */
    public void makeSureDriverIsClosed(){
        try{
            closeBrowserDriver();
        }catch (Exception e) {
            log(LogLevel.DEBUG, "Could not close browser. Was probably already closed.");
        }
    }

    /**
     * Checks if the given object exist in the HTML. Compare with verifyObjectIsDisplayed, that also checks if the element is visible in the GUI.
     *
     * @param guiElement The GUI element to find
     */
    public void verifyObjectExistence(GuiElement guiElement){
        DomElement domElement = (DomElement) guiElement;
        if(getRuntimeElementWithTimeout(domElement, standardTimeoutInSeconds) == null){
            log(LogLevel.VERIFICATION_FAILED, "Object " + domElement.LogIdentification() + " was expected to be present but could not be identified.");
            saveScreenshot(null);
            saveDesktopScreenshot();
            saveHtmlContentOfCurrentPage();
            writeRunningProcessListDeviationsSinceTestCaseStart();
        } else {
            log(LogLevel.VERIFICATION_PASSED, "Existence of object " + domElement.LogIdentification() + " verified.");
        }
    }

    /**
     * Checks if the given object is not displayed in the HTML. Compare with verifyObjectDoesNotExist, that checks if the element exist in the html.
     *
     * @param guiElement The GUI element to find
     */
    public void verifyObjectIsNotDisplayed(GuiElement guiElement){
        DomElement domElement = (DomElement) guiElement;
        long startTime = System.currentTimeMillis();
        WebElement webElement = getRuntimeElementWithoutLogging(domElement);
        boolean disappeared = (webElement == null);
        while (!disappeared && System.currentTimeMillis() - startTime < standardTimeoutInSeconds * 1000){
            wait(100);
            webElement = getRuntimeElementWithoutLogging(domElement);
            disappeared = (webElement == null || !webElement.isDisplayed());
        }

        if(webElement == null){
            log(LogLevel.DEBUG, "Object " + domElement.LogIdentification() + " could not be identified in the html.");
            log(LogLevel.VERIFICATION_PASSED, "Object " + domElement.LogIdentification() + " verified to not be present.");
        } else if(!webElement.isDisplayed()) {
            log(LogLevel.DEBUG, "Object " + domElement.LogIdentification() + " could be identified in the html, but it is suppressed from being displayed in the GUI.");
            log(LogLevel.VERIFICATION_PASSED, "Object " + domElement.LogIdentification() + " verified to not displayed.");
        }else {
            log(LogLevel.VERIFICATION_FAILED, "Object " + domElement.LogIdentification() + " was identified as displayed although expected to not be displayed.");
            saveScreenshot(webElement);
            saveDesktopScreenshot();
            saveHtmlContentOfCurrentPage();
            writeRunningProcessListDeviationsSinceTestCaseStart();
        }
    }

    /**
     * Checks if the given object is not displayed in the HTML. Compare with verifyObjectDoesNotExist, that checks if the element exist in the html.
     *
     * @param guiElement The GUI element to find
     * @param timeoutInSeconds The time to wait for object disappearance.
     */
    public void verifyObjectIsNotDisplayedWithTimeout(GuiElement guiElement, int timeoutInSeconds){
        DomElement domElement = (DomElement) guiElement;
        long startTime = System.currentTimeMillis();
        WebElement webElement = getRuntimeElementWithoutLogging(domElement);
        boolean disappeared = (webElement == null);
        while (!disappeared && System.currentTimeMillis() - startTime < timeoutInSeconds * 1000){
            wait(100);
            webElement = getRuntimeElementWithoutLogging(domElement);
            disappeared = (webElement == null || !webElement.isDisplayed());
        }

        if(webElement == null){
            log(LogLevel.DEBUG, "Object " + domElement.LogIdentification() + " could not be identified in the html.");
            log(LogLevel.VERIFICATION_PASSED, "Object " + domElement.LogIdentification() + " verified to not be present.");
        } else if(!webElement.isDisplayed()) {
            log(LogLevel.DEBUG, "Object " + domElement.LogIdentification() + " could be identified in the html, but it is suppressed from being displayed in the GUI.");
            log(LogLevel.VERIFICATION_PASSED, "Object " + domElement.LogIdentification() + " verified to not displayed.");
        }else {
            log(LogLevel.VERIFICATION_FAILED, "Object " + domElement.LogIdentification() + " was identified as displayed although expected to not be displayed.");
            saveScreenshot(webElement);
            saveDesktopScreenshot();
            saveHtmlContentOfCurrentPage();
            writeRunningProcessListDeviationsSinceTestCaseStart();
        }
    }

    /**
     * Checks if the given element is displayed in the GUI.
     *
     * @param guiElement The element to check for
     * @return Return true if the element is displayed
     */
    public boolean isDisplayed(GuiElement guiElement){
        DomElement domElement = (DomElement) guiElement;
        WebElement webElement = getRuntimeElementWithTimeout(domElement, standardTimeoutInSeconds);
        boolean displayed = (webElement == null || !webElement.isDisplayed());
        log(LogLevel.DEBUG, "Checking if " + domElement.LogIdentification() + " is displayed. Returning " + displayed + ".");
        return displayed;
    }

    /**
     * Checks if the given element is displayed in the GUI. Check performed only once, but wait for element to appear.
     *
     * @param guiElement The element to check for
     * @return Return true if the element is not displayed
     */
    public boolean isNotDisplayed(GuiElement guiElement){
        DomElement domElement = (DomElement) guiElement;
        WebElement webElement = getRuntimeElementWithTimeout(domElement, standardTimeoutInSeconds);
        boolean notDisplayed = (webElement == null || !webElement.isDisplayed());
        log(LogLevel.DEBUG, "Checking if "  + domElement.LogIdentification() + " is not displayed. Returning " + notDisplayed + ".");
        return notDisplayed;
    }

    /**
     * Checks if the given element is displayed in the GUI. Checks only once, but right away. If element is not present directly it continues.
     *
     * @param guiElement The element to check for
     * @return Return true if the element is not displayed
     */
    public boolean isNotDisplayedExactlyNow(GuiElement guiElement){
        DomElement domElement = (DomElement) guiElement;
        WebElement webElement = getRuntimeElementWithoutLogging(domElement);
        boolean notDisplayed = (webElement == null || !webElement.isDisplayed());
        log(LogLevel.DEBUG, "Checking if "  + domElement.LogIdentification() + " is not displayed. Returning " + notDisplayed + ".");
        return notDisplayed;
    }

    /**
     * Checks if the given element is displayed in the GUI. Checks repeatedly for element to disappear from the GUI of the system under test the whole length of the timeout.
     *
     * @param guiElement The element to check for
     * @param timeoutInSeconds Seconds to wait for element being disappeared
     * @return Return true if the element is not displayed
     */
    public boolean isNotDisplayedWithinTimeout(GuiElement guiElement, int timeoutInSeconds) {
        long startTime = System.currentTimeMillis();
        DomElement domElement = (DomElement) guiElement;
        WebElement webElement = getRuntimeElementWithoutLogging(domElement);
        if (webElement == null) return true;
        while ((webElement != null && webElement.isDisplayed()) && (System.currentTimeMillis() - startTime) < timeoutInSeconds * 1000) {
            webElement = getRuntimeElementWithoutLogging(domElement);
        }
        return webElement == null || !webElement.isDisplayed();
    }

    /**
     * Checks if the given object is displayed in the HTML. Compare with verifyObjectExistence, that checks if the element exist in the html.
     *
     * @param guiElement The GUI element to find
     */
    public void verifyObjectIsDisplayed(GuiElement guiElement){
        DomElement domElement = (DomElement) guiElement;
        WebElement webElement = getRuntimeElementWithTimeout(domElement, standardTimeoutInSeconds);
        if(webElement == null){
            log(LogLevel.VERIFICATION_FAILED, "Object " + domElement.LogIdentification() + " was expected to be displayed but could not be identified at all.");
            saveScreenshot(null);
            saveDesktopScreenshot();
            saveHtmlContentOfCurrentPage();
            writeRunningProcessListDeviationsSinceTestCaseStart();
        } else if(!webElement.isDisplayed()) {
            log(LogLevel.VERIFICATION_FAILED, "Object " + domElement.LogIdentification() + " is present, but the display of the object is suppressed.");
            saveScreenshot(webElement);
            saveDesktopScreenshot();
            saveHtmlContentOfCurrentPage();
            writeRunningProcessListDeviationsSinceTestCaseStart();
        }else {
            log(LogLevel.VERIFICATION_PASSED, "Existence of object " + domElement.LogIdentification() + " verified.");
        }
    }

    /**
     * Checks if the given object exist in the GUI
     *
     * @param guiElement The GUI element to find
     */
    public void verifyObjectDoesNotExist(GuiElement guiElement){
        DomElement domElement = (DomElement) guiElement;
        WebElement webElement = getRuntimeElementWithTimeout(domElement, standardTimeoutInSeconds);
        if(webElement != null){
            log(LogLevel.VERIFICATION_FAILED, "Object " + domElement.LogIdentification() + " was expected to not be present but was able to be identified.");
            saveScreenshot(webElement);
            saveDesktopScreenshot();
            saveHtmlContentOfCurrentPage();
            writeRunningProcessListDeviationsSinceTestCaseStart();
        } else {
            log(LogLevel.VERIFICATION_PASSED, "Verified that the object " + domElement.LogIdentification() + " was not present.");
        }
    }

    /**
     * Checks if the given object exist in the GUI, within a timeout.
     *
     * @param guiElement The GUI element to find.
     * @param timeoutInSeconds The timeout to wait for object disappearance.
     */
    public void verifyObjectDoesNotExistWithTimeout(GuiElement guiElement, int timeoutInSeconds){
        long startTime = System.currentTimeMillis();
        DomElement domElement = (DomElement) guiElement;
        WebElement webElement = getRuntimeElementWithTimeout(domElement, timeoutInSeconds);
        if(webElement != null){
            log(LogLevel.VERIFICATION_FAILED, "Object " + domElement.LogIdentification() + " was expected to not be present but was able to be identified.");
            saveScreenshot(webElement);
            saveDesktopScreenshot();
            saveHtmlContentOfCurrentPage();
            writeRunningProcessListDeviationsSinceTestCaseStart();
        } else {
            log(LogLevel.VERIFICATION_PASSED, "Verified that the object " + domElement.LogIdentification() + " was not present.");
        }
    }

    /**
     * Checks if an element exist in the browser within a given timeout
     *
     * @param guiElement The element to verify
     * @param timeoutInSeconds The number of seconds to keep retrying before calling it a failure
     */
    public void verifyObjectExistenceWithTimeout(GuiElement guiElement, int timeoutInSeconds){
        DomElement domElement = (DomElement) guiElement;
        if(getRuntimeElementWithTimeout(domElement, timeoutInSeconds) == null){
            log(LogLevel.VERIFICATION_PROBLEM, "Object " + domElement.LogIdentification() + " was expected to be present but was not identified.");
            saveScreenshot(null);
            saveDesktopScreenshot();
            saveHtmlContentOfCurrentPage();
            writeRunningProcessListDeviationsSinceTestCaseStart();
        } else {
            log(LogLevel.VERIFICATION_PASSED, "Existence of object " + domElement.LogIdentification() + " verified.");
        }
    }

    /**
     * Checks if specified text exist in the browser page
     *
     * @param text test string to find
     */
    public void verifyTextExistOnCurrentPage(String text){
        DomElement domElement = new DomElement("//*[contains(text(),'" + text + "')]", DomElement.IdentificationType.BY_X_PATH);
        //List<WebElement> list = driver.findElements(By.xpath("//*[contains(text(),'" + text + "')]"));
        WebElement webElement = getRuntimeElementWithTimeout(domElement, standardTimeoutInSeconds);
        if(webElement != null){
            log(LogLevel.VERIFICATION_PASSED, "The text '" + text + "' could be found on the current page.");
        }else {
            log(LogLevel.VERIFICATION_FAILED, "The text '" + text + "' could not be found on the current page.");
            saveScreenshot(null);
            saveDesktopScreenshot();
            saveHtmlContentOfCurrentPage();
            writeRunningProcessListDeviationsSinceTestCaseStart();
        }
    }

    /**
     * Checks if specified regular expression text pattern exist in the browser page source code
     *
     * @param textAsRegexPattern test string to find
     */
    public void verifyTextAsRegexPatternExistInPageSource(String textAsRegexPattern){
        if(driver == null){
            log(LogLevel.EXECUTION_PROBLEM, "Driver is null.");
            haltFurtherExecution();
        }
        String pageSource = driver.getPageSource();
        if(SupportMethods.isRegexMatch(pageSource, textAsRegexPattern)){
            log(LogLevel.VERIFICATION_PASSED, "The regular expression pattern '" + textAsRegexPattern + "' could be found on the current page.");
        }else {
            log(LogLevel.VERIFICATION_FAILED, "The regular expression pattern '" + textAsRegexPattern + "' could not be found on the current page.");
            saveScreenshot(null);
            saveDesktopScreenshot();
            saveHtmlContentOfCurrentPage();
            writeRunningProcessListDeviationsSinceTestCaseStart();
        }
    }

    /**
     * Checks the web browser page title against expected result, written as a regular expression
     *
     * @param expectedTitleAsRegexPattern Expected regular expression pattern to match the current title of the web page
     */
    @SuppressWarnings("unused")
    public void verifyPageTitleAsRegex(String expectedTitleAsRegexPattern){
        if(driver == null){
            log(LogLevel.EXECUTION_PROBLEM, "Driver is null.");
            haltFurtherExecution();
        }
        String currentTitle = driver.getTitle();
        if(SupportMethods.isRegexMatch(currentTitle, expectedTitleAsRegexPattern)){
            log(LogLevel.VERIFICATION_PASSED, "The current page title was '" + currentTitle + "', and that title matches the given regex pattern '" + expectedTitleAsRegexPattern + "'.");
        }else {
            log(LogLevel.VERIFICATION_FAILED, "The current page title was expected to match the regex pattern '" + expectedTitleAsRegexPattern+ "' but was '" + currentTitle + "'.");
            saveScreenshot(null);
            saveDesktopScreenshot();
            saveHtmlContentOfCurrentPage();
            writeRunningProcessListDeviationsSinceTestCaseStart();
        }
    }


    /**
     * Checks the web browser page title against expected result
     *
     * @param expectedTitle Expected current title of the web page
     */
    @SuppressWarnings("unused")
    public void verifyPageTitle(String expectedTitle){
        if(driver == null){
            log(LogLevel.EXECUTION_PROBLEM, "Driver is null.");
            haltFurtherExecution();
        }
        String currentTitle = driver.getTitle();
        if(currentTitle.equals(expectedTitle)){
            log(LogLevel.VERIFICATION_PASSED, "The current page title was '" + expectedTitle + "' as expected.");
        }else {
            log(LogLevel.VERIFICATION_FAILED, "The current page title was expected to be '" + expectedTitle + "' but was '" + currentTitle + "'.");
            saveScreenshot(null);
            saveDesktopScreenshot();
            saveHtmlContentOfCurrentPage();
            writeRunningProcessListDeviationsSinceTestCaseStart();
        }
    }

    /**
     * Checks that the web browser page title matches the expected title pattern within specified timeout
     *
     * @param expectedTitleAsRegexPattern Expected title pattern in a regular expression format, for the web page
     * @param timeoutInSeconds Timeout in seconds
     */
    @SuppressWarnings("SameParameterValue")
    public void verifyPageTitleAsRegexWithTimeout(String expectedTitleAsRegexPattern, int timeoutInSeconds){
        double startTime = System.currentTimeMillis();
        if(driver == null){
            log(LogLevel.EXECUTION_PROBLEM, "Driver is null.");
            haltFurtherExecution();
        }

        String currentTitle = driver.getTitle();
        long retryPeriodInMS = 50;
        while(!SupportMethods.isRegexMatch(currentTitle, expectedTitleAsRegexPattern) && System.currentTimeMillis() - startTime < timeoutInSeconds *1000){
            try {
                Thread.sleep(retryPeriodInMS);
            } catch (InterruptedException e) {
                log(LogLevel.FRAMEWORK_ERROR, "Could not put thread to sleep.");
                e.printStackTrace();
            }
            currentTitle = driver.getTitle();
        }
        if(SupportMethods.isRegexMatch(currentTitle, expectedTitleAsRegexPattern)){
            log(LogLevel.VERIFICATION_PASSED, "The current page title was '" + currentTitle + "' and that title is found to be a match for given regular expression pattern '" + expectedTitleAsRegexPattern + "' (found after " + (System.currentTimeMillis() - startTime) + " milliseconds).");
        }else {
            log(LogLevel.VERIFICATION_FAILED, "The current page title was expected to match the regular expression pattern '" + expectedTitleAsRegexPattern + "' but was '" + currentTitle + "' even after " + timeoutInSeconds + " milliseconds.");
            saveScreenshot(null);
            saveDesktopScreenshot();
            saveHtmlContentOfCurrentPage();
            writeRunningProcessListDeviationsSinceTestCaseStart();
        }
    }

    /**
     * Checks that the web browser page title is (or become) the expected title within specified timeout
     *
     * @param expectedTitle Expected title of the web page
     * @param timeoutInSeconds Timeout in seconds
     */
    @SuppressWarnings("SameParameterValue")
    public void verifyPageTitleWithTimeout(String expectedTitle, int timeoutInSeconds){
        double startTime = System.currentTimeMillis();
        if(driver == null){
            log(LogLevel.EXECUTION_PROBLEM, "Driver is null.");
            haltFurtherExecution();
        }
        String currentTitle = driver.getTitle();
        long retryPeriodInMS = 50;
        while(!currentTitle.equals(expectedTitle) && System.currentTimeMillis() - startTime < timeoutInSeconds *1000){
            try {
                Thread.sleep(retryPeriodInMS);
            } catch (InterruptedException e) {
                log(LogLevel.FRAMEWORK_ERROR, "Could not put thread to sleep.");
                e.printStackTrace();
            }
            currentTitle = driver.getTitle();
        }
        if(currentTitle.equals(expectedTitle)){
            log(LogLevel.VERIFICATION_PASSED, "The current page title was '" + expectedTitle + "' as expected (found after " + (System.currentTimeMillis() - startTime) + " milliseconds).");
        }else {
            log(LogLevel.VERIFICATION_FAILED, "The current page title was expected to be '" + expectedTitle + "' but was '" + currentTitle + "' even after " + (System.currentTimeMillis() - startTime) + " milliseconds.");
            saveScreenshot(null);
            saveDesktopScreenshot();
            saveHtmlContentOfCurrentPage();
            writeRunningProcessListDeviationsSinceTestCaseStart();
        }
    }

    /**
     * Verifies that the current text of the given element correspond to the expected text.
     *
     * @param guiElement The element to check the text of
     * @param expectedText The expected text to find
     */
    public void verifyElementText(GuiElement guiElement, String expectedText){
        boolean verifiedOk = false;
        String currentText = "";
        long startTime = System.currentTimeMillis();
        while(!verifiedOk && System.currentTimeMillis() - startTime <= standardTimeoutInSeconds * 1000){
            currentText = getTextWithoutLogging(guiElement);
            if(currentText != null && currentText.equals(expectedText)){
                verifiedOk = true;
            }
        }
        if(verifiedOk){
            log(LogLevel.VERIFICATION_PASSED, "Element " + ((DomElement)guiElement).LogIdentification() + " found to have the text '" + expectedText + "' as expected.");
        } else {
            if(exists(guiElement)){
                log(LogLevel.VERIFICATION_FAILED, "Element " + ((DomElement)guiElement).LogIdentification() + " was expected to have the text '" + expectedText + "', but it actually was '" + currentText + "'.");
            } else {
                DomElement domElement = (DomElement) guiElement;
                log(LogLevel.VERIFICATION_PROBLEM, "Could not find element " + domElement.LogIdentification() + " when attempting to verify the text '" + expectedText + "'.");
            }
            saveScreenshot(null);
            saveDesktopScreenshot();
            saveHtmlContentOfCurrentPage();
            writeRunningProcessListDeviationsSinceTestCaseStart();
        }
    }

    /**
     * Verifies that the current text of the given element correspond to the expected text.
     *
     * @param guiElement The element to check the text of
     * @param expectedText The expected text to find
     */
    public void verifyElementTextContainsText(GuiElement guiElement, String expectedText){
        boolean verifiedOk = false;
        String currentText = "";
        long startTime = System.currentTimeMillis();
        while(!verifiedOk && System.currentTimeMillis() - startTime <= standardTimeoutInSeconds * 1000){
            currentText = getTextWithoutLogging(guiElement);
            if(currentText != null && currentText.contains(expectedText)){
                verifiedOk = true;
            }
        }
        if(verifiedOk){
            log(LogLevel.VERIFICATION_PASSED, "Element " + ((DomElement)guiElement).LogIdentification() + " found to have the text '" + expectedText + "' as expected.");
        } else {
            if(exists(guiElement)){
                log(LogLevel.VERIFICATION_FAILED, "Element " + ((DomElement)guiElement).LogIdentification() + " was expected to have the text '" + expectedText + "', but it actually was '" + currentText + "'.");
            } else {
                DomElement domElement = (DomElement) guiElement;
                log(LogLevel.VERIFICATION_PROBLEM, "Could not find element " + domElement.LogIdentification() + " when attempting to verify the text '" + expectedText + "'.");
            }
            saveScreenshot(null);
            saveDesktopScreenshot();
            saveHtmlContentOfCurrentPage();
            writeRunningProcessListDeviationsSinceTestCaseStart();
        }
    }

    /**
     * Verifies that the current text of the given element correspond to the expected text.
     *
     * @param guiElement The element to check the text of
     * @param expectedTextAsRegexPattern The expected text to find
     */
    public void verifyElementTextWithRegexPattern(GuiElement guiElement, String expectedTextAsRegexPattern){
        String currentText = "";
        long startTime = System.currentTimeMillis();
        boolean verifiedOk = false;
        while (!verifiedOk && System.currentTimeMillis() - startTime <= standardTimeoutInSeconds * 1000){
            currentText = getTextWithoutLogging(guiElement);
            if(SupportMethods.isRegexMatch(currentText, expectedTextAsRegexPattern)){
                verifiedOk = true;
            }
        }
        if(SupportMethods.isRegexMatch(currentText, expectedTextAsRegexPattern)){
            log(LogLevel.VERIFICATION_PASSED, "Element " + ((DomElement)guiElement).LogIdentification() + " found to be '" + currentText + ". It is a match with the regular expression pattern '" + expectedTextAsRegexPattern + "'.");
        } else {
            if(exists(guiElement)){
                log(LogLevel.VERIFICATION_FAILED, "Element " + ((DomElement)guiElement).LogIdentification() + " was expected to have match the regular expression pattern '" + expectedTextAsRegexPattern+ "', but it actually was '" + currentText + "'. Not a match.");
            } else {
                DomElement domElement = (DomElement) guiElement;
                log(LogLevel.VERIFICATION_PROBLEM, "Could not find element " + domElement.LogIdentification() + " when attempting to verify the text from regular expression pattern '" + expectedTextAsRegexPattern + "'.");
            }
            saveScreenshot(null);
            saveDesktopScreenshot();
            saveHtmlContentOfCurrentPage();
            writeRunningProcessListDeviationsSinceTestCaseStart();
        }
    }

    /**
     * Clicks the row matching the given strings in a table
     *
     * @param guiTableElement The table element in the gui
     * @param textsToFindOnRow the text strings to find
     */
    public void pickTableRow(GuiElement guiTableElement, String[] textsToFindOnRow){
        boolean doneOk = false;
        long startTime = System.currentTimeMillis();
        DomElement domElement = (DomElement)guiTableElement;
        WebElement webElement;
        while (!doneOk && System.currentTimeMillis() - startTime <= standardTimeoutInSeconds *1000){
            webElement = getRuntimeElementWithoutLogging(domElement);
            List<String> partialMatches = new ArrayList<>();
            boolean allValuesFoundInRow = false;
            List<WebElement> rows = webElement.findElements(By.xpath(".//tr"));
            for (WebElement row : rows)
            {
                ArrayList<String> rowStrings = new ArrayList<>();
                boolean someValueFoundInRow = false;
                boolean valueMissingOnRow = false;
                List<WebElement> cells = row.findElements(By.xpath("(.//td | .//th)"));
                for(String textToFindOnRow : textsToFindOnRow)
                {
                    boolean thisValueFoundOnRow = false;
                    for(WebElement cell : cells)
                    {
                        rowStrings.add(cell.getText());
                        if (cell.getText().contains(textToFindOnRow))
                        {
                            thisValueFoundOnRow = true;
                            someValueFoundInRow = true;
                        }
                    }
                    if (!thisValueFoundOnRow)
                    {
                        valueMissingOnRow = true;
                        break;
                    }

                }
                //log(LogLevel.DEBUG, String.join(", ", rowStrings) + " > Match: " + String.valueOf(!valueMissingOnRow));
                if (!valueMissingOnRow)
                {
                    allValuesFoundInRow = true;
                    row.click();
                    break;
                } else if (someValueFoundInRow){
                    partialMatches.add("'" + String.join("', '", rowStrings) + "'");
                }
            }
            if(!allValuesFoundInRow){
                log(LogLevel.EXECUTION_PROBLEM, "Could not find row matching '" + String.join("', '", textsToFindOnRow) + "' in " + domElement.LogIdentification() + ".");
                testCase.logDifferentlyToTextLogAndHtmlLog(LogLevel.DEVIATION_EXTRA_INFO,
                        "Rows with partial matches for '"  + String.join("', '", textsToFindOnRow) + "':" + System.lineSeparator() + "[" + String.join("]" + System.lineSeparator() + "[", partialMatches) + "]",
                        "Rows with partial matches for '"  + String.join("', '", textsToFindOnRow) + "':<br><table><tr><td>" + String.join("</td></tr><tr><td>", partialMatches) + "</td></tr></table>");
                saveScreenshot(webElement);
                saveDesktopScreenshot();
                saveHtmlContentOfCurrentPage();
            } else {
                doneOk = true;
                log(LogLevel.EXECUTED, "Clicked the row with values '" + String.join("', '", textsToFindOnRow) + "' in table " + domElement.LogIdentification() + ".");
            }
        }
    }

    public void executeJavascript(String script){
        if(driver == null){
            log(LogLevel.EXECUTION_PROBLEM, "Driver is null.");
            haltFurtherExecution();
        }
        if (driver instanceof JavascriptExecutor) {
            try {
                ((JavascriptExecutor)driver).executeScript(script);
                testCase.logDifferentlyToTextLogAndHtmlLog(LogLevel.EXECUTED,
                        "Executed the javascript '" + script + "'.",
                        "Executed the javascript:" + StringManagement.htmlContentToDisplayableHtmlCode(script));
            }catch (Exception e){
                testCase.logDifferentlyToTextLogAndHtmlLog(LogLevel.EXECUTION_PROBLEM,
                        "Errors while trying to run the javascript:" + SupportMethods.LF + script + SupportMethods.LF + "Error:" + SupportMethods.LF + e.toString(),
                        "Errors while trying to run the javascript:" + SupportMethods.LF + StringManagement.htmlContentToDisplayableHtmlCode(script) + SupportMethods.LF + "Error:" + SupportMethods.LF + e.toString());
                saveHtmlContentOfCurrentPage();
            }
        } else {
            testCase.logDifferentlyToTextLogAndHtmlLog(LogLevel.EXECUTION_PROBLEM,
                    "Attempted executing javascript, but browser type driver does not seem to be compatible. Javascript that did not run below:" + SupportMethods.LF + script,
                    "Attempted executing javascript, but browser type driver does not seem to be compatible. Javascript that did not run below:" + SupportMethods.LF + StringManagement.htmlContentToDisplayableHtmlCode(script));
            saveHtmlContentOfCurrentPage();
        }

    }


    /**
     * Check the page source for current page with the W3C Validator API for HTML consistency.
     *
     * @param verbose If set to true warning messages will be logged, as well as extra debugging information from the W3C validation service. If set to false only errors will be logged.
     */
    public void verifyCurrentPageSourceWithW3validator(boolean verbose){
        if(driver == null){
            log(LogLevel.EXECUTION_PROBLEM, "Driver is null.");
            haltFurtherExecution();
        }

        W3CHtmlValidatorService w3CHtmlValidatorService = new W3CHtmlValidatorService(testCase, driver.getPageSource(), verbose);
        w3CHtmlValidatorService.verifyPageSourceWithW3validator();
        if(w3CHtmlValidatorService.failed()) saveHtmlContentOfCurrentPage();
    }


    /**
     * Verifies that an element is enabled for interaction (displayed and enabled).
     *
     * @param guiElement The element to assess.
     */
    public void verifyIsEnabled(GuiElement guiElement){
        if(isEnabled(guiElement)){
            log(LogLevel.VERIFICATION_PASSED, "Element " + ((DomElement)guiElement).LogIdentification() + " found to be enabled as expected.");
        }else {
            long startTime = System.currentTimeMillis();
            DomElement domElement = (DomElement) guiElement;
            boolean enabled = false;
            WebElement webElement = null;
            while (!enabled && System.currentTimeMillis() - startTime < standardTimeoutInSeconds * 1000){
                webElement = getRuntimeElementWithoutLogging(domElement);
                if(webElement.isEnabled()) enabled = true;
            }
            if(webElement == null) {
                log(LogLevel.VERIFICATION_FAILED, "Element " + ((DomElement)guiElement).LogIdentification() + " was expected to be enabled, but could not be identified.");
                saveScreenshot(null);
                saveDesktopScreenshot();
                saveHtmlContentOfCurrentPage();
                writeRunningProcessListDeviationsSinceTestCaseStart();
                return;
            }
            if(!webElement.isDisplayed() && !enabled){
                log(LogLevel.VERIFICATION_FAILED, "Element " + ((DomElement)guiElement).LogIdentification() + " was expected to be enabled, but it's neither displayed, nor enabled.");
            }else if(!enabled){
                log(LogLevel.VERIFICATION_FAILED, "Element " + ((DomElement)guiElement).LogIdentification() + " was expected to be enabled. It seem to be enabled, but not displayed.");
            }else{
                log(LogLevel.VERIFICATION_FAILED, "Element " + ((DomElement)guiElement).LogIdentification() + " was expected to be enabled. It's enabled, but not displayed and cannot be used for interaction.");
            }
            saveScreenshot(webElement);
            saveDesktopScreenshot();
            saveHtmlContentOfCurrentPage();
            writeRunningProcessListDeviationsSinceTestCaseStart();
        }
    }

    /**
     * Changes what browser tab is currently activated.
     *
     * @param tabNameAsRegexForTabToSwitchTo The name of the tab to switch to.
     */
    public void switchBrowserTabWithTabNameGivenAsRegexPattern(String tabNameAsRegexForTabToSwitchTo){
        if(driver == null){
            log(LogLevel.EXECUTION_PROBLEM, "Driver is null.");
            haltFurtherExecution();
        }
        String currentTabId;
        String initialTitle;
        try
        {
            initialTitle = driver.getTitle();
            currentTabId = driver.getWindowHandle();
            log(LogLevel.DEBUG, "Switching browser tabs, trying to switch to a tab with title matching the regular expression pattern '" + tabNameAsRegexForTabToSwitchTo + "'. Initial browser tab title = '" + initialTitle + "' (tab id='" + currentTabId + "').");
        }
        catch (Exception e)
        {
            log(LogLevel.EXECUTION_PROBLEM, "Could not switch browser tab. Browser seem to be closed.");
            saveScreenshot(null);
            saveDesktopScreenshot();
            saveHtmlContentOfCurrentPage();
            writeRunningProcessListDeviationsSinceTestCaseStart();
            return;
        }

        for (String tabId : driver.getWindowHandles())
        {
            if ( !currentTabId.equals(tabId) )
            {
                driver.switchTo().window(tabId);
                String tabName = driver.getTitle();
                log(LogLevel.DEBUG, "Identified browser tab with tab title = '" + tabName + " (id='" + tabId + "').");
                if(SupportMethods.isRegexMatch(driver.getTitle(), tabNameAsRegexForTabToSwitchTo) ){
                    return;
                }
            }
        }
        log(LogLevel.EXECUTED, "Switched browser tab from tab '" + initialTitle + "' to tab with title '" + driver.getTitle() + "'. Matched with regular expression pattern '" + tabNameAsRegexForTabToSwitchTo + "'.");
    }


    /**
     * Changes what browser tab is currently activated.
     *
     * @param tabNameForTabToSwitchTo The name of the tab to switch to.
     */
    public void switchBrowserTab(String tabNameForTabToSwitchTo){
        if(driver == null){
            log(LogLevel.EXECUTION_PROBLEM, "Driver is null.");
            haltFurtherExecution();
        }
        String currentTabId;
        String initialTitle;
        try
        {
            initialTitle = driver.getTitle();
            currentTabId = driver.getWindowHandle();
            log(LogLevel.DEBUG, "Switching browser tabs, trying to switch to tab with title '" + tabNameForTabToSwitchTo + "'. Initial browser tab title = '" + initialTitle + "' (tab id='" + currentTabId + "').");
        }
        catch (Exception e)
        {
            log(LogLevel.EXECUTION_PROBLEM, "Could not switch browser tab. Browser seem to be closed.");
            saveScreenshot(null);
            saveDesktopScreenshot();
            saveHtmlContentOfCurrentPage();
            writeRunningProcessListDeviationsSinceTestCaseStart();
            return;
        }

        for (String tabId : driver.getWindowHandles())
        {
            if ( !currentTabId.equals(tabId) )
            {
                driver.switchTo().window(tabId);
                String tabName = driver.getTitle();
                log(LogLevel.DEBUG, "Identified browser tab with tab title = '" + tabName + " (id='" + tabId + "').");
                if(driver.getTitle().equals(tabNameForTabToSwitchTo) ){
                    return;
                }
            }
        }
        log(LogLevel.EXECUTED, "Switched browser tab from tab '" + initialTitle + "' to tab with title '" + driver.getTitle() + "'.");

    }

    /**
     * Closes the current browser tab. If it's the last one the browser is closed.
     */
    public void closeCurrentBrowserTab(){
        log(LogLevel.FRAMEWORK_ERROR, "Close current browser tab is not yet implemented.");
    }


    /**
     * Holds mouse cursor over given element
     *
     * @param guiElement Element to hover
     */
    public void hover(GuiElement guiElement){
        if(driver == null){
            log(LogLevel.EXECUTION_PROBLEM, "Driver is null.");
            haltFurtherExecution();
        }
        String javaScript = "var evObj = document.createEvent('MouseEvents');" +
                "evObj.initMouseEvent(\"mouseover\",true, false, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);" +
                "arguments[0].dispatchEvent(evObj);";
        try{
            ((JavascriptExecutor)driver).executeScript(javaScript, getRuntimeElementWithTimeout(((DomElement)guiElement), standardTimeoutInSeconds));
            log(LogLevel.EXECUTED, "Hover over " + ((DomElement)guiElement).LogIdentification() + ".");
        }catch (Exception e){
            log(LogLevel.EXECUTION_PROBLEM, "Could not hover over " + ((DomElement)guiElement).LogIdentification() + ".");
            saveScreenshot(getRuntimeElementWithoutLogging((DomElement)guiElement));
            saveDesktopScreenshot();
            saveHtmlContentOfCurrentPage();
            writeRunningProcessListDeviationsSinceTestCaseStart();

        }
    }

    /**
     * Returns if the element is able to interact with (actually if it is displayed and enabled)
     *
     * @param guiElement The element to check
     * @return Return true if the element is displayed and enabled.
     */
    @SuppressWarnings("WeakerAccess")
    public boolean isEnabled(GuiElement guiElement){
        DomElement domElement = (DomElement)guiElement;
        WebElement webElement = getRuntimeElementWithTimeout(domElement, standardTimeoutInSeconds);
        if(webElement == null) return false;
        boolean interactionable = (webElement.isEnabled() && webElement.isDisplayed());
        log(LogLevel.DEBUG, "Checking if " + ((DomElement)guiElement).LogIdentification() + " is interactionable and " + String.valueOf(interactionable).toLowerCase().replace("true", "it seems to be both displayed and enabled.").replace("false", " it is not."));
        return interactionable;
    }

    /**
     * Selecting multiple choices in a dropdown element (deselects what's in it to begin with.
     *
     * @param dropdownElement The element to interact with
     * @param selectedOptions The list of options to select, based on visible text
     */
    public void selectInMultipleChoiceDropdown(GuiElement dropdownElement, ArrayList<String> selectedOptions){
        selectInDropdownManager(dropdownElement, selectedOptions);
    }

    /**
     * Selecting an option in a dropdown element by visible text
     *
     * @param guiElement The element to interact with
     * @param selection The visible text of the option to choose
     */
    public void selectInDropdown(GuiElement guiElement, String selection){
        ArrayList<String> selectionsList = new ArrayList<>();
        selectionsList.add(selection);
        selectInDropdownManager(guiElement, selectionsList);
    }

    /**
     * Chooses a selection in a radio button set
     *
     * @param radioButtonContainer The element to interact with
     * @param text The visible text of the element to choose
     */
    public void chooseRadioButton(GuiElement radioButtonContainer, String text){
        DomElement domElement = (DomElement) radioButtonContainer;
        if(text == null) {
            log(LogLevel.DEBUG, "Did not choose anything in " + domElement.LogIdentification() + " since there was no input to select.");
            return;
        }

        WebElement webElement = getRuntimeElementWithTimeout(domElement, standardTimeoutInSeconds);
        if(webElement == null){
            log(LogLevel.EXECUTION_PROBLEM, "Could not identify radio button element " + domElement.LogIdentification() + " where '" + text + "' was supposed to be selected. Continuing test case execution nevertheless.");
            saveScreenshot(null);
            saveDesktopScreenshot();
            saveHtmlContentOfCurrentPage();
            writeRunningProcessListDeviationsSinceTestCaseStart();
            return;
        }
        if(!webElement.getTagName().toLowerCase().equals("form")){
            if(webElement.getTagName().toLowerCase().equals("input") && (webElement.getText().contains(text) || webElement.getAttribute("value").contains(text))){
                webElement.click();
                log(LogLevel.EXECUTED, "Clicked the '" + webElement.getAttribute("value") + "' radiobutton element.");
                return;
            }
            log(LogLevel.EXECUTION_PROBLEM, "Trying to select '" + text + "' in radio button set " + domElement.LogIdentification() + ". However the tag of the element is not 'form', but '" + webElement.getTagName() + "'.");
            saveScreenshot(webElement);
            saveDesktopScreenshot();
            saveHtmlContentOfCurrentPage();
            writeRunningProcessListDeviationsSinceTestCaseStart();
            return;
        }

        List<String> optionStrings = new ArrayList<>();
        try {
            List<WebElement> optionButtons = webElement.findElements(By.xpath("//*[@type='radio']"));
            log(LogLevel.DEBUG, "Found " + optionButtons.size() + " options for radiobutton.");
            if(optionButtons.size()==0){
                log(LogLevel.FRAMEWORK_ERROR, "Could not identify any radiobuttons within " + domElement.LogIdentification() + ". Does it contain elements of type 'radio'?");
                saveScreenshot(webElement);
                saveDesktopScreenshot();
                saveHtmlContentOfCurrentPage();
                writeRunningProcessListDeviationsSinceTestCaseStart();
                return;
            }
            for(WebElement optionButton : optionButtons){
                if(optionButton.isSelected()){
                    log(LogLevel.DEBUG, "Initial selected value in " + domElement.LogIdentification() + " was '" + optionButton.getText() + "'.");
                    if(optionButton.getText().equals(text)){
                        log(LogLevel.EXECUTED, "Made sure the radiobutton " + domElement.LogIdentification() + " had the value '" + text + "' checked, and it already did.");
                        return;
                    }
                }
                if(optionButton.isDisplayed()){
                    optionStrings.add(optionButton.getText() + " (value='" + optionButton.getAttribute("value") + "')" + String.valueOf(optionButton.isEnabled()).toLowerCase().replace("false", " (not enabled)").replace("true", ""));
                }else {
                    optionStrings.add(optionButton.getText() + " (hidden field)" + String.valueOf(optionButton.isEnabled()).toLowerCase().replace("false", " (not enabled)").replace("true", ""));
                }
            }
            for (WebElement optionButton : optionButtons){
                if(optionButton.getText().equals(text)){
                    optionButton.click();
                    log(LogLevel.EXECUTED, "Clicked the '" + text + "' radiobutton of " + domElement.LogIdentification() + ".");
                    return;
                }
            }
            for (WebElement optionButton : optionButtons){
                if(optionButton.getAttribute("value").equals(text)){
                    optionButton.click();
                    log(LogLevel.EXECUTED, "Clicked the '" + text + "' radiobutton of " + domElement.LogIdentification() + ".");
                    return;
                }
            }
            errorManagementProcedures("Could not click the '" + text + "' radiobutton of " + domElement.LogIdentification() + ". Available options are '" + String.join("', '", optionStrings) + "'.");
        }catch (Exception e){
            log(LogLevel.FRAMEWORK_ERROR, "Method 'chooseRadioButton()' crashed with error." + e.getMessage());
            saveScreenshot(webElement);
            saveDesktopScreenshot();
            saveHtmlContentOfCurrentPage();
            writeRunningProcessListDeviationsSinceTestCaseStart();
            haltFurtherExecution();
        }
    }

    /**
     * Ticking or un-ticking checkboxes. Common code to reduce duplicated code.
     *
     * @param checkboxElement The element to interact with
     * @param expectedToBeTicked True if expected to be ticked after procedure, false if expected to be un-ticked after procedure. If null is provided, execution will proceed without interaction.
     */
    public void manageCheckbox(GuiElement checkboxElement, Boolean expectedToBeTicked){
        long startTime = System.currentTimeMillis();
        DomElement domElement = (DomElement)checkboxElement;
        if (expectedToBeTicked == null){
            log(LogLevel.DEBUG, "Leaving checkbox " + domElement.LogIdentification() + " without interaction since input was null.");
            return;
        }
        WebElement webElement = null;
        boolean success = false;
        while (!success && (System.currentTimeMillis() - startTime <= standardTimeoutInSeconds * 1000)){
            webElement = getRuntimeElementWithoutLogging(domElement);
            if(webElement == null){
                wait(50);
                continue;
            }
            if(!webElement.getTagName().toLowerCase().equals("input") || !webElement.getAttribute("type").toLowerCase().equals("checkbox")){
                List<WebElement> subElements = webElement.findElements(By.xpath("//input"));
                if(subElements.size() == 1){
                    if(!subElements.get(0).isSelected() == expectedToBeTicked){
                        subElements.get(0).click();
                        log(LogLevel.EXECUTED, "Clicked the " + domElement.LogIdentification() + " to make it " + String.valueOf(expectedToBeTicked).toLowerCase().replace("true", "ticked").replace("false", "unticked") + ".");
                        success = true;
                        continue;
                    } else {
                        log(LogLevel.EXECUTED, "Made sure that " + domElement.LogIdentification() + " was " + String.valueOf(expectedToBeTicked).toLowerCase().replace("true", "ticked").replace("false", "un-ticked") + ". And it already was.");
                        success = true;
                        continue;
                    }
                }
                log(LogLevel.EXECUTION_PROBLEM, "Element " + domElement.LogIdentification() + " was expected to be a 'input' tag with the type 'checkbox', but it seem to be a '" + webElement.getTagName() + "' tag with type '" + webElement.getAttribute("type") + "'.");
                webElement = null;
                saveScreenshot(null);
                saveDesktopScreenshot();
                saveHtmlContentOfCurrentPage();
                writeRunningProcessListDeviationsSinceTestCaseStart();
                haltFurtherExecution();
            } else {
                try {
                    if(webElement.isSelected() == expectedToBeTicked){
                        log(LogLevel.EXECUTED, "Made sure the " + domElement.LogIdentification() + " was " + String.valueOf(expectedToBeTicked).toLowerCase().replace("true", "ticked").replace("false", "unticked") + ", and it already was.");
                        success = true;
                    } else {
                        webElement.click();
                        log(LogLevel.EXECUTED, "Clicked on the " + domElement.LogIdentification() + " checkbox since it was expected to be " + String.valueOf(expectedToBeTicked).toLowerCase().replace("true", "ticked").replace("false", "unticked") + " but it was not.");
                        success = true;
                    }
                } catch (Exception e){
                    log(LogLevel.FRAMEWORK_ERROR, "Something went wrong while interacting with the " + domElement.LogIdentification() + " checkbox. " + e.getMessage());
                    webElement = null;
                    errorManagementProcedures("This should not happen.");
                }
            }
        }
        if(webElement == null){
            errorManagementProcedures("Could not identify the checkbox " + domElement.LogIdentification() + ". Was supposed to " + String.valueOf(expectedToBeTicked).toLowerCase().replace("true", "tick").replace("false", "untick") + " it.");
        }
    }


    /**
     * Verifies that an image looks as expected
     *
     * @param guiElement The image to check
     * @param pathToOracleImage The oracle image to compare with
     */
    public void verifyImage(GuiElement guiElement, String pathToOracleImage){
        log(LogLevel.FRAMEWORK_ERROR, "Method 'verifyImage()' is not yet implemented.");
        saveScreenshot(getRuntimeElementWithoutLogging((DomElement)guiElement));
        saveDesktopScreenshot();
        saveHtmlContentOfCurrentPage();
        writeRunningProcessListDeviationsSinceTestCaseStart();

    }

    /**
     * Picks a value in a selector dropdown
     *
     * @param dropdownElement The element to interact with
     * @param selections The value(s) to choose
     */
    private void selectInDropdownManager(GuiElement dropdownElement, List<String> selections){
        DomElement domElement = (DomElement) dropdownElement;
        if(selections == null ||selections.size() == 0) {
            log(LogLevel.DEBUG, "Did not choose anything in " + domElement.LogIdentification() + " since there was no input to select.");
            return;
        }
        WebElement webElement = getRuntimeElementWithTimeout(domElement, standardTimeoutInSeconds);
        if(webElement == null) {
            errorManagementProcedures("Could not identify element " + domElement.LogIdentification() + " where '" + String.join("', '", selections) + "' was supposed to be selected. Continuing test case execution nevertheless.");
            return;
        }
        if(!webElement.getTagName().toLowerCase().equals("select"))
            errorManagementProcedures("Trying to select '" + String.join("', '", selections) + "' in dropdown " + domElement.LogIdentification() + ". However the tag of the element is not 'select', but '" + webElement.getTagName() + "'.");

        //Create Select element and log originally selected values
        boolean allSelectionsOkSoFar = true;
        List<String> nonSelectedStrings = new ArrayList<>();
        List<String> optionStrings = new ArrayList<>();
        try{
            Select select = new Select(webElement);
            List<String> selectedOptions = new ArrayList<>();
            for(WebElement selectedOption : select.getAllSelectedOptions()){
                selectedOptions.add(selectedOption.getText());
            }
            log(LogLevel.DEBUG, "Initial selected value(s) in " + domElement.LogIdentification() + ": '" + String.join("', '", selectedOptions) + "'.");

            //Save logging info - what options actually was enabled
            List<WebElement> options = select.getOptions();
            for(WebElement option : options){
                if(option.isDisplayed()){
                    optionStrings.add(option.getText() + String.valueOf(option.isEnabled()).toLowerCase().replace("false", " (not enabled)").replace("true", ""));
                }else {
                    optionStrings.add(option.getText() + " (hidden field)" + String.valueOf(option.isEnabled()).toLowerCase().replace("false", " (not enabled)").replace("true", ""));
                }
            }

            //Select options
            if(select.isMultiple()){
                select.deselectAll();
            }
            for(String selection : selections){
                boolean thisSelectionPerformed = false;
                for(WebElement option : options){
                    if(!option.isDisplayed() || !option.isEnabled()) continue;
                    if(option.getText().equals(selection)){
                        select.selectByVisibleText(selection);
                        thisSelectionPerformed = true;
                        break;
                    }
                }
                if(!thisSelectionPerformed){
                    nonSelectedStrings.add(selection);
                    allSelectionsOkSoFar = false;
                }
            }
        }catch (Exception e){
            log(LogLevel.FRAMEWORK_ERROR, "Something went terribly bad while trying to select '" + String.join("', '", selections) + "' in " + domElement.LogIdentification() + ". " + e.getMessage());
            errorManagementProcedures("This should not happen.");
        }

        //Log results
        if(allSelectionsOkSoFar){
            testCase.logDifferentlyToTextLogAndHtmlLog(LogLevel.DEBUG, "Found available options in " + domElement.LogIdentification() + ": '" + String.join("', '", optionStrings) + "'.",
                    "Found available options in " + domElement.LogIdentification() + ": '" + String.join("', '", optionStrings) + "'.");
            log(LogLevel.EXECUTED, "Selected '" + String.join("', '", selections) + "' in dropdown " + domElement.LogIdentification());
        } else {
            testCase.logDifferentlyToTextLogAndHtmlLog(LogLevel.EXECUTION_PROBLEM,
                    "Could not select '" + String.join("', '", nonSelectedStrings) + "' in element " + domElement.LogIdentification() + " when attempting to select '" + String.join("', '", selections) + "'. Available options are :'" + String.join("', '", optionStrings) + "'.",
                    "Could not select:<ul><li>'" + String.join("'</li><li>'", nonSelectedStrings) + "'</li></ul> in element " + domElement.LogIdentification() + " when attempting to select: <ul><li>'" + String.join("'</li><li>'", selections) + "'</li></ul>. Available options are:<ul><li>'" + String.join("'</li><li>'", optionStrings) + "'</li></ul>.");
            saveScreenshot(webElement);
            saveDesktopScreenshot();
            saveHtmlContentOfCurrentPage();
            writeRunningProcessListDeviationsSinceTestCaseStart();
            haltFurtherExecution();
        }
    }

    /**
     * Verifies if html table data holds expected data. Top row expected to hold headlines.
     *
     * @param guiElement The table to search
     * @param headlineColonValueSemicolonSeparatedString The data to find, in the pattern example of 'Headline1:ExpectedCorrespondingCellValue1;Headline2:ExpectedCorrespondingCellValue2'. If all values can be matched on the same row the test is passed.
     * @param regex True if match with regular expression pattern, false will check for cells containing the strings.
     */
    public void verifyTableRows(GuiElement guiElement, String[] headlineColonValueSemicolonSeparatedString, boolean regex){
        boolean doneOk = false;
        long startTime = System.currentTimeMillis();
        while (!doneOk && System.currentTimeMillis() - startTime <= standardTimeoutInSeconds * 1000){
            TableData tableData = tableDataFromGuiElement(guiElement, false);
            if(tableData == null ){
                DomElement table = (DomElement) guiElement;
                testCase.log(LogLevel.VERIFICATION_PROBLEM, "Table data for " + table.LogIdentification() + " is null.");
                saveScreenshot(getRuntimeElementWithoutLogging(table));
                saveDesktopScreenshot();
                saveHtmlContentOfCurrentPage();
                writeRunningProcessListDeviationsSinceTestCaseStart();
                return;
            }
            boolean nonErroneous = true;
            for(String searchPattern : headlineColonValueSemicolonSeparatedString){
                if(!tableData.rowExists(searchPattern, regex, null)){
                    nonErroneous = false;
                }
            }
            if(nonErroneous) doneOk = true;
        }
        TableData tableData = tableDataFromGuiElement(guiElement, true);
        if(tableData == null) return;
        tableData.verifyRows(headlineColonValueSemicolonSeparatedString, regex);
    }

    /**
     * Verifies existence of a row matching the stated values corresponding to the stated headlines.
     *
     * @param tableElement The table element.
     * @param headlineColonValueSemicolonSeparatedString The data to find, in the pattern example of 'Headline1:ExpectedCorrespondingCellValue1;Headline2:ExpectedCorrespondingCellValue2'. If all values can be matched on the same row the test is passed.
     * @param regex Matched as a regular expression pattern, or a check if the cell contains the value.
     */
    public void verifyTableRow(GuiElement tableElement, String headlineColonValueSemicolonSeparatedString, boolean regex){
        boolean doneOk = false;
        long startTime = System.currentTimeMillis();
        while (!doneOk && System.currentTimeMillis() - startTime <= standardTimeoutInSeconds * 1000){
            TableData tableData = tableDataFromGuiElement(tableElement, false);
            if(tableData == null ){
                DomElement table = (DomElement) tableElement;
                testCase.log(LogLevel.VERIFICATION_PROBLEM, "Table data for " + table.LogIdentification() + " is null.");
                saveScreenshot(getRuntimeElementWithoutLogging(table));
                saveDesktopScreenshot();
                saveHtmlContentOfCurrentPage();
                writeRunningProcessListDeviationsSinceTestCaseStart();
                return;
            }
            doneOk = tableData.rowExists(headlineColonValueSemicolonSeparatedString, regex, null);
        }
        TableData tableData = tableDataFromGuiElement(tableElement, true);
        if(tableData == null)return;
        tableData.verifyRow(headlineColonValueSemicolonSeparatedString, regex);

    }

    /**
     * Reloads the page. Similar as pressing F5 in the browser to refresh the page.
     */
    public void reloadPage(){
        driver.navigate().refresh();
    }

    /**
     * Verifies that the headline exists in the table.
     *
     * @param tableElement Table element
     * @param expectedHeadline Headline name, as seen in the table
     */
    public void verifyTableHeadline(GuiElement tableElement, String expectedHeadline){
        getRuntimeElementWithTimeout((DomElement)tableElement, standardTimeoutInSeconds);
        TableData tableData = tableDataFromGuiElement(tableElement, true);
        if(tableData == null) return;
        tableData.verifyHeadlineExist(expectedHeadline);
    }



    /**
     * Checks if a certain row exist in table.
     *
     * @param tableElement The table element
     * @param headlineColonValueSemicolonSeparatedString The pattern to find ('Headline1:CorrespondingDataValueOnRow;Headline2:CorrespondingDataValueForThisHeadline').
     * @param regex True if data value pattern is states as a regular expressions. Otherwise a check for cells containing the data value is performed.
     * @return Returns true if rows matching is found.
     */
    public boolean tableRowExists(GuiElement tableElement, String headlineColonValueSemicolonSeparatedString, boolean regex){
        return tableRowExists(tableElement, headlineColonValueSemicolonSeparatedString, regex, null);
    }

    /**
     * Checks if a certain row exist in table.
     *
     * @param tableElement The table element
     * @param headlineColonValueSemicolonSeparatedString The pattern to find ('Headline1:CorrespondingDataValueOnRow;Headline2:CorrespondingDataValueForThisHeadline').
     * @param regex True if data value pattern is states as a regular expressions. Otherwise a check for cells containing the data value is performed.
     * @param expectedMatchCount The number of expected row matches. If set to null tests will be passed if at least one row is matched.
     * @return Returns true if rows matching is found.
     */
    public boolean tableRowExists(GuiElement tableElement, String headlineColonValueSemicolonSeparatedString, boolean regex, Integer expectedMatchCount) {
        getRuntimeElementWithTimeout((DomElement)tableElement, standardTimeoutInSeconds);
        TableData tableData = tableDataFromGuiElement(tableElement, false);
        return tableData != null && tableData.rowExists(headlineColonValueSemicolonSeparatedString, regex, expectedMatchCount);
    }


    /**
     * Checks if a certain row exist in table. Gives the GUI table time to load
     *
     * @param tableElement The table element
     * @param headlineColonValueSemicolonSeparatedString The pattern to find ('Headline1:CorrespondingDataValueOnRow;Headline2:CorrespondingDataValueForThisHeadline').
     * @param regex True if data value pattern is states as a regular expressions. Otherwise a check for cells containing the data value is performed.
     * @param expectedMatchCount The number of expected row matches. If set to null tests will be passed if at least one row is matched.
     * @return Returns true if rows matching is found.
     */
    public boolean tableRowExistsWithTimeout(GuiElement tableElement, String headlineColonValueSemicolonSeparatedString, boolean regex, Integer expectedMatchCount) {
        boolean doneOk = false;
        long startTime = System.currentTimeMillis();
        while (!doneOk && System.currentTimeMillis() - startTime <= standardTimeoutInSeconds * 1000){
            TableData tableData = tableDataFromGuiElement(tableElement, false);
            if(tableData == null ) continue;
            doneOk = tableData.rowExists(headlineColonValueSemicolonSeparatedString, regex, expectedMatchCount);
        }
        return doneOk;
    }

    /**
     * Checks that the expected headlines exist in table
     *
     * @param tableElement The table element
     * @param expectedHeadlines The list of expected headlines
     */
    public void verifyTableHeadlines(GuiElement tableElement, List<String> expectedHeadlines){
        getRuntimeElementWithTimeout((DomElement)tableElement, standardTimeoutInSeconds);
        TableData tableData = tableDataFromGuiElement(tableElement, false);
        if(tableData == null) {
            testCase.log(LogLevel.FRAMEWORK_ERROR, "Could not construct TableData for HTML table " + ((DomElement)tableElement).LogIdentification() + ".");
            saveScreenshot(getRuntimeElementWithoutLogging((DomElement)tableElement));
            saveDesktopScreenshot();
            saveHtmlContentOfCurrentPage();
            writeRunningProcessListDeviationsSinceTestCaseStart();
            return;
        }
        tableData.verifyHeadlinesExist(expectedHeadlines);
    }

    /**
     * Checks if table data rows are empty/doesn't exist. Headlines does not count as data.
     *
     * @param tableElement The table element.
     * @return Return true if table is empty.
     */
    public boolean tableIsEmpty(GuiElement tableElement) {
        getRuntimeElementWithTimeout((DomElement)tableElement, standardTimeoutInSeconds);
        TableData tableData = tableDataFromGuiElement(tableElement, true);
        return tableData != null && tableData.tableIsEmpty();
    }

    private TableData tableDataFromGuiElement(GuiElement guiElement, boolean logErrors){
        DomElement domElement = (DomElement)guiElement;
        StringBuilder tableContent = new StringBuilder();
        WebElement tableElement = getRuntimeElementWithoutLogging(domElement);
        if(!tableElement.getTagName().toLowerCase().equals("table")){
            try {
                tableElement = tableElement.findElement(By.xpath(".//table"));
            }catch (Exception ignored){
            }
        }
        if(tableElement == null) {
            if(logErrors) testCase.log(LogLevel.VERIFICATION_PROBLEM, "Could nog find table " + domElement.LogIdentification() + " to verify data in.");
            saveScreenshot(null);
            saveDesktopScreenshot();
            saveHtmlContentOfCurrentPage();
            writeRunningProcessListDeviationsSinceTestCaseStart();
            return null;
        }
        List<WebElement> rows;
        try {
            rows = tableElement.findElements(By.xpath(".//tr"));
        }catch (Exception e){
            if(logErrors) testCase.log(LogLevel.VERIFICATION_PROBLEM, "Cannot get hold of table rows for HTML table " + domElement.LogIdentification() + ".");
            saveScreenshot(tableElement);
            saveDesktopScreenshot();
            saveHtmlContentOfCurrentPage();
            writeRunningProcessListDeviationsSinceTestCaseStart();
            return null;
        }
        for(WebElement row : rows){
            List<WebElement> cells;
            try{
                cells = row.findElements(By.xpath("(.//td|.//th)"));
            } catch (Exception e){
                if(logErrors) {
                    testCase.log(LogLevel.VERIFICATION_PROBLEM, "Cannot find any table cells for table " + domElement.LogIdentification() + " for row '" + row.toString() + "'.");
                    saveScreenshot(tableElement);
                    saveDesktopScreenshot();
                    saveHtmlContentOfCurrentPage();
                    writeRunningProcessListDeviationsSinceTestCaseStart();
                }
                return null;
            }
            for(WebElement cell : cells){
                try{
                    tableContent.append(cell.getText()).append(";");
                } catch (Exception e) {
                    log(LogLevel.DEBUG, "Could not read text from table cell. Replacing with ''.");
                    tableContent.append(";");
                }
            }
            tableContent.append(SupportMethods.LF);
        }
        return new TableData(tableContent.toString(), domElement.LogIdentification(), testCase, true);
    }

    /**
     * Navigates to specified url
     *
     * @param url String formed url
     * @throws NavigationError Error thrown if Navigation cannot be performed
     */
    private void goToUrl(String url) throws NavigationError{
        try{
            driver.navigate().to(url);
        }catch (Exception e){
            throw new NavigationError();
        }
    }

    /**
     * Writes the input string to specified DOM element
     *
     * @param element Selenium WebElement to interact with
     * @param text Text to enter
     * @param clearElement clears the existing element value
     * @throws TextEnteringError Error thrown when text cannot be entered by sendKeys method
     */
    private void enterText(WebElement element, String text, boolean clearElement) throws TextEnteringError{
        if(element != null){
            try {
                if( clearElement ) {
                    element.clear();
                    log(LogLevel.DEBUG, "Clearing existing text " + element.getText());
                }
                element.sendKeys(text);
                log(LogLevel.DEBUG, "Sending keys '" + text + "'.");
            }catch (Exception e){
                log(LogLevel.EXECUTION_PROBLEM, "Could not send keys '" + text + "'.");
                saveScreenshot(element);
                saveDesktopScreenshot();
                saveHtmlContentOfCurrentPage();
                writeRunningProcessListDeviationsSinceTestCaseStart();
                throw new TextEnteringError();
            }
        }else {
            log(LogLevel.EXECUTION_PROBLEM, "Could not send keys '" + text + "' since the webElement was null.");
            saveScreenshot(null);
            saveDesktopScreenshot();
            saveHtmlContentOfCurrentPage();
            writeRunningProcessListDeviationsSinceTestCaseStart();
            throw new TextEnteringError();
        }
    }

    /**
     * Saves the current HTML of the page interacted with to the testCaseLog folder for debugging purposes and write a testCaseLog post about it
     * Used for provide debugging information when execution or verification problems (or errors) occur.
     */
    public void saveHtmlContentOfCurrentPage(){
        if(driver == null){
            log(LogLevel.EXECUTION_PROBLEM, "Driver is null.");
            haltFurtherExecution();
        }

        String filePath = LogFolder.testRunLogFolder + testCase.testName + TestRun.fileCounter + ".html";
        String LF = SupportMethods.LF;
        String htmlStyle =                 "          pre              { font-family: Consolas, Menlo, Monaco, Lucida Console, Liberation Mono, DejaVu Sans Mono, Bitstream Vera Sans Mono, Courier New, monospace, serif;" + LF +
                "                             margin-bottom: 10px;" + LF +
                "                             overflow: auto;" + LF +
                "                             width: auto;" + LF +
                "                             padding: 5px;" + LF +
                "                             background-color: #eee;" + LF +
                "                             width: 100%;" + LF +
                "                             padding-bottom: 20px!ie7;"  + LF +
                "                             max - height: 600px;" + LF +
                "          }" + LF;

        String html =
                "<!DOCTYPE html>" + LF + "<html lang=\"en\">" + LF + LF +
                "   <head>" + LF +
                "      <title>Source code of page</title>" + LF +
                "      <style>" +
                htmlStyle +
                "      </style>" + LF +
                "   </head>" + LF +
                "   <body>" + LF +
                "         " + StringManagement.htmlContentToDisplayableHtmlCode(driver.getPageSource()) + LF + LF +
                "   </body>" + LF +
                "</html>" + LF;
        SupportMethods.saveToFile(html, filePath);
        testCase.logDifferentlyToTextLogAndHtmlLog(LogLevel.INFO, "Page source saved as '" + filePath.replace("\\", "/") + "'.", "<a href=\"" + TestRun.reportLinkPrefix() + "://" + filePath.replace("\\", "/") + "\" target=\"_blank\">View saved page (source)</a>");
        TestRun.fileCounter++;
    }


    /**
     * Halts further execution of the test case when further execution is considered non-valuable
     */
    public void haltFurtherExecution(){
        log(LogLevel.INFO, "Halting further execution due to perceived problems.");
        makeSureDriverIsClosed();
        testCase.report();
    }

    /**
     * Gets a runtime element from the WebDriver driver to be able to interact with it
     *
     * @param element Declared DomElement to interact with
     * @return WebElement for WebDriver interaction
     */
    public WebElement getRuntimeElementWithoutLogging(DomElement element){
        if(element == null) return null;
        List<WebElement> relevantWebElements = gatherRelevantElements(element);
        return mostRelevantElement(relevantWebElements, element);
    }

    /**
     * Gathers a list of relevant elements when trying to get hold of the WebElement corresponding to a DomElement.
     *
     * @param element The DomElement to find
     * @return Returns a list of relevant matches for DomElement
     */
    private List<WebElement> gatherRelevantElements(DomElement element){
        if(driver == null){
            log(LogLevel.EXECUTION_PROBLEM, "Driver is null.");
            haltFurtherExecution();
        }

        List<WebElement> webElements = new ArrayList<>();
        if(element == null) {
            log(LogLevel.DEBUG, "Trying to get relevant WebElements for DomElement that is null.");
            return webElements;
        }
        try {
            for(String recognitionString : element.recognitionStrings){
                if (element.identificationType == DomElement.IdentificationType.BY_LINK_TEXT) {
                    webElements.addAll(driver.findElements(By.linkText(recognitionString)));
                } else if (element.identificationType == DomElement.IdentificationType.BY_ID) {
                    webElements.addAll(driver.findElements(By.id(recognitionString)));
                } else if(element.identificationType == DomElement.IdentificationType.BY_X_PATH) {
                    webElements.addAll(driver.findElements(By.xpath(recognitionString)));
                } else if(element.identificationType == DomElement.IdentificationType.BY_NAME) {
                    webElements.addAll(driver.findElements(By.name(recognitionString)));
                } else if (element.identificationType == DomElement.IdentificationType.BY_CSS){
                    webElements.addAll(driver.findElements(By.cssSelector(recognitionString)));
                } else if (element.identificationType == DomElement.IdentificationType.BY_CLASS){
                    webElements.addAll(driver.findElements(By.className(recognitionString)));
                } else if (element.identificationType == DomElement.IdentificationType.BY_VISIBLE_TEXT){
                    webElements.addAll(driver.findElements(By.xpath("//*[.='" + recognitionString + "']")));
                    if(webElements.size() == 0){
                        webElements.addAll(driver.findElements(By.xpath("//*[contains(text(), '" + recognitionString + "')]")));
                    }
                }else {
                    log(LogLevel.FRAMEWORK_ERROR, "Tried to identify " + element.LogIdentification() + ", but the IdentificationType '" + element.identificationType.toString() + "' was not supported in getRuntimeElementWithoutLogging() method.");
                    saveDesktopScreenshot();
                    saveHtmlContentOfCurrentPage();
                }
            }
        }catch (Exception e){
            log(LogLevel.DEBUG, "Tried to identify " + element.LogIdentification() + ", but something went wrong. " + e.getMessage());
        }
        return webElements;
    }

    /**
     * If several elements are found, the displayed one is returned. If several still are displayed, returning the enabled one.
     * @param webElements List of elements
     * @param element The element (only for logging purposes)
     * @return Returns a webElement, or null if none are found.
     */
    private WebElement mostRelevantElement(List<WebElement> webElements, DomElement element){
        if (webElements.size() == 0) return null;

        //More than one match - trying to find best, most relevant match
        String debugString = "Found " + webElements.size() + " elements when trying to identify " + element.LogIdentification() + ". ";

        if(element.ordinalNumber != null){
            if(webElements.size() <= element.ordinalNumber){
                log(LogLevel.DEBUG, debugString + "Using WebElement #" + element.ordinalNumber + ", given by the DomElement object. ");
                return webElements.get(element.ordinalNumber + 1);
            } else {
                log(LogLevel.DEBUG, debugString + "The ordinal number given by the DomElement object was supposed to be " +
                        element.ordinalNumber + ", so it could not be matched.");
                return null;
            }
        }

        if (webElements.size() == 1) return webElements.get(0);

        List<WebElement> visibleElementsList = new ArrayList<>();
        for(WebElement webElement : webElements){
            if(webElement.isDisplayed()) visibleElementsList.add(webElement);
        }

        if(visibleElementsList.size() == 0){
            log(LogLevel.DEBUG, debugString + "None of these elements were visible. Returning first match and holding thumbs this is the best match.");
            return webElements.get(0);
        }

        if(visibleElementsList.size() == 1){
            log(LogLevel.DEBUG, debugString + "Only one of these was visible. Returning that element, assuming that was the element meant.");
            return visibleElementsList.get(0);
        }

        List<WebElement> enabledElements = new ArrayList<>();
        for(WebElement visibleElement : visibleElementsList){
            if(visibleElement.isEnabled()) enabledElements.add(visibleElement);
        }

        debugString += visibleElementsList.size() + " of these elements were visible, and out of these " + enabledElements.size() + " were enabled. ";

        if(enabledElements.size() == 0){
            log(LogLevel.DEBUG, debugString + "Using first visible match. No element was enabled, so no element seemed more likely than another.");
            return visibleElementsList.get(0);
        }

        if(enabledElements.size() == 1){
            log(LogLevel.DEBUG, debugString + "Assuming the only enabled element is the element to interact with.");
            return enabledElements.get(0);
        }

        log(LogLevel.DEBUG, debugString + visibleElementsList.size() + " of these was visible. Returning the first match in hope of successful execution.");
        return  visibleElementsList.get(0);
    }

    /**
     * Gets a runtime element from the WebDriver driver to be able to interact with it
     *
     * @param element Declared DomElement to interact with
     * @param timeoutInSeconds Number of seconds to wait for element before giving up on it
     * @return WebElement for WebDriver interaction
     */
    WebElement getRuntimeElementWithTimeout(DomElement element, int timeoutInSeconds){
        double startTickCount = System.currentTimeMillis();
        WebElement returnElement = getRuntimeElementWithoutLogging(element);
        long sleepTime = 50;
        while(returnElement == null && System.currentTimeMillis()- startTickCount < timeoutInSeconds *1000){
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            returnElement = getRuntimeElementWithoutLogging(element);
        }
        if(returnElement == null){
            log(LogLevel.DEBUG, "Could not identify element " + element.LogIdentification() + " within the " + timeoutInSeconds + " second timeout.");
        } else {
            log(LogLevel.DEBUG, "Identified element " + element.LogIdentification() + " after " + (System.currentTimeMillis() - startTickCount) + " ms.");
        }
        return returnElement;
    }

    /**
     * Performing driver shutdown procedures
     *
     * @throws BrowserClosingError Error thrown when driver cannot close browser
     */
    private void closeBrowserDriver() throws BrowserClosingError{
        try {
            driver.close();
            driver.quit();
        }catch (Exception e){
            throw new BrowserClosingError();
        }
    }

    /**
     * Saves debug information and halts further execution.
     *
     * @param errorMessage The error message to write to the test case log as a EXECUTION_PROBLEM log post.
     */
    private void errorManagementProcedures(String errorMessage){
        log(LogLevel.EXECUTION_PROBLEM, errorMessage);
        saveScreenshot(null);
        saveDesktopScreenshot();
        saveHtmlContentOfCurrentPage();
        writeRunningProcessListDeviationsSinceTestCaseStart();
        haltFurtherExecution();
    }

    public boolean pageTitleExistWithTimeout(String expectedPageTitle, int timeoutInSeconds){
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < timeoutInSeconds * 1000 && !driver.getTitle().equals(expectedPageTitle)){
            wait(100);
        }
        boolean success = driver.getTitle().equals(expectedPageTitle);
        long timeSpent = System.currentTimeMillis() - startTime;
        if(timeSpent > timeoutInSeconds * 1000){
            timeSpent = timeoutInSeconds * 1000;
        }
        if(success){
            testCase.log(LogLevel.DEBUG, "Waited for page title to become '" + expectedPageTitle + "', and that was identified after " + timeSpent + " milliseconds. ");
        } else {
            testCase.log(LogLevel.DEBUG, "Waited for page title to become '" + expectedPageTitle + "', but that did not happen within the " + timeoutInSeconds + " second timeout. Page title is '" + driver.getTitle() + "'.");
        }
        return success;
    }


}
