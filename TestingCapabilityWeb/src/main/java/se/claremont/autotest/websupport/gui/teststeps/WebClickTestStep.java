package se.claremont.autotest.websupport.gui.teststeps;

import se.claremont.autotest.common.gui.teststructure.TestStep;
import se.claremont.autotest.common.gui.teststructure.TestStepResult;

public class WebClickTestStep extends TestStep {

    public WebClickTestStep(){
        super();
        setName("Click on element");
        setDescription("Click on element");
    }

    public WebClickTestStep(String name, String description){
        super(name, description);
    }

    @Override
    public String asCode() {
        return null;
    }

    @Override
    public TestStep clone() {
        return null;
    }

    @Override
    public String getTestStepTypeShortName() {
        return "Web";
    }

    @Override
    public TestStepResult execute() {
        return null;
    }
}
