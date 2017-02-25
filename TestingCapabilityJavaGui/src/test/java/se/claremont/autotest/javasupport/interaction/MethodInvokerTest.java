package se.claremont.autotest.javasupport.interaction;

import org.junit.*;
import se.claremont.autotest.common.testset.TestSet;
import se.claremont.autotest.javasupport.objectstructure.JavaGuiElement;

/**
 * Created by jordam on 2017-02-11.
 */
@SuppressWarnings("WeakerAccess")
public class MethodInvokerTest extends TestSet {

    @Before
    public void setup(){
        JavaTestApplicationRunner.hideWindow();
    }

    @Test
    public void invokeMethodOnComponent(){
        GenericInteractionMethods java = new GenericInteractionMethods(currentTestCase);
        JavaGuiElement button = new JavaGuiElement("OkButton", "Ok", JavaGuiElement.IdType.ELEMENT_TEXT);
        Assert.assertTrue(MethodInvoker.invokeMethod(currentTestCase, button.getRuntimeComponent(), "getName()").equals("OkButton"));
    }

}
