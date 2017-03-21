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

import java.io.IOException;
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
    @JsonProperty String testRunName = "";
    @JsonProperty Settings settings = TestRun.settings;
    @JsonProperty List<String> testCasesJsonsList = new ArrayList<>();
    @JsonProperty List<String> testSetJsonsList = new ArrayList<>();
    List<String> testSetNames = new ArrayList<>();

    public TafBackendServerTestRunReporter(){
        testRunName = TestRun.testRunName;
    }

    public List<String> getTestCasesJsonsList() { return testCasesJsonsList; }
    public List<String> getTestSetJsonsList() { return testSetJsonsList; }
    public Date getRunStartTime() { return runStartTime; }
    public void setRunStartTime(Date runStartTime) { this.runStartTime = runStartTime; }
    public Date getRunStopTime() { return runStopTime; }
    public void setRunStopTime(Date runStopTime) { this.runStopTime = runStopTime; }
    public Settings getSettings() { return settings; }
    public void setTestRunName() {
        if(testRunName == null || testRunName.trim().length() == 0){
            if(TestRun.testRunName != null && TestRun.testRunName.length() > 0){
                testRunName = TestRun.testRunName;
            } else if(String.join("", testSetNames).trim().length() > 0) {
                testRunName = String.join(", " + testSetNames);
            } else {
                testRunName = "Un-named test run";
            }
        }
    }

    public void evaluateTestCase(TestCase testCase){
        testCasesJsonsList.add(testCase.toJson());
    }

    public void report(){
        setRunStartTime(TestRun.startTime);
        setRunStopTime(new Date());
        setTestRunName();
        BackendServerConnection backendServerConnection = new BackendServerConnection();
        backendServerConnection.postTestRunResult(toJson());
    }

    public void evaluateTestSet(TestSet testSet){
        testSetJsonsList.add(testSet.toJson());
        testSetNames.add(testSet.name);
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
