package se.claremont.autotest.common.reporting.testrunreports;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.claremont.autotest.common.logging.KnownError;
import se.claremont.autotest.common.logging.LogFolder;
import se.claremont.autotest.common.logging.LogLevel;
import se.claremont.autotest.common.logging.LogPost;
import se.claremont.autotest.common.reporting.UxColors;
import se.claremont.autotest.common.reporting.testcasereports.TestCaseLogReporterHtmlLogFile;
import se.claremont.autotest.common.support.StringManagement;
import se.claremont.autotest.common.support.SupportMethods;
import se.claremont.autotest.common.testcase.TestCase;
import se.claremont.autotest.common.testrun.Settings;
import se.claremont.autotest.common.testrun.TestRun;
import se.claremont.autotest.common.testset.TestSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A summary of the test results for a number of test cases, like a test set or a test run.
 * Formatted in HTML for emailing, links and so forth.
 *
 * Created by jordam on 2016-08-25.
 */
public class SummaryReport {

    private final static Logger logger = LoggerFactory.getLogger( SummaryReport.class );

    int successfulTestCases = 0;
    int failedTestCasesWithNewDeviations = 0;
    int testCasesWithOnlyKnownErrors = 0;
    int testCasesWithBothNewAndKnownErrors = 0;
    int unevaluatedCount = 0;
    final ArrayList<KnownError> encounteredKnownErrorInfos = new ArrayList<>();
    final ArrayList<NewErrorInfo> newErrorInfos = new ArrayList<>();
    final ArrayList<KnownError> solvedKnownErrorsList = new ArrayList<>();
    private String testCaseSummary = "";
    private final int barWidthInPixels = 400;
    private String resultsBarStyleInfo = "";
    private String resultBarHtml;

    /**
     * Evaluates a {@link TestCase} against previous test case results in this test run. Eventually this evaluation information might be written to a {@link SummaryReport}.
     * @param testCase The {@link TestCase} to evaluate and add to accumulated list of results for later {@link SummaryReport} compilation.
     */
    void evaluateTestCase(TestCase testCase){
        appendTestCaseResultToSummary(testCase);
        evaluateTestCaseLocalKnownErrorsList(testCase);
        evaluateTestCaseUnknownErrors(testCase);
    }

    private void appendTestCaseResultToSummary(TestCase testCase){
        testCaseSummary += "            <tr class=\"" + testCase.resultStatus.toString() + "\"><td>" + testCase.testSetName + "</td><td>" + testCase.testName + "</td><td>" + StringManagement.enumCapitalNameToFriendlyString(testCase.resultStatus.toString()) + "</td><td><a href=\"file://" + testCase.pathToHtmlLog.replace("\\", "/") + "\" target=\"_blank\">TestCaseLog</a></td></tr>" + LF;
        //testCaseSummary += solvedKnownErrorsFromTestCaseLocalKnownErrorsList(testCase);
        switch (testCase.resultStatus){
            case PASSED:
                successfulTestCases++;
                break;
            case FAILED_WITH_ONLY_NEW_ERRORS:
                failedTestCasesWithNewDeviations++;
                break;
            case FAILED_WITH_ONLY_KNOWN_ERRORS:
                testCasesWithOnlyKnownErrors++;
                break;
            case FAILED_WITH_BOTH_NEW_AND_KNOWN_ERRORS:
                testCasesWithBothNewAndKnownErrors++;
                break;
            default:
                unevaluatedCount++;
                break;
        }
    }

    private void evaluateTestCaseUnknownErrors(TestCase testCase){
        ArrayList<LogPost> errorLogMessages = testCase.testCaseLog.onlyErroneousLogPosts().stream().filter(logPost -> !logPost.identifiedToBePartOfKnownError).collect(Collectors.toCollection(ArrayList::new));
        if(errorLogMessages.size() > 0){
            newErrorInfos.add(new NewErrorInfo(errorLogMessages, testCase));
        }
    }

    private void evaluateTestCaseLocalKnownErrorsList(TestCase testCase){
        for(KnownError knownError : testCase.testCaseKnownErrorsList.knownErrors){
            if(knownError.encountered()){
                encounteredKnownErrorInfos.add(knownError);
            } else {
                solvedKnownErrorsList.add(knownError);
            }
        }
    }

    public void evaluateTestSet(TestSet testSet){
        for(KnownError knownError : testSet.knownErrorsList.knownErrors){
            if(knownError.encountered()){
                encounteredKnownErrorInfos.add(knownError);
            } else {
                solvedKnownErrorsList.add(knownError);
            }
        }
    }

    /**
     * Creates a HTML string for inlining {@link KnownError} that has not been encountered for the test case into the test case results summary list
     *
     * @param testCase The {@link TestCase} to assess the TestCaseKnownErrorsList from.
     * @return A HTML table row formatted text string
     */
    private String solvedKnownErrorsFromTestCaseLocalKnownErrorsList(TestCase testCase) {
        String solvedKnownErrorsString = "";
        ArrayList<String> solvedKnownErrors = testCase.testCaseKnownErrorsList.knownErrors.stream().filter(knownError -> !knownError.encountered()).map(knownError -> knownError.description).collect(Collectors.toCollection(ArrayList::new));
        if (solvedKnownErrors.size() > 0) {
            boolean plural = (solvedKnownErrors.size() > 1);
            String s = String.valueOf(plural).toLowerCase().replace("true", "s").replace("false", "");
            solvedKnownErrorsString += "<tr class=\"" + HtmlStyleNames.SOLVED_KNOWN_ERRORS.toString() + "\"><td colspan=\"2\">Known error" + s + " for test case <i>" + testCase.testName + "</i> not encountered</td><td colspan=\"2\">";
            for (String solvedKnownErrorDescription : solvedKnownErrors) {
                solvedKnownErrorsString += solvedKnownErrorDescription + "<br>" + LF;
            }
            solvedKnownErrorsString += "</td></tr>";
        }
        return solvedKnownErrorsString;
    }

    /**
     * Line-feed for local OS
     */
    private static final String LF = SupportMethods.LF;

    /**
     * An enum that exist to cope with style changes by avoiding unlinked references.
     */
    @SuppressWarnings("unused")
    enum HtmlStyleNames {
        STATISTICS,
        CONTENT,
        RESULTS_BAR,
        STATISTICS_HEADER_ROW,
        STATISTICS_COUNT,
        HOVERABLE,
        STRIPED_ROWS,
        NEW_ERRORS,
        SOLVED_KNOWN_ERRORS,
        SOLVED_TEST_SET_ERRORS,
        EXECUTED_TEST_CASES,
        KNOWN_ERRORS,
        CHECKINS,
        SETTINGS,
        COPYRIGHT
    }

    /**
     * The test case count of all test cases analyzed for this summary testCaseLog information
     * @return Total count
     */
    private int numberOfTestCases(){
        return successfulTestCases + failedTestCasesWithNewDeviations + testCasesWithOnlyKnownErrors + testCasesWithBothNewAndKnownErrors + unevaluatedCount;
    }

    /**
     * Check to see if the reportTestRun should be written at all
     * @return Return true if the number of test cases exceeds one
     */
    private boolean reportShouldBeWritten(){
        return (numberOfTestCases() > 1);
    }

    /**
     * Produces the style section for the HTML HEAD section, to visually format the reportTestRun
     * @return HTML document style section as string
     */
    private String htmlElementStyles(){
        return LF +
                "    <style>" + LF +
                "      #" + HtmlStyleNames.SOLVED_KNOWN_ERRORS.toString() + "               { color: " + UxColors.DARK_GREY.getHtmlColorCode() + "; }" + LF +
                "      body               { color: " + UxColors.DARK_GREY.getHtmlColorCode() + "; background-color: " + UxColors.DARK_GREY.getHtmlColorCode() + "; }" + LF +
                "      table#" + HtmlStyleNames.CONTENT.toString() + "      { background-color: " + UxColors.WHITE.getHtmlColorCode() + "; padding: 30px; }" + LF +
                "      tr." + HtmlStyleNames.HOVERABLE.toString() + ":hover           { background-color: " + UxColors.LIGHT_GREY.getHtmlColorCode() + "; }" + LF +
                "      tr." + HtmlStyleNames.SOLVED_KNOWN_ERRORS.toString() + "       { font-weight: bold; color: " + UxColors.BLACK.getHtmlColorCode() + "; }" + LF +
                "      li." + HtmlStyleNames.HOVERABLE.toString() + ":hover           { background-color: " + UxColors.LIGHT_GREY.getHtmlColorCode() + "; }" + LF +
                "      table#" + HtmlStyleNames.STATISTICS.toString() + "                           { background-color: " + UxColors.WHITE.getHtmlColorCode() + "; border-collapse: collapse; border: 1p solid " + UxColors.DARK_GREY.getHtmlColorCode() + "; width: " + barWidthInPixels + "px; }" + LF +
                "      ." + TestCase.ResultStatus.FAILED_WITH_ONLY_KNOWN_ERRORS.toString() + "             { color: " + UxColors.DARK_YELLOW.getHtmlColorCode() + "; }" + LF +
                "      ." + TestCase.ResultStatus.PASSED.toString() + "                                    { color: " + UxColors.GREEN.getHtmlColorCode() + "; }" + LF +
                "      ." + TestCase.ResultStatus.FAILED_WITH_BOTH_NEW_AND_KNOWN_ERRORS.toString() + "     { color: " + UxColors.ORANGE.getHtmlColorCode() + "; font-weight: bold; }" + LF +
                "      ." + TestCase.ResultStatus.FAILED_WITH_ONLY_NEW_ERRORS.toString() + "               { color: " + UxColors.RED.getHtmlColorCode() + "; font-weight: bold; }" + LF +
                "      ." + TestCase.ResultStatus.UNEVALUATED.toString() + "                               { color: " + UxColors.DARK_GREY.getHtmlColorCode() + "; text-align: center; }" + LF +
                "      ." + HtmlStyleNames.COPYRIGHT.toString() + "                                 { background-color: " + UxColors.WHITE.getHtmlColorCode() + "; color: " + UxColors.DARK_BLUE.getHtmlColorCode() + "; text-align: center; }" + LF +
                "       tr#" + HtmlStyleNames.STATISTICS_COUNT.toString() + "          { background-color: " + UxColors.LIGHT_GREY.getHtmlColorCode() + "; }" + LF +
                "       tr#" + HtmlStyleNames.STATISTICS_HEADER_ROW.toString() + "          { background-color: " + UxColors.LIGHT_GREY.getHtmlColorCode() + "; }" + LF +
                "       table." + HtmlStyleNames.STRIPED_ROWS.toString() + " tr:nth-child(even)                 { background-color: " + UxColors.LIGHT_GREY.getHtmlColorCode() + " }" + LF +
                resultsBarStyleInfo +
                "      a." + HtmlSummaryReport.HtmlStyleNames.LICENSE_LINK.toString().toLowerCase() + "      { color: " + UxColors.DARK_BLUE.getHtmlColorCode() + "; text-decoration: none; }" + LF +
                "      a." + HtmlSummaryReport.HtmlStyleNames.LICENSE_LINK.toString().toLowerCase() + ":visited      { color: " + UxColors.DARK_BLUE.getHtmlColorCode() + "; text-decoration: none; }" + LF +
                "      a." + HtmlSummaryReport.HtmlStyleNames.LICENSE_LINK.toString().toLowerCase() + ":hover      { color: " + UxColors.DARK_BLUE.getHtmlColorCode() + "; text-decoration: underline; }" + LF +
                "    </style>" + LF + LF;
    }

    /**
     * Compiles a HTML formatted summary reportTestRun from analyzed test case information.
     * @return HTML document as string
     */
    private String createReport(){
        StringBuilder html = new StringBuilder();
        if(reportShouldBeWritten()){
            resultBarHtml = resultsGraphBar(); //Must be created before CSS Style info is genereated. Used in statistics.
            html.append("<!DOCTYPE html>").append(LF);
            html.append("<html lang=\"en\">").append(LF).append(LF);
            html.append("  <HEAD>").append(LF).append(LF);
            html.append("    <title>Test summary</title>").append(LF);
            html.append("    <link rel=\"shortcut icon\" href=\"http://46.101.193.212/TAF/images/facicon.png\">").append(LF);
            html.append("    <meta charset=\"UTF-8\">").append(LF);
            html.append("    <meta name=\"description\" content=\"Summary result for test run\">").append(LF);
            html.append(     htmlElementStyles());
            html.append("  </HEAD>").append(LF).append(LF);
            html.append("  <body>").append(LF).append(LF);
            html.append("    <table id=\"").append(HtmlStyleNames.CONTENT.toString()).append("\">").append(LF).append(LF);
            html.append("      <tr>").append(LF);
            html.append("        <td>").append(LF).append(LF);
            html.append(     htmlElementTitle());
            html.append(     htmlElementStatistics());
            html.append(     htmlElementNewErrorsGrouped());
            html.append(     htmlElementEncounteredKnownErrors());
            html.append(     htmlElementExecutedTestCasesStatusList());
            html.append(     htmlElementSolvedKnownErrors());
            html.append("        </td>").append(LF);
            html.append("      </tr>").append(LF).append(LF);
            html.append("      <tr>").append(LF);
            html.append("        <td class=\"centered\">").append(LF);
            html.append(     htmlElementCopyright());
            html.append("        </td>").append(LF);
            html.append("      </tr>").append(LF).append(LF);
            html.append("    </table>").append(LF).append(LF);
            html.append("  </body>").append(LF).append(LF);
            html.append("</html>").append(LF);
        }
        return html.toString();
    }

    /**
     * Produces the title section of the HTML page the summary reportTestRun consists of.
     * @return HTML section as string
     */
    private String htmlElementTitle(){
        return "          <img alt=\"toplogo\" src=\"" + TestRun.getSettingsValue(Settings.SettingParameters.PATH_TO_LOGO) + "\">" + LF +
                "          <h1>Test reportTestRun " + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()) + "</h1>" + LF;
    }

    /**
     * Produces the statistics section of the summary reportTestRun
     * @return HTML section as string
     */
    private String htmlElementStatistics(){
        StringBuilder html = new StringBuilder();
        html.append("          <h2>Statistics</h2>").append(LF);
        html.append(resultBarHtml).append(LF);
        html.append("          <br>").append(LF).append(LF);
        html.append("          <table id=\"").append(HtmlStyleNames.STATISTICS.toString()).append("\">").append(LF);
        html.append("            <tr id=\"").append(HtmlStyleNames.STATISTICS_HEADER_ROW.toString()).append("\"><th>Result</th><th>Count</th></tr>").append(LF);
        if(successfulTestCases > 0){
            html.append("            <tr class=\"").append(HtmlStyleNames.HOVERABLE.toString()).append("\"><td>Passed test cases</td><td>").append(successfulTestCases).append("</td></tr>").append(LF);
        }
        if(failedTestCasesWithNewDeviations > 0){
            html.append("            <tr class=\"").append(HtmlStyleNames.HOVERABLE.toString()).append("\"><td>Test cases with new deviations</td><td>").append(failedTestCasesWithNewDeviations).append("</td></tr>").append(LF);
        }
        if(testCasesWithOnlyKnownErrors > 0){
            html.append("            <tr class=\"").append(HtmlStyleNames.HOVERABLE.toString()).append("\"><td>Test cases with only known errors</td><td>").append(testCasesWithOnlyKnownErrors).append("</td></tr>").append(LF);
        }
        if(testCasesWithBothNewAndKnownErrors > 0){
            html.append("            <tr class=\"").append(HtmlStyleNames.HOVERABLE.toString()).append("\"><td>Test cases with both new and known errors</td><td>").append(testCasesWithBothNewAndKnownErrors).append("</td></tr>").append(LF);
        }
        if(unevaluatedCount > 0){
            html.append("            <tr class=\"").append(HtmlStyleNames.HOVERABLE.toString()).append("\"><td>Unevaluated test cases</td><td>").append(unevaluatedCount).append("</td></tr>").append(LF);
        }
        html.append("            <tr id=\"").append(HtmlStyleNames.STATISTICS_COUNT.toString()).append("\"><td><i>Total test case count</i></td><td><i>").append(numberOfTestCases()).append("</i></td></tr>").append(LF);
        html.append("          </table>").append(LF);
        return html.toString();
    }

    /**
     * Produces the HTML section for new errors encountered during a test run, grouped by similarity).
     * @return HTML section as string
     */
    private String htmlElementNewErrorsGrouped(){
        StringBuilder html = new StringBuilder();
        if(this.failedTestCasesWithNewDeviations + this.testCasesWithBothNewAndKnownErrors > 0){
            html.append("          <div id=\"").append(TestCaseLogReporterHtmlLogFile.enumMemberNameToLower(HtmlStyleNames.NEW_ERRORS.toString())).append("\">").append(LF);
            html.append("          <h2>New deviations</h2>").append(LF);
            html.append(       newErrors());
            html.append("          </div>").append(LF).append(LF);
        }
        return html.toString();
    }

    /**
     * Produces the HTML section for known errors
     * @return HTML section as string
     */
    private String htmlElementEncounteredKnownErrors(){
        StringBuilder html = new StringBuilder();
        if(encounteredKnownErrorInfos.size() > 0){
            html.append("          <div id=\"").append(TestCaseLogReporterHtmlLogFile.enumMemberNameToLower(HtmlStyleNames.KNOWN_ERRORS.toString())).append("\">").append(LF);
            html.append("            <h2>Encountered known errors</h2>").append(LF);
            for(KnownError knownError : encounteredKnownErrorInfos){
                html.append("            <p>").append(LF);
                html.append("              ['").append(knownError.description).append("']").append(LF);
                html.append("              <ul>").append(LF);
                ArrayList<String> idsOfTestCases = new ArrayList<>();
                boolean alreadyReported = false;
                for(TestCase testCase : knownError.testCasesWhereErrorWasEncountered){
                    for(String uid : idsOfTestCases){
                        if(testCase.uid.toString().equals(uid)){
                            alreadyReported = true;
                            break;
                        }
                    }
                    if(!alreadyReported){
                        idsOfTestCases.add(testCase.uid.toString());
                        html.append("                <li class=\"").append(HtmlStyleNames.HOVERABLE.toString()).append("\">").append(testCase.testSetName).append(": ").append(testCase.testName).append(" (<a href=\"file://").append(testCase.pathToHtmlLog.replace("\\", "/")).append("\" target=\"_blank\">Log</a>)</li>").append(LF);
                    }
                }
                html.append("              </ul>").append(LF);
                html.append("            </p>").append(LF).append(LF);
            }
            html.append("            <br>").append(LF);
            html.append("          </div>").append(LF).append(LF);
        }
        return html.toString();
    }

    /**
     * Produces the HTML section for known errors not encountered
     * @return HTML section as string
     */
    private String htmlElementSolvedKnownErrors(){
        StringBuilder html = new StringBuilder();
        List<String> solvedKnownErrors = new ArrayList<>();
        if(solvedKnownErrorsList.size() > 0){
            html.append("          <div id=\"").append(HtmlStyleNames.SOLVED_TEST_SET_ERRORS.toString()).append("\">").append(LF);
            html.append("            <h2>Registered known errors not encountered</h2>").append(LF);
            for(KnownError knownError : solvedKnownErrorsList){
                if(solvedKnownErrors.contains(knownError.description)) continue;
                solvedKnownErrors.add(knownError.description);
                html.append("            <p>").append(LF);
                html.append("              ['").append(knownError.description).append("']").append(LF);
                html.append("              <ul>").append(LF);
                for(TestCase testCase : knownError.testCasesWhereErrorWasEncountered){
                    html.append("                <li class=\"").append(HtmlStyleNames.HOVERABLE.toString()).append("\">").append(testCase.testSetName).append(": ").append(testCase.testName).append(" (<a href=\"file://").append(testCase.pathToHtmlLog.replace("\\", "/")).append("\" target=\"_blank\">Log</a>)</li>").append(LF);
                }
                html.append("            </ul>").append(LF);
                html.append("          </p>").append(LF).append(LF);
            }
            html.append("          <br>").append(LF);
            html.append("        </div>").append(LF).append(LF);
        }
        return html.toString();
    }



    /**
     * Produces the HTML section for the list of executed test cases and their
     * results each. Includes links to individual test case logs.
     * @return HTML section as string
     */
    private String htmlElementExecutedTestCasesStatusList(){
        return "          <h2>Test case summary</h2>" + LF +
                "          <table class=\"" + HtmlStyleNames.STRIPED_ROWS.toString() + " " + HtmlStyleNames.HOVERABLE.toString() + "\" id=\"" + HtmlStyleNames.EXECUTED_TEST_CASES.toString() + "\">" + LF +
                "      " + testCaseSummary +
                "          </table>" + LF;
    }

    /**
     * Produces a document footer for the summary reportTestRun.
     * @return HTML section for footer
     */
    private String htmlElementCopyright(){
        //noinspection deprecation
        return "          <table>" + LF +
                "            <tr>" + LF +
                "              <td class=\"" + HtmlStyleNames.COPYRIGHT.toString() + "\"><br>&copy; Claremont " + new SimpleDateFormat("yyyy").format(new Date()) + ". TAF is licensed under the <a href=\"https://www.apache.org/licenses/LICENSE-2.0\" target=\"_blank\" class=\"" + HtmlSummaryReport.HtmlStyleNames.LICENSE_LINK.toString().toLowerCase() + "\">Apache 2.0</a> license.</td>" + LF +
                "            </tr>" + LF +
                "          </table>" + LF;
    }

    /**
     * Writes the compiled summary reportTestRun to a file in the test run catalogue.
     */
    public void writeReport(){
        if(reportShouldBeWritten()){
            SupportMethods.saveToFile(createReport(), LogFolder.testRunLogFolder + "_summary.html");
            LogPost logPost = new LogPost(LogLevel.EXECUTED, "Summary reportTestRun saved as '" + LogFolder.testRunLogFolder + "_summary.html'.");
            logger.debug( LF + logPost.toString() );
        }
    }

    /**
     * Produces a graphic bar of the test run results.
     * Green for successfully run test cases
     * Yellow for test cases with known bugs
     * Orange for test cases with both new and known errors
     * Red for test cases with pure new errors
     * Gray for test cases that hasn't been evaluated
     * @return HTML section for hte results bar
     */
    private String resultsGraphBar(){
        StringBuilder bar = new StringBuilder();
        resultsBarStyleInfo += "      table#" + HtmlStyleNames.RESULTS_BAR.toString() + "    { background-color: " + UxColors.WHITE.getHtmlColorCode() + "; width: " + barWidthInPixels + "px; }" + LF;
        bar.append("          <table id=\"").append(HtmlStyleNames.RESULTS_BAR.toString()).append("\">").append(LF);
        bar.append("            <tr>").append(LF);
        if(successfulTestCases > 0){
            bar.append("              <td class=\"resultsgraphpassed\"></td>").append(LF);
            resultsBarStyleInfo += "      td.resultsgraphpassed { background-color: " + UxColors.GREEN.getHtmlColorCode() + "; height: 15px; width: " + (this.successfulTestCases * 100) / numberOfTestCases() + "%; }" + LF;
        }
        if(testCasesWithOnlyKnownErrors > 0){
            bar.append("              <td class=\"resultsgraphwarning\"></td>").append(LF);
            resultsBarStyleInfo += "      td.resultsgraphwarning { background-color: " + UxColors.YELLOW.getHtmlColorCode() + "; height: 15px; width: " + (this.testCasesWithOnlyKnownErrors * 100) / numberOfTestCases() + "%; }" + LF;
        }
        if(unevaluatedCount > 0){
            bar.append("              <td class=\"resultsgraphunevaluated\"></td>").append(LF);
            resultsBarStyleInfo += "      td.resultsgraphunevaluated { background-color: " + UxColors.MID_GREY.getHtmlColorCode() + "; height: 15px; width: " + this.unevaluatedCount * 100 / numberOfTestCases() + "%; }" + LF;
        }
        if(testCasesWithBothNewAndKnownErrors > 0){
            bar.append("              <td class=\"resultsgraphboth\"></td>").append(LF);
            resultsBarStyleInfo += "      td.resultsgraphboth          { background-color: " + UxColors.ORANGE.getHtmlColorCode() + "; height: 15px; width: " + (this.testCasesWithBothNewAndKnownErrors * 100) / numberOfTestCases() + "%; }" + LF;
        }
        if(failedTestCasesWithNewDeviations > 0){
            bar.append("              <td class=\"resultsgraphbad\"></td>").append(LF);
            resultsBarStyleInfo += "      td.resultsgraphbad           { background-color: " + UxColors.RED.getHtmlColorCode() + "; height: 15px; width: " + (this.failedTestCasesWithNewDeviations * 100) / numberOfTestCases() + "%; }" + LF;
        }
        bar.append("            </tr>").append(LF);
        bar.append("          </table>").append(LF);
        return bar.toString();
    }


    /**
     * Produces the HTML section for new errors in the summary testCaseLog, if new errors (errors
     * that cannot be identified as known errors) are identified in the test case logs.
     * @return HTML section for new errors.
     */
    private String newErrors(){
        StringBuilder html = new StringBuilder();
        for(NewErrorInfo newErrorInfo : newErrorInfos){
            html.append("          <p>").append(LF);
            html.append("            <b>").append(newErrorInfo.testCase.testSetName).append(": ").append(newErrorInfo.testCase.testName).append("</b>(<a href=\"file://").append(newErrorInfo.testCase.pathToHtmlLog.replace("\\", "/")).append("\" target=\"_blank\">Log</a>)<br>").append(LF);
            for(LogPost logRow : newErrorInfo.logEntries){
                html.append("            ").append(logRow.logLevel.toString()).append(": ").append(logRow.message).append("<br>").append(LF);
            }
            html.append("          </p>").append(LF);
        }
        html.append("          <br>").append(LF);
        return html.toString();
    }


    private class NewErrorInfo{
        ArrayList<LogPost> logEntries = new ArrayList<>();
        final TestCase testCase;

        NewErrorInfo(ArrayList<LogPost> logPosts, TestCase testCase){
            this.logEntries= logPosts;
            this.testCase = testCase;
        }
    }

    void evaluateTestCaseLogForErrorGrouping(TestCase testCase){
        ErrorLogRowList errorLogRows = new ErrorLogRowList();
        testCase.testCaseLog.onlyErroneousLogPosts().stream().filter(logPost -> !logPost.identifiedToBePartOfKnownError).forEachOrdered(logPost -> {
            if (errorLogRows.logPostIsRegistered(logPost)) {
                errorLogRows.addTestCaseForLogRow(logPost, testCase);
            } else {
                errorLogRows.addNewLogRowEntry(logPost, testCase);
            }
        });
    }

    private class ErrorLogRowList extends ArrayList<ErrorLogRow>{

        boolean logPostIsRegistered(LogPost logPost){
            for(ErrorLogRow errorLogRow : this ){
                if(errorLogRow.logPostMessage.equals(LogPost.removeDataElements(logPost.message))){
                    return true;
                }
            }
            return false;
        }


        void addTestCaseForLogRow(LogPost logPost, TestCase testCase){
            for(ErrorLogRow errorLogRow : this){
                if(errorLogRow.logPostMessage.equals(LogPost.removeDataElements(logPost.message))){
                    errorLogRow.addTestCase(testCase);
                    break;
                }
            }
        }

        void addNewLogRowEntry(LogPost logPost, TestCase testCase){
            this.add(new ErrorLogRow(logPost, testCase));
        }
    }

    private class ErrorLogRow{
        final String logPostMessage;
        final ArrayList<TestCase> testCasesWhereSimilarLogRowsAreEncountered = new ArrayList<>();

        ErrorLogRow(LogPost logPost, TestCase testCase){
            this.logPostMessage = LogPost.removeDataElements(logPost.message);
            this.testCasesWhereSimilarLogRowsAreEncountered.add(testCase);

        }

        boolean hasSameTestCasesAs(ErrorLogRow errorLogRow){
            for(TestCase testCase : testCasesWhereSimilarLogRowsAreEncountered){
                boolean testCaseFound = false;
                for (TestCase testCase1 : errorLogRow.testCasesWhereSimilarLogRowsAreEncountered){
                    if(testCase.isSameAs(testCase1)){
                        testCaseFound = true;
                        break;
                    }
                }
                if(!testCaseFound){
                    return false;
                }
            }
            for(TestCase testCase : errorLogRow.testCasesWhereSimilarLogRowsAreEncountered){
                boolean testCaseFound = false;
                for (TestCase testCase1 : testCasesWhereSimilarLogRowsAreEncountered){
                    if(testCase.isSameAs(testCase1)){
                        testCaseFound = true;
                        break;
                    }
                }
                if(!testCaseFound){
                    return false;
                }
            }
            return true;
        }

        void addTestCase(TestCase testCase){
            this.testCasesWhereSimilarLogRowsAreEncountered.add(testCase);
        }
    }


}
