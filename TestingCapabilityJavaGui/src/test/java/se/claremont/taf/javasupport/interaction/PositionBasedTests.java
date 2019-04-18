package se.claremont.taf.javasupport.interaction;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import se.claremont.taf.core.guidriverpluginstructure.PositionBasedIdentification.ElementsList;
import se.claremont.taf.core.guidriverpluginstructure.PositionBasedIdentification.PositionBasedGuiElement;
import se.claremont.taf.core.guidriverpluginstructure.PositionBasedIdentification.PositionBasedIdentificator;
import se.claremont.taf.core.logging.LogLevel;
import se.claremont.taf.core.testcase.TestCase;
import se.claremont.taf.core.testset.UnitTestClass;
import se.claremont.taf.javasupport.objectstructure.JavaGuiElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests to make sure the relative position based identification of GUI elements work
 *
 * Created by jordam on 2017-02-23.
 */
@SuppressWarnings("WeakerAccess")
public class PositionBasedTests extends UnitTestClass{

    @Before
    public void testSetup() {
        JavaTestApplicationRunner.tryStart();
    }
    @After
    public void testTeardown(){
        JavaTestApplicationRunner.hideWindow();
    }

    @Test
    public void getText(){
        JavaTestApplicationRunner.showWindow();
        TestCase tempTestCase = new TestCase(null, currentTestNameInternal.getMethodName());
        GenericInteractionMethods java = new GenericInteractionMethods(tempTestCase);
        List<Object> objects = java.allSubElementsOf(JavaTestApplication.panel0());
        Assert.assertNotNull(objects);
        ArrayList<PositionBasedGuiElement> elementsList = new ArrayList<>();
        for(Object object : objects){
           elementsList.add(new JavaGuiElement(object));
        }
        ElementsList allElementsInPanel = PositionBasedIdentificator.fromAllThePositionBasedElements(elementsList);
        MethodInvoker m = new MethodInvoker(tempTestCase);
        Assert.assertNotNull(allElementsInPanel);
        //Object textField = allElementsInPanel.atTheSameHeightAs(JavaTestApplication.textField(), 20, 20).theObjectMostToTheRight();
        JavaGuiElement textField = new JavaGuiElement(PositionBasedIdentificator
                .fromAllThePositionBasedElements(elementsList)
                .atTheSameHeightAs(JavaTestApplication.textField(), 20, 20)
                .theObjectMostToTheRight());
        Assert.assertNotNull(textField);
        tempTestCase.log(LogLevel.INFO, textField.getClass().toString());
        tempTestCase.log(LogLevel.INFO, java.getText(textField));
        Assert.assertTrue(java.getText(textField).equals("Checkbox Swing"));
        JavaTestApplicationRunner.hideWindow();
    }

    @Test
    public void getText2(){
        JavaTestApplicationRunner.showWindow();
        TestCase tempTestCase = new TestCase(null, currentTestNameInternal.getMethodName());
        GenericInteractionMethods java = new GenericInteractionMethods(tempTestCase);
        JavaGuiElement textField = (JavaGuiElement) PositionBasedIdentificator.
                fromAllThePositionBasedElements(JavaTestApplication.panel0().getSubElements()).
                atTheSameHeightAs(JavaTestApplication.textField(), 10, 10).
                theObjectMostToTheRight();
        //java.takeScreenshot();
        Assert.assertTrue(java.getText(textField.getRuntimeComponent()), java.getText(textField.getRuntimeComponent()).equals("Checkbox Swing"));
        JavaTestApplicationRunner.hideWindow();
    }

    @Test
    public void toTheRightOf(){
        JavaTestApplicationRunner.showWindow();
        TestCase tempTestCase = new TestCase(null, currentTestNameInternal.getMethodName());
        GenericInteractionMethods java = new GenericInteractionMethods(tempTestCase);
        java.wait(500);
        JavaGuiElement textField = (JavaGuiElement) PositionBasedIdentificator.fromAllThePositionBasedElements(JavaTestApplication.window().getComponentsAsJavaGuiElements()).elementImmediatelyToTheRightOf(JavaTestApplication.okbutton());
        Assert.assertNotNull(textField);
        Assert.assertTrue(java.getText(textField.getRuntimeComponent()), java.getText(textField.getRuntimeComponent()).equals("Checkbox awt"));
        JavaTestApplicationRunner.hideWindow();
    }
}
