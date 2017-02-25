package se.claremont.autotest.javasupport.interaction;

import org.junit.*;
import se.claremont.autotest.common.guidriverpluginstructure.PositionBasedIdentification.ElementsList;
import se.claremont.autotest.common.guidriverpluginstructure.PositionBasedIdentification.PositionBasedGuiElement;
import se.claremont.autotest.common.guidriverpluginstructure.PositionBasedIdentification.PositionBasedIdentificator;
import se.claremont.autotest.common.logging.LogLevel;
import se.claremont.autotest.common.testset.TestSet;
import se.claremont.autotest.javasupport.objectstructure.JavaGuiElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jordam on 2017-02-23.
 */
@SuppressWarnings("WeakerAccess")
public class PositionBasedTests extends TestSet{

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
        GenericInteractionMethods java = new GenericInteractionMethods(currentTestCase);
        List<Object> objects = java.allSubElementsOf(JavaTestApplication.panel0());
        //System.out.println(String.join(System.lineSeparator(), MethodInvoker.getAvailableMethods(JavaTestApplication.okbutton().getRuntimeComponent())));
        Assert.assertNotNull(objects);
        ArrayList<PositionBasedGuiElement> elementsList = new ArrayList<>();
        for(Object object : objects){
            elementsList.add(new JavaGuiElement(object, currentTestCase));
        }
        ElementsList allElementsInPanel = PositionBasedIdentificator.fromAllTheElements(elementsList);
        MethodInvoker m = new MethodInvoker(currentTestCase);
        Assert.assertNotNull(allElementsInPanel);
        Object textField = allElementsInPanel.atTheSameHeightAs(JavaTestApplication.textField(), 20, 20).theObjectMostToTheRight();
        Assert.assertNotNull(textField);
        currentTestCase.log(LogLevel.INFO, textField.getClass().toString());
        currentTestCase.log(LogLevel.INFO, java.getText(((JavaGuiElement)textField).getRuntimeComponent()));
        Assert.assertTrue(java.getText(((JavaGuiElement)textField).getRuntimeComponent()).equals("Checkbox Swing"));
        JavaTestApplicationRunner.hideWindow();
    }

    @Test
    public void getText2(){
        JavaTestApplicationRunner.showWindow();
        GenericInteractionMethods java = new GenericInteractionMethods(currentTestCase);
        JavaGuiElement textField = (JavaGuiElement) PositionBasedIdentificator.
                fromAllTheElements(JavaTestApplication.panel0().getSubElements()).
                atTheSameHeightAs(JavaTestApplication.textField(), 10, 10).
                theObjectMostToTheRight();
        //java.takeScreenshot();
        Assert.assertTrue(java.getText(textField.getRuntimeComponent()), java.getText(textField.getRuntimeComponent()).equals("Checkbox Swing"));
        JavaTestApplicationRunner.hideWindow();
    }

    @Test
    public void toTheRightOf(){
        JavaTestApplicationRunner.showWindow();
        GenericInteractionMethods java = new GenericInteractionMethods(currentTestCase);
        java.wait(500);
        JavaGuiElement textField = (JavaGuiElement) PositionBasedIdentificator.fromAllTheElements(JavaTestApplication.window().getComponentsAsJavaGuiElements()).elementImmediatelyToTheRightOf(JavaTestApplication.okbutton());
        Assert.assertNotNull(textField);
        Assert.assertTrue(java.getText(textField.getRuntimeComponent()), java.getText(textField.getRuntimeComponent()).equals("Checkbox awt"));
        JavaTestApplicationRunner.hideWindow();
    }
}
