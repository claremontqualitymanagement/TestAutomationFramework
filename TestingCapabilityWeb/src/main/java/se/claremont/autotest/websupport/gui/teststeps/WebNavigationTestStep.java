package se.claremont.autotest.websupport.gui.teststeps;

import se.claremont.autotest.common.gui.teststructure.TestCaseManager;
import se.claremont.autotest.common.gui.teststructure.TestStep;
import se.claremont.autotest.common.gui.teststructure.TestStepResult;
import se.claremont.autotest.common.testcase.TestCaseResult;

public class WebNavigationTestStep extends TestStep {

    public WebNavigationTestStep(){
        super();
    }

    public WebNavigationTestStep(String name, String description){
        super(name, description);
    }

    public WebNavigationTestStep(String newUrl){
        super("Navigation to '" + newUrl + "'", "Navigation performed to new URL '" + newUrl + "'.");
        setActionName("Navigate");
        setElementName("browser");
        setAssociatedData(newUrl);
    }

    @Override
    public String asCode() {
        TestCaseManager.testSetCode.makeSureRequiredImportIsAdded("import se.claremont.autotest.websupport.webdrivergluecode.WebInteractionMethods;");
        TestCaseManager.testSetCode.makeSureBeginTestSectionDeclarationExist("web = new WebInteractionsMethod(currentTestCase());");
        TestCaseManager.testSetCode.makeSureClassVariableIsDeclared("WebInteractionMethods web;");
        return "   web.navigate(\"" + getAssociatedData().toString() + "\");";
    }

    @Override
    public TestStep clone() {
        WebNavigationTestStep clonedStep = new WebNavigationTestStep(getName(), getDescription());
        clonedStep.setActionName(actionName);
        clonedStep.setElementName(elementName);
        clonedStep.setAssociatedData(getAssociatedData());
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
            ReplayManager.WebDriverSingleton.getInstance().navigate((String) getAssociatedData());
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
