package se.claremont.autotest.common;

import org.junit.Assert;
import org.junit.Assume;
import se.claremont.autotest.support.SupportMethods;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Test case information used during and after a test case execution.
 *
 * Created by jordam on 2016-08-17.
 */
public class TestCase {
    public final TestCaseLog testCaseLog;
    public String testName;
    String pathToHtmlLog;
    final String testSetName;
    final Date startTime;
    Date stopTime;
    final List<ValuePair> testCaseData;
    final KnownErrorsList testCaseKnownErrorsList = new KnownErrorsList();
    private final KnownErrorsList testSetKnownErrorsEncounteredInThisTestCase = new KnownErrorsList();
    ResultStatus resultStatus = ResultStatus.UNEVALUATED;
    private boolean reported = false;
    @SuppressWarnings("WeakerAccess")
    final KnownErrorsList testSetKnownErrors;
    private final ArrayList<TestCaseLogReporter> reporters = new ArrayList<>();
    final UUID uid = UUID.randomUUID();

    /**
     * Setting up a new test case run and prepares it for execution
     */
    public TestCase(KnownErrorsList knownErrorsList, String testName){
        testCaseLog = new TestCaseLog(testName);
        testSetName = SupportMethods.classNameAtStacktraceLevel(4);
        testSetKnownErrors = knownErrorsList;
        this.testName = testName;
        testCaseData = new ArrayList<>();
        addTestCaseData("Test case name", testName);
        startTime = new Date();
        testCaseLog.log(LogLevel.INFO, "Starting test execution at " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startTime) + ".");
        testCaseLog.log(LogLevel.INFO, "Running tests on machine with OS " + System.getProperty("OS.name"));
        reporters.add(new TestCaseLogReporterPureTextBasedLogFile(this));
        reporters.add(new TestCaseLogReporterHtmlLogFile(this));
        setLogFolderIfNotAlreadySet();
    }

    /**
     * Sets the log folder if no log folder is already set
     */
    private void setLogFolderIfNotAlreadySet(){
        LogFolder.setLogFolder(this.getClass().getName());
        pathToHtmlLog = LogFolder.testRunLogFolder + testName + ".html";
    }


    boolean isSameAs(Object object){
        if(object.getClass() != TestCase.class){ return false; }
        TestCase otherTestCase = (TestCase) object;
        return otherTestCase.uid.equals(this.uid);
    }

    /**
     * Interpreting test case results and writing to logs
     */
    public void report(){
        if(reported){
           return;
        }
        stopTime = new Date();
        testCaseLog.log(LogLevel.INFO, "Ending test execution at " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(stopTime) + ".");
        logKnownErrors();
        evaluateResultStatus();
        testCaseLog.log(LogLevel.DEBUG, "Evaluated test result status to '" + SupportMethods.enumCapitalNameToFriendlyString(resultStatus.toString()) + "'.");
        CliTestRunner.testRun.reporters.evaluateTestCase(this);
        reporters.forEach(TestCaseLogReporter::report);
        reported = true;
        assertExecutionResultsToTestRunner();
    }

    private void logKnownErrors(){
        testCaseLog.log(LogLevel.DEBUG, "Assessing test case testCaseLog for known errors.");
        testCaseKnownErrorsList.assessLogForKnownErrors(this);
        testCaseKnownErrorsList.knownErrors.stream().filter(KnownError::encountered).forEachOrdered(knownError -> testCaseLog.log(LogLevel.INFO, "Known error '" + knownError.description + "' encountered during execution."));
        for(KnownError knownError : testCaseKnownErrorsList.nonencounteredKnownErrors().knownErrors){
            testCaseLog.log(LogLevel.INFO, "Known error '" + knownError.description + "' was not encountered during test execution.");
        }

        //testSetKnownErrorsEncounteredInThisTestCase = KnownErrorsList.returnEncounteredKnownErrorsFromKnownErrorsListMatchedToLog(testSetKnownErrors, testCaseLog);

        testSetKnownErrorsEncounteredInThisTestCase.knownErrors.stream().filter(KnownError::encountered).forEachOrdered(knownError -> testCaseLog.log(LogLevel.INFO, "Known error from test set '" + knownError.description + "' encountered during execution."));
    }

    /**
     * Used to add a known error to a specific test case. All entered patterns must be matched for the known error to be fulfilled.
     *
     * @param description The friendly text string describing the known error
     * @param regexPatternsForLogRowsThatMustOccur The string patterns to find in the testCaseLog.
     */
    @SuppressWarnings("SameParameterValue")
    public void addKnownError(String description, String[] regexPatternsForLogRowsThatMustOccur){
        testCaseKnownErrorsList.add(new KnownError(description, regexPatternsForLogRowsThatMustOccur));
    }

    /**
     * Used to add a known error to a specific test case.
     *
     * @param description The friendly text string describing the known error
     * @param regexPatternForLogRow The string patterns to find in the testCaseLog.
     */
    public void addKnownError(String description, String regexPatternForLogRow){
        testCaseKnownErrorsList.add(new KnownError(description, regexPatternForLogRow));
    }

    /**
     * Adds some test case DATA that follows the test case and can be retrieved at any point during execution.
     * Test case DATA is used in parameters with a parameter name and a string based parameter value.
     *
     * @param parameterName Parameter name
     * @param parameterValue Parameter value
     */
    public void addTestCaseData(String parameterName, String parameterValue){
        testCaseLog.log(LogLevel.DEBUG, "Adding test case DATA parameter '" + parameterName + "' with parameter value '" + parameterValue + "'.");
        testCaseData.add(new ValuePair(parameterName, parameterValue));
    }

    /**
     * Retrieves the parameter value for the given parameter name.
     * @param parameterName The name of the parameter to find the value for
     * @return Returns the parameter value for the given parameter
     */
    @SuppressWarnings("WeakerAccess")
    public List<String> valuesForTestCaseDataParameter(String parameterName){
        List<String> returnStrings = testCaseData.stream().filter(valuePair -> valuePair.parameter.equals(parameterName)).map(valuePair -> valuePair.value).collect(Collectors.toList());
        if(returnStrings.size() > 0){
            String logString = "Reading parameter values ";
            for(String returnString : returnStrings){
                logString = logString + "'" + returnString + "', ";
            }
            logString = logString + "for parameter '" + parameterName + "'.";
            testCaseLog.log(LogLevel.DEBUG, logString);
        }else{
            testCaseLog.log(LogLevel.DEBUG, "Reading test case DATA but could not find any values for parameter '" + parameterName + "'.");
        }
        return returnStrings;
    }

    /**
     * Retrieves the parameter value for the given parameter name.
     * @param parameterName The name of the parameter to return value of
     * @return Return the value of the first encountered parameter with stated name
     */
    public String valueForFirstMatchForTestCaseDataParameter(String parameterName){
        String returnString = "";
        List<String> values = valuesForTestCaseDataParameter(parameterName);
        if(values.size() > 0){
            returnString = values.get(0);
            testCaseLog.log(LogLevel.DEBUG, "Reading first match '" + returnString + "' for parameter name '" + parameterName + "'.");
        }else {
            testCaseLog.log(LogLevel.DEBUG, "Reading test case DATA but could not find any values for parameter '" + parameterName + "'.");
        }
        return returnString;
    }

    /**
     * When a test case is evaluated after a test run the result status is set
     */
    enum ResultStatus{
        UNEVALUATED,
        PASSED,
        FAILED_WITH_ONLY_KNOWN_ERRORS,
        FAILED_WITH_BOTH_NEW_AND_KNOWN_ERRORS,
        FAILED_WITH_ONLY_NEW_ERRORS
    }

    /**
     * Evaluates test case execution testCaseLog to assess if the test case ran successfully, if known errors were encountered - or new errors. Return result status.
     */
    void evaluateResultStatus(){
        ArrayList<LogPost> erroneousPosts = testCaseLog.onlyErroneousLogPosts();
        if(erroneousPosts.size() == 0) {
            resultStatus = ResultStatus.PASSED;
            return;
        }

        testCaseKnownErrorsList.assessLogForKnownErrors(this);
        testSetKnownErrors.assessLogForKnownErrors(this);
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
            resultStatus = ResultStatus.FAILED_WITH_BOTH_NEW_AND_KNOWN_ERRORS;
        } else if (newErrorsEncountered){
            resultStatus = ResultStatus.FAILED_WITH_ONLY_NEW_ERRORS;
        } else {
            resultStatus = ResultStatus.FAILED_WITH_ONLY_KNOWN_ERRORS;
        }
    }

    /**
     * Marks the test case status in the style of the test runner according to result status.
     * Also halts further test case execution.
     */
    private void assertExecutionResultsToTestRunner(){
        if(resultStatus == ResultStatus.UNEVALUATED) evaluateResultStatus();
        if(resultStatus == ResultStatus.FAILED_WITH_BOTH_NEW_AND_KNOWN_ERRORS || resultStatus == ResultStatus.FAILED_WITH_ONLY_NEW_ERRORS){
            Assert.assertFalse(testCaseLog.toString(), true);
        } else if(resultStatus == ResultStatus.FAILED_WITH_ONLY_KNOWN_ERRORS){
            Assume.assumeTrue(false);
            Assert.assertFalse(testCaseLog.toString(), true);
        }

    }


    /**
     * Writes an entry to the log of the test case
     *
     * @param logLevel Log level for the log post
     * @param message Log message for the log post
     */
    public void log(LogLevel logLevel, String message){
        testCaseLog.log(logLevel, message);
    }

    /**
     * Some log posts is best logged with different strings for html log and text based log.
     *
     * @param logLevel Log level of log post
     * @param pureTestLogMessage Text string for pure text log
     * @param htmlFormattedLogMessage HTML formatted text string for HTML test case log
     */
    @SuppressWarnings("SameParameterValue")
    public void logDifferentlyToTextLogAndHtmlLog(LogLevel logLevel, String pureTestLogMessage, String htmlFormattedLogMessage){
        testCaseLog.logDifferentlyToTextLogAndHtmlLog(logLevel, pureTestLogMessage, htmlFormattedLogMessage);
    }

    /**
     * Friendly representation of the test case as a string.
     *
     * @return A string describing the relevant test case information
     */
    public @Override String toString(){
        return testName + " in class " + testSetName + " with testCaseLog " + testCaseLog.toString();
    }
}
