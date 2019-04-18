package se.claremont.taf.javasupport.gui.teststeps;

import se.claremont.taf.gui.teststructure.TestCaseManager;
import se.claremont.taf.gui.teststructure.TestStepResult;
import se.claremont.taf.testcase.TestCaseResult;
import se.claremont.taf.javasupport.interaction.GenericInteractionMethods;
import se.claremont.taf.javasupport.objectstructure.JavaGuiElement;

import java.io.Serializable;

public class JavaClictTestStep extends JavaTestStep implements Serializable{

    JavaGuiElement javaGuiElement;

    public JavaClictTestStep(){
        super();
    }

    public JavaClictTestStep(String name, String description){
        super(name, description);
    }

    public JavaClictTestStep(JavaGuiElement javaGuiElement){
        this.javaGuiElement = javaGuiElement;
        setName("Click " + javaGuiElement.getName());
        setActionName("Click");
        setElementName(javaGuiElement.getName());
        setAssociatedData(null);
        setDescription("Clicking the JavaGuiElement named '" + javaGuiElement.getName() + "'.");
    }

    @Override
    public JavaClictTestStep clone(){
        JavaClictTestStep clonedStep = new JavaClictTestStep(getName(), getDescription());
        clonedStep.setActionName(actionName);
        clonedStep.setElementName(elementName);
        clonedStep.setAssociatedData(data);
        clonedStep.javaGuiElement = javaGuiElement;
        return clonedStep;

    }

    @Override
    public String getTestStepTypeShortName() {
        return "Java";
    }

    @Override
    public String asCode(){
        TestCaseManager.testSetCode.makeSureRequiredImportIsAdded("import se.claremont.autotest.javasupport.interaction.*;");
        TestCaseManager.testSetCode.makeSureClassVariableIsDeclared("GenericInteractionMethods java;");
        TestCaseManager.testSetCode.makeSureBeginTestSectionDeclarationExist("java = new GenericInteractionMethods(currentTestCase());");
        return "java.click(" + javaGuiElement.getName() + ");";
    }

    @Override
    public TestStepResult execute() {
        TestCaseManager.startTestStep();
        GenericInteractionMethods java = new GenericInteractionMethods(TestCaseManager.getTestCase());
        try{
            java.click(javaGuiElement);
        }catch (Exception e){
            TestCaseManager.wrapUpTestCase();
            return new TestStepResult(this, TestStepResult.Result.FAIL);
        }
        if(java.testCase.testCaseResult.resultStatus.equals(TestCaseResult.ResultStatus.PASSED)){
            TestCaseManager.wrapUpTestCase();
            return new TestStepResult(this, TestStepResult.Result.PASS);
        } else {
            TestCaseManager.wrapUpTestCase();
            return new TestStepResult(this, TestStepResult.Result.FAIL);
        }
    }
}
