package se.claremont.taf.core.logging;

import org.junit.Assert;
import org.junit.Test;
import se.claremont.taf.core.testcase.TestCase;
import se.claremont.taf.core.testcase.TestCaseResult;
import se.claremont.taf.core.testset.TestSet;
import se.claremont.taf.core.testset.UnitTestClass;

/**
 * Test that the KnownErrorList class is behaving as expected
 *
 * Created by jordam on 2016-08-30.
 */
public class KnownErrorsList_Test extends UnitTestClass {
    private final TestClass testTestSet = new TestClass();

    @Test
    public void testCaseKnownErrorOnlyOneLogPostThatIsKnownError(){
        KnownErrorsList knownErrorsList = new KnownErrorsList();
        TestCase testCase1 = new TestCase(knownErrorsList, "KnownErrorsList_Test");
        testCase1.addKnownError("Description1", "TestCaseLog row 1.");
        testCase1.testCaseResult.testCaseLog.log(LogLevel.EXECUTION_PROBLEM, "TestCaseLog row 1.");
        testCase1.testCaseResult.evaluateResultStatus();
        Assert.assertTrue("Test case known error wasn't triggered. Expected resultStatus to be '" + TestCaseResult.ResultStatus.FAILED_WITH_ONLY_KNOWN_ERRORS.toString() + "', but was '" + testCase1.testCaseResult.resultStatus.toString() + "'.", testCase1.testCaseResult.resultStatus.equals(TestCaseResult.ResultStatus.FAILED_WITH_ONLY_KNOWN_ERRORS));
    }

    @Test
    public void testCaseKnownErrorBothKnownErrorAndNewError(){
        KnownErrorsList knownErrorsList = new KnownErrorsList();
        TestCase testCase1 = new TestCase(knownErrorsList, "KnownErrorsList_Test");
        testCase1.addKnownError("Description1", "TestCaseLog row 1.");
        testCase1.testCaseResult.testCaseLog.log(LogLevel.EXECUTION_PROBLEM, "TestCaseLog row 1.");
        testCase1.testCaseResult.testCaseLog.log(LogLevel.EXECUTION_PROBLEM, "TestCaseLog row 2.");
        testCase1.testCaseResult.evaluateResultStatus();
        Assert.assertTrue("Test case known error wasn't triggered. Expected resultStatus to be '" + TestCaseResult.ResultStatus.FAILED_WITH_BOTH_NEW_AND_KNOWN_ERRORS.toString() + "', but was '" + testCase1.testCaseResult.resultStatus.toString() + "'.", testCase1.testCaseResult.resultStatus.equals(TestCaseResult.ResultStatus.FAILED_WITH_BOTH_NEW_AND_KNOWN_ERRORS));
    }

    @Test
    public void testCaseKnownErrorNewError(){
        KnownErrorsList knownErrorsList = new KnownErrorsList();
        TestCase testCase1 = new TestCase(knownErrorsList, "KnownErrorsList_Test");
        testCase1.addKnownError("Description1", "TestCaseLog row 1.");
        testCase1.testCaseResult.testCaseLog.log(LogLevel.EXECUTION_PROBLEM, "TestCaseLog row 2.");
        testCase1.testCaseResult.evaluateResultStatus();
        Assert.assertTrue("Test case known error was triggered. Expected resultStatus to be '" + TestCaseResult.ResultStatus.FAILED_WITH_ONLY_NEW_ERRORS.toString() + "', but was '" + testCase1.testCaseResult.resultStatus.toString() + "'.", testCase1.testCaseResult.resultStatus.equals(TestCaseResult.ResultStatus.FAILED_WITH_ONLY_NEW_ERRORS));
    }

    @Test
    public void testCaseKnownErrorNoErrors(){
        KnownErrorsList knownErrorsList = new KnownErrorsList();
        TestCase testCase1 = new TestCase(knownErrorsList, "KnownErrorsList_Test");
        testCase1.addKnownError("Description1", "TestCaseLog row 1.");
        testCase1.testCaseResult.testCaseLog.log(LogLevel.INFO, "TestCaseLog row 1.");
        testCase1.testCaseResult.testCaseLog.log(LogLevel.DEBUG, "TestCaseLog row 2.");
        testCase1.testCaseResult.testCaseLog.log(LogLevel.EXECUTED, "TestCaseLog row 2.");
        testCase1.testCaseResult.testCaseLog.log(LogLevel.DEVIATION_EXTRA_INFO, "TestCaseLog row 2.");
        testCase1.testCaseResult.testCaseLog.log(LogLevel.VERIFICATION_PASSED, "TestCaseLog row 2.");
        testCase1.testCaseResult.evaluateResultStatus();
        Assert.assertTrue("Test case known error wasn't triggered. Expected resultStatus to be '" + TestCaseResult.ResultStatus.PASSED.toString() + "', but was '" + testCase1.testCaseResult.resultStatus.toString() + "'.", testCase1.testCaseResult.resultStatus.equals(TestCaseResult.ResultStatus.PASSED));
    }


    @Test
    public void testSetKnownErrorOnlyOneLogPostThatIsKnownError(){
        testTestSet.addKnownError("Description1", "TestCaseLog row 1.");
        TestCase testCase1 = new TestCase(testTestSet.knownErrorsList, "KnownErrorsList_Test");
        testCase1.testCaseResult.testCaseLog.log(LogLevel.EXECUTION_PROBLEM, "TestCaseLog row 1.");
        testCase1.testCaseResult.evaluateResultStatus();
        Assert.assertTrue("Test case known error wasn't triggered. Expected resultStatus to be '" + TestCaseResult.ResultStatus.FAILED_WITH_ONLY_KNOWN_ERRORS.toString() + "', but was '" + testCase1.testCaseResult.resultStatus.toString() + "'.", testCase1.testCaseResult.resultStatus.equals(TestCaseResult.ResultStatus.FAILED_WITH_ONLY_KNOWN_ERRORS));
    }

    public static class TestClass extends TestSet{

        @SuppressWarnings("EmptyMethod")
        @Test
        public void dummyTest(){}
    }
}
