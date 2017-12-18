package se.claremont.autotest.javasupport.gui.teststeps;

import se.claremont.autotest.common.gui.teststructure.TestStepResult;
import se.claremont.autotest.javasupport.applicationundertest.ApplicationUnderTest;

public class JavaStartApplicationTestStep extends JavaTestStep {

    ApplicationUnderTest applicationUnderTest;

    public JavaStartApplicationTestStep(ApplicationUnderTest applicationUnderTest){
        this.applicationUnderTest = applicationUnderTest;
        setName("Start " + applicationUnderTest.friendlyName);
        setActionName("Start");
        setElementName(applicationUnderTest.friendlyName);
        setAssociatedData(applicationUnderTest.startMechanism.startUrlOrPathToJarFile + " " +
                applicationUnderTest.startMechanism.mainClass + " " +
                String.join(" ", applicationUnderTest.startMechanism.arguments));
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
