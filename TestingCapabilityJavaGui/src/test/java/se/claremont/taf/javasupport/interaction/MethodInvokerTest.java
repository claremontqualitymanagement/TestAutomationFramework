package se.claremont.taf.javasupport.interaction;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import se.claremont.taf.core.testcase.TestCase;
import se.claremont.taf.core.testset.UnitTestClass;
import se.claremont.taf.javasupport.interaction.elementidentification.By;
import se.claremont.taf.javasupport.objectstructure.JavaGuiElement;

/**
 * Test for generic method invocation method
 *
 * Created by jordam on 2017-02-11.
 */
@SuppressWarnings("WeakerAccess")
public class MethodInvokerTest extends UnitTestClass {

    @Before
    public void setup(){
        JavaTestApplicationRunner.tryStart();
    }

    @Test
    public void invokeMethodOnComponent(){
        TestCase tempTestCase = new TestCase(null, currentTestNameInternal.getMethodName());
        GenericInteractionMethods java = new GenericInteractionMethods(tempTestCase);
        JavaGuiElement button = new JavaGuiElement(By.byExactText("Ok"), "OkButton");
        Assert.assertTrue(MethodInvoker.invokeMethod(tempTestCase, button.getRuntimeComponent(), "getName()").equals("OkButton"));
    }

}
