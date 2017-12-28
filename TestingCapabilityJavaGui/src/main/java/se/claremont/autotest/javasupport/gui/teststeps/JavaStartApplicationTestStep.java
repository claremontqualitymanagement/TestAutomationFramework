package se.claremont.autotest.javasupport.gui.teststeps;

import se.claremont.autotest.common.gui.teststructure.TestCaseManager;
import se.claremont.autotest.common.gui.teststructure.TestStepResult;
import se.claremont.autotest.common.testcase.TestCase;
import se.claremont.autotest.javasupport.applicationundertest.ApplicationUnderTest;
import se.claremont.autotest.javasupport.applicationundertest.applicationstarters.ApplicationStartMechanism;

import java.io.Serializable;

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
    public String asCode(){
        TestCaseManager.testSetCode.makeSureRequiredImportIsAdded("import se.claremont.autotest.javasupport.applicationundertest.*;");
        String code = "ApplicationStartMechanism asm = new ApplicationStartMechanism(currentTestCase());" + System.lineSeparator();
        code += "asm.startUrlOrPathToJarFile = \"" + applicationUnderTest.startMechanism.startUrlOrPathToJarFile + "\";" + System.lineSeparator();
        code += "asm.mainClass = \"" + applicationUnderTest.startMechanism.mainClass + "\";" + System.lineSeparator();
        code += "asm.arguments = new String[]{\"" + String.join("\", \"", applicationUnderTest.startMechanism.arguments) + "\"};" + System.lineSeparator();
        code += "ApplicationUnderTest aut = new ApplicationUnderTest(currentTestCase(), asm);" + System.lineSeparator();
        code += "aut.start();";
        return code;
    }

    @Override
    public TestStepResult execute(){
        TestCaseManager.startTestStep();
        applicationUnderTest.reAssignTestCase(TestCaseManager.getTestCase());
        TestStepResult result = new TestStepResult();
        result.updateResultStatus(TestStepResult.Result.NOT_RUN);
        try {
            applicationUnderTest.start();
            result.updateResultStatus(TestStepResult.Result.PASS);
        }catch (Exception e){
            result.updateResultStatus(TestStepResult.Result.FAIL);
        }
        TestCaseManager.wrapUpTestCase();
        return result;
    }
}
