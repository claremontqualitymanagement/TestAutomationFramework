package se.claremont.autotest.websupport;

import org.openqa.selenium.logging.LogEntry;
import se.claremont.autotest.common.StringComparisonType;
import se.claremont.autotest.common.VerificationMethods;
import se.claremont.autotest.common.logging.LogLevel;
import se.claremont.autotest.websupport.brokenlinkcheck.BrokenLinkReporter;
import se.claremont.autotest.websupport.webdrivergluecode.WebInteractionMethods;

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
        brokenLinkReporter.reportBrokenLinks(true);
        return this;
    }

    public BrowserVerificationMethods noBrokenLinksOnCurrentPage_IncludeNonDisplayedLinks(){
        BrokenLinkReporter brokenLinkReporter = new BrokenLinkReporter(testCase, web.driver);
        brokenLinkReporter.reportBrokenLinks(false);
        return this;
    }

    public BrowserVerificationMethods title(String expectedTitlePattern, StringComparisonType stringComparisonType){
        boolean isMatch = stringComparisonType.match(web.driver.getTitle(), expectedTitlePattern);
        long timer = System.currentTimeMillis();
        while (!isMatch && ((System.currentTimeMillis() - timer) < web.getStandardTimeout() * 10000)){
            try {
                Thread.sleep(50);
            } catch (InterruptedException ignored) { }
            isMatch = stringComparisonType.match(web.driver.getTitle(), expectedTitlePattern);
        }
        if(isMatch){
            testCase.log(LogLevel.VERIFICATION_PASSED, "Current title matched '" + expectedTitlePattern + "' as expected.");
        } else {
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
        } else {
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
     */
    public BrowserVerificationMethods currentPageSourceWithW3validator(boolean verbose){
        if(web.driver == null){
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Driver is null.");
            web.haltFurtherExecution();
        }

        W3CHtmlValidatorService w3CHtmlValidatorService = new W3CHtmlValidatorService(testCase, web.driver.getPageSource(), verbose);
        w3CHtmlValidatorService.verifyPageSourceWithW3validator();
        if(w3CHtmlValidatorService.failed()) web.saveHtmlContentOfCurrentPage();
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
        } else {
            testCase.log(LogLevel.VERIFICATION_PASSED, "Browser had no severe log posts in console.");
        }
    }
}
