package se.claremont.autotest.common.reporting.testrunreports;

import se.claremont.autotest.common.logging.LogFolder;
import se.claremont.autotest.common.logging.LogLevel;
import se.claremont.autotest.common.logging.LogPost;
import se.claremont.autotest.common.reporting.testrunreports.htmlsummaryreport.HtmlSummaryReport;
import se.claremont.autotest.common.support.SupportMethods;
import se.claremont.autotest.common.testcase.TestCase;
import se.claremont.autotest.common.testrun.reportingengine.TestRunReporter;
import se.claremont.autotest.common.testset.TestSet;

import static se.claremont.autotest.common.support.SupportMethods.LF;

/**
 * Saves the results of a test run to a HTML file
 *
 * Created by jordam on 2016-09-19.
 */
public class TestRunReporterHtmlSummaryReportFile implements TestRunReporter {
    public final HtmlSummaryReport htmlSummaryReport = new HtmlSummaryReport();

    @Override
    public void report() {
        writeReport();
    }

    @Override
    public void evaluateTestCase(TestCase testCase) {
        htmlSummaryReport.evaluateTestCase(testCase);

    }

    public void evaluateTestSet(TestSet testSet){
        htmlSummaryReport.evaluateTestSet(testSet);
    }

    public String reportContent(){
        String content = htmlSummaryReport.createReport();
        content = content.replace("<head>", "<head>" + LF + "    <meta http-equiv=\"refresh\" content=\"120\">");
        return content;
    }

    /**
     * Check to see if the reportTestRun should be written at all
     * @return Return true if the number of test cases exceeds one
     */
    private boolean reportShouldBeWritten(){
        return (htmlSummaryReport.numberOfTestCases() > 1);
    }

    /**
     * Writes the compiled summary reportTestRun to a file in the test run catalogue.
     */
    private void writeReport(){
        if(reportShouldBeWritten()){
            SupportMethods.saveToFile(reportContent(), LogFolder.testRunLogFolder + "_summary.html");
            LogPost logPost = new LogPost(LogLevel.EXECUTED, "Summary reportTestRun saved as '" + LogFolder.testRunLogFolder + "_summary.html'.", null,"Framework", "Framework actions", this.getClass().getSimpleName());
            System.out.println(LF + logPost.toString());
        }
    }

}
