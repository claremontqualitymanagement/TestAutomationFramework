package se.claremont.autotest.javasupport.objectstructure;

import org.junit.*;
import se.claremont.autotest.common.testset.UnitTestClass;
import se.claremont.autotest.javasupport.interaction.JavaTestApplication;
import se.claremont.autotest.javasupport.interaction.JavaTestApplicationRunner;

import java.util.List;

/**
 * Created by jordam on 2017-02-25.
 */
public class JavaGuiElementTest extends UnitTestClass{

    @Before     public void testSetup() {
        JavaTestApplicationRunner.tryStart();
    }

    @After     public void tearDown() { JavaTestApplicationRunner.hideWindow(); }

    @Test
    public void getParent(){
        Assert.assertTrue(JavaTestApplication.okbutton().getParent().getName(), JavaTestApplication.okbutton().getParent().getName().toLowerCase().contains("panel"));
    }

    @Test
    public void getSubElements(){
        List<JavaGuiElement> elementList = JavaTestApplication.panel0().getSubElements();
        Assert.assertTrue(JavaTestApplication.panel0().getSubElements().size() > 5);
        boolean foundCancelButton = false;
        for(JavaGuiElement element: elementList){
            if(element.toString().contains("Button_Cancel")){
                System.out.println(element.toString());
                foundCancelButton = true;
            }
        }
        Assert.assertTrue(foundCancelButton);
    }

    @Test
    public void getElementsInTheSameComponent(){
        List<JavaGuiElement> elementList = JavaTestApplication.okbutton().getAllElementsInSameContainer();
        boolean foundCancelButton = false;
        for(JavaGuiElement element: elementList){
            if(element.toString().contains("Button_Cancel")){
                System.out.println(element.toString());
                foundCancelButton = true;
            }
        }
        Assert.assertTrue(foundCancelButton);
    }
}
