package se.claremont.autotest.websupport.brokenlinkcheck;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import se.claremont.autotest.common.logging.LogLevel;
import se.claremont.autotest.common.reporting.UxColors;
import se.claremont.autotest.common.testcase.TestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jordam on 2017-04-01.
 */
public class BrokenLinkReporter {
    TestCase testCase;
    WebDriver driver;
    ArrayList<String[]> linkCheckResults = null;
    long startTime = 0;
    LogLevel logLevel = LogLevel.VERIFICATION_PASSED;

    public BrokenLinkReporter(TestCase testCase, WebDriver driver){
        this.testCase = testCase;
        this.driver = driver;
    }

    /**
     * Checks current page for broken links and reports results to log as verifications.
     * TODO: Should also add context path to local links
     */
    public void reportBrokenLinks(boolean onlyDisplayedLinks){
        log(LogLevel.DEBUG, "Initiating a check for broken links on current page (URL: '" + driver.getCurrentUrl() + "').");
        startTime = System.currentTimeMillis();
        linkCheckResults = getLinkCheckResults(onlyDisplayedLinks);
        setLogLevel();
        testCase.logDifferentlyToTextLogAndHtmlLog(logLevel, getReportAsText(), getReportAsHtml());
    }

    private void setLogLevel(){
        for(String[] linkresults : linkCheckResults){
            if(linkresults == null)continue;
            String responseCode = linkresults[1];
            if(responseCode != null && (responseCode.startsWith("2") || responseCode.startsWith("3"))){
                continue;
            } else if (responseCode == null || responseCode == "null"){
                if(logLevel != LogLevel.VERIFICATION_FAILED) logLevel = LogLevel.VERIFICATION_PROBLEM;
            } else {
                logLevel = LogLevel.VERIFICATION_FAILED;
                return;
            }
        }
    }

    private List<WebElement> getAllLinksOnCurrentPage(){
        return driver.findElements(By.xpath("//a"));
    }

    private ArrayList<String[]> getLinkCheckResults(boolean onlyDisplayedLinks){
        ArrayList<String[]> resultsTableRows = new ArrayList<>();
        List<Thread> linkCheckingThreads = new ArrayList<>();
        for(WebElement link : getAllLinksOnCurrentPage()){
            if(link == null)continue;
            try{
                if(onlyDisplayedLinks && !link.isDisplayed())continue;
                Thread linkCheck = new Thread(new LinkCheck(resultsTableRows, link.getAttribute("href")));
                linkCheckingThreads.add(linkCheck);
                linkCheck.start();
            }catch (Exception e){
                System.out.println("Problems checking link: '" + link + "': " + e.toString());
            }
        }

        //Code below for waiting for all threads to finish due to log timing issues
        //noinspection ForLoopReplaceableByForEach
        for(int i = 0; i < linkCheckingThreads.size(); i++)
            try {
                linkCheckingThreads.get(i).join();
            } catch (InterruptedException e) {
                log(LogLevel.FRAMEWORK_ERROR, "Problems with checking links on page. Error: " + e.getMessage());
            }
        return resultsTableRows;
    }

    private void log(LogLevel logLevel, String message){
        if(testCase == null) {
            System.out.println(logLevel.toString() + ": " + message);
        } else {
            testCase.log(logLevel, message);
        }
    }

    private String getReportAsHtml(){
        StringBuilder htmlResults = new StringBuilder();
        htmlResults.append("<div class=\"linktestresults\">");
        htmlResults.append("<style type=\"text/css\" scoped>");
        htmlResults.append("  linkcheckresultstable  { border: 1px solid " + UxColors.DARK_GREY + "; }");
        htmlResults.append("  tr.linkcheckheadlines { font-weight: bold; text-align: left; }");
        htmlResults.append("  tr.linkcheckpassed { color: " + UxColors.GREEN + "; }");
        htmlResults.append("  tr.linkcheckproblem { color: " + UxColors.RED + "; font-weight: bold; }");
        htmlResults.append("  tr.linkcheckfail { color: " + UxColors.RED + "; font-weight: bold; }");
        htmlResults.append("  td.linkcheckresponsetime { color: " + UxColors.MID_GREY + "; text-align: right; }");
        htmlResults.append("</style>");
        htmlResults.append("Checking for broken links on current page. Current URL: '").append(driver.getCurrentUrl()).append("'<br>");
        htmlResults.append("A total of " + linkCheckResults.size() + " links were found on page. Results listing:<br>");
        htmlResults.append("<table class=\"linkcheckresultstable\"><tr class=\"linkcheckheadlines\"><th>Link</th><th>Response<br>code</th><th>Response<br>time (ms)</th><th>Comment</th></tr>");
        for(String[] linkresults : linkCheckResults) {
            String linkUrl = linkresults[0];
            String responseCode = linkresults[1];
            String responseTime = linkresults[2];
            String comment = linkresults[3];
            String rowClass = "linkcheckresultrow";
            if (responseCode != null && (responseCode.startsWith("2") || responseCode.startsWith("3"))) {
                rowClass = "linkcheckpassed";
                if (comment == null) comment = "Ok";
            } else if (responseCode == null || responseCode == "null") {
                rowClass = "linkcheckproblem";
                if (comment == null) comment = "Problems checking link";
            } else {
                rowClass = "linkcheckfail";
                if (comment == null) comment = "Link fail";
            }
            htmlResults.append("<tr class=\"" + rowClass + "\"><td class=\"linkcheckurl\">" + linkUrl + "</td><td class=\"linkcheckresponsecode\">" + responseCode + "</td><td class=\"linkcheckresponsetime\">" + responseTime + "</td><td class=\"linkcheckcomment\">" + comment + "</td></tr>");
        }
        htmlResults.append("</table>");
        htmlResults.append("<br>Total verification time for link checks was " + (System.currentTimeMillis() - startTime) + " milliseconds.<br><br>");
        htmlResults.append("</div>");
        return htmlResults.toString();
    }

    private String getReportAsText(){
        StringBuilder textResults = new StringBuilder();
        textResults.append(System.lineSeparator()).append("Checking for broken links on current page. Current URL: '").append(driver.getCurrentUrl()).append("'").append(System.lineSeparator());
        textResults.append("A total of " + linkCheckResults.size() + " links were found on page. Results listing:").append(System.lineSeparator());
        for(String[] linkresults : linkCheckResults){
            String linkUrl = linkresults[0];
            String responseCode = linkresults[1];
            String responseTime = linkresults[2];
            String comment = linkresults[3];
            if(responseCode != null && (responseCode.startsWith("2") || responseCode.startsWith("3"))){
                if(comment == null) comment = "Ok";
            } else if (responseCode == null || responseCode == "null"){
                if(comment == null) comment = "Problems checking link";
            } else {
                if(comment == null) comment = "Link fail";
            }
            textResults.append("URL: '" + linkUrl + "' => " + responseCode + " (" + responseTime + " ms) - " + comment + "").append(System.lineSeparator());
        }
        textResults.append("Total verification time for link checks was " + (System.currentTimeMillis() - startTime) + " milliseconds.").append(System.lineSeparator());
        return textResults.toString();
    }

}