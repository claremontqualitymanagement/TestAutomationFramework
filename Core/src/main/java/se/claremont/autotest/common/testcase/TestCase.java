package se.claremont.autotest.common.testcase;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Rule;
import org.junit.rules.TestName;
import se.claremont.autotest.common.logging.KnownError;
import se.claremont.autotest.common.logging.KnownErrorsList;
import se.claremont.autotest.common.logging.LogFolder;
import se.claremont.autotest.common.logging.LogLevel;
import se.claremont.autotest.common.logging.logmessage.LogMessage;
import se.claremont.autotest.common.reporting.testcasereports.TestCaseLogReporterHtmlLogFile;
import se.claremont.autotest.common.reporting.testcasereports.TestCaseLogReporterPureTextBasedLogFile;
import se.claremont.autotest.common.support.ApplicationManager;
import se.claremont.autotest.common.support.SupportMethods;
import se.claremont.autotest.common.support.ValuePair;
import se.claremont.autotest.common.support.api.Taf;
import se.claremont.autotest.common.testrun.TestRun;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Test case information used during and after a test case execution.
 *
 * Created by jordam on 2016-08-17.
 */
@SuppressWarnings("WeakerAccess")
@JsonIgnoreProperties({"reported", "reporters", "processesRunningAtTestCaseStart", "currentTestStepName", "testCaseMethodName", "currentTestNameInternal"})
public class TestCase {
    public TestCaseResult testCaseResult;
    @JsonProperty public String testName;
    @JsonProperty public final String testSetName;
    @JsonProperty public final KnownErrorsList testCaseKnownErrorsList;
    @JsonProperty public final KnownErrorsList testSetKnownErrors;
    @JsonProperty public final UUID uid = UUID.randomUUID();
    public final String testCaseMethodName;
    final ArrayList<TestCaseLogReporter> reporters = new ArrayList<>();
    private boolean reported = false;
    List<String> processesRunningAtTestCaseStart = new ArrayList<>();
    @JsonProperty public String pathToHtmlLogFile;
    @Rule
    public TestName currentTestNameInternal = new TestName();

    public TestCase(){
        this(null, "Nameless test case");
    }

    public TestCase(String testName){
        this(null, testName);
    }

    public TestCase(KnownErrorsList knownErrorsList, String testName){
        this(knownErrorsList, testName, SupportMethods.classNameAtStacktraceLevel(4));
    }

    /**
     * Setting up a new test case run and prepares it for execution
     *
     *  @param knownErrorsList An instance of KnownErrorsList. Could be null. Used for test set level known errors.
     *  @param testName The name of the test. For reporting purposes.
     */
    public TestCase(KnownErrorsList knownErrorsList, String testName, String testSetName){
        TestRun.initializeIfNotInitialized();
        this.testCaseMethodName = testName;
        this.testSetName = testSetName;
        if(testName == null) testName = "Nameless test case";
        if(knownErrorsList == null) knownErrorsList = new KnownErrorsList();
        testCaseKnownErrorsList = new KnownErrorsList();
        if(knownErrorsList == null) knownErrorsList = new KnownErrorsList();
        testSetKnownErrors = knownErrorsList;
        this.testName = testName;
        setLogFolderIfNotAlreadySet();
        testCaseResult = new TestCaseResult(this);
        addTestCaseData("Test case name", testName);
        String memoryInfo = "Total memory available to JVM (bytes): " + Runtime.getRuntime().totalMemory() + ". ";
        long maxMemory = Runtime.getRuntime().maxMemory();
        memoryInfo += "Maximum memory (bytes): " + (maxMemory == Long.MAX_VALUE ? "no limit" : maxMemory) + ". ";
        testCaseResult.testCaseLog.log(LogLevel.INFO, "Running tests as user '" + Taf.tafUserInfon().getUserAccountName() + "' on machine with " + Taf.tafUserInfon().getOperatingSystemName() + " as operating system (version reported as '" + Taf.tafUserInfon().getOperatingSystemVersion() + "', and architecture '" + Taf.tafUserInfon().getOperatingSystemArchitecture() +"') and " + Runtime.getRuntime().availableProcessors() + " processors. " + memoryInfo);
        reporters.add(new TestCaseLogReporterPureTextBasedLogFile(testCaseResult));
        reporters.add(new TestCaseLogReporterHtmlLogFile(testCaseResult));
        ApplicationManager applicationManager = new ApplicationManager(this);
        processesRunningAtTestCaseStart = applicationManager.listActiveRunningProcessesOnLocalMachine();
    }


    public void setName(String name){
        log(LogLevel.DEBUG, "Resetting name for test case from '" + testName + "' to '" + name + "'.");
        this.testName = name;
    }

    /**
     * Compares the currently running processes on the executing machine with the ones that
     * were running when the test case started, and reports the difference to the test case
     * log in a log post.
     */
    public void writeProcessListDeviationsFromSystemStartToLog(){
        ApplicationManager applicationManager = new ApplicationManager(this);
        List<String> currentProcessesRunning = applicationManager.listActiveRunningProcessesOnLocalMachine();
        HashSet<String> copyOfCurrentProcesses = new HashSet<>(currentProcessesRunning);
        HashSet<String> copyOfProcessListAtStart = new HashSet<>(processesRunningAtTestCaseStart);

        copyOfCurrentProcesses.removeAll(processesRunningAtTestCaseStart);
        copyOfProcessListAtStart.removeAll(processesRunningAtTestCaseStart);

        StringBuilder sb = new StringBuilder();
        sb.append("Process(es) added since test case start: '").append(String.join("', '", copyOfCurrentProcesses)).append("'.").append(SupportMethods.LF);
        sb.append("Process(es) that has exited since test case start: '").append(String.join("', '", copyOfProcessListAtStart)).append("'.").append(SupportMethods.LF);
        if(copyOfProcessListAtStart.isEmpty() && copyOfCurrentProcesses.isEmpty()){
            log(LogLevel.DEBUG, "No changes to what processes are running, from test case start until now, could be detected.");
        } else {
            log(LogLevel.INFO, "Running process list deviation since test case start:" + SupportMethods.LF + sb.toString());
        }


    }

    /**
     * Sets the log folder if no log folder is already set
     */
    private void setLogFolderIfNotAlreadySet(){
        LogFolder.setLogFolder(testSetName.replace(".", "_"));
        pathToHtmlLogFile = LogFolder.testRunLogFolder + testName + ".html";
    }


    public boolean isSameAs(Object object){
        if(object.getClass() != TestCase.class){ return false; }
        TestCase otherTestCase = (TestCase) object;
        return otherTestCase.uid.equals(this.uid);
    }

    /**
     * Interpreting test case results and writing to logs
     */
    public void report(){
        if(reported) return;
        testCaseResult.assessResults();
        TestRun.reporters.evaluateTestCase(this);
        reporters.forEach(TestCaseLogReporter::report);
        reported = true;
        assertExecutionResultsToTestRunner();
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
        testCaseResult.testCaseLog.log(LogLevel.DEBUG, "Adding test case DATA parameter '" + parameterName + "' with parameter value '" + parameterValue + "'.");
        testCaseResult.testCaseData.testCaseDataList.add(new ValuePair(parameterName, parameterValue));
    }

    /**
     * Retrieves the parameter value for the given parameter name.
     * @param parameterName The name of the parameter to find the value for
     * @return Returns the parameter value for the given parameter
     */
    @SuppressWarnings("WeakerAccess")
    public List<String> valuesForTestCaseDataParameter(String parameterName){
        List<String> returnStrings = testCaseResult.testCaseData.testCaseDataList.stream().filter(valuePair -> valuePair.parameter.equals(parameterName)).map(valuePair -> valuePair.value).collect(Collectors.toList());
        if(returnStrings.size() > 0){
            StringBuilder logString = new StringBuilder("Reading parameter values ");
            for(String returnString : returnStrings){
                logString.append("'").append(returnString).append("', ");
            }
            logString.append("for parameter '").append(parameterName).append("'.");
            testCaseResult.testCaseLog.log(LogLevel.DEBUG, logString.toString());
        }else{
            testCaseResult.testCaseLog.log(LogLevel.DEBUG, "Reading test case DATA but could not find any values for parameter '" + parameterName + "'.");
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
            testCaseResult.testCaseLog.log(LogLevel.DEBUG, "Reading first match '" + returnString + "' for parameter name '" + parameterName + "'.");
        }else {
            testCaseResult.testCaseLog.log(LogLevel.DEBUG, "Reading test case DATA but could not find any values for parameter '" + parameterName + "'.");
        }
        return returnString;
    }



    /**
     * Writes an entry to the log of the test case
     *
     * @param logLevel Log level for the log post
     * @param message Log message for the log post
     */
    public void log(LogLevel logLevel, String message){
        testCaseResult.testCaseLog.log(logLevel, message);
    }

    public String getCurrentTestStepName(){
        return TestCaseLog.getCurrentTestStepName(testName);
    }

    @SuppressWarnings("unused")
    public void log(LogLevel logLevel, LogMessage logMessage){
        testCaseResult.testCaseLog.log(logLevel, logMessage);
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
        testCaseResult.testCaseLog.logDifferentlyToTextLogAndHtmlLog(logLevel, pureTestLogMessage, htmlFormattedLogMessage);
    }

    /**
     * Friendly representation of the test case as a string.
     *
     * @return A string describing the relevant test case information
     */
    public @Override String toString(){
        return testName + " in class " + testSetName + " with testCaseLog " + testCaseResult.testCaseLog.toString();
    }

    public String toJson(){
        ObjectMapper mapper = new ObjectMapper();
        String json = null;
        try {
            json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
            if(json == null) System.out.println("Oups! Trying to create json from test case failed. Test case: " + this.toString());
        } catch (JsonProcessingException e) {
            System.out.println(e.toString());
        }
        return json;
    }

    /**
     * Marks the test case status in the style of the test runner according to result status.
     * Also halts further test case execution.
     */
    private void assertExecutionResultsToTestRunner(){
        if(testCaseResult.resultStatus == TestCaseResult.ResultStatus.UNEVALUATED) testCaseResult.evaluateResultStatus();
        if(testCaseResult.resultStatus == TestCaseResult.ResultStatus.FAILED_WITH_BOTH_NEW_AND_KNOWN_ERRORS || testCaseResult.resultStatus == TestCaseResult.ResultStatus.FAILED_WITH_ONLY_NEW_ERRORS){
            //noinspection ConstantConditions
            Assert.assertFalse(SupportMethods.LF + testCaseResult.testCaseLog.toString(), true);
            if( testCaseResult.resultStatus == TestCaseResult.ResultStatus.FAILED_WITH_ONLY_NEW_ERRORS )
                TestRun.exitCode = TestRun.ExitCodeTable.RUN_TEST_ERROR_FATAL.getValue();
            else
                TestRun.exitCode = TestRun.ExitCodeTable.RUN_TEST_ERROR_MODERATE.getValue();
        } else if(testCaseResult.resultStatus == TestCaseResult.ResultStatus.FAILED_WITH_ONLY_KNOWN_ERRORS){
            //noinspection ConstantConditions
            Assume.assumeTrue(false);
            Assert.assertFalse(SupportMethods.LF + testCaseResult.testCaseLog.toString(), true);
            TestRun.exitCode = TestRun.ExitCodeTable.RUN_TEST_ERROR_MODERATE.getValue();
        }
    }


}
