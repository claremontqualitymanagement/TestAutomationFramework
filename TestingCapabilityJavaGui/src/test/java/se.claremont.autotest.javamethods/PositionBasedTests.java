package se.claremont.autotest.javamethods;

import org.junit.*;
import se.claremont.autotest.common.guidriverpluginstructure.PositionBasedIdentification.ElementsList;
import se.claremont.autotest.common.guidriverpluginstructure.PositionBasedIdentification.PositionBasedGuiElement;
import se.claremont.autotest.common.guidriverpluginstructure.PositionBasedIdentification.PositionBasedIdentificator;
import se.claremont.autotest.common.logging.LogLevel;
import se.claremont.autotest.common.logging.LogPost;
import se.claremont.autotest.common.testcase.TestCase;
import se.claremont.autotest.common.testset.TestSet;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jordam on 2017-02-23.
 */
public class PositionBasedTests extends TestSet{

    @BeforeClass
    public static void classSetup(){
        javaApp  = new JavaAwtAppWithSomeSwingComponents();
    }
    @Before
    public void testSetup() {
        if (javaApp == null) javaApp = new JavaAwtAppWithSomeSwingComponents();
        javaApp.show();
    }
    @AfterClass
    public static void classTeardown(){
        if(javaApp != null){
            javaApp.removeAll();
            javaApp.dispose();
        };
    }

    String[] args = new String[]{};
    static JavaAwtAppWithSomeSwingComponents javaApp;

    private boolean logPostIsFoundInLog(LogLevel logLevel, String partOfMessage, TestCase testCase){
        for(LogPost lp : testCase.testCaseLog.logPosts){
            if(logLevel == null) if(lp.message.contains(partOfMessage)) return true;
            if(partOfMessage == null) if(lp.logLevel.equals(logLevel)) return true;
            if(lp.logLevel == logLevel && lp.message.contains(partOfMessage))return true;
        }
        return false;
    }

    @Test
    //@Ignore //Not yet working as expected
    public void getText(){
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
    }

    @Test
    public void getText2(){
        GenericInteractionMethods java = new GenericInteractionMethods(currentTestCase);
        Object textField = PositionBasedIdentificator.
                fromAllTheElements(JavaTestApplication.panel0().getSubElements()).
                atTheSameHeightAs(JavaTestApplication.textField(), 20, 20).
                theObjectMostToTheRight();
        Assert.assertTrue(java.getText(((JavaGuiElement)textField).getRuntimeComponent()).equals("Checkbox Swing"));
    }
}
