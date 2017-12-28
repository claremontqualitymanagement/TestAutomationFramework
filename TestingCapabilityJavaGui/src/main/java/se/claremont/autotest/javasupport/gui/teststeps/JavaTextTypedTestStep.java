package se.claremont.autotest.javasupport.gui.teststeps;

import se.claremont.autotest.common.gui.teststructure.TestStep;
import se.claremont.autotest.common.gui.teststructure.TestStepResult;
import se.claremont.autotest.common.testcase.TestCase;
import se.claremont.autotest.javasupport.interaction.GenericInteractionMethods;
import se.claremont.autotest.javasupport.objectstructure.JavaGuiElement;

public class JavaTextTypedTestStep extends JavaTestStep {

    public JavaTextTypedTestStep(String text, JavaGuiElement javaGuiElement){
        setActionName("Type");
        setDescription("Typing text '" + text + "' to element '" + javaGuiElement.getName() + "'.");
        setElementName(javaGuiElement.getName());
        setAssociatedData(text);
    }

    @Override
    public TestStepResult execute() {
        return null;
    }
}
