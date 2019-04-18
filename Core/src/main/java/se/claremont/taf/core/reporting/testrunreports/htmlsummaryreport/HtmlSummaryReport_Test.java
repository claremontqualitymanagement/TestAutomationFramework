package se.claremont.taf.core.reporting.testrunreports.htmlsummaryreport;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import se.claremont.taf.core.logging.KnownErrorsList;
import se.claremont.taf.core.logging.LogLevel;
import se.claremont.taf.core.logging.LogPost;
import se.claremont.taf.core.testcase.TestCase;
import se.claremont.taf.core.testset.TestSet;
import se.claremont.taf.core.testset.UnitTestClass;

/**
 * Tests for the HtmlSummaryReport class
 *
 * Created by jordam on 2016-08-31.
 */
public class HtmlSummaryReport_Test extends UnitTestClass {
    @SuppressWarnings("CanBeFinal")
    @Rule public TestName currentTestName = new TestName();

    @Test
    public void successfulTest(){
        HtmlSummaryReport htmlSummaryReport = new HtmlSummaryReport();
        TestCase testCase = new TestCase(new KnownErrorsList(), currentTestName.getMethodName());
        testCase.log(LogLevel.DEBUG, "Test");
        testCase.log(LogLevel.EXECUTED, "Test");
        testCase.log(LogLevel.DEBUG, "Test");
        testCase.testCaseResult.evaluateResultStatus();
        htmlSummaryReport.evaluateTestCase(testCase);
        Assert.assertTrue(htmlSummaryReport.successfulTestCases == 1);
        Assert.assertTrue(htmlSummaryReport.failedTestCasesWithNewDeviations == 0);
        Assert.assertTrue(htmlSummaryReport.unevaluatedCount == 0);
        Assert.assertTrue(htmlSummaryReport.testCasesWithBothNewAndKnownErrors == 0);
        Assert.assertTrue(htmlSummaryReport.testCasesWithOnlyKnownErrors == 0);
        Assert.assertTrue(htmlSummaryReport.encounteredKnownErrorInfos.size() == 0);
        Assert.assertTrue(htmlSummaryReport.newErrorInfos.size() == 0);
        Assert.assertTrue(htmlSummaryReport.solvedKnownErrorsList.size() == 0);
    }

    @Test
    public void newErrorTest(){
        HtmlSummaryReport htmlSummaryReport = new HtmlSummaryReport();
        TestCase testCase = new TestCase(new KnownErrorsList(), currentTestName.getMethodName());
        testCase.log(LogLevel.DEBUG, "Test");
        testCase.log(LogLevel.EXECUTION_PROBLEM, "Execution problem");
        testCase.log(LogLevel.DEBUG, "Test");
        testCase.testCaseResult.evaluateResultStatus();
        htmlSummaryReport.evaluateTestCase(testCase);
        Assert.assertTrue(htmlSummaryReport.successfulTestCases == 0);
        Assert.assertTrue(htmlSummaryReport.failedTestCasesWithNewDeviations == 1);
        Assert.assertTrue(htmlSummaryReport.unevaluatedCount == 0);
        Assert.assertTrue(htmlSummaryReport.testCasesWithBothNewAndKnownErrors == 0);
        Assert.assertTrue(htmlSummaryReport.testCasesWithOnlyKnownErrors == 0);
        Assert.assertTrue(htmlSummaryReport.encounteredKnownErrorInfos.size() == 0);
        Assert.assertTrue(htmlSummaryReport.newErrorInfos.size() == 1);
        Assert.assertTrue(htmlSummaryReport.solvedKnownErrorsList.size() == 0);
    }

    @Test
    public void knownErrorTest(){
        HtmlSummaryReport htmlSummaryReport = new HtmlSummaryReport();
        TestCase testCase = new TestCase(new KnownErrorsList(), currentTestName.getMethodName());
        testCase.addKnownError("Failed verification is being tested", ".*Failed verification.*");
        testCase.log(LogLevel.DEBUG, "Test");
        testCase.log(LogLevel.VERIFICATION_FAILED, "Failed verification");
        testCase.log(LogLevel.DEBUG, "Test");
        testCase.testCaseResult.evaluateResultStatus();
        htmlSummaryReport.evaluateTestCase(testCase);
        Assert.assertTrue(htmlSummaryReport.successfulTestCases == 0);
        Assert.assertTrue(htmlSummaryReport.failedTestCasesWithNewDeviations == 0);
        Assert.assertTrue(htmlSummaryReport.unevaluatedCount == 0);
        Assert.assertTrue(htmlSummaryReport.testCasesWithBothNewAndKnownErrors == 0);
        Assert.assertTrue(htmlSummaryReport.testCasesWithOnlyKnownErrors == 1);
        Assert.assertTrue(htmlSummaryReport.encounteredKnownErrorInfos.size() == 1);
        Assert.assertTrue(htmlSummaryReport.newErrorInfos.size() == 0);
        Assert.assertTrue(htmlSummaryReport.solvedKnownErrorsList.size() == 0);
    }

    @Test
    public void solvedErrorsTest(){
        HtmlSummaryReport htmlSummaryReport = new HtmlSummaryReport();
        TestCase testCase = new TestCase(new KnownErrorsList(), currentTestName.getMethodName());
        testCase.log(LogLevel.DEBUG, "Test");
        testCase.addKnownError("Fixed error", "String that is not encountered.");
        testCase.log(LogLevel.EXECUTED, "Framework error");
        testCase.log(LogLevel.DEBUG, "Test");
        testCase.testCaseResult.evaluateResultStatus();
        htmlSummaryReport.evaluateTestCase(testCase);
        Assert.assertTrue(htmlSummaryReport.successfulTestCases == 1);
        Assert.assertTrue(htmlSummaryReport.failedTestCasesWithNewDeviations == 0);
        Assert.assertTrue(htmlSummaryReport.unevaluatedCount == 0);
        Assert.assertTrue(htmlSummaryReport.testCasesWithBothNewAndKnownErrors == 0);
        Assert.assertTrue(htmlSummaryReport.testCasesWithOnlyKnownErrors == 0);
        Assert.assertTrue(htmlSummaryReport.encounteredKnownErrorInfos.size() == 0);
        Assert.assertTrue(htmlSummaryReport.newErrorInfos.size() == 0);
        Assert.assertTrue(htmlSummaryReport.solvedKnownErrorsList.size() == 1);
    }

    @Test
    public void bothNewAndSolvedErrorsTest(){
        HtmlSummaryReport htmlSummaryReport = new HtmlSummaryReport();
        TestCase testCase = new TestCase(new KnownErrorsList(), currentTestName.getMethodName());
        testCase.log(LogLevel.DEBUG, "Test");
        testCase.addKnownError("Fixed error", "String that is not encountered.");
        testCase.log(LogLevel.FRAMEWORK_ERROR, "Framework error");
        testCase.log(LogLevel.DEBUG, "Test");
        testCase.testCaseResult.evaluateResultStatus();
        htmlSummaryReport.evaluateTestCase(testCase);
        Assert.assertTrue(htmlSummaryReport.successfulTestCases == 0);
        Assert.assertTrue("New deviations count was expected to be 1 but was " + htmlSummaryReport.failedTestCasesWithNewDeviations + ".", htmlSummaryReport.failedTestCasesWithNewDeviations == 1);
        Assert.assertTrue("Unevaluated count was expected to be 0 but was " + htmlSummaryReport.unevaluatedCount + ".", htmlSummaryReport.unevaluatedCount == 0);
        Assert.assertTrue("Test cases with both new and known bugs count was expected to be 0 but was " + htmlSummaryReport.testCasesWithBothNewAndKnownErrors + ".", htmlSummaryReport.testCasesWithBothNewAndKnownErrors == 0);
        Assert.assertTrue("Test cases count for test cases with only known errors was expected to be 0 but was " + htmlSummaryReport.testCasesWithOnlyKnownErrors + ".", htmlSummaryReport.testCasesWithOnlyKnownErrors == 0);
        Assert.assertTrue(htmlSummaryReport.encounteredKnownErrorInfos.size() == 0);
        Assert.assertTrue(htmlSummaryReport.newErrorInfos.size() == 1);
        Assert.assertTrue(htmlSummaryReport.solvedKnownErrorsList.size() == 1);
    }

    @Test
    public void knownTestSetErrorTest(){
        HtmlSummaryReport htmlSummaryReport = new HtmlSummaryReport();
        TestClass testSet = new TestClass();
        testSet.addKnownError("Failed verification is being tested", ".*Failed verification.*");
        TestCase testCase = new TestCase(testSet.knownErrorsList, currentTestName.getMethodName());
        testCase.log(LogLevel.DEBUG, "Test");
        testCase.log(LogLevel.VERIFICATION_FAILED, "Failed verification");
        testCase.log(LogLevel.DEBUG, "Test");
        testCase.testCaseResult.evaluateResultStatus();
        htmlSummaryReport.evaluateTestCase(testCase);
        htmlSummaryReport.evaluateTestSet(testSet);
        Assert.assertTrue(htmlSummaryReport.successfulTestCases == 0);
        Assert.assertTrue(htmlSummaryReport.failedTestCasesWithNewDeviations == 0);
        Assert.assertTrue(htmlSummaryReport.unevaluatedCount == 0);
        Assert.assertTrue(htmlSummaryReport.testCasesWithBothNewAndKnownErrors == 0);
        Assert.assertTrue(htmlSummaryReport.testCasesWithOnlyKnownErrors == 1);
        Assert.assertTrue(htmlSummaryReport.encounteredKnownErrorInfos.size() == 1);
        Assert.assertTrue(htmlSummaryReport.newErrorInfos.size() == 0);
        Assert.assertTrue(htmlSummaryReport.solvedKnownErrorsList.size() == 0);
    }

    @Test
    public void solvedTestSetRegisteredKnownErrorsTest(){
        HtmlSummaryReport htmlSummaryReport = new HtmlSummaryReport();
        TestClass testSet = new TestClass();
        testSet.addKnownError("Fixed error", "String that is not encountered.");
        TestCase testCase = new TestCase(testSet.knownErrorsList, currentTestName.getMethodName());
        testCase.log(LogLevel.DEBUG, "Test");
        testCase.log(LogLevel.EXECUTED, "Framework error");
        testCase.log(LogLevel.DEBUG, "Test");
        testCase.testCaseResult.evaluateResultStatus();
        htmlSummaryReport.evaluateTestCase(testCase);
        htmlSummaryReport.evaluateTestSet(testSet);
        Assert.assertTrue(htmlSummaryReport.successfulTestCases == 1);
        Assert.assertTrue(htmlSummaryReport.failedTestCasesWithNewDeviations == 0);
        Assert.assertTrue(htmlSummaryReport.unevaluatedCount == 0);
        Assert.assertTrue(htmlSummaryReport.testCasesWithBothNewAndKnownErrors == 0);
        Assert.assertTrue(htmlSummaryReport.testCasesWithOnlyKnownErrors == 0);
        Assert.assertTrue(htmlSummaryReport.encounteredKnownErrorInfos.size() == 0);
        Assert.assertTrue(htmlSummaryReport.newErrorInfos.size() == 0);
        Assert.assertTrue(htmlSummaryReport.solvedKnownErrorsList.size() == 1);
    }

    @Test
    public void bothNewErrorAndSolvedTestSetKnownErrorTest(){
        HtmlSummaryReport htmlSummaryReport = new HtmlSummaryReport();
        TestClass testSet = new TestClass();
        testSet.addKnownError("Fixed error", "String that is not encountered.");
        TestCase testCase = new TestCase(testSet.knownErrorsList, currentTestName.getMethodName());
        testCase.log(LogLevel.DEBUG, "Test");
        testCase.log(LogLevel.FRAMEWORK_ERROR, "Framework error");
        testCase.log(LogLevel.DEBUG, "Test");
        testCase.testCaseResult.evaluateResultStatus();
        htmlSummaryReport.evaluateTestCase(testCase);
        htmlSummaryReport.evaluateTestSet(testSet);
        Assert.assertTrue(htmlSummaryReport.successfulTestCases == 0);
        Assert.assertTrue("New deviations count was expected to be 1 but was " + htmlSummaryReport.failedTestCasesWithNewDeviations + ".", htmlSummaryReport.failedTestCasesWithNewDeviations == 1);
        Assert.assertTrue("Unevaluated count was expected to be 0 but was " + htmlSummaryReport.unevaluatedCount + ".", htmlSummaryReport.unevaluatedCount == 0);
        Assert.assertTrue("Test cases with both new and known bugs count was expected to be 0 but was " + htmlSummaryReport.testCasesWithBothNewAndKnownErrors + ".", htmlSummaryReport.testCasesWithBothNewAndKnownErrors == 0);
        Assert.assertTrue("Test cases count for test cases with only known errors was expected to be 0 but was " + htmlSummaryReport.testCasesWithOnlyKnownErrors + ".", htmlSummaryReport.testCasesWithOnlyKnownErrors == 0);
        Assert.assertTrue(htmlSummaryReport.encounteredKnownErrorInfos.size() == 0);
        Assert.assertTrue(htmlSummaryReport.newErrorInfos.size() == 1);
        Assert.assertTrue(htmlSummaryReport.solvedKnownErrorsList.size() == 1);
    }

    @Test
    public void classPrintTest(){
        HtmlSummaryReport htmlSummaryReport = new HtmlSummaryReport();
        TestCase testCase1 = new TestCase(null, "TestCase1");
        //LogPost(LogLevel logLevel, String message, String htmlMessage, String testCaseName, String testStepName, String testStepClassName){
        testCase1.testCaseResult.testCaseLog.logPosts.add(new LogPost(LogLevel.EXECUTION_PROBLEM, "Problem", "<p>Problem</p>", "TestCase1", "Step1", "TestClass"));
        testCase1.testCaseResult.evaluateResultStatus();
        htmlSummaryReport.evaluateTestCase(testCase1);
        TestCase testCase2 = new TestCase(null, "TestCase2");
        testCase2.testCaseResult.testCaseLog.logPosts.add(new LogPost(LogLevel.EXECUTION_PROBLEM, "Problem", "<p>Problem</p>", "TestCase2", "Step1", "TestClass"));
        testCase2.testCaseResult.evaluateResultStatus();
        htmlSummaryReport.evaluateTestCase(testCase2);
        String output = htmlSummaryReport.createReport();
        Assert.assertTrue(output, output.contains("All errors were found in test step"));
    }

    public static class TestClass extends TestSet{

        @SuppressWarnings("EmptyMethod")
        @Test
        public void dummyTest(){}
    }

}
