package se.claremont.autotest.javasupport.interaction;

import se.claremont.autotest.common.logging.LogLevel;
import se.claremont.autotest.common.logging.LogPost;
import se.claremont.autotest.common.testcase.TestCase;
import org.junit.*;
import se.claremont.autotest.common.testset.TestSet;
import se.claremont.autotest.common.testset.UnitTestClass;
import se.claremont.autotest.javasupport.applicationstart.ApplicationStarter;
import se.claremont.autotest.javasupport.objectstructure.JavaGuiElement;
import se.claremont.autotest.javasupport.objectstructure.JavaWindow;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by jordam on 2017-02-08.
 */
@SuppressWarnings("WeakerAccess")
public class JavaInteractionMethodstest extends UnitTestClass {

    @Before     public void testSetup() {
        JavaTestApplicationRunner.tryStart();
    }

    @After     public void tearDown() { JavaTestApplicationRunner.hideWindow(); }

    private boolean logPostIsFoundInLog(LogLevel logLevel, String partOfMessage, TestCase testCase){
        for(LogPost lp : testCase.testCaseLog.logPosts){
            if(logLevel == null) if(lp.message.contains(partOfMessage)) return true;
            if(partOfMessage == null) if(lp.logLevel.equals(logLevel)) return true;
            if(lp.logLevel == logLevel && lp.message.contains(partOfMessage))return true;
        }
        return false;
    }

    @Test
    public void getText(){
        TestCase testCase = new TestCase(null, currentTestNameInternal.getMethodName());
        GenericInteractionMethods java = new GenericInteractionMethods(testCase);
        List<String> texts = new ArrayList<>();
        for (Window w : ApplicationStarter.getWindows()){
            JavaWindow javaWindow = new JavaWindow(w);
            texts.addAll(javaWindow.textsInComponents());
        }
        Assert.assertTrue(texts.contains("Ok"));
    }

    @Test
    public void javaMethodsTestJButtonIdentificationByExactElementText(){
        TestCase testCase = new TestCase(null, currentTestNameInternal.getMethodName());
        GenericInteractionMethods java = new GenericInteractionMethods(testCase);
        JavaGuiElement button = new JavaGuiElement("OkButton", "Ok", JavaGuiElement.IdType.ELEMENT_TEXT);
        Object c = button.getRuntimeComponent();
        Assert.assertNotNull(c);
        Assert.assertTrue(c.toString().contains("Ok"));
    }

    @Test
    public void javaMethodsTestJButtonIdentificationByExactElementName(){
        TestCase testCase = new TestCase(null, currentTestNameInternal.getMethodName());
        GenericInteractionMethods java = new GenericInteractionMethods(testCase);
        JavaGuiElement button = new JavaGuiElement("OkButton", "OkButton", JavaGuiElement.IdType.ELEMENT_NAME);
        Object c = button.getRuntimeComponent();
        Assert.assertNotNull(c);
        Assert.assertTrue(c.toString().contains("Ok"));
    }

    @Test
    public void javaMethodsTestButtonIdentificationByExactElementText(){
        TestCase testCase = new TestCase(null, currentTestNameInternal.getMethodName());
        GenericInteractionMethods java = new GenericInteractionMethods(testCase);
        JavaGuiElement button = new JavaGuiElement("CancelButton", "Cancel", JavaGuiElement.IdType.ELEMENT_TEXT);
        Object c = button.getRuntimeComponent();
        Assert.assertNotNull(c);
        Assert.assertTrue(c.toString().contains("Cancel"));
    }

    @Test
    public void javaMethodsTestButtonIdentificationByExactElementName(){
        TestCase testCase = new TestCase(null, currentTestNameInternal.getMethodName());
        GenericInteractionMethods java = new GenericInteractionMethods(testCase);
        JavaGuiElement button = new JavaGuiElement("CancelButton", "Cancel", JavaGuiElement.IdType.ELEMENT_NAME);
        Object c = button.getRuntimeComponent();
        Assert.assertNotNull(c);
        Assert.assertTrue(c.toString().contains("Cancel"));
    }

    @Test
    public void javaMethodsTestJButtonIdentificationByPartialElementText(){
        TestCase testCase = new TestCase(null, currentTestNameInternal.getMethodName());
        GenericInteractionMethods java = new GenericInteractionMethods(testCase);
        JavaGuiElement button = new JavaGuiElement("OkButton", "O", JavaGuiElement.IdType.ELEMENT_TEXT);
        Object c = button.getRuntimeComponent();
        Assert.assertNotNull(c);
        Assert.assertTrue(c.toString().contains("Ok"));
    }

    @Test
    public void javaMethodsTestJButtonIdentificationByPartialElementName(){
        TestCase testCase = new TestCase(null, currentTestNameInternal.getMethodName());
        GenericInteractionMethods java = new GenericInteractionMethods(testCase);
        JavaGuiElement button = new JavaGuiElement("OkButton", "OkButt", JavaGuiElement.IdType.ELEMENT_NAME);
        Object c = button.getRuntimeComponent();
        Assert.assertNotNull(c);
        Assert.assertTrue(c.toString().contains("Ok"));
    }

    @Test
    public void javaMethodsTestButtonIdentificationByPartialElementText(){
        TestCase testCase = new TestCase(null, currentTestNameInternal.getMethodName());
        GenericInteractionMethods java = new GenericInteractionMethods(testCase);
        JavaGuiElement button = new JavaGuiElement("CancelButton", "Canc", JavaGuiElement.IdType.ELEMENT_TEXT);
        Object c = button.getRuntimeComponent();
        Assert.assertNotNull(c);
        Assert.assertTrue(c.toString().contains("Cancel"));
    }

    @Test
    public void javaMethodsTestButtonIdentificationByPartialElementName(){
        TestCase testCase = new TestCase(null, currentTestNameInternal.getMethodName());
        GenericInteractionMethods java = new GenericInteractionMethods(testCase);
        JavaGuiElement button = new JavaGuiElement("CancelButton", "Canc", JavaGuiElement.IdType.ELEMENT_NAME);
        Object c = button.getRuntimeComponent();
        Assert.assertNotNull(c);
        Assert.assertTrue(c.toString().contains("Cancel"));
    }

    @Test
    @Ignore //This test is supposed to fail and take screenshot
    public void takeScreenshotOnFailedClick(){
        TestCase testCase = new TestCase(null, currentTestNameInternal.getMethodName());
        JavaTestApplicationRunner.showWindow();
        GenericInteractionMethods java = new GenericInteractionMethods(testCase);
        JavaGuiElement button = new JavaGuiElement("CancelButton", "Canc", JavaGuiElement.IdType.ELEMENT_TEXT);
        java.click(button);
        java.wait(10000);
        JavaTestApplicationRunner.hideWindow();
    }


    @Test
    public void getTextFromComponentWithGetLabelMethod(){
        TestCase testCase = new TestCase(null, currentTestNameInternal.getMethodName());
        GenericInteractionMethods java = new GenericInteractionMethods(testCase);
        JavaGuiElement button = new JavaGuiElement("CancelButton", "Canc", JavaGuiElement.IdType.ELEMENT_TEXT);
        Assert.assertTrue(java.getText(button).equals("Cancel"));
    }

    @Test
    public void getTextFromComponentWithGetTextMethod(){
        TestCase testCase = new TestCase(null, currentTestNameInternal.getMethodName());
        GenericInteractionMethods java = new GenericInteractionMethods(testCase);
        JavaGuiElement button = new JavaGuiElement("OkButton", "Ok", JavaGuiElement.IdType.ELEMENT_TEXT);
        Assert.assertTrue(java.getText(button).equals("Ok"));
    }

    @Test
    public void writeTextToTextField(){
        TestCase testCase = new TestCase(null, currentTestNameInternal.getMethodName());
        GenericInteractionMethods java = new GenericInteractionMethods(testCase);
        JavaGuiElement textField = new JavaGuiElement("TestTextTield", "Textfield2", JavaGuiElement.IdType.ELEMENT_NAME);
        java.write(textField, "Yippie");
        Assert.assertTrue(java.getText(textField).equals("Yippie"));
    }

    @Test
    public void verifyCorrectExactMatchOfElementText(){
        TestCase tempTestCase = new TestCase(null, currentTestNameInternal.getMethodName());
        GenericInteractionMethods java = new GenericInteractionMethods(tempTestCase);
        JavaGuiElement button = new JavaGuiElement("OkButton", "Ok", JavaGuiElement.IdType.ELEMENT_TEXT);
        java.verifyElementTextIsExactly(button, "Ok");
        boolean found = false;
        for(LogPost logPost : tempTestCase.testCaseLog.logPosts){
            if(logPost.logLevel == LogLevel.VERIFICATION_PASSED && logPost.message.contains("OkButton")){
                found = true;
            }
        }
        Assert.assertTrue(found);
    }

    @Test
    public void verifyCorrectContainsMatchOfElementText(){
        TestCase tempTestCase = new TestCase(null, currentTestNameInternal.getMethodName());
        GenericInteractionMethods java = new GenericInteractionMethods(tempTestCase);
        JavaGuiElement button = new JavaGuiElement("OkButton", "Ok", JavaGuiElement.IdType.ELEMENT_TEXT);
        java.verifyElementTextContains(button, "O");
        boolean found = false;
        for(LogPost logPost : tempTestCase.testCaseLog.logPosts){
            if(logPost.logLevel == LogLevel.VERIFICATION_PASSED && logPost.message.contains("OkButton")){
                found = true;
            }
        }
        Assert.assertTrue(found);
    }

    @Test
    public void verifyCorrectRegexMatchOfElementText(){
        TestCase tempTestCase = new TestCase(null, currentTestNameInternal.getMethodName());
        GenericInteractionMethods java = new GenericInteractionMethods(tempTestCase);
        JavaGuiElement button = new JavaGuiElement("OkButton", "Ok", JavaGuiElement.IdType.ELEMENT_TEXT);
        java.verifyElementTextIsRegexMatch(button, ".*k.*");
        boolean found = false;
        for(LogPost logPost : tempTestCase.testCaseLog.logPosts){
            if(logPost.logLevel == LogLevel.VERIFICATION_PASSED && logPost.message.contains("OkButton")){
                found = true;
            }
        }
        Assert.assertTrue(found);
    }

    @Test
    public void verifyFailedExactMatchOfElementText(){
        TestCase tempTestCase = new TestCase(null, currentTestNameInternal.getMethodName());
        GenericInteractionMethods java = new GenericInteractionMethods(tempTestCase);
        JavaGuiElement button = new JavaGuiElement("OkButton", "Ok", JavaGuiElement.IdType.ELEMENT_TEXT);
        java.verifyElementTextIsExactly(button, "Nok");
        boolean found = false;
        for(LogPost logPost : tempTestCase.testCaseLog.logPosts){
            if(logPost.logLevel == LogLevel.VERIFICATION_FAILED && logPost.message.contains("OkButton")){
                found = true;
            }
        }
        Assert.assertTrue(found);
    }

    @Test
    public void verifyFailedContainsMatchOfElementText(){
        TestCase tempTestCase = new TestCase(null, currentTestNameInternal.getMethodName());
        GenericInteractionMethods java = new GenericInteractionMethods(tempTestCase);
        JavaGuiElement button = new JavaGuiElement("OkButton", "Ok", JavaGuiElement.IdType.ELEMENT_TEXT);
        java.verifyElementTextContains(button, "Nok");
        boolean found = false;
        for(LogPost logPost : tempTestCase.testCaseLog.logPosts){
            if(logPost.logLevel == LogLevel.VERIFICATION_FAILED && logPost.message.contains("OkButton")){
                found = true;
            }
        }
        Assert.assertTrue(found);
    }

    @Test
    public void verifyFailedRegexMatchOfElementText(){
        TestCase tempTestCase = new TestCase(null, currentTestNameInternal.getMethodName());
        GenericInteractionMethods java = new GenericInteractionMethods(tempTestCase);
        JavaGuiElement button = new JavaGuiElement("OkButton", "Ok", JavaGuiElement.IdType.ELEMENT_TEXT);
        java.verifyElementTextIsRegexMatch(button, "No.*k");
        boolean found = false;
        for(LogPost logPost : tempTestCase.testCaseLog.logPosts){
            if(logPost.logLevel == LogLevel.VERIFICATION_FAILED && logPost.message.contains("OkButton")){
                found = true;
            }
        }
        Assert.assertTrue(found);
    }

    @Test
    @Ignore
    public void verifyIsDisplayedPositive(){
        TestCase tempTestCase = new TestCase(null, currentTestNameInternal.getMethodName());
        JavaTestApplicationRunner.showWindow();
        GenericInteractionMethods java = new GenericInteractionMethods(tempTestCase);
        JavaGuiElement button = new JavaGuiElement("OkButton", "Ok", JavaGuiElement.IdType.ELEMENT_TEXT);
        Assert.assertTrue(java.isDisplayedWithinTimeout(button, java.standardTimeout));
        JavaTestApplicationRunner.hideWindow();
    }

    @Test
    public void verifyExistPositiveShouldReturnTrue(){
        TestCase tempTestCase = new TestCase(null, currentTestNameInternal.getMethodName());
        GenericInteractionMethods java = new GenericInteractionMethods(tempTestCase);
        JavaGuiElement button = new JavaGuiElement("OkButton", "Ok", JavaGuiElement.IdType.ELEMENT_TEXT);
        Assert.assertTrue(java.exists(button));
    }

    @Test
    public void verifyIsDisplayedNegativeExistanceShouldReturnFalse(){
        TestCase tempTestCase = new TestCase(null, currentTestNameInternal.getMethodName());
        GenericInteractionMethods java = new GenericInteractionMethods(tempTestCase);
        JavaGuiElement button = new JavaGuiElement("OkButton", "Does not exist", JavaGuiElement.IdType.ELEMENT_TEXT);
        Assert.assertFalse(java.isDisplayedWithinTimeout(button, java.standardTimeout));
    }

    @Test
    public void verifyIsDisplayedExistsButNotDisplayedShouldReturnFalse(){
        TestCase tempTestCase = new TestCase(null, currentTestNameInternal.getMethodName());
        GenericInteractionMethods java = new GenericInteractionMethods(tempTestCase);
        JavaGuiElement button = new JavaGuiElement("OkButton", "Does not exist", JavaGuiElement.IdType.ELEMENT_TEXT);
        MethodInvoker.invokeMethod(tempTestCase, button.getRuntimeComponent(), "hide()");
        Assert.assertFalse(java.isDisplayedWithinTimeout(button, java.standardTimeout));
    }

    @Test
    public void verifyExistNegative(){
        TestCase tempTestCase = new TestCase(null, currentTestNameInternal.getMethodName());
        GenericInteractionMethods java = new GenericInteractionMethods(tempTestCase);
        JavaGuiElement button = new JavaGuiElement("OkButton", "Does not exist", JavaGuiElement.IdType.ELEMENT_TEXT);
        Assert.assertFalse(java.exists(button));
    }

    @Test
    @Ignore
    public void clickMethodShouldMoveMouseAndClick(){
        TestCase tempTestCase = new TestCase(null, currentTestNameInternal.getMethodName());
        JavaTestApplicationRunner.showWindow();
        GenericInteractionMethods java = new GenericInteractionMethods(tempTestCase);
        JavaGuiElement button = new JavaGuiElement("OkButton", "Ok", JavaGuiElement.IdType.ELEMENT_TEXT);
        Assert.assertTrue(JavaTestApplicationRunner.javaApp.okButton.isShowing());
        java.click(button);
        java.wait(2000);
        Assert.assertTrue(java.isDisplayedWithinTimeout(button, java.standardTimeout));
        JavaTestApplicationRunner.hideWindow();
    }

    @Test
    public void dropDownSelectionShouldWork(){
        TestCase tempTestCase = new TestCase(null, currentTestNameInternal.getMethodName());
        GenericInteractionMethods java = new GenericInteractionMethods(tempTestCase);
        JavaGuiElement dropDown = new JavaGuiElement("Pet dropdown", "Pet dropDown", JavaGuiElement.IdType.ELEMENT_NAME);
        java.getDropDownSelectedOption(dropDown);
        java.selectInDropdown(dropDown, "Bird");
        java.getDropDownSelectedOptions(dropDown);
    }

    @Test
    public void dropDownSelectionDoesNotExistShouldPrintAvalableOptions(){
        TestCase tempTestCase = new TestCase(null, currentTestNameInternal.getMethodName());
        GenericInteractionMethods java = new GenericInteractionMethods(tempTestCase);
        JavaGuiElement dropDown = new JavaGuiElement("Pet dropdown", "Pet dropDown", JavaGuiElement.IdType.ELEMENT_NAME);
        java.selectInDropdown(dropDown, "Boat");
        Assert.assertTrue(logPostIsFoundInLog(LogLevel.EXECUTION_PROBLEM, "Could not select 'Boat' in dropdown Pet dropdown'. Identified available choices were", tempTestCase));
        Assert.assertTrue(logPostIsFoundInLog(LogLevel.EXECUTION_PROBLEM, "'Dog'", tempTestCase));
    }

    @Test
    public void checkboxAwtSettingTest(){
        TestCase tempTestCase = new TestCase(null, currentTestNameInternal.getMethodName());
        GenericInteractionMethods java = new GenericInteractionMethods(tempTestCase);
        JavaGuiElement checkbox = new JavaGuiElement("Checkbox awt", "Checkbox awt", JavaGuiElement.IdType.ELEMENT_NAME);
        java.setCheckboxToChecked(checkbox);
        Assert.assertTrue(java.checkboxIsChecked(checkbox, 3));
    }

    @Test
    public void checkboxSwingSettingTest(){
        TestCase tempTestCase = new TestCase(null, currentTestNameInternal.getMethodName());
        GenericInteractionMethods java = new GenericInteractionMethods(tempTestCase);
        JavaGuiElement checkbox = new JavaGuiElement("Checkbox swing", "Checkbox swing", JavaGuiElement.IdType.ELEMENT_NAME);
        java.setCheckboxToChecked(checkbox);
        Assert.assertTrue(java.checkboxIsChecked(checkbox, 3));
    }

    @Test
    public void checkboxAwtUnSettingTest(){
        TestCase tempTestCase = new TestCase(null, currentTestNameInternal.getMethodName());
        GenericInteractionMethods java = new GenericInteractionMethods(tempTestCase);
        JavaGuiElement checkbox = new JavaGuiElement("Checkbox awt", "Checkbox awt", JavaGuiElement.IdType.ELEMENT_NAME);
        java.setCheckboxToUnChecked(checkbox);
        Assert.assertFalse(java.checkboxIsChecked(checkbox, 3));
    }

    @Test
    public void checkboxSwingUnSettingTest(){
        TestCase tempTestCase = new TestCase(null, currentTestNameInternal.getMethodName());
        GenericInteractionMethods java = new GenericInteractionMethods(tempTestCase);
        JavaGuiElement checkbox = new JavaGuiElement("Checkbox swing", "Checkbox swing", JavaGuiElement.IdType.ELEMENT_NAME);
        java.setCheckboxToUnChecked(checkbox);
        Assert.assertFalse(java.checkboxIsChecked(checkbox, 3));
    }
}