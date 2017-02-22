package se.claremont.autotest.common.logging;

import org.junit.Assert;
import org.junit.Test;
import se.claremont.autotest.common.testcase.TestCase;
import se.claremont.autotest.common.testset.TestSet;

/**
 * Test that the KnownErrorList class is behaving as expected
 *
 * Created by jordam on 2016-08-30.
 */
public class KnownErrorsList_Test {
    private final TestClass testTestSet = new TestClass();

    @Test
    public void testCaseKnownErrorOnlyOneLogPostThatIsKnownError(){
        KnownErrorsList knownErrorsList = new KnownErrorsList();
        TestCase testCase1 = new TestCase(knownErrorsList, "KnownErrorsList_Test");
        testCase1.addKnownError("Description1", "TestCaseLog row 1.");
        testCase1.testCaseLog.log(LogLevel.EXECUTION_PROBLEM, "TestCaseLog row 1.");
        testCase1.evaluateResultStatus();
        Assert.assertTrue("Test case known error wasn't triggered. Expected resultStatus to be '" + TestCase.ResultStatus.FAILED_WITH_ONLY_KNOWN_ERRORS.toString() + "', but was '" + testCase1.resultStatus.toString() + "'.", testCase1.resultStatus.equals(TestCase.ResultStatus.FAILED_WITH_ONLY_KNOWN_ERRORS));
    }

    @Test
    public void testCaseKnownErrorBothKnownErrorAndNewError(){
        KnownErrorsList knownErrorsList = new KnownErrorsList();
        TestCase testCase1 = new TestCase(knownErrorsList, "KnownErrorsList_Test");
        testCase1.addKnownError("Description1", "TestCaseLog row 1.");
        testCase1.testCaseLog.log(LogLevel.EXECUTION_PROBLEM, "TestCaseLog row 1.");
        testCase1.testCaseLog.log(LogLevel.EXECUTION_PROBLEM, "TestCaseLog row 2.");
        testCase1.evaluateResultStatus();
        Assert.assertTrue("Test case known error wasn't triggered. Expected resultStatus to be '" + TestCase.ResultStatus.FAILED_WITH_BOTH_NEW_AND_KNOWN_ERRORS.toString() + "', but was '" + testCase1.resultStatus.toString() + "'.", testCase1.resultStatus.equals(TestCase.ResultStatus.FAILED_WITH_BOTH_NEW_AND_KNOWN_ERRORS));
    }

    @Test
    public void testCaseKnownErrorNewError(){
        KnownErrorsList knownErrorsList = new KnownErrorsList();
        TestCase testCase1 = new TestCase(knownErrorsList, "KnownErrorsList_Test");
        testCase1.addKnownError("Description1", "TestCaseLog row 1.");
        testCase1.testCaseLog.log(LogLevel.EXECUTION_PROBLEM, "TestCaseLog row 2.");
        testCase1.evaluateResultStatus();
        Assert.assertTrue("Test case known error was triggered. Expected resultStatus to be '" + TestCase.ResultStatus.FAILED_WITH_ONLY_NEW_ERRORS.toString() + "', but was '" + testCase1.resultStatus.toString() + "'.", testCase1.resultStatus.equals(TestCase.ResultStatus.FAILED_WITH_ONLY_NEW_ERRORS));
    }

    @Test
    public void testCaseKnownErrorNoErrors(){
        KnownErrorsList knownErrorsList = new KnownErrorsList();
        TestCase testCase1 = new TestCase(knownErrorsList, "KnownErrorsList_Test");
        testCase1.addKnownError("Description1", "TestCaseLog row 1.");
        testCase1.testCaseLog.log(LogLevel.INFO, "TestCaseLog row 1.");
        testCase1.testCaseLog.log(LogLevel.DEBUG, "TestCaseLog row 2.");
        testCase1.testCaseLog.log(LogLevel.EXECUTED, "TestCaseLog row 2.");
        testCase1.testCaseLog.log(LogLevel.DEVIATION_EXTRA_INFO, "TestCaseLog row 2.");
        testCase1.testCaseLog.log(LogLevel.VERIFICATION_PASSED, "TestCaseLog row 2.");
        testCase1.evaluateResultStatus();
        Assert.assertTrue("Test case known error wasn't triggered. Expected resultStatus to be '" + TestCase.ResultStatus.PASSED.toString() + "', but was '" + testCase1.resultStatus.toString() + "'.", testCase1.resultStatus.equals(TestCase.ResultStatus.PASSED));
    }


    @Test
    public void testSetKnownErrorOnlyOneLogPostThatIsKnownError(){
        testTestSet.addKnownError("Description1", "TestCaseLog row 1.");
        TestCase testCase1 = new TestCase(testTestSet.knownErrorsList, "KnownErrorsList_Test");
        testCase1.testCaseLog.log(LogLevel.EXECUTION_PROBLEM, "TestCaseLog row 1.");
        testCase1.evaluateResultStatus();
        Assert.assertTrue("Test case known error wasn't triggered. Expected resultStatus to be '" + TestCase.ResultStatus.FAILED_WITH_ONLY_KNOWN_ERRORS.toString() + "', but was '" + testCase1.resultStatus.toString() + "'.", testCase1.resultStatus.equals(TestCase.ResultStatus.FAILED_WITH_ONLY_KNOWN_ERRORS));
    }

    class TestClass extends TestSet{}
}
