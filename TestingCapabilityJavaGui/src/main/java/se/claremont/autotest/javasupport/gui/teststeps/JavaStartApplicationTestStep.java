package se.claremont.autotest.javasupport.gui.teststeps;

import se.claremont.autotest.common.gui.teststructure.TestStepResult;
import se.claremont.autotest.javasupport.applicationundertest.ApplicationUnderTest;

public class JavaStartApplicationTestStep extends JavaTestStep {

    ApplicationUnderTest applicationUnderTest;

    public JavaStartApplicationTestStep(ApplicationUnderTest applicationUnderTest){
        this.applicationUnderTest = applicationUnderTest;
        setActionName("Start application");
        setElementName(applicationUnderTest.friendlyName);
        setAssociatedData(null);
    }

    @Override
    public TestStepResult execute(){
        TestStepResult result = new TestStepResult();
        result.updateResultStatus(TestStepResult.Result.NOT_RUN);
        try {
            applicationUnderTest.start();
            result.updateResultStatus(TestStepResult.Result.PASS);
        }catch (Exception e){
            result.updateResultStatus(TestStepResult.Result.FAIL);
        }
        return result;
    }
}
