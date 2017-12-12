package se.claremont.autotest.common.gui.teststructure;

public class FrameworkTestStep extends TestStep {

    public FrameworkTestStep(){}

    public FrameworkTestStep(String name, String description){
        super(name, description);
    }

    @Override
    public String getTestStepTypeShortName() {
        return "TAF";
    }

    @Override
    public TestStepResult execute() {
        return null;
    }
}
