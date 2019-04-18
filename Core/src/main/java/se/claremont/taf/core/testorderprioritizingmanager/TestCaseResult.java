package se.claremont.taf.core.testorderprioritizingmanager;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

class TestCaseResult {

    @JsonProperty public String testName;
    @JsonProperty public TestOutcome testOutcome;
    @JsonProperty public Date executionTime;

    public TestCaseResult(){}

    public Date getDate(){
        return executionTime;
    }
}
