package se.claremont.autotest.testmanagementtoolintegration.testlink;

import se.claremont.autotest.common.LogLevel;
import se.claremont.autotest.common.TestCase;
import se.claremont.autotest.common.TestCaseLog;
import se.claremont.autotest.support.SupportMethods;
//import br.eti.kinoshita.testlinkjavaapi.util.TestLinkAPIException;
import testlink.api.java.client.TestLinkAPIClient;
import testlink.api.java.client.TestLinkAPIException;
import testlink.api.java.client.TestLinkAPIResults;


/**
 * Created by jordam on 2016-10-25.
 */
public class TestlinkReporter {
    public static String devKey;
    public static String url;
    boolean hasReportedConfigText = false;
    TestCaseLog environmentIssuesLog = null;
    TestLinkAPIClient api = null;

    public TestlinkReporter(String devKey, String url, TestCaseLog environmentIssuesLog){
        this.environmentIssuesLog = environmentIssuesLog;
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

    public void reportResult(String testProject, String buildName, TestCase testCase){
        if(this.api == null || !api.isConnected) {
            log(LogLevel.EXECUTION_PROBLEM, "No connection to Testlink API. Could not report test results for test case '" + testCase.testName + "' to Testlink.");
            testCase.log(LogLevel.EXECUTION_PROBLEM, "No connection to Testlink API. Could not report this test result to Testlink.");
            return;
        }
        if(testCase.resultStatus == TestCase.ResultStatus.UNEVALUATED){
            testCase.evaluateResultStatus();
        }
        try{
            reportResult(testProject, testCase.testSetName, testCase.testName, buildName, testCase.testCaseLog.toString(), testlinkResultStatusFromTestCaseResultStatus(testCase.resultStatus));
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

    public void reportResult(String testProject,String testPlan,String testCaseName, String build, String notes, String result) throws TestLinkAPIException{
        api.reportTestCaseResult(testProject, testPlan, testCaseName, build, notes, result);
        log(LogLevel.EXECUTED, "Reported results for test case '" + testCaseName + "' as '" + result + "' to Testlink.");
    }

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
