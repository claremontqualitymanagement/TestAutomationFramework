package se.claremont.taf.core.reporting.testrunreports;

import org.junit.Assert;
import org.junit.Test;
import se.claremont.taf.core.logging.LogLevel;
import se.claremont.taf.core.logging.LogPost;
import se.claremont.taf.core.reporting.testrunreports.htmlsummaryreport.HtmlSummaryReport;
import se.claremont.taf.core.support.SupportMethods;
import se.claremont.taf.core.testcase.TestCase;
import se.claremont.taf.core.testset.TestSet;
import se.claremont.taf.core.testset.UnitTestClass;

/**
 * Test the HTML summary report file from a test run
 *
 * Created by jordam on 2017-03-20.
 */
@SuppressWarnings("ALL")
public class TestRunHtmlSummaryReportTest extends UnitTestClass {

    @Test
    public void yippieTextIsDisplayedWhenNoNewErrors(){
        TestRunReporterHtmlSummaryReportFile testRunReporterHtmlSummaryReportFile = new TestRunReporterHtmlSummaryReportFile();
        TestCase testCase = new TestCase();
        testCase.addKnownError("KnownErrorDescription", ".*KnownErrorPattern.*");
        testCase.log(LogLevel.INFO, "Info message");
        testCase.log(LogLevel.VERIFICATION_FAILED, "KnownErrorPattern");
        testCase.testCaseResult.evaluateResultStatus();
        testRunReporterHtmlSummaryReportFile.evaluateTestCase(testCase);
        TestCase testCase2 = new TestCase();
        testCase2.testCaseResult.evaluateResultStatus();
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
        testCase.testCaseResult.evaluateResultStatus();
        testRunReporterHtmlSummaryReportFile.evaluateTestCase(testCase);
        TestCase testCase2 = new TestCase();
        testCase2.testCaseResult.evaluateResultStatus();
        testRunReporterHtmlSummaryReportFile.evaluateTestCase(testCase2);
        testRunReporterHtmlSummaryReportFile.report();
        String htmlReport = testRunReporterHtmlSummaryReportFile.htmlSummaryReport.createReport();
        System.out.println(htmlReport);
        Assert.assertFalse(htmlReport, htmlReport.contains("YIPPIE!"));
    }

    void pause() {
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void onlyMostRelevantLogRowsShownForErroneousTestCases(){
        TestRunReporterHtmlSummaryReportFile report = new TestRunReporterHtmlSummaryReportFile();
        TestCase testCase1 = new TestCase(null, "TestCase1");
        testCase1.log(LogLevel.INFO, "Text");
        testCase1.log(LogLevel.VERIFICATION_FAILED, "Failed1");
        pause();
        testCase1.log(LogLevel.EXECUTION_PROBLEM, "Failed2");
        pause();
        testCase1.log(LogLevel.VERIFICATION_PROBLEM, "Failed3");
        pause();
        testCase1.log(LogLevel.FRAMEWORK_ERROR, "Failed4");
        pause();
        testCase1.log(LogLevel.INFO, "Passed1");
        testCase1.log(LogLevel.DEVIATION_EXTRA_INFO, "Passed2");
        testCase1.log(LogLevel.EXECUTED, "Passed3");
        testCase1.log(LogLevel.DEBUG, "Passed4");
        testCase1.log(LogLevel.VERIFICATION_PASSED,"Passed5");
        testCase1.testCaseResult.evaluateResultStatus();
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
        pause();
        testCase1.log(LogLevel.FRAMEWORK_ERROR, "Failed4");
        pause();
        testCase1.log(LogLevel.EXECUTION_PROBLEM, "Failed2");
        pause();
        testCase1.log(LogLevel.VERIFICATION_PROBLEM, "Failed3");
        pause();
        testCase1.log(LogLevel.INFO, "Passed1");
        testCase1.log(LogLevel.DEVIATION_EXTRA_INFO, "Passed2");
        pause();
        testCase1.log(LogLevel.EXECUTED, "Passed3");
        testCase1.log(LogLevel.DEBUG, "Passed4");
        testCase1.log(LogLevel.VERIFICATION_PASSED,"Passed5");
        testCase1.testCaseResult.evaluateResultStatus();
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
        pause();
        testCase1.log(LogLevel.VERIFICATION_FAILED, "Failed11");
        pause();
        testCase1.log(LogLevel.VERIFICATION_FAILED, "Failed12");
        pause();
        testCase1.log(LogLevel.VERIFICATION_FAILED, "Failed13");
        pause();
        testCase1.log(LogLevel.INFO, "Passed1");
        testCase1.log(LogLevel.DEVIATION_EXTRA_INFO, "Passed2");
        testCase1.log(LogLevel.EXECUTED, "Passed3");
        testCase1.log(LogLevel.DEBUG, "Passed4");
        testCase1.log(LogLevel.VERIFICATION_PASSED,"Passed5");
        testCase1.log(LogLevel.EXECUTION_PROBLEM, "Failed2");
        pause();
        testCase1.log(LogLevel.INFO, "Passed1");
        testCase1.log(LogLevel.DEVIATION_EXTRA_INFO, "Passed2");
        testCase1.log(LogLevel.EXECUTED, "Passed3");
        testCase1.log(LogLevel.DEBUG, "Passed4");
        testCase1.log(LogLevel.VERIFICATION_PASSED,"Passed5");
        testCase1.log(LogLevel.VERIFICATION_PROBLEM, "Failed3");
        pause();
        testCase1.log(LogLevel.INFO, "Passed1");
        testCase1.log(LogLevel.DEVIATION_EXTRA_INFO, "Passed2");
        testCase1.log(LogLevel.EXECUTED, "Passed3");
        testCase1.log(LogLevel.DEBUG, "Passed4");
        testCase1.log(LogLevel.VERIFICATION_PASSED,"Passed5");

        //Framework error is the most severe type of error
        testCase1.log(LogLevel.FRAMEWORK_ERROR, "Failed4");
        testCase1.testCaseResult.evaluateResultStatus();
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
        pause();
        testCase1.log(LogLevel.VERIFICATION_FAILED, "Failed1");
        pause();
        testCase1.log(LogLevel.VERIFICATION_FAILED, "Failed11");
        pause();
        testCase1.log(LogLevel.VERIFICATION_FAILED, "Failed12");
        pause();
        testCase1.log(LogLevel.VERIFICATION_FAILED, "Failed13");
        pause();
        testCase1.log(LogLevel.INFO, "Passed1");
        testCase1.log(LogLevel.DEVIATION_EXTRA_INFO, "Passed2");
        testCase1.log(LogLevel.EXECUTED, "Passed3");
        testCase1.log(LogLevel.DEBUG, "Passed4");
        testCase1.log(LogLevel.VERIFICATION_PASSED,"Passed5");
        testCase1.log(LogLevel.EXECUTION_PROBLEM, "Failed2");
        pause();
        testCase1.log(LogLevel.INFO, "Passed1");
        testCase1.log(LogLevel.DEVIATION_EXTRA_INFO, "Passed2");
        testCase1.log(LogLevel.EXECUTED, "Passed3");
        testCase1.log(LogLevel.DEBUG, "Passed4");
        testCase1.log(LogLevel.VERIFICATION_PASSED,"Passed5");
        testCase1.log(LogLevel.VERIFICATION_PROBLEM, "Failed3");
        pause();
        testCase1.log(LogLevel.INFO, "Passed1");
        testCase1.log(LogLevel.DEVIATION_EXTRA_INFO, "Passed2");
        testCase1.log(LogLevel.EXECUTED, "Passed3");
        testCase1.log(LogLevel.DEBUG, "Passed4");
        testCase1.log(LogLevel.VERIFICATION_PASSED,"Passed5");
        testCase1.testCaseResult.evaluateResultStatus();
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
        pause();
        testCase1.log(LogLevel.VERIFICATION_FAILED, "Failed11");
        pause();
        testCase1.log(LogLevel.FRAMEWORK_ERROR, "Failed4");
        pause();
        testCase1.log(LogLevel.VERIFICATION_FAILED, "Failed12");
        pause();
        testCase1.log(LogLevel.VERIFICATION_FAILED, "Failed13");
        pause();
        testCase1.log(LogLevel.INFO, "Passed1");
        testCase1.log(LogLevel.DEVIATION_EXTRA_INFO, "Passed2");
        pause();
        testCase1.log(LogLevel.EXECUTED, "Passed3");
        testCase1.log(LogLevel.DEBUG, "Passed4");
        testCase1.log(LogLevel.VERIFICATION_PASSED,"Passed5");
        pause();
        testCase1.log(LogLevel.EXECUTION_PROBLEM, "Failed2");
        pause();
        testCase1.log(LogLevel.INFO, "Passed1");
        testCase1.log(LogLevel.DEVIATION_EXTRA_INFO, "Passed2");
        testCase1.log(LogLevel.EXECUTED, "Passed3");
        testCase1.log(LogLevel.DEBUG, "Passed4");
        testCase1.log(LogLevel.VERIFICATION_PASSED,"Passed5");
        testCase1.log(LogLevel.VERIFICATION_PROBLEM, "Failed3");
        pause();
        testCase1.log(LogLevel.INFO, "Passed1");
        testCase1.log(LogLevel.DEVIATION_EXTRA_INFO, "Passed2");
        testCase1.log(LogLevel.EXECUTED, "Passed3");
        testCase1.log(LogLevel.DEBUG, "Passed4");
        testCase1.log(LogLevel.VERIFICATION_PASSED,"Passed5");
        testCase1.testCaseResult.evaluateResultStatus();
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
        testCase1.testCaseResult.evaluateResultStatus();
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
        testCase1.testCaseResult.evaluateResultStatus();
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
        testCase1.testCaseResult.evaluateResultStatus();
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
        testCase1.testCaseResult.evaluateResultStatus();
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
        testCase1.testCaseResult.evaluateResultStatus();
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
    public void testRunReportsToFileShouldReloadEveryOtherMinute(){
        TestRunReporterHtmlSummaryReportFile report = new TestRunReporterHtmlSummaryReportFile();
        TestCase testCase1 = new TestCase(null, "TestCase1");
        testCase1.log(LogLevel.INFO, "Text");
        //Framework error is the most severe type of error
        testCase1.log(LogLevel.VERIFICATION_FAILED, "SharedLogRow '234' found. Duration 123 milliseconds.");
        testCase1.log(LogLevel.VERIFICATION_FAILED, "UnSharedLogRow");
        testCase1.testCaseResult.evaluateResultStatus();
        report.evaluateTestCase(testCase1);
        TestCase testCase2 = new TestCase(null, "TestCase2");
        testCase2.log(LogLevel.VERIFICATION_FAILED, "SharedLogRow '354' found. Duration 7643 milliseconds.");
        testCase2.testCaseResult.evaluateResultStatus();
        report.evaluateTestCase(testCase2);
        String reportContent = report.reportContent();
        System.out.println(reportContent);
        Assert.assertTrue(reportContent.contains("<meta http-equiv=\"refresh\" content=\"120\">"));
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
        testCase1.testCaseResult.evaluateResultStatus();
        report.evaluateTestCase(testCase1);
        TestCase testCase2 = new TestCase(null, "TestCase2");
        testCase2.log(LogLevel.VERIFICATION_FAILED, "SharedLogRow '354' found. Duration 7643 milliseconds.");
        testCase2.testCaseResult.evaluateResultStatus();
        report.evaluateTestCase(testCase2);
        String reportContent = report.htmlSummaryReport.createReport();
        System.out.println(reportContent);
        Assert.assertTrue(reportContent.contains("sharedlogposts"));
        Assert.assertTrue(reportContent.contains("TestCase1"));
        Assert.assertTrue(reportContent.contains("TestCase2"));
    }

    @Test
    public void bothKnownNewAndSharedNewErrors(){
        //Same test cases for unshared log records are not merged as expected
        HtmlSummaryReport htmlSummaryReport = new HtmlSummaryReport();
        TestClass testSet = new TestClass();
        testSet.addKnownError("Fixed error registered on test set", "String that is not encountered.");
        testSet.addKnownError("Encountered known error registered on test set", ".*Known error.*");
        TestCase testCase0 = new TestCase(testSet.knownErrorsList, "TestCase0");
        testCase0.log(LogLevel.DEBUG, "Test");
        testCase0.log(LogLevel.FRAMEWORK_ERROR, "Framework error");
        testCase0.log(LogLevel.DEBUG, "Test");
        testCase0.testCaseResult.evaluateResultStatus();
        htmlSummaryReport.evaluateTestCase(testCase0);
        TestCase testCase1 = new TestCase(null, "TestCase1");
        //LogPost(LogLevel logLevel, String message, String htmlMessage, String testCaseName, String testStepName, String testStepClassName){
        testCase1.testCaseResult.testCaseLog.logPosts.add(new LogPost(LogLevel.EXECUTION_PROBLEM, "Problem", "<p>Problem</p>", "TestCase1", "Step1", "TestClass"));
        testCase1.testCaseResult.evaluateResultStatus();
        htmlSummaryReport.evaluateTestCase(testCase1);
        TestCase testCase2 = new TestCase(null, "TestCase2");
        testCase2.testCaseResult.testCaseLog.logPosts.add(new LogPost(LogLevel.EXECUTION_PROBLEM, "Problem", "<p>Problem</p>", "TestCase2", "Step1", "TestClass"));
        testCase2.log(LogLevel.EXECUTION_PROBLEM, "Shared log post between test case 2 and test case 3");
        testCase2.testCaseResult.evaluateResultStatus();
        htmlSummaryReport.evaluateTestCase(testCase2);
        TestCase testCase3 = new TestCase(null, "TestCase3");
        testCase3.log(LogLevel.INFO, "Text");
        testCase3.log(LogLevel.EXECUTION_PROBLEM, "Shared log post between test case 2 and test case 3");
        //Framework error is the most severe type of error
        testCase3.log(LogLevel.VERIFICATION_FAILED, "SharedLogRow '234' found. Duration 123 milliseconds.");
        testCase3.log(LogLevel.VERIFICATION_FAILED, "UnSharedLogRow");
        testCase3.log(LogLevel.EXECUTION_PROBLEM, "SharedLogRow 'xyz'. Component bac. Time 543 ms.");
        testCase3.log(LogLevel.FRAMEWORK_ERROR, "Really really long error message. Really really long error message. Really really long error message. Really really long error message. ");
        testCase3.testCaseResult.evaluateResultStatus();
        htmlSummaryReport.evaluateTestCase(testCase3);
        TestCase testCase4 = new TestCase(null, "TestCase4");
        testCase4.log(LogLevel.VERIFICATION_FAILED, "SharedLogRow '354' found. Duration 7643 milliseconds.");
        testCase4.log(LogLevel.EXECUTION_PROBLEM, "SharedLogRow 'xyz'. Component bac. Time 543 ms.");
        testCase4.log(LogLevel.FRAMEWORK_ERROR, "Really really long error message. Really really long error message. Really really long error message. Really really long error message. ");
        testCase4.testCaseResult.evaluateResultStatus();
        htmlSummaryReport.evaluateTestCase(testCase4);
        TestCase testCase5 = new TestCase(null, "TestCase5");
        testCase5.log(LogLevel.VERIFICATION_PASSED, "Yes. Did good.");
        testCase5.log(LogLevel.VERIFICATION_PASSED, "SharedLogRow '234' found. Duration 123 milliseconds.");
        testCase5.testCaseResult.evaluateResultStatus();
        htmlSummaryReport.evaluateTestCase(testCase5);
        TestCase testCase6 = new TestCase(testSet.knownErrorsList, "TestCase6");
        testCase6.log(LogLevel.VERIFICATION_FAILED, "Known error");
        testCase6.testCaseResult.evaluateResultStatus();
        htmlSummaryReport.evaluateTestCase(testCase6);
        TestCase testCase7 = new TestCase(null, "TestCase7");
        testCase7.addKnownError("Known error registered on test case 7", ".*Known error2.*");
        testCase7.log(LogLevel.VERIFICATION_FAILED, "Known error2");
        testCase7.testCaseResult.evaluateResultStatus();
        htmlSummaryReport.evaluateTestCase(testCase7);
        TestCase testCase8 = new TestCase(null, "TestCase8");
        testCase8.log(LogLevel.VERIFICATION_PASSED, "Passed");
        testCase8.log(LogLevel.EXECUTED, "Yay!");
        testCase8.log(LogLevel.VERIFICATION_FAILED, "Nooooooooo1....");
        pause(20);
        testCase8.log(LogLevel.VERIFICATION_FAILED, "Nooooooooo2....");
        pause(20);
        testCase8.log(LogLevel.VERIFICATION_FAILED, "Nooooooooo3....");
        pause(20);
        testCase8.log(LogLevel.VERIFICATION_FAILED, "Nooooooooo4....");
        pause(20);
        testCase8.log(LogLevel.VERIFICATION_FAILED, "Nooooooooo5....");
        pause(20);
        testCase8.log(LogLevel.VERIFICATION_FAILED, "Nooooooooo6....");
        pause(20);
        testCase8.log(LogLevel.VERIFICATION_FAILED, "Nooooooooo7....");
        pause(20);
        testCase8.log(LogLevel.VERIFICATION_FAILED, "Nooooooooo8....");
        pause(20);
        testCase8.testCaseResult.evaluateResultStatus();
        htmlSummaryReport.evaluateTestCase(testCase8);
        htmlSummaryReport.evaluateTestSet(testSet);
        String output = htmlSummaryReport.createReport();
        System.out.println(output);
        Assert.assertTrue(output.contains("Similar log records found in multiple test cases"));
        Assert.assertTrue(output.contains("<span class=\"errorloglevel\">Execution problem</span></td><td><span class=\"logmessage\">Problem</span>"));
        Assert.assertTrue(output.contains("...(6 more problem log posts)..."));
        String regexp = ".*Nooooooooo1.*Nooooooooo8.*";
        Assert.assertTrue(SupportMethods.isRegexMatch(output, regexp));
    }

    @SuppressWarnings("SameParameterValue")
    private void pause(int milliseconds){
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static class TestClass extends TestSet{

        @Test
        public void dummyTest(){}
    }
}
