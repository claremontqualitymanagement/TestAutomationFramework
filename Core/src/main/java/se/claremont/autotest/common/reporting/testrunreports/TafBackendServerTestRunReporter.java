package se.claremont.autotest.common.reporting.testrunreports;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import se.claremont.autotest.common.backendserverinteraction.TafBackendServerConnection;
import se.claremont.autotest.common.logging.KnownErrorsList;
import se.claremont.autotest.common.testcase.TestCase;
import se.claremont.autotest.common.testcase.TestCaseResult;
import se.claremont.autotest.common.testrun.Settings;
import se.claremont.autotest.common.testrun.TestRun;
import se.claremont.autotest.common.testrun.reportingengine.TestRunReporter;
import se.claremont.autotest.common.testset.TestSet;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Collects test run information during test execution, and sends it to a TAF Backend Server after the test run.
 *
 * Created by jordam on 2017-03-19.
 */
@JsonIgnoreProperties({"testSetNames"})
public class TafBackendServerTestRunReporter implements TestRunReporter {
    @SuppressWarnings("WeakerAccess")
    @JsonProperty public Date runStartTime;
    @SuppressWarnings("WeakerAccess")
    @JsonProperty public Date runStopTime;
    @JsonProperty public String testRunName = "";
    @SuppressWarnings("WeakerAccess")
    @JsonProperty public Settings settings = TestRun.settings;
    @SuppressWarnings("WeakerAccess")
    @JsonProperty public List<String> testCasesJsonsList = new ArrayList<>();
    @SuppressWarnings("WeakerAccess")
    @JsonProperty public List<String> testSetJsonsList = new ArrayList<>();
    @JsonProperty public List<String> testSetNames = new ArrayList<>();
    @SuppressWarnings("WeakerAccess")
    @JsonProperty public TestCaseResult.ResultStatus mostSevereErrorEncountered = TestCaseResult.ResultStatus.UNEVALUATED;
    @JsonProperty public String testRunId = "";

    public TafBackendServerTestRunReporter(){
        TestRun.initializeIfNotInitialized();
        testRunName = TestRun.testRunName;
        runStartTime = TestRun.startTime;
    }

    @SuppressWarnings({"SameParameterValue", "WeakerAccess"})
    public void setRunStartTime(Date runStartTime) { this.runStartTime = runStartTime; }
    @SuppressWarnings("WeakerAccess")
    public void setRunStopTime(Date runStopTime) { this.runStopTime = runStopTime; }
    public Settings getSettings() { return settings; }
    @SuppressWarnings("WeakerAccess")
    public void setTestRunName() {
        testRunName = TestRun.testRunName;
        if(testRunName == null || testRunName.trim().length() == 0){
            testRunName = String.join(", ", testSetNames);
        }
        if(testRunName == null || testRunName.trim().length() == 0){
            testRunName = "Un-named test run";
        }
    }

    public void evaluateTestCase(TestCase testCase){
        if(TestRun.getSettingsValue(Settings.SettingParameters.URL_TO_TAF_BACKEND).equals(TafBackendServerConnection.defaultServerUrl)) return;
        if(testCase == null)return;
        if(testCase.testCaseResult.resultStatus.value() > mostSevereErrorEncountered.value()) mostSevereErrorEncountered = testCase.testCaseResult.resultStatus;
        testCasesJsonsList.add(testCase.toJson());
    }

    public void report(){
        if(TestRun.getSettingsValue(Settings.SettingParameters.URL_TO_TAF_BACKEND).equals(TafBackendServerConnection.defaultServerUrl)) return;
        setRunStartTime(TestRun.startTime);
        if(TestRun.stopTime == null) {
            setRunStopTime(new Date());
        } else {
            setRunStopTime(TestRun.stopTime);
        }
        setTestRunName();
        TafBackendServerConnection tafBackendServerConnection = new TafBackendServerConnection();
        tafBackendServerConnection.postTestRunResult(toJson());
    }

    public void evaluateTestSet(TestSet testSet){
        if(TestRun.getSettingsValue(Settings.SettingParameters.URL_TO_TAF_BACKEND).equals(TafBackendServerConnection.defaultServerUrl)) return;
        testSetNames.add(testSet.name);
        testSetJsonsList.add(new TafBackendServerTestSet(testSet.knownErrorsList, testSet.name).toJson());
    }

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

    public class TafBackendServerTestSet{
        @JsonProperty public KnownErrorsList knownErrorsList;
        @JsonProperty public String name;

        public TafBackendServerTestSet(){}

        public TafBackendServerTestSet(KnownErrorsList knownErrorsList, String name){
            this.knownErrorsList = knownErrorsList;
            this.name = name;
        }

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
