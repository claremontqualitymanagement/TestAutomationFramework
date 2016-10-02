package se.claremont.autotest.guidriverpluginstructure.websupport;

import se.claremont.autotest.common.LogLevel;
import se.claremont.autotest.common.TestCase;
import se.claremont.autotest.restsupport.JsonParser;
import se.claremont.autotest.restsupport.RestSupport;
import se.claremont.autotest.support.SupportMethods;

import java.util.ArrayList;

/**
 * Utilizes the external W3C HTML validation service for checking of web pages.
 *
 * Created by jordam on 2016-10-01.
 */
public class W3CHtmlValidatorService {

    /**
     * Check the page source for current page with the W3C Validator API for HTML consistency.
     *
     * @param testCase The test case to log verification results to.
     * @param pageSource The page HTML source code to validate
     * @param verbose If set to false only errors will be logged. If set to true also warnings and W3C information messages will be logged.
     */
    public void verifyCurrentPageSourceWithW3validator(TestCase testCase, String pageSource, boolean verbose){
        RestSupport rest = new RestSupport(testCase);
        String responseJson = rest.responseBodyFromPostRequest("https://validator.w3.org/nu/?out=json", "text/html; charset=utf-8", pageSource);
        if(responseJson == null){
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not get any response from HTML validation service.");
            return;
        }
        if(JsonParser.childObjects(responseJson, "messages").size() == 0){
            testCase.log(LogLevel.VERIFICATION_PASSED, "Checking of page content against W3C validator passed with no messages.");
            return;
        }
        LogLevel logLevel = LogLevel.INFO;

        for(String child : JsonParser.childObjects(responseJson, "messages")) {
            if(JsonParser.get(child, "type").contains("error")){
                logLevel = LogLevel.VERIFICATION_FAILED;
            }
        }

        StringBuilder textLogMessage = new StringBuilder();
        ArrayList<String> htmlLogMessage = new ArrayList<>();

        for(String child : JsonParser.childObjects(responseJson, "messages")){
            String lineNumberString = "";
            try{
                lineNumberString = " - At line number " + JsonParser.getInt(child, "lastline");
            } catch (Exception e) {
                try {
                    lineNumberString = " - At line number " + JsonParser.getInt(child, "lastLine");
                }catch (Exception ignored){}
            }
            if(verbose && JsonParser.get(child, "type").contains("info")){
                textLogMessage.append(SupportMethods.LF + "W3C Validation " + JsonParser.get(child, "subType") + ": " + JsonParser.get(child, "message"));
                htmlLogMessage.add("<p><font class=\"w3cvalidationinfo\">W3C Validation information info</font>" + lineNumberString + "<br>" + JsonParser.get(child, "subType").toString() + ":<br>" + JsonParser.get(child, "message").toString() + "<br>Extract:<pre>" + JsonParser.get(child, "extract").replace("<", "&lt;").replace(">", "&gt;") + "</pre></p>");
            } else if(JsonParser.get(child, "type").contains("error")){
                textLogMessage.append(SupportMethods.LF + "W3C Validation error: " +  JsonParser.get(child, "message").toString() + " Extract: '" + JsonParser.get(child, "extract").toString() + "'.");
                htmlLogMessage.add("<p><font class=\"w3cvalidationerror\">W3C Validation information: Error</font>" + lineNumberString + "<br>'" + JsonParser.get(child, "message").toString() + "'<br>Extract:<pre>" + JsonParser.get(child, "extract").replace("<", "&lt;").replace(">", "&gt;") + "</pre></p>");
            } else if(verbose){
                textLogMessage.append(SupportMethods.LF + "W3C Validation " + JsonParser.get(child, "type").toString() + ": " + JsonParser.get(child, "message").toString());
                htmlLogMessage.add("<p><font class=\"w3validationother\">W3C Validation information</font>" + lineNumberString + "<br>" + JsonParser.get(child, "type").toString() + ":<br>" + JsonParser.get(child, "message").toString() + "<br>Extract:<pre>" + JsonParser.get(child, "extract").replace("<", "&lt;").replace(">", "&gt;") + "</pre></p>");
            }
            testCase.log(LogLevel.DEBUG, "W3C JSON response content: '" + child.replace("<", "&lt;").replace(">", "&gt;") + "'.");
        }
        if(logLevel == LogLevel.VERIFICATION_FAILED || (logLevel == LogLevel.INFO && verbose)){
            testCase.logDifferentlyToTextLogAndHtmlLog(logLevel, textLogMessage.toString(), String.join("<hr>", htmlLogMessage));
        } else {
            testCase.log(LogLevel.VERIFICATION_PASSED, "No indications of anything in the HTML to act on after validation of HTML with W3C validation service.");
        }
    }


}
