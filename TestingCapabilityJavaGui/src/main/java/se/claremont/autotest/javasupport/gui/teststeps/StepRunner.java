package se.claremont.autotest.javasupport.gui.teststeps;

import se.claremont.autotest.common.gui.teststructure.TestStep;
import se.claremont.autotest.common.gui.teststructure.TestStepResult;
import se.claremont.autotest.common.testcase.TestCase;
import se.claremont.autotest.javasupport.interaction.GenericInteractionMethods;

import java.util.LinkedList;
import java.util.List;

public class StepRunner {
    GenericInteractionMethods java;
    List<TestStep> steps = new LinkedList<>();

    public StepRunner(TestCase testCase){
        java = new GenericInteractionMethods(testCase);
    }

    public void setExecutionSteps(List<TestStep> steps){
        this.steps.addAll(steps);
    }

    public TestStepResult execute(){
        TestStepResult testStepResult = new TestStepResult();
        for(TestStep step:steps){
            testStepResult.merge(step.execute());
        }
        return testStepResult;
    }
}
