package se.claremont.autotest.javasupport.gui.teststeps;

import se.claremont.autotest.common.gui.teststructure.TestStep;
import se.claremont.autotest.common.gui.teststructure.TestStepResult;

public class JavaTestStep extends TestStep {

    @Override
    public String getTestStepTypeShortName() {
        return "Java";
    }

    @Override
    public TestStepResult execute() {
        return null;
    }
}
