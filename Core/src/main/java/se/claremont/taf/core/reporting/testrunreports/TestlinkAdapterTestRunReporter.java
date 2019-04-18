package se.claremont.taf.core.reporting.testrunreports;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import se.claremont.taf.core.backendserverinteraction.TestlinkAdapterServerConnection;
import se.claremont.taf.core.logging.LogLevel;
import se.claremont.taf.core.testcase.TestCase;
import se.claremont.taf.core.testrun.Settings;
import se.claremont.taf.core.testrun.TestRun;
import se.claremont.taf.core.testrun.reportingengine.TestRunReporter;
import se.claremont.taf.core.testset.TestSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Collects test run information during a test run, and helps send it to a TAF Testlink Adapter Server.
 *
 * Created by jordam on 2017-03-19.
 */
public class TestlinkAdapterTestRunReporter implements TestRunReporter {
    @SuppressWarnings("WeakerAccess")
    @JsonProperty public final List<String> testCasesJsonsList = new ArrayList<>();
    private final TestlinkTestCasesFromTestRun testlinkTestCasesFromTestRun = new TestlinkTestCasesFromTestRun();

    public TestlinkAdapterTestRunReporter(){
    }

    public void evaluateTestCase(TestCase testCase){
        if(testCase == null)return;
        testCase.log(LogLevel.INFO, "Logging test case results for test case '" + testCase.testName + "' to Testlink Adapter server at '" + TestRun.getSettingsValue(Settings.SettingParameters.URL_TO_TESTLINK_ADAPTER) + "'.");
        testCasesJsonsList.add(testCase.toJson());
        testlinkTestCasesFromTestRun.testCases.add(new TestlinkTestCaseMapper(testCase));
    }

    public void report(){
        if(TestRun.getSettingsValue(Settings.SettingParameters.URL_TO_TESTLINK_ADAPTER).equals(TestlinkAdapterServerConnection.defaultServerUrl)) return;
        System.out.println(System.lineSeparator() + "Sending test run results to Testlink Adapter server at '" + TestRun.getSettingsValue(Settings.SettingParameters.URL_TO_TESTLINK_ADAPTER) + "'." + System.lineSeparator());
        TestlinkAdapterServerConnection testlinkAdapterServerConnection = new TestlinkAdapterServerConnection();
        testlinkAdapterServerConnection.postTestRunResult(testlinkTestCasesFromTestRun.toJson());
    }

    public void evaluateTestSet(TestSet testSet){
        //No need to evaluate test set for Testlink reporting.
    }

    @SuppressWarnings("WeakerAccess")
    public String toJson(){
        ObjectMapper mapper = new ObjectMapper();
        String json = null;
        try {
            json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            System.out.println(e.toString());
        }
        return json;
    }

    class TestlinkTestCasesFromTestRun {
        @JsonProperty final ArrayList<TestlinkTestCaseMapper> testCases = new ArrayList<>();

        @SuppressWarnings("WeakerAccess")
        public String toJson(){
            ObjectMapper mapper = new ObjectMapper();
            String json = null;
            try {
                json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
            } catch (JsonProcessingException e) {
                System.out.println(e.toString());
            }
            return json;
        }
    }

    class TestlinkTestCaseMapper{
        @JsonProperty String testName;
        @JsonProperty String testSetName;
        @JsonProperty String notes;
        @JsonProperty String executionStatus;

        public TestlinkTestCaseMapper(){} //For mapper

        public TestlinkTestCaseMapper(String testName, String testSetName, String notes, String executionStatus){
            this.testName = testName;
            this.testSetName = testSetName;
            this.notes = notes;
            this.executionStatus = executionStatus;
        }

        TestlinkTestCaseMapper(TestCase testCase){
            this.testName = testCase.testName;
            this.testSetName = testCase.testSetName;
            this.notes = testCase.testCaseResult.testCaseLog.toString();
            this.executionStatus = testCase.testCaseResult.resultStatus.toString();
        }

        @SuppressWarnings("WeakerAccess")
        public String toJson(){
            ObjectMapper mapper = new ObjectMapper();
            String json = null;
            try {
                json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
            } catch (JsonProcessingException e) {
                System.out.println(e.toString());
            }
            return json;
        }
    }
}
