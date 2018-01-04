package se.claremont.autotest.websupport.gui.teststeps;

import se.claremont.autotest.common.gui.teststructure.TestCaseManager;
import se.claremont.autotest.common.gui.teststructure.TestStep;
import se.claremont.autotest.common.gui.teststructure.TestStepResult;
import se.claremont.autotest.common.testcase.TestCaseResult;
import se.claremont.autotest.websupport.DomElement;

public class WebInputTestStep extends TestStep {
    DomElement domElement;

    public WebInputTestStep(){
        super();
        setName("Input on element");
        setDescription("Input on element");
    }

    public WebInputTestStep(String name, String description){
        super(name, description);
    }

    public WebInputTestStep(DomElement domElement, String text){
        super("Text entered to " + domElement.name, "The text '" + text + "' was entered to the DomElement named '" + domElement.name + "'.");
        this.domElement = domElement;
        this.actionName = "Write";
        this.setAssociatedData(text);
        this.setElementName(domElement.name);
    }

    @Override
    public String asCode() {
        TestCaseManager.testSetCode.makeSureRequiredImportIsAdded("import se.claremont.autotest.websupport.webdrivergluecode.WebInteractionMethods;");
        TestCaseManager.testSetCode.makeSureBeginTestSectionDeclarationExist("web = new WebInteractionsMethod(currentTestCase());");
        TestCaseManager.testSetCode.makeSureClassVariableIsDeclared("WebInteractionMethods web;");
        return "web.write(new DomElement(By." + domElement.by.toString() + ", \"" + getAssociatedData().toString().replace("\"", "\\" + "\"") + "\");";
    }

    @Override
    public TestStep clone() {
        WebInputTestStep clonedStep = new WebInputTestStep(this.getName(), this.getDescription());
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
