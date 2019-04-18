package se.claremont.taf.core.testcase;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import se.claremont.taf.core.logging.KnownError;
import se.claremont.taf.core.logging.LogLevel;
import se.claremont.taf.core.logging.LogPost;
import se.claremont.taf.core.support.StringManagement;
import se.claremont.taf.core.testrun.TestRun;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
    @JsonProperty public final String testSetName;
    @JsonProperty public final Date startTime;
    @JsonProperty public Date stopTime;
    @JsonProperty public String pathToHtmlLogFile;
    @JsonProperty public TestCaseData testCaseData;
    @JsonProperty public ResultStatus resultStatus = ResultStatus.UNEVALUATED;
    @JsonProperty public final UUID uid = UUID.randomUUID();
    @JsonProperty public List<KnownError> knownErrorsEncountered;
    @JsonProperty public List<KnownError> knownErrorsNotEncountered;

    /**
     * This constructor is only used automatic for JSON transformation
     */
    public TestCaseResult(){
        testSetName = "Tests constructed from JSON";
        startTime = new Date();
    }

    public TestCaseResult(TestCase testCase){
        this.testCase = testCase;
        testCaseLog = new TestCaseLog(testCase.testName, testCase.testCaseMethodName);
        testCaseData = new TestCaseData();
        startTime = new Date();
        testSetName = testCase.testSetName;
        testName = testCase.testName;
        knownErrorsEncountered = new ArrayList<>();
        knownErrorsNotEncountered = new ArrayList<>();
        pathToHtmlLogFile = testCase.pathToHtmlLogFile;
        testCaseLog.log(LogLevel.INFO, "Starting test execution at " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startTime) + ".");
    }


    public void assessResults(){
        stopTime = new Date();
        testCaseLog.log(LogLevel.INFO, "Ending test execution at " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(stopTime) + ".");
        evaluateResultStatus();
        testCaseLog.log(LogLevel.DEBUG, "Evaluated test result status to '" + StringManagement.enumCapitalNameToFriendlyString(resultStatus.toString()) + "'.");
    }

    private void identifyEncounteredKnownErrors(){
        testCaseLog.log(LogLevel.DEBUG, "Assessing test case testCaseLog for known errors.");
        for(KnownError knownError : testCase.testCaseKnownErrorsList.knownErrors) {
            if(knownError.thisErrorIsEncountered(testCase)) {
                knownErrorsEncountered.add(knownError);
            } else {
                knownErrorsNotEncountered.add(knownError);
            }
        }
        for(KnownError knownError : testCase.testSetKnownErrors.knownErrors){
            if(knownError.thisErrorIsEncountered(testCase)){
                knownErrorsEncountered.add(knownError);
            } else {
                knownErrorsNotEncountered.add(knownError);
            }
        }
        if(knownErrorsEncountered.size() == 0){
            testCaseLog.log(LogLevel.DEBUG, "No registered known errors were encountered during test execution.");
        } else {
            for(KnownError knownError : knownErrorsEncountered){
                testCaseLog.log(LogLevel.INFO, "Known error '" + knownError.description + "' was encountered during test execution.");
            }
        }
    }

    /**
     * Evaluates test case execution testCaseLog to assess if the test case ran successfully, if known errors were encountered - or new errors. Return result status.
     */
    public void evaluateResultStatus(){
        ArrayList<LogPost> erroneousPosts = testCaseLog.onlyErroneousLogPosts();
        if(erroneousPosts.size() == 0) {
            resultStatus = ResultStatus.PASSED;
            String message = "   (•̀ᴗ•́)و    No deviations found for test case '" + testName + "'. Success! Passed.";
            System.out.println(message);
            testCaseLog.log(LogLevel.INFO, message);
            return;
        }
        identifyEncounteredKnownErrors();
        if(knownErrorsEncountered.size() > 0){
            for(LogPost logPost : testCaseLog.onlyErroneousLogPosts()){
                if(!logPost.identifiedToBePartOfKnownError){
                    String message = "   (⊙_◎)   Both new and known errors found for test case '" + testName + "'.";
                    resultStatus = ResultStatus.FAILED_WITH_BOTH_NEW_AND_KNOWN_ERRORS;
                    System.out.println(message);
                    testCaseLog.log(LogLevel.INFO, message);
                    TestRun.setExitCode(TestRun.ExitCodeTable.RUN_TEST_ERROR_MODERATE.getValue());
                    return;
                }
            }
            resultStatus = ResultStatus.FAILED_WITH_ONLY_KNOWN_ERRORS;
            String message = " ¯\\_(ツ)_/¯ Deviations found, but all of them was marked as known for test case '" + testName + "'.";
            testCaseLog.log(LogLevel.INFO, message);
            System.out.println(message);
        } else {
            resultStatus = ResultStatus.FAILED_WITH_ONLY_NEW_ERRORS;
            String message = "  ٩(͡๏̯͡๏)۶  Deviations not marked as known was found for test case '" + testName + "'.";
            System.out.println(message);
            testCaseLog.log(LogLevel.INFO, message);
            TestRun.setExitCode(TestRun.ExitCodeTable.RUN_TEST_ERROR_FATAL.getValue());
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
        private final int value;

        public int value() {
            return value;
        }
    }


}
