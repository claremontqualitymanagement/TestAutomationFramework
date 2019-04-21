package se.claremont.taf.websupport.webdrivergluecode;

import org.openqa.selenium.logging.LogEntry;
import se.claremont.taf.core.StringComparisonType;
import se.claremont.taf.core.VerificationMethods;
import se.claremont.taf.core.guidriverpluginstructure.GuiElement;
import se.claremont.taf.core.logging.LogLevel;
import se.claremont.taf.websupport.W3CHtmlValidatorService;
import se.claremont.taf.websupport.brokenlinkcheck.BrokenLinkReporter;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class BrowserVerificationMethods extends VerificationMethods {

    private WebInteractionMethods web;

    public BrowserVerificationMethods(WebInteractionMethods web){
        super(web.getTestCase());
        this.web = web;
    }

    public BrowserVerificationMethods noBrokenLinksOnCurrentPage(){
        BrokenLinkReporter brokenLinkReporter = new BrokenLinkReporter(testCase, web.driver);
        wasSuccess = brokenLinkReporter.reportBrokenLinks(true);
        if(!wasSuccess) noFailsInBuilderChain = false;
        return this;
    }

    public BrowserVerificationMethods noBrokenLinksOnCurrentPage_IncludeNonDisplayedLinks(){
        BrokenLinkReporter brokenLinkReporter = new BrokenLinkReporter(testCase, web.driver);
        wasSuccess = brokenLinkReporter.reportBrokenLinks(false);
        if(!wasSuccess) noFailsInBuilderChain = false;
        return this;
    }

    public ElementVerificationMethods verifyElement(GuiElement guiElement){
        return new ElementVerificationMethods(guiElement, web, noFailsInBuilderChain);
    }

    public BrowserVerificationMethods title(String expectedTitlePattern, StringComparisonType stringComparisonType){
        boolean isMatch = stringComparisonType.match(web.driver.getTitle(), expectedTitlePattern);
        long timer = System.currentTimeMillis();
        while (!isMatch && ((System.currentTimeMillis() - timer) < web.getStandardTimeout() * 1000)){
            try {
                Thread.sleep(50);
            } catch (InterruptedException ignored) { }
            isMatch = stringComparisonType.match(web.driver.getTitle(), expectedTitlePattern);
        }
        if(isMatch){
            testCase.log(LogLevel.VERIFICATION_PASSED, "Current title matched '" + expectedTitlePattern + "' as expected.");
            wasSuccess = true;
        } else {
            wasSuccess = false;
            noFailsInBuilderChain = false;
            testCase.log(LogLevel.VERIFICATION_FAILED, "Expected title to match '" + expectedTitlePattern + "', but it was '" + web.driver.getTitle() + "'.");
            web.saveScreenshot(null);
            web.saveDesktopScreenshot();
            web.saveHtmlContentOfCurrentPage();
            web.writeRunningProcessListDeviationsSinceTestCaseStart();
        }
        return this;
    }

    public BrowserVerificationMethods textOnPage(String pattern, StringComparisonType stringComparisonType){
        boolean success = stringComparisonType.match(web.driver.getPageSource(), pattern);
        long startTime = System.currentTimeMillis();
        while (!success && (System.currentTimeMillis() - startTime) < web.getStandardTimeout() * 1000){
            try{
                Thread.sleep(50);
            }catch (Exception ignored){}
            success = stringComparisonType.match(web.driver.getPageSource(), pattern);
        }
        if(success){
            testCase.log(LogLevel.VERIFICATION_PASSED, "Pattern '" + pattern + "' successfully matched in page source.");
            wasSuccess = true;
        } else {
            wasSuccess = false;
            noFailsInBuilderChain = false;
            testCase.log(LogLevel.VERIFICATION_FAILED, "Could not match pattern '" + pattern + "' in page source.");
            web.saveScreenshot(null);
            web.saveDesktopScreenshot();
            web.saveHtmlContentOfCurrentPage();
            web.writeRunningProcessListDeviationsSinceTestCaseStart();
        }
        return this;
    }

    /**
     * Check the page source for current page with the W3C Validator API for HTML consistency.
     *
     * @param verbose If set to true warning messages will be logged, as well as extra debugging information from the W3C validation service. If set to false only errors will be logged.
     * @return Methods for verifications
     */
    public BrowserVerificationMethods currentPageSourceWithW3validator(boolean verbose){
        if(web.driver == null){
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Driver is null.");
            web.haltFurtherExecution();
        }

        W3CHtmlValidatorService w3CHtmlValidatorService = new W3CHtmlValidatorService(testCase, web.driver.getPageSource(), verbose);
        w3CHtmlValidatorService.verifyPageSourceWithW3validator();
        if(w3CHtmlValidatorService.failed()) {
            web.saveHtmlContentOfCurrentPage();
            noFailsInBuilderChain = false;
            wasSuccess = false;
        } else {
            wasSuccess = true;
        }
        return this;
    }

    /**
     * Logs severe entries in browser console, client log, and driver log to test case log. Clears browser console in the process.
     */
    public void browserConsoleHasNoErrors_AlsoClearsBrowserConsole(){
        List<LogEntry> logEntries = web.getLogEntriesFromBrowser(Level.SEVERE);
        List<String> logEntriesAsStrings = new ArrayList<>();
        for(LogEntry logEntry : logEntries){
            logEntriesAsStrings.add(logEntry.toString());
        }
        if(logEntries.size() > 0){
            testCase.log(LogLevel.VERIFICATION_FAILED, "Browser has the following log posts of at least log level 'severe' in console:" + System.lineSeparator() + String.join(System.lineSeparator(), logEntriesAsStrings));
            wasSuccess = false;
            noFailsInBuilderChain = false;
        } else {
            testCase.log(LogLevel.VERIFICATION_PASSED, "Browser had no severe log posts in console.");
            wasSuccess = true;
        }
    }
}
