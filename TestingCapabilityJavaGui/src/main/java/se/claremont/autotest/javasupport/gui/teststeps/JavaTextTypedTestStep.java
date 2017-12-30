package se.claremont.autotest.javasupport.gui.teststeps;

import se.claremont.autotest.common.gui.teststructure.TestCaseManager;
import se.claremont.autotest.common.gui.teststructure.TestStep;
import se.claremont.autotest.common.gui.teststructure.TestStepResult;
import se.claremont.autotest.common.testcase.TestCase;
import se.claremont.autotest.javasupport.interaction.GenericInteractionMethods;
import se.claremont.autotest.javasupport.objectstructure.JavaGuiElement;

public class JavaTextTypedTestStep extends JavaTestStep {

    JavaGuiElement javaGuiElement;

    public JavaTextTypedTestStep(String name, String description){
        super(name, description);
    }

    public JavaTextTypedTestStep(String text, JavaGuiElement javaGuiElement){
        setActionName("Type");
        setDescription("Typing text '" + text + "' to element '" + javaGuiElement.getName() + "'.");
        setElementName(javaGuiElement.getName());
        setAssociatedData(text);
        this.javaGuiElement = javaGuiElement;
    }

    @Override
    public JavaTextTypedTestStep clone() {
        JavaTextTypedTestStep clonedStep = new JavaTextTypedTestStep(getName(), getDescription());
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
        TestStepResult testStepResult;
        GenericInteractionMethods java = new GenericInteractionMethods(TestCaseManager.getTestCase());
        try{
            java.write(javaGuiElement, (String)data);
            testStepResult = new TestStepResult(this, TestStepResult.Result.PASS);
        }catch (Exception e){
            testStepResult = new TestStepResult(this, TestStepResult.Result.FAIL);
        }
        TestCaseManager.wrapUpTestCase();
        return testStepResult;
    }
}
