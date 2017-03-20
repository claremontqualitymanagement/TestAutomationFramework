package se.claremont.autotest.common.reporting.testrunreports;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import se.claremont.autotest.common.backendserverinteraction.BackendServerConnection;
import se.claremont.autotest.common.testcase.TestCase;
import se.claremont.autotest.common.testrun.Settings;
import se.claremont.autotest.common.testrun.TestRun;
import se.claremont.autotest.common.testrun.TestRunReporter;
import se.claremont.autotest.common.testset.TestSet;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by jordam on 2017-03-19.
 */
public class TafBackendServerTestRunReporter implements TestRunReporter {
    @JsonProperty Date runStartTime;
    @JsonProperty Date runStopTime;
    @JsonProperty
    Settings settings = TestRun.settings;
    @JsonProperty List<String> testCasesJsonsList = new ArrayList<>();
    @JsonProperty List<String> testSetJsonsList = new ArrayList<>();

    public TafBackendServerTestRunReporter(){}

    public Date getRunStartTime() { return runStartTime; }
    public void setRunStartTime(Date runStartTime) { this.runStartTime = runStartTime; }
    public Date getRunStopTime() { return runStopTime; }
    public void setRunStopTime(Date runStopTime) { this.runStopTime = runStopTime; }
    public Settings getSettings() { return settings; }

    public void evaluateTestCase(TestCase testCase){
        testCasesJsonsList.add(testCase.toJson());
    }

    public void report(){
        BackendServerConnection backendServerConnection = new BackendServerConnection();
        backendServerConnection.postTestRunResult(this);
    }

    public void evaluateTestSet(TestSet testSet){
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
