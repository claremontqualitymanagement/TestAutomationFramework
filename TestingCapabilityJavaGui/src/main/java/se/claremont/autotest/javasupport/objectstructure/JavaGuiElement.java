package se.claremont.autotest.javasupport.objectstructure;

import jdk.nashorn.internal.runtime.regexp.joni.Regex;
import se.claremont.autotest.common.guidriverpluginstructure.PositionBasedIdentification.PositionBasedGuiElement;
import se.claremont.autotest.common.logging.LogLevel;
import se.claremont.autotest.common.support.SupportMethods;
import se.claremont.autotest.common.testcase.TestCase;
import se.claremont.autotest.javasupport.applicationundertest.applicationstarters.ApplicationStarter;
import se.claremont.autotest.javasupport.interaction.GenericInteractionMethods;
import se.claremont.autotest.javasupport.interaction.MethodDeclarations;
import se.claremont.autotest.javasupport.interaction.MethodInvoker;
import se.claremont.autotest.javasupport.interaction.elementidentification.By;
import se.claremont.autotest.javasupport.interaction.elementidentification.SearchCondition;
import se.claremont.autotest.javasupport.interaction.elementidentification.SearchConditionType;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * A GUI component in any Java client application.
 *
 * Created by jordam on 2017-02-08.
 */
@SuppressWarnings("WeakerAccess")
public class JavaGuiElement implements GuiComponent, PositionBasedGuiElement {
    String name;
    String recognitionString;
    IdType idType = IdType.UNKNOWN;
    String className;
    JavaWindow window = null;
    By by;
    List<String> recognitionDescription = new ArrayList<>();
    TestCase testCase;
    Object cachedElement = null;

    public IdType getIdType() {
        return idType;
    }

    public void setIdType(IdType idType) {
        this.idType = idType;
    }


    /**
     * Element identification mechanism used to identify the element
     */
    public enum IdType{
        ELEMENT_NAME,
        ELEMENT_TEXT,
        POSITION_BASED,
        UNKNOWN
    }

    public JavaGuiElement(By by, String name){
        this.by = by;
        this.name = name;
    }

    public JavaGuiElement(Object object) {
        if(object == null)return;
        try {
            String elementName = (String) MethodInvoker.invokeTheFirstEncounteredMethod(testCase, object, MethodDeclarations.componentNameGetterMethodsInAttemptOrder);
            String objectName = elementName;
            if (objectName == null || objectName.length() == 0) objectName = "NoNamedObject";
            name = object.getClass().toString().replace(".", "_").replace(" ", "") + "_" + objectName.replace(" ", "");
            String text = (String) MethodInvoker.invokeTheFirstEncounteredMethod(null, object, MethodDeclarations.textGettingMethodsInAttemptOrder);
            if (elementName != null) {
                recognitionString = elementName;
                idType = IdType.ELEMENT_NAME;
            } else if (text != null && text.length() > 0){
                recognitionString = text;
                idType = IdType.ELEMENT_TEXT;
            } else {
                idType = IdType.UNKNOWN;
                log(LogLevel.DEBUG, "Warning: Could not find any recognition characteristics for element [" + object.toString() + "].");
            }
            cachedElement = object;
            className = object.getClass().toString();
        }catch (Exception e){
            log(LogLevel.DEBUG, "Could not create JavaGuiElement from Object. Error: "+ e.toString());
            log(LogLevel.DEBUG, "Possible methods of object are:" + System.lineSeparator() + String.join(System.lineSeparator(), MethodInvoker.getAvailableMethods(object)));
        }
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

    /**
     * Element name.
     *
     * @return Return the element name.
     */
    public String getName(){
        return name;
    }

    /**
     * Identifies the runtime element that is identified by a GuiElement of JavaGuiElement class.
     *
     * @return Returns the actual runtime element to interact with.
     */
    public Object getRuntimeComponent(){
        long startTime = System.currentTimeMillis();
        recognitionDescription.clear();
        List<Object> windowComponents = getWindowComponents();
        List<Object> matchingComponents = new ArrayList<>();
        if(by != null){
            windowComponents = filterByAncestorElementIfApplicable(windowComponents);
            recognitionDescription.add("Attempting to identify runtime object for " + getName() + " by By statement:" + System.lineSeparator() + by.toString());
            matchingComponents.addAll(attemptToIdentifyElementByByStatement(windowComponents));
            matchingComponents = filterObjectByOrdinalNumber(matchingComponents);
        } else {
            recognitionDescription.add("Attempting to identify runtime object for " + getName() + " by " + idType.toString().toLowerCase() + " and with the recognition string '" + recognitionString + "'.");
            if(idType == IdType.ELEMENT_NAME){
                matchingComponents.addAll(returnElementByName(windowComponents));
            } else if(idType == JavaGuiElement.IdType.ELEMENT_TEXT) {
                matchingComponents.addAll(returnElementByText(windowComponents));
            } else {
                recognitionDescription.add("Identification mechanism for element recognition type '" + idType.toString() + "' is not yet implemented.");
            }
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

    private List<Object> filterObjectByOrdinalNumber(List<Object> objects){
        List<Object> returnObjects = new ArrayList<>(objects);
        for(SearchCondition searchCondition : by.searchConditions){
            if(searchCondition.searchConditionType == SearchConditionType.ORDINAL_NUMBER){
                returnObjects.clear();
                Integer ordinalNumber = Integer.valueOf((int)searchCondition.objects[0]);
                if(ordinalNumber == null){
                    recognitionDescription.add("Wrong format of ordinal number encountered. Was '" + searchCondition.objects[0] + "'.");
                } else if(ordinalNumber > objects.size()){
                    recognitionDescription.add("Attempting to get element number " + ordinalNumber + " but there are only " + objects.size() + " element(s) to choose from. Returning last match.");
                    returnObjects.add(objects.get((int)searchCondition.objects[objects.size()]));
                } else if (ordinalNumber < 0) {
                    recognitionDescription.add("Ordinal number should not be negative. Was " + ordinalNumber + ". Returning first match.");
                    returnObjects.add(objects.get(0));
                } else {
                    recognitionDescription.add("Out of the " + objects.size() + " elements that are left, the " + ordinalNumber + " is chosen. Removing the other ones.");
                    returnObjects.add(objects.get(ordinalNumber));
                }
                break;
            }
        }
        return returnObjects;
    }

    private List<Object> filterByAncestorElementIfApplicable(List<Object> objects) {
        List<Object> returnObjects = new ArrayList<>(objects);
        for(SearchCondition searchCondition : by.searchConditions){
            if(searchCondition.searchConditionType == SearchConditionType.BEING_DESCENDANT_OF){
                JavaGuiElement ancestor = (JavaGuiElement)searchCondition.objects[0];
                returnObjects = ancestor.getDescendants();
                if(returnObjects == null){
                    recognitionDescription.add("Could not get descendants from element '" + ancestor.name + "'.");
                    returnObjects = new ArrayList<>();
                } else {
                    recognitionDescription.add("Removed " + (objects.size() - returnObjects.size()) + " elements that waa not descendants of element '" + ancestor.name + "'.");
                }
                break;
            }
        }
        return returnObjects;
    }

    private List<Object> getDescendants() {
        Object element = getRuntimeElementCacheable();
        MethodInvoker methodInvoker = new MethodInvoker();
        List<Object> componentList = new ArrayList<>();
        if(!methodInvoker.objectHasAnyOfTheMethods(element, MethodDeclarations.subComponentCountMethodsInAttemptOrder) || !methodInvoker.objectHasAnyOfTheMethods(element, MethodDeclarations.subComponentGetterMethodsInAttemptOrder)){
            return componentList;
        }

        Integer windowComponentCount = (Integer) methodInvoker.invokeTheFirstEncounteredMethod(element, MethodDeclarations.subComponentCountMethodsInAttemptOrder);
        if(windowComponentCount == null)return componentList;
        Object[] returnList = (Object[]) methodInvoker.invokeTheFirstEncounteredMethod(element, MethodDeclarations.subAllComponentsGettersMethodsInAttemptOrder);
        if(returnList != null && returnList.length > 0){
            for(Object object : returnList){
                componentList.add(object);
                componentList.addAll(addSubComponents(methodInvoker, object));
            }
            return componentList;
        }
        for(int i = 0; i < windowComponentCount; i++){
            Object component = methodInvoker.invokeTheFirstEncounteredMethod(element, MethodDeclarations.subComponentGetterMethodsInAttemptOrder, i);
            if(component == null) continue;
            componentList.add(component);
            componentList.addAll(addSubComponents(methodInvoker, component));
        }
        return componentList;
    }

    private List<Object> addSubComponents(MethodInvoker methodInvoker, Object component){
        List<Object> componentList = new ArrayList<>();
        if(!methodInvoker.objectHasAnyOfTheMethods(component, MethodDeclarations.subComponentCountMethodsInAttemptOrder) || !methodInvoker.objectHasAnyOfTheMethods(component, MethodDeclarations.subComponentGetterMethodsInAttemptOrder)) return componentList;
        int numberOfSubItems = (int) methodInvoker.invokeTheFirstEncounteredMethod(component, MethodDeclarations.subComponentCountMethodsInAttemptOrder);
        for(int i = 0; i<numberOfSubItems; i++){
            Object o = methodInvoker.invokeTheFirstEncounteredMethod(component, MethodDeclarations.subComponentGetterMethodsInAttemptOrder, i);
            componentList.add(o);
            componentList.addAll(addSubComponents(methodInvoker, o));
        }
        return componentList;
    }


    private Collection<Object> attemptToIdentifyElementByByStatement(List<Object> componentList) {
        ArrayList<Object> returnElements = new ArrayList<>(componentList);
        recognitionDescription.add("Identifying " + componentList.size() + " elements in window.");
        ArrayList<Object> removeElements = new ArrayList<>();
        GenericInteractionMethods jim = new GenericInteractionMethods(null);
        for(SearchCondition searchCondition : by.searchConditions){
            switch (searchCondition.searchConditionType){
                case CLASS:
                    for(Object o : componentList){
                        if(!o.getClass().getSimpleName().equals((String)searchCondition.objects[0])){
                            removeElements.add(o);
                        }
                    }
                    recognitionDescription.add("Removing " + removeElements.size() + " elements not being of class '" + (String)searchCondition.objects[0] + "'.");
                    returnElements.removeAll(removeElements);
                    removeElements.clear();
                    break;
                case NAME:
                    for(Object o : componentList){
                        String name = jim.getName(0);
                        if(name == null || name.equals((String)searchCondition.objects[0])){
                            removeElements.add(o);
                        }
                    }
                    recognitionDescription.add("Removing " + removeElements.size() + " elements not being named '" + (String)searchCondition.objects[0] + "'.");
                    returnElements.removeAll(removeElements);
                    removeElements.clear();
                    break;
                case EXACT_TEXT:
                    for(Object o : componentList){
                        boolean toBeRemoved = true;
                        for(Object acceptedText : searchCondition.objects){
                            String elementText = jim.getText(o);
                            if(elementText != null && elementText.equals((String)acceptedText)){
                                toBeRemoved = false;
                                break;
                            }
                        }
                        if(toBeRemoved)
                            removeElements.add(o);
                    }
                    recognitionDescription.add("Removing " + removeElements.size() + " elements not having any of the exact texts '" + String.join("', '", ((String[])searchCondition.objects)) + "'.");
                    returnElements.removeAll(removeElements);
                    removeElements.clear();
                    break;
                case TEXT_CONTAINS:
                    for(Object o : componentList){
                        boolean toBeRemoved = true;
                        for(Object acceptedText : searchCondition.objects){
                            String elementText = jim.getText(o);
                            if(elementText != null && elementText.contains((String)acceptedText)){
                                toBeRemoved = false;
                                break;
                            }
                        }
                        if(toBeRemoved)
                            removeElements.add(o);
                    }
                    recognitionDescription.add("Removing " + removeElements.size() + " elements not containing any of the exact texts '" + String.join("', '", ((String[])searchCondition.objects)) + "'.");
                    returnElements.removeAll(removeElements);
                    removeElements.clear();
                    break;
                case TEXT_REGEX_MATCH:
                    for(Object o : componentList){
                        String elementText = jim.getText(o);
                        if(elementText == null || !elementText.matches((String)searchCondition.objects[0])){
                            removeElements.add(o);
                        }
                    }
                    recognitionDescription.add("Removing " + removeElements.size() + " elements not matching pattern '" + (String[])searchCondition.objects[0] + "'.");
                    returnElements.removeAll(removeElements);
                    removeElements.clear();
                    break;
                default:
                    recognitionDescription.add("No By statement implementation for SearchCondition " + searchCondition.toString() + ". Ignoring this.");
                    break;
            }
        }
        return returnElements;
    }

    public Object getRuntimeElementCacheable(){
        if(cachedElement != null) return cachedElement;
        return getRuntimeComponent();
    }

    @SuppressWarnings("StringConcatenationInLoop")
    public String getRecognitionDescription(){
        int counter = 1;
        String recognitionString = System.lineSeparator();
        for(String descLine : recognitionDescription){
            recognitionString += counter + "). " + descLine + System.lineSeparator();
            counter++;
        }
        return recognitionString;
    }

    /**
     * Return the JavaWindow this element resides in.
     *
     * @return Returns the JavaWindow
     */
    public JavaWindow getJavaWindow(){
        if(window != null) return window;
        return new JavaWindow((Window)getWindow());
    }

    /**
     * Return the Window element this element resides in.
     *
     * @return Returns the Window object that holds the element
     */
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    public Object getWindow(){
        Object thisElement = getRuntimeElementCacheable();
        if(window != null){
            return window.getWindow();
        } else {
            List<Object> objects = new ArrayList<>();
            Window[] windows = Window.getOwnerlessWindows ();
            List<Window> nonDisplayedWindows = new ArrayList<>();
            for(Window w : windows){
                if(!w.isShowing()) {
                    nonDisplayedWindows.add(w);
                } else {
                    JavaWindow javaWindow = new JavaWindow(w);
                    List<Object> objectList = javaWindow.getComponents();
                    for(Object o : objectList){
                        if(o.equals(thisElement)) return w;
                    }
                }
            }
            if(objects.size() == 0 && nonDisplayedWindows.size() > 0){
                for(Window w : nonDisplayedWindows){
                    JavaWindow javaWindow = new JavaWindow(nonDisplayedWindows.get(0));
                    List<Object> objectList = javaWindow.getComponents();
                    for(Object o : objectList){
                        if(o.equals(thisElement)) return w;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Returns the subelements of this element. Many element types don't have sub elements.
     *
     * @return Returns sub elements of this element.
     */
    public ArrayList<JavaGuiElement> getSubElements(){
        ArrayList<JavaGuiElement> javaGuiElements = new ArrayList<>();
        GenericInteractionMethods genericInteractionMethods = new GenericInteractionMethods(null);
        Object component = this.getRuntimeElementCacheable();
        if(component == null){
            log(LogLevel.DEBUG, "Could not identify runtime element for element " + getName() + ". Cannot retrieve subelements from this element.");
            return javaGuiElements;
        }
        List<Object> subElements = genericInteractionMethods.allSubElementsOf(component);
        for(Object object : subElements){
            try {
                JavaGuiElement javaGuiElement = new JavaGuiElement(object);
                javaGuiElements.add(javaGuiElement);
                try {
                    javaGuiElements.addAll(javaGuiElement.getSubElements());
                }catch (Exception e){
                    log(LogLevel.DEBUG, "Could not retrieve subElements from " + javaGuiElement.getName() + ".");
                    return javaGuiElements;
                }
            }catch (Exception ignored){
                log(LogLevel.DEBUG, "Could not cast element of type '" + object.getClass().toString() + "' to JavaGuiElement. Error: " + ignored.toString());
                return javaGuiElements;
            }
        }
        return javaGuiElements;
    }

    /**
     * Returns the parent element of this element.
     *
     * @return Returns the parent element of this element.
     */
    public JavaGuiElement getParent(){
        Object object = MethodInvoker.invokeTheFirstEncounteredMethod(null, this.getRuntimeComponent(), MethodDeclarations.componentParentGetterMethodsInAttemptOrder);
        if(object == null) return null;
        return new JavaGuiElement(object);
    }

    /**
     * Returns a list of all the other elements in the same container.
     *
     * @return Returns a list of all the other elements in the same container.
     */
    public List<JavaGuiElement> getAllElementsInSameContainer(){
        List<JavaGuiElement> elements = new ArrayList<>();
        List<JavaGuiElement> subElements = getSubElements();
        if(subElements.size() > 0) return subElements;
        JavaGuiElement parent = getParent();
        if(parent == null) return elements;
        return parent.getSubElements();
    }

    /**
     * @return Returns the left most position of this element.
     */
    @Override
    public Integer getLeftPosition() {
        Object element = getRuntimeElementCacheable();
        return (Integer) MethodInvoker.invokeTheFirstEncounteredMethod(testCase, element, MethodDeclarations.methodsToGetLeftPositionInOrder);
    }

    /**
     * @return Returns the right most position of this element.
     */
    @Override
    public Integer getRightPosition() {
        Object element = getRuntimeElementCacheable();
        if(element == null) return null;
        Integer location = (Integer) MethodInvoker.invokeTheFirstEncounteredMethod(testCase, element, MethodDeclarations.methodsToGetLeftPositionInOrder);
        Integer width = (Integer) MethodInvoker.invokeTheFirstEncounteredMethod(testCase, element, MethodDeclarations.componentWidthGetterMethodsInAttemptOrder);
        return location + width;
    }

    /**
     * @return Returns the top most position of this element.
     */
    @Override
    public Integer getTopPosition() {
        Object element = getRuntimeElementCacheable();
        if(element == null) return null;
        return (Integer) MethodInvoker.invokeTheFirstEncounteredMethod(testCase, element, MethodDeclarations.methodsToGetTopPositionInOrder);
    }

    /**
     * @return Returns the bottom most position of this element.
     */
    @Override
    public Integer getBottomPosition() {
        Object element = getRuntimeElementCacheable();
        if(element == null) return null;
        Integer location = (Integer) MethodInvoker.invokeTheFirstEncounteredMethod(testCase, element, MethodDeclarations.methodsToGetTopPositionInOrder);
        Integer height = (Integer) MethodInvoker.invokeTheFirstEncounteredMethod(testCase, element, MethodDeclarations.componentHightGetterMethodsInAttemptOrder);
        return location + height;
    }

    /**
     * @return Returns the type of this element.
     */
    @Override
    public String getTypeName() {
        if(className != null) return className;
        Object element = getRuntimeElementCacheable();
        if(element == null) return null;
        if(element != null) return element.getClass().toString();
        return null;
    }

    @Override
    public Object runtimeElement() {
        return cachedElement;
    }

    @Override
    public ArrayList<PositionBasedGuiElement> childElements() {
        ArrayList<PositionBasedGuiElement> list = new ArrayList<>();
        for (JavaGuiElement javaGuiElement : getSubElements()){
            list.add(javaGuiElement);
        }
        return list;
    }

    private List<Object> getWindowComponents() {
        List<Object> objects = new ArrayList<>();
        if(window != null){
            objects = window.getComponents();
            recognitionDescription.add("Identified " + objects.size() + " objects in the window.");
        } else {
            recognitionDescription.add("No window was given for object. Trying to identify java windows.");
            Window[] windows = Window.getOwnerlessWindows ();
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
        if(testCase == null) {
            System.out.println(logLevel.toString() + ": " + message);
            return;
        }
        testCase.log(logLevel, message);
    }

    @Override
    public String toString(){
        String description = "[";
        if(name == null){
            description += "name: null";
        } else {
            description += "name: '" + name + "'";
        }
        if(recognitionString == null){
            description += ", recognitionString: null";
        }else{
            description += ", recognitionString: '" + recognitionString + "'";
        }
        if(idType == null){
            description += ", idType: null";
        } else {
            description += ", idType: '" + idType.toString() + "'";
        }
        if(className == null){
            description += ", class name: null";
        }else{
            description += ", class name: '" + className + "'";
        }
        description += "', cached object stored: " + String.valueOf(cachedElement != null) + "]";
        return description;
    }


}
