package se.claremont.taf.core.reporting.testrunreports.htmlsummaryreport;

import se.claremont.taf.core.logging.LogLevel;
import se.claremont.taf.core.logging.LogPost;
import se.claremont.taf.core.testcase.TestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jordam on 2017-04-10.
 */
class NewError {
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
        NewError compareNewError;
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
            html.append("                  &#9659; ").append(testCase.testName).append(" (<a href=\"" + testCase.pathToHtmlLogFile + "\">Log</a>)<br>");
        }
        html.append(System.lineSeparator()).append("          </p>").append(System.lineSeparator());
        return html.toString();
    }
    */

    private String truncateLogMessageIfNeeded(String logMessage){
        if(logMessage.length() < 100)return logMessage;
        return logMessage.substring(0, 97) + "...";
    }

    @SuppressWarnings("SpellCheckingInspection")
    public String toHtml(){
        StringBuilder html = new StringBuilder();
        html.append("          <p>").append(System.lineSeparator());
        html.append("             <table class=\"unsharederror\">").append(System.lineSeparator());
        if(this.sampleLogPosts.size() <= 3){
            for(LogPost logPost: sampleLogPosts){
                html.append("                <tr>").append(System.lineSeparator());
                html.append("                   <td><span class=\"errorloglevel\">").append(logPost.logLevel.toString()).append("</span></td><td><span class=\"logmessage\">").append(truncateLogMessageIfNeeded(logPost.message)).append("</span></td>").append(System.lineSeparator());
                html.append("                </tr>").append(System.lineSeparator());
            }
        } else {
            LogPost mostTroubleSomeLogPost = new LogPost(LogLevel.DEBUG, "", "", "", "", "");
            int mostTroubleSomeLogPostOrder = 0;
            for(int i = 0; i < sampleLogPosts.size(); i++){
                LogPost logPost = sampleLogPosts.get(i);
                if(logPost.logLevel.getValue() > mostTroubleSomeLogPost.logLevel.getValue()){
                    mostTroubleSomeLogPostOrder = i; //We want the first error of this log level in report
                    mostTroubleSomeLogPost = logPost;
                }
            }

            //Always print first encountered error in log
            LogPost logRow = sampleLogPosts.get(0);
            html.append("                <tr>").append(System.lineSeparator());
            html.append("                   <td><span class=\"errorloglevel\">").append(logRow.logLevel.toString()).append("</span></td><td><span class=\"logmessage\">").append(truncateLogMessageIfNeeded(logRow.message)).append("</span></td>").append(System.lineSeparator());
            html.append("                </tr>").append(System.lineSeparator());

            //Print the rest of the error log rows depending on where in the log the most erroneous log post was found
            if(mostTroubleSomeLogPostOrder == 0){ //First log post is the worst
                if(sampleLogPosts.size() > 2){
                    html.append("                <tr>").append(System.lineSeparator());
                    html.append("                   <td colspan=\"2\">...(").append(sampleLogPosts.size() - 2).append(" more problem log posts)...</td>").append(System.lineSeparator());
                    html.append("                </tr>").append(System.lineSeparator());
                }
                if(sampleLogPosts.size() > 1){ //Last error should also be printed
                    logRow = sampleLogPosts.get(sampleLogPosts.size()-1);
                    html.append("                <tr>").append(System.lineSeparator());
                    html.append("                   <td><span class=\"errorloglevel\">").append(logRow.logLevel.toString()).append("</span></td><td><span class=\"logmessage\">").append(truncateLogMessageIfNeeded(logRow.message)).append("</span></td>").append(System.lineSeparator());
                    html.append("                </tr>").append(System.lineSeparator());
                }
            } else if(mostTroubleSomeLogPostOrder == 1){
                logRow = sampleLogPosts.get(1);
                html.append("                <tr>").append(System.lineSeparator());
                html.append("                   <td><span class=\"errorloglevel\">").append(logRow.logLevel.toString()).append("</span></td><td><span class=\"logmessage\">").append(truncateLogMessageIfNeeded(logRow.message)).append("</span></td>").append(System.lineSeparator());
                html.append("                </tr>").append(System.lineSeparator());
                if(sampleLogPosts.size() > 3){
                    html.append("                <tr>").append(System.lineSeparator());
                    html.append("                   <td colspan=\"2\">...(").append(sampleLogPosts.size() - 2).append(" more problem log posts)...</td>").append(System.lineSeparator());
                    html.append("                </tr>").append(System.lineSeparator());
                } else {
                    logRow = sampleLogPosts.get(2);
                    html.append("                <tr>").append(System.lineSeparator());
                    html.append("                   <td><span class=\"errorloglevel\">").append(logRow.logLevel.toString()).append("</span></td><td><span class=\"logmessage\">").append(truncateLogMessageIfNeeded(logRow.message)).append("</span></td>").append(System.lineSeparator());
                    html.append("                </tr>").append(System.lineSeparator());
                }
            } else {
                if(mostTroubleSomeLogPostOrder == 2){ //If only one log post between the first error and the most troublesome: print it.
                    logRow = sampleLogPosts.get(1);
                    html.append("                <tr>").append(System.lineSeparator());
                    html.append("                   <td><span class=\"errorloglevel\">").append(logRow.logLevel.toString()).append("</span></td><td><span class=\"logmessage\">").append(truncateLogMessageIfNeeded(logRow.message)).append("</span></td>").append(System.lineSeparator());
                    html.append("                </tr>").append(System.lineSeparator());
                } else { //Suppress log posts until the most erroneous log post
                    html.append("                <tr>").append(System.lineSeparator());
                    html.append("                   <td colspan=\"2\">...(").append(mostTroubleSomeLogPostOrder-1).append(" more problem log posts)...</td>").append(System.lineSeparator());
                    html.append("                </tr>").append(System.lineSeparator());
                }

                logRow = sampleLogPosts.get(mostTroubleSomeLogPostOrder);
                html.append("                <tr>").append(System.lineSeparator());
                html.append("                   <td><span class=\"errorloglevel\">").append(logRow.logLevel.toString()).append("</span></td><td><span class=\"logmessage\">").append(truncateLogMessageIfNeeded(logRow.message)).append("</span></td>").append(System.lineSeparator());
                html.append("                </tr>").append(System.lineSeparator());

                if(mostTroubleSomeLogPostOrder == sampleLogPosts.size() - 2){
                    logRow = sampleLogPosts.get(sampleLogPosts.size()-1);
                    html.append("                <tr>").append(System.lineSeparator());
                    html.append("                   <td><span class=\"errorloglevel\">").append(logRow.logLevel.toString()).append("</span></td><td><span class=\"logmessage\">").append(truncateLogMessageIfNeeded(logRow.message)).append("</span></td>").append(System.lineSeparator());
                    html.append("                </tr>").append(System.lineSeparator());
                } else if(mostTroubleSomeLogPostOrder != sampleLogPosts.size() -1){
                    html.append("                <tr>").append(System.lineSeparator());
                    html.append("                   <td colspan=\"2\">...(").append(sampleLogPosts.size() - mostTroubleSomeLogPostOrder - 1).append(" more problem log posts)...</td>").append(System.lineSeparator());
                    html.append("                </tr>").append(System.lineSeparator());
                }
            }
        }
        html.append("             </table>").append(System.lineSeparator());
        for(TestCase testCase : testCasesWhereEncountered){
            String link = testCase.pathToHtmlLogFile.replace("\\", "/");
            String[] parts = link.split("/");
            link = parts[parts.length -1];
            if (testCase.urlToCloudResultStorage!=null) {
                link = testCase.urlToCloudResultStorage + link;
            }
            html.append("                  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&#9659; <span class=\"testsetname\">").append(testCase.testSetName).append("</span>: <span class=\"testcasename\">").append(testCase.testName).append("</span> (<a href=\"").append(link).append("\" target=\"_blank\">Log</a>)<br>").append(System.lineSeparator());
        }
        html.append("          <br>").append(System.lineSeparator());
        html.append("       </p>").append(System.lineSeparator());
        return html.toString();

    }
}
