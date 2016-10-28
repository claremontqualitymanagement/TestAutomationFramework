package se.claremont.autotest.testmanagementtoolintegration.testlink;

import se.claremont.autotest.common.*;
import se.claremont.autotest.restsupport.JsonParser;
import se.claremont.autotest.support.SupportMethods;
import testlink.api.java.client.TestLinkAPIClient;
import testlink.api.java.client.TestLinkAPIException;
import testlink.api.java.client.TestLinkAPIHelper;
import testlink.api.java.client.TestLinkAPIResults;

import java.util.ArrayList;


/**
 * Class to report test results to open source test management tool Testlink
 *
 * Created by jordam on 2016-10-25.
 */
public class TestlinkReporter {
    ArrayList<String> projects = new ArrayList<>();
    public static String devKey;
    public static String url;
    String testProjectName = null;
    String buildName = null;
    String userName = null;
    boolean createTestCaseInTestlinkIfItDoesNotExistThere = false;
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
     * @param testlinkUserName The user name that creates test cases in Testlink if they don't exist.
     */
    public TestlinkReporter(String devKey, String url, TestCaseLog environmentIssuesLog, String testProjectName, String buildName, String testlinkUserName, boolean createTestCaseInTestlinkIfItDoesNotExistThere){
        String about = null;
        this.environmentIssuesLog = environmentIssuesLog;
        this.buildName = buildName;
        this.userName = testlinkUserName;
        this.testProjectName = testProjectName;
        this.createTestCaseInTestlinkIfItDoesNotExistThere = createTestCaseInTestlinkIfItDoesNotExistThere;
        this.devKey = devKey;
        this.url = url;
        try {
            api = new TestLinkAPIClient(devKey, url);
            //about = api.about().toString();
        } catch (Exception e) {
            log(LogLevel.EXECUTION_PROBLEM, "Could not connect to Testlink at url '" + url + "' with the supplied devKey '" + devKey + "'. " + e.getMessage());
            e.printStackTrace();
        }
/*        if(!api.isConnected){
            if(api.connectErrorMsg.contains("Unable to create a XML-RPC client.")){
                log(LogLevel.EXECUTION_PROBLEM, "Probably got the wrong url to Testlink RPC API? Current url = '" + url + "'.");
            }
            log(LogLevel.EXECUTION_PROBLEM, "Not connected to Testlink at url '" + url + "'. " + SupportMethods.LF + "Error msg: " + api.connectErrorMsg + SupportMethods.LF + "About: " + about + SupportMethods.LF + "Are you sure you have enabled the API in Testlink config?" + SupportMethods.LF + setupInformation());
            hasReportedConfigText = true;
        } else {
            log(LogLevel.INFO, "Connected to Testlink API at '" + url + "'. " + api.connectErrorMsg);
        }
  */
        this.projects = testlinkProjects();
    }

    @Deprecated
    public void report(){
        log(LogLevel.INFO, "The report() method of TestlinkReporter is not used. To report a test case result, use the evaluateTestCase() method.");
    }

    @Deprecated
    public void evaluateTestSet(TestSet testSet){
        log(LogLevel.INFO, "The evaluateTestSet() method of TestlinkReporter is not used. To report a test case result, use the evaluateTestCase() method.");
    }

    public String testlinkProjectsAndPlansListing(){
        StringBuilder sb = new StringBuilder(SupportMethods.LF);
        ArrayList<String> projects = testlinkProjects();
        for(String project : projects){
            sb.append("Project: '").append(project).append("'").append(SupportMethods.LF);
            ArrayList<String> plans = testlinkTestPlans(project);
            if(plans.size() == 0) continue;
            sb.append("   Plans in '" + project + "':").append(SupportMethods.LF);
            for (String plan : plans){
                sb.append("   > '").append(plan).append("'").append(SupportMethods.LF);
                ArrayList<String> suites = testlinkTestSuites(project, plan);
                if(suites.size() == 0) continue;
                sb.append("      Test suites in plan '" + plan + "':").append(SupportMethods.LF);
                for (String suite : suites){
                    sb.append("        > '").append(suite).append("'").append(SupportMethods.LF);
                }
            }
        }
        return sb.toString();
    }

    /**
     * Reports the result of a test case to Testlink.
     *
     * @param testCase The test case name in Testlink to report results to.
     */
    public void evaluateTestCase(String testProjectName, String testPlanName, String testSuiteName, String testName, TestCase testCase){
        log(LogLevel.INFO, "Evaluating test case '" + testCase.testName + "' in test suite '" + testCase.testSetName + "' for Testlink.");
        TestlinkTestResult testResult = new TestlinkTestResult(testProjectName, testPlanName, testSuiteName, testName, testCase);
        if(apiIsNotReady()) {
            reportApiProblem(testCase);
            //return;
        }
        evaluateTestCaseIfNotAlreadyDone(testCase);
        if(createTestCaseInTestlinkIfItDoesNotExistThere)
            createTestCaseInTestlinkIfNotExistThere(testCase, testResult);
        reportToLogIfTestCaseDoesNotExistInTestlink(testCase, testResult);
        if(testCaseExistInTestlink(testResult))
            tryReportResults(testCase, testResult);
    }

    private boolean testCaseExistInTestlink(TestlinkTestResult testlinkTestResult){
        try {
            api.getTestCaseIDByName(testlinkTestResult.testName, testlinkTestResult.testProjectName, testlinkTestResult.testSuiteName);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    private void reportToLogIfTestCaseDoesNotExistInTestlink(TestCase testCase, TestlinkTestResult testlinkTestResult){
        if(testCaseExistInTestlink(testlinkTestResult)){
            log(LogLevel.DEBUG, "Checking that test case '" + testlinkTestResult.testName + "' exists in Testlink. It does. Proceeding.");
            testCase.log(LogLevel.DEBUG, "Checking that this test case exists in Testlink. It does. Proceeding.");
        }else{
            log(LogLevel.EXECUTION_PROBLEM, "Could not report results for test automation test case '" + testCase.testName + "' in test set '" + testCase.testSetName + "' tried to report results to Testlink for the Testlink test case '" + testlinkTestResult.testName + "' in test suite '" + testlinkTestResult.testSuiteName + "' in test plan '" + testlinkTestResult.testPlanName + "' in test project '" + testlinkTestResult.testProjectName + "'.");
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not report results for this test case to Testlink. Instructions was to write results to Testlink test case '" + testlinkTestResult.testName + "' in test suite '" + testlinkTestResult.testSuiteName + "' in test plan '" + testlinkTestResult.testPlanName + "' in test project '" + testlinkTestResult.testProjectName + "', but it could not be found.");
        }
    }

    public ArrayList<String> testlinkProjects(){
        ArrayList<String> projects = new ArrayList<>();
        String queryResult = null;
        try {
            queryResult = api.getProjects().toString();
        } catch (TestLinkAPIException e) {
            e.printStackTrace();
        }
        for(String entry : queryResult.split(",")){
            if(entry.contains("=")){
                if(entry.split("=")[0].trim().toLowerCase().equals("name")){
                    projects.add(entry.split("=")[1].trim());
                }
            }
        }
        return projects;
    }

    public ArrayList<String> testlinkTestPlans(String projectName){
        ArrayList<String> testPlans = new ArrayList<>();
        String queryResult = "";
        try {
             queryResult = api.getProjectTestPlans(projectName).toString();
        } catch (TestLinkAPIException e) {
            e.printStackTrace();
        }
        for(String entry : queryResult.split(",")){
            if(entry.contains("=")){
                if(entry.split("=")[0].trim().toLowerCase().equals("name")){
                    testPlans.add(entry.split("=")[1].trim());
                }
            }
        }
        return testPlans;
    }


    public ArrayList<String> testlinkTestSuites(String projectName, String testPlanName){
        ArrayList<String> suites = new ArrayList<>();
        String queryResult = null;
        try {
            queryResult = api.getTestSuitesForTestPlan(projectName, testPlanName).toString();
        } catch (TestLinkAPIException e) {
            e.printStackTrace();
        }
        for(String entry : queryResult.split(",")){
            if(entry.contains("=")){
                if(entry.split("=")[0].trim().toLowerCase().equals("name")){
                    suites.add(entry.split("=")[1].trim());
                }
            }
        }
        return suites;
    }

    /**
     * Returns brief configuration information.
     *
     * @return Returns a brief text on how to set up Testlink to enable automation integration.
     */
    public String configInformation(){
        StringBuilder sb = new StringBuilder();
        sb.append("Check this out: 'ww.softwaretestinghelp.com/testlink-tutorial-3/'").append(SupportMethods.LF);
        sb.append("Basically:").append(SupportMethods.LF);
        sb.append("1). Install the Testlink test management tool somewhere where you have administrative rights enough to alter the config files of the installation.").append(SupportMethods.LF);
        sb.append("2). Enable Testlink API and automation integration on testlink server (edit config files and restart).").append(SupportMethods.LF);
        sb.append("3). Generate API reference access key in Testlink GUI, as admin.").append(SupportMethods.LF);
        sb.append("4). Change Testlink test cases to the 'Automated' status, and make sure your automated implementation of the test case reports the results to this test case using the reportResults() method.").append(SupportMethods.LF);
        return sb.toString();
    }

    private void createTestProjectInTestlinkIfNotExistThere(String testProjectName){
        if(testlinkProjects().contains(testProjectName)){
            log(LogLevel.DEBUG, "Verifying that test project '" + testProjectName + "' exist in Testlink, and it does.");
            return;
        }
        try{
            log(LogLevel.DEBUG, "Test project '" + testProjectName + "' is missing in Testlink. Trying to create it.");
            log(LogLevel.DEBUG, api.createTestProject(this.testProjectName, this.testProjectName.substring(0,1), "").toString());
            log(LogLevel.EXECUTED, "Created test project '" + testProjectName + "' in Testlink since it was missing.");
        }catch (Exception e){
            log(LogLevel.EXECUTION_PROBLEM, "Could not create missing test project '" + testProjectName + "' in Testlink. " + e.getMessage());
        }
    }

    private void createTestPlanInTestlinkIfNotExistThere(String testPlanName){
        createTestProjectInTestlinkIfNotExistThere(testProjectName);
        log(LogLevel.DEBUG, "Verifying that test plan '" + testPlanName + "' need to be created in Testlink test project '" + testProjectName + "'.");
        if(testlinkTestPlans(testProjectName).contains(testPlanName)) return;
        log(LogLevel.INFO, "Test plan '" + testPlanName + "' does not exist in test project '" + testProjectName + "' and should be created.");
    }

    private void createTestSuiteInTestlinkIfNotExistThere(String testSuiteName, String testPlanName){
        createTestPlanInTestlinkIfNotExistThere(testPlanName);
        if(testlinkTestSuites(testProjectName, testPlanName).contains(testSuiteName)) return;
        try {
            log(LogLevel.DEBUG, "Assessing if Testlink test suite '" + testSuiteName + "' need to be created in test plan '" + testPlanName + "' in test project '" + testProjectName + "'.");
            log(LogLevel.DEBUG, api.createTestSuite(testProjectName, testSuiteName, "Automatically created from test automation TAF.").toString());
            log(LogLevel.EXECUTED, "Created test suite '" + testSuiteName + "' in test plan '" + testPlanName + "' in Testlink test project '" + testProjectName + "'.");
        } catch (TestLinkAPIException e) {
            log(LogLevel.EXECUTION_PROBLEM, "Could not create test suite test suite '" + testSuiteName + "' in test plan '" + testPlanName + "' in Testlink test project '" + testProjectName + "'. " + e.getMessage());
        }
    }

    public Integer getProjectId(String testProjectName){
        Integer id = null;
        try{
            id = TestLinkAPIHelper.getProjectID(api, testProjectName);
        }catch (Exception ignored){
        }
        log(LogLevel.DEBUG, "Returning id '" + id.toString() + "' for test project '" + testProjectName + "'.");
        return id;
    }

    public Integer getTestSuiteId(String testProjectName, String testSuiteName){
        Integer id = null;
        try {
            id = TestLinkAPIHelper.getSuiteID(api, testProjectName, testSuiteName);
        }catch (Exception ignored){
        }
        log(LogLevel.DEBUG, "Returning id '" + id.toString() + "' for test suite '" + testSuiteName + "' in test project '" + testProjectName + "' in Testlink.");
        return id;

    }


    private void createTestCaseInTestlinkIfNotExistThere(TestCase testCase, TestlinkTestResult testlinkTestResult){
        createTestSuiteInTestlinkIfNotExistThere(testlinkTestResult.testSuiteName, testlinkTestResult.testPlanName);
        try {
            log(LogLevel.DEBUG, api.getTestCaseIDByName(testlinkTestResult.testName, testlinkTestResult.testProjectName, testlinkTestResult.testSuiteName).toString());
            log(LogLevel.DEBUG, "Verified that test case '" + testlinkTestResult.testName + "' exist in test suite '" + testlinkTestResult.testSuiteName + "' in test plan '" + testlinkTestResult.testPlanName + "' in test project '" + testlinkTestResult.testProjectName + "', and it does.");
        }catch (Exception e){
            try {
                //log(LogLevel.DEBUG, api.createTestCase(userName, getProjectId(testProjectName), getTestSuiteId(testProjectName, testlinkTestResult.testSuiteName), testlinkTestResult.testName, "Created by automation", "", "", (Integer)null, (Integer)null, false, (String)null, "Automated", "Medium").toString());
                log(LogLevel.DEBUG, api.createTestCase(userName, testlinkTestResult.testProjectName, testlinkTestResult.testSuiteName, testlinkTestResult.testName, "Test case automatically created by test automation execution.", "Step1", "ExpectedToPass", "Medium").toString());
                log(LogLevel.EXECUTED, "Creating test case '" + testlinkTestResult.testName + "' in Testlink (in test suite '" + testlinkTestResult.testSuiteName + "' and project '" + testlinkTestResult.testProjectName + "') since it didn't exist.");
            } catch (TestLinkAPIException e1) {
                log(LogLevel.EXECUTION_PROBLEM, "Tried to create test case in Testlink since the test case didn't exist. This did not work out as expected." + e1.getMessage());
            }
        }
    }

    private void tryReportResults(TestCase testCase, TestlinkTestResult testlinkTestResult){
        try{
            doReportResult(testlinkTestResult.testProjectName, testlinkTestResult.testPlanName, testlinkTestResult.testName, buildName, testCase.testCaseLog.toString(), testlinkResultStatusFromTestCaseResultStatus(testCase.resultStatus));
            testCase.log(LogLevel.EXECUTED, "Reported results to Testlink.");
        }catch (Exception e){
            log(LogLevel.EXECUTION_PROBLEM, "Error from TestlinkReporter: " + e.getMessage());
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Error from TestlinkReporter: " + e.getMessage());
            if(!hasReportedConfigText) {
                hasReportedConfigText = true;
                log(LogLevel.DEVIATION_EXTRA_INFO, configInformation() + SupportMethods.LF + testlinkProjectsAndPlansListing());
                testCase.log(LogLevel.DEVIATION_EXTRA_INFO, configInformation() + SupportMethods.LF + testlinkProjectsAndPlansListing());
            }
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

    class TestlinkTestResult{
        String testProjectName;
        String testPlanName;
        String testSuiteName;
        String testName;
        String notes;
        String resultStatus;

        public TestlinkTestResult(String testProjectName, String testPlanName, String testSuiteName, String testName, TestCase testCase){
            this.testProjectName = testProjectName;
            this.testPlanName = testPlanName;
            this.testSuiteName = testSuiteName;
            this.testName = testName;
            this.notes = testCase.testCaseLog.toString();
            this.resultStatus = testlinkResultStatusFromTestCaseResultStatus(testCase.resultStatus);
        }
    }
}
