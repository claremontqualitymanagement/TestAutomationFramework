package se.claremont.taf.core.testset;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import se.claremont.taf.core.logging.LogLevel;
import se.claremont.taf.core.logging.LogPost;
import se.claremont.taf.core.testcase.TestCase;
import se.claremont.taf.core.testcase.TestCaseResult;

/**
 * Tests for the TestSet class
 *
 * Created by jordam on 2016-08-31.
 */
public class TestSet_Tests extends UnitTestClass{
    @SuppressWarnings("CanBeFinal")
    @Rule public TestName currentTestName = new TestName();

    @Test
    public void instantiation(){
        TestClass testSet = new TestClass();
        Assert.assertTrue("Expected the TestSet name to be 'TestSet_Tests' but it was '" + testSet.name + "'.", testSet.name.contains("TestSet_Tests"));
    }

    @Test
    public void knownErrorsListAssessed(){
        TestClass testSet = new TestClass();
        testSet.addKnownError("Description", ".*Pattern string.*");
        testSet.startUpTestCase(currentTestName.getMethodName(), this.getClass().getName());
        testSet.currentTestCase().log(LogLevel.FRAMEWORK_ERROR, "Pattern string");
        testSet.currentTestCase().testCaseResult.evaluateResultStatus();
        Assert.assertTrue("Expected result status of test case to be '" + TestCaseResult.ResultStatus.FAILED_WITH_ONLY_KNOWN_ERRORS.toString() + " but it was " + testSet.currentTestCase().testCaseResult.resultStatus.toString() + ".", testSet.currentTestCase().testCaseResult.resultStatus == TestCaseResult.ResultStatus.FAILED_WITH_ONLY_KNOWN_ERRORS);
    }

    @Test
    public void knownErrorsListMultiPatternAssessed(){
        TestClass testSet = new TestClass();
        testSet.addKnownError("Description", "PatternString1");
        testSet.addKnownError("Description", "PatternString2");
        testSet.startUpTestCase(currentTestName.getMethodName(), this.getClass().getName());
        testSet.currentTestCase().testCaseResult.testCaseLog.log(LogLevel.FRAMEWORK_ERROR, "PatternString1");
        testSet.currentTestCase().testCaseResult.testCaseLog.log(LogLevel.FRAMEWORK_ERROR, "PatternString2");
        testSet.currentTestCase().testCaseResult.evaluateResultStatus();
        Assert.assertTrue(testSet.currentTestCase().testCaseResult.resultStatus == TestCaseResult.ResultStatus.FAILED_WITH_ONLY_KNOWN_ERRORS);
    }

    @Test
    public void wrapUpCurrentTestSetShould(){
        TestClass testSet = new TestClass();
        testSet.startUpTestCase(currentTestName.getMethodName(), this.getClass().getName());
        testSet.currentTestCase().testCaseResult.testCaseLog.log(LogLevel.EXECUTED, "PatternString1");
        //testSet.currentTestCase().evaluateResultStatus();
        TestCase theTestCase = testSet.currentTestCase();
        testSet.wrapUpTestCase();
        Assert.assertNotNull(testSet);
        Assert.assertNotNull(theTestCase);
        Assert.assertNotNull(theTestCase.testCaseResult.stopTime);

        boolean logRowAboutEndingExecutionFound = false;
        for(LogPost logPost : theTestCase.testCaseResult.testCaseLog.logPosts){
            if(logPost.message.contains("Ending test execution at ")){
                logRowAboutEndingExecutionFound = true;
                break;
            }
        }
        Assert.assertTrue(logRowAboutEndingExecutionFound);

        boolean logRowAboutEvaluatingTestResultStatusFound = false;
        for(LogPost logPost : theTestCase.testCaseResult.testCaseLog.logPosts){
            if(logPost.message.contains("Evaluated test result status to")){
                logRowAboutEvaluatingTestResultStatusFound = true;
                break;
            }
        }
        Assert.assertTrue(logRowAboutEvaluatingTestResultStatusFound);

    }

    @Test
    public void knownErrorsListWithStringArrayAssessed(){
        TestClass testSet = new TestClass();
        testSet.addKnownError("Description", new String[] {".*Pattern string.*"});
        testSet.startUpTestCase(currentTestName.getMethodName(), this.getClass().getName());
        testSet.currentTestCase().testCaseResult.testCaseLog.log(LogLevel.FRAMEWORK_ERROR, "Pattern string");
        testSet.currentTestCase().testCaseResult.evaluateResultStatus();
        Assert.assertTrue("Expected result status of test case to be '" + TestCaseResult.ResultStatus.FAILED_WITH_ONLY_KNOWN_ERRORS.toString() + " but it was " + testSet.currentTestCase().testCaseResult.resultStatus.toString() + ".", testSet.currentTestCase().testCaseResult.resultStatus == TestCaseResult.ResultStatus.FAILED_WITH_ONLY_KNOWN_ERRORS);
    }

    @SuppressWarnings("EmptyMethod")
    public static class TestClass extends TestSet{

        @Test
        public void dummyTest(){}
    }

}
