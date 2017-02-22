package se.claremont.autotest.common.testset;

import org.junit.*;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import se.claremont.autotest.common.logging.KnownError;
import se.claremont.autotest.common.logging.KnownErrorsList;
import se.claremont.autotest.common.support.SupportMethods;
import se.claremont.autotest.common.testcase.TestCase;
import se.claremont.autotest.common.testrun.TestRun;

/**
 * A test set is a set of test cases
 *
 * Created by jordam on 2016-08-17.
 */
@RunWith(se.claremont.autotest.common.testrun.TafTestRunner.class)
public abstract class TestSet {
    @Rule
    public TestName currentTestName = new TestName();

    @Before
    public void testSetClassInternalSetup(){
        startUpTestCase(currentTestName.getMethodName());
    }

    @After
    public void testSetClassInternalTearDown(){
        wrapUpTestCase();
    }

    @AfterClass
    public static void testSetClassInternalClassTeardown(){
        TestRun.reporters.evaluateTestSet(TestRun.currentTestSet);
    }


    public TestCase currentTestCase;
    public String name;
    public final KnownErrorsList knownErrorsList = new KnownErrorsList();

    /**
     * Setting up a new test set instance
     */
    public TestSet(){
        TestRun.initializeIfNotInitialized();
        TestRun.currentTestSet = this;
        name = SupportMethods.classNameAtStacktraceLevel(3);
    }

    /**
     * Known errors can be entered at a test set level, making them valid for all test cases in the test set.
     * All patterns entered must be found in a test case for the known error to match
     * @param description The text string describing the error
     * @param regexPatternsForLogRowsThatMustOccur TestCaseLog patterns to find in the test case execution testCaseLog
     */
    @SuppressWarnings("unused")
    public void addKnownError(String description, String[] regexPatternsForLogRowsThatMustOccur){
        knownErrorsList.add(new KnownError(description, regexPatternsForLogRowsThatMustOccur));
    }

    /**
     * Known errors can be entered at a test set level, making them valid for all test cases in the test set.
     * All patterns entered must be found in a test case for the known error to match
     * @param description The text string describing the error
     * @param regexPatternForLogRow TestCaseLog pattern to find in the test case execution testCaseLog
     */
    public void addKnownError(String description, String regexPatternForLogRow){
        knownErrorsList.add(new KnownError(description, regexPatternForLogRow));
    }

    /**
     * Procedures common for all test cases
     * @param testName The name of the test, for reporting purposes.
     */
    public void startUpTestCase(String testName){
        currentTestCase = new TestCase(knownErrorsList, testName);
    }

    /**
     * Closes down test case execution.
     * Test case tear down procedure at the test set level
      */
    protected void wrapUpTestCase(){
        currentTestCase.report();
        currentTestCase = null;
    }



}
