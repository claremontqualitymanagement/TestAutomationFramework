package se.claremont.autotest.javasupport.gui.teststeps;

import se.claremont.autotest.common.gui.teststructure.TestStep;
import se.claremont.autotest.common.gui.teststructure.TestStepResult;
import se.claremont.autotest.common.testcase.TestCaseResult;
import se.claremont.autotest.javasupport.objectstructure.JavaGuiElement;

public class JavaWriteTestStep extends JavaTestStep {

    JavaGuiElement javaGuiElement;
    String textToWrite;
    StepRunner stepRunner;

    public JavaWriteTestStep(JavaGuiElement javaGuiElement, String keysPressedUponComponent) {
        textToWrite = keysPressedUponComponent;
        this.javaGuiElement = javaGuiElement;
        setName("Write '" + keysPressedUponComponent + "' to element '" + javaGuiElement.getName() + "'");
        setDescription("Write '" + keysPressedUponComponent + "' to element '" + javaGuiElement.getName() + "'");
        setActionName("Write");
        setElementName(javaGuiElement.getName());
        setAssociatedData(keysPressedUponComponent);
    }

    public void prepareExecution(StepRunner stepRunner){
        this.stepRunner = stepRunner;
    }


    @Override
    public TestStepResult execute() {
        if(stepRunner == null) System.out.println("Need to prepare execution prior to run Java Click test step.");
        stepRunner.java.write(javaGuiElement, textToWrite);
        if(stepRunner.java.testCase.testCaseResult.resultStatus.equals(TestCaseResult.ResultStatus.PASSED)){
            return new TestStepResult(this, TestStepResult.Result.PASS);
        } else {
            return new TestStepResult(this, TestStepResult.Result.FAIL);
        }
    }
}
