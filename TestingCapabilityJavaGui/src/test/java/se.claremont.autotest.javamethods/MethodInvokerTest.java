package se.claremont.autotest.javamethods;

import org.junit.*;
import se.claremont.autotest.common.testset.TestSet;

/**
 * Created by jordam on 2017-02-11.
 */
public class MethodInvokerTest extends TestSet {
    String[] args = new String[]{};
    JavaAwtAppWithSomeSwingComponents javaApp;

    @After
    public void teardown(){
        javaApp.removeAll();
        if(javaApp != null) javaApp.dispose();;
    }


    @Test
    public void invokeMethodOnComponent(){
        javaApp = new JavaAwtAppWithSomeSwingComponents();
        GenericInteractionMethods java = new GenericInteractionMethods(currentTestCase);
        JavaGuiElement button = new JavaGuiElement("OkButton", "Ok", JavaGuiElement.IdType.ELEMENT_TEXT);
        Assert.assertTrue(MethodInvoker.invokeMethod(currentTestCase, button.getRuntimeComponent(), "getName()").equals("OkButton"));
    }

}
