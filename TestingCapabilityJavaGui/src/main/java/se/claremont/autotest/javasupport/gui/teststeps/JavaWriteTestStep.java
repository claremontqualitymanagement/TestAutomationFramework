package se.claremont.autotest.javasupport.gui.teststeps;

import se.claremont.autotest.common.gui.teststructure.TestCaseManager;
import se.claremont.autotest.common.gui.teststructure.TestStep;
import se.claremont.autotest.common.gui.teststructure.TestStepResult;
import se.claremont.autotest.common.testcase.TestCaseResult;
import se.claremont.autotest.javasupport.interaction.GenericInteractionMethods;
import se.claremont.autotest.javasupport.objectstructure.JavaGuiElement;

public class JavaWriteTestStep extends JavaTestStep {

    JavaGuiElement javaGuiElement;
    String textToWrite;

    public JavaWriteTestStep(JavaGuiElement javaGuiElement, String keysPressedUponComponent) {
        textToWrite = keysPressedUponComponent;
        this.javaGuiElement = javaGuiElement;
        setName("Write '" + keysPressedUponComponent + "' to element '" + javaGuiElement.getName() + "'");
        setDescription("Write '" + keysPressedUponComponent + "' to element '" + javaGuiElement.getName() + "'");
        setActionName("Write");
        setElementName(javaGuiElement.getName());
        setAssociatedData(keysPressedUponComponent);
    }

    @Override
    public String asCode(){
        TestCaseManager.testSetCode.makeSureRequiredImportIsAdded("import se.claremont.autotest.javasupport.interaction.*;");
        TestCaseManager.testSetCode.makeSureClassVariableIsDeclared("GenericInteractionMethods java;");
        TestCaseManager.testSetCode.makeSureBeginTestSectionDeclarationExist("java = new GenericInteractionMethods(currentTestCase());");
        return "java.write(" + textToWrite + ", " + javaGuiElement.getName() + ");";
    }

    @Override
    public TestStepResult execute() {
        TestCaseManager.startTestStep();
        GenericInteractionMethods java = new GenericInteractionMethods(TestCaseManager.getTestCase());
        java.write(javaGuiElement, textToWrite);
        if(java.testCase.testCaseResult.resultStatus.equals(TestCaseResult.ResultStatus.PASSED)){
            TestCaseManager.wrapUpTestCase();
            return new TestStepResult(this, TestStepResult.Result.PASS);
        } else {
            TestCaseManager.wrapUpTestCase();
            return new TestStepResult(this, TestStepResult.Result.FAIL);
        }
    }
}
