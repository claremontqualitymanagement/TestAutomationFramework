package se.claremont.taf.core.testcase;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import se.claremont.taf.core.logging.KnownError;
import se.claremont.taf.core.logging.KnownErrorsList;
import se.claremont.taf.core.logging.LogLevel;
import se.claremont.taf.core.support.SupportMethods;
import se.claremont.taf.core.testset.UnitTestClass;

/**
 *
 * Tests for the TestCase class
 *
 * Created by jordam on 2016-08-30.
 */
public class TestCase_Tests extends UnitTestClass{

    @Test
    public void matchOfSeveralStringsInitial(){
        TestCase testCase = new TestCase(new KnownErrorsList(), "TestCase_Tests");
        String[] patterns = {"Pattern1", "Pattern2"};
        testCase.addKnownError("Description", patterns);
        Assert.assertTrue("Test case status expected to be '" + TestCaseResult.ResultStatus.UNEVALUATED.toString() + "', but was '" + testCase.testCaseResult.resultStatus.toString() + "'.", testCase.testCaseResult.resultStatus.equals(TestCaseResult.ResultStatus.UNEVALUATED));
    }

    @Test
    public void matchOfSeveralStringsNoError(){
        TestCase testCase = new TestCase(new KnownErrorsList(), "TestCase_Tests");
        String[] patterns = {"Pattern1", "Pattern2"};
        testCase.addKnownError("Description", patterns);
        testCase.testCaseResult.testCaseLog.log(LogLevel.EXECUTED, "Pattern0");
        testCase.testCaseResult.evaluateResultStatus();
        Assert.assertTrue("Test case status expected to be '" + TestCaseResult.ResultStatus.PASSED.toString() + "', but was '" + testCase.testCaseResult.resultStatus.toString() + "'.", testCase.testCaseResult.resultStatus.equals(TestCaseResult.ResultStatus.PASSED));
    }

    @Test
    public void matchOfSeveralStringsMatch(){
        TestCase testCase = new TestCase(new KnownErrorsList(), "TestCase_Tests");
        String[] patterns = {"Pattern1", "Pattern2"};
        testCase.addKnownError("Description", patterns);
        testCase.testCaseResult.testCaseLog.log(LogLevel.VERIFICATION_PROBLEM, "Pattern1");
        testCase.testCaseResult.testCaseLog.log(LogLevel.VERIFICATION_PROBLEM, "Pattern2");
        testCase.testCaseResult.evaluateResultStatus();
        Assert.assertTrue("Test case status expected to be '" + TestCaseResult.ResultStatus.FAILED_WITH_ONLY_KNOWN_ERRORS.toString() + "', but was '" + testCase.testCaseResult.resultStatus.toString() + "'.", testCase.testCaseResult.resultStatus.equals(TestCaseResult.ResultStatus.FAILED_WITH_ONLY_KNOWN_ERRORS));
    }

    @Test
    public void matchOfSeveralStringsBothMatchAndOtherErrors(){
        TestCase testCase = new TestCase(new KnownErrorsList(), "TestCase_Tests");
        String[] patterns = {"Pattern1", "Pattern2"};
        testCase.addKnownError("Description", patterns);
        testCase.testCaseResult.testCaseLog.log(LogLevel.VERIFICATION_PROBLEM, "Pattern1");
        testCase.testCaseResult.testCaseLog.log(LogLevel.VERIFICATION_PROBLEM, "Pattern2");
        testCase.testCaseResult.testCaseLog.log(LogLevel.VERIFICATION_PROBLEM, "Pattern3");
        testCase.testCaseResult.evaluateResultStatus();
        Assert.assertTrue("Test case status expected to be '" + TestCaseResult.ResultStatus.FAILED_WITH_BOTH_NEW_AND_KNOWN_ERRORS.toString() + "', but was '" + testCase.testCaseResult.resultStatus.toString() + "'.", testCase.testCaseResult.resultStatus.equals(TestCaseResult.ResultStatus.FAILED_WITH_BOTH_NEW_AND_KNOWN_ERRORS));
    }

    @Test
    public void matchOfSeveralStringsOnlyNewErrors(){
        TestCase testCase = new TestCase(new KnownErrorsList(), "TestCase_Tests");
        String[] patterns = {"Pattern1", "Pattern2"};
        testCase.addKnownError("Description", patterns);
        testCase.testCaseResult.testCaseLog.log(LogLevel.EXECUTED, "Pattern0");
        testCase.testCaseResult.testCaseLog.log(LogLevel.VERIFICATION_PROBLEM, "Pattern2");
        testCase.testCaseResult.testCaseLog.log(LogLevel.VERIFICATION_PROBLEM, "Pattern3");
        testCase.testCaseResult.evaluateResultStatus();
        Assert.assertTrue("Test case status expected to be '" + TestCaseResult.ResultStatus.FAILED_WITH_ONLY_NEW_ERRORS.toString() + "', but was '" + testCase.testCaseResult.resultStatus.toString() + "'.", testCase.testCaseResult.resultStatus.equals(TestCaseResult.ResultStatus.FAILED_WITH_ONLY_NEW_ERRORS));
    }

    @Test
    public void testCaseDataAddingAndReading(){
        TestCase testCase = new TestCase(new KnownErrorsList(), "TestCase_Tests");
        testCase.addTestCaseData("Parameter name", "Parameter DATA");
        Assert.assertTrue("Test case DATA was expected to be 'Parameter DATA' but was '"  + testCase.valueForFirstMatchForTestCaseDataParameter("Parameter name"), testCase.valueForFirstMatchForTestCaseDataParameter("Parameter name").equals("Parameter DATA"));
    }

    @Test
    public void testCaseDataNoMatchIsEmptyString(){
        TestCase testCase = new TestCase(new KnownErrorsList(), "TestCase_Tests");
        testCase.addTestCaseData("Parameter name", "Parameter DATA");
        Assert.assertTrue(testCase.valueForFirstMatchForTestCaseDataParameter("Wrong Parameter name").equals(""));
    }

    @Test
    public void testCaseDataMultipleMatches(){
        TestCase testCase = new TestCase(new KnownErrorsList(), "TestCase_Tests");
        testCase.addTestCaseData("Parameter name", "Parameter DATA");
        testCase.addTestCaseData("Parameter name", "Parameter DATA 2");
        Assert.assertTrue(testCase.valueForFirstMatchForTestCaseDataParameter("Parameter name").equals("Parameter DATA"));
    }

    @Test
    public void isSameAsTest(){
        TestCase testCase = new TestCase(new KnownErrorsList(), "isSameAsTest");
        TestCase testCase2 = new TestCase(new KnownErrorsList(), "isSameAsTest");
        Assert.assertTrue(testCase.isSameAs(testCase));
        Assert.assertFalse(testCase.isSameAs(testCase2));
    }

    //Used for printing test
    @Test
    @Ignore
    public void
    testCaseEvaluationStatusPrintoutCheck(){
        KnownErrorsList knownErrorsList = new KnownErrorsList();
        knownErrorsList.add(new KnownError("TEST", ".*Text.*"));
        TestCase testCase = new TestCase(knownErrorsList, "dummy");

        //Passed
        testCase.log(LogLevel.VERIFICATION_PASSED, "Text");
        testCase.testCaseResult.evaluateResultStatus();

        //Only new error
        testCase.log(LogLevel.FRAMEWORK_ERROR, "Other error");
        testCase.testCaseResult.evaluateResultStatus();

        //Both new and known errors
        testCase.log(LogLevel.VERIFICATION_FAILED, "Text");
        testCase.testCaseResult.evaluateResultStatus();

        knownErrorsList.add(new KnownError("TEST2", ".*Other error.*"));
        testCase.testCaseResult.evaluateResultStatus();
    }

    @Ignore("Fails on mac, needs to be tested on windows")
    @Test
    public void writingProcessChangesSinceStartOfTestCaseIfNoChanges(){
        TestCase testCase = new TestCase(null, "dummy");
        testCase.writeProcessListDeviationsFromSystemStartToLog();

        boolean processesFound = testCase.testCaseResult.testCaseLog.logPosts.stream().
                anyMatch(logPost -> logPost.message.contains("No changes to what processes are running, from test case start until now, could be detected."));

        Assert.assertTrue("Changes in processes when not expecting any", processesFound);
    }

    @Ignore("Fails on travis")
    @Test
    public void writingProcessChangesSinceStartOfTestCaseIfChanges(){
        TestCase testCase = new TestCase(null, "dummy");
        SupportMethods.startProgram("cmd.exe", testCase);

        testCase.writeProcessListDeviationsFromSystemStartToLog();

        boolean logRowAboutChangesToRunningProcessesFound = testCase.testCaseResult.testCaseLog.logPosts.stream().
                anyMatch(logPost -> logPost.message.contains("Process(es) added since test case start: '"));


        Assert.assertTrue(logRowAboutChangesToRunningProcessesFound);
    }


    @Test
    public void testCaseCreatedWithKnownErrorListNullShouldCreateEmptyKnownErrorsList(){
        TestCase testCase = new TestCase(null, "dummy");
        Assert.assertNotNull(testCase.testCaseKnownErrorsList);
    }

}
