package se.claremont.autotest.common.reporting.testrunreports.htmlsummaryreport;

import se.claremont.autotest.common.logging.LogPost;
import se.claremont.autotest.common.testcase.TestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jordam on 2017-04-10.
 */
public class PotentiallySharedError {
    List<TestCase> testCasesWhereEncountered = new ArrayList<>();
    List<LogPost>  sampleLogPosts = new ArrayList<>();

    public PotentiallySharedError(TestCase testCase, LogPost logPost){
        this.testCasesWhereEncountered.add(testCase);
        this.sampleLogPosts.add(logPost);
    }

    public PotentiallySharedError(LogPost logPost){
        sampleLogPosts.add(logPost);
    }

    public PotentiallySharedError(List<TestCase> testCases, List<LogPost> sampleLogPosts){
        this.testCasesWhereEncountered = testCases;
        this.sampleLogPosts = sampleLogPosts;
    }

    public PotentiallySharedError(TestCase testCase, List<LogPost> logPosts){
        List<TestCase> testCases = new ArrayList<>();
        testCases.add(testCase);
        this.testCasesWhereEncountered = testCases;
        this.sampleLogPosts = logPosts;
    }

    public int getNumberOfTestCases(){
        return testCasesWhereEncountered.size();
    }

    public int getNumberOfLogRows(){
        return sampleLogPosts.size();
    }

    public boolean isSimilar(Object sharedError){
        PotentiallySharedError comparePotentiallySharedError = null;
        try{
            comparePotentiallySharedError = (PotentiallySharedError) sharedError;
        }catch (Exception ignored){
            return false;
        }
        if(comparePotentiallySharedError.testCasesWhereEncountered.size() != this.testCasesWhereEncountered.size())return false;
        if(comparePotentiallySharedError.sampleLogPosts.size() != this.sampleLogPosts.size())return false;
        for(TestCase compareTestCase : comparePotentiallySharedError.testCasesWhereEncountered){
            boolean testCaseFound = false;
            for(TestCase testCase : this.testCasesWhereEncountered){
                if(testCase.isSameAs(compareTestCase)){
                    testCaseFound = true;
                    break;
                }
            }
            if(!testCaseFound) return false;
        }
        for(LogPost compareLogPost : comparePotentiallySharedError.sampleLogPosts){
            boolean logPostFound = false;
            for(LogPost logPost : this.sampleLogPosts){
                if(logPost.isSimilar(compareLogPost)){
                    logPostFound = true;
                    break;
                }
            }
            if(!logPostFound) return false;
        }
        return true;
    }

    public String toHtml(){
        StringBuilder html = new StringBuilder();
        html.append("          <p>").append(System.lineSeparator());
        for(LogPost logPost : sampleLogPosts){
            html.append("              ").append(logPost.logLevel.toString()).append(": ").append(truncateLogMessageIfNeeded(LogPost.removeDataElements(logPost.message))).append("<br>").append(System.lineSeparator());
        }
        for(TestCase testCase : testCasesWhereEncountered){
            html.append("                  &#9659; ").append(testCase.testName).append(" (<a href=\"" + testCase.pathToHtmlLog + "\">Log</a>)<br>");
        }
        html.append(System.lineSeparator()).append("          </p>").append(System.lineSeparator());
        return html.toString();
    }

    private String truncateLogMessageIfNeeded(String logMessage){
        if(logMessage.length() < 100)return logMessage;
        return logMessage.substring(0, 97) + "...";
    }

}
