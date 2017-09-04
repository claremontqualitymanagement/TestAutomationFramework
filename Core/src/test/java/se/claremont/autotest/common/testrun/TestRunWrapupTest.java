package se.claremont.autotest.common.testrun;

import org.junit.Assert;
import org.junit.Test;
import se.claremont.autotest.common.reporting.testrunreports.TestRunReporterHtmlSummaryReportFile;
import se.claremont.autotest.common.reporting.testrunreports.htmlsummaryreport.HtmlSummaryReport;
import se.claremont.autotest.common.testcase.TestCase;
import se.claremont.autotest.common.testrun.reportingengine.TestRunReporterFactory;
import se.claremont.autotest.common.testset.TestSet;
import se.claremont.autotest.common.testset.UnitTestClass;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests that correct actions are performed on a test case after it has been performed
 *
 * Created by jordam on 2017-03-17.
 */
public class TestRunWrapupTest extends UnitTestClass{

    @Test
    public void singleTestRunReportingFromMultipleTestSetExecutions(){
        FakeTestRunReporter fakeEmailTestRunReporter = new FakeTestRunReporter();
        TestRun.initializeIfNotInitialized();
        TestRun.reporters.reporters.clear();
        TestRun.reporters.addTestRunReporterIfNotAlreadyRegistered(fakeEmailTestRunReporter);
        String[] args = {"runname=HappyTest", "PARALLEL_TEST_EXECUTION_MODE=1", TestSet1.class.getName(), TestSet2.class.getName()};
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
        TestRun.initializeIfNotInitialized();
        TestRun.reporters.reporters.clear();
        TestRun.reporters.addTestRunReporterIfNotAlreadyRegistered(fakeEmailTestRunReporter);
        String[] args = {"runname=HappyTest", "PARALLEL_TEST_EXECUTION_MODE=2", TestSet1.class.getName(), TestSet2.class.getName()};
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
    public void singleTestRunReportingFromMultipleTestSetExecutionsParallelExecutionByClasses(){
        FakeTestRunReporter fakeEmailTestRunReporter = new FakeTestRunReporter();
        TestRun.initializeIfNotInitialized();
        TestRun.reporters.reporters.clear();
        try{
            TestRun.reporters.addTestRunReporterIfNotAlreadyRegistered(fakeEmailTestRunReporter);
            TestRun.reporters.addTestRunReporterIfNotAlreadyRegistered(new TestRunReporterHtmlSummaryReportFile());
            String[] args = {"runname=HappyTest", "PARALLEL_TEST_EXECUTION_MODE=classes", TestSet1.class.getName(), TestSet2.class.getName()};
            CliTestRunner.runInTestMode(args);
            for(String testCase : fakeEmailTestRunReporter.testCaseNames){
                System.out.println(testCase);
            }
            for(String testSet : fakeEmailTestRunReporter.testSetNames){
                System.out.println(testSet);
            }
            Assert.assertTrue(String.valueOf
                    (fakeEmailTestRunReporter.testCaseNames.size()), fakeEmailTestRunReporter.testCaseNames.size() == 4);
            List<String> testSets = new ArrayList<>();
            for(String testSet : fakeEmailTestRunReporter.testSetNames){
                testSets.add(testSet);
            }
            //Assert.assertTrue("Expected 2 test sets in testSetList. Was " +fakeEmailTestRunReporter.testSetNames.size() + ". '" + String.join("', '", testSets) + "'." , fakeEmailTestRunReporter.testSetNames.size() == 2);
            Assert.assertTrue("Expecting one report. Was " + fakeEmailTestRunReporter.numberOfReportsPerformed + ".", fakeEmailTestRunReporter.numberOfReportsPerformed == 1);
        }finally {
            TestRun.reporters.reporters.clear();
        }
    }

    @Test
    //Test case used to check that exactly four test cases were reported in the _summary.html report file
    public void summaryReportShouldHaveExactlyOneInstanceOfEachTest(){
        TestRunReporterHtmlSummaryReportFile testRunReporterHtmlSummaryReportFile = new TestRunReporterHtmlSummaryReportFile();
        FakeTestRunReporter fakeEmailTestRunReporter = new FakeTestRunReporter();
        TestRun.initializeIfNotInitialized();
        TestRun.reporters.reporters.clear();
        TestRun.reporters.addTestRunReporter(testRunReporterHtmlSummaryReportFile);
        //TestRun.reporters.addTestRunReporterIfNotAlreadyRegistered(fakeEmailTestRunReporter);
        String[] args = {"runname=HappyTest", TestSet1.class.getName(), TestSet2.class.getName()};
        try{
            CliTestRunner.runInTestMode(args);
        }catch (Exception e){
            System.out.println("Could not save html summary report file.");
        }
        Assert.assertTrue(testRunReporterHtmlSummaryReportFile.htmlSummaryReport.numberOfTestCases() + "", testRunReporterHtmlSummaryReportFile.htmlSummaryReport.numberOfTestCases() == 4);
    }

}
