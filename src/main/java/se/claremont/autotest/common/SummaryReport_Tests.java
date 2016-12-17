package se.claremont.autotest.common;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

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
        Assert.assertEquals(1, summaryReport.successfulTestCases);
        Assert.assertEquals(0, summaryReport.failedTestCasesWithNewDeviations);
        Assert.assertEquals(0, summaryReport.unevaluatedCount);
        Assert.assertEquals(0, summaryReport.testCasesWithBothNewAndKnownErrors);
        Assert.assertEquals(0, summaryReport.testCasesWithOnlyKnownErrors);
        Assert.assertEquals(0, summaryReport.encounteredKnownErrorInfos.size());
        Assert.assertEquals(0, summaryReport.newErrorInfos.size());
        Assert.assertEquals(0, summaryReport.solvedKnownErrorsList.size());
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
        Assert.assertEquals(0, summaryReport.successfulTestCases);
        Assert.assertEquals(1, summaryReport.failedTestCasesWithNewDeviations);
        Assert.assertEquals(0, summaryReport.unevaluatedCount);
        Assert.assertEquals(0, summaryReport.testCasesWithBothNewAndKnownErrors);
        Assert.assertEquals(0, summaryReport.testCasesWithOnlyKnownErrors);
        Assert.assertEquals(0, summaryReport.encounteredKnownErrorInfos.size());
        Assert.assertEquals(1, summaryReport.newErrorInfos.size());
        Assert.assertEquals(0, summaryReport.solvedKnownErrorsList.size());
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
        Assert.assertEquals(0, summaryReport.successfulTestCases);
        Assert.assertEquals(0, summaryReport.failedTestCasesWithNewDeviations);
        Assert.assertEquals(0, summaryReport.unevaluatedCount);
        Assert.assertEquals(0, summaryReport.testCasesWithBothNewAndKnownErrors);
        Assert.assertEquals(1, summaryReport.testCasesWithOnlyKnownErrors);
        Assert.assertEquals(1, summaryReport.encounteredKnownErrorInfos.size());
        Assert.assertEquals(0, summaryReport.newErrorInfos.size());
        Assert.assertEquals(0, summaryReport.solvedKnownErrorsList.size());
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
        Assert.assertEquals(1, summaryReport.successfulTestCases);
        Assert.assertEquals(0, summaryReport.failedTestCasesWithNewDeviations);
        Assert.assertEquals(0, summaryReport.unevaluatedCount);
        Assert.assertEquals(0, summaryReport.testCasesWithBothNewAndKnownErrors);
        Assert.assertEquals(0, summaryReport.testCasesWithOnlyKnownErrors);
        Assert.assertEquals(0, summaryReport.encounteredKnownErrorInfos.size());
        Assert.assertEquals(0, summaryReport.newErrorInfos.size());
        Assert.assertEquals(1, summaryReport.solvedKnownErrorsList.size());
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
        Assert.assertEquals(0, summaryReport.successfulTestCases);
        Assert.assertEquals(1, summaryReport.failedTestCasesWithNewDeviations);
        Assert.assertEquals(0, summaryReport.unevaluatedCount);
        Assert.assertEquals(0, summaryReport.testCasesWithBothNewAndKnownErrors);
        Assert.assertEquals(0, summaryReport.testCasesWithOnlyKnownErrors);
        Assert.assertEquals(0, summaryReport.encounteredKnownErrorInfos.size());
        Assert.assertEquals(1, summaryReport.newErrorInfos.size());
        Assert.assertEquals(1, summaryReport.solvedKnownErrorsList.size());
    }

    @Test
    public void knownTestSetErrorTest(){
        SummaryReport summaryReport = new SummaryReport();
        TestSet testSet = new TestSet();
        testSet.addKnownError("Failed verification is being tested", ".*Failed verification.*");
        TestCase testCase = new TestCase(testSet.knownErrorsList, currentTestName.getMethodName());
        testCase.log(LogLevel.DEBUG, "Test");
        testCase.log(LogLevel.VERIFICATION_FAILED, "Failed verification");
        testCase.log(LogLevel.DEBUG, "Test");
        testCase.evaluateResultStatus();
        summaryReport.evaluateTestCase(testCase);
        summaryReport.evaluateTestSet(testSet);
        Assert.assertEquals(0, summaryReport.successfulTestCases);
        Assert.assertEquals(0, summaryReport.failedTestCasesWithNewDeviations);
        Assert.assertEquals(0, summaryReport.unevaluatedCount);
        Assert.assertEquals(0, summaryReport.testCasesWithBothNewAndKnownErrors);
        Assert.assertEquals(1, summaryReport.testCasesWithOnlyKnownErrors);
        Assert.assertEquals(1, summaryReport.encounteredKnownErrorInfos.size());
        Assert.assertEquals(0, summaryReport.newErrorInfos.size());
        Assert.assertEquals(0, summaryReport.solvedKnownErrorsList.size());
    }

    @Test
    public void solvedTestSetRegisteredKnownErrorsTest(){
        SummaryReport summaryReport = new SummaryReport();
        TestSet testSet = new TestSet();
        testSet.addKnownError("Fixed error", "String that is not encountered.");
        TestCase testCase = new TestCase(testSet.knownErrorsList, currentTestName.getMethodName());
        testCase.log(LogLevel.DEBUG, "Test");
        testCase.log(LogLevel.EXECUTED, "Framework error");
        testCase.log(LogLevel.DEBUG, "Test");
        testCase.evaluateResultStatus();
        summaryReport.evaluateTestCase(testCase);
        summaryReport.evaluateTestSet(testSet);
        Assert.assertEquals(1, summaryReport.successfulTestCases);
        Assert.assertEquals(0, summaryReport.failedTestCasesWithNewDeviations);
        Assert.assertEquals(0, summaryReport.unevaluatedCount);
        Assert.assertEquals(0, summaryReport.testCasesWithBothNewAndKnownErrors);
        Assert.assertEquals(0, summaryReport.testCasesWithOnlyKnownErrors);
        Assert.assertEquals(0, summaryReport.encounteredKnownErrorInfos.size());
        Assert.assertEquals(0, summaryReport.newErrorInfos.size());
        Assert.assertEquals(1, summaryReport.solvedKnownErrorsList.size());
    }

    @Test
    public void bothNewErrorAndSolvedTestSetKnownErrorTest(){
        SummaryReport summaryReport = new SummaryReport();
        TestSet testSet = new TestSet();
        testSet.addKnownError("Fixed error", "String that is not encountered.");
        TestCase testCase = new TestCase(testSet.knownErrorsList, currentTestName.getMethodName());
        testCase.log(LogLevel.DEBUG, "Test");
        testCase.log(LogLevel.FRAMEWORK_ERROR, "Framework error");
        testCase.log(LogLevel.DEBUG, "Test");
        testCase.evaluateResultStatus();
        summaryReport.evaluateTestCase(testCase);
        summaryReport.evaluateTestSet(testSet);
        Assert.assertEquals(0, summaryReport.successfulTestCases);
        Assert.assertEquals(1, summaryReport.failedTestCasesWithNewDeviations);
        Assert.assertEquals(0, summaryReport.unevaluatedCount);
        Assert.assertEquals(0, summaryReport.testCasesWithBothNewAndKnownErrors);
        Assert.assertEquals(0, summaryReport.testCasesWithOnlyKnownErrors);
        Assert.assertEquals(0, summaryReport.encounteredKnownErrorInfos.size());
        Assert.assertEquals(1, summaryReport.newErrorInfos.size());
        Assert.assertEquals(1, summaryReport.solvedKnownErrorsList.size());
    }

}
