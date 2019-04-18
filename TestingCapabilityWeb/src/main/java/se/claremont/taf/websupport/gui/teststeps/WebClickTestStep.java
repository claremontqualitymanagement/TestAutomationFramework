package se.claremont.taf.websupport.gui.teststeps;

import se.claremont.taf.core.gui.teststructure.TestCaseManager;
import se.claremont.taf.core.gui.teststructure.TestStep;
import se.claremont.taf.core.gui.teststructure.TestStepResult;
import se.claremont.taf.core.testcase.TestCaseResult;
import se.claremont.taf.websupport.DomElement;

import java.io.Serializable;

public class WebClickTestStep extends TestStep implements Serializable{

    public WebClickTestStep(){
        super();
        setName("Click on element");
        setDescription("Click on element");
    }

    public WebClickTestStep(String name, String description){
        super(name, description);
    }

    public WebClickTestStep(DomElement domElement){
        super("Click on " + domElement.name, "A click on the DomElement named '" + domElement.name + "'.");
        this.setRunTimeElement(domElement);
        this.actionName = "Click";
        this.setElementName(domElement.name);
    }

    @Override
    public String asCode() {
        TestCaseManager.testSetCode.makeSureRequiredImportIsAdded("import se.claremont.autotest.websupport.webdrivergluecode.WebInteractionMethods;");
        TestCaseManager.testSetCode.makeSureBeginTestSectionDeclarationExist("web = new WebInteractionsMethod(currentTestCase());");
        TestCaseManager.testSetCode.makeSureClassVariableIsDeclared("WebInteractionMethods web;");
        return "web.click(new DomElement(By." + ((DomElement)getRunTimeElement()).by.toString() + ");";
    }

    @Override
    public TestStep clone() {
        WebClickTestStep clonedStep = new WebClickTestStep(this.getName(), this.getDescription());
        clonedStep.setActionName(this.actionName);
        clonedStep.setElementName(this.elementName);
        clonedStep.setRunTimeElement(this.getRunTimeElement());
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
            ReplayManager.WebDriverSingleton.getInstance().click((DomElement)getRunTimeElement());
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
