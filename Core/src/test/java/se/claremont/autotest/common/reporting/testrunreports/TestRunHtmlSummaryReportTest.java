package se.claremont.autotest.common.reporting.testrunreports;

import org.junit.Assert;
import org.junit.Test;
import se.claremont.autotest.common.logging.LogLevel;
import se.claremont.autotest.common.testcase.TestCase;
import se.claremont.autotest.common.testset.UnitTestClass;

/**
 * Test the HTML summary report file from a test run
 *
 * Created by jordam on 2017-03-20.
 */
public class TestRunHtmlSummaryReportTest extends UnitTestClass {

    @Test
    public void yippieTextIsDisplayedWhenNoNewErrors(){
        TestRunReporterHtmlSummaryReportFile testRunReporterHtmlSummaryReportFile = new TestRunReporterHtmlSummaryReportFile();
        TestCase testCase = new TestCase();
        testCase.addKnownError("KnownErrorDescription", ".*KnownErrorPattern.*");
        testCase.log(LogLevel.INFO, "Info message");
        testCase.log(LogLevel.VERIFICATION_FAILED, "KnownErrorPattern");
        testCase.evaluateResultStatus();
        testRunReporterHtmlSummaryReportFile.evaluateTestCase(testCase);
        TestCase testCase2 = new TestCase();
        testCase2.evaluateResultStatus();
        testRunReporterHtmlSummaryReportFile.evaluateTestCase(testCase2);
        testRunReporterHtmlSummaryReportFile.report();
        String htmlReport = testRunReporterHtmlSummaryReportFile.htmlSummaryReport.createReport();
        System.out.println(htmlReport);
        Assert.assertTrue(htmlReport, htmlReport.contains("YIPPIE!"));
    }

    @Test
    public void yippieTextIsNotDisplayedWhenNewErrors(){
        TestRunReporterHtmlSummaryReportFile testRunReporterHtmlSummaryReportFile = new TestRunReporterHtmlSummaryReportFile();
        TestCase testCase = new TestCase();
        testCase.log(LogLevel.INFO, "Info message");
        testCase.log(LogLevel.VERIFICATION_FAILED, "KnownErrorPattern");
        testCase.evaluateResultStatus();
        testRunReporterHtmlSummaryReportFile.evaluateTestCase(testCase);
        TestCase testCase2 = new TestCase();
        testCase2.evaluateResultStatus();
        testRunReporterHtmlSummaryReportFile.evaluateTestCase(testCase2);
        testRunReporterHtmlSummaryReportFile.report();
        String htmlReport = testRunReporterHtmlSummaryReportFile.htmlSummaryReport.createReport();
        System.out.println(htmlReport);
        Assert.assertFalse(htmlReport, htmlReport.contains("YIPPIE!"));
    }
}
