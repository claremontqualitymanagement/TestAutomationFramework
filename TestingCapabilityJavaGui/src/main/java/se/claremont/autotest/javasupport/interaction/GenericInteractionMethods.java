package se.claremont.autotest.javasupport.interaction;

import se.claremont.autotest.common.logging.LogFolder;
import se.claremont.autotest.common.logging.LogLevel;
import se.claremont.autotest.common.logging.LogPost;
import se.claremont.autotest.common.support.SupportMethods;
import se.claremont.autotest.common.testcase.TestCase;
import se.claremont.autotest.common.testrun.Settings;
import se.claremont.autotest.common.testrun.TestRun;
import se.claremont.autotest.javasupport.applicationundertest.ApplicationUnderTest;
import se.claremont.autotest.javasupport.objectstructure.GuiComponent;
import se.claremont.autotest.javasupport.objectstructure.JavaGuiElement;
import se.claremont.autotest.javasupport.objectstructure.JavaWindow;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Class enabling interaction with rich Java application GUIs
 *
 * Created by jordam on 2017-02-08.
 */
@SuppressWarnings({"SameParameterValue", "WeakerAccess"})
public class GenericInteractionMethods {
    public TestCase testCase;
    public int standardTimeout = 5;
    MethodInvoker methodInvoker;
    ApplicationUnderTest app;

    /**
     * Constructor to enable interaction with Java GUIs
     *
     * @param testCase The test case to log interactions to
     */
    public GenericInteractionMethods(TestCase testCase){
        this.testCase = testCase;
        methodInvoker = new MethodInvoker(testCase);
    }

    public void setApplicationUnderTest(ApplicationUnderTest app){
        this.app = app;
    }

    /**
     * Returns the parent element if that can be identified.
     *
     * @param component The component to find the parent of.
     * @return Returns the parent
     */
    public Object getParentComponent(Object component){
        Object parent = methodInvoker.invokeTheFirstEncounteredMethod(component, MethodDeclarations.componentParentGetterMethodsInAttemptOrder);
        if(parent == null){
            log(LogLevel.DEBUG, "Could not find any parent for element " + component.toString() + ".");
        }else {
            log(LogLevel.DEBUG, "Found parent for element.");
        }
        return parent;
    }

    /**
     * Returns the first parent that has the ability to hold child objects.
     *
     * @param component Component to find the container for
     * @return Returns the inclosing parent container
     */
    public Object getContainerComponent(Object component){
        Object parent = getParentComponent(component);
        if(parent == null){
            log(LogLevel.DEBUG, "Could not get any contaner component since no parent element could be identified.");
            return null;
        }
        if(methodInvoker.invokeTheFirstEncounteredMethod(parent, MethodDeclarations.subComponentGetterMethodsInAttemptOrder) != null){
            log(LogLevel.DEBUG, "Found container component.");
            return parent;
        } else {
            return getContainerComponent(parent);
        }
    }

    /**
     * Retrieves all sub-elements (recursively) of the object provided.
     *
     * @param component The component to identify sub-elements of
     * @return Returns a list of the sub-elements of the provided element
     */
    public ArrayList<Object> allSubElementsOf(Object component){
        if(component.getClass().equals(JavaGuiElement.class)) component = ((JavaGuiElement) component).getRuntimeComponent();

        ArrayList<Object> componentList = new ArrayList<>();
        Object[] returnList = (Object[]) MethodInvoker.invokeTheFirstEncounteredMethodFromListOfMethodNames(component, MethodDeclarations.subAllComponentsGettersMethodsInAttemptOrder);
        if(returnList != null && returnList.length > 0){
            for(Object object : returnList){
                componentList.add(object);
                componentList.addAll(addSubComponents(object));
            }
            return componentList;
        }
        Integer componentCount = (Integer) MethodInvoker.invokeTheFirstEncounteredMethodFromListOfMethodNames(component, MethodDeclarations.subComponentCountMethodsInAttemptOrder);
        if(componentCount == null)return componentList;
        for(int i = 0; i < componentCount; i++){
            Object subElement = MethodInvoker.invokeTheFirstEncounteredMethodFromListOfMethodNames(component, MethodDeclarations.subComponentGetterMethodsInAttemptOrder, i);
            if(subElement == null) continue;
            componentList.add(subElement);
            componentList.addAll(addSubComponents(subElement));
        }
        return componentList;
    }

    /**
     * Writes currently active window to the test case log
     */
    public void logCurrentActiveWindows(){
        if(app == null){
            log(LogLevel.EXECUTION_PROBLEM, "You need to use the method setApplicationUnderTest() in the GenericInteractionMethods class in order to use the logCurrentActiveWindows() method.");
            return;
        }
        app.logCurrentWindows(testCase);
    }


    /**
     * Returns the name of a component, as specified by the programmer when creating the GUI. Some elements do not get names.
     *
     * @param component The component to find the name of.
     * @return Returns the given name the application programmer gave it when creating the GUI.
     */
    public String getName(Object component){
        return (String) methodInvoker.invokeTheFirstEncounteredMethod(component, MethodDeclarations.componentNameGetterMethodsInAttemptOrder);
    }

    public static void takeScreenshot(TestCase testCase){
        GenericInteractionMethods g = new GenericInteractionMethods(testCase);
        g.takeScreenshot();
    }

    private BufferedImage GrabAllScreens(){
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] screens = ge.getScreenDevices();

        Rectangle allScreenBounds = new Rectangle();
        for (GraphicsDevice screen : screens) {
            Rectangle screenBounds = screen.getDefaultConfiguration().getBounds();

            allScreenBounds.width += screenBounds.width;
            allScreenBounds.height = Math.max(allScreenBounds.height, screenBounds.height);
        }

        Robot robot = null;
        try {
            robot = new Robot();
        } catch (AWTException e) {
            log(LogLevel.DEBUG, "Could not start Robot framework for taking desktop screenshot.");
            return null;
        }
        BufferedImage screenShot = robot.createScreenCapture(allScreenBounds);
        return  screenShot;
    }

    private String saveScreenshotToFile(BufferedImage screenShot){
        String filePath = LogFolder.testRunLogFolder + testCase.testName + TestRun.getFileCounter() + ".png";
        TestRun.increaseFileCounter();
        try {
            if (screenShot == null) {
                log(LogLevel.INFO, "Could not take desktop screenshot.");
                return null;
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(screenShot, "png", baos);
            baos.flush();
            byte[] imageInByte = baos.toByteArray();
            baos.close();

            SupportMethods.saveToFile(imageInByte, filePath);
        } catch (Exception e) {
            log(LogLevel.INFO, "Could not save desktop screenshot. Error: " + e.toString());
            return null;
        }
        return filePath;
    }


    /**
     * Saves a screenshot of all screens and writes its save path to the test case log.
     */
    public void takeScreenshot(){
        BufferedImage screenShot = GrabAllScreens();
        String fileName = saveScreenshotToFile(screenShot);
        logDesktopScreenshot(fileName);
    }

    private void logDesktopScreenshot(String filePath){
        String htmlFilePath = filePath.replace("\\", "/");
        if(!htmlFilePath.startsWith(TestRun.getSettingsValue(Settings.SettingParameters.HTML_REPORTS_LINK_PREFIX))){
            if(htmlFilePath.contains("://") && htmlFilePath.indexOf("://") < 7)
                htmlFilePath = htmlFilePath.substring(htmlFilePath.indexOf("://") + 3);
            htmlFilePath = TestRun.getSettingsValue(Settings.SettingParameters.HTML_REPORTS_LINK_PREFIX) + "://" + htmlFilePath;
        }
        testCase.logDifferentlyToTextLogAndHtmlLog(LogLevel.INFO, "Saved desktop screenshot as '" + filePath + "'.",
                "Saved desktop screenshot as <a href=\"" + htmlFilePath + "\" target=\"_blank\">" +
                        "<span class=\"screenshotfile\">" + filePath + "</span></a><br>" +
                        "<a href=\"" + htmlFilePath + "\" target=\"_blank\">" +
                        "<img src=\"" + htmlFilePath + "\" alt=\"browser screenshot\" class=\"screenshot\">" +
                        "</a>");
    }

    /**
     * Returns the current text of a GUI component.
     *
     * @param component The component to find text in.
     * @return The current text at runtime for the GUI component.
     */
    public String getText(Object component){
        String returnText = null;
        if(component == null) {
            log(LogLevel.DEBUG, "Could not retrieve any text from a null object.");
            return null;
        }
        returnText = (String) methodInvoker.invokeTheFirstEncounteredMethod(component, MethodDeclarations.textGettingMethodsInAttemptOrder);
        //Section below might prove useful, but in separate method, since some types of components introduce complex container structires and subcomponents around them.
        /*
        if(returnText == null || returnText.length() < 0){
            List<String> texts = new ArrayList<>();
            JavaGuiElement thisElement = new JavaGuiElement(component);
            for(JavaGuiElement subComponent : thisElement.getSubElements()){
                String subComponentText = getText(subComponent.getRuntimeElementCacheable());
                if(subComponentText != null) texts.add(subComponentText);
            }
            returnText = String.join(System.lineSeparator(), texts);
        }
        */
        return returnText;
    }


    /**
     * Performs a left mouse button click upon the given element.
     *
     * @param guiElement The element to click.
     */
    public void click(GuiComponent guiElement) {
        Object c = guiElement.getRuntimeComponent();
        if(c==null){
            log(LogLevel.EXECUTION_PROBLEM, "Could not click on element " + guiElement.getName() + " since it could not be identified." );
            log(LogLevel.INFO, "Identification procedure of " + guiElement.getName() + ":" + guiElement.getRecognitionDescription());
            takeScreenshot();
            if(testCase != null)
                testCase.report();
            return;
        }
        Point clickPoint = getClickablePoint(guiElement);
        if(clickPoint == null) return;
        Robot r = null;
        try {
            r = new Robot();
            r.mouseMove(clickPoint.x,clickPoint.y);
            r.mousePress( InputEvent.BUTTON1_MASK );
            r.mouseRelease( InputEvent.BUTTON1_MASK );
            log(LogLevel.EXECUTED, "Clicked the " + guiElement.getName() + " component.");
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    /**
     * Finds the element with the specified text and click on it. First attempt is exact match, then partial match, last regular expression pattern match is tried.
     *
     * @param window The JavaWindow to search for the element in.
     * @param textOfElementToClick The text to find (exact match, partial match, or regular expression pattern)
     */
    public void clickElementWithText(JavaWindow window, String textOfElementToClick){
        for(Object object: window.getComponents()){
            try{
                String objectText = (String)methodInvoker.invokeTheFirstEncounteredMethod(object, MethodDeclarations.textGettingMethodsInAttemptOrder);
                if(objectText != null && objectText.equals(textOfElementToClick)){
                    click(new JavaGuiElement(object));
                    return;
                }
            }catch (Exception e){
                log(LogLevel.DEBUG, "Could not retrieve text from object " + object.toString() + " or could turn it into a JavaGuiElement, or could not click it. Error: " + e.toString());
            }
        }
        for(Object object:  window.getComponents()){
            try{
                String objectText = (String)methodInvoker.invokeTheFirstEncounteredMethod(object, MethodDeclarations.textGettingMethodsInAttemptOrder);
                if(objectText != null && objectText.contains(textOfElementToClick)){
                    click(new JavaGuiElement(object));
                    return;
                }
            }catch (Exception e){
                log(LogLevel.DEBUG, "Could not retrieve text from object " + object.toString() + " or could turn it into a JavaGuiElement, or could not click it. Error: " + e.toString());
            }
        }
        for(Object object:  window.getComponents()){
            try{
                String objectText = (String)methodInvoker.invokeTheFirstEncounteredMethod(object, MethodDeclarations.textGettingMethodsInAttemptOrder);
                if(objectText != null && SupportMethods.isRegexMatch(objectText, textOfElementToClick)){
                    click(new JavaGuiElement(object));
                    return;
                }
            }catch (Exception e){
                log(LogLevel.DEBUG, "Could not retrieve text from object " + object.toString() + " or could turn it into a JavaGuiElement, or could not click it. Error: " + e.toString());
            }
        }
    }

    /**
     * Finds the element with the specified text and click on it. First attempt is exact match, then partial match, last regular expression pattern match is tried.
     *
     * @param parentElement The object where to look for sub-elements with the specified text.
     * @param textOfElementToClick The text to find (exact match, partial match, or regular expression pattern)
     */
    public void clickElementWithText(GuiComponent parentElement, String textOfElementToClick){
        String elementText = getText(parentElement);
        if(elementText != null && (elementText.equals(textOfElementToClick) || elementText.contains(textOfElementToClick) || SupportMethods.isRegexMatch(elementText, textOfElementToClick))){
            click(parentElement);
            log(LogLevel.EXECUTED, "Choosed '" + textOfElementToClick + "' in radiobutton " + parentElement.getName() + ".");
            return;
        }
        JavaGuiElement javaGuiElement = (JavaGuiElement) parentElement;
        for(Object object: javaGuiElement.getSubElements()){
            try{
                String objectText = (String)methodInvoker.invokeTheFirstEncounteredMethod(object, MethodDeclarations.textGettingMethodsInAttemptOrder);
                if(objectText != null && objectText.equals(textOfElementToClick)){
                    click(new JavaGuiElement(object));
                    return;
                }
            }catch (Exception e){
                log(LogLevel.DEBUG, "Could not retrieve text from object " + object.toString() + " or could turn it into a JavaGuiElement, or could not click it. Error: " + e.toString());
            }
        }
        for(Object object: javaGuiElement.getSubElements()){
            try{
                String objectText = (String)methodInvoker.invokeTheFirstEncounteredMethod(object, MethodDeclarations.textGettingMethodsInAttemptOrder);
                if(objectText != null && objectText.contains(textOfElementToClick)){
                    click(new JavaGuiElement(object));
                    return;
                }
            }catch (Exception e){
                log(LogLevel.DEBUG, "Could not retrieve text from object " + object.toString() + " or could turn it into a JavaGuiElement, or could not click it. Error: " + e.toString());
            }
        }
        for(Object object: javaGuiElement.getSubElements()){
            try{
                String objectText = (String)methodInvoker.invokeTheFirstEncounteredMethod(object, MethodDeclarations.textGettingMethodsInAttemptOrder);
                if(objectText != null && SupportMethods.isRegexMatch(objectText, textOfElementToClick)){
                    click(new JavaGuiElement(object));
                    return;
                }
            }catch (Exception e){
                log(LogLevel.DEBUG, "Could not retrieve text from object " + object.toString() + " or could turn it into a JavaGuiElement, or could not click it. Error: " + e.toString());
            }
        }
    }

    /**
     * Pauses execution the stated time.
     *
     * @param milliseconds Number of milliseconds to pause
     */
    public void wait(int milliseconds){
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            //Ignored
        }
    }

    /**
     * Writes the given text to the given component. Checks afterwards that the entered
     * characters are in the object, and that they appear in the same order as written.
     * This procedure is because some elements reformat input texts.
     *
     * @param guiComponent Element to write to.
     * @param textToWrite Text to write.
     */
    public void write(GuiComponent guiComponent, String textToWrite){
        performWrite(guiComponent, textToWrite, true);
    }

    /**
     * Writes the given text to the given component.
     *
     * @param guiComponent Element to write to.
     * @param textToWrite Text to write.
     */
    public void writeWithoutCheck(GuiComponent guiComponent, String textToWrite){
        performWrite(guiComponent, textToWrite, false);
    }

    /**
     * When implemented, this method will set radio button status.
     *
     * @param guiElement The radio button element to interact with
     * @param textOfElementToChoose The text of the option to choose. First attempt is exact match, then partial match, then regular expression pattern
     */
    public void chooseRadioButton(GuiComponent guiElement, String textOfElementToChoose) {
        JavaGuiElement javaGuiElement = null;
        try{
            javaGuiElement = (JavaGuiElement) guiElement;
        }catch (Exception e){
            log(LogLevel.DEBUG, "Could not convert element " + guiElement.getName() + " to a JavaGuiElement to use it as a RadioButton.");
            takeScreenshot();
            return;
        }
        clickElementWithText(javaGuiElement, textOfElementToChoose);
        log(LogLevel.EXECUTED, "Choosed '" + textOfElementToChoose + "' in radiobutton " + javaGuiElement.getName() + ".");
    }

    /**
     * Selects a value in a dropdown. First an exact match is attempted. If the exact match fails a search for an
     * option containing the given selection is attempted to be found. If that attempt fails too a regular expression
     * pattern search is attempted.
     *
     * @param guiElement The dropdown element (ComboBox) to select a value in
     * @param selection The selection expected to be able to make.
     */
    public void selectInDropdown(GuiComponent guiElement, String selection) {
        Object component = guiElement.getRuntimeComponent();
        if(component == null){
            log(LogLevel.EXECUTION_PROBLEM, "Could not identify " + guiElement.getName() + ". Tried by identification procedure:" + guiElement.getRecognitionDescription());
            return;
        } else {
            log(LogLevel.DEBUG, "Identified element " + guiElement.getName() + " by:" + guiElement.getRecognitionDescription());
        }
        Integer optionCount = (Integer) MethodInvoker.invokeTheFirstEncounteredMethod(testCase, component, MethodDeclarations.getItemCountOfComboBoxOptions);
        if(optionCount == null) {
            log(LogLevel.EXECUTION_PROBLEM, "Could not retrieve item count of combobox " + guiElement.getName() + ".");
            return;
        }
        List<String> options = new ArrayList<>();
        for(int i = 0; i < optionCount; i++){
            Object optionObject = MethodInvoker.invokeTheFirstEncounteredMethod(testCase, component, MethodDeclarations.getSpecificComboBoxItemBasedOnIndex, i);
            if(optionObject == null) continue;
            String option = optionObject.toString();
            options.add(option);
        }
        log(LogLevel.DEBUG, "Available options in dropdown " + guiElement.getName() + ": '" + String.join("', '", options + "'."));

        for(int i = 0; i < optionCount; i++){
            Object optionObject = MethodInvoker.invokeTheFirstEncounteredMethod(testCase, component, MethodDeclarations.getSpecificComboBoxItemBasedOnIndex, i);
            if(optionObject == null) continue;
            String option = optionObject.toString();
            if(option.equals(selection)){
                log(LogLevel.EXECUTED, "Selecting '" + selection + "' in dropdown " + guiElement.getName() + ".");
                MethodInvoker.invokeTheFirstEncounteredMethod(testCase, component, MethodDeclarations.setSelectionItemBasedOnIndex, i);
                return;
            }
        }
        for(int i = 0; i < optionCount; i++){
            Object optionObject = MethodInvoker.invokeTheFirstEncounteredMethod(testCase, component, MethodDeclarations.getSpecificComboBoxItemBasedOnIndex, i);
            if(optionObject == null) continue;
            String option = optionObject.toString();
            if(option.contains(selection)){
                log(LogLevel.EXECUTED, "Selecting '" + selection + "' in dropdown " + guiElement.getName() + ". Actual selection option was '" + option + "'.");
                MethodInvoker.invokeTheFirstEncounteredMethod(testCase, component, MethodDeclarations.setSelectionItemBasedOnIndex, i);
                return;
            }
        }
        for(int i = 0; i < optionCount; i++){
            Object optionObject = MethodInvoker.invokeTheFirstEncounteredMethod(testCase, component, MethodDeclarations.getSpecificComboBoxItemBasedOnIndex, i);
            if(optionObject == null) continue;
            String option = optionObject.toString();
            if(SupportMethods.isRegexMatch(option, selection)){
                log(LogLevel.EXECUTED, "Selecting '" + option + "' in dropdown " + guiElement.getName() + " based on regular expression pattern '" + selection + "'.");
                MethodInvoker.invokeTheFirstEncounteredMethod(testCase, component, MethodDeclarations.setSelectionItemBasedOnIndex, i);
                return;
            }
        }
        log(LogLevel.EXECUTION_PROBLEM, "Could not select '" + selection + "' in dropdown " + guiElement.getName() + "'. Identified available choices were:" + System.lineSeparator() + "'" + String.join("'" + System.lineSeparator() + "'", options) + "'");
    }

    /**
     * Returns the currently selected value of a dropdown
     *
     * @param guiComponent The dropdown
     * @return The selection
     */
    public String getDropDownSelectedOption(GuiComponent guiComponent){
        List<String> selections = getDropDownSelectedOptions(guiComponent);
        if(selections.size() == 1) return selections.get(0);
        log(LogLevel.DEBUG, "Several selections of dropdown " + guiComponent.getName() + ". Selected values are '" + String.join("', '", selections) + "'. Returning all selections.");
        return "[" + String.join("], [", selections) + "]";
    }

    /**
     * Returns a list of the selected options of a dropdown
     *
     * @param guiElement The dropdown
     * @return List of selected strings
     */
    public List<String> getDropDownSelectedOptions(GuiComponent guiElement){
        List<String> selections = new ArrayList<>();
        Object component = guiElement.getRuntimeComponent();
        if(component == null){
            log(LogLevel.EXECUTION_PROBLEM, "Could not identify " + guiElement.getName() + ". Tried by identification procedure:" + guiElement.getRecognitionDescription());
            return selections;
        } else {
            log(LogLevel.DEBUG, "Identified element " + guiElement.getName() + " by:" + guiElement.getRecognitionDescription());
        }
        Object[] selectedObjects = (Object[]) MethodInvoker.invokeTheFirstEncounteredMethod(testCase, component, MethodDeclarations.getAllSelectedObjectsInComboBox);
        if(selectedObjects == null) {
            log(LogLevel.EXECUTION_PROBLEM, "Could not identify any selections in " + guiElement.getName() + ".");
            return selections;
        } else if(selectedObjects.length == 0){
            log(LogLevel.DEBUG, "Nothing seem to be selected in " + guiElement.getName() + ".");
        }
        for(Object o : selectedObjects){
            selections.add(o.toString());
        }
        if(selections.size() > 0){
            log(LogLevel.DEBUG, "DropDown " + guiElement.getName() + " contained selected element(s) '" + String.join("', '", selections) + "'.");
        }
        return selections;

    }

    /**
     * Retrieves the text of a runtime element and verifies the text against the given oracle.
     * This verification requires an exact string match to pass.
     *
     * @param guiElement The GUI element to grep the text of
     * @param expectedText The oracle text to verify against
     */
    public void verifyElementTextIsExactly(GuiComponent guiElement, String expectedText){
        String text = getText(guiElement);
        if(text == null){
            log(LogLevel.VERIFICATION_PROBLEM, "Cannot find any text in element " + guiElement.getName() + ".");
        } else if (text.equals(expectedText)){
            log(LogLevel.VERIFICATION_PASSED, "Found text '" + text + "' in element " + guiElement.getName() + ".");
        } else {
            log(LogLevel.VERIFICATION_FAILED, "Expected to find the text '" + expectedText + "' in element " + guiElement.getName() + " but the actual value was '" + text + "'.");
            takeScreenshot();
        }
    }

    /**
     * Retrieves the text of a runtime element and verifies the text against the given oracle.
     * This verification will pass of the expected oracle text is found within the text of the
     * runtime GUI element.
     *
     * @param guiElement The GUI element to grep the text of
     * @param expectedText The oracle text to verify against
     */
    public void verifyElementTextContains(GuiComponent guiElement, String expectedText){
        String text = getText(guiElement);
        if(text == null){
            log(LogLevel.VERIFICATION_PROBLEM, "Cannot find any text in element " + guiElement.getName() + ".");
        } else if (text.contains(expectedText)){
            log(LogLevel.VERIFICATION_PASSED, "The text '" + expectedText + "' was found in element " + guiElement.getName() + ". Full text was: '" + text + "'.");
        } else {
            log(LogLevel.VERIFICATION_FAILED, "Expected to find the text '" + expectedText + "' in element " + guiElement.getName() + " but the actual value was '" + text + "'.");
            takeScreenshot();
        }
    }

    /**
     * Retrieves the text of a runtime element and verifies the text against the given oracle.
     * This verification passes if the actual text in the runtime component passes a regular
     * expresseion matching with the given pattern string.
     *
     * @param guiElement The GUI element to grep the text of
     * @param pattern The regular expression pattern to verify against
     */
    public void verifyElementTextIsRegexMatch(GuiComponent guiElement, String pattern){
        String text = getText(guiElement);
        if(text == null){
            log(LogLevel.VERIFICATION_PROBLEM, "Cannot find any text in element " + guiElement.getName() + ".");
        } else if (SupportMethods.isRegexMatch(text, pattern)){
            log(LogLevel.VERIFICATION_PASSED, "Found text matching the pattern '" + pattern + "' in element " + guiElement.getName() + ". Full text was: '" + text + "'.");
        } else {
            log(LogLevel.VERIFICATION_FAILED, "Expected to find text matching the regular expression pattern '" + pattern + "' in element " + guiElement.getName() + " but the actual value was '" + text + "'.");
            takeScreenshot();
        }
    }



    /**
     * Returns the runtime text of the element.
     *
     * @param guiElement The element to get the text of.
     * @return Returns the current text of the runtime element
     */
    public String getText(GuiComponent guiElement) {
        Object component = guiElement.getRuntimeComponent();
        if(component == null){
            log(LogLevel.INFO, "Could not identify " + guiElement.getName() + ". Tried by identification procedure:" + guiElement.getRecognitionDescription());
            return null;
        } else {
            log(LogLevel.DEBUG, "Identified element " + guiElement.getName() + " by:" + guiElement.getRecognitionDescription());
        }
        String text = getText(component);
        if(text == null){
            log(LogLevel.DEBUG, "Tried retrieving text from element " + guiElement.getName() + " but got null.");
        } else {
            log(LogLevel.DEBUG, "Retrieved the text '" + text + "' from element " + guiElement.getName() + ".");
        }
        return text;
    }

    /**
     * Checks if an element exists in the GUI. Make note that an element might exist, but not be displayed.
     *
     * @param guiElement The GUI element to verify existance of.
     * @return Returns true if the element exist, regardless if it is displayed or not.
     */
    public boolean exists(GuiComponent guiElement) {
        if(guiElement.getRuntimeComponent() != null){
            log(LogLevel.DEBUG, "Checked if element " + guiElement.getName() + " could be identified and it was, by the procedure:" + guiElement.getRecognitionDescription());
            return true;
        } else {
            log(LogLevel.DEBUG, "Checked if element " + guiElement.getName() + " could be identified and it was not. Identification attempts by the procedure:" + guiElement.getRecognitionDescription());
            return false;
        }
    }

    /**
     * Checks if a component exist, but actually gives it time to appear in the GUI.
     *
     * @param guiElement Element to check existance of.
     * @param timeoutInSeconds Timeout to wait.
     * @return Returns true if found.
     */
    public boolean existsWithTimeout(GuiComponent guiElement, int timeoutInSeconds) {
        long startTime = System.currentTimeMillis();
        if(guiElement == null) {
            log(LogLevel.DEBUG, "Could not check existance of null element.");
            return false;
        }
        while(System.currentTimeMillis() - startTime < timeoutInSeconds*1000){
            if(guiElement.getRuntimeComponent() != null) {
                log(LogLevel.DEBUG, "Checked if element " + guiElement.getName() + " could be identified within a " + timeoutInSeconds + " second timeout, and it was identified after " + (System.currentTimeMillis() - startTime) + " milliseconds by the identification procedure: " + guiElement.getRecognitionDescription());
                return true;
            }
            wait(50);
        }
        log(LogLevel.DEBUG, "Checked if element " + guiElement.getName() + " could be identified within a " + timeoutInSeconds + " second timeout, and it did not. Identification attempts procedure: " + guiElement.getRecognitionDescription());
        return false;
    }

    /**
     * Checks if an element does not exist at runtime.
     *
     * @param guiElement Element to check for
     * @return Returns false if element is identified.
     */
    public boolean doesNotExist(GuiComponent guiElement){
        boolean found = exists(guiElement);
        if(found) {
            log(LogLevel.DEBUG, "Checked if element " + guiElement.getName() + " did not exist, but it does indeed exist.");
        } else {
            log(LogLevel.DEBUG, "Checked if element " + guiElement.getName() + " did not exist, and it did not.");
        }
        return !found;
    }

    /**
     * Checks if an element exist, but gives the element time to disappear for a timeout.
     *
     * @param guiElement The element to verify does not exist
     * @param timeoutInSeconds The time to wait for the element to disappear, if needed
     * @return Returns true if the element does not exist, or disappear within the stated
     * timeout. If the element still exist after the timeout it returns false.
     */
    public boolean doesNotExistWithTimeout(GuiComponent guiElement, int timeoutInSeconds){
        long startTime = System.currentTimeMillis();
        while(System.currentTimeMillis() - startTime < timeoutInSeconds * 1000){
            if(!exists(guiElement)) return true;
            wait(50);
        }
        return false;
    }

    /**
     * Checks if the element gets displayed within the timeout.
     *
     * @param guiElement The element to check.
     * @param timeoutInSeconds The number of seconds to wait for the element to get displayed.
     * @return Returns true if the element gets displayed within the given timeout period, othervise fale.
     */
    public boolean isDisplayedWithinTimeout(GuiComponent guiElement, int timeoutInSeconds) {
        long startTime = System.currentTimeMillis();
        if(!existsWithTimeout(guiElement, timeoutInSeconds)){
            log(LogLevel.DEBUG, "Checked if element " + guiElement.getName() + " is displayed, but it does not exist.");
            return false;
        }
        Boolean shown = (Boolean) methodInvoker.invokeTheFirstEncounteredMethod(guiElement.getRuntimeComponent(), MethodDeclarations.componentIsVisibleMethodsInAttemptOrder);
        if(shown == null){
            log(LogLevel.DEBUG, "Check if element " + guiElement.getName() + " is displayed, and applicable method does not exist.");
            return false;
        } else if(shown){
            log(LogLevel.DEBUG, "Check if element " + guiElement.getName() + " is displayed, and it is.");
            return true;
        }
        while (System.currentTimeMillis() - startTime < timeoutInSeconds*1000){
            shown = (Boolean) methodInvoker.invokeTheFirstEncounteredMethod(guiElement.getRuntimeComponent(), MethodDeclarations.componentIsVisibleMethodsInAttemptOrder);
            if(shown){
                log(LogLevel.DEBUG, "Check if element " + guiElement.getName() + " is displayed, and it is.");
                return true;
            }
            wait(50);
        }
        log(LogLevel.DEBUG, "Check if element " + guiElement.getName() + " is displayed, and it is not displayed.");
        return false;
    }

    /**
     * Checks if the element is currently displayed (no timeout, no waiting), meaning that it exists, and is set as visible in the GUI.
     *
     * @param guiElement The element to check.
     * @return Returns true if element is identified, it exists, and it is visible.
     */
    public boolean isDisplayed(GuiComponent guiElement){
        Boolean displayed = (Boolean)methodInvoker.invokeTheFirstEncounteredMethod(guiElement, MethodDeclarations.componentIsVisibleMethodsInAttemptOrder);
        if(displayed == null){
            log(LogLevel.FRAMEWORK_ERROR, "No applicable method seemed to be found for checking if " + guiElement.getName() + " was displayed.");
            return false;
        } else if (displayed){
            log(LogLevel.DEBUG, "Checked if " + guiElement.getName() + " was displayed and it was.");
            return true;
        } else {
            log(LogLevel.DEBUG, "Checked if " + guiElement.getName() + " was displayed and it was not.");
            return false;
        }
    }

    /**
     * Checks if the element is currently not displayed (no timeout, no waiting), meaning that it does not exists, or it is set as non-visible in the GUI.
     *
     * @param guiElement The element to check.
     * @return Returns false if element is identified, it exists, and it is visible.
     */
    public boolean isNotDisplayed(GuiComponent guiElement) {
        return !isDisplayed(guiElement);
    }

    /**
     * Checks if the element get displayed within the timeout.
     *
     * @param guiComponent The element to check
     * @param timeoutInSeconds The timout to wait for the element to get displayed
     * @return Returns true if the element is identified, and is visible during the timeout
     */
    public boolean isNotDisplayedWithTimeout(GuiComponent guiComponent, int timeoutInSeconds){
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < timeoutInSeconds * 1000){
            if(isNotDisplayed(guiComponent)){
                return true;
            }
            wait(50);
        }
        return false;
    }

    /**
     * Verifies that a GUI element is displayed, and writes the outcome to the test case log.
     *
     * @param guiElement The GUI element to check if it is displayed
     */
    public void verifyObjectIsDisplayed(GuiComponent guiElement) {
        if(isDisplayedWithinTimeout(guiElement, standardTimeout)){
            log(LogLevel.VERIFICATION_PASSED, "Verified that element " + guiElement.getName() + " is displayed.");
        } else {
            log(LogLevel.VERIFICATION_FAILED, "Tried to verify that element " + guiElement.getName() + " was displayed, but it does not seem to be displayed.");
            takeScreenshot();
        }
    }

    /**
     * Verifies that a GUI element is not displayed, and writes the outcome to the test case log.
     *
     * @param guiElement The GUI element to check if it is displayed.
     */
    public void verifyObjectIsNotDisplayed(GuiComponent guiElement) {
        if(!isDisplayedWithinTimeout(guiElement, standardTimeout)){
            log(LogLevel.VERIFICATION_PASSED, "Verified that element " + guiElement.getName() + " is not displayed.");
        } else {
            log(LogLevel.VERIFICATION_FAILED, "Tried to verify that element " + guiElement.getName() + " was not displayed, but it seem to be displayed.");
            takeScreenshot();
        }
    }

    /**
     * Verifies that a GUI element exists. An element can exist but not be displayed.
     * Use verifyObjectIsDisplayed() if you want to make sure the element is visible.
     *
     * @param guiElement The GUI element to check existance of.
     */
    public void verifyObjectExistence(GuiComponent guiElement) {
        if(existsWithTimeout(guiElement, standardTimeout)){
            log(LogLevel.VERIFICATION_PASSED, "Existance of " + guiElement.getName() + " verified.");
        } else {
            log(LogLevel.VERIFICATION_FAILED, "Verification of existance of " + guiElement.getName() + " failed. Could not find element.");
            takeScreenshot();
        }
    }

    /**
     * Verifies object existance, but gives the element time to appear. Writes results to log.
     * An element can exist but set to not be visible. Other methods check if elements are displayed.
     *
     * @param guiElement The GUI element to check for.
     * @param timeoutInSeconds The timeout to wait for the element to become present.
     */
    public void verifyObjectExistenceWithTimeout(GuiComponent guiElement, int timeoutInSeconds) {
        if(existsWithTimeout(guiElement, timeoutInSeconds)){
            log(LogLevel.VERIFICATION_PASSED, "Existance of " + guiElement.getName() + " verified.");
        } else {
            log(LogLevel.VERIFICATION_FAILED, "Verification of existance of " + guiElement.getName() + " failed. Could not find element.");
            takeScreenshot();
        }
    }

    /**
     * Sets a checkbox to be checked (=true).
     *
     * @param component The checkbox element
     */
    public void setCheckboxToChecked(GuiComponent component){
        setCheckboxToState(component, true, standardTimeout);
    }

    /**
     * Sets a checkbox to be un-checked (=false).
     *
     * @param component The checkbox element
     */
    public void setCheckboxToUnChecked(GuiComponent component){
        setCheckboxToState(component, false, standardTimeout);
    }

    /**
     * Sets a checkbox element to selected state.
     *
     * @param component The checkbox element
     * @param expectedEndState The expected state to set the element to (checked = true, un-checked=false).
     */
    public void setCheckboxToState(GuiComponent component, boolean expectedEndState){
        setCheckboxToState(component, expectedEndState, standardTimeout);
    }

    /**
     * Tries to set a checkbox element to selected state for a certain number of seconds to enable elements to appear or to get enabled.
     *
     * @param component The checkbox element
     * @param expectedEndState The expected state to set the element to (checked = true, un-checked=false).
     * @param timeoutInSeconds The time, in seconds to wait for successful checkbox interaction
     */
    public void setCheckboxToState(GuiComponent component, boolean expectedEndState, int timeoutInSeconds){
        long startTime = System.currentTimeMillis();
        JavaGuiElement javaGuiElement = null;
        try {
            javaGuiElement = (JavaGuiElement)component;
        }catch (Exception e){
            log(LogLevel.EXECUTION_PROBLEM, "Could not identify " + component.getName() + " as any object with implementation to interact with. The class is " + component.getClass().toString() + ".");
            takeScreenshot();
            return;
        }
        if(!existsWithTimeout(javaGuiElement, timeoutInSeconds)) {
            log(LogLevel.EXECUTION_PROBLEM, "Could not make sure checkbox " + component.getName() + " was checked since it could not be identified within the timeout of " + timeoutInSeconds + " seconds.");
            takeScreenshot();
            return;
        }
        Boolean selected = !expectedEndState;
        while (selected != expectedEndState && System.currentTimeMillis() - startTime < timeoutInSeconds * 1000){
            selected = (Boolean) MethodInvoker.invokeTheFirstEncounteredMethod(null, component.getRuntimeComponent(), MethodDeclarations.getCheckboxCurrentStatus);
            if(selected == null){
                selected = !expectedEndState;
            } else if(selected != expectedEndState){
                MethodInvoker.invokeTheFirstEncounteredMethod(testCase, component.getRuntimeComponent(), MethodDeclarations.setCheckboxCurrentStatus, expectedEndState);
                selected = (Boolean) MethodInvoker.invokeTheFirstEncounteredMethod(null, component.getRuntimeComponent(), MethodDeclarations.getCheckboxCurrentStatus);
                if(selected == expectedEndState){
                    log(LogLevel.EXECUTED, "Made sure checkbox " + javaGuiElement.getName() + " was " + String.valueOf(expectedEndState).toLowerCase().replace("false", "un-").replace("true", "") + "checked.");
                    return;
                } else {
                    log(LogLevel.EXECUTION_PROBLEM, "Could not set checkbox " + javaGuiElement.getName() + " to " + String.valueOf(expectedEndState).replace("true", "checked.").replace("false", "un-checked."));
                    takeScreenshot();
                    return;
                }
            }
            wait(50);
        }
        log(LogLevel.EXECUTION_PROBLEM, "Could not make sure checkbox " + javaGuiElement.getName() + " was in state " + String.valueOf(expectedEndState).toLowerCase().replace("false", "un-").replace("true", "") + "checked.");
        if(!methodInvoker.objectHasAnyOfTheMethods(javaGuiElement.getRuntimeElementCacheable(), MethodDeclarations.getCheckboxCurrentStatus)){
            log(LogLevel.INFO, "Element of class " + javaGuiElement.getRuntimeElementCacheable().getClass().toString() + " has the following methods implemented '" + String.join("', '", methodInvoker.getAvalableMethods(javaGuiElement.getRuntimeElementCacheable())) + "'. Tried invoking the methods '" + String.join("', '", MethodDeclarations.getCheckboxCurrentStatus) + "'.");
        }
        takeScreenshot();
    }

    /**
     * Returns the current status of a checkbox.
     *
     * @param component The checkbox element
     * @param timeoutInSeconds The number of seconds to wait for the checkbox appearance
     * @return Returns true if the checkbox is checked, and false if the checbox is checked or if the checkbox is not found.
     */
    public boolean checkboxIsChecked(GuiComponent component, int timeoutInSeconds){
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis()-startTime < timeoutInSeconds *1000){
            Boolean isChecked = (Boolean) MethodInvoker.invokeTheFirstEncounteredMethod(null, component.getRuntimeComponent(), MethodDeclarations.getCheckboxCurrentStatus);
            if(isChecked != null) {
                log(LogLevel.DEBUG, "Checkbox " + component.getName() + " identified and was identified as " + String.valueOf(isChecked).toLowerCase().replace("true", "checked.").replace("false", "un-checked."));
                return isChecked;
            }
            wait(50);
        }
        if(methodInvoker.objectHasAnyOfTheMethods(component.getRuntimeComponent(), MethodDeclarations.getCheckboxCurrentStatus)){
            log(LogLevel.EXECUTION_PROBLEM, "Something went wrong trying to get checkbox status for " + component.getName() + ".");
        } else {
            log(LogLevel.EXECUTION_PROBLEM, "Checkbox " + component.getName() + " did not have any implementations of the methods '" + String.join("', '", MethodDeclarations.getCheckboxCurrentStatus) + "'. Available methods are:" + System.lineSeparator() + methodInvoker.getAvalableMethods(component.getRuntimeComponent()));
        }
        return false;
    }

    /**
     * Verifies the status of a checkbox element.
     *
     * @param component The checkbox element
     * @param expectedStatus True if the expectation is that the checkbox should be checked, othervise false.
     * @param timeoutInSeconds The timeout to wait for the checkbox element to appear and get the expected status.
     */
    public void verifyCheckboxStatus(GuiComponent component, boolean expectedStatus, int timeoutInSeconds){
        if(checkboxIsChecked(component, timeoutInSeconds) == expectedStatus){
            log(LogLevel.VERIFICATION_PASSED, "Checkbox " + component.getName() + " was " + String.valueOf(expectedStatus).toLowerCase().replace("true", "checked").replace("false", "un-checked") + " as expected.");
        } else {
            log(LogLevel.VERIFICATION_FAILED, "Checkbox " + component.getName() + " was not " + String.valueOf(expectedStatus).toLowerCase().replace("true", "checked").replace("false", "un-checked") + " as expected.");
            takeScreenshot();
        }
    }

    /**
     * Verifies the status of a checkbox element.
     *
     * @param component The checkbox element
     * @param expectedStatus True if the expectation is that the checkbox should be checked, othervise false.
     */
    public void verifyCheckboxStatus(GuiComponent component, boolean expectedStatus){
        verifyCheckboxStatus(component, expectedStatus, standardTimeout);
    }

    /**
     * Verifies that a checkbox element is checked.
     *
     * @param component The checkbox element
     */
    public void verifyCheckboxIsChecked(GuiComponent component){
        verifyCheckboxStatus(component, true);
    }

    /**
     * Verifies that a checkbox element is un-checked.
     *
     * @param component The checkbox element
     */
    public void verifyCheckboxIsUnChecked(GuiComponent component){
        verifyCheckboxStatus(component, false);
    }

    public void verifyImage(GuiComponent guiElement, String s) {
        log(LogLevel.FRAMEWORK_ERROR, "The image verification method is not yet implemented. Sorry. Please contribute by implementing it.");
    }

    /**
     * Retrieves a point in the center of the element provided, if the element can be found.
     *
     * @param guiElement The element to find click point of
     * @return Returns a Point in the middle of the element.
     */
    private Point getClickablePoint(GuiComponent guiElement){
        Object component = guiElement.getRuntimeComponent();
        if(component == null){
            log(LogLevel.DEBUG, "Could not get clickable point from " + guiElement.getName() + " since it could not be identified.");
            return null;
        }
        Point clickPoint = null;
        try{
            Point upperLeftCorner = (Point) methodInvoker.invokeTheFirstEncounteredMethod(component, MethodDeclarations.componentLocationGetterMethodsInAttemptOrder);
            int width = (int) methodInvoker.invokeTheFirstEncounteredMethod(component, MethodDeclarations.componentWidthGetterMethodsInAttemptOrder);
            int height = (int) methodInvoker.invokeTheFirstEncounteredMethod(component, MethodDeclarations.componentHightGetterMethodsInAttemptOrder);
            clickPoint = new Point(upperLeftCorner.x + width/2, upperLeftCorner.y + height/2);
            log(LogLevel.DEBUG, "Found clickable point for component " + guiElement.getName() + ": " + clickPoint.x + "x" + clickPoint.y + ".");
        }catch (Exception e){
            log(LogLevel.DEBUG, "Could not retrieve clickable point from component " + guiElement.getName() + " of class '" + component.getClass().toString() + "'");
        }
        return clickPoint;
    }

    /**
     * Writes to test case if provided, and to console.
     *
     * @param logLevel Log level of this log post
     * @param message Log message
     */
    private void log(LogLevel logLevel, String message){
        System.out.println(new LogPost(logLevel, message, "", "", "", "").toString());
        if(testCase != null)
            testCase.log(logLevel, message);
    }

    /**
     * Writes text to a component, and makes sure the text is there.
     *
     * @param guiElement The GUI component to set the text of.
     * @param textToWrite The text to enter to the component.
     */
    private void performWrite(GuiComponent guiElement, String textToWrite, boolean performCheckAfterwards) {
        Object component = guiElement.getRuntimeComponent();
        if(component == null){
            log(LogLevel.EXECUTION_PROBLEM, "Could not write '" + textToWrite + "' to component " + guiElement.getName() + " since it could not be identified.");
            log(LogLevel.INFO, "Identification procedure for " + guiElement.getName() + ":" + guiElement.getRecognitionDescription());
            takeScreenshot();
            return;
        }
        if(performCheckAfterwards){
            for(String methodName : MethodDeclarations.componentWriteMethodsInAttemptOrder){
                methodInvoker.invokeMethod(component, methodName, textToWrite);
                String actualText = getText(guiElement);
                if(actualText.equals(textToWrite)){
                    log(LogLevel.EXECUTED, "Wrote '" + textToWrite + "' to component " + guiElement.getName() + ".");
                    return;
                } else if(allCharactersExistAndInCorrectOrder(actualText, textToWrite)){ //Sometimes for example dates are formatted
                    log(LogLevel.EXECUTED, "Wrote '" + textToWrite + "' to component " + guiElement.getName() + ". Text after write is '" + actualText + "'.");
                    return;
                } else {
                    log(LogLevel.DEBUG, "Tried writing the text '" + textToWrite + "' to " + guiElement.getName() + " with the method '" + methodName +
                            "' but the resulting text is '" + actualText + ". If this still is correct you could use the write method not performing checks afterwards.");
                }
            }
        } else {
            methodInvoker.invokeTheFirstEncounteredMethod(component, MethodDeclarations.componentWriteMethodsInAttemptOrder, textToWrite);
            log(LogLevel.EXECUTED, "Wrote '" + textToWrite + "' to component " + guiElement.getName() + ", without making sure text was correct.");
            return;
        }
        log(LogLevel.EXECUTION_PROBLEM, "Could not write '" + textToWrite + "' to " + guiElement.getName() + ". Text in element after operation is '" + getText(guiElement) + "'.");
    }

    /**
     * Method using a regular expression pattern to check if all the characters in one string exists in another string, and in the same order.
     * Used for verification of text entries.
     *
     * @param actualText The text to find all characters in.
     * @param expectedText The characters to find.
     * @return Returns true if all characters in expectedText are found in the same order in actualText.
     */
    private boolean allCharactersExistAndInCorrectOrder(String actualText, String expectedText){
        StringBuilder matchingString = new StringBuilder(".*");
        for(int i = 0; i < expectedText.length(); i++){
            matchingString.append(expectedText.substring(i, i + 1)).append(".*");
        }
        return SupportMethods.isRegexMatch(actualText, matchingString.toString());
    }

    /**
     * Retrieves all sub-components of an element.
     *
     * @param component The element to find the sub-components of
     * @return Returns all sub-components.
     */
    private List<Object> addSubComponents(Object component){
        List<Object> componentList = new ArrayList<>();
        if(!methodInvoker.objectHasAnyOfTheMethods(component, MethodDeclarations.subComponentCountMethodsInAttemptOrder) || !methodInvoker.objectHasAnyOfTheMethods(component, MethodDeclarations.subComponentGetterMethodsInAttemptOrder)) return componentList;
        int numberOfSubItems = (int) methodInvoker.invokeTheFirstEncounteredMethod(component, MethodDeclarations.subComponentCountMethodsInAttemptOrder);
        for(int i = 0; i<numberOfSubItems; i++){
            Object o = methodInvoker.invokeTheFirstEncounteredMethod(component, MethodDeclarations.subComponentGetterMethodsInAttemptOrder, i);
            componentList.add(o);
            componentList.addAll(addSubComponents(o));
        }
        return componentList;
    }

}
