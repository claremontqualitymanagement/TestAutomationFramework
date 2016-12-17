package se.claremont.autotest.common;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

/**
 * Tests for the HtmlSummaryReport class
 *
 * Created by jordam on 2016-08-31.
 */
public class HtmlSummaryReport_Tests {
    @SuppressWarnings("CanBeFinal")
    @Rule public TestName currentTestName = new TestName();

    @Test
    public void successfulTest(){
        HtmlSummaryReport htmlSummaryReport = new HtmlSummaryReport();
        TestCase testCase = new TestCase(new KnownErrorsList(), currentTestName.getMethodName());
        testCase.log(LogLevel.DEBUG, "Test");
        testCase.log(LogLevel.EXECUTED, "Test");
        testCase.log(LogLevel.DEBUG, "Test");
        testCase.evaluateResultStatus();
        htmlSummaryReport.evaluateTestCase(testCase);
        Assert.assertEquals(1, htmlSummaryReport.successfulTestCases);
        Assert.assertEquals(0, htmlSummaryReport.failedTestCasesWithNewDeviations);
        Assert.assertEquals(0, htmlSummaryReport.unevaluatedCount);
        Assert.assertEquals(0, htmlSummaryReport.testCasesWithBothNewAndKnownErrors);
        Assert.assertEquals(0, htmlSummaryReport.testCasesWithOnlyKnownErrors);
        Assert.assertEquals(0, htmlSummaryReport.encounteredKnownErrorInfos.size());
        Assert.assertEquals(0, htmlSummaryReport.newErrorInfos.size());
        Assert.assertEquals(0, htmlSummaryReport.solvedKnownErrorsList.size());
    }

    @Test
    public void newErrorTest(){
        HtmlSummaryReport htmlSummaryReport = new HtmlSummaryReport();
        TestCase testCase = new TestCase(new KnownErrorsList(), currentTestName.getMethodName());
        testCase.log(LogLevel.DEBUG, "Test");
        testCase.log(LogLevel.EXECUTION_PROBLEM, "Execution problem");
        testCase.log(LogLevel.DEBUG, "Test");
        testCase.evaluateResultStatus();
        htmlSummaryReport.evaluateTestCase(testCase);
        Assert.assertEquals(0, htmlSummaryReport.successfulTestCases);
        Assert.assertEquals(1, htmlSummaryReport.failedTestCasesWithNewDeviations);
        Assert.assertEquals(0, htmlSummaryReport.unevaluatedCount);
        Assert.assertEquals(0, htmlSummaryReport.testCasesWithBothNewAndKnownErrors);
        Assert.assertEquals(0, htmlSummaryReport.testCasesWithOnlyKnownErrors);
        Assert.assertEquals(0, htmlSummaryReport.encounteredKnownErrorInfos.size());
        Assert.assertEquals(1, htmlSummaryReport.newErrorInfos.size());
        Assert.assertEquals(0, htmlSummaryReport.solvedKnownErrorsList.size());
    }

    @Test
    public void knownErrorTest(){
        HtmlSummaryReport htmlSummaryReport = new HtmlSummaryReport();
        TestCase testCase = new TestCase(new KnownErrorsList(), currentTestName.getMethodName());
        testCase.addKnownError("Failed verification is being tested", ".*Failed verification.*");
        testCase.log(LogLevel.DEBUG, "Test");
        testCase.log(LogLevel.VERIFICATION_FAILED, "Failed verification");
        testCase.log(LogLevel.DEBUG, "Test");
        testCase.evaluateResultStatus();
        htmlSummaryReport.evaluateTestCase(testCase);
        Assert.assertEquals(0, htmlSummaryReport.successfulTestCases);
        Assert.assertEquals(0, htmlSummaryReport.failedTestCasesWithNewDeviations);
        Assert.assertEquals(0, htmlSummaryReport.unevaluatedCount);
        Assert.assertEquals(0, htmlSummaryReport.testCasesWithBothNewAndKnownErrors);
        Assert.assertEquals(1, htmlSummaryReport.testCasesWithOnlyKnownErrors);
        Assert.assertEquals(1, htmlSummaryReport.encounteredKnownErrorInfos.size());
        Assert.assertEquals(0, htmlSummaryReport.newErrorInfos.size());
        Assert.assertEquals(0, htmlSummaryReport.solvedKnownErrorsList.size());
    }

    @Test
    public void solvedErrorsTest(){
        HtmlSummaryReport htmlSummaryReport = new HtmlSummaryReport();
        TestCase testCase = new TestCase(new KnownErrorsList(), currentTestName.getMethodName());
        testCase.log(LogLevel.DEBUG, "Test");
        testCase.addKnownError("Fixed error", "String that is not encountered.");
        testCase.log(LogLevel.EXECUTED, "Framework error");
        testCase.log(LogLevel.DEBUG, "Test");
        testCase.evaluateResultStatus();
        htmlSummaryReport.evaluateTestCase(testCase);
        Assert.assertEquals(1, htmlSummaryReport.successfulTestCases);
        Assert.assertEquals(0, htmlSummaryReport.failedTestCasesWithNewDeviations);
        Assert.assertEquals(0, htmlSummaryReport.unevaluatedCount);
        Assert.assertEquals(0, htmlSummaryReport.testCasesWithBothNewAndKnownErrors);
        Assert.assertEquals(0, htmlSummaryReport.testCasesWithOnlyKnownErrors);
        Assert.assertEquals(0, htmlSummaryReport.encounteredKnownErrorInfos.size());
        Assert.assertEquals(0, htmlSummaryReport.newErrorInfos.size());
        Assert.assertEquals(1, htmlSummaryReport.solvedKnownErrorsList.size());
    }

    @Test
    public void bothNewAndSolvedErrorsTest(){
        HtmlSummaryReport htmlSummaryReport = new HtmlSummaryReport();
        TestCase testCase = new TestCase(new KnownErrorsList(), currentTestName.getMethodName());
        testCase.log(LogLevel.DEBUG, "Test");
        testCase.addKnownError("Fixed error", "String that is not encountered.");
        testCase.log(LogLevel.FRAMEWORK_ERROR, "Framework error");
        testCase.log(LogLevel.DEBUG, "Test");
        testCase.evaluateResultStatus();
        htmlSummaryReport.evaluateTestCase(testCase);
        Assert.assertEquals(0, htmlSummaryReport.successfulTestCases);
        Assert.assertEquals("New deviations count was expected to be 1 but was " + htmlSummaryReport.failedTestCasesWithNewDeviations + ".", 1, htmlSummaryReport.failedTestCasesWithNewDeviations);
        Assert.assertEquals("Unevaluated count was expected to be 0 but was " + htmlSummaryReport.unevaluatedCount + ".",0,  htmlSummaryReport.unevaluatedCount);
        Assert.assertEquals("Test cases with both new and known bugs count was expected to be 0 but was " + htmlSummaryReport.testCasesWithBothNewAndKnownErrors + ".",0,  htmlSummaryReport.testCasesWithBothNewAndKnownErrors);
        Assert.assertEquals("Test cases count for test cases with only known errors was expected to be 0 but was " + htmlSummaryReport.testCasesWithOnlyKnownErrors + ".",0, htmlSummaryReport.testCasesWithOnlyKnownErrors);
        Assert.assertEquals(0, htmlSummaryReport.encounteredKnownErrorInfos.size());
        Assert.assertEquals(1, htmlSummaryReport.newErrorInfos.size());
        Assert.assertEquals(1, htmlSummaryReport.solvedKnownErrorsList.size());
    }

    @Test
    public void knownTestSetErrorTest(){
        HtmlSummaryReport htmlSummaryReport = new HtmlSummaryReport();
        TestSet testSet = new TestSet();
        testSet.addKnownError("Failed verification is being tested", ".*Failed verification.*");
        TestCase testCase = new TestCase(testSet.knownErrorsList, currentTestName.getMethodName());
        testCase.log(LogLevel.DEBUG, "Test");
        testCase.log(LogLevel.VERIFICATION_FAILED, "Failed verification");
        testCase.log(LogLevel.DEBUG, "Test");
        testCase.evaluateResultStatus();
        htmlSummaryReport.evaluateTestCase(testCase);
        htmlSummaryReport.evaluateTestSet(testSet);
        Assert.assertEquals(0, htmlSummaryReport.successfulTestCases);
        Assert.assertEquals(0, htmlSummaryReport.failedTestCasesWithNewDeviations);
        Assert.assertEquals(0, htmlSummaryReport.unevaluatedCount);
        Assert.assertEquals(0, htmlSummaryReport.testCasesWithBothNewAndKnownErrors);
        Assert.assertEquals(1, htmlSummaryReport.testCasesWithOnlyKnownErrors);
        Assert.assertEquals(1, htmlSummaryReport.encounteredKnownErrorInfos.size());
        Assert.assertEquals(0, htmlSummaryReport.newErrorInfos.size());
        Assert.assertEquals(0, htmlSummaryReport.solvedKnownErrorsList.size());
    }

    @Test
    public void solvedTestSetRegisteredKnownErrorsTest(){
        HtmlSummaryReport htmlSummaryReport = new HtmlSummaryReport();
        TestSet testSet = new TestSet();
        testSet.addKnownError("Fixed error", "String that is not encountered.");
        TestCase testCase = new TestCase(testSet.knownErrorsList, currentTestName.getMethodName());
        testCase.log(LogLevel.DEBUG, "Test");
        testCase.log(LogLevel.EXECUTED, "Framework error");
        testCase.log(LogLevel.DEBUG, "Test");
        testCase.evaluateResultStatus();
        htmlSummaryReport.evaluateTestCase(testCase);
        htmlSummaryReport.evaluateTestSet(testSet);
        Assert.assertEquals(1, htmlSummaryReport.successfulTestCases);
        Assert.assertEquals(0, htmlSummaryReport.failedTestCasesWithNewDeviations);
        Assert.assertEquals(0, htmlSummaryReport.unevaluatedCount);
        Assert.assertEquals(0, htmlSummaryReport.testCasesWithBothNewAndKnownErrors);
        Assert.assertEquals(0, htmlSummaryReport.testCasesWithOnlyKnownErrors);
        Assert.assertEquals(0, htmlSummaryReport.encounteredKnownErrorInfos.size());
        Assert.assertEquals(0, htmlSummaryReport.newErrorInfos.size());
        Assert.assertEquals(1, htmlSummaryReport.solvedKnownErrorsList.size());
    }

    @Test
    public void bothNewErrorAndSolvedTestSetKnownErrorTest(){
        HtmlSummaryReport htmlSummaryReport = new HtmlSummaryReport();
        TestSet testSet = new TestSet();
        testSet.addKnownError("Fixed error", "String that is not encountered.");
        TestCase testCase = new TestCase(testSet.knownErrorsList, currentTestName.getMethodName());
        testCase.log(LogLevel.DEBUG, "Test");
        testCase.log(LogLevel.FRAMEWORK_ERROR, "Framework error");
        testCase.log(LogLevel.DEBUG, "Test");
        testCase.evaluateResultStatus();
        htmlSummaryReport.evaluateTestCase(testCase);
        htmlSummaryReport.evaluateTestSet(testSet);
        Assert.assertEquals(0, htmlSummaryReport.successfulTestCases);
        Assert.assertEquals("New deviations count was expected to be 1 but was " + htmlSummaryReport.failedTestCasesWithNewDeviations + ".", 1, htmlSummaryReport.failedTestCasesWithNewDeviations);
        Assert.assertEquals("Unevaluated count was expected to be 0 but was " + htmlSummaryReport.unevaluatedCount + ".", 0, htmlSummaryReport.unevaluatedCount);
        Assert.assertEquals("Test cases with both new and known bugs count was expected to be 0 but was " + htmlSummaryReport.testCasesWithBothNewAndKnownErrors + ".", 0, htmlSummaryReport.testCasesWithBothNewAndKnownErrors);
        Assert.assertEquals("Test cases count for test cases with only known errors was expected to be 0 but was " + htmlSummaryReport.testCasesWithOnlyKnownErrors + ".", 0, htmlSummaryReport.testCasesWithOnlyKnownErrors);
        Assert.assertEquals(0, htmlSummaryReport.encounteredKnownErrorInfos.size());
        Assert.assertEquals(1, htmlSummaryReport.newErrorInfos.size());
        Assert.assertEquals(1, htmlSummaryReport.solvedKnownErrorsList.size());
    }

}
