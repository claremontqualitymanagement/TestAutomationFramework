package se.claremont.autotest.common.reporting.testcasereports;

import se.claremont.autotest.common.logging.KnownError;
import se.claremont.autotest.common.logging.LogFolder;
import se.claremont.autotest.common.support.ColoredConsolePrinter;
import se.claremont.autotest.common.support.StringManagement;
import se.claremont.autotest.common.support.SupportMethods;
import se.claremont.autotest.common.support.ValuePair;
import se.claremont.autotest.common.testcase.TestCaseLogReporter;
import se.claremont.autotest.common.testcase.TestCaseResult;

/**
 * The text based (non-HTML) log file for the log posts of a test case
 *
 * Created by jordam on 2016-09-18.
 */
public class TestCaseLogReporterPureTextBasedLogFile implements TestCaseLogReporter {
    private final TestCaseResult testCaseResult;

    public TestCaseLogReporterPureTextBasedLogFile(TestCaseResult testCaseResult){
        this.testCaseResult = testCaseResult;
    }

    /**
     * Writes the test case pure text log to the log folder of the test run
     */
    public void report(){
        SupportMethods.saveToFile(pureTextLogText(), LogFolder.testRunLogFolder + testCaseResult.testName + ".txt");
    }

    /**
     * Prints the outcome of the test case execution to pure text based test runner console
     */
    private String pureTextLogText(){
        StringBuilder logText = new StringBuilder(SupportMethods.LF + "Test case '" + testCaseResult.testName + "'" + SupportMethods.LF + SupportMethods.LF);
        logText
                .append("Test status: ")
                .append(StringManagement.enumCapitalNameToFriendlyString(testCaseResult.resultStatus.toString()))
                .append(SupportMethods.LF)
                .append(SupportMethods.LF);
        if(testCaseResult.testCaseData.testCaseDataList.size() > 0){
            logText.append("Test case data:").append(SupportMethods.LF);
            for(ValuePair valuePair : testCaseResult.testCaseData.testCaseDataList){
                logText.append(valuePair.toString()).append(SupportMethods.LF);
            }
            logText.append(SupportMethods.LF);
        }
        if(testCaseResult.knownErrorsEncountered.size() > 0 ){
            logText.append("Known errors encountered:").append(SupportMethods.LF);
            for(KnownError knownError : testCaseResult.knownErrorsEncountered){
                logText.append(" * ").append(knownError.description).append(SupportMethods.LF);
            }
            logText.append(SupportMethods.LF);
        }
        logText.append("Test execution testCaseLog").append(SupportMethods.LF);
        logText.append(ColoredConsolePrinter.removeFormattingFromString(testCaseResult.testCaseLog.toString())).append(SupportMethods.LF);
        return logText.toString();
    }
}
