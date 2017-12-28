package se.claremont.autotest.javasupport.gui.teststeps;

import se.claremont.autotest.common.gui.teststructure.TestStep;
import se.claremont.autotest.common.gui.teststructure.TestStepResult;
import se.claremont.autotest.common.testcase.TestCaseResult;
import se.claremont.autotest.javasupport.objectstructure.JavaGuiElement;

import java.util.stream.StreamSupport;

public class JavaClictTestStep extends JavaTestStep {

    StepRunner stepRunner;
    JavaGuiElement javaGuiElement;

    public JavaClictTestStep(JavaGuiElement javaGuiElement){
        this.javaGuiElement = javaGuiElement;
        setName("Click " + javaGuiElement.getName());
        setActionName("Click");
        setElementName(javaGuiElement.getName());
        setAssociatedData(null);
        setDescription("Clicking the JavaGuiElement named '" + javaGuiElement.getName() + "'.");
    }

    public void prepareExecution(StepRunner stepRunner){
        this.stepRunner = stepRunner;
    }

    @Override
    public String getTestStepTypeShortName() {
        return "Java";
    }

    @Override
    public TestStepResult execute() {
        if(stepRunner == null) System.out.println("Need to prepare execution prior to run Java Click test step.");
        stepRunner.java.click(javaGuiElement);
        if(stepRunner.java.testCase.testCaseResult.resultStatus.equals(TestCaseResult.ResultStatus.PASSED)){
            return new TestStepResult(this, TestStepResult.Result.PASS);
        } else {
            return new TestStepResult(this, TestStepResult.Result.FAIL);
        }
    }
}
