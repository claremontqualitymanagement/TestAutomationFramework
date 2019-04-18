package se.claremont.taf.websupport.gui.teststeps;

import se.claremont.taf.core.gui.teststructure.TestCaseManager;
import se.claremont.taf.core.gui.teststructure.TestStep;
import se.claremont.taf.core.gui.teststructure.TestStepResult;
import se.claremont.taf.core.testcase.TestCaseResult;
import se.claremont.taf.websupport.DomElement;

import java.io.Serializable;

public class WebAttributeChangeTestStep extends TestStep implements Serializable{
    DomElement domElement;
    String attributeName;
    String oldAttributeValue;
    String newAttributeValue;

    public WebAttributeChangeTestStep(){
        super();
        setName("Attribute change on element");
        setDescription("Attribute change on element " + domElement.name + ".");
    }

    public WebAttributeChangeTestStep(String name, String description){
        super(name, description);
    }

    public WebAttributeChangeTestStep(DomElement domElement, String attributeName, String oldAttributeValue, String newAttributeValue){
        super("Attribute '" + attributeName + "' changed on " + domElement.name, "The attribute '" + attributeName + "' was changed from '" + oldAttributeValue + "' to '" + newAttributeValue + "' on the DomElement named '" + domElement.name + "'.");
        this.attributeName = attributeName;
        this.oldAttributeValue = oldAttributeValue;
        this.newAttributeValue = newAttributeValue;
        this.domElement = domElement;
        this.actionName = "Attribute change";
        this.setElementName(domElement.name);
    }

    @Override
    public String asCode() {
        TestCaseManager.testSetCode.makeSureRequiredImportIsAdded("import se.claremont.autotest.websupport.webdrivergluecode.WebInteractionMethods;");
        TestCaseManager.testSetCode.makeSureBeginTestSectionDeclarationExist("web = new WebInteractionsMethod(currentTestCase());");
        TestCaseManager.testSetCode.makeSureClassVariableIsDeclared("WebInteractionMethods web;");
        return "web.click(new DomElement(By." + domElement.by.toString() + ");";
    }

    @Override
    public TestStep clone() {
        WebAttributeChangeTestStep clonedStep = new WebAttributeChangeTestStep(this.getName(), this.getDescription());
        clonedStep.setActionName(this.actionName);
        clonedStep.setElementName(this.elementName);
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
            ReplayManager.WebDriverSingleton.getInstance().click(domElement);
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
