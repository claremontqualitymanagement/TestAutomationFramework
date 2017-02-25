package se.claremont.autotest.javamethods;

import org.junit.*;

/**
 * Created by jordam on 2017-02-25.
 */
public class JavaGuiElementTest {

    @Before
    public void setup(){
        JavaTestApplicationRunner.hideWindow();
    }

    @Test
    public void getParent(){
        Assert.assertTrue(JavaTestApplication.okbutton().getParent().getName(), JavaTestApplication.okbutton().getParent().getName().toLowerCase().contains("panel"));
    }

    @Test
    public void getSubElements(){
        System.out.println(JavaTestApplication.panel0().getSubElements().size());
        Assert.assertTrue(JavaTestApplication.panel0().getSubElements().size() == 6);
    }

    @Test
    public void getElementsInTheSameComponent(){
        System.out.println(JavaTestApplication.okbutton().getAllElementsInSameContainer().size());
        Assert.assertTrue(JavaTestApplication.okbutton().getAllElementsInSameContainer().size() == 6);
    }
}
