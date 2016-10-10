package se.claremont.autotest.common;

import se.claremont.autotest.support.SupportMethods;

/**
 * The text based (non-HTML) log file for the log posts of a test case
 *
 * Created by jordam on 2016-09-18.
 */
public class TestCaseLogReporterPureTextBasedLogFile implements TestCaseLogReporter {
    private final TestCase testCase;

    TestCaseLogReporterPureTextBasedLogFile(TestCase testCase){
        this.testCase = testCase;
    }

    /**
     * Writes the test case pure text log to the log folder of the test run
     */
    public void report(){
        SupportMethods.saveToFile(pureTextLogText(), LogFolder.testRunLogFolder + testCase.testName + ".txt");
    }

    /**
     * Prints the outcome of the test case execution to pure text based test runner console
     */
    private String pureTextLogText(){
        StringBuilder logText = new StringBuilder(SupportMethods.LF + "Test case '" + testCase.testName + "'" + SupportMethods.LF + SupportMethods.LF);
        logText.append("Test status: ").append(SupportMethods.enumCapitalNameToFriendlyString(testCase.resultStatus.toString())).append(SupportMethods.LF).append(SupportMethods.LF);
        if(testCase.testCaseData.testCaseDataList.size() > 0){
            logText.append("Test case data:").append(SupportMethods.LF);
            for(ValuePair valuePair : testCase.testCaseData.testCaseDataList){
                logText.append(valuePair.toString()).append(SupportMethods.LF);
            }
            logText.append(SupportMethods.LF);
        }
        if(testCase.testCaseKnownErrorsList.encounteredKnownErrors().size() > 0 ){
            logText.append("Known errors encountered:").append(SupportMethods.LF);
            for(KnownError knownError : testCase.testCaseKnownErrorsList.encounteredKnownErrors().knownErrors){
                logText.append(" * ").append(knownError.description).append(SupportMethods.LF);
            }
            logText.append(SupportMethods.LF);
        }
        logText.append("Test execution testCaseLog").append(SupportMethods.LF);
        logText.append(testCase.testCaseLog.toString()).append(SupportMethods.LF);
        return logText.toString();
    }
}
