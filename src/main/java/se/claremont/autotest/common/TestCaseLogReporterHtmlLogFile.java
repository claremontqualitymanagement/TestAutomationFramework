package se.claremont.autotest.common;

import se.claremont.autotest.support.SupportMethods;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * This class represents the HTML based test case execution testCaseLog, as compared to the pure text testCaseLog that might be considered for some occasions.
 *
 * Created by jordam on 2016-08-27.
 */
class TestCaseLogReporterHtmlLogFile implements TestCaseLogReporter {
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
            this.runEndTime = testCase.testCaseLog.logPosts.get(testCase.testCaseLog.logPosts.size()-1).date;
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
                sb.append(words[i].substring(0, 0).toUpperCase());
                sb.append(words[i].substring(1).toLowerCase());
            }
        }
        return sb.toString();
    }

    /**
     * Used to append HTML style information to the HTML based testCaseLog
     * @return A HTML formatted string to incorporate in the style tag in the HTML testCaseLog
     */
    private static String styles(){
        return "      body                    { background-color: honeydew; }" + LF +
                "      table                   { border: 1px solid black; margin-left: 10px;}" + LF +
                "      table.timeGraph         { border: 0px solid white; margin-left: 0px; }" + LF +
                "      td.before               { background-color: grey; }" + LF +
                "      td.during               { background-color: blue; }" + LF +
                "      td.after                { background-color: grey; }" + LF +
                "      img.screenShot          { border: 0 none; width: 105px; background: #999; }" + LF +
                "      img.screenShot:hover    { margin: -1px -2px -2px -1px; width: 340px; }" + LF +
                "      .testStepClassName      { color: grey; }" + LF +
                "      td." + enumMemberNameToLower(HtmlLogStyleNames.KNOWN_ERROR.toString()) + "           { color: red; font-weight: bold; } " + LF +
                "      table#" + enumMemberNameToLower(HtmlLogStyleNames.LOG_POSTS_LIST.toString()) + "             { background-color: white; width: 80%; }" + LF +
                "      table#" + enumMemberNameToLower(HtmlLogStyleNames.LOG_POSTS_LIST.toString()) + " tr:hover     { background-color: lightgrey; }" + LF +
                "      table." + enumMemberNameToLower(HtmlLogStyleNames.STRIPED.toString()) + " tr:nth-child(even)                 { background-color: #f2f2f2 }" + LF +
                "      td.logPostLogLevel       { width: 130px; }" + LF +
                "      td." + enumMemberNameToLower(HtmlLogStyleNames.TIMESTAMP.toString()) + "            { color: grey; width: 80px; }" + LF +
                "      td." + enumMemberNameToLower(LogLevel.VERIFICATION_PASSED.toString()) + "  { color: green; }" + LF +
                "      td." + enumMemberNameToLower(LogLevel.EXECUTED.toString()) + "             { color: black; }" + LF +
                "      td." + enumMemberNameToLower(LogLevel.VERIFICATION_FAILED.toString()) + "  { color: red; font-weight: bold; }" + LF +
                "      td." + enumMemberNameToLower(LogLevel.EXECUTION_PROBLEM.toString()) + "    { color: red; font-weight: bold; }" + LF +
                "      td." + enumMemberNameToLower(LogLevel.VERIFICATION_PROBLEM.toString()) + " { color: red; font-weight: bold; }" + LF +
                "      td." + enumMemberNameToLower(LogLevel.FRAMEWORK_ERROR.toString()) + "      { color: red; font-weight: bold; }" + LF +
                "      td." + enumMemberNameToLower(LogLevel.INFO.toString()) + "                 { color: blue; }" + LF +
                "      td." + enumMemberNameToLower(LogLevel.DEBUG.toString()) + "                { color: Grey; }" + LF +
                "      td." + enumMemberNameToLower(LogLevel.DEVIATION_EXTRA_INFO.toString()) + " { color: blue; }" + LF +
                "      td.logMessage           { max-width: 99%; }" + LF +
                "      font." + enumMemberNameToLower(HtmlLogStyleNames.DATA.toString()) + "               { color: blue; }" + LF +
                "      tr.deviationSection       { font-size: 120%; font-weight: bold; color: red; }" + LF +
                "      tr .noDeviationSection     { font-size: 110%; font-weight: bold; color: green; }" + LF +
                "      tr.testDataTitleRow       { background-color: lightgrey; }" + LF +
                "      pre              { font-family: Consolas, Menlo, Monaco, Lucida Console, Liberation Mono, DejaVu Sans Mono, Bitstream Vera Sans Mono, Courier New, monospace, serif;" + LF +
                "                             margin-bottom: 10px;" + LF +
                "                             overflow: auto;" + LF +
                "                             width: auto;" + LF +
                "                             padding: 5px;" + LF +
                "                             background-color: #eee;" + LF +
                "                             width: 650px!ie7;" + LF +
                "                             padding-bottom: 20px!ie7;"  + LF +
                "                             max - height: 600px;" + LF +
                "      }" + LF;
    }

    /**
     * Prints the output of the test case to an HTML formatted text file
     */
    public void report(){
        testCase.log(LogLevel.DEBUG, "Saving html report to '" + testCase.pathToHtmlLog + "'.");
        System.out.println("Saving html report to '" + testCase.pathToHtmlLog + "'.");
        StringBuilder html = new StringBuilder();
        html.append("<html>").append(LF).append(LF);
        html.append(htmlSectionHtmlHead());
        html.append("  <body onload=\"onLoad()\">").append(LF).append(LF);
        html.append(htmlSectionBodyHeader());
        html.append(htmlSectionEncounteredKnownErrors());
        html.append(htmlSectionTestCaseData());
        html.append(htmlSectionNonEncounteredKnownTestCaseErrors());
        html.append(htmlSectionTestCaseLogEntries());
        html.append("  </body>").append(LF).append(LF);
        html.append("</html>").append(LF);
        SupportMethods.saveToFile(html.toString(), testCase.pathToHtmlLog);
    }

    private String htmlSectionBodyHeader(){
        StringBuilder html = new StringBuilder();
        html.append("    <div id=\"").append(enumMemberNameToLower(HtmlLogStyleNames.HEAD.toString())).append("\">").append(LF);
        html.append("      <img id=\"logo\" src=\"").append(TestRun.settings.getValueForProperty("pathToLogo")).append("\">").append(LF);
        html.append("      <h1>Test results for test case '").append(testCase.testName).append("'</h1>").append(LF);
        html.append("      <p>").append(LF);
        html.append("        Status: ").append(SupportMethods.enumCapitalNameToFriendlyString(testCase.resultStatus.toString())).append("<br>").append(LF);
        html.append("        Start time: ").append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(testCase.startTime)).append("<br>").append(LF);
        html.append("        Stop time:  ").append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(testCase.stopTime)).append(LF);
        html.append("      </p>").append(LF);
        html.append("    </div>").append(LF).append(LF);
        return html.toString();
    }

    private String htmlSectionHtmlHead(){
        StringBuilder html = new StringBuilder();
        html.append("  <head>").append(LF).append(LF);
        html.append("    <title>Test testCaseLog ").append(testCase.testName).append("</title>").append(LF);
        html.append("    <meta charset=\"UTF-8\">").append(LF);
        html.append("    <meta name=\"description\" content=\"Test case result for test run for test case ").append(testCase.testName).append("\">").append(LF);
        html.append("    <style>").append(LF);
        html.append(       styles() );
        html.append("    </style>").append(LF).append(LF);
        html.append(     scriptSection());
        html.append("  </head>").append(LF).append(LF);
        return html.toString();
    }

    private String htmlSectionTestCaseLogEntries(){
        StringBuilder html = new StringBuilder();
        html.append("      <h2>Test case testCaseLog entries</h2>").append(LF);
        html.append("      <label><input type=\"checkbox\" id=\"showDebugCheckbox\" onchange=\"showDebug(this)\">Suppress debug rows</label>").append(LF);
        html.append("      <table class=\"").append(enumMemberNameToLower(HtmlLogStyleNames.STRIPED.toString())).append("\" id=\"").append(enumMemberNameToLower(HtmlLogStyleNames.LOG_POSTS_LIST.toString())).append("\">").append(LF);
        html.append( testStepLogPostSections(testCase));
        html.append("      </table>").append(LF);
        return html.toString();
    }

    private String htmlSectionTestCaseData(){
        StringBuilder html = new StringBuilder();
        if(testCase.testCaseData.size() > 0){
            html.append("      <h2>Test case data</h2>").append(LF);
            html.append("      <table class=\"").append(enumMemberNameToLower(HtmlLogStyleNames.STRIPED.toString())).append("\" id=\"").append(enumMemberNameToLower(HtmlLogStyleNames.TEST_CASE_DATA.toString())).append("\">").append(LF);
            html.append("        <tr class=\"testDataTitleRow\" onclick=\"toggleVisibilityByClass('additionalDataSection')\"><td class=\"collapseIcon\">+</td><td>Test DATA saved during execution</td></tr>").append(LF);
            html.append("        <tbody class=\"additionalDataSection\">").append(LF);
            for(ValuePair valuePair : testCase.testCaseData){
                html.append("        <tr><td class=\"").
                        append(enumMemberNameToLower(HtmlLogStyleNames.TEST_CASE_DATA_PARAMETER_NAME.toString())).
                        append("\">").append(valuePair.parameter).append("</td><td class=\"").
                        append(enumMemberNameToLower(enumMemberNameToLower(HtmlLogStyleNames.TEST_CASE_DATA_PARAMETER_VALUE.toString()))).
                        append("\">").append(valuePair.value).append("</tr>").append(LF);
            }
            html.append("        </tbody>").append(LF);
            html.append("      </table>").append(LF);
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
        StringBuilder html = new StringBuilder();
//        html.append("        <tr><td colspan=\"3\">Log posts</td></tr>").append(LF);
 //       html.append("        <tr><th>Time</th><th>Log level</th><th>Message</th></tr>").append(LF);
        String lastTestStepName = "";
        ArrayList<ArrayList<LogPost>> logSectionList = new ArrayList<>();
        ArrayList<LogPost> logPostsInTestStep = new ArrayList<>();
        for(LogPost logPost : testCase.testCaseLog.logPosts){
            if(!logPost.testStepName.equals(lastTestStepName)){
                logSectionList.add(logPostsInTestStep);
                logPostsInTestStep = new ArrayList<>();
                lastTestStepName = logPost.testStepName;
            }
            logPostsInTestStep.add(logPost);
        }
        logSectionList.add(logPostsInTestStep);
        int sectionCounter = 0;
        for(ArrayList<LogPost> logPosts : logSectionList){
            if(logPosts.size() > 0){
                Date firstLogPostTime = logPosts.get(0).date;
                Date lastLogPostTime = logPosts.get(logPosts.size()-1).date;
                if(logPosts.size() == 0) continue;
                sectionCounter++;
                String sectionClass = sectionCounter + logPosts.get(0).testStepName;
                boolean hasErrors = false;
                for(LogPost logPost : logPosts){
                    if(logPost.isFail()){
                        hasErrors = true;
                        break;
                    }
                }
                if(hasErrors){
                    html.append("        <tbody class=\"sectionHeadline\">").append(LF);
                    html.append("          <tr class=\"deviationSection\" onclick=\"toggleVisibilityByClass('").append(sectionClass).append("')\" style=\"display: block;\">").append(LF);
                    html.append("            <td class=\"stepCounter\" style=\"display: inline;\">").append(LF);
                    html.append("              Step ").append(sectionCounter);
                    html.append("            </td>").append(LF);
                    html.append("            <td class=\"sectionName\" style=\"display: inline;\">").append(LF);
                    html.append(              logPosts.get(0).testStepName).append("<span class=\"testStepClassName\"> (in class '").append(logPosts.get(0).testStepClassName).append("')</span>").append(LF);
                    html.append("            </td>").append(LF);
                    html.append("            <td class=\"progressGraph\" style=\"display: inline;\">").append(LF);
                    html.append("              ").append(timeProgressGraph(runStartTime, firstLogPostTime, runEndTime, lastLogPostTime, 150));
                    html.append("            </td>").append(LF);
                    html.append("          </tr>").append(LF);
                    html.append("        </tbody>").append(LF);
                    html.append("        <tbody class=\"deviationSectionTable ").append(sectionClass).append("\">").append(LF);
                } else {
                    html.append("          <tbody class=\"sectionHeadline\">").append(LF);
                    html.append("            <tr class=\"noDeviationSection\" onclick=\"toggleVisibilityByClass('").append(sectionClass).append("')\" style=\"display: block;\">").append(LF);
                    html.append("            <td class=\"stepCounter\" style=\"display: inline;\">").append(LF);
                    html.append("              Step ").append(sectionCounter);
                    html.append("            </td>").append(LF);
                    html.append("            <td class=\"sectionName\" style=\"display: inline;\">").append(LF);
                    html.append(              logPosts.get(0).testStepName).append("<span class=\"testStepClassName\"> (in class '").append(logPosts.get(0).testStepClassName).append("')</span>").append(LF);
                    html.append("            </td>").append(LF);
                    html.append("            <td class=\"progressGraph\" style=\"display: inline;\">").append(LF);
                    html.append("              ").append(timeProgressGraph(runStartTime, firstLogPostTime, runEndTime, lastLogPostTime, 150));
                    html.append("            </td>").append(LF);
                    html.append("          </tr>").append(LF);
                    html.append("        </tbody>").append(LF);
                    html.append("        <tbody class=\"noDeviationSectionTable ").append(sectionClass).append("\">").append(LF);
                }
                for(LogPost logPost : logPosts){
                    html.append(logPost.toHtmlTableRow(null));
                }
                html.append("        </tbody>").append(LF).append("      </td>").append(LF).append("    </tr>").append(LF);
            }
        }
        return html.toString();
    }

    @SuppressWarnings("SameParameterValue")
    private String timeProgressGraph(Date sectionStartTime, Date partialEventInSectionStartTime, Date sectionEndTime, Date partialEventInSectionEndTime, int graphWidth){
        StringBuilder sb = new StringBuilder();
        sb.append("<span title=\"Test step start time: ").append(new SimpleDateFormat("HH:mm:ss").format(partialEventInSectionStartTime))
                .append(LF).append("Test step end time: ").append(new SimpleDateFormat("HH:mm:ss").format(partialEventInSectionEndTime)).append("\">");
        sb.append("<table class=\"timeGraph\" width=\"").append(graphWidth).append("\"><tr>");
        if(sectionEndTime.getTime()-sectionStartTime.getTime() != 0){
            long widthOfInitPartPercent = (graphWidth*(partialEventInSectionStartTime.getTime() - sectionStartTime.getTime()))/(sectionEndTime.getTime() - sectionStartTime.getTime());
            sb.append("<td width=\"").append(widthOfInitPartPercent).append("px\" class=\"before\"></td>");
            long widthOfPartPercent = (graphWidth*(partialEventInSectionEndTime.getTime() - partialEventInSectionStartTime.getTime()))/(sectionEndTime.getTime() - sectionStartTime.getTime());
            sb.append("<td width=\"").append(widthOfPartPercent).append("px\" class=\"during\"></td>");
            long widthOfEndPartPercent = (graphWidth*(sectionEndTime.getTime() - partialEventInSectionEndTime.getTime()))/(sectionEndTime.getTime() - sectionStartTime.getTime());
            sb.append("<td width=\"").append(widthOfEndPartPercent).append("px\" class=\"after\"></td>");
        }
        sb.append("</tr></table>");
        sb.append("</span>");
        return sb.toString();
    }

    /**
     * Line feed/Form feed for relevant OS
     */
    private static final String LF = SupportMethods.LF;

    private String scriptSection(){
        return "<script>" + LF +
                LF +
                "    function onLoad()" + LF +
                "    {" + LF +
                "          hideElementsByClass('noDeviationSectionTable');" + LF +
                "          unHideElementsByClass('DeviationSectionTable');" + LF +
                "          hideElementsByClass('additionalDataSection');" + LF +
                "          document.getElementById(\"showDebugCheckbox\").checked = true;" + LF +
                "          showDebug();" + LF +
                "      }" + LF +
                LF +
                "    function toggleVisibilityByClass(className)" + LF +
                "         {" + LF +
                "          var classElements = document.getElementsByClassName(className);" + LF +
                "          for (i = 0; i < classElements.length; i++)" + LF +
                "          {" + LF +
                "              if(classElements[i].style.display == 'block')" + LF +
                "              {" + LF +
                "                  classElements[i].style.display = 'none';" + LF +
                "              }" + LF +
                "              else if(classElements[i].style.display == 'none')" + LF +
                "              {" + LF +
                "                  classElements[i].style.display = 'block';" + LF +
                "              }" + LF +
                "              else" + LF +
                "              {" + LF +
                "                  classElements[i].style.display = 'block';" + LF +
                "              }" + LF +
                "          }" + LF +
                "         }" + LF +
                LF +
                "         function unHideElementsByClass(className)" + LF +
                "         {" + LF +
                "          var classElements = document.getElementsByClassName(className);" + LF +
                "          for (i = 0; i < classElements.length; i++)" + LF +
                "          {" + LF +
                "              classElements[i].style.display = 'block';" + LF +
                "          }" + LF +
                "         }" + LF +
                LF +
                "         function hideElementsByClass(className)" + LF +
                "         {" + LF +
                "          var classElements = document.getElementsByClassName(className);" + LF +
                "          for (i = 0; i < classElements.length; i++)" + LF +
                "          {" + LF +
                "              classElements[i].style.display = 'none';" + LF +
                "          }" + LF +
                "         }" + LF +
                LF +
                "         function showDebug()" + LF +
                "         {" + LF +
                "          if(document.getElementById(\"showDebugCheckbox\").checked)" + LF +
                "          {" + LF +
                "              hideElementsByClass('debug');" + LF +
                "          }" + LF +
                "          else" + LF +
                "          {" + LF +
                "              unHideElementsByClass('debug');" + LF +
                "          }" + LF +
                "         }" + LF +
                LF +
                "      </script>" + LF;
    }

}
