package se.claremont.autotest.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.claremont.autotest.support.SupportMethods;
import se.claremont.tools.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * This class represents the HTML based test case execution testCaseLog, as compared to the pure text testCaseLog that might be considered for some occasions.
 *
 * Created by jordam on 2016-08-27.
 */
class TestCaseLogReporterHtmlLogFile implements TestCaseLogReporter {

    private final static Logger logger = LoggerFactory.getLogger( TestCaseLogReporterHtmlLogFile.class );

    private final TestCase testCase;
    private Date runStartTime;
    private Date runEndTime;

    /**
     * Compiles and writes the test case HTML based execution testCaseLog.
     *
     * @param testCase the test case
     */
    public TestCaseLogReporterHtmlLogFile(TestCase testCase){
        this.testCase = testCase;
        if(testCase.testCaseLog.logPosts.size() > 0){
            this.runStartTime = testCase.testCaseLog.logPosts.get(0).date;
        } else {
            this.runStartTime = new Date();
        }
    }

    /**
     * Enums for the class names of the HTML document to avoid orphan references.
     */
    @SuppressWarnings("unused")
    enum HtmlLogStyleNames{
        KNOWN_ERROR,
        KNOWN_ERRORS_NOT_ENCOUNTERED,
        TIMESTAMP,
        STRIPED,
        LOG_ROW,
        LOG_POSTS_LIST,
        KNOWN_ERRORS,
        HEAD,
        DATA,
        TEST_CASE_DATA,
        TEST_STEP_CLASS_NAME,
        TEST_STEP_NAME,
        TEST_CASE_NAME,
        TEST_CASE_DATA_PARAMETER_NAME,
        TEST_CASE_DATA_PARAMETER_VALUE
    }

    static String enumMemberNameToLower(String enumMemberName){
        StringBuilder sb = new StringBuilder();
        String[] words = enumMemberName.split("_");
        if(words.length > 0){
            sb.append(words[0].toLowerCase());
        }
        for(int i = 1; i < words.length ; i++){
            if(words[i].length() > 0){
                sb.append(words[i].substring(0, 1).toUpperCase());
                if(words[i].length() > 1){
                    sb.append(words[i].substring(1).toLowerCase());
                }
            }
        }
        return sb.toString();
    }

    //#99CCFF - Claremont blue

    /**
     * Used to append HTML style information to the HTML based testCaseLog
     * @return A HTML formatted string to incorporate in the style tag in the HTML testCaseLog
     */
    private static String styles(){
        return "      body                    { font-family: Helvetica Neue, Helvetica, Arial, sans-serif; font-size: 14px; background-color: white; }" + LF +
                "      h1, h2                  { margin-top: 20px; margin-bottom: 10px; line-height: 1.1; font-family: inherit; }" + LF +
                "      h1                      { font-size:24px; }" + LF +
                "      h2                      { font-size:20px; }" + LF +
                "      table                   { border: 1px solid grey; }" + LF +
                "      .pagetitle              { color: #99CCFF; font-size:24px; font-weight: bold; }" + LF +
                TestCaseLogSection.htmlStyleInformation() +
                LogPost.htmlStyleInformation() +
                "      td." + enumMemberNameToLower(HtmlLogStyleNames.KNOWN_ERROR.toString()) + "           { color: red; font-weight: bold; } " + LF +
                "      table#" + enumMemberNameToLower(HtmlLogStyleNames.LOG_POSTS_LIST.toString()) + "             { background-color: white; width: 80%; }" + LF +
                "      table#" + enumMemberNameToLower(HtmlLogStyleNames.LOG_POSTS_LIST.toString()) + " tr:hover     { background-color: lightgrey; }" + LF +
                "      table." + enumMemberNameToLower(HtmlLogStyleNames.STRIPED.toString()) + " tr:nth-child(even)                 { background-color: #f2f2f2 }" + LF +
                "     .logpost:nth-child(odd), .testdatapost:nth-child(odd)  { background-color: floralwhite; }" + LF +
                "     .logpost, .testdatapost                                { border-bottom: 1px solid lightgrey; }" + LF +
                "      td.logPostLogLevel       { width: 130px; }" + LF +
                "      td.logMessage           { max-width: 99%; }" + LF +
                "      img.screenshot:hover    { margin: -1px -2px -2px -1px; width: 340px; }" + SupportMethods.LF +
                "      img.screenshot          { border: 0px none; width:105px; background: #999; }" + LF +

                //W3C checker
                "      font.w3cvalidationinfo    { color: darkgrey; font-weight: bold; }" + LF +
                "      font.w3cvalidationerror   { color: red; font-weight: bold; }" + LF +
                "      font.w3cvalidationother   { color: darkgrey; font-weight: bold; }" + LF +
                "      pre              { font-family: Consolas, Menlo, Monaco, Lucida Console, Liberation Mono, DejaVu Sans Mono, Bitstream Vera Sans Mono, Courier New, monospace, serif;" + LF +
                "                             margin-bottom: 10px;" + LF +
                "                             overflow: auto;" + LF +
                "                             width: auto;" + LF +
                "                             padding: 5px;" + LF +
                "                             background-color: #eee;" + LF +
                "                             width: 70%;" + LF +
                "                             padding-bottom: 20px!ie7;"  + LF +
                "                             max - height: 600px;" + LF +
                "      }" + LF +
                "      .footer                  { border: 0px none; width: 100%; color: #99CCFF; text-align: center; align: center; }" + LF;
    }

    /**
     * Prints the output of the test case to an HTML formatted text file
     */
    public void report(){
        testCase.log(LogLevel.DEBUG, "Saving html report to '" + testCase.pathToHtmlLog + "'.");
        logger.debug( "Saving html report to '" + testCase.pathToHtmlLog + "'." );
        if(testCase.testCaseLog.logPosts.size() > 0){
            this.runEndTime = testCase.testCaseLog.logPosts.get(testCase.testCaseLog.logPosts.size()-1).date;
        } else {
            this.runEndTime = new Date();
        }
        String html = "<!DOCTYPE html>" + LF + "<html lang=\"en\">" + LF + LF +
                htmlSectionHtmlHead() +
                "  <body>" + LF + LF +
                htmlSectionBodyHeader() +
                htmlSectionEncounteredKnownErrors() +
                htmlSectionTestCaseData() +
                htmlSectionNonEncounteredKnownTestCaseErrors() +
                htmlSectionTestCaseLogEntries() +
                footer() +
                "  </body>" + LF + LF +
                "</html>" + LF;
        SupportMethods.saveToFile(html, testCase.pathToHtmlLog);
    }

    private String htmlSectionBodyHeader(){
        return "    <div id=\"" + enumMemberNameToLower(HtmlLogStyleNames.HEAD.toString()) + "\">" + LF +
                "      <img alt=\"logo\" id=\"logo\" src=\"https://avatars3.githubusercontent.com/u/22028977?v=3&s=400\">" + LF +
                "<span class=\"pagetitle\">Claremont TAF test case results log</span>" + LF +
                status() + "<br>" + LF +
                //"      <img alt=\"logo\" id=\"logo\" src=\"" + TestRun.settings.getValue(Settings.SettingParameters.PATH_TO_LOGO) + "\">" + LF +
                "      <h1>Test results for test case '" + testCase.testName + "'</h1>" + LF +
                "      <p>" + LF +
                "        Result status: " + SupportMethods.enumCapitalNameToFriendlyString(testCase.resultStatus.toString()) + "<br>" + LF +
                "        Start time: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(testCase.startTime) + "<br>" + LF +
                "        Stop time:  " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(testCase.stopTime) + LF +
                "      </p>" + LF +
                "    </div>" + LF + LF;
    }

    private String htmlSectionHtmlHead(){
        return "  <head>" + LF + LF +
                "    <title>Test testCaseLog " + testCase.testName + "</title>" + LF +
                "    <meta name=\"description\" content=\"Test case result for test run for test case " + testCase.testName + "\"/>" + LF +
                "    <meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\"/>" + LF +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"/>" + LF + LF +
                "    <link rel=\"stylesheet\" href=\"http://ajax.googleapis.com/ajax/libs/jqueryui/1.11.4/themes/smoothness/jquery-ui.css\"/>\n" + LF + LF +
                "    <style>" + LF +
                styles() +
                "    </style>" + LF + LF +
                scriptSection() +
                "  </head>" + LF + LF;
    }

    private String footer(){
        return "<table class=\"footer\"><tr class=\"footer\"><td class=\"footer\">TAF Test case report</td></tr></table>" + LF;
    }

    private String status(){
        if(testCase.testCaseLog.hasEncounteredErrors()){
            return "<p><font color=\"lightgrey\">Status: </font><b><font color=\"red\">&#x2717;</font></b></p>" + LF;
        } else {
            return "<p><font color=\"lightgrey\">Status: </font><b><font color=\"green\">&#x2713;</font></b></p>" + LF;
        }
    }

    private String htmlSectionTestCaseLogEntries(){
        return "<br>" + LF +
                "      <h2>Test case log</h2>" + LF +
                "     <label><input type=\"checkbox\" id=\"showDebugCheckbox\"\">Show verbose debugging information</label>" + LF +
                "     <div id=\"logpostlist\">" + LF + LF +
                testStepLogPostSections(testCase) + LF +
                "     </div>" + LF +
                "     <br><br>" + LF;
    }

    private String htmlSectionTestCaseData(){
        StringBuilder html = new StringBuilder();
        if(testCase.testCaseData.testCaseDataList.size() > 0){
            html.append("      <div id=\"testdata\" class=\"testcasedata expandable\" >").append(LF);
            html.append("         <h2>Test case data</h2>").append(LF);
            html.append("         <div id=\"expandable_content\">").append(LF);
            html.append("         <table class=\"").append(enumMemberNameToLower(HtmlLogStyleNames.STRIPED.toString())).append("\" id=\"").append(enumMemberNameToLower(HtmlLogStyleNames.TEST_CASE_DATA.toString())).append("\">").append(LF);
            for(ValuePair valuePair : testCase.testCaseData.testCaseDataList){
                html.append("           <tr><td class=\"").
                        append(enumMemberNameToLower(HtmlLogStyleNames.TEST_CASE_DATA_PARAMETER_NAME.toString())).
                        append("\">").append(valuePair.parameter).append("</td><td class=\"").
                        append(enumMemberNameToLower(enumMemberNameToLower(HtmlLogStyleNames.TEST_CASE_DATA_PARAMETER_VALUE.toString()))).
                        append("\">").append(valuePair.value).append("</tr>").append(LF);
            }
            html.append("         </table>").append(LF);
            html.append("         </div>").append(LF);
            html.append("      </div>").append(LF).append(LF);
        }
        return html.toString();
    }

    private String htmlSectionEncounteredKnownErrors(){
        StringBuilder html = new StringBuilder();
        boolean knownErrorsEncountered = false;
        for(KnownError knownError : testCase.testCaseKnownErrorsList.knownErrors) {
            if(knownError.encountered()) {
                knownErrorsEncountered = true;
                break;
            }
        }
        if(!knownErrorsEncountered){
            for(KnownError knownError : testCase.testSetKnownErrors.knownErrors){
                if(knownError.encountered()){
                    knownErrorsEncountered = true;
                    break;
                }
            }
        }
        if(knownErrorsEncountered){
            html.append("    <div id=\"").append(enumMemberNameToLower(HtmlLogStyleNames.KNOWN_ERRORS.toString())).append("\">").append(LF);
            html.append("      <h2>Encountered known errors</h2>").append(LF);
            html.append("      <table>").append(LF);
            for(KnownError knownError : testCase.testCaseKnownErrorsList.knownErrors){
                if(knownError.encountered()){
                    html.append("        <tr><td class=\"").append(enumMemberNameToLower(HtmlLogStyleNames.KNOWN_ERROR.toString())).append("\">").append(knownError.description).append("</td></tr>").append(LF);
                }
            }
            for(KnownError knownError : testCase.testSetKnownErrors.knownErrors){
                if(knownError.encountered()){
                    html.append("        <tr><td class=\"").append(enumMemberNameToLower(HtmlLogStyleNames.KNOWN_ERROR.toString())).append("\">").append(knownError.description).append("</td></tr>").append(LF);
                }
            }
            html.append("      </table>").append(LF);
            html.append("    </div>").append(LF).append(LF);
        }
        return html.toString();
    }


    private String htmlSectionNonEncounteredKnownTestCaseErrors(){
        StringBuilder html = new StringBuilder();
        boolean hasKnownErrorsNotEncountered = false;
        for(KnownError knownError : testCase.testCaseKnownErrorsList.knownErrors) {
            if(!knownError.encountered()) {
                hasKnownErrorsNotEncountered = true;
                break;
            }
        }
        if(hasKnownErrorsNotEncountered){
            html.append("    <div id=\"").append(enumMemberNameToLower(HtmlLogStyleNames.KNOWN_ERRORS_NOT_ENCOUNTERED.toString())).append("\">").append(LF);
            html.append("      <h2>Known test case errors that were not encountered (possibly fixed)</h2>").append(LF);
            html.append("      <table>").append(LF);
            for(KnownError knownError : testCase.testCaseKnownErrorsList.knownErrors){
                if(!knownError.encountered()){
                    html.append("        <tr><td class=\"").append(enumMemberNameToLower(HtmlLogStyleNames.KNOWN_ERROR.toString())).append("\">").append(knownError.description).append("</td></tr>").append(LF);
                }
            }
            html.append("      </table>").append(LF);
            html.append("    </div>").append(LF).append(LF);
        }
        return html.toString();

    }

    private String testStepLogPostSections(TestCase testCase){
        if(testCase.testCaseLog.logPosts.size() == 0) return null;
        StringBuilder html = new StringBuilder();
        ArrayList<TestCaseLogSection> logSections = testCase.testCaseLog.toLogSections();
        for(TestCaseLogSection testCaseLogSection : logSections){
            html.append(testCaseLogSection.toHtml());
        }
        return html.toString();
    }


    /**
     * Line feed/Form feed for relevant OS
     */
    private static final String LF = SupportMethods.LF;

    private String scriptSection(){
        return "      <script type=\"text/javascript\" src=\"http://code.jquery.com/jquery-latest.min.js\"></script>" + LF +
                "      <script type=\"text/javascript\" src=\"http://ajax.googleapis.com/ajax/libs/jqueryui/1.11.4/jquery-ui.min.js\"></script>" + LF +
                "      <script type=\"text/javascript\">" + LF + LF +
                "         function showDebug(shouldShowDebug){" + LF +
                "           $(\".logpost.debug\").toggle(shouldShowDebug);" + LF +
                "         }" + LF +
                "" + LF +
                "         $(function() {" + LF +
                "           $(\"#showDebugCheckbox\").on(\"click\", function(evt) {" + LF +
                "              var shouldShowDebug = $(this).prop(\"checked\");" + LF +
                "              showDebug(shouldShowDebug);" + LF +
                "           });" + LF +
                "           showDebug(false);" + LF +
                "           $(\".expandable\").accordion({ collapsible: true, active: false, heightStyle: \"content\" });" + LF +
                "           $(\".expandable.initially-expanded\").accordion(\"option\", \"active\", 0);" + LF +
                "         });" + LF +
                "" + LF +
                "      </script>" + LF;
    }


}
