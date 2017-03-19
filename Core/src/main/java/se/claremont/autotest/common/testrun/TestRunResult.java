package se.claremont.autotest.common.testrun;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import se.claremont.autotest.common.testcase.TestCase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by jordam on 2017-03-19.
 */
public class TestRunResult {
    Date runStartTime;
    Date runStopTime;
    Settings settings = TestRun.settings;
    @JsonProperty List<TestCaseResultOverview> testCaseResultOverviews = new ArrayList<>();

    public TestRunResult(){}

    public Date getRunStartTime() { return runStartTime; }
    public void setRunStartTime(Date runStartTime) { this.runStartTime = runStartTime; }
    public Date getRunStopTime() { return runStopTime; }
    public void setRunStopTime(Date runStopTime) { this.runStopTime = runStopTime; }
    public Settings getSettings() { return settings; }

    public void addTestCaseResult(TestCase testCase){
        this.testCaseResultOverviews.add(new TestCaseResultOverview(testCase));
    }

    public void addTestCaseResult(String testName, String testSetName, String testOutcome){

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

    public class TestCaseResultOverview{
        @JsonProperty String testName;
        @JsonProperty String testSetName;
        @JsonProperty String resultStatus;
        @JsonProperty String uid;
        @JsonProperty long durationInMilliseconds = 0;

        public TestCaseResultOverview(){}

        public TestCaseResultOverview(String testName, String testSetName, String testOutCome){
            UUID uid = UUID.randomUUID();
            this.uid = uid.toString();
            this.testName = testName;
            this.testSetName = testSetName;
            this.resultStatus = testOutCome;
        }

        public TestCaseResultOverview(TestCase testCase){
            this.testName = testCase.testName;
            this.testSetName = testCase.testName;
            this.resultStatus = testCase.resultStatus.toString();
            this.uid = testCase.uid.toString();
            if(testCase.stopTime != null && testCase.startTime != null){
                durationInMilliseconds = testCase.stopTime.getTime() - testCase.startTime.getTime();
            }
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
