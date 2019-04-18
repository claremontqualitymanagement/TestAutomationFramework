package se.claremont.taf.websupport.gui.teststeps;

import se.claremont.taf.core.gui.teststructure.TestCaseManager;
import se.claremont.taf.core.gui.teststructure.TestStep;
import se.claremont.taf.core.gui.teststructure.TestStepResult;
import se.claremont.taf.core.testcase.TestCaseResult;
import se.claremont.taf.websupport.DomElement;

import java.io.Serializable;

public class WebCheckboxChangeTestStep extends TestStep implements Serializable{

    DomElement domElement;

    public WebCheckboxChangeTestStep(){
        super();
        setName("Input on element");
        setDescription("Input on element");
    }

    public WebCheckboxChangeTestStep(String name, String description){
        super(name, description);
    }

    public WebCheckboxChangeTestStep(DomElement domElement, boolean checked){
        super("Checkbox " + domElement.name +
                String.valueOf(checked).toLowerCase().replace("true", " checked").replace("false", " unchecked"),
                "The checkbox '" + domElement.name + "' was " + String.valueOf(checked).toLowerCase().replace("true", "checked").replace("false", "unchecked") + ".");
        this.domElement = domElement;
        this.actionName = "Checked";
        this.setAssociatedData(checked);
        this.setElementName(domElement.name);
    }


    @Override
    public String asCode() {
        TestCaseManager.testSetCode.makeSureRequiredImportIsAdded("import se.claremont.autotest.websupport.webdrivergluecode.WebInteractionMethods;");
        TestCaseManager.testSetCode.makeSureBeginTestSectionDeclarationExist("web = new WebInteractionsMethod(currentTestCase());");
        TestCaseManager.testSetCode.makeSureClassVariableIsDeclared("WebInteractionMethods web;");
        return "web.select(new DomElement(By." + domElement.by.toString() + ", \"" + getAssociatedData().toString().replace("\"", "\\" + "\"") + "\");";
    }

    @Override
    public TestStep clone() {
        WebCheckboxChangeTestStep clonedStep = new WebCheckboxChangeTestStep(this.getName(), this.getDescription());
        clonedStep.setActionName(this.actionName);
        clonedStep.setElementName(this.elementName);
        clonedStep.setAssociatedData(this.getAssociatedData());
        clonedStep.domElement = this.domElement;
        return clonedStep;
    }

    @Override
    public String getTestStepTypeShortName() {
        return "Web";
    }

    @Override
    public TestStepResult execute() {
        TestStepResult testStepResult = new TestStepResult(this, TestStepResult.Result.NOT_RUN);
        try {
            ReplayManager.WebDriverSingleton.getInstance().write(domElement, getAssociatedData().toString());
        }catch (Throwable e){
            testStepResult.updateResultStatus(TestStepResult.Result.FAIL);
            return testStepResult;
        }
        if(TestCaseManager.getTestCase().testCaseResult.resultStatus == TestCaseResult.ResultStatus.PASSED){
            testStepResult.updateResultStatus(TestStepResult.Result.PASS);
        } else {
            testStepResult.updateResultStatus(TestStepResult.Result.FAIL);
        }
        return testStepResult;
    }
}
