package se.claremont.autotest.common.testset;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import se.claremont.autotest.common.logging.LogLevel;
import se.claremont.autotest.common.logging.LogPost;
import se.claremont.autotest.common.testcase.TestCase;

/**
 * Tests for the TestSet class
 *
 * Created by jordam on 2016-08-31.
 */
public class TestSet_Tests {
    @SuppressWarnings("CanBeFinal")
    @Rule public TestName currentTestName = new TestName();

    @Test
    public void instantiation(){
        TestSet testSet = new TestSet();
        Assert.assertTrue("Expected the TestSet name to be 'TestSet_Tests' but it was '" + testSet.name + "'.", testSet.name.contains("TestSet_Tests"));
    }

    @Test
    public void knownErrorsListAssessed(){
        TestSet testSet = new TestSet();
        testSet.addKnownError("Description", ".*Pattern string.*");
        testSet.startUpTestCase(currentTestName.getMethodName());
        testSet.currentTestCase.testCaseLog.log(LogLevel.FRAMEWORK_ERROR, "Pattern string");
        testSet.currentTestCase.evaluateResultStatus();
        Assert.assertTrue("Expected result status of test case to be '" + TestCase.ResultStatus.FAILED_WITH_ONLY_KNOWN_ERRORS.toString() + " but it was " + testSet.currentTestCase.resultStatus.toString() + ".", testSet.currentTestCase.resultStatus == TestCase.ResultStatus.FAILED_WITH_ONLY_KNOWN_ERRORS);
    }

    @Test
    public void knownErrorsListMultiPatternAssessed(){
        TestSet testSet = new TestSet();
        testSet.addKnownError("Description", "PatternString1");
        testSet.addKnownError("Description", "PatternString2");
        testSet.startUpTestCase(currentTestName.getMethodName());
        testSet.currentTestCase.testCaseLog.log(LogLevel.FRAMEWORK_ERROR, "PatternString1");
        testSet.currentTestCase.testCaseLog.log(LogLevel.FRAMEWORK_ERROR, "PatternString2");
        testSet.currentTestCase.evaluateResultStatus();
        Assert.assertTrue(testSet.currentTestCase.resultStatus == TestCase.ResultStatus.FAILED_WITH_ONLY_KNOWN_ERRORS);
    }

    @Test
    public void wrapUpCurrentTestSetShould(){
        TestSet testSet = new TestSet();
        testSet.startUpTestCase(currentTestName.getMethodName());
        testSet.currentTestCase.testCaseLog.log(LogLevel.EXECUTED, "PatternString1");
        //testSet.currentTestCase.evaluateResultStatus();
        TestCase theTestCase = testSet.currentTestCase;
        testSet.wrapUpTestCase();
        Assert.assertNotNull(testSet);
        Assert.assertNotNull(theTestCase);
        Assert.assertNotNull(theTestCase.stopTime);

        boolean logRowAboutEndingExecutionFound = false;
        for(LogPost logPost : theTestCase.testCaseLog.logPosts){
            if(logPost.message.contains("Ending test execution at ")){
                logRowAboutEndingExecutionFound = true;
                break;
            }
        }
        Assert.assertTrue(logRowAboutEndingExecutionFound);

        boolean logRowAboutEvaluatingTestResultStatusFound = false;
        for(LogPost logPost : theTestCase.testCaseLog.logPosts){
            if(logPost.message.contains("Evaluated test result status to")){
                logRowAboutEvaluatingTestResultStatusFound = true;
                break;
            }
        }
        Assert.assertTrue(logRowAboutEvaluatingTestResultStatusFound);

    }

    @Test
    public void knownErrorsListWithStringArrayAssessed(){
        TestSet testSet = new TestSet();
        testSet.addKnownError("Description", new String[] {".*Pattern string.*"});
        testSet.startUpTestCase(currentTestName.getMethodName());
        testSet.currentTestCase.testCaseLog.log(LogLevel.FRAMEWORK_ERROR, "Pattern string");
        testSet.currentTestCase.evaluateResultStatus();
        Assert.assertTrue("Expected result status of test case to be '" + TestCase.ResultStatus.FAILED_WITH_ONLY_KNOWN_ERRORS.toString() + " but it was " + testSet.currentTestCase.resultStatus.toString() + ".", testSet.currentTestCase.resultStatus == TestCase.ResultStatus.FAILED_WITH_ONLY_KNOWN_ERRORS);
    }

}
