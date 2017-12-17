package se.claremont.autotest.common.gui.teststructure;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedList;
import java.util.List;

public class SubProcedureTestStep extends TestStep {

    @JsonProperty List<TestStep> testSteps = new LinkedList<>();

    public SubProcedureTestStep(){}

    public SubProcedureTestStep(String name, String description) {
        super(name, description);
    }

    public void addTestStep(TestStep testStep){
        testSteps.add(testStep);
    }

    @Override
    public String getTestStepTypeShortName() {
        return "Sub";
    }

    @Override
    public TestStepResult execute() {
        TestStepResult testStepResult = new TestStepResult(this, TestStepResult.Result.NOT_RUN);
        for(TestStep testStep : testSteps){
            testStepResult.merge(testStep.execute());
        }
        return testStepResult;
    }
}
