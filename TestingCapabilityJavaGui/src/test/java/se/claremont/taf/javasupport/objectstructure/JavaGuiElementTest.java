package se.claremont.taf.javasupport.objectstructure;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import se.claremont.taf.core.testset.UnitTestClass;
import se.claremont.taf.javasupport.interaction.JavaTestApplication;
import se.claremont.taf.javasupport.interaction.JavaTestApplicationRunner;

import java.util.List;

/**
 * Tests for class JavaGuiElement
 *
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
        Assert.assertTrue(elementList.size() > 5);
        boolean foundCancelButton = false;
        for(JavaGuiElement element: elementList){
            System.out.println(element.toString());
            if(element.toString().contains("cancelButton")){
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
            if(element.toString().contains("cancelButton")){
                System.out.println(element.toString());
                foundCancelButton = true;
            }
        }
        Assert.assertTrue(foundCancelButton);
    }
}
