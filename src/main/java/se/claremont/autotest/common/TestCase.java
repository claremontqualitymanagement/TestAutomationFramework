package se.claremont.autotest.common;

import org.junit.Assert;
import org.junit.Assume;
import se.claremont.autotest.guidriverpluginstructure.swingsupport.festswinggluecode.ApplicationManager;
import se.claremont.autotest.support.StringManagement;
import se.claremont.autotest.support.SupportMethods;
import se.claremont.taf.api.Taf;
import se.claremont.tools.Utils;

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
    public TestCaseLog testCaseLog;
    public String testName;
    String pathToHtmlLog;
    public final String testSetName;
    final Date startTime;
    Date stopTime;
    final TestCaseData testCaseData;
    final KnownErrorsList testCaseKnownErrorsList = new KnownErrorsList();
    private final KnownErrorsList testSetKnownErrorsEncounteredInThisTestCase = new KnownErrorsList();
    public ResultStatus resultStatus = ResultStatus.UNEVALUATED;
    private boolean reported = false;
    @SuppressWarnings("WeakerAccess")
    final KnownErrorsList testSetKnownErrors;
    private final ArrayList<TestCaseLogReporter> reporters = new ArrayList<>();
    final UUID uid = UUID.randomUUID();
    List<String> processesRunningAtTestCaseStart = new ArrayList<>();

    /**
     * Setting up a new test case run and prepares it for execution
     *
     *  @param knownErrorsList An instance of KnownErrorsList. Could be null.
     *  @param testName The name of the test. For reporting purposes.
     */
    public TestCase(KnownErrorsList knownErrorsList, String testName){
        if(knownErrorsList == null){
            knownErrorsList = new KnownErrorsList();
        }
        testCaseLog = new TestCaseLog(testName);
        testSetName = SupportMethods.classNameAtStacktraceLevel(4);
        testSetKnownErrors = knownErrorsList;
        this.testName = testName;
        testCaseData = new TestCaseData();
        addTestCaseData("Test case name", testName);
        startTime = new Date();
        testCaseLog.log(LogLevel.INFO, "Starting test execution at " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startTime) + ".");
        String memoryInfo = "Total memory available to JVM (bytes): " + Runtime.getRuntime().totalMemory() + ". ";
        long maxMemory = Runtime.getRuntime().maxMemory();
        memoryInfo += "Maximum memory (bytes): " + (maxMemory == Long.MAX_VALUE ? "no limit" : maxMemory) + ". ";
        testCaseLog.log(LogLevel.INFO, "Running tests as user '" + Taf.tafUserInfon().getUserAccountName() + "' on machine with " + Taf.tafUserInfon().getOperatingSystemName() + " as operating system (version reported as '" + Taf.tafUserInfon().getOperatingSystemVersion() + "', and architecture '" + Taf.tafUserInfon().getOperatingSystemArchitecture() +"') and " + Runtime.getRuntime().availableProcessors() + " processors. " + memoryInfo);
        reporters.add(new TestCaseLogReporterPureTextBasedLogFile(this));
        reporters.add(new TestCaseLogReporterHtmlLogFile(this));
        setLogFolderIfNotAlreadySet();
        ApplicationManager applicationManager = new ApplicationManager(this);
        processesRunningAtTestCaseStart = applicationManager.listActiveRunningProcessesOnLocalMachine();
    }

    /**
     * Compares the currently running processes on the executing machine with the ones that
     * were running when the test case started, and reports the difference to the test case
     * log in a log post.
     */
    public void writeProcessListDeviationsFromSystemStartToLog(){
        ApplicationManager applicationManager = new ApplicationManager(this);
        List<String> currentProcessesRunning = applicationManager.listActiveRunningProcessesOnLocalMachine();

        List<String> copyOfCurrentProcesses = new ArrayList<>();
        copyOfCurrentProcesses.addAll(currentProcessesRunning);
        List<String> copyOfProcessListAtStart = new ArrayList<>();
        copyOfProcessListAtStart.addAll(processesRunningAtTestCaseStart);

        for(int i = 0; i < copyOfCurrentProcesses.size(); i++){
            for(int j = 0; j < copyOfProcessListAtStart.size(); j++){
                if(copyOfProcessListAtStart.get(j).equals(copyOfCurrentProcesses.get(i))){
                    copyOfProcessListAtStart = remove(copyOfCurrentProcesses.get(i), copyOfProcessListAtStart);
                    copyOfCurrentProcesses = remove((copyOfCurrentProcesses.get(i)), copyOfCurrentProcesses);
                    if(j != 0) j--;
                    if(i != 0) i--;
                    break;
                }
            }
        }
        if(copyOfCurrentProcesses.get(0).equals(copyOfProcessListAtStart.get(0))){
            copyOfCurrentProcesses.remove(0);
            copyOfProcessListAtStart.remove(0);
        }
        //currentProcessesRunning.removeAll(copyOfProcessListAtStart);
        //copyOfProcessListAtStart.removeAll(applicationManager.listActiveRunningProcessesOnLocalMachine());
        StringBuilder sb = new StringBuilder();
        sb.append("Process(es) added since test case start: '").append(String.join("', '", copyOfCurrentProcesses)).append("'.").append(SupportMethods.LF);
        sb.append("Process(es) that has exited since test case start: '").append(String.join("', '", copyOfProcessListAtStart)).append("'.").append(SupportMethods.LF);
        if(copyOfProcessListAtStart.size() > 0 || copyOfCurrentProcesses.size() > 0){
            log(LogLevel.INFO, "Running process list deviation since test case start:" + SupportMethods.LF + sb.toString());
        } else {
            log(LogLevel.DEBUG, "No changes to what processes are running, from test case start until now, could be detected.");
        }


    }

    private List<String> remove(String string, List<String> list){
        boolean found = false;
        int index = 0;
        for(int i = 0; i < list.size(); i++){
            if(list.get(i).equals(string)){
                index = i;
                found = true;
                break;
            }
        }
        if(found){
            list.remove(index);
        }
        return list;
    }


    /**
     * Sets the log folder if no log folder is already set
     */
    private void setLogFolderIfNotAlreadySet(){
        LogFolder.setLogFolder(testSetName.replace(".", "_"));
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
        testCaseLog.log(LogLevel.DEBUG, "Evaluated test result status to '" + StringManagement.enumCapitalNameToFriendlyString(resultStatus.toString()) + "'.");
        TestRun.reporters.evaluateTestCase(this);
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
        testCaseData.testCaseDataList.add(new ValuePair(parameterName, parameterValue));
    }

    /**
     * Retrieves the parameter value for the given parameter name.
     * @param parameterName The name of the parameter to find the value for
     * @return Returns the parameter value for the given parameter
     */
    @SuppressWarnings("WeakerAccess")
    public List<String> valuesForTestCaseDataParameter(String parameterName){
        List<String> returnStrings = testCaseData.testCaseDataList.stream().filter(valuePair -> valuePair.parameter.equals(parameterName)).map(valuePair -> valuePair.value).collect(Collectors.toList());
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
    public enum ResultStatus{
        UNEVALUATED,
        PASSED,
        FAILED_WITH_ONLY_KNOWN_ERRORS,
        FAILED_WITH_BOTH_NEW_AND_KNOWN_ERRORS,
        FAILED_WITH_ONLY_NEW_ERRORS
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
     * Marks the test case status in the style of the test runner according to result status.
     * Also halts further test case execution.
     */
    private void assertExecutionResultsToTestRunner(){
        if(resultStatus == ResultStatus.UNEVALUATED)
            evaluateResultStatus();
        if(resultStatus == ResultStatus.FAILED_WITH_BOTH_NEW_AND_KNOWN_ERRORS || resultStatus == ResultStatus.FAILED_WITH_ONLY_NEW_ERRORS){
            Assert.assertFalse(SupportMethods.LF + testCaseLog.toString(), true);
            if( resultStatus == ResultStatus.FAILED_WITH_ONLY_NEW_ERRORS )
                TestRun.exitCode = TestRun.ExitCodeTable.RUN_TEST_ERROR_FATAL.getValue();
            else
                TestRun.exitCode = TestRun.ExitCodeTable.RUN_TEST_ERROR_MODERATE.getValue();
        } else if(resultStatus == ResultStatus.FAILED_WITH_ONLY_KNOWN_ERRORS){
            Assume.assumeTrue(false);
            Assert.assertFalse(SupportMethods.LF + testCaseLog.toString(), true);
            TestRun.exitCode = TestRun.ExitCodeTable.RUN_TEST_ERROR_MODERATE.getValue();
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

    public String toJson(){
        StringBuilder json = new StringBuilder();
        json.append("{\"testCaseRunInstance\": {").append(SupportMethods.LF);
        if(testSetName != null) json.append("   \"testSetName\": \"").append(testSetName).append("\",").append(SupportMethods.LF);
        if(testName != null) json.append("   \"testName\": \"").append(testName).append("\",").append(SupportMethods.LF);
        if(resultStatus != null) json.append("   \"status\": \"").append(StringManagement.enumCapitalNameToFriendlyString(resultStatus.toString())).append("\",").append(SupportMethods.LF);
        if(uid != null) json.append("   \"guid\": \"").append(uid.toString()).append("\",").append(SupportMethods.LF);
        if(pathToHtmlLog != null) json.append("   \"pathToHtmlLog\": \"").append(pathToHtmlLog.replace("\\", "\\\\")).append("\",").append(SupportMethods.LF);
        if(testCaseLog != null) json.append(testCaseLog.toJson()).append(",").append(SupportMethods.LF);
        json.append("   \"reported\": ").append(String.valueOf(reported)).append(",").append(SupportMethods.LF);
        if(startTime != null) json.append("   \"startTime\": \"").append(new SimpleDateFormat("yyyyMMdd HH:mm:ss").format(startTime)).append("\",").append(SupportMethods.LF);
        if(stopTime != null) json.append("   \"stopTime\": \"").append(new SimpleDateFormat("yyyyMMdd HH:mm:ss").format(stopTime)).append("\",").append(SupportMethods.LF);
        if(testCaseData != null) json.append(testCaseData.toJson()).append(",").append(SupportMethods.LF);
        if(testCaseKnownErrorsList != null) json.append("   \"testCaseKnownErrorsList\": ").append(testCaseKnownErrorsList.toJson()).append(",").append(SupportMethods.LF);
        if(testSetKnownErrorsEncounteredInThisTestCase != null) json.append("   \"testSetKnownErrorsEncounteredInThisTestCase\": ").append(testSetKnownErrorsEncounteredInThisTestCase.toJson()).append(",").append(SupportMethods.LF);
        if(testSetKnownErrors != null) json.append("   \"testSetKnownErrors\": ").append(testSetKnownErrors.toJson()).append(SupportMethods.LF);
        json.append("   }").append(SupportMethods.LF);
        json.append("}").append(SupportMethods.LF);
        return json.toString();
    }

    class TestCaseData {
        List<ValuePair> testCaseDataList = new ArrayList<>();

        public String toJson(){
            StringBuilder json = new StringBuilder();
            List<String> datastrings = new ArrayList<>();
            json.append("\"testCaseData\": [").append(SupportMethods.LF);
            for(ValuePair valuePair : testCaseDataList){
                datastrings.add("{" + SupportMethods.LF + "    \"parameter\": \"" + valuePair.parameter + "\"," + SupportMethods.LF + "\"value\": \"" + valuePair.value + "\"" + SupportMethods.LF + "}");
            }
            json.append(String.join("," + SupportMethods.LF, datastrings));
            json.append("    ]").append(SupportMethods.LF);
            return json.toString();
        }
    }
}
