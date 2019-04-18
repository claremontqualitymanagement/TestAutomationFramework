package se.claremont.taf.javasupport.objectstructure;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import se.claremont.taf.guidriverpluginstructure.PositionBasedIdentification.ElementsList;
import se.claremont.taf.guidriverpluginstructure.PositionBasedIdentification.PositionBasedGuiElement;
import se.claremont.taf.logging.LogLevel;
import se.claremont.taf.support.StringManagement;
import se.claremont.taf.support.SupportMethods;
import se.claremont.taf.testcase.TestCase;
import se.claremont.taf.javasupport.applicationundertest.ApplicationUnderTest;
import se.claremont.taf.javasupport.interaction.GenericInteractionMethods;
import se.claremont.taf.javasupport.interaction.MethodDeclarations;
import se.claremont.taf.javasupport.interaction.MethodInvoker;
import se.claremont.taf.javasupport.interaction.elementidentification.By;
import se.claremont.taf.javasupport.interaction.elementidentification.SearchCondition;
import se.claremont.taf.javasupport.interaction.elementidentification.SearchConditionType;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * A GUI component in any Java client application.
 *
 * Created by jordam on 2017-02-08.
 */
@SuppressWarnings("WeakerAccess")
public class JavaGuiElement implements GuiComponent, PositionBasedGuiElement, Serializable {
    @JsonProperty String name;
    @JsonProperty JavaWindow window = null;
    @JsonProperty public By by;
    @JsonIgnore private boolean cacheIsEssential = false;
    @JsonIgnore List<String> recognitionDescription = new ArrayList<>();
    @JsonIgnore TestCase testCase;
    @JsonIgnore Component cachedElement = null;

    public JavaGuiElement(JavaWindow window, By by, String name, TestCase testCase){
        this.window = window;
        this.name = name;
        this.by = by;
        this.testCase = testCase;
    }

    public JavaGuiElement(JavaWindow window, By by, String name){
        this(window, by, name, null);
    }

    public JavaGuiElement(By by, String name){
        this.by = by;
        this.name = name;
    }

    public JavaGuiElement(Object object) {
        this(object, null);
        cacheIsEssential = true;
    }

    public JavaGuiElement(PositionBasedGuiElement positionBasedGuiElement){
        if(positionBasedGuiElement.runtimeElement() == null) return;
        cachedElement = (Component)positionBasedGuiElement.runtimeElement();
        cacheIsEssential = true;
        name = "NoNamed" + cachedElement.getClass().getSimpleName();
    }

    public JavaGuiElement(Object object, TestCase testCase) {
        this.testCase = testCase;
        if(object == null)return;
        cacheIsEssential = true;
        if(By.class.isAssignableFrom(object.getClass())){
                this.by = (By)object;
                return;
        }else if(PositionBasedGuiElement.class.isAssignableFrom(object.getClass())){
            cachedElement = (Component)((PositionBasedGuiElement)object).runtimeElement();
            name = "NoNamed" + ((PositionBasedGuiElement)object).getTypeName() + "Element";
            return;
        } else if(JavaGuiElement.class.isAssignableFrom(object.getClass())){
            JavaGuiElement element = ((JavaGuiElement)object);
            this.by = element.by;
            this.name = element.name;
            this.cachedElement = element.cachedElement;
            this.testCase = element.testCase;
            this.window = element.window;
            this.recognitionDescription = element.recognitionDescription;
        }
        String nameSuggestion = "";
        try {
            by = By.byClass(object.getClass().getName());
            String elementName = (String) MethodInvoker.invokeTheFirstEncounteredMethod(testCase, object, MethodDeclarations.componentNameGetterMethodsInAttemptOrder);
            if(elementName != null && elementName.length() > 0){
                nameSuggestion = elementName;
                by.andByName(elementName);
            }
            String text = (String) MethodInvoker.invokeTheFirstEncounteredMethod(null, object, MethodDeclarations.textGettingMethodsInAttemptOrder);
            if(text != null && text.length() > 0){
                if(nameSuggestion == ""){
                    nameSuggestion = text;
                }
                by.andByExactText(text);
            }
            name = StringManagement.safeVariableName(StringManagement.stringToCapitalInitialCharacterForEachWordAndNoSpaces(nameSuggestion + object.getClass().getSimpleName()));
            if(name.length() == 0)
                name = "NoNameElement";
            cachedElement = (Component)object;
        }catch (Exception e){
            log(LogLevel.DEBUG, "Could not create JavaGuiElement from Object. Error: "+ e.toString());
            log(LogLevel.DEBUG, "Possible methods of object are:" + System.lineSeparator() + String.join(System.lineSeparator(), MethodInvoker.getAvailableMethods(object)));
        }
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
    public Component getRuntimeComponent(){
        if(cachedElement != null)return cachedElement;
        long startTime = System.currentTimeMillis();
        recognitionDescription.clear();
        List<Component> windowComponents = getWindowComponents();
        List<Component> matchingComponents = new ArrayList<>();
        if(by != null){
            Object positionBasedElement = getElementFromRelativePositionReferenceIfApplicable();
            if(positionBasedElement != null){
                windowComponents.add((Component)positionBasedElement);
            }
            windowComponents = filterByAncestorElementIfApplicable(windowComponents);
            recognitionDescription.add("Attempting to identify runtime object for " + getName() + " by By statement:" + System.lineSeparator() + by.toString());
            matchingComponents.addAll(attemptToIdentifyElementByByStatement(windowComponents));
            matchingComponents = filterObjectByOrdinalNumber(matchingComponents);
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

    private List<Component> filterObjectByOrdinalNumber(List<Component> objects){
        List<Component> returnObjects = new ArrayList<>(objects);
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

    private List<Component> filterByAncestorElementIfApplicable(List<Component> objects) {
        List<Component> returnObjects = new ArrayList<>(objects);
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

    private List<Component> getDescendants() {
        Object element = getRuntimeElementCacheable();
        MethodInvoker methodInvoker = new MethodInvoker();
        List<Component> componentList = new ArrayList<>();
        if(!methodInvoker.objectHasAnyOfTheMethods(element, MethodDeclarations.subComponentCountMethodsInAttemptOrder) || !methodInvoker.objectHasAnyOfTheMethods(element, MethodDeclarations.subComponentGetterMethodsInAttemptOrder)){
            return componentList;
        }

        Integer windowComponentCount = (Integer) methodInvoker.invokeTheFirstEncounteredMethod(element, MethodDeclarations.subComponentCountMethodsInAttemptOrder);
        if(windowComponentCount == null)return componentList;
        Component[] returnList = (Component[]) methodInvoker.invokeTheFirstEncounteredMethod(element, MethodDeclarations.subAllComponentsGettersMethodsInAttemptOrder);
        if(returnList != null && returnList.length > 0){
            for(Component object : returnList){
                componentList.add(object);
                componentList.addAll(addSubComponents(methodInvoker, object));
            }
            return componentList;
        }
        for(int i = 0; i < windowComponentCount; i++){
            Component component = (Component)methodInvoker.invokeTheFirstEncounteredMethod(element, MethodDeclarations.subComponentGetterMethodsInAttemptOrder, i);
            if(component == null) continue;
            componentList.add(component);
            componentList.addAll(addSubComponents(methodInvoker, component));
        }
        return componentList;
    }

    private List<Component> addSubComponents(MethodInvoker methodInvoker, Component component){
        List<Component> componentList = new ArrayList<>();
        if(!methodInvoker.objectHasAnyOfTheMethods(component, MethodDeclarations.subComponentCountMethodsInAttemptOrder) || !methodInvoker.objectHasAnyOfTheMethods(component, MethodDeclarations.subComponentGetterMethodsInAttemptOrder)) return componentList;
        int numberOfSubItems = (int) methodInvoker.invokeTheFirstEncounteredMethod(component, MethodDeclarations.subComponentCountMethodsInAttemptOrder);
        for(int i = 0; i<numberOfSubItems; i++){
            Component o = (Component)methodInvoker.invokeTheFirstEncounteredMethod(component, MethodDeclarations.subComponentGetterMethodsInAttemptOrder, i);
            componentList.add(o);
            componentList.addAll(addSubComponents(methodInvoker, o));
        }
        return componentList;
    }

    private List<? extends PositionBasedGuiElement> getElementFromRelativePositionReferenceIfApplicable(){
        for(SearchCondition sc : by.searchConditions){
            if(sc.searchConditionType == null || sc.objects == null || sc.objects[0] == null) continue;
            if(!sc.searchConditionType.equals(SearchConditionType.POSITION_BASED))continue;
            if(ElementsList.class.isAssignableFrom(sc.objects[0].getClass())){
                ElementsList elementsList = (ElementsList)sc.objects[0];
                return elementsList.elements;
            } else {
                JavaGuiElement positionBasedGuiElement = (JavaGuiElement) sc.objects[0];
                ArrayList<PositionBasedGuiElement> tempList = new ArrayList<>();
                tempList.add(positionBasedGuiElement);
                return tempList;
            }
        }
        return null;
    }


    private Collection<Component> attemptToIdentifyElementByByStatement(List<Component> componentList) {
        ArrayList<Component> returnElements = new ArrayList<>(componentList);
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
                        String name = jim.getName(o);
                        if(name == null || !name.equals((String)searchCondition.objects[0])){
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
                    recognitionDescription.add("Removing " + removeElements.size() + " elements not having any of the exact texts '" + searchCondition.toString() + "'.");
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

    public void logIdentification(LogLevel logLevel, TestCase testCase){
        testCase.logDifferentlyToTextLogAndHtmlLog(logLevel,
                "Identification procedure for " + name + ":" + getRecognitionDescription().replace(System.lineSeparator(), ", "),
                "Identification procedure for " + name + ":" + getRecognitionDescription().replace(System.lineSeparator(), "<br>"));
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

    public void clearCache(){
        if(cacheIsEssential) return;
        this.cachedElement = null;
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
                    List<Component> objectList = javaWindow.getComponents();
                    for(Object o : objectList){
                        if(o.equals(thisElement)) return w;
                    }
                }
            }
            if(objects.size() == 0 && nonDisplayedWindows.size() > 0){
                for(Window w : nonDisplayedWindows){
                    JavaWindow javaWindow = new JavaWindow(nonDisplayedWindows.get(0));
                    List<Component> objectList = javaWindow.getComponents();
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

    private List<Component> getWindowComponents() {
        List<Component> objects = new ArrayList<>();
        if(window != null && window.isShown() ){
            objects = window.getComponents();
            recognitionDescription.add("Identified " + objects.size() + " objects in the window.");
        } else {
            recognitionDescription.add("No window was given for object. Trying to identify java windows.");
            Set<Window> windows = ApplicationUnderTest.getWindows();
            List<Window> nonDisplayedWindows = new ArrayList<>();
            for(Window w : windows){
                if(!w.isShowing()) {
                    JavaWindow javaWindow = new JavaWindow(w);
                    nonDisplayedWindows.add(w);
                    recognitionDescription.add("Identified running java window with title '" + javaWindow.getTitle() + "' but it was suppressed from displaying. No elements added.");
                } else {
                    JavaWindow javaWindow = new JavaWindow(w);
                    List<Component> objectList = javaWindow.getComponents();
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
        String regexPattern = null;
        for(SearchCondition sc : by.searchConditions){
            if(sc.searchConditionType.equals(SearchConditionType.TEXT_REGEX_MATCH)){
                regexPattern = sc.objects.toString();
            }
        }
        if(regexPattern == null) return returnElements;
        for(Object c : componentList){
            if(SupportMethods.isRegexMatch(jim.getText(c), regexPattern)) {
                recognitionDescription.add("Found regular expression match for text '" + jim.getText(c) + "' in element of class '" + c.getClass().toString() + "' with expected regular expression pattern '" + regexPattern + "'. Adding this object to possible matches list.");
                returnElements.add(c);
            }
        }
        return returnElements;
    }

    private ArrayList<Object> getElementByExactMatchOfText(List<Object> componentList){
        ArrayList<Object> returnElements = new ArrayList<>();
        String expectedText = null;
        for(SearchCondition sc : by.searchConditions){
            if(sc.searchConditionType.equals(SearchConditionType.TEXT_REGEX_MATCH)){
                expectedText = sc.objects.toString();
            }
        }
        if(expectedText == null) return returnElements;
        GenericInteractionMethods jim = new GenericInteractionMethods(null);
        for(Object c : componentList){
            if(jim.getText(c) != null && jim.getText(c).equals(expectedText)) {
                recognitionDescription.add("Found exact text match in element of class '" + c.getClass().toString() + "'. Adding this object to possible matches list.");
                returnElements.add(c);
            }
        }
        return returnElements;
    }

    private ArrayList<Object> getElementByContainsMatchOfText(List<Object> componentList){
        ArrayList<Object> returnElements = new ArrayList<>();
        String expectedText = null;
        for(SearchCondition sc : by.searchConditions){
            if(sc.searchConditionType.equals(SearchConditionType.TEXT_REGEX_MATCH)){
                expectedText = sc.objects.toString();
            }
        }
        if(expectedText == null) return returnElements;
        GenericInteractionMethods jim = new GenericInteractionMethods(null);
        for(Object c : componentList){
            if(jim.getText(c) != null && jim.getText(c).contains(expectedText)){
                recognitionDescription.add("Found partial match for text '" + expectedText + "' in object with text '" + jim.getText(c) + "' of class '" + c.getClass().toString() + "'. Adding this object to possible matches list.");
                returnElements.add(c);
            }
        }
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
        if(by == null){
            description += ", by: null";
        }else {
            description += ", by: " + by.toString();
        }
        description += "', cached object stored: " + String.valueOf(cachedElement != null) + "]";
        return description;
    }


}
