package se.claremont.autotest.javamethods;

import se.claremont.autotest.common.guidriverpluginstructure.PositionBasedIdentification.PositionBasedGuiElement;
import se.claremont.autotest.common.logging.LogLevel;
import se.claremont.autotest.common.support.SupportMethods;
import se.claremont.autotest.common.testcase.TestCase;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jordam on 2017-02-08.
 */
@SuppressWarnings("WeakerAccess")
public class JavaGuiElement implements GuiComponent, PositionBasedGuiElement {
    String name;
    String recognitionString;
    IdType idType;
    String className;
    JavaWindow window = null;
    List<String> recognitionDescription = new ArrayList<>();
    TestCase testCase;
    Object cachedElement = null;


    public enum IdType{
        ELEMENT_NAME,
        ELEMENT_TEXT
    }

    public JavaGuiElement(Object object) {
        if(object == null)return;
        String elementName = (String) MethodInvoker.invokeMethod(testCase, object, "getName");
        name = object.getClass().toString().replace(".", "_").replace(" ", "") + "_" + elementName.replace(" ", "");
        if(elementName != null){
            recognitionString = elementName;
            idType = IdType.ELEMENT_NAME;
        } else {
            recognitionString = (String) MethodInvoker.invokeMethod(null, object, "getText");
            idType = IdType.ELEMENT_TEXT;
        }
        className = object.getClass().toString();

    }

    public JavaGuiElement(Object object, TestCase testCase) {
        this.testCase = testCase;
        if(object == null)return;
        String elementName = (String) MethodInvoker.invokeMethod(testCase, object, "getName");
        name = object.getClass().toString().replace(".", "_") + "_" + elementName;
        if(elementName != null){
            recognitionString = elementName;
            idType = IdType.ELEMENT_NAME;
        } else {
            recognitionString = (String) MethodInvoker.invokeMethod(testCase, object, "getText");
            idType = IdType.ELEMENT_TEXT;
        }
        className = object.getClass().toString();
    }

    public JavaGuiElement(String name, String recognitionString){
        this.name  = name;
        this.idType = IdType.ELEMENT_TEXT;
        this.recognitionString = recognitionString;
        this.className = null;
    }

    public JavaGuiElement(String name, String recognitionString, IdType idType, String className){
        this.name = name;
        this.recognitionString = recognitionString;
        this.idType = idType;
        this.className = className;
    }

    public JavaGuiElement(String name, String recognitionString, IdType idType){
        this.name = name;
        this.recognitionString = recognitionString;
        this.idType = idType;
        this.className = null;
    }

    public JavaGuiElement(JavaWindow window, String name, String recognitionString){
        this.name  = name;
        this.idType = IdType.ELEMENT_TEXT;
        this.recognitionString = recognitionString;
        this.className = null;
        this.window = window;
    }

    public JavaGuiElement(JavaWindow window, String name, String recognitionString, IdType idType, String className){
        this.name = name;
        this.recognitionString = recognitionString;
        this.idType = idType;
        this.className = className;
        this.window = window;
    }

    public JavaGuiElement(JavaWindow window, String name, String recognitionString, IdType idType){
        this.name = name;
        this.recognitionString = recognitionString;
        this.idType = idType;
        this.className = null;
        this.window = window;
    }

    public String getName(){
        return name;
    }

    /**
     * Identifies the run-time element that is identified by a GuiElement of JavaGuiElement class.
     *
     * @return Returns the actual runtime element to interact with.
     */
    public Object getRuntimeComponent(){
        long startTime = System.currentTimeMillis();
        recognitionDescription.clear();
        recognitionDescription.add("Attempting to identify runtime object for " + getName() + " by " + idType.toString().toLowerCase() + " and with the recognition string '" + recognitionString + "'.");
        List<Object> windowComponents = getWindowComponents();
        ArrayList<Object> matchingComponents = new ArrayList<>();
        if(idType == IdType.ELEMENT_NAME){
            matchingComponents.addAll(returnElementByName(windowComponents));
        } else if(idType == JavaGuiElement.IdType.ELEMENT_TEXT) {
            matchingComponents.addAll(returnElementByText(windowComponents));
        } else {
            recognitionDescription.add("Identification mechanism for element recognition type '" + idType.toString() + "' is not yet implemented.");
        }
        if(matchingComponents.size() == 0){
            recognitionDescription.add("Could not identify any matching runtime object for " + getName() + ". Time for identification: " + (System.currentTimeMillis() - startTime) + " milliseconds.");
            return null;
        } else if (matchingComponents.size() == 1){
            recognitionDescription.add("Identified exactly one matching runtime object for " + getName() + ". Returning this object. Time for identification: " + (System.currentTimeMillis() - startTime) + " milliseconds.");
            cachedElement = matchingComponents.get(0);
            return matchingComponents.get(0);
        } else {
            recognitionDescription.add("Identified " + matchingComponents.size() + " matching runtime object for " + getName() + ".  Time for identification: " + (System.currentTimeMillis() - startTime) + " milliseconds. The objects were: ");
            for(Object component : matchingComponents){
                recognitionDescription.add(component.toString());
            }
            recognitionDescription.add("Returning the first object.");
            cachedElement = matchingComponents.get(0);
            return matchingComponents.get(0);
        }
    }

    public Object getRuntimeElementCacheable(){
        if(cachedElement != null) return cachedElement;
        return getRuntimeComponent();
    }

    public String getRecognitionDescription(){
        int counter = 1;
        String recognitionString = System.lineSeparator();
        for(String descLine : recognitionDescription){
            recognitionString += counter + "). " + descLine + System.lineSeparator();
            counter++;
        }
        return recognitionString;
    }

    public ArrayList<JavaGuiElement> getSubElements(){
        ArrayList<JavaGuiElement> javaGuiElements = new ArrayList<>();
        GenericInteractionMethods genericInteractionMethods = new GenericInteractionMethods(null);
        List<Object> subElements = genericInteractionMethods.allSubElementsOf(this.getRuntimeComponent());
        for(Object object : subElements){
            try {
                JavaGuiElement javaGuiElement = new JavaGuiElement(object);
                javaGuiElements.add(javaGuiElement);
                javaGuiElements.addAll(javaGuiElement.getSubElements());
            }catch (Exception ignored){
                System.out.println("Could not cast element of type '" + object.getClass().toString() + "' to JavaGuiElement. Error: " + ignored.toString());
            }
        }
        return javaGuiElements;
    }

    @Override
    public Integer getLeftPosition() {
        Object element = getRuntimeElementCacheable();
        return (Integer) MethodInvoker.invokeTheFirstEncounteredMethod(testCase, element, MethodDeclarations.methodsToGetLeftPositionInOrder);
    }

    @Override
    public Integer getRightPosition() {
        Object element = getRuntimeElementCacheable();
        if(element == null) return null;
        Integer location = (Integer) MethodInvoker.invokeTheFirstEncounteredMethod(testCase, element, MethodDeclarations.methodsToGetLeftPositionInOrder);
        Integer width = (Integer) MethodInvoker.invokeTheFirstEncounteredMethod(testCase, element, MethodDeclarations.componentWidthGetterMethodsInAttemptOrder);
        return location + width;
    }

    @Override
    public Integer getTopPosition() {
        Object element = getRuntimeElementCacheable();
        if(element == null) return null;
        return (Integer) MethodInvoker.invokeTheFirstEncounteredMethod(testCase, element, MethodDeclarations.methodsToGetTopPositionInOrder);
    }

    @Override
    public Integer getBottomPosition() {
        Object element = getRuntimeElementCacheable();
        if(element == null) return null;
        Integer location = (Integer) MethodInvoker.invokeTheFirstEncounteredMethod(testCase, element, MethodDeclarations.methodsToGetTopPositionInOrder);
        Integer height = (Integer) MethodInvoker.invokeTheFirstEncounteredMethod(testCase, element, MethodDeclarations.componentHightGetterMethodsInAttemptOrder);
        return location + height;
    }

    @Override
    public String getTypeName() {
        Object element = getRuntimeElementCacheable();
        if(element == null) return null;
        if(element != null) return element.getClass().toString();
        return null;
    }

    private List<Object> getWindowComponents() {
        List<Object> objects = new ArrayList<>();
        if(window != null){
            objects = window.getComponents();
            recognitionDescription.add("Identified " + objects.size() + " objects in the window.");
        } else {
            recognitionDescription.add("No window was given for object. Trying to identify java windows.");
            List<Window> windows = ApplicationStarter.getWindows();
            List<Window> nonDisplayedWindows = new ArrayList<>();
            for(Window w : windows){
                if(!w.isShowing()) {
                    JavaWindow javaWindow = new JavaWindow(w);
                    nonDisplayedWindows.add(w);
                    recognitionDescription.add("Identified running java window with title '" + javaWindow.getTitle() + "' but it was suppressed from displaying. No elements added.");
                } else {
                    JavaWindow javaWindow = new JavaWindow(w);
                    List<Object> objectList = javaWindow.getComponents();
                    recognitionDescription.add("Identified java window with title '" + javaWindow.getTitle() + "' and " + objectList.size() + " objects.");
                    objects.addAll(objectList);
                }
            }
            if(objects.size() == 0 && nonDisplayedWindows.size() > 0){
                recognitionDescription.add("Found zero objects in displaying windows. Assuming a window with suppressed display is meant to be used. Adding elements from all suppressed windows.");
                for(Window w : nonDisplayedWindows){
                    JavaWindow javaWindow = new JavaWindow(nonDisplayedWindows.get(0));
                    objects.addAll(javaWindow.getComponents());
                }
            }
            recognitionDescription.add("Identified " + objects.size() + " relevant java objects.");
        }
        return objects;
    }

    private ArrayList<Object> returnElementByText(List<Object> componentList) {
        ArrayList<Object> returnElementList = new ArrayList<>();
        returnElementList.addAll(getElementByExactMatchOfText(componentList));
        if(returnElementList.size() > 0) return returnElementList;
        returnElementList.addAll(getElementByContainsMatchOfText(componentList));
        if(returnElementList.size() > 0) return returnElementList;
        return getElementByRegexMatchOfText(componentList);
    }

    private ArrayList<Object> getElementByRegexMatchOfText(List<Object> componentList) {
        ArrayList<Object> returnElements = new ArrayList<>();
        GenericInteractionMethods jim = new GenericInteractionMethods(null);
        for(Object c : componentList){
            if(className != null && !c.getClass().toString().equals(className)) continue;
            if(SupportMethods.isRegexMatch(jim.getText(c), recognitionString)) {
                recognitionDescription.add("Found regular expression match for text '" + jim.getText(c) + "' in element of class '" + c.getClass().toString() + "' with expected regular expression pattern '" + recognitionString + "'. Adding this object to possible matches list.");
                returnElements.add(c);
            }
        }
        return returnElements;
    }

    private ArrayList<Object> getElementByExactMatchOfText(List<Object> componentList){
        ArrayList<Object> returnElements = new ArrayList<>();
        GenericInteractionMethods jim = new GenericInteractionMethods(null);
        for(Object c : componentList){
            if(className != null && !c.getClass().toString().equals(className)) continue;
            if(jim.getText(c) != null && jim.getText(c).equals(recognitionString)) {
                recognitionDescription.add("Found exact text match in element of class '" + c.getClass().toString() + "'. Adding this object to possible matches list.");
                returnElements.add(c);
            }
        }
        return returnElements;
    }

    private ArrayList<Object> getElementByContainsMatchOfText(List<Object> componentList){
        GenericInteractionMethods jim = new GenericInteractionMethods(null);
        ArrayList<Object> returnElements = new ArrayList<>();
        for(Object c : componentList){
            if(className != null && !c.getClass().toString().equals(className)) continue;
            if(jim.getText(c) != null && jim.getText(c).contains(recognitionString)){
                recognitionDescription.add("Found partial match for text '" + recognitionString + "' in object with text '" + jim.getText(c) + "' of class '" + c.getClass().toString() + "'. Adding this object to possible matches list.");
                returnElements.add(c);
            }
        }
        return returnElements;
    }

    private ArrayList<Object> returnElementByName(List<Object> componentList){
        ArrayList<Object> returnElements = new ArrayList<>();
        GenericInteractionMethods jim = new GenericInteractionMethods(null);
        for(Object c : componentList){
            if(className != null && !c.getClass().toString().equals(className)) continue;
            if(jim.getName(c) != null && jim.getName(c).equals(recognitionString)){
                recognitionDescription.add("Adding element of class '" + c.getClass().toString() + "' with exact match of name '" + jim.getName(c) + "' as a possible match.");
                returnElements.add(c);
            }
        }
        if(returnElements.size() > 0) return returnElements;
        recognitionDescription.add("Found no exact match for recognition string '" + recognitionString + "' for any object.");
        for(Object c : componentList){
            if(className != null && !c.getClass().toString().equals(className)) continue;
            if(jim.getName(c) != null && jim.getName(c).contains(recognitionString)){
                recognitionDescription.add("Adding element of class '" + c.getClass().toString() + "' with name '" + jim.getName(c) + "' that contains the expected recognition string '" + recognitionString + "' as a possible match.");
                returnElements.add(c);
            }
        }
        if(returnElements.size() > 0) return returnElements;
        recognitionDescription.add("Found no partial match for recognition string '" + recognitionString + "' either, for any object.");

        for(Object c : componentList){
            if(className != null && !c.getClass().toString().equals(className)) continue;
            if(SupportMethods.isRegexMatch(jim.getName(c), recognitionString)){
                recognitionDescription.add("Adding element of class '" + c.getClass().toString() + "' with name '" + jim.getName(c) + "' that is a regular expression match for the recognition string pattern '" + recognitionString + "' as a possible match.");
                returnElements.add(c);
            }
        }
        if(returnElements.size() > 0) return returnElements;
        recognitionDescription.add("No object matching the recognition string '" + recognitionString + "' was found.");
        return returnElements;
    }

    private void log(LogLevel logLevel, String message){
        if(testCase == null) return;
        testCase.log(logLevel, message);
    }


}
