package se.claremont.autotest.common.testrun;

import org.junit.Assert;
import org.junit.Test;
import se.claremont.autotest.common.reporting.testrunreports.HtmlSummaryReport;
import se.claremont.autotest.common.reporting.testrunreports.TestRunReporterHtmlSummaryReportFile;
import se.claremont.autotest.common.testset.UnitTestClass;

/**
 * Tests that correct actions are performed on a test case after it has been performed
 *
 * Created by jordam on 2017-03-17.
 */
public class TestRunWrapupTest extends UnitTestClass{

    @Test
    public void singleTestRunReportingFromMultipleTestSetExecutions(){
        FakeEmailTestRunReporter fakeEmailTestRunReporter = new FakeEmailTestRunReporter();
        TestRun.initializeIfNotInitialized();
        TestRun.reporters.addTestRunReporterIfNotAlreadyRegistered(fakeEmailTestRunReporter);
        String[] args = {"runname=HappyTest", TestSet1.class.getName(), TestSet2.class.getName()};
        CliTestRunner.runInTestMode(args);
        Assert.assertTrue(fakeEmailTestRunReporter.testCaseList.size() == 4);
        Assert.assertTrue(fakeEmailTestRunReporter.testSetList.size() == 2);
        Assert.assertTrue("Expecting one report. Was " + fakeEmailTestRunReporter.reportingCounts + ".", fakeEmailTestRunReporter.reportingCounts == 1);
    }

    @Test
    //Test case used to check that exactly four test cases were reported in the _summary.html report file
    public void summaryReportShouldHaveExactlyOneInstanceOfEachTest(){
        TestRunReporterHtmlSummaryReportFile testRunReporterHtmlSummaryReportFile = new TestRunReporterHtmlSummaryReportFile();
        HtmlSummaryReport htmlSummaryReport = new HtmlSummaryReport();
        FakeEmailTestRunReporter fakeEmailTestRunReporter = new FakeEmailTestRunReporter();
        TestRun.initializeIfNotInitialized();
        TestRun.reporters.addTestRunReporter(testRunReporterHtmlSummaryReportFile);
        TestRun.reporters.addTestRunReporterIfNotAlreadyRegistered(fakeEmailTestRunReporter);
        String[] args = {"runname=HappyTest", TestSet1.class.getName(), TestSet2.class.getName()};
        try{
            CliTestRunner.runInTestMode(args);
        }catch (Exception e){
            System.out.println("Could not save html summary report file.");
        }
        Assert.assertTrue(testRunReporterHtmlSummaryReportFile.htmlSummaryReport.numberOfTestCases() == 4);
    }

}
