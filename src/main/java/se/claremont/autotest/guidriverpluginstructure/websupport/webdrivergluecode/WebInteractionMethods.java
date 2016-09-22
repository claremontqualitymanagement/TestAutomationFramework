package se.claremont.autotest.guidriverpluginstructure.websupport.webdrivergluecode;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import se.claremont.autotest.common.CliTestRunner;
import se.claremont.autotest.common.LogFolder;
import se.claremont.autotest.common.LogLevel;
import se.claremont.autotest.common.TestCase;
import se.claremont.autotest.guidriverpluginstructure.GuiDriver;
import se.claremont.autotest.guidriverpluginstructure.GuiElement;
import se.claremont.autotest.guidriverpluginstructure.websupport.DomElement;
import se.claremont.autotest.support.SupportMethods;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Methods for interaction with web elements in a web page DOM. Utilizes Selenium WebDriver components.
 *
 * Created by jordam on 2016-08-17.
 */
public class WebInteractionMethods implements GuiDriver {
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
            driver = WebDriverManager.initializeWebDriver(webBrowser, testCase);
            driver.manage().window().maximize();
        }catch (Exception e){
            log(LogLevel.FRAMEWORK_ERROR, "Could not initialize driver.");
            saveScreenshot();
        }
    }

    /**
     * Navigates to specified url
     *
     * @param url String formed url
     * @throws NavigationError Error thrown if Navigation cannot be performed
     */
    private void goToUrl(String url) throws NavigationError{
        try{
            driver.get(url);
        }catch (Exception e){
            throw new NavigationError();
        }
    }

    /**
     * Writes a testCaseLog post to the testCaseLog
     *
     * @param logLevel The testCaseLog level of the testCaseLog post
     * @param message The testCaseLog message string
     */
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
            log(LogLevel.EXECUTED, "Navigation performed to url '" + url + "'.");
        }catch (NavigationError e){
            log(LogLevel.EXECUTION_PROBLEM, "Could not navigate to url '" + url + "'.");
        }
    }


    /**
     * Writes the input string to specified DOM element
     *
     * @param element Selenium WebElement to interact with
     * @param text Text to enter
     * @throws TextEnteringError Error thrown when text cannot be entered by sendKeys method
     */
    private void enterText(WebElement element, String text) throws TextEnteringError{
        if(element != null){
            try {
                element.sendKeys(text);
            }catch (Exception e){
                log(LogLevel.EXECUTION_PROBLEM, "Could not send keys '" + text + "'.");
                throw new TextEnteringError();
            }
            log(LogLevel.DEBUG, "Sending keys '" + text + "'.");
        }else{
            log(LogLevel.EXECUTION_PROBLEM, "Could not send keys '" + text + "' since the webElement was null.");
            saveScreenshot();
            saveHtmlContentOfCurrentPage();
        }
    }

    /**
     * Reads the text from an element
     *
     * @param guiElement The element to read the current text from
     * @return the current text in the element
     */
    public String getText(GuiElement guiElement){
        DomElement domElement = (DomElement) guiElement;
        String text = null;
        WebElement webElement = getRuntimeElementWithTimeout(domElement, standardTimeoutInSeconds);
        if(webElement != null){
            text = webElement.getText();
        } else{
            log(LogLevel.EXECUTION_PROBLEM, "Could not retrieve text from element " + domElement.LogIdentification() + " since it could not be identified at runtime.");
            saveScreenshot();
            saveHtmlContentOfCurrentPage();
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
        try {
            WebDriverWait wait = new WebDriverWait(driver, 5);
            wait.until(ExpectedConditions.alertIsPresent());
            Alert alert = driver.switchTo().alert();
            alert.accept();
        } catch (Exception e) {
            //exception handling
        }
    }

    /**
     * Re-scaling the browser window to a new size.
     *
     * @param width The new width of the browser window
     * @param height The new height of the browser window
     */
    public void setBrowserWindowSize(int width, int height) {
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
     * @param expectedAttributevalue The expected attribute of the element
     */
    public void verifyElementAttribute(GuiElement linkElement, String attributeName, String expectedAttributevalue){
        DomElement domElement = (DomElement) linkElement;
        try{
            WebElement element = getRuntimeElementWithTimeout(domElement, standardTimeoutInSeconds);
            if(element.getAttribute("href").equals(expectedAttributevalue)){
                log(LogLevel.VERIFICATION_PASSED, "Element " + domElement.LogIdentification() + " was found to have the expected attribute value of '" + expectedAttributevalue + "' for attribute '" + attributeName + "'.");
            } else {
                log(LogLevel.VERIFICATION_FAILED, "Element " + domElement.LogIdentification() + " was expected to have the value '" + expectedAttributevalue + "' for attribute '" + attributeName + "' but actually had the value of '" + element.getAttribute(attributeName) + "'.");
            }
        } catch (Exception e){
            log(LogLevel.VERIFICATION_PROBLEM, "Could not check the attribute '" + attributeName + "' of element " + domElement.LogIdentification() + " (was expected to have the value '" + expectedAttributevalue + "'." + SupportMethods.LF + e.toString() );
        }
    }


    /**
     * Writes text to the given element.
     *
     * @param guiElement The element to write to
     * @param textToWrite The text to write
     */
    public void write(GuiElement guiElement, String textToWrite){
        DomElement domElement = (DomElement) guiElement;
        WebElement webElement = getRuntimeElementWithTimeout(domElement, standardTimeoutInSeconds);
        try {
            enterText(webElement, textToWrite);
        }catch (Exception e){
            log(LogLevel.EXECUTION_PROBLEM, "Could not enter the text '" + textToWrite + "' to element " + domElement.LogIdentification() + ". ");
            saveScreenshot();
            saveHtmlContentOfCurrentPage();
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
        try {
            WebElement webElement = getRuntimeElementWithTimeout(domElement, standardTimeoutInSeconds);
            enterText(webElement, text);
            try{
                webElement.submit();
                log(LogLevel.DEBUG, "Submitted text '" + text + "' to " + domElement.LogIdentification() + ".");
            }catch (Exception e){
                log(LogLevel.EXECUTION_PROBLEM, "Could not submit the text entered to " + domElement.LogIdentification() + ".");
                saveScreenshot();
                saveHtmlContentOfCurrentPage();
            }
        } catch (TextEnteringError textEnteringError) {
            log(LogLevel.EXECUTION_PROBLEM, "Could not enter '" + text + "' in " + domElement.LogIdentification() + "-");
            saveScreenshot();
            saveHtmlContentOfCurrentPage();
        }
    }

    /**
     * Saves the current HTML of the page interacted with to the testCaseLog folder for debugging purposes and write a testCaseLog post about it
     * Used for provide debugging information when execution or verification problems (or errors) occur.
     */
    public void saveHtmlContentOfCurrentPage(){
        String filePath = LogFolder.testRunLogFolder + testCase.testName + CliTestRunner.testRun.fileCounter + ".html";
        String html = driver.getPageSource();
        SupportMethods.saveToFile(html, filePath);
        testCase.logDifferentlyToTextLogAndHtmlLog(LogLevel.DEVIATION_EXTRA_INFO, "Page source saved as '" + filePath + "'.", "<a href=\"file://" + filePath + "\" target=\"_blank\">View saved page (source)</a>");
        CliTestRunner.testRun.fileCounter++;
    }

    /**
     * Saves a screenshot of the web browser content to the testCaseLog folder and writes a testCaseLog post about it.
     * Used for provide debugging information when execution or verification problems (or errors) occur.
     */
    private void saveScreenshot(){
        String filePath = LogFolder.testRunLogFolder + testCase.testName + CliTestRunner.testRun.fileCounter + ".png";
        System.out.println("Saving screenshot of web browser content to '" + filePath + "'.");
        CliTestRunner.testRun.fileCounter++;
        byte[] fileImage = null;
        try{
            fileImage = ((TakesScreenshot)driver).getScreenshotAs(OutputType.BYTES);
        }catch (Exception e){
            log(LogLevel.FRAMEWORK_ERROR, "Could not take screenshot. Is driver ok? " + e.toString());
        }
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
    }

    /**
     * Halts further execution of the test case when further execution is considered non-valuable
     */
    private void haltFurtherExecution(){
        log(LogLevel.INFO, "Halting further execution due to perceived problems.");
        //closeBrowser();
        testCase.report();
    }

    /**
     * Gets a runtime element from the WebDriver driver to be able to interact with it
     *
     * @param element Declared DomElement to interact with
     * @return WebElement for WebDriver interaction
     */
    private WebElement getRuntimeElement(DomElement element){
        WebElement returnElement = null;
        try {
            if (element.identificationType == DomElement.IdentificationType.BY_LINK_TEXT) {
                returnElement = driver.findElement(By.linkText(element.recognitionString));
            } else if (element.identificationType == DomElement.IdentificationType.BY_ID) {
                returnElement = driver.findElement(By.id(element.recognitionString));
            } else if(element.identificationType == DomElement.IdentificationType.BY_X_PATH) {
                returnElement = driver.findElement(By.xpath(element.recognitionString));
            } else if(element.identificationType == DomElement.IdentificationType.BY_NAME) {
                returnElement = driver.findElement(By.name(element.recognitionString));
            } else if (element.identificationType == DomElement.IdentificationType.BY_CSS){
                returnElement = driver.findElement(By.cssSelector(element.recognitionString));
            } else {
                returnElement = null;
            }
        }catch (Exception e){
            log(LogLevel.DEBUG, "Could not get element by '" + element.recognitionString +
                    "' with identification technique '" + element.identificationType.toString() + "'.");
        }
        return returnElement;
    }

    /**
     * Gets a runtime element from the WebDriver driver to be able to interact with it
     *
     * @param element Declared DomElement to interact with
     * @return WebElement for WebDriver interaction
     */
    private WebElement getRuntimeElementWithoutLogging(DomElement element){
        WebElement returnElement = null;
        try {
            if (element.identificationType == DomElement.IdentificationType.BY_LINK_TEXT) {
                returnElement = driver.findElement(By.linkText(element.recognitionString));
            } else if (element.identificationType == DomElement.IdentificationType.BY_ID) {
                returnElement = driver.findElement(By.id(element.recognitionString));
            } else if(element.identificationType == DomElement.IdentificationType.BY_X_PATH) {
                returnElement = driver.findElement(By.xpath(element.recognitionString));
            } else if(element.identificationType == DomElement.IdentificationType.BY_NAME) {
                returnElement = driver.findElement(By.name(element.recognitionString));
            } else if (element.identificationType == DomElement.IdentificationType.BY_CSS){
                returnElement = driver.findElement(By.cssSelector(element.recognitionString));
            } else {
                returnElement = null;
            }
        }catch (Exception ignored){
        }
        return returnElement;
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
        return getRuntimeElement(domElement) != null;
    }

    /**
     * Gets a runtime element from the WebDriver driver to be able to interact with it
     *
     * @param element Declared DomElement to interact with
     * @param timeoutInSeconds Number of seconds to wait for element before giving up on it
     * @return WebElement for WebDriver interaction
     */
    private WebElement getRuntimeElementWithTimeout(DomElement element, int timeoutInSeconds){
        WebElement returnElement = getRuntimeElementWithoutLogging(element);
        long sleepTime = 50;
        double startTickCount = System.currentTimeMillis();
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
     * Changing the standard timeout value for waiting for objects in the GUI for most methods.
     *
     * @param standardTimeoutInSeconds The new value for standard timeout, in seconds.
     */
    public void setStandardTimeout(int standardTimeoutInSeconds){
        log(LogLevel.DEBUG, "Resetting standard timeout from " + this.standardTimeoutInSeconds + " seconds to " + standardTimeoutInSeconds + " seconds.");
        this.standardTimeoutInSeconds = standardTimeoutInSeconds;
    }

    /**
     * Performing a click event on an element
     *
     * @param guiElement the GUI element to click
     */
    public void click(GuiElement guiElement){
        DomElement element = (DomElement) guiElement;
        log(LogLevel.DEBUG, "Attempting to click on " + element.LogIdentification() + ".");
        try {
            WebElement webelement = getRuntimeElementWithTimeout(element, standardTimeoutInSeconds);
            if(webelement == null){
                log(LogLevel.DEBUG, "Element does not exist.");
                log(LogLevel.EXECUTION_PROBLEM, "Could not click on element " + element.LogIdentification() + ".");
                saveScreenshot();
                saveHtmlContentOfCurrentPage();
                haltFurtherExecution();
            } else{
                webelement.click();
                log(LogLevel.EXECUTED, "Clicked the " + element.LogIdentification()+ " element.");
            }
        }catch (Exception e){
            if(e.toString().contains("Other element would receive the click")){
                log(LogLevel.EXECUTION_PROBLEM, "It seems something is blocking the possibility to click on " + element.LogIdentification() + ". It could for example be a popup overlaying the element?");
            } else {
                testCase.logDifferentlyToTextLogAndHtmlLog(LogLevel.FRAMEWORK_ERROR, "Could not click on element " + element.LogIdentification() + ". " + e.toString(), "Could not click och element " + element.LogIdentification() + ".<br>Error:<br><br>" + e.toString());
            }
            saveScreenshot();
            saveHtmlContentOfCurrentPage();
            haltFurtherExecution();
        }
    }

    /**
     * Performing driver shutdown procedures
     *
     * @throws BrowserClosingError Error thrown when driver cannot close browser
     */
    private void closeBrowserDriver() throws BrowserClosingError{
        try {
            driver.close();
        }catch (Exception e){
            throw new BrowserClosingError();
        }
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
            saveScreenshot();
            saveHtmlContentOfCurrentPage();
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
        WebElement webElement = getRuntimeElementWithTimeout(domElement, standardTimeoutInSeconds);
        if(webElement == null){
            log(LogLevel.DEBUG, "Object " + domElement.LogIdentification() + " could not be identified in the html.");
            log(LogLevel.VERIFICATION_PASSED, "Object " + domElement.LogIdentification() + " verified to not be present.");
        } else if(!webElement.isDisplayed()) {
            log(LogLevel.DEBUG, "Object " + domElement.LogIdentification() + " could be identified in the html, but it's suppressed from being displayed in the GUI.");
            log(LogLevel.VERIFICATION_PASSED, "Object " + domElement.LogIdentification() + " verified to not displayed.");
        }else {
            log(LogLevel.VERIFICATION_FAILED, "Object " + domElement.LogIdentification() + " was identified as displayed although expected to not be displayed.");
            saveScreenshot();
            saveHtmlContentOfCurrentPage();
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
     * Checks if the given element is displayed in the GUI.
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
     * Checks if the given object is displayed in the HTML. Compare with verifyObjectExistence, that checks if the element exist in the html.
     *
     * @param guiElement The GUI element to find
     */
    public void verifyObjectIsDisplayed(GuiElement guiElement){
        DomElement domElement = (DomElement) guiElement;
        WebElement webElement = getRuntimeElementWithTimeout(domElement, standardTimeoutInSeconds);
        if(webElement == null){
            log(LogLevel.VERIFICATION_FAILED, "Object " + domElement.LogIdentification() + " was expected to be displayed but could not be identified at all.");
            saveScreenshot();
            saveHtmlContentOfCurrentPage();
        } else if(!webElement.isDisplayed()) {
            log(LogLevel.VERIFICATION_FAILED, "Object " + domElement.LogIdentification() + " is present, but the display of the object is suppressed.");
            saveScreenshot();
            saveHtmlContentOfCurrentPage();
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
            saveScreenshot();
            saveHtmlContentOfCurrentPage();
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
            saveScreenshot();
            saveHtmlContentOfCurrentPage();
        } else {
            log(LogLevel.VERIFICATION_PASSED, "Existence of object " + domElement.LogIdentification() + " verified.");
        }
    }

    /**
     * Checks if specified text exist in the browser page
     *
     * @param text test string to find
     */
    public void verifyTextOnCurrentPage(String text){
        DomElement domElement = new DomElement("//*[contains(text(),'" + text + "')]", DomElement.IdentificationType.BY_X_PATH);
        //List<WebElement> list = driver.findElements(By.xpath("//*[contains(text(),'" + text + "')]"));
        WebElement webElement = getRuntimeElementWithTimeout(domElement, standardTimeoutInSeconds);
        if(webElement != null){
            log(LogLevel.VERIFICATION_PASSED, "The text '" + text + "' could be found on the current page.");
        }else {
            log(LogLevel.VERIFICATION_FAILED, "The text '" + text + "' could not be found on the current page.");
            saveScreenshot();
            saveHtmlContentOfCurrentPage();
        }
    }

    /**
     * Checks the web browser page title against expected result
     *
     * @param expectedTitle Expected current title of the web page
     */
    @SuppressWarnings("unused")
    public void verifyPageTitle(String expectedTitle){
        String currentTitle = driver.getTitle();
        if(currentTitle.equals(expectedTitle)){
            log(LogLevel.VERIFICATION_PASSED, "The current page title was '" + expectedTitle + "' as expected.");
        }else {
            log(LogLevel.VERIFICATION_FAILED, "The current page title was expected to be '" + expectedTitle + "' but was '" + currentTitle + "'.");
            saveScreenshot();
            saveHtmlContentOfCurrentPage();
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
            saveScreenshot();
            saveHtmlContentOfCurrentPage();
        }
    }

    /**
     * Picks a value in a dropdown
     *
     * @param guiElement The element to interact with
     * @param choice The value to choose
     */
    public void chooseInDropdown(GuiElement guiElement, String choice){
        log(LogLevel.FRAMEWORK_ERROR, "Method 'chooseInDropdown()' is not yet implemented.");
    }

    /**
     * Chooses a selection in a radio button set
     *
     * @param guiElement The element to interact with
     * @param text The text of the element to choose
     */
    public void chooseRadioButton(GuiElement guiElement, String text){
        log(LogLevel.FRAMEWORK_ERROR, "Method 'chooseRadioButton()' is not yet implemented.");
    }

    /**
     * Verifies that an image looks as expected
     *
     * @param guiElement The image to check
     * @param pathToOracleImage The oracle image to compare with
     */
    public void verifyImage(GuiElement guiElement, String pathToOracleImage){
        log(LogLevel.FRAMEWORK_ERROR, "Method 'verifyImage()' is not yet implemented.");
    }

}
