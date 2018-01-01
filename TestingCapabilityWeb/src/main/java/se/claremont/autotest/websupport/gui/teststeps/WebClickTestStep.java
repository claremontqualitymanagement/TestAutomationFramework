package se.claremont.autotest.websupport.gui.teststeps;

import se.claremont.autotest.common.gui.teststructure.TestStep;
import se.claremont.autotest.common.gui.teststructure.TestStepResult;
import se.claremont.autotest.websupport.DomElement;

public class WebClickTestStep extends TestStep {

    DomElement domElement;

    public WebClickTestStep(){
        super();
        setName("Click on element");
        setDescription("Click on element");
    }

    public WebClickTestStep(String name, String description){
        super(name, description);
    }

    public WebClickTestStep(DomElement domElement){
        super("Click on " + domElement.name, "A click on the DomElement named '" + domElement.name + "'.");
        this.domElement = domElement;
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
