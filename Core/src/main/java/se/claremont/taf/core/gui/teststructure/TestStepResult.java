package se.claremont.taf.core.gui.teststructure;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import se.claremont.taf.core.gui.guistyle.TafLabel;

import java.awt.*;

public class TestStepResult {
    @JsonProperty private Result result = Result.NOT_RUN;
    @JsonIgnore private TafLabel guiComponent = new TafLabel(result.getFriendlyName());
    @JsonProperty private TestStep testStep;

    public TestStepResult(){}

    public TestStepResult(TestStep testStep, Result result){
        this.testStep = testStep;
        this.result = result;
    }

    public void merge(TestStepResult testStepResult){
        if(testStepResult.getResult().getValue() > result.getValue())
            result = testStepResult.getResult();
    }

    public Result getResult(){
        return result;
    }

    public void updateResultStatus(Result result){
        guiComponent.setText(result.getFriendlyName());
        this.result = result;
    }

    public Component guiComponent(){
        return guiComponent;
    }

    public enum Result{
        RUNNING(1, "Running"),
        PASS(2, "Passed"),
        PROBLEM(3, "Problem"),
        FAIL(4, "Failed"),
        NOT_RUN(0, "Not run");

        private int value;
        private String friendlyName;

        Result(int value, String friendlyName){
            this.value = value;
            this.friendlyName = friendlyName;
        }

        public String getFriendlyName(){
            return friendlyName;
        }

        public int getValue(){
            return value;
        }
    }
}
