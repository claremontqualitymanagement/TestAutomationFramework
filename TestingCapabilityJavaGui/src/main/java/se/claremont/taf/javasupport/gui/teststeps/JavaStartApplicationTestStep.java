package se.claremont.taf.javasupport.gui.teststeps;

import se.claremont.taf.gui.teststructure.TestCaseManager;
import se.claremont.taf.gui.teststructure.TestStepResult;
import se.claremont.taf.javasupport.applicationundertest.ApplicationUnderTest;

import java.io.Serializable;

public class JavaStartApplicationTestStep extends JavaTestStep implements Serializable {

    ApplicationUnderTest applicationUnderTest;

    public JavaStartApplicationTestStep(){
        super();
    }

    public JavaStartApplicationTestStep(String name, String description){
        super(name, description);
    }

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
    public JavaStartApplicationTestStep clone() {
        JavaStartApplicationTestStep clonedStep = new JavaStartApplicationTestStep(getName(), getDescription());
        clonedStep.setActionName(actionName);
        clonedStep.setElementName(elementName);
        clonedStep.setAssociatedData(data);
        clonedStep.applicationUnderTest = applicationUnderTest;
        return clonedStep;
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
