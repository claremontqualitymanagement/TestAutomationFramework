package se.claremont.taf.javasupport.gui.teststeps;

import se.claremont.taf.gui.teststructure.TestCaseManager;
import se.claremont.taf.gui.teststructure.TestStepResult;
import se.claremont.taf.testcase.TestCaseResult;
import se.claremont.taf.javasupport.interaction.GenericInteractionMethods;
import se.claremont.taf.javasupport.objectstructure.JavaGuiElement;

import java.io.Serializable;

public class JavaWriteTestStep extends JavaTestStep implements Serializable{

    JavaGuiElement javaGuiElement;

    public JavaWriteTestStep(String name, String description){
        super(name, description);
    }

    public JavaWriteTestStep(JavaGuiElement javaGuiElement, String keysPressedUponComponent) {
        this.javaGuiElement = javaGuiElement;
        setName("Write '" + keysPressedUponComponent + "' to element '" + javaGuiElement.getName() + "'");
        setDescription("Write '" + keysPressedUponComponent + "' to element '" + javaGuiElement.getName() + "'");
        setActionName("Write");
        setElementName(javaGuiElement.getName());
        setAssociatedData(keysPressedUponComponent);
    }

    @Override
    public JavaWriteTestStep clone() {
        JavaWriteTestStep clonedStep = new JavaWriteTestStep(getName(), getDescription());
        clonedStep.setActionName(actionName);
        clonedStep.setElementName(elementName);
        clonedStep.setAssociatedData(data);
        clonedStep.javaGuiElement = javaGuiElement;
        return clonedStep;
    }

    @Override
    public String asCode(){
        TestCaseManager.testSetCode.makeSureRequiredImportIsAdded("import se.claremont.autotest.javasupport.interaction.*;");
        TestCaseManager.testSetCode.makeSureClassVariableIsDeclared("GenericInteractionMethods java;");
        TestCaseManager.testSetCode.makeSureBeginTestSectionDeclarationExist("java = new GenericInteractionMethods(currentTestCase());");
        return "java.write(" + data + ", " + javaGuiElement.getName() + ");";
    }

    @Override
    public TestStepResult execute() {
        TestCaseManager.startTestStep();
        GenericInteractionMethods java = new GenericInteractionMethods(TestCaseManager.getTestCase());
        java.write(javaGuiElement, (String)data);
        if(java.testCase.testCaseResult.resultStatus.equals(TestCaseResult.ResultStatus.PASSED)){
            TestCaseManager.wrapUpTestCase();
            return new TestStepResult(this, TestStepResult.Result.PASS);
        } else {
            TestCaseManager.wrapUpTestCase();
            return new TestStepResult(this, TestStepResult.Result.FAIL);
        }
    }
}
