package se.claremont.autotest.testmanagementtoolintegration.testlink;

import se.claremont.autotest.common.*;
import se.claremont.autotest.support.SupportMethods;
import testlink.api.java.client.TestLinkAPIClient;
import testlink.api.java.client.TestLinkAPIException;
import testlink.api.java.client.TestLinkAPIResults;


/**
 * Class to report test results to open source test management tool Testlink
 *
 * Created by jordam on 2016-10-25.
 */
public class TestlinkReporter implements TestRunReporter {
    public static String devKey;
    public static String url;
    String testProjectName = null;
    String buildName = null;
    String userName = null;
    boolean hasReportedConfigText = false;
    TestCaseLog environmentIssuesLog = null;
    TestLinkAPIClient api = null;

    /**
     * Setup connection
     *
     * @param devKey The API key that can be generated from within Testlink GUI (My settings), for a specified user.
     * @param url The url to the testlink installation API, for example 'http://127.0.0.1/testlink/lib/api/xmlrpc/v1/xmlrpc.php'
     * @param environmentIssuesLog A possible log to report environment log posts to - prefferably for a full test run.
     * @param testProjectName The project name in Testlink where the test case can be found.
     * @param buildName A build name to pass to Testlink test results, e.g. 'AutomationExecution'.
     * @param userName The user name that creates test cases in Testlink if they don't exist.
     */
    public TestlinkReporter(String devKey, String url, TestCaseLog environmentIssuesLog, String testProjectName, String buildName, String userName){
        this.environmentIssuesLog = environmentIssuesLog;
        this.buildName = buildName;
        this.testProjectName = testProjectName;
        this.devKey = devKey;
        this.url = url;
        try {
            api = new TestLinkAPIClient(devKey, url);
        } catch (Exception e) {
            log(LogLevel.EXECUTION_PROBLEM, "Could not connect to Testlink at url '" + url + "' with the supplied devKey '" + devKey + "'. " + e.getMessage());
        }
        if(!api.isConnected){
            log(LogLevel.EXECUTION_PROBLEM, "Not connected to Testlink at url '" + url + "'. Are you sure you have enabled the API in Testlink config?" + SupportMethods.LF + setupInformation());
            hasReportedConfigText = true;
        } else {
            log(LogLevel.INFO, "Connected to Testlink API at '" + url + "'. " + api.connectErrorMsg);
        }
    }

    @Deprecated
    public void report(){
        log(LogLevel.INFO, "The report() method of TestlinkReporter is not used. To report a test case result, use the evaluateTestCase() method.");
    }

    @Deprecated
    public void evaluateTestSet(TestSet testSet){
        log(LogLevel.INFO, "The evaluateTestSet() method of TestlinkReporter is not used. To report a test case result, use the evaluateTestCase() method.");
    }

    /**
     * Reports the result of a test case to Testlink.
     *
     * @param testCase The test case name in Testlink to report results to.
     */
    public void evaluateTestCase(TestCase testCase){
        if(apiIsNotReady()) {
            reportApiProblem(testCase);
            return;
        }
        evaluateTestCaseIfNotAlreadyDone(testCase);
        createTestCaseInTestlinkIfNotExistThere(testCase);
        tryReportResults(testCase);
    }

    /**
     * Returns brief configuration information.
     *
     * @return Returns a brief text on how to set up Testlink to enable automation integration.
     */
    public String setupInformation(){
        StringBuilder sb = new StringBuilder();
        sb.append("Check this out: 'ww.softwaretestinghelp.com/testlink-tutorial-3/'").append(SupportMethods.LF);
        sb.append("Basically:").append(SupportMethods.LF);
        sb.append("1). Install the Testlink test management tool somewhere where you have administrative rights enough to alter the config files of the installation.").append(SupportMethods.LF);
        sb.append("2). Enable Testlink API and automation integration on testlink server (edit config files and restart).").append(SupportMethods.LF);
        sb.append("3). Generate API reference access key in Testlink GUI, as admin.").append(SupportMethods.LF);
        sb.append("4). Change Testlink test cases to the 'Automated' status, and make sure your automated implementation of the test case reports the results to this test case using the reportResults() method.").append(SupportMethods.LF);
        return sb.toString();
    }

    private void createTestCaseInTestlinkIfNotExistThere(TestCase testCase){
        try {
            api.getTestCaseIDByName(testProjectName, testCase.testName, testCase.testSetName);
        }catch (Exception e){
            try {
                api.createTestCase(userName, testProjectName, testCase.testSetName, testCase.testName, "Test case automatically created by test automation execution.", "Step1", "ExpectedToPass", "Medium");
                log(LogLevel.EXECUTED, "Creating test case '" + testCase.testName + "' in Testlink (in test suite '" + testCase.testSetName + "' and project '" + testProjectName + "').");
            } catch (TestLinkAPIException e1) {
                log(LogLevel.EXECUTION_PROBLEM, "Tried to create test case in Testlink since the test case didn't exist. This did not work out as expected." + e1.getMessage());
            }
        }

    }

    private void tryReportResults(TestCase testCase){
        try{
            doReportResult(testProjectName, testCase.testSetName, testCase.testName, buildName, testCase.testCaseLog.toString(), testlinkResultStatusFromTestCaseResultStatus(testCase.resultStatus));
            testCase.log(LogLevel.EXECUTED, "Reported results to Testlink.");
        }catch (Exception e){
            if(!hasReportedConfigText) {
                log(LogLevel.DEVIATION_EXTRA_INFO, setupInformation());
                testCase.log(LogLevel.DEVIATION_EXTRA_INFO, setupInformation());
                hasReportedConfigText = true;
            }
            log(LogLevel.EXECUTION_PROBLEM, "Error from TestlinkReporter: " + e.getMessage());
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Error from TestlinkReporter: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void doReportResult(String testProject,String testPlan,String testCaseName, String build, String notes, String result) throws TestLinkAPIException{
        api.reportTestCaseResult(testProject, testPlan, testCaseName, build, notes, result);
        log(LogLevel.EXECUTED, "Reported results for test case '" + testCaseName + "' as '" + result + "' to Testlink.");
    }

    private void evaluateTestCaseIfNotAlreadyDone(TestCase testCase){
        if(testCase.resultStatus == TestCase.ResultStatus.UNEVALUATED)
            testCase.evaluateResultStatus();
    }

    private boolean apiIsNotReady(){
        if(this.api == null || !api.isConnected) {
            return true;
        }
        return false;
    }

    private void reportApiProblem(TestCase testCase){
        log(LogLevel.EXECUTION_PROBLEM, "No connection to Testlink API. Could not report test results for test case '" + testCase.testName + "' to Testlink.");
        testCase.log(LogLevel.EXECUTION_PROBLEM, "No connection to Testlink API. Could not report this test result to Testlink.");
    }

    private static String testlinkResultStatusFromTestCaseResultStatus(TestCase.ResultStatus resultStatus){
        if(resultStatus.equals(TestCase.ResultStatus.PASSED)) return TestLinkAPIResults.TEST_PASSED;
        if(resultStatus.equals(TestCase.ResultStatus.FAILED_WITH_ONLY_KNOWN_ERRORS) || resultStatus.equals(TestCase.ResultStatus.UNEVALUATED)) return TestLinkAPIResults.TEST_WRONG;
        return TestLinkAPIResults.TEST_FAILED;
    }

    private void log(LogLevel logLevel, String message){
        if(this.environmentIssuesLog != null) {
            environmentIssuesLog.log(logLevel, message);
        }else {
            System.out.println(logLevel.toString() + ": " + message);
        }
    }
}
