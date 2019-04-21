package se.claremont.taf.websupport;

import se.claremont.taf.core.logging.LogLevel;
import se.claremont.taf.core.support.SupportMethods;
import se.claremont.taf.core.testcase.TestCase;
import se.claremont.taf.json.JsonParser;
import se.claremont.taf.restsupport.RestSupport;

import java.util.ArrayList;

/**
 * Utilizes the external W3C HTML validation service for checking of web pages.
 *
 * Created by jordam on 2016-10-01.
 */
public class W3CHtmlValidatorService {
    private boolean failed = false;
    private final TestCase testCase;
    private final String pageSource;
    private final boolean verbose;
    private String responseJson;

    /**
     * Prepares for running a scan of current page source against W3C validation service.
     *
     * @param testCase The test case to log verification results to.
     * @param pageSource The page HTML source code to validate
     * @param verbose If set to false only errors will be logged. If set to true also warnings and W3C information messages will be logged.
     */
    @SuppressWarnings("unused")
    public W3CHtmlValidatorService(TestCase testCase, String pageSource, boolean verbose){
        this.testCase = testCase;
        this.pageSource = pageSource;
        this.verbose = verbose;
    }


    /**
     * Check the page source for current page with the W3C Validator API for HTML consistency.
     *
     * @return Returns true if successful
     */
    public boolean verifyPageSourceWithW3validator(){
        reportProblemsIfExist();
        if(!isRunnable()) return false;
        RestSupport rest = new RestSupport(testCase);
        responseJson = rest.responseBodyFromPostRequest("https://validator.w3.org/nu/?out=json", "text/html; charset=utf-8", pageSource);
        if(responseJson == null){
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not get any response from HTML validation service.");
            failed = true;
            return false;
        }
        if(JsonParser.childObjects(responseJson, "messages").size() == 0){
            testCase.log(LogLevel.VERIFICATION_PASSED, "Checking of page content against W3C validator passed with no messages.");
        }
        reportResults();
        return !failed;
    }

    /**
     * Returns the result of the assessment.
     *
      * @return Returns true if the test failed.
     */
    public boolean failed(){
        return failed;
    }

    private void reportProblemsIfExist(){
        if(pageSource == null || pageSource.length() == 0){
            testCase.log(LogLevel.VERIFICATION_PROBLEM, "Trying to check HTML with W3C, but the source seem to be empty.");
            failed = true;
        }
    }

    private boolean isRunnable(){
        return !(pageSource == null || pageSource.length() == 0);
    }

    private LogLevel getLogLevelDependingOnErrorLevel(){
        LogLevel logLevel = LogLevel.INFO;
        for(String child : JsonParser.childObjects(responseJson, "messages")) {
            String text = JsonParser.get(child, "type");
            if(text == null)continue;
            if(text.contains("error")){
                logLevel = LogLevel.VERIFICATION_FAILED;
                break;
            }
        }
        return logLevel;
    }

    private void reportResults(){
        LogLevel logLevel = getLogLevelDependingOnErrorLevel();

        StringBuilder textLogMessage = new StringBuilder();
        ArrayList<String> htmlLogMessage = new ArrayList<>();

        for(String child : JsonParser.childObjects(responseJson, "messages")){
            String lineNumberString = getLineNumberString(child);
            String text = JsonParser.get(child, "type");
            if(text == null)continue;
            if(verbose && text.contains("info")){
                textLogMessage.append(SupportMethods.LF).append("W3C Validation ").append(JsonParser.get(child, "subType")).append(": ").append(JsonParser.get(child, "message"));
                //noinspection ConstantConditions
                htmlLogMessage.add("<p><font class=\"w3cvalidationinfo\">W3C Validation information info</font>" + lineNumberString + "<br>" + JsonParser.get(child, "subType") + ":<br>" + JsonParser.get(child, "message") + "<br>Extract:<pre>" + JsonParser.get(child, "extract").replace("<", "&lt;").replace(">", "&gt;") + "</pre></p>");
            } else //noinspection ConstantConditions
                if(JsonParser.get(child, "type").contains("error")){
                textLogMessage.append(SupportMethods.LF).append("W3C Validation error: ").append(JsonParser.get(child, "message")).append(" Extract: '").append(JsonParser.get(child, "extract")).append("'.");
                    //noinspection ConstantConditions
                    htmlLogMessage.add("<p><font class=\"w3cvalidationerror\">W3C Validation information: Error</font>" + lineNumberString + "<br>'" + JsonParser.get(child, "message") + "'<br>Extract:<pre>" + JsonParser.get(child, "extract").replace("<", "&lt;").replace(">", "&gt;") + "</pre></p>");
            } else if(verbose){
                textLogMessage.append(SupportMethods.LF).append("W3C Validation ").append(JsonParser.get(child, "type")).append(": ").append(JsonParser.get(child, "message"));
                    //noinspection ConstantConditions
                    htmlLogMessage.add("<p><font class=\"w3validationother\">W3C Validation information</font>" + lineNumberString + "<br>" + JsonParser.get(child, "type") + ":<br>" + JsonParser.get(child, "message") + "<br>Extract:<pre>" + JsonParser.get(child, "extract").replace("<", "&lt;").replace(">", "&gt;") + "</pre></p>");
            }
            testCase.log(LogLevel.DEBUG, "W3C JSON response content: '" + child.replace("<", "&lt;").replace(">", "&gt;") + "'.");
        }
        reportResultsToTestCaseLog(logLevel, textLogMessage.toString(), htmlLogMessage);
    }

    private String getLineNumberString(String child){
        String lineNumberString = "";
        try{
            lineNumberString = " - At line number " + JsonParser.getInt(child, "lastline");
        } catch (Exception e) {
            try {
                lineNumberString = " - At line number " + JsonParser.getInt(child, "lastLine");
            }catch (Exception ignored){}
        }
        return lineNumberString;
    }

    private void reportResultsToTestCaseLog(LogLevel logLevel, String textLogMessage, ArrayList<String> htmlLogMessage){
        if(logLevel == LogLevel.VERIFICATION_FAILED || (logLevel == LogLevel.INFO && verbose)){
            testCase.logDifferentlyToTextLogAndHtmlLog(logLevel, textLogMessage, String.join("<hr>", htmlLogMessage));
        } else {
            testCase.log(LogLevel.VERIFICATION_PASSED, "No indications of anything in the HTML to act on after validation of HTML with W3C validation service.");
        }
    }
}
