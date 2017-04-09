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

    @Test
    public void onlyMostRelevantLogRowsShownForErroneousTestCases(){
        TestRunReporterHtmlSummaryReportFile report = new TestRunReporterHtmlSummaryReportFile();
        TestCase testCase1 = new TestCase(null, "TestCase1");
        testCase1.log(LogLevel.INFO, "Text");
        testCase1.log(LogLevel.VERIFICATION_FAILED, "Failed1");
        testCase1.log(LogLevel.EXECUTION_PROBLEM, "Failed2");
        testCase1.log(LogLevel.VERIFICATION_PROBLEM, "Failed3");
        testCase1.log(LogLevel.FRAMEWORK_ERROR, "Failed4");
        testCase1.log(LogLevel.INFO, "Passed1");
        testCase1.log(LogLevel.DEVIATION_EXTRA_INFO, "Passed2");
        testCase1.log(LogLevel.EXECUTED, "Passed3");
        testCase1.log(LogLevel.DEBUG, "Passed4");
        testCase1.log(LogLevel.VERIFICATION_PASSED,"Passed5");
        testCase1.evaluateResultStatus();
        report.evaluateTestCase(testCase1);
        TestCase testCase2 = new TestCase(null, "TestCase2");
        report.evaluateTestCase(testCase2);
        String reportContent = report.htmlSummaryReport.createReport();
        System.out.println(reportContent);
        Assert.assertTrue(reportContent.contains("Failed1"));
        Assert.assertTrue(reportContent.contains("...(2 more"));
        Assert.assertTrue(reportContent.contains("Failed4"));
        Assert.assertFalse(reportContent.contains("Failed3"));
        Assert.assertFalse(reportContent.contains("Failed2"));
        Assert.assertFalse(reportContent.contains("Passed1"));
        Assert.assertFalse(reportContent.contains("Passed3"));
        Assert.assertFalse(reportContent.contains("Passed2"));
        Assert.assertFalse(reportContent.contains("Passed5"));
        Assert.assertFalse(reportContent.contains("Passed4"));
        Assert.assertTrue(reportContent.contains("TestCase1"));
        Assert.assertTrue(reportContent.contains("TestCase2"));
    }

    @Test
    public void highestLogLevelPostForNewErrorShownForErroneousTestCases(){
        TestRunReporterHtmlSummaryReportFile report = new TestRunReporterHtmlSummaryReportFile();
        TestCase testCase1 = new TestCase(null, "TestCase1");
        testCase1.log(LogLevel.INFO, "Text");
        testCase1.log(LogLevel.VERIFICATION_FAILED, "Failed1");
        testCase1.log(LogLevel.FRAMEWORK_ERROR, "Failed4");
        testCase1.log(LogLevel.EXECUTION_PROBLEM, "Failed2");
        testCase1.log(LogLevel.VERIFICATION_PROBLEM, "Failed3");
        testCase1.log(LogLevel.INFO, "Passed1");
        testCase1.log(LogLevel.DEVIATION_EXTRA_INFO, "Passed2");
        testCase1.log(LogLevel.EXECUTED, "Passed3");
        testCase1.log(LogLevel.DEBUG, "Passed4");
        testCase1.log(LogLevel.VERIFICATION_PASSED,"Passed5");
        testCase1.evaluateResultStatus();
        report.evaluateTestCase(testCase1);
        TestCase testCase2 = new TestCase(null, "TestCase2");
        report.evaluateTestCase(testCase2);
        String reportContent = report.htmlSummaryReport.createReport();
        System.out.println(reportContent);
        Assert.assertTrue(reportContent.contains("Failed1"));
        Assert.assertTrue(reportContent.contains("...(2 more"));
        Assert.assertTrue(reportContent.contains("Failed4"));
        Assert.assertFalse(reportContent.contains("Failed3"));
        Assert.assertFalse(reportContent.contains("Failed2"));
        Assert.assertFalse(reportContent.contains("Passed1"));
        Assert.assertFalse(reportContent.contains("Passed3"));
        Assert.assertFalse(reportContent.contains("Passed2"));
        Assert.assertFalse(reportContent.contains("Passed5"));
        Assert.assertFalse(reportContent.contains("Passed4"));
        Assert.assertTrue(reportContent.contains("TestCase1"));
        Assert.assertTrue(reportContent.contains("TestCase2"));
    }

    @Test
    public void highestLogLevelForNewErrorAsLastLogPostShouldBePresentedCorrectly(){
        TestRunReporterHtmlSummaryReportFile report = new TestRunReporterHtmlSummaryReportFile();
        TestCase testCase1 = new TestCase(null, "TestCase1");
        testCase1.log(LogLevel.INFO, "Text");
        testCase1.log(LogLevel.VERIFICATION_FAILED, "Failed1");
        testCase1.log(LogLevel.VERIFICATION_FAILED, "Failed11");
        testCase1.log(LogLevel.VERIFICATION_FAILED, "Failed12");
        testCase1.log(LogLevel.VERIFICATION_FAILED, "Failed13");
        testCase1.log(LogLevel.INFO, "Passed1");
        testCase1.log(LogLevel.DEVIATION_EXTRA_INFO, "Passed2");
        testCase1.log(LogLevel.EXECUTED, "Passed3");
        testCase1.log(LogLevel.DEBUG, "Passed4");
        testCase1.log(LogLevel.VERIFICATION_PASSED,"Passed5");
        testCase1.log(LogLevel.EXECUTION_PROBLEM, "Failed2");
        testCase1.log(LogLevel.INFO, "Passed1");
        testCase1.log(LogLevel.DEVIATION_EXTRA_INFO, "Passed2");
        testCase1.log(LogLevel.EXECUTED, "Passed3");
        testCase1.log(LogLevel.DEBUG, "Passed4");
        testCase1.log(LogLevel.VERIFICATION_PASSED,"Passed5");
        testCase1.log(LogLevel.VERIFICATION_PROBLEM, "Failed3");
        testCase1.log(LogLevel.INFO, "Passed1");
        testCase1.log(LogLevel.DEVIATION_EXTRA_INFO, "Passed2");
        testCase1.log(LogLevel.EXECUTED, "Passed3");
        testCase1.log(LogLevel.DEBUG, "Passed4");
        testCase1.log(LogLevel.VERIFICATION_PASSED,"Passed5");

        //Framework error is the most severe type of error
        testCase1.log(LogLevel.FRAMEWORK_ERROR, "Failed4");
        testCase1.evaluateResultStatus();
        report.evaluateTestCase(testCase1);
        TestCase testCase2 = new TestCase(null, "TestCase2");
        report.evaluateTestCase(testCase2);
        String reportContent = report.htmlSummaryReport.createReport();
        System.out.println(reportContent);
        Assert.assertTrue(reportContent.contains("Failed1"));
        Assert.assertTrue(reportContent.contains("...(5 more"));
        Assert.assertTrue(reportContent.contains("Failed4"));
        Assert.assertFalse(reportContent.contains("Failed3"));
        Assert.assertFalse(reportContent.contains("Failed2"));
        Assert.assertFalse(reportContent.contains("Passed1"));
        Assert.assertFalse(reportContent.contains("Passed3"));
        Assert.assertFalse(reportContent.contains("Passed2"));
        Assert.assertFalse(reportContent.contains("Passed5"));
        Assert.assertFalse(reportContent.contains("Passed4"));
        Assert.assertTrue(reportContent.contains("TestCase1"));
        Assert.assertTrue(reportContent.contains("TestCase2"));
    }

    @Test
    public void highestLogLevelForNewErrorAsFirstLogPostShouldBePresentedCorrectly(){
        TestRunReporterHtmlSummaryReportFile report = new TestRunReporterHtmlSummaryReportFile();
        TestCase testCase1 = new TestCase(null, "TestCase1");
        testCase1.log(LogLevel.INFO, "Text");
        //Framework error is the most severe type of error
        testCase1.log(LogLevel.FRAMEWORK_ERROR, "Failed4");
        testCase1.log(LogLevel.VERIFICATION_FAILED, "Failed1");
        testCase1.log(LogLevel.VERIFICATION_FAILED, "Failed11");
        testCase1.log(LogLevel.VERIFICATION_FAILED, "Failed12");
        testCase1.log(LogLevel.VERIFICATION_FAILED, "Failed13");
        testCase1.log(LogLevel.INFO, "Passed1");
        testCase1.log(LogLevel.DEVIATION_EXTRA_INFO, "Passed2");
        testCase1.log(LogLevel.EXECUTED, "Passed3");
        testCase1.log(LogLevel.DEBUG, "Passed4");
        testCase1.log(LogLevel.VERIFICATION_PASSED,"Passed5");
        testCase1.log(LogLevel.EXECUTION_PROBLEM, "Failed2");
        testCase1.log(LogLevel.INFO, "Passed1");
        testCase1.log(LogLevel.DEVIATION_EXTRA_INFO, "Passed2");
        testCase1.log(LogLevel.EXECUTED, "Passed3");
        testCase1.log(LogLevel.DEBUG, "Passed4");
        testCase1.log(LogLevel.VERIFICATION_PASSED,"Passed5");
        testCase1.log(LogLevel.VERIFICATION_PROBLEM, "Failed3");
        testCase1.log(LogLevel.INFO, "Passed1");
        testCase1.log(LogLevel.DEVIATION_EXTRA_INFO, "Passed2");
        testCase1.log(LogLevel.EXECUTED, "Passed3");
        testCase1.log(LogLevel.DEBUG, "Passed4");
        testCase1.log(LogLevel.VERIFICATION_PASSED,"Passed5");
        testCase1.evaluateResultStatus();
        report.evaluateTestCase(testCase1);
        TestCase testCase2 = new TestCase(null, "TestCase2");
        report.evaluateTestCase(testCase2);
        String reportContent = report.htmlSummaryReport.createReport();
        System.out.println(reportContent);
        Assert.assertTrue(reportContent.contains("Failed4"));
        Assert.assertTrue(reportContent.contains("...(5 more"));
        Assert.assertTrue(reportContent.contains("Failed3"));
        Assert.assertFalse(reportContent.contains("Failed1"));
        Assert.assertFalse(reportContent.contains("Failed2"));
        Assert.assertFalse(reportContent.contains("Passed1"));
        Assert.assertFalse(reportContent.contains("Passed3"));
        Assert.assertFalse(reportContent.contains("Passed2"));
        Assert.assertFalse(reportContent.contains("Passed5"));
        Assert.assertFalse(reportContent.contains("Passed4"));
        Assert.assertTrue(reportContent.contains("TestCase1"));
        Assert.assertTrue(reportContent.contains("TestCase2"));
    }

    @Test
    public void highestLogLevelForNewErrorAsMiddleLogPostShouldBePresentedCorrectly(){
        TestRunReporterHtmlSummaryReportFile report = new TestRunReporterHtmlSummaryReportFile();
        TestCase testCase1 = new TestCase(null, "TestCase1");
        testCase1.log(LogLevel.INFO, "Text");
        //Framework error is the most severe type of error
        testCase1.log(LogLevel.VERIFICATION_FAILED, "Failed1");
        testCase1.log(LogLevel.VERIFICATION_FAILED, "Failed11");
        testCase1.log(LogLevel.FRAMEWORK_ERROR, "Failed4");
        testCase1.log(LogLevel.VERIFICATION_FAILED, "Failed12");
        testCase1.log(LogLevel.VERIFICATION_FAILED, "Failed13");
        testCase1.log(LogLevel.INFO, "Passed1");
        testCase1.log(LogLevel.DEVIATION_EXTRA_INFO, "Passed2");
        testCase1.log(LogLevel.EXECUTED, "Passed3");
        testCase1.log(LogLevel.DEBUG, "Passed4");
        testCase1.log(LogLevel.VERIFICATION_PASSED,"Passed5");
        testCase1.log(LogLevel.EXECUTION_PROBLEM, "Failed2");
        testCase1.log(LogLevel.INFO, "Passed1");
        testCase1.log(LogLevel.DEVIATION_EXTRA_INFO, "Passed2");
        testCase1.log(LogLevel.EXECUTED, "Passed3");
        testCase1.log(LogLevel.DEBUG, "Passed4");
        testCase1.log(LogLevel.VERIFICATION_PASSED,"Passed5");
        testCase1.log(LogLevel.VERIFICATION_PROBLEM, "Failed3");
        testCase1.log(LogLevel.INFO, "Passed1");
        testCase1.log(LogLevel.DEVIATION_EXTRA_INFO, "Passed2");
        testCase1.log(LogLevel.EXECUTED, "Passed3");
        testCase1.log(LogLevel.DEBUG, "Passed4");
        testCase1.log(LogLevel.VERIFICATION_PASSED,"Passed5");
        testCase1.evaluateResultStatus();
        report.evaluateTestCase(testCase1);
        TestCase testCase2 = new TestCase(null, "TestCase2");
        report.evaluateTestCase(testCase2);
        String reportContent = report.htmlSummaryReport.createReport();
        System.out.println(reportContent);
        Assert.assertTrue(reportContent.contains("Failed1"));
        Assert.assertTrue(reportContent.contains("Failed11"));
        Assert.assertTrue(reportContent.contains("Failed4"));
        Assert.assertTrue(reportContent.contains("...(4 more"));
        Assert.assertFalse(reportContent.contains("Failed2"));
        Assert.assertFalse(reportContent.contains("Passed1"));
        Assert.assertFalse(reportContent.contains("Passed3"));
        Assert.assertFalse(reportContent.contains("Passed2"));
        Assert.assertFalse(reportContent.contains("Passed5"));
        Assert.assertFalse(reportContent.contains("Passed4"));
        Assert.assertTrue(reportContent.contains("TestCase1"));
        Assert.assertTrue(reportContent.contains("TestCase2"));
    }

    @Test
    public void singleErrorShouldBeReported(){
        TestRunReporterHtmlSummaryReportFile report = new TestRunReporterHtmlSummaryReportFile();
        TestCase testCase1 = new TestCase(null, "TestCase1");
        testCase1.log(LogLevel.INFO, "Text");
        //Framework error is the most severe type of error
        testCase1.log(LogLevel.VERIFICATION_FAILED, "Failed1");
        testCase1.evaluateResultStatus();
        report.evaluateTestCase(testCase1);
        TestCase testCase2 = new TestCase(null, "TestCase2");
        report.evaluateTestCase(testCase2);
        String reportContent = report.htmlSummaryReport.createReport();
        System.out.println(reportContent);
        Assert.assertTrue(reportContent.contains("Failed1"));
        Assert.assertTrue(reportContent.contains("TestCase1"));
        Assert.assertTrue(reportContent.contains("TestCase2"));
    }

    @Test
    public void twoErrorShouldBeReported(){
        TestRunReporterHtmlSummaryReportFile report = new TestRunReporterHtmlSummaryReportFile();
        TestCase testCase1 = new TestCase(null, "TestCase1");
        testCase1.log(LogLevel.INFO, "Text");
        //Framework error is the most severe type of error
        testCase1.log(LogLevel.VERIFICATION_FAILED, "Failed1");
        testCase1.log(LogLevel.VERIFICATION_FAILED, "Failed2");
        testCase1.evaluateResultStatus();
        report.evaluateTestCase(testCase1);
        TestCase testCase2 = new TestCase(null, "TestCase2");
        report.evaluateTestCase(testCase2);
        String reportContent = report.htmlSummaryReport.createReport();
        System.out.println(reportContent);
        Assert.assertTrue(reportContent.contains("Failed1"));
        Assert.assertTrue(reportContent.contains("Failed2"));
        Assert.assertTrue(reportContent.contains("TestCase1"));
        Assert.assertTrue(reportContent.contains("TestCase2"));
    }

    @Test
    public void threeErrorShouldBeReported(){
        TestRunReporterHtmlSummaryReportFile report = new TestRunReporterHtmlSummaryReportFile();
        TestCase testCase1 = new TestCase(null, "TestCase1");
        testCase1.log(LogLevel.INFO, "Text");
        //Framework error is the most severe type of error
        testCase1.log(LogLevel.VERIFICATION_FAILED, "Failed1");
        testCase1.log(LogLevel.VERIFICATION_FAILED, "Failed2");
        testCase1.log(LogLevel.VERIFICATION_FAILED, "Failed3");
        testCase1.evaluateResultStatus();
        report.evaluateTestCase(testCase1);
        TestCase testCase2 = new TestCase(null, "TestCase2");
        report.evaluateTestCase(testCase2);
        String reportContent = report.htmlSummaryReport.createReport();
        System.out.println(reportContent);
        Assert.assertTrue(reportContent.contains("Failed1"));
        Assert.assertTrue(reportContent.contains("Failed2"));
        Assert.assertTrue(reportContent.contains("Failed3"));
        Assert.assertTrue(reportContent.contains("TestCase1"));
        Assert.assertTrue(reportContent.contains("TestCase2"));
    }

    @Test
    public void longLogRowsShouldBeTruncated(){
        TestRunReporterHtmlSummaryReportFile report = new TestRunReporterHtmlSummaryReportFile();
        TestCase testCase1 = new TestCase(null, "TestCase1");
        testCase1.log(LogLevel.INFO, "Text");
        //Framework error is the most severe type of error
        testCase1.log(LogLevel.VERIFICATION_FAILED, "12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");
        testCase1.evaluateResultStatus();
        report.evaluateTestCase(testCase1);
        TestCase testCase2 = new TestCase(null, "TestCase2");
        report.evaluateTestCase(testCase2);
        String reportContent = report.htmlSummaryReport.createReport();
        System.out.println(reportContent);
        Assert.assertTrue(reportContent.contains("1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567..."));
        Assert.assertTrue(reportContent.contains("TestCase1"));
        Assert.assertTrue(reportContent.contains("TestCase2"));
    }

    @Test
    public void sharedLogRowsFoundInSeveralTestCasesShouldBeReported(){
        TestRunReporterHtmlSummaryReportFile report = new TestRunReporterHtmlSummaryReportFile();
        TestCase testCase1 = new TestCase(null, "TestCase1");
        testCase1.log(LogLevel.INFO, "Text");
        //Framework error is the most severe type of error
        testCase1.log(LogLevel.VERIFICATION_FAILED, "SharedLogRow");
        testCase1.log(LogLevel.VERIFICATION_FAILED, "UnSharedLogRow");
        testCase1.evaluateResultStatus();
        report.evaluateTestCase(testCase1);
        TestCase testCase2 = new TestCase(null, "TestCase2");
        testCase2.log(LogLevel.VERIFICATION_FAILED, "SharedLogRow");
        report.evaluateTestCase(testCase2);
        String reportContent = report.htmlSummaryReport.createReport();
        System.out.println(reportContent);
        Assert.assertTrue(reportContent.contains("sharedlogposts"));
        Assert.assertTrue(reportContent.contains("TestCase1"));
        Assert.assertTrue(reportContent.contains("TestCase2"));
    }

    @Test
    public void testCasesWithSimilarErrorsShouldBeReportedAsANote(){
        TestRunReporterHtmlSummaryReportFile report = new TestRunReporterHtmlSummaryReportFile();
        TestCase testCase1 = new TestCase(null, "TestCase1");
        testCase1.log(LogLevel.INFO, "Text");
        //Framework error is the most severe type of error
        testCase1.log(LogLevel.VERIFICATION_FAILED, "SharedLogRow '234' found. Duration 123 milliseconds.");
        testCase1.log(LogLevel.VERIFICATION_FAILED, "UnSharedLogRow");
        testCase1.evaluateResultStatus();
        report.evaluateTestCase(testCase1);
        TestCase testCase2 = new TestCase(null, "TestCase2");
        testCase2.log(LogLevel.VERIFICATION_FAILED, "SharedLogRow '354' found. Duration 7643 milliseconds.");
        testCase2.evaluateResultStatus();
        report.evaluateTestCase(testCase2);
        String reportContent = report.htmlSummaryReport.createReport();
        System.out.println(reportContent);
        Assert.assertTrue(reportContent.contains("sharedlogposts"));
        Assert.assertTrue(reportContent.contains("TestCase1"));
        Assert.assertTrue(reportContent.contains("TestCase2"));
    }


}
