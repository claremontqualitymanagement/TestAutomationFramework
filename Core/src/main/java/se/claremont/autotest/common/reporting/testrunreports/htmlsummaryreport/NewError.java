package se.claremont.autotest.common.reporting.testrunreports.htmlsummaryreport;

import se.claremont.autotest.common.logging.LogLevel;
import se.claremont.autotest.common.logging.LogPost;
import se.claremont.autotest.common.testcase.TestCase;
import se.claremont.autotest.common.testrun.TestRun;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jordam on 2017-04-10.
 */
public class NewError {
    List<TestCase> testCasesWhereEncountered = new ArrayList<>();
    List<LogPost>  sampleLogPosts = new ArrayList<>();

    public NewError(TestCase testCase, LogPost logPost){
        this.testCasesWhereEncountered.add(testCase);
        this.sampleLogPosts.add(logPost);
    }

    public NewError(LogPost logPost){
        sampleLogPosts.add(logPost);
    }

    public NewError(List<TestCase> testCases, List<LogPost> sampleLogPosts){
        this.testCasesWhereEncountered = testCases;
        this.sampleLogPosts = sampleLogPosts;
    }

    public NewError(TestCase testCase, List<LogPost> logPosts){
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
        NewError compareNewError = null;
        try{
            compareNewError = (NewError) sharedError;
        }catch (Exception ignored){
            return false;
        }
        if(compareNewError.testCasesWhereEncountered.size() != this.testCasesWhereEncountered.size())return false;
        if(compareNewError.sampleLogPosts.size() != this.sampleLogPosts.size())return false;
        for(TestCase compareTestCase : compareNewError.testCasesWhereEncountered){
            boolean testCaseFound = false;
            for(TestCase testCase : this.testCasesWhereEncountered){
                if(testCase.isSameAs(compareTestCase)){
                    testCaseFound = true;
                    break;
                }
            }
            if(!testCaseFound) return false;
        }
        for(LogPost compareLogPost : compareNewError.sampleLogPosts){
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

    /*
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
    */

    private String truncateLogMessageIfNeeded(String logMessage){
        if(logMessage.length() < 100)return logMessage;
        return logMessage.substring(0, 97) + "...";
    }

    public String toHtml(){
        StringBuilder html = new StringBuilder();
        html.append("          <p>").append(System.lineSeparator());
        if(this.sampleLogPosts.size() <= 3){
            for(LogPost logPost: sampleLogPosts){
                html.append("            <span class=\"errorloglevel\">").append(logPost.logLevel.toString()).append("</span>: <span class=\"logmessage\"").append(truncateLogMessageIfNeeded(logPost.message)).append("</span><br>").append(System.lineSeparator());
            }
        } else if(sampleLogPosts.size() > 3){
            LogPost mostTroubleSomeLogPost = new LogPost(LogLevel.DEBUG, "");
            int mostTroubleSomeLogPostOrder = 0;
            for(int i = 0; i < sampleLogPosts.size(); i++){
                LogPost logPost = sampleLogPosts.get(i);
                if(logPost.logLevel.getValue() > mostTroubleSomeLogPost.logLevel.getValue()){
                    mostTroubleSomeLogPostOrder = i; //We want the first error of this log level in report
                    mostTroubleSomeLogPost = logPost;
                }
            }

            //Allways print first encountered error in log
            LogPost logRow = sampleLogPosts.get(0);
            html.append("            <span class=\"errorloglevel\">").append(logRow.logLevel.toString()).append("</span>: <span class=\"logmessage\"").append(truncateLogMessageIfNeeded(logRow.message)).append("</span><br>").append(System.lineSeparator());

            //Print the rest of the error log rows depending on where in the log the most erroneous log post was found
            if(mostTroubleSomeLogPostOrder == 0){ //First log post is the worst
                if(sampleLogPosts.size() > 2){
                    html.append("            ...(").append(sampleLogPosts.size() - 2).append(" more problem log posts)...<br>").append(System.lineSeparator());
                }
                if(sampleLogPosts.size() > 1){ //Last error should also be printed
                    logRow = sampleLogPosts.get(sampleLogPosts.size()-1);
                    html.append("            <span class=\"errorloglevel\">").append(logRow.logLevel.toString()).append("</span>: <span class=\"logmessage\"").append(truncateLogMessageIfNeeded(logRow.message)).append("</span><br>").append(System.lineSeparator());
                }
            } else if(mostTroubleSomeLogPostOrder == 1){
                logRow = sampleLogPosts.get(1);
                html.append("            <span class=\"errorloglevel\">").append(logRow.logLevel.toString()).append("</span>: <span class=\"logmessage\"").append(truncateLogMessageIfNeeded(logRow.message)).append("</span><br>").append(System.lineSeparator());
                if(sampleLogPosts.size() > 3){
                    html.append("            ...(").append(sampleLogPosts.size() - 2).append(" more problem log posts)...<br>").append(System.lineSeparator());
                } else {
                    logRow = sampleLogPosts.get(2);
                    html.append("            <span class=\"errorloglevel\">").append(logRow.logLevel.toString()).append("</span>: <span class=\"logmessage\"").append(truncateLogMessageIfNeeded(logRow.message)).append("</span><br>").append(System.lineSeparator());
                }
            } else {
                if(mostTroubleSomeLogPostOrder == 2){ //If only one log post between the first error and the most troublesome: print it.
                    logRow = sampleLogPosts.get(1);
                    html.append("            <span class=\"errorloglevel\">").append(logRow.logLevel.toString()).append("</span>: <span class=\"logmessage\"").append(truncateLogMessageIfNeeded(logRow.message)).append("</span><br>").append(System.lineSeparator());
                } else { //Suppress log posts until the most erroneous log post
                    html.append("            ...(").append(mostTroubleSomeLogPostOrder-1).append(" more problem log posts)...<br>").append(System.lineSeparator());
                }

                logRow = sampleLogPosts.get(mostTroubleSomeLogPostOrder);
                html.append("            <span class=\"errorloglevel\">").append(logRow.logLevel.toString()).append("</span>: <span class=\"logmessage\"").append(truncateLogMessageIfNeeded(logRow.message)).append("</span><br>").append(System.lineSeparator());

                if(mostTroubleSomeLogPostOrder == sampleLogPosts.size() - 2){
                    logRow = sampleLogPosts.get(sampleLogPosts.size()-1);
                    html.append("            <span class=\"errorloglevel\">").append(logRow.logLevel.toString()).append("</span>: <span class=\"logmessage\"").append(truncateLogMessageIfNeeded(logRow.message)).append("</span><br>").append(System.lineSeparator());
                } else if(mostTroubleSomeLogPostOrder != sampleLogPosts.size() -1){
                    html.append("            ...(").append(sampleLogPosts.size() - mostTroubleSomeLogPostOrder - 1).append(" more problem log posts)...<br>").append(System.lineSeparator());
                }
            }
        }
        for(TestCase testCase : testCasesWhereEncountered){
            String link = testCase.pathToHtmlLog;
            if(link.replace("\\", "/").toLowerCase().startsWith("smb://"))
                link = link.replace("\\", "/").substring(6);
            html.append("                  &#9659; <span class=\"testsetname\">").append(testCase.testSetName).append("</span>: <span class=\"testcasename\">").append(testCase.testName).append("</span> (<a href=\"").append(TestRun.reportLinkPrefix()).append("://").append(link).append("\" target=\"_blank\">Log</a>)<br>").append(System.lineSeparator());
        }
        html.append("          <br>").append(System.lineSeparator());
        html.append("       </p>").append(System.lineSeparator());
        return html.toString();

}
}
