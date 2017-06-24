package se.claremont.autotest.common.testcase;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.junit.Assert;
import org.junit.Assume;
import se.claremont.autotest.common.logging.KnownError;
import se.claremont.autotest.common.logging.KnownErrorsList;
import se.claremont.autotest.common.logging.LogLevel;
import se.claremont.autotest.common.logging.LogPost;
import se.claremont.autotest.common.support.StringManagement;
import se.claremont.autotest.common.support.SupportMethods;
import se.claremont.autotest.common.testrun.TestRun;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * Test execution result information
 *
 * Created by jordam on 2017-06-24.
 */
@JsonIgnoreProperties({"testCase"})
public class TestCaseResult {
    private TestCase testCase;
    @JsonProperty public TestCaseLog testCaseLog;
    @JsonProperty public String testName;
    @JsonProperty public String testSetName;
    @JsonProperty public Date startTime;
    @JsonProperty public Date stopTime;
    @JsonProperty public TestCaseData testCaseData;
    @JsonProperty public ResultStatus resultStatus = ResultStatus.UNEVALUATED;
    @JsonProperty public final UUID uid = UUID.randomUUID();
    @JsonProperty private final KnownErrorsList testSetKnownErrorsEncounteredInThisTestCase = new KnownErrorsList();

    public TestCaseResult(){
        testSetName = "Tests constructed from JSON";
        startTime = new Date();
    }

    public TestCaseResult(TestCase testCase){
        this.testCase = testCase;
        testCaseLog = new TestCaseLog();
        testCaseData = new TestCaseData();
        startTime = new Date();
        testSetName = testCase.testSetName;
        testName = testCase.testName;
        testCaseLog.log(LogLevel.INFO, "Starting test execution at " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startTime) + ".");
    }

    public void assessResults(TestCase testCase){
        stopTime = new Date();
        testCaseLog.log(LogLevel.INFO, "Ending test execution at " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(stopTime) + ".");
        evaluateResultStatus();
        testCaseLog.log(LogLevel.DEBUG, "Evaluated test result status to '" + StringManagement.enumCapitalNameToFriendlyString(resultStatus.toString()) + "'.");
        testCase.reporters.forEach(TestCaseLogReporter::report);
        TestRun.reporters.evaluateTestCase(testCase);
        assertExecutionResultsToTestRunner();
    }

    /**
     * Evaluates test case execution testCaseLog to assess if the test case ran successfully, if known errors were encountered - or new errors. Return result status.
     */
    public void evaluateResultStatus(){
        logKnownErrors();
        ArrayList<LogPost> erroneousPosts = testCaseLog.onlyErroneousLogPosts();
        if(erroneousPosts.size() == 0) {
            resultStatus = ResultStatus.PASSED;
            String message = "   (•̀ᴗ•́)و    No deviations found for test case '" + testName + "'. Success! Passed.";
            System.out.println(message);
            testCaseLog.log(LogLevel.INFO, message);
            return;
        }

        testCase.testCaseKnownErrorsList.assessLogForKnownErrors(testCase);
        testCase.testSetKnownErrors.assessLogForKnownErrors(testCase);
        //testSetKnownErrorsEncounteredInThisTestCase = KnownErrorsList.returnEncounteredKnownErrorsFromKnownErrorsListMatchedToLog(testSetKnownErrors, testCaseLog);

        boolean knownErrorsEncountered = false;
        boolean newErrorsEncountered = false;
        for(LogPost logPost : testCaseLog.onlyErroneousLogPosts()){
            if(logPost.identifiedToBePartOfKnownError){
                knownErrorsEncountered = true;
                if(newErrorsEncountered) break; //if both are set to true: continue
            }else{
                newErrorsEncountered = true;
                if(knownErrorsEncountered) break;
            }
        }

        if(newErrorsEncountered && knownErrorsEncountered){
            String message = "   (⊙_◎)   Both new and known errors found for test case '" + testName + "'.";
            resultStatus = ResultStatus.FAILED_WITH_BOTH_NEW_AND_KNOWN_ERRORS;
            System.out.println(message);
            testCaseLog.log(LogLevel.INFO, message);
            TestRun.exitCode = TestRun.ExitCodeTable.RUN_TEST_ERROR_MODERATE.getValue();
        } else if (newErrorsEncountered){
            resultStatus = ResultStatus.FAILED_WITH_ONLY_NEW_ERRORS;
            String message = "  ٩(͡๏̯͡๏)۶  Deviations not marked as known was found for test case '" + testName + "'.";
            System.out.println(message);
            testCaseLog.log(LogLevel.INFO, message);
            TestRun.exitCode = TestRun.ExitCodeTable.RUN_TEST_ERROR_FATAL.getValue();
        } else {
            resultStatus = ResultStatus.FAILED_WITH_ONLY_KNOWN_ERRORS;
            String message = " ¯\\_(ツ)_/¯ Deviations found, but all of them was marked as known for test case '" + testName + "'.";
            testCaseLog.log(LogLevel.INFO, message);
            System.out.println(message);
        }
    }

    /**
     * When a test case is evaluated after a test run the result status is set
     */
    public enum ResultStatus{
        UNEVALUATED (0),
        PASSED (1),
        FAILED_WITH_ONLY_KNOWN_ERRORS (2),
        FAILED_WITH_BOTH_NEW_AND_KNOWN_ERRORS (4),
        FAILED_WITH_ONLY_NEW_ERRORS (3);

        ResultStatus(int value){
            this.value = value;
        }
        private int value;

        public int value() {
            return value;
        }
    }


    /**
     * Marks the test case status in the style of the test runner according to result status.
     * Also halts further test case execution.
     */
    private void assertExecutionResultsToTestRunner(){
        if(resultStatus == ResultStatus.UNEVALUATED) evaluateResultStatus();
        if(resultStatus == ResultStatus.FAILED_WITH_BOTH_NEW_AND_KNOWN_ERRORS || resultStatus == ResultStatus.FAILED_WITH_ONLY_NEW_ERRORS){
            //noinspection ConstantConditions
            Assert.assertFalse(SupportMethods.LF + testCaseLog.toString(), true);
            if( resultStatus == ResultStatus.FAILED_WITH_ONLY_NEW_ERRORS )
                TestRun.exitCode = TestRun.ExitCodeTable.RUN_TEST_ERROR_FATAL.getValue();
            else
                TestRun.exitCode = TestRun.ExitCodeTable.RUN_TEST_ERROR_MODERATE.getValue();
        } else if(resultStatus == ResultStatus.FAILED_WITH_ONLY_KNOWN_ERRORS){
            //noinspection ConstantConditions
            Assume.assumeTrue(false);
            Assert.assertFalse(SupportMethods.LF + testCaseLog.toString(), true);
            TestRun.exitCode = TestRun.ExitCodeTable.RUN_TEST_ERROR_MODERATE.getValue();
        }
    }

    private void logKnownErrors(){
        testCaseLog.log(LogLevel.DEBUG, "Assessing test case testCaseLog for known errors.");
        testCase.testCaseKnownErrorsList.assessLogForKnownErrors(testCase);
        testCase.testCaseKnownErrorsList.knownErrors.stream().filter(KnownError::encountered).forEachOrdered(knownError -> testCaseLog.log(LogLevel.INFO, "Known error '" + knownError.description + "' encountered during execution."));
        for(KnownError knownError : testCase.testCaseKnownErrorsList.nonencounteredKnownErrors().knownErrors){
            testCaseLog.log(LogLevel.INFO, "Known error '" + knownError.description + "' was not encountered during test execution.");
        }

        //testSetKnownErrorsEncounteredInThisTestCase = KnownErrorsList.returnEncounteredKnownErrorsFromKnownErrorsListMatchedToLog(testSetKnownErrors, testCaseLog);

        testSetKnownErrorsEncounteredInThisTestCase.knownErrors.stream().filter(KnownError::encountered).forEachOrdered(knownError -> testCaseLog.log(LogLevel.INFO, "Known error from test set '" + knownError.description + "' encountered during execution."));
    }

}
