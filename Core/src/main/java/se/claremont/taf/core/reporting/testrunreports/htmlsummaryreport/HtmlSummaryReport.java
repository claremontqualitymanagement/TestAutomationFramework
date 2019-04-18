package se.claremont.taf.core.reporting.testrunreports.htmlsummaryreport;

import se.claremont.taf.core.logging.KnownError;
import se.claremont.taf.core.logging.KnownErrorsList;
import se.claremont.taf.core.logging.LogPost;
import se.claremont.taf.core.reporting.UxColors;
import se.claremont.taf.core.reporting.testcasereports.TestCaseLogReporterHtmlLogFile;
import se.claremont.taf.core.support.StringManagement;
import se.claremont.taf.core.support.SupportMethods;
import se.claremont.taf.core.testcase.TestCase;
import se.claremont.taf.core.testcase.TestCaseResult;
import se.claremont.taf.core.testrun.Settings;
import se.claremont.taf.core.testrun.TestRun;
import se.claremont.taf.core.testset.TestSet;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A summary of the test results for a number of test cases, like a test set or a test run.
 * Formatted in HTML for emailing, links and so forth.
 *
 * If TestRun.settings has a customValue called 'HtmlReportsLinkPrefix' with the value
 * of 'http' the link prefix in the summary reportTestRun will be http:// instead of file://.
 *
 * Created by jordam on 2016-08-25.
 */
public class HtmlSummaryReport {
    int successfulTestCases = 0;
    int failedTestCasesWithNewDeviations = 0;
    int testCasesWithOnlyKnownErrors = 0;
    int testCasesWithBothNewAndKnownErrors = 0;
    int unevaluatedCount = 0;
    private String resultBarHtml;
    final ArrayList<KnownError> encounteredKnownErrorInfos = new ArrayList<>();
    final ArrayList<NewError> newErrorInfos = new ArrayList<>();
    final ArrayList<KnownError> solvedKnownErrorsList = new ArrayList<>();
    private String testCaseSummary = "";
    private final int barWidthInPixels = 400;
    private String resultsBarStyleInfo = "";
    private StringBuilder html;
    private final Set<String> errorClassNames = new HashSet<>();


    /**
     * Evaluates a {@link TestCase} against previous test case results in this test run. Eventually this evaluation information might be written to a {@link HtmlSummaryReport}.
     * @param testCase The {@link TestCase} to evaluate and add to accumulated list of results for later {@link HtmlSummaryReport} compilation.
     */
    public void evaluateTestCase(TestCase testCase){
        appendTestCaseResultToSummary(testCase);
        addErrorLogPostClassNamesToList(testCase);
        evaluateTestCaseLocalKnownErrorsList(testCase);
        evaluateTestCaseUnknownErrors(testCase);
    }

    private void addErrorLogPostClassNamesToList(TestCase testCase) {
        for(LogPost logPost : testCase.testCaseResult.testCaseLog.onlyErroneousLogPosts()){
            errorClassNames.add(logPost.testStepClassName);
        }
    }

    private void appendTestCaseResultToSummary(TestCase testCase){
        String link = testCase.pathToHtmlLogFile.replace("\\", "/");
        String[] parts = link.split("/");
        link = parts[parts.length -1];
        if (testCase.urlToCloudResultStorage!=null) {
            link = testCase.urlToCloudResultStorage + link;
        }
            testCaseSummary += "            <tr class=\"" + testCase.testCaseResult.resultStatus.toString() + "\"><td>" + testCase.testSetName + "</td><td>" + testCase.testName + "</td><td>" + StringManagement.enumCapitalNameToFriendlyString(testCase.testCaseResult.resultStatus.toString()) + "</td><td><a href=\"" + link + "\" target=\"_blank\">Log</a></td></tr>" + LF;
        switch (testCase.testCaseResult.resultStatus){
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
        ArrayList<LogPost> errorLogMessages = testCase.testCaseResult.testCaseLog.onlyErroneousLogPosts().stream().filter(logPost -> !logPost.identifiedToBePartOfKnownError).collect(Collectors.toCollection(ArrayList::new));
        if(errorLogMessages.size() > 0){
            List<TestCase> testCases = new ArrayList<>();
            testCases.add(testCase);
            newErrorInfos.add(new NewError(testCases, errorLogMessages));
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
     * @param testCase The {@link TestCase} to assess the {@link KnownErrorsList} from.
     * @return A HTML table row formatted text string
     */
    private String solvedKnownErrorsFromTestCaseLocalKnownErrorsList(TestCase testCase) {
        StringBuilder solvedKnownErrorsString = new StringBuilder();
        ArrayList<String> solvedKnownErrors = testCase.testCaseKnownErrorsList.knownErrors.stream().filter(knownError -> !knownError.encountered()).map(knownError -> knownError.description).collect(Collectors.toCollection(ArrayList::new));
        if (solvedKnownErrors.size() > 0) {
            boolean plural = (solvedKnownErrors.size() > 1);
            String s = String.valueOf(plural).toLowerCase().replace("true", "s").replace("false", "");
            solvedKnownErrorsString.append("<tr class=\"").append(HtmlStyleNames.SOLVED_KNOWN_ERRORS.toString()).append("\"><td colspan=\"2\">Known error").append(s).append(" for test case <i>").append(testCase.testName).append("</i> not encountered</td><td colspan=\"2\">");
            for (String solvedKnownErrorDescription : solvedKnownErrors) {
                solvedKnownErrorsString.append(solvedKnownErrorDescription).append("<br>").append(LF);
            }
            solvedKnownErrorsString.append("</td></tr>");
        }
        return solvedKnownErrorsString.toString();
    }

    /**
     * Line-feed for local OS
     */
    private static final String LF = SupportMethods.LF;

    /**
     * An enum that exist to cope with style changes by avoiding unlinked references.
     */
    @SuppressWarnings("unused")
    public enum HtmlStyleNames {
        STATISTICS,
        CONTENT,
        LICENSE_LINK,
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
    public int numberOfTestCases(){
        return successfulTestCases + failedTestCasesWithNewDeviations + testCasesWithOnlyKnownErrors + testCasesWithBothNewAndKnownErrors + unevaluatedCount;
    }

    /**
     * Produces the style section for the HTML HEAD section, to visually format the reportTestRun
     */
    @SuppressWarnings("SpellCheckingInspection")
    private void addHtmlElementStyles(){
        html
                .append(LF)
                .append("    <style>").append(LF)
                .append("      #").append(HtmlStyleNames.SOLVED_KNOWN_ERRORS.toString()).append("               { color: ").append(UxColors.DARK_GREY.getHtmlColorCode()).append("; }").append(LF)
                .append("      body                  { font-family: Helvetica Neue, Helvetica, Arial, sans-serif; color: ").append(UxColors.DARK_GREY.getHtmlColorCode()).append("; background-color: ").append(UxColors.LIGHT_BLUE.getHtmlColorCode()).append("; }").append(LF)
                .append("      a                     { color: ").append(UxColors.DARK_BLUE.getHtmlColorCode()).append(";}").append(LF)
                .append("      th                    { text-align: left; }").append(LF)
                .append("      img.toplogo           { width: 30%; }").append(LF)
                .append("      img.bottomlogo        { width: 20%; }").append(LF)
                .append("      td.bottomlogo         { text-align: center; background-color: ").append(UxColors.WHITE.getHtmlColorCode()).append("; }").append(LF)
                .append("      table#").append(HtmlStyleNames.CONTENT.toString()).append("      { background-color: ").append(UxColors.WHITE.getHtmlColorCode()).append("; padding: 30px; margin: 30px; }").append(LF)
                .append("      .moreerrorsasterisk   { color: ").append(UxColors.DARK_BLUE.getHtmlColorCode()).append("; }").append(LF)
                .append("      .testcasename { font-weight: bold; }").append(LF)
                .append("      .errorloglevel { color: ").append(UxColors.RED.getHtmlColorCode()).append("; }").append(LF)
                .append("      .testsetname  {}").append(LF)
                .append("      tr.").append(HtmlStyleNames.HOVERABLE.toString()).append(":hover           { background-color: ").append(UxColors.MID_GREY.getHtmlColorCode()).append("; }").append(LF)
                .append("      tr.").append(HtmlStyleNames.SOLVED_KNOWN_ERRORS.toString()).append("       { font-weight: bold; color: ").append(UxColors.DARK_GREY.getHtmlColorCode()).append("; }").append(LF)
                .append("      li.").append(HtmlStyleNames.HOVERABLE.toString()).append(":hover           { background-color: ").append(UxColors.MID_GREY.getHtmlColorCode()).append("; }").append(LF)
                .append("      table#").append(HtmlStyleNames.STATISTICS.toString()).append("                           { background-color: ").append(UxColors.WHITE.getHtmlColorCode()).append("; border-collapse: collapse; border: 1p solid ").append(UxColors.DARK_GREY.getHtmlColorCode()).append("; width: ").append(barWidthInPixels).append("px; }").append(LF)
                .append("      .").append(TestCaseResult.ResultStatus.FAILED_WITH_ONLY_KNOWN_ERRORS.toString()).append("             { color: ").append(UxColors.DARK_YELLOW.getHtmlColorCode()).append("; }").append(LF)
                .append("      .").append(TestCaseResult.ResultStatus.PASSED.toString()).append("                                    { color: ").append(UxColors.GREEN.getHtmlColorCode()).append("; }").append(LF)
                .append("      .").append(TestCaseResult.ResultStatus.FAILED_WITH_BOTH_NEW_AND_KNOWN_ERRORS.toString()).append("     { color: ").append(UxColors.ORANGE.getHtmlColorCode()).append("; font-weight: bold; }").append(LF)
                .append("      .").append(TestCaseResult.ResultStatus.FAILED_WITH_ONLY_NEW_ERRORS.toString()).append("               { color: ").append(UxColors.RED.getHtmlColorCode()).append("; font-weight: bold; }").append(LF)
                .append("      .").append(TestCaseResult.ResultStatus.UNEVALUATED.toString()).append("                               { color: ").append(UxColors.DARK_GREY.getHtmlColorCode()).append("; text-align: center; }").append(LF)
                .append("      .").append(HtmlStyleNames.COPYRIGHT.toString()).append("                                 { background-color: ").append(UxColors.WHITE.getHtmlColorCode()).append("; color: ").append(UxColors.DARK_BLUE.getHtmlColorCode()).append("; text-align: center; }").append(LF)
                .append("       tr#").append(HtmlStyleNames.STATISTICS_COUNT.toString()).append("          { background-color: ").append(UxColors.MID_GREY.getHtmlColorCode()).append("; }").append(LF)
                .append("       tr#").append(HtmlStyleNames.STATISTICS_HEADER_ROW.toString()).append("          { background-color: ").append(UxColors.DARK_BLUE.getHtmlColorCode()).append("; color: ").append(UxColors.WHITE.getHtmlColorCode()).append("; text-aligned: left; }").append(LF)
                .append("       table.").append(HtmlStyleNames.STRIPED_ROWS.toString()).append("                                    { background-color: ").append(UxColors.MID_GREY.getHtmlColorCode()).append("; text-align: left; }").append(LF)
                .append("       tr.testcasesummaryheadline                                     { background-color: ").append(UxColors.DARK_BLUE.getHtmlColorCode()).append("; color: ").append(UxColors.WHITE.getHtmlColorCode()).append("; }").append(LF)
                .append("       table.").append(HtmlStyleNames.STRIPED_ROWS.toString()).append(" tr:nth-child(even)                 { background-color: ").append(UxColors.LIGHT_GREY.getHtmlColorCode()).append("; }").append(LF)
                .append("       .noerrorsexclamtaion    { color: black; font-weight: bold; }").append(LF)
                .append("       h3#settingsheading      { color: ").append(UxColors.DARK_GREY.getHtmlColorCode()).append("; }").append(LF)
                .append("       table.settingsTable     { color: ").append(UxColors.DARK_GREY.getHtmlColorCode()).append("; font-size: 80%; }").append(LF)
                .append("      a.").append(HtmlStyleNames.LICENSE_LINK.toString().toLowerCase()).append("      { color: ").append(UxColors.DARK_BLUE.getHtmlColorCode()).append("; text-decoration: none; }").append(LF)
                .append("      a.").append(HtmlStyleNames.LICENSE_LINK.toString().toLowerCase()).append(":visited      { color: ").append(UxColors.DARK_BLUE.getHtmlColorCode()).append("; text-decoration: none; }").append(LF)
                .append("      a.").append(HtmlStyleNames.LICENSE_LINK.toString().toLowerCase()).append(":hover      { color: ").append(UxColors.DARK_BLUE.getHtmlColorCode()).append("; text-decoration: underline; }").append(LF)
                .append(resultsBarStyleInfo).append("    </style>").append(LF)
                .append(LF);
    }

    /**
     * Check to see if the reportTestRun should be written at all
     * @return Return true if the number of test cases exceeds one
     */
    private boolean reportShouldBeWritten(){
        return (numberOfTestCases() > 1);
    }


    /**
     * Compiles a HTML formatted summary reportTestRun from analyzed test case information.
     * @return HTML document as string
     */
    public String createReport(){
        html = new StringBuilder();
        if(reportShouldBeWritten()){
            resultBarHtml = resultsGraphBar(); //Must be created before CSS Style info. Used in statistics section
            html.append("<!DOCTYPE html>").append(LF);
            html.append("<html>").append(LF).append(LF);
            addHtmlHeadSection();
            html.append("  <body>").append(LF).append(LF);
            html.append("    <table id=\"").append(HtmlStyleNames.CONTENT.toString()).append("\">").append(LF).append(LF);
            html.append("      <tr>").append(LF);
            html.append("        <td>").append(LF).append(LF);
            addHtmlElementTitle();
            addHtmlElementStatistics();
            addHtmlElementNewErrorsGrouped();
            addHtmlElementEncounteredKnownErrors();
            addHtmlElementExecutedTestCasesStatusList();
            addHtmlElementSolvedKnownErrors();
            addHtmlElementSettings();
            html.append("        </td>").append(LF);
            html.append("      </tr>").append(LF).append(LF);
            html.append("      <tr>").append(LF);
            html.append("        <td class=\"centered\">").append(LF);
            addHtmlElementCopyright();
            html.append("        </td>").append(LF);
            html.append("      </tr>").append(LF).append(LF);
            html.append("    </table>").append(LF).append(LF);
            html.append("  </body>").append(LF).append(LF);
            html.append("</html>").append(LF);
        }
        return html.toString();
    }

    private void addHtmlHeadSection(){
        html.append("  <head>").append(LF).append(LF);
        html.append("    <title>Test summary</title>").append(LF);
        html.append("    <link rel=\"shortcut icon\" href=\"http://46.101.193.212/TAF/images/facicon.png\">").append(LF);
        html.append("    <meta charset=\"UTF-8\">").append(LF);
        html.append("    <meta name=\"description\" content=\"Summary result for test run\">").append(LF);
        addHtmlElementStyles();
        html.append("  </head>").append(LF).append(LF);
    }

    private void addHtmlElementSettings(){
        html
                .append("          <h3 id=\"settingsheading\">Test run settings</h2>").append(LF)
                .append(TestRun.getSettings().toHtmlTable());
    }

    /**
     * Produces the title section of the HTML page the summary reportTestRun consists of.
     */
    @SuppressWarnings("SpellCheckingInspection")
    private void addHtmlElementTitle(){
        html
                .append("          <img class=\"toplogo\" src=\"").append(TestRun.getSettingsValue(Settings.SettingParameters.PATH_TO_LOGO)).append("\">").append(LF)
                .append("          <h1>Test run report</h1>").append(LF)
                .append("          <table class=\"rundetails\">").append(LF)
                .append("             <tr><td>Run name: </td><td>").append(TestRun.getRunName()).append("</td></tr>").append(LF)
                .append("             <tr><td>Start time: </td><td>").append(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(TestRun.getStartTime())).append("</td></tr>").append(LF)
                .append("             <tr><td>Stop time :</td><td>").append(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(getStopTime())).append("</td></tr>").append(LF)
                .append("             <tr><td>Duration: </td><td>").append(StringManagement.timeDurationAsString(TestRun.getStartTime(), getStopTime())).append("</td></tr>").append(LF)
                .append("          </table>").append(LF);
    }

    private Date getStopTime(){
        if(TestRun.getStopTime() == null){
            TestRun.setStopTime(new Date());
        }
        return TestRun.getStopTime();
    }

    /**
     * Produces the statistics section of the summary reportTestRun
     */
    private void addHtmlElementStatistics(){
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
        html.append("          </table>").append(LF).append("<br>").append(LF);
    }

    /**
     * Produces the HTML section for new errors encountered during a test run, grouped by similarity).
     */
    private void addHtmlElementNewErrorsGrouped(){
        if(this.failedTestCasesWithNewDeviations + this.testCasesWithBothNewAndKnownErrors > 0){
            html.append("          <div id=\"").append(TestCaseLogReporterHtmlLogFile.enumMemberNameToLower(HtmlStyleNames.NEW_ERRORS.toString())).append("\">").append(LF);
            html.append("          <h2>New deviations</h2>").append(LF);
            html.append(       printNewErrors());
            html.append("          </div>").append(LF).append("<br>").append(LF);
        }
    }



    /**
     * Produces the HTML section for known errors
     */
    private void addHtmlElementEncounteredKnownErrors(){
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
                        String link = testCase.pathToHtmlLogFile.replace("\\", "/");
                        String[] parts = link.split("/");
                        link = parts[parts.length -1];
                        if (testCase.urlToCloudResultStorage!=null) {
                            link = testCase.urlToCloudResultStorage + link;
                        }
                        idsOfTestCases.add(testCase.uid.toString());
                        html.append("                <li class=\"").append(HtmlStyleNames.HOVERABLE.toString()).append("\">").append(testCase.testSetName).append(": ").append(testCase.testName).append(" (<a href=\"").append(link).append("\" target=\"_blank\">Log</a>)</li>").append(LF);
                    }
                }
                html.append("              </ul>").append(LF);
                html.append("            </p>").append(LF).append(LF);
            }
            html.append("            <br>").append(LF);
            html.append("          </div>").append(LF).append("<br>").append(LF);
        }
    }

    /**
     * Produces the HTML section for known errors not encountered
     */
    private void addHtmlElementSolvedKnownErrors(){
        if(solvedKnownErrorsList.size() > 0){
            ArrayList<String> displayableKnownErrors = new ArrayList<>();
            for(KnownError knownError : solvedKnownErrorsList){
                if(displayableKnownErrors.contains(knownError.description)) continue;
                displayableKnownErrors.add(knownError.description);
            }
            html.append("          <div id=\"").append(HtmlStyleNames.SOLVED_TEST_SET_ERRORS.toString()).append("\">").append(LF);
            html.append("            <h2>Registered known errors not encountered</h2>").append(LF);
            html.append("            <p>").append(LF);
            for(String knownError : displayableKnownErrors){
                html.append("              ['").append(knownError).append("']<br>").append(LF);
            }
            html.append("            </p>").append(LF).append(LF);
            html.append("          <br>").append(LF);
            html.append("        </div>").append(LF).append("<br>").append(LF);
        }
    }



    /**
     * Produces the HTML section for the list of executed test cases and their
     * results each. Includes links to individual test case logs.
     */
    private void addHtmlElementExecutedTestCasesStatusList(){
        html
                .append("          <h2>Test case summary</h2>").append(LF)
                .append("          <table class=\"").append(HtmlStyleNames.STRIPED_ROWS.toString()).append(" ").append(HtmlStyleNames.HOVERABLE.toString()).append("\" id=\"").append(HtmlStyleNames.EXECUTED_TEST_CASES.toString()).append("\">").append(LF)
                .append("            <tr class=\"testcasesummaryheadline\"><th>Test set</th><th>Test case name</th><th>Test status</th><th>Log</th></tr>").append(LF)
                .append("      ").append(testCaseSummary)
                .append("          </table>").append(LF)
                .append("          <br>").append(LF);
    }

    /**
     * Produces a document footer for the summary reportTestRun.
     */
    private void addHtmlElementCopyright(){
        //noinspection deprecation
        html
                .append("          <br>").append(LF)
                .append("          <br>").append(LF)
                .append("          <table width=\"100%\">").append(LF)
                .append("            <tr>").append(LF)
                .append("              <td class=\"bottomlogo\" width=\"100%\"><a href=\"http://www.claremont.se\"><img alt=\"Claremont logo\" class=\"bottomlogo\" src=\"https://www.claremont.se/globalassets/bilder/logotyp/logo-long-lightblue.svg\"></a></td>").append(LF)
                .append("            </tr><tr>").append(LF)
                .append("              <td width=\"100%\" class=\"").append(HtmlStyleNames.COPYRIGHT.toString()).append("\"><br>TAF is licensed under the <a href=\"https://www.apache.org/licenses/LICENSE-2.0\" target=\"_blank\" class=\"").append(HtmlStyleNames.LICENSE_LINK.toString().toLowerCase()).append("\">Apache 2.0</a> license. &copy; Claremont ").append(new SimpleDateFormat("yyyy").format(new Date())).append(".</td>").append(LF)
                .append("            </tr>").append(LF)
                .append("          </table>").append(LF);
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
            bar.append("              <td class=\"resultsgraphpassed\">").append(yippieTextIfNoErrors()).append("</td>").append(LF);
            resultsBarStyleInfo += "      td.resultsgraphpassed { color: " + UxColors.WHITE.getHtmlColorCode() + "; font-style: italic; font-weight: bold; text-align: center; background-color: " + UxColors.GREEN.getHtmlColorCode() + "; height: 15px; width: " + (this.successfulTestCases * 100) / numberOfTestCases() + "%; }" + LF;
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

    @SuppressWarnings("SpellCheckingInspection")
    private String yippieTextIfNoErrors(){
        if(this.failedTestCasesWithNewDeviations + this.testCasesWithBothNewAndKnownErrors == 0){
            return "YIPPIE!";
        }
        return "";
    }
    /**
     * Produces the HTML section for new errors in the summary testCaseLog, if new errors (errors
     * that cannot be identified as known errors) are identified in the test case logs.
     * @return HTML section for new errors.
     */
    private String printNewErrors(){
        NewErrorsList newErrorsList = new NewErrorsList(newErrorInfos, new ArrayList<>(errorClassNames));
        return newErrorsList.toString();
    }


    private String truncateLogMessageIfNeeded(String logMessage){
        if(logMessage.length() < 100)return logMessage;
        return logMessage.substring(0, 97) + "...";
    }

}
