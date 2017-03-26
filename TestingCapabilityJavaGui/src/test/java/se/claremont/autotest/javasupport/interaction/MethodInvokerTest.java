package se.claremont.autotest.javasupport.interaction;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import se.claremont.autotest.common.testcase.TestCase;
import se.claremont.autotest.common.testset.UnitTestClass;
import se.claremont.autotest.javasupport.objectstructure.JavaGuiElement;

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
        JavaGuiElement button = new JavaGuiElement("OkButton", "Ok", JavaGuiElement.IdType.ELEMENT_TEXT);
        Assert.assertTrue(MethodInvoker.invokeMethod(tempTestCase, button.getRuntimeComponent(), "getName()").equals("OkButton"));
    }

}
