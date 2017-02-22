package se.claremont.autotest.common.reporting.testrunreports;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import se.claremont.autotest.common.logging.KnownErrorsList;
import se.claremont.autotest.common.logging.LogLevel;
import se.claremont.autotest.common.testcase.TestCase;
import se.claremont.autotest.common.testset.TestSet;

/**
 * Tests for the SummaryReport class
 *
 * Created by jordam on 2016-08-31.
 */
public class SummaryReport_Tests {
    @SuppressWarnings("CanBeFinal")
    @Rule public TestName currentTestName = new TestName();

    @Test
    public void successfulTest(){
        SummaryReport summaryReport = new SummaryReport();
        TestCase testCase = new TestCase(new KnownErrorsList(), currentTestName.getMethodName());
        testCase.log(LogLevel.DEBUG, "Test");
        testCase.log(LogLevel.EXECUTED, "Test");
        testCase.log(LogLevel.DEBUG, "Test");
        testCase.evaluateResultStatus();
        summaryReport.evaluateTestCase(testCase);
        Assert.assertTrue(summaryReport.successfulTestCases == 1);
        Assert.assertTrue(summaryReport.failedTestCasesWithNewDeviations == 0);
        Assert.assertTrue(summaryReport.unevaluatedCount == 0);
        Assert.assertTrue(summaryReport.testCasesWithBothNewAndKnownErrors == 0);
        Assert.assertTrue(summaryReport.testCasesWithOnlyKnownErrors == 0);
        Assert.assertTrue(summaryReport.encounteredKnownErrorInfos.size() == 0);
        Assert.assertTrue(summaryReport.newErrorInfos.size() == 0);
        Assert.assertTrue(summaryReport.solvedKnownErrorsList.size() == 0);
    }

    @Test
    public void newErrorTest(){
        SummaryReport summaryReport = new SummaryReport();
        TestCase testCase = new TestCase(new KnownErrorsList(), currentTestName.getMethodName());
        testCase.log(LogLevel.DEBUG, "Test");
        testCase.log(LogLevel.EXECUTION_PROBLEM, "Execution problem");
        testCase.log(LogLevel.DEBUG, "Test");
        testCase.evaluateResultStatus();
        summaryReport.evaluateTestCase(testCase);
        Assert.assertTrue(summaryReport.successfulTestCases == 0);
        Assert.assertTrue(summaryReport.failedTestCasesWithNewDeviations == 1);
        Assert.assertTrue(summaryReport.unevaluatedCount == 0);
        Assert.assertTrue(summaryReport.testCasesWithBothNewAndKnownErrors == 0);
        Assert.assertTrue(summaryReport.testCasesWithOnlyKnownErrors == 0);
        Assert.assertTrue(summaryReport.encounteredKnownErrorInfos.size() == 0);
        Assert.assertTrue(summaryReport.newErrorInfos.size() == 1);
        Assert.assertTrue(summaryReport.solvedKnownErrorsList.size() == 0);
    }

    @Test
    public void knownErrorTest(){
        SummaryReport summaryReport = new SummaryReport();
        TestCase testCase = new TestCase(new KnownErrorsList(), currentTestName.getMethodName());
        testCase.addKnownError("Failed verification is being tested", ".*Failed verification.*");
        testCase.log(LogLevel.DEBUG, "Test");
        testCase.log(LogLevel.VERIFICATION_FAILED, "Failed verification");
        testCase.log(LogLevel.DEBUG, "Test");
        testCase.evaluateResultStatus();
        summaryReport.evaluateTestCase(testCase);
        Assert.assertTrue(summaryReport.successfulTestCases == 0);
        Assert.assertTrue(summaryReport.failedTestCasesWithNewDeviations == 0);
        Assert.assertTrue(summaryReport.unevaluatedCount == 0);
        Assert.assertTrue(summaryReport.testCasesWithBothNewAndKnownErrors == 0);
        Assert.assertTrue(summaryReport.testCasesWithOnlyKnownErrors == 1);
        Assert.assertTrue(summaryReport.encounteredKnownErrorInfos.size() == 1);
        Assert.assertTrue(summaryReport.newErrorInfos.size() == 0);
        Assert.assertTrue(summaryReport.solvedKnownErrorsList.size() == 0);
    }

    @Test
    public void solvedErrorsTest(){
        SummaryReport summaryReport = new SummaryReport();
        TestCase testCase = new TestCase(new KnownErrorsList(), currentTestName.getMethodName());
        testCase.log(LogLevel.DEBUG, "Test");
        testCase.addKnownError("Fixed error", "String that is not encountered.");
        testCase.log(LogLevel.EXECUTED, "Framework error");
        testCase.log(LogLevel.DEBUG, "Test");
        testCase.evaluateResultStatus();
        summaryReport.evaluateTestCase(testCase);
        Assert.assertTrue(summaryReport.successfulTestCases == 1);
        Assert.assertTrue(summaryReport.failedTestCasesWithNewDeviations == 0);
        Assert.assertTrue(summaryReport.unevaluatedCount == 0);
        Assert.assertTrue(summaryReport.testCasesWithBothNewAndKnownErrors == 0);
        Assert.assertTrue(summaryReport.testCasesWithOnlyKnownErrors == 0);
        Assert.assertTrue(summaryReport.encounteredKnownErrorInfos.size() == 0);
        Assert.assertTrue(summaryReport.newErrorInfos.size() == 0);
        Assert.assertTrue(summaryReport.solvedKnownErrorsList.size() == 1);
    }

    @Test
    public void bothNewAndSolvedErrorsTest(){
        SummaryReport summaryReport = new SummaryReport();
        TestCase testCase = new TestCase(new KnownErrorsList(), currentTestName.getMethodName());
        testCase.log(LogLevel.DEBUG, "Test");
        testCase.addKnownError("Fixed error", "String that is not encountered.");
        testCase.log(LogLevel.FRAMEWORK_ERROR, "Framework error");
        testCase.log(LogLevel.DEBUG, "Test");
        testCase.evaluateResultStatus();
        summaryReport.evaluateTestCase(testCase);
        Assert.assertTrue(summaryReport.successfulTestCases == 0);
        Assert.assertTrue("New deviations count was expected to be 1 but was " + summaryReport.failedTestCasesWithNewDeviations + ".", summaryReport.failedTestCasesWithNewDeviations == 1);
        Assert.assertTrue("Unevaluated count was expected to be 0 but was " + summaryReport.unevaluatedCount + ".", summaryReport.unevaluatedCount == 0);
        Assert.assertTrue("Test cases with both new and known bugs count was expected to be 0 but was " + summaryReport.testCasesWithBothNewAndKnownErrors + ".", summaryReport.testCasesWithBothNewAndKnownErrors == 0);
        Assert.assertTrue("Test cases count for test cases with only known errors was expected to be 0 but was " + summaryReport.testCasesWithOnlyKnownErrors + ".", summaryReport.testCasesWithOnlyKnownErrors == 0);
        Assert.assertTrue(summaryReport.encounteredKnownErrorInfos.size() == 0);
        Assert.assertTrue(summaryReport.newErrorInfos.size() == 1);
        Assert.assertTrue(summaryReport.solvedKnownErrorsList.size() == 1);
    }

    @Test
    public void knownTestSetErrorTest(){
        SummaryReport summaryReport = new SummaryReport();
        TestClass testSet = new TestClass();
        testSet.addKnownError("Failed verification is being tested", ".*Failed verification.*");
        TestCase testCase = new TestCase(testSet.knownErrorsList, currentTestName.getMethodName());
        testCase.log(LogLevel.DEBUG, "Test");
        testCase.log(LogLevel.VERIFICATION_FAILED, "Failed verification");
        testCase.log(LogLevel.DEBUG, "Test");
        testCase.evaluateResultStatus();
        summaryReport.evaluateTestCase(testCase);
        summaryReport.evaluateTestSet(testSet);
        Assert.assertTrue(summaryReport.successfulTestCases == 0);
        Assert.assertTrue(summaryReport.failedTestCasesWithNewDeviations == 0);
        Assert.assertTrue(summaryReport.unevaluatedCount == 0);
        Assert.assertTrue(summaryReport.testCasesWithBothNewAndKnownErrors == 0);
        Assert.assertTrue(summaryReport.testCasesWithOnlyKnownErrors == 1);
        Assert.assertTrue(summaryReport.encounteredKnownErrorInfos.size() == 1);
        Assert.assertTrue(summaryReport.newErrorInfos.size() == 0);
        Assert.assertTrue(summaryReport.solvedKnownErrorsList.size() == 0);
    }

    @Test
    public void solvedTestSetRegisteredKnownErrorsTest(){
        SummaryReport summaryReport = new SummaryReport();
        TestClass testSet = new TestClass();
        testSet.addKnownError("Fixed error", "String that is not encountered.");
        TestCase testCase = new TestCase(testSet.knownErrorsList, currentTestName.getMethodName());
        testCase.log(LogLevel.DEBUG, "Test");
        testCase.log(LogLevel.EXECUTED, "Framework error");
        testCase.log(LogLevel.DEBUG, "Test");
        testCase.evaluateResultStatus();
        summaryReport.evaluateTestCase(testCase);
        summaryReport.evaluateTestSet(testSet);
        Assert.assertTrue(summaryReport.successfulTestCases == 1);
        Assert.assertTrue(summaryReport.failedTestCasesWithNewDeviations == 0);
        Assert.assertTrue(summaryReport.unevaluatedCount == 0);
        Assert.assertTrue(summaryReport.testCasesWithBothNewAndKnownErrors == 0);
        Assert.assertTrue(summaryReport.testCasesWithOnlyKnownErrors == 0);
        Assert.assertTrue(summaryReport.encounteredKnownErrorInfos.size() == 0);
        Assert.assertTrue(summaryReport.newErrorInfos.size() == 0);
        Assert.assertTrue(summaryReport.solvedKnownErrorsList.size() == 1);
    }

    @Test
    public void bothNewErrorAndSolvedTestSetKnownErrorTest(){
        SummaryReport summaryReport = new SummaryReport();
        TestClass testSet = new TestClass();
        testSet.addKnownError("Fixed error", "String that is not encountered.");
        TestCase testCase = new TestCase(testSet.knownErrorsList, currentTestName.getMethodName());
        testCase.log(LogLevel.DEBUG, "Test");
        testCase.log(LogLevel.FRAMEWORK_ERROR, "Framework error");
        testCase.log(LogLevel.DEBUG, "Test");
        testCase.evaluateResultStatus();
        summaryReport.evaluateTestCase(testCase);
        summaryReport.evaluateTestSet(testSet);
        Assert.assertTrue(summaryReport.successfulTestCases == 0);
        Assert.assertTrue("New deviations count was expected to be 1 but was " + summaryReport.failedTestCasesWithNewDeviations + ".", summaryReport.failedTestCasesWithNewDeviations == 1);
        Assert.assertTrue("Unevaluated count was expected to be 0 but was " + summaryReport.unevaluatedCount + ".", summaryReport.unevaluatedCount == 0);
        Assert.assertTrue("Test cases with both new and known bugs count was expected to be 0 but was " + summaryReport.testCasesWithBothNewAndKnownErrors + ".", summaryReport.testCasesWithBothNewAndKnownErrors == 0);
        Assert.assertTrue("Test cases count for test cases with only known errors was expected to be 0 but was " + summaryReport.testCasesWithOnlyKnownErrors + ".", summaryReport.testCasesWithOnlyKnownErrors == 0);
        Assert.assertTrue(summaryReport.encounteredKnownErrorInfos.size() == 0);
        Assert.assertTrue(summaryReport.newErrorInfos.size() == 1);
        Assert.assertTrue(summaryReport.solvedKnownErrorsList.size() == 1);
    }

    class TestClass extends TestSet{}
}
