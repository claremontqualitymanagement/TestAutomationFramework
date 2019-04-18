package se.claremont.taf.core.reporting.testrunreports;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import se.claremont.taf.core.backendserverinteraction.TafBackendServerConnection;
import se.claremont.taf.core.logging.KnownErrorsList;
import se.claremont.taf.core.testcase.TestCase;
import se.claremont.taf.core.testcase.TestCaseResult;
import se.claremont.taf.core.testrun.Settings;
import se.claremont.taf.core.testrun.TestRun;
import se.claremont.taf.core.testrun.reportingengine.TestRunReporter;
import se.claremont.taf.core.testset.TestSet;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Collects test run information during test execution, and sends it to a TAF Backend Server after the test run.
 *
 * Created by jordam on 2017-03-19.
 */
@JsonIgnoreProperties({"testSetNames"})
@SuppressWarnings("SpellCheckingInspection")
public class TafBackendServerTestRunReporter implements TestRunReporter {
    @SuppressWarnings("WeakerAccess")
    @JsonProperty public Date runStartTime;
    @SuppressWarnings("WeakerAccess")
    @JsonProperty public Date runStopTime;
    @JsonProperty public String testRunName = "";
    @SuppressWarnings("WeakerAccess")
    @JsonProperty public Settings settings;
    @SuppressWarnings("WeakerAccess")
    @JsonProperty public final List<String> testCasesJsonsList = new ArrayList<>();
    @SuppressWarnings("WeakerAccess")
    @JsonProperty public final List<String> testSetJsonsList = new ArrayList<>();
    @JsonProperty public final List<String> testSetNames = new ArrayList<>();
    @SuppressWarnings("WeakerAccess")
    @JsonProperty public TestCaseResult.ResultStatus mostSevereErrorEncountered = TestCaseResult.ResultStatus.UNEVALUATED;
    @JsonProperty public String testRunId = "";

    public TafBackendServerTestRunReporter(){
    }

    @SuppressWarnings("WeakerAccess")
    public void setRunStopTime(Date runStopTime) { this.runStopTime = runStopTime; }
    public Settings getSettings() { return settings; }
    @SuppressWarnings("WeakerAccess")
    public void setTestRunName() {
        testRunName = TestRun.getRunName();
        if(testRunName == null || testRunName.trim().length() == 0){
            testRunName = String.join(", ", testSetNames);
        }
        if(testRunName == null || testRunName.trim().length() == 0){
            testRunName = "Un-named test run";
        }
    }

    public void evaluateTestCase(TestCase testCase){
        if(testCase == null)return;
        if(testCase.testCaseResult.resultStatus.value() > mostSevereErrorEncountered.value()) mostSevereErrorEncountered = testCase.testCaseResult.resultStatus;
        testCasesJsonsList.add(testCase.toJson());
    }

    public void report(){
        if(TestRun.getSettingsValue(Settings.SettingParameters.URL_TO_TAF_BACKEND).equals(TafBackendServerConnection.defaultServerUrl)) return;
        runStartTime = TestRun.getStartTime();
        if(TestRun.getStopTime() == null) {
            TestRun.setStopTime(new Date());
        }
        setRunStopTime(TestRun.getStopTime());
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

    class TafBackendServerTestSet{
        @JsonProperty
        KnownErrorsList knownErrorsList;
        @JsonProperty String name;

        public TafBackendServerTestSet(){}

        TafBackendServerTestSet(KnownErrorsList knownErrorsList, String name){
            this.knownErrorsList = knownErrorsList;
            this.name = name;
        }

        String toJson(){
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
