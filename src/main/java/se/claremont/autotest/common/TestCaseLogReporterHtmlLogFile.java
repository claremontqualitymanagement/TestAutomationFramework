package se.claremont.autotest.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.claremont.autotest.dataformats.TableData;
import se.claremont.autotest.support.StringManagement;
import se.claremont.autotest.support.SupportMethods;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * This class represents the HTML based test case execution testCaseLog, as compared to the pure text testCaseLog that might be considered for some occasions.
 *
 * Created by jordam on 2016-08-27.
 */
public class TestCaseLogReporterHtmlLogFile implements TestCaseLogReporter {
    private final static Logger logger = LoggerFactory.getLogger( TestCaseLogReporterHtmlLogFile.class );
    private final TestCase testCase;
    private Date runEndTime;

    /**
     * Compiles and writes the test case HTML based execution testCaseLog.
     *
     * @param testCase the test case
     */
    public TestCaseLogReporterHtmlLogFile(TestCase testCase){
        this.testCase = testCase;
        Date runStartTime;
        if(testCase.testCaseLog.logPosts.size() > 0){
            runStartTime = testCase.testCaseLog.logPosts.get(0).date;
        } else {
            runStartTime = new Date();
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

    /**
     * Compiles a test case result to html content in a format that could be inserted into a web page
     *
     * @return HTML
     */
    public String asHtmlSection(){
        testCase.log(LogLevel.DEBUG, "Creating HTML log content.");
        if(testCase.testCaseLog.logPosts.size() > 0){
            this.runEndTime = testCase.testCaseLog.logPosts.get(testCase.testCaseLog.logPosts.size()-1).date;
        } else {
            this.runEndTime = new Date();
        }
        return htmlSectionBodyHeader() +
                htmlSectionEncounteredKnownErrors() +
                htmlSectionTestCaseData() +
                htmlSectionNonEncounteredKnownTestCaseErrors() +
                htmlSectionTestCaseLogEntries() +
                footer();
    }

    /**
     * Saves the output of the test case to an HTML formatted text file
     */
    public void report(){
        testCase.log(LogLevel.DEBUG, "Saving html reportTestRun to '" + testCase.pathToHtmlLog + "'.");
        logger.debug( "Saving html reportTestRun to '" + testCase.pathToHtmlLog + "'." );
        if(testCase.testCaseLog.logPosts.size() > 0){
            this.runEndTime = testCase.testCaseLog.logPosts.get(testCase.testCaseLog.logPosts.size()-1).date;
        } else {
            this.runEndTime = new Date();
        }
        String html = "<!DOCTYPE html>" + LF + "<html lang=\"en\">" + LF + LF +
                htmlSectionHtmlHead() +
                "  <body>" + LF + LF +
                asHtmlSection() +
                "  </body>" + LF + LF +
                "</html>" + LF;
        SupportMethods.saveToFile(html, testCase.pathToHtmlLog);
    }


    public String htmlSectionHtmlHead(){
        return "  <head>" + LF + LF +
                "    <title>" + testCase.testName + " execution log</title>" + LF +
                "    <link rel=\"shortcut icon\" href=\"http://46.101.193.212/TAF/images/facicon.png\">" + LF +
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

    /**
     * Produces a document footer for the summary reportTestRun.
     * @return HTML section for footer
     */
    private String footer(){
        //noinspection deprecation
        return "<br><br>" +
                "          <table class=\"footer\" width=\"100%\">" + LF +
                "            <tr>" + LF +
                "              <td class=\"bottomlogo\" width=\"100%\"><a href=\"http://www.claremont.se\"><img alt=\"Claremont logo\" class=\"bottomlogo\" src=\"http://46.101.193.212/TAF/images/claremontlogo.gif\"></a></td>" + LF +
                "            </tr><tr>" + LF +
                "              <td width=\"100%\" class=\"" + HtmlSummaryReport.HtmlStyleNames.COPYRIGHT.toString() + "\"><br>(c) Claremont " + new SimpleDateFormat("yyyy").format(new Date()) + "</td>" + LF +
                "            </tr>" + LF +
                "          </table>" + LF;
    }


    /**
     * Used to append HTML style information to the HTML based testCaseLog
     * @return A HTML formatted string to incorporate in the style tag in the HTML testCaseLog
     */
    private static String styles(){
        return "      body                    { font-family: Helvetica Neue, Helvetica, Arial, sans-serif; font-size: 14px; background-color: " + UxColors.WHITE.getHtmlColorCode() + "; width:90%; margin-left: 2%; margin-top: 1%; color: " + UxColors.DARK_GREY.getHtmlColorCode() + "; }" + LF +
                "      h1, h2                  { margin-top: 20px; margin-bottom: 10px; line-height: 1.1; font-family: inherit; }" + LF +
                "      h1                      { font-size:24px; }" + LF +
                "      img.toplogo             { max-width: 30%; max-height: 10%; }" + LF +
                "      h2                      { font-size:20px; }" + LF +
                "      .ui-accordion .ui-accordion-content  { padding:0px; }" + LF +
                "      table                   { border: 1px solid " + UxColors.MID_GREY.getHtmlColorCode() + "; }" + LF +
                "      table.footer            { border: 0px solid " + UxColors.WHITE.getHtmlColorCode() + "; }" + LF +
                "      .pagetitle              { color: " + UxColors.DARK_BLUE.getHtmlColorCode() + "; font-size:24px; font-weight: bold; }" + LF +
                TestCaseLogSection.htmlStyleInformation() +
                LogPost.htmlStyleInformation() +
                "      b.good                  { color: " + UxColors.GREEN.getHtmlColorCode() + "; }" + LF +
                "      b.bad                   { color: " + UxColors.RED.getHtmlColorCode() + "; }" + LF +
                "      ." + HtmlSummaryReport.HtmlStyleNames.COPYRIGHT.toString() + "                                 { background-color: " + UxColors.WHITE.getHtmlColorCode() + "; color: " + UxColors.DARK_BLUE.getHtmlColorCode() + "; text-align: center; }" + LF +
                "      td." + enumMemberNameToLower(HtmlLogStyleNames.KNOWN_ERROR.toString()) + "           { color: " + UxColors.RED.getHtmlColorCode() + "; font-weight: bold; } " + LF +
                "      table." + enumMemberNameToLower(HtmlLogStyleNames.STRIPED.toString()) + "  { background-color:" + UxColors.WHITE.getHtmlColorCode() + "; }" + LF +
                "      table." + enumMemberNameToLower(HtmlLogStyleNames.STRIPED.toString()) + " tr:nth-child(even)                 { background-color: " + UxColors.LIGHT_GREY.getHtmlColorCode() + "; }" + LF +
                "     .logpost:nth-child(odd), .testdatapost:nth-child(odd)  { background-color: " + UxColors.LIGHT_GREY.getHtmlColorCode() + "; }" + LF +
                "     .logpost, .testdatapost                                { border-bottom: 1px solid " + UxColors.MID_GREY.getHtmlColorCode() + "; }" + LF +
                "      td.logPostLogLevel       { width: 130px; }" + LF +
                "      td.logMessage            { max-width: 99%; }" + LF +
                "      img.screenshot:hover     { margin: -1px -2px -2px -1px; width: 340px; }" + SupportMethods.LF +
                "      img.screenshot           { border: 0px none; width:105px; background: #999; }" + LF +
                "      img.bottomlogo           { width: 20%; }" + LF +
                "      td.bottomlogo            { text-align: center; background-color: " + UxColors.WHITE.getHtmlColorCode() + "; }" + LF +

                //Table verifier
                TableData.TableVerifierLoggingHtmlStyles.styles() +

                //W3C checker
                "      font.w3cvalidationinfo   { color: " + UxColors.DARK_GREY.getHtmlColorCode() + "; font-weight: bold; }" + LF +
                "      font.w3cvalidationerror  { color: " + UxColors.RED.getHtmlColorCode() + "; font-weight: bold; }" + LF +
                "      font.w3cvalidationother  { color: " + UxColors.DARK_GREY.getHtmlColorCode() + "; font-weight: bold; }" + LF +
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
                "      .footer                  { border: 0px none; width: 100%; color: " + UxColors.DARK_BLUE.getHtmlColorCode() + "; text-align: center; align: center; }" + LF +
                htmlStyleHelpOverlay();

    }

    private static String htmlStyleHelpOverlay(){
         return  "            #help {" + LF +
             "                vertical-align: top;" + LF +
             "                float: right;" + LF +
             "                text-align: right;" + LF +
             "                color: " + UxColors.LIGHT_BLUE.getHtmlColorCode() + ";" + LF +
             "            }" + LF +
             "" + LF +
             "            #helpText {" + LF +
             "                visibility: hidden;" + LF +
             "                left: 50px;" + LF +
             "                width: auto;" + LF +
             "                background-color: " + UxColors.DARK_BLUE.getHtmlColorCode() + ";" + LF +
             "                color: " + UxColors.WHITE.getHtmlColorCode() + ";" + LF +
             "                text-align: left;" + LF +
             "                padding: 15px;" + LF +
             "                border-radius: 6px;" + LF +
             "                position: absolute;" + LF +
             "                z-index: 1;" + LF +
             "            }" + LF +
             "" + LF +
             "            /* Show the tooltip text when you mouse over the tooltip container */" + LF +
             "            #help:hover #helpText {" + LF +
             "                visibility: visible;" + LF +
             "            }" + LF;
    }

    private String htmlSectionBodyHeader(){
        return "    <div id=\"" + enumMemberNameToLower(HtmlLogStyleNames.HEAD.toString()) + "\">" + LF +
                "      <img src=\"" + TestRun.settings.getValue(Settings.SettingParameters.PATH_TO_LOGO) + "\" alt=\"logo\" class=\"toplogo\">" + LF + "<br>" + LF + "<br>" + LF +
                //"      <a href=\"https://github.com/claremontqualitymanagement/TestAutomationFramework\" target=\"_blank\"><img alt=\"logo\" id=\"logo\" src=\"https://avatars3.githubusercontent.com/u/22028977?v=3&s=400\"></a>" + LF +
                "      <br><span class=\"pagetitle\">TAF test case results log</span>" + LF +
                "         <span class=\"pagetitle\" id=\"help\">(?)<span id=\"helpText\">" + helpText() +
                "         </span>" + LF +
                "      </span>" + LF +
                status() + "<br>" + LF +
                //"      <img alt=\"logo\" id=\"logo\" src=\"" + TestRun.settings.getValue(Settings.SettingParameters.PATH_TO_LOGO) + "\">" + LF +
                "      <h1>Test results for test case '" + testCase.testName + "'</h1>" + LF +
                "      <p>" + LF +
                "        Result status: " + StringManagement.enumCapitalNameToFriendlyString(testCase.resultStatus.toString()) + "<br>" + LF +
                "        Start time: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(testCase.startTime) + "<br>" + LF +
                "        Stop time:  " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(testCase.stopTime) + LF +
                "      </p>" + LF +
                "      <p>" + LF +
                "         Number of verifications performed: " + numberOfVerificationsPerformed() + LF +
                "         <br>" + LF +
                "         Number of non-verifying execution steps performed: " + numberOfExecitionStepsPerformed() + LF +
                "      </p>" + LF +
                "      <br>" + LF +
                "    </div>" + LF + LF;
    }

    private int numberOfVerificationsPerformed() {
        int count = 0;
        for(LogPost logPost : testCase.testCaseLog.logPosts){
            if(logPost.logLevel == LogLevel.VERIFICATION_PASSED || logPost.logLevel == LogLevel.VERIFICATION_FAILED){
                count++;
            }
        }
        return count;
    }

    private int numberOfExecitionStepsPerformed() {
        int count = 0;
        for(LogPost logPost : testCase.testCaseLog.logPosts){
            if(logPost.logLevel == LogLevel.EXECUTED){
                count++;
            }
        }
        return count;
    }

    private String helpText(){
        return  LF + LF + "<b>Brief help for interpreting the log</b><br>" + LF +
                "This test results log is divided into structured sections.<br>" + LF +
                "   * First there is some statistics and overview information.<br>" + LF +
                "   * The next section is test case data used during execution. If no test data was registered during test case execution this section is suppressed.<br>" + LF +
                "   * The log entries from the test run is the main part of the test case log<br>" + LF +
                "<br>" + LF +
                "<b>Log sections and log behavior</b>" + LF +
                "The log entries are grouped in test steps that are named after the actions in the test case.<br>" + LF +
                "Log sections with errors are expanded upon opening the log file." + LF +
                "If a log section title is clicked the log section log content display is toggled.<br>" +
                "<br>" + LF +
                "<b>Progress bar</b><br>" + LF +
                "Between log sections a thin line can be seen. This is a progress bar that indicate how much time was spent in this log section." + LF +
                "<br><br>" +
                "<b>Debug log rows diplay</b><br>" + LF +
                "More detailed information is displayed if the <b>debug checkbox</b> is checked. Then even debug log entries are displayed." + LF +
                "<br><br>" + LF +
                "<b>Test step class name</b><br>" + LF +
                "When hovering over a log section title the class name for the method producing the log entries are displayed in a tooltip." + LF +
                "<br><br>" +
                "<b>Screenshots, images and references</b><br>" + LF +
                "If screenshots exist in the log these will become larger upon hovering over them, and they till open in another browser window upon click.<br>" +
                "Some other relevant information will also be displayed in another window upon click, for example saved html content." + LF +
                "<br><br>" + LF +
                "More information could be reached by clicking the logo in the top left corner of this document and checking out the GitHub Wiki.<br>" + LF + LF;
    }

    private String status(){
        if(testCase.testCaseLog.hasEncounteredErrors()){
            return "      <p class=\"statussymbol\">Status: <b class=\"bad\">&#x2717;</b></p>" + LF;
        } else {
            return "      <p class=\"statussymbol\">Status: <b class=\"good\">&#x2713;</b></p>" + LF;
        }
    }

    private String htmlSectionTestCaseLogEntries(){
        return "<br>" + LF +
                "      <h2>Test case log</h2>" + LF +
                "     <label><input type=\"checkbox\" id=\"showDebugCheckbox\">Show verbose debugging information</label>" + LF +
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
            html.append("      <table class=\"encounteredknownerrors\">").append(LF);
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
            html.append("    </div>").append(LF).append("<br><br>").append(LF);
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
