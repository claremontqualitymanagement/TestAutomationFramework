package se.claremont.taf.core.testrun;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;
import se.claremont.taf.core.reporting.testrunreports.TestRunReporterHtmlSummaryReportFile;
import se.claremont.taf.core.testset.UnitTestClass;

/**
 * Tests that correct actions are performed on a test case after it has been performed
 *
 * Created by jordam on 2017-03-17.
 */
public class TestRunWrapupTest extends UnitTestClass{

    @Test
    public void singleTestRunReportingFromMultipleTestSetExecutions(){
        FakeTestRunReporter fakeEmailTestRunReporter = new FakeTestRunReporter();
        TestRun.getReporterFactory().reporters.clear();
        TestRun.addTestRunReporterIfNotAlreadyRegistered(fakeEmailTestRunReporter);
        String[] args = {"runname=HappyTest", "PARALLEL_TEST_EXECUTION_MODE=1", TestSet1.class.getName(), TestSet2.class.getName() };
        CliTestRunner.runInTestMode(args);
        for(String testCase : fakeEmailTestRunReporter.testCaseNames){
            System.out.println(testCase);
        }
        Assert.assertTrue(String.valueOf
                (fakeEmailTestRunReporter.testCaseNames.size()) + System.lineSeparator() + fakeEmailTestRunReporter.toString(), fakeEmailTestRunReporter.testCaseNames.size() == 4);
        //Assert.assertTrue("Expected 2 test sets in testSetList. Was " +fakeEmailTestRunReporter.testSetNames.size() + System.lineSeparator() + fakeEmailTestRunReporter.toString(), fakeEmailTestRunReporter.testSetNames.size() == 2);
        Assert.assertTrue("Expecting one report. Was " + fakeEmailTestRunReporter.numberOfReportsPerformed + "." + System.lineSeparator() + fakeEmailTestRunReporter.toString(), fakeEmailTestRunReporter.numberOfReportsPerformed == 1);
    }

    @Test
    public void singleTestRunReportingFromMultipleTestSetExecutionsParallelExecutionByThreads(){
        FakeTestRunReporter fakeEmailTestRunReporter = new FakeTestRunReporter();
        TestRun.getReporterFactory().reporters.clear();
        TestRun.addTestRunReporterIfNotAlreadyRegistered(fakeEmailTestRunReporter);
        String[] args = {"runname=HappyTest", "PARALLEL_TEST_EXECUTION_MODE=2", TestSet1.class.getName(), TestSet2.class.getName() };
        CliTestRunner.runInTestMode(args);
        for(String testCase : fakeEmailTestRunReporter.testCaseNames){
            System.out.println(testCase);
        }
        Assert.assertTrue(String.valueOf
                (fakeEmailTestRunReporter.testCaseNames.size()), fakeEmailTestRunReporter.testCaseNames.size() == 4);
        //Assert.assertTrue("Expected 2 test sets in testSetList. Was " +fakeEmailTestRunReporter.testSetNames.size(), fakeEmailTestRunReporter.testSetNames.size() == 2);
        Assert.assertTrue("Expecting one report. Was " + fakeEmailTestRunReporter.numberOfReportsPerformed + ".", fakeEmailTestRunReporter.numberOfReportsPerformed == 1);
    }

    @Test
    public void singleTestRunReportingFromMultipleTestSetExecutionsParallelExecutionByClasses() throws IllegalAccessException, InstantiationException {
        TestRun.getReporterFactory().reporters.clear();
        FakeTestRunReporter fakeTestRunReporter = new FakeTestRunReporter();
        TestRun.addTestRunReporterIfNotAlreadyRegistered(fakeTestRunReporter);
//        TestRun.reporters.addTestRunReporterIfNotAlreadyRegistered(new TestRunReporterHtmlSummaryReportFile());
        try{
            String[] args = {"runname=HappyTest", TestSet1.class.getName(), TestSet2.class.getName() };
            CliTestRunner.runInTestMode(args);
            Assert.assertTrue("Expected 4 test cases evaluated. Was: " + String.valueOf
                    (fakeTestRunReporter.testCaseNames.size()) + ", namely '" + String.join("', '", fakeTestRunReporter.testCaseNames) + "'.", fakeTestRunReporter.numberOfTestCaseEvaluationsPerformed == 4);
            Assert.assertTrue("Expected 4 test cases evaluated. Was: " + String.valueOf
                    (fakeTestRunReporter.testCaseNames.size()) + ", namely '" + String.join("', '", fakeTestRunReporter.testCaseNames) + "'.", fakeTestRunReporter.testCaseNames.size() == 4);
            //Assert.assertTrue("Expected 2 test sets in testSetList. Was " +fakeTestRunReporter.testSetNames.size() + ". '" + String.join("', '", testSets) + "'." , fakeTestRunReporter.testSetNames.size() == 2);
            Assert.assertTrue("Expecting one report. Was " + fakeTestRunReporter.numberOfReportsPerformed + ".", fakeTestRunReporter.numberOfReportsPerformed == 1);
        }finally {
            TestRun.getReporterFactory().reporters.clear();
        }
    }

    @Test
    //Test case used to check that exactly four test cases were reported in the _summary.html report file
    public void summaryReportShouldHaveExactlyOneInstanceOfEachTest(){
        TestRunReporterHtmlSummaryReportFile testRunReporterHtmlSummaryReportFile = new TestRunReporterHtmlSummaryReportFile();
        TestRun.getReporterFactory().reporters.clear();
        TestRun.addTestRunReporter(testRunReporterHtmlSummaryReportFile);
        String[] args = {"runname=HappyTest", TestSet1.class.getName(), TestSet2.class.getName() };
        try{
            CliTestRunner.runInTestMode(args);
        }catch (Exception e){
            Assume.assumeTrue("Could not save html summary report file.", false);
        }
        Assert.assertTrue("Expected four tests. Found " + testRunReporterHtmlSummaryReportFile.htmlSummaryReport.numberOfTestCases() + ". Report: "  + System.lineSeparator() +testRunReporterHtmlSummaryReportFile.reportContent() , testRunReporterHtmlSummaryReportFile.htmlSummaryReport.numberOfTestCases() == 4);
    }


}
