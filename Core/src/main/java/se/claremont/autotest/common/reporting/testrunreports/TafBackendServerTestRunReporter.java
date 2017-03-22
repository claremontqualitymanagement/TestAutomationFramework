package se.claremont.autotest.common.reporting.testrunreports;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import se.claremont.autotest.common.backendserverinteraction.BackendServerConnection;
import se.claremont.autotest.common.testcase.TestCase;
import se.claremont.autotest.common.testrun.Settings;
import se.claremont.autotest.common.testrun.TestRun;
import se.claremont.autotest.common.testrun.TestRunReporter;
import se.claremont.autotest.common.testset.TestSet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by jordam on 2017-03-19.
 */
@JsonIgnoreProperties({"testSetNames"})
public class TafBackendServerTestRunReporter implements TestRunReporter {
    @JsonProperty public Date runStartTime;
    @JsonProperty public Date runStopTime;
    @JsonProperty public String testRunName = "";
    @JsonProperty public Settings settings = TestRun.settings;
    @JsonProperty public List<String> testCasesJsonsList = new ArrayList<>();
    @JsonProperty public List<String> testSetJsonsList = new ArrayList<>();
    @JsonProperty public List<String> testSetNames = new ArrayList<>();
    @JsonProperty public TestCase.ResultStatus mostSevereErrorEncountered = TestCase.ResultStatus.UNEVALUATED;
    @JsonProperty public String testRunId;

    public TafBackendServerTestRunReporter(){
        testRunName = TestRun.testRunName;
    }

    public TafBackendServerTestRunReporter(String json){
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.readValue(json, this.getClass());
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    public List<String> getTestCasesJsonsList() { return testCasesJsonsList; }
    public List<String> getTestSetJsonsList() { return testSetJsonsList; }
    public Date getRunStartTime() { return runStartTime; }
    public void setRunStartTime(Date runStartTime) { this.runStartTime = runStartTime; }
    public Date getRunStopTime() { return runStopTime; }
    public void setRunStopTime(Date runStopTime) { this.runStopTime = runStopTime; }
    public Settings getSettings() { return settings; }
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
        if(testCase.resultStatus.value() > mostSevereErrorEncountered.value()) mostSevereErrorEncountered = testCase.resultStatus;
        testCasesJsonsList.add(testCase.toJson());
    }

    public void report(){
        testRunId = TestRun.testRunId.toString();
        setRunStartTime(TestRun.startTime);
        setRunStopTime(new Date());
        setTestRunName();
        BackendServerConnection backendServerConnection = new BackendServerConnection();
        backendServerConnection.postTestRunResult(toJson());
    }

    public void evaluateTestSet(TestSet testSet){
        testSetNames.add(testSet.name);
        testSetJsonsList.add(testSet.toJson());
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
