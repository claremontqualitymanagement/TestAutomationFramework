package se.claremont.autotest.javasupport.gui.teststeps;

import com.sun.org.apache.xerces.internal.impl.xpath.XPath;
import se.claremont.autotest.common.gui.teststructure.TestStep;
import se.claremont.autotest.common.gui.teststructure.TestStepResult;
import se.claremont.autotest.common.testcase.TestCaseResult;
import se.claremont.autotest.javasupport.objectstructure.JavaGuiElement;

public class JavaClictTestStep extends TestStep {

    StepRunner stepRunner;
    JavaGuiElement javaGuiElement;

    public JavaClictTestStep(JavaGuiElement javaGuiElement){
        this.javaGuiElement = javaGuiElement;
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
        stepRunner.java.click(javaGuiElement);
        if(stepRunner.java.testCase.testCaseResult.resultStatus.equals(TestCaseResult.ResultStatus.PASSED)){
            return new TestStepResult(this, TestStepResult.Result.PASS);
        } else {
            return new TestStepResult(this, TestStepResult.Result.FAIL);
        }
    }
}
