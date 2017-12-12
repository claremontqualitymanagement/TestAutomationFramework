package se.claremont.autotest.restsupport.gui;

import se.claremont.autotest.common.gui.teststructure.TestStep;
import se.claremont.autotest.common.gui.teststructure.TestStepResult;

public class RestTestStep extends TestStep{

    public RestTestStep(){
        super();
    }

    public RestTestStep(String name, String description){
        super(name, description);
    }

    @Override
    public String getTestStepTypeShortName() {
        return "REST";
    }

    @Override
    public TestStepResult execute() {
        return null;
    }
}
