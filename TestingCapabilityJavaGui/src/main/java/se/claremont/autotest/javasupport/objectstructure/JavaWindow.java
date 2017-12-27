package se.claremont.autotest.javasupport.objectstructure;

import se.claremont.autotest.common.support.StringManagement;
import se.claremont.autotest.common.support.SupportMethods;
import se.claremont.autotest.javasupport.applicationundertest.applicationstarters.ApplicationStarter;
import se.claremont.autotest.javasupport.interaction.GenericInteractionMethods;
import se.claremont.autotest.javasupport.interaction.MethodDeclarations;
import se.claremont.autotest.javasupport.interaction.MethodInvoker;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A JavaWindow is any GUI Window used in automation.
 *
 * Created by jordam on 2017-02-14.
 */
@SuppressWarnings({"SameParameterValue", "WeakerAccess"})
public class JavaWindow {
    MethodInvoker methodInvoker = new MethodInvoker();
    String titleAsRegularExpression;

    public JavaWindow(String titleAsRegularExpression){
        this.titleAsRegularExpression = titleAsRegularExpression;
    }

    public JavaWindow(Window window){
        if(window != null){
            titleAsRegularExpression = (String) methodInvoker.invokeTheFirstEncounteredMethod(window, MethodDeclarations.titleGetterMethodsInAttemptOrder);
        }
    }

    /**
     * Halts execution while waiting for this window to become visible. If it has not become visible before the timeout it returns null.
     *
     * @param timeoutInSeconds Timeout period to wait, stated in seconds.
     * @return Returns the Window itself if encountered before the timeout, otherwise returning null.
     */
    public Object waitForWindowToAppear(int timeoutInSeconds){
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < timeoutInSeconds *1000){
            Object o = getWindow();
            if(getWindow() != null) return o;
            wait(100);
        }
        return null;
    }

    /**
     * Returns the runtime title of this window.
     *
     * @return Returns the runtime title of this window.
     */
    public String getTitle(){
        return (String) methodInvoker.invokeTheFirstEncounteredMethod(getWindow(), MethodDeclarations.titleGetterMethodsInAttemptOrder);
    }

    /**
     * Identifies and returns the runtime interaction object of this window.
     *
     * @return The interaction runtime instance of this window if possible to identify, otherwise null.
     */
    public Object getWindow(){
        Window[] windows = Window.getOwnerlessWindows();
        if(windows.length == 0) return null;
        ArrayList<Window> nonShownWindows = new ArrayList<>();
        for(Window w : windows){
            String title = (String)methodInvoker.invokeTheFirstEncounteredMethod(w, MethodDeclarations.titleGetterMethodsInAttemptOrder);
            if(SupportMethods.isRegexMatch(title, this.titleAsRegularExpression)){
                if(w.isShowing())
                    return w;
                nonShownWindows.add(w);
            }
        }
        if(nonShownWindows.size() > 0) return nonShownWindows.get(0);
        return null;
    }

    /**
     * Collects all sub element of the window and tries converting them to JavaGuiElements and returning that list.
     * Used for PositionBasedIdentification.
     *
     * @return Returns all sub element of the window.
     */
    public ArrayList<JavaGuiElement> getComponentsAsJavaGuiElements(){
        ArrayList<JavaGuiElement> javaGuiElements = new ArrayList<>();
        for(Object o: getComponents()){
            javaGuiElements.add(new JavaGuiElement(o));
        }
        return javaGuiElements;
    }

    /**
     * Returns all components found at runtime in the window.
     *
     * @return Returns a list of all subcomponents of the window.
     */
    public List<Object> getComponents(){
        Object window = getWindow();
        List<Object> componentList = new ArrayList<>();
        if(!methodInvoker.objectHasAnyOfTheMethods(window, MethodDeclarations.subComponentCountMethodsInAttemptOrder) || !methodInvoker.objectHasAnyOfTheMethods(window, MethodDeclarations.subComponentGetterMethodsInAttemptOrder)){
            return componentList;
        }

        Integer windowComponentCount = (Integer) methodInvoker.invokeTheFirstEncounteredMethod(window, MethodDeclarations.subComponentCountMethodsInAttemptOrder);
        if(windowComponentCount == null)return componentList;
        Object[] returnList = (Object[]) methodInvoker.invokeTheFirstEncounteredMethod(window, MethodDeclarations.subAllComponentsGettersMethodsInAttemptOrder);
        if(returnList != null && returnList.length > 0){
            for(Object object : returnList){
                componentList.add(object);
                componentList.addAll(addSubComponents(object));
            }
            return componentList;
        }
        for(int i = 0; i < windowComponentCount; i++){
            Object component = methodInvoker.invokeTheFirstEncounteredMethod(window, MethodDeclarations.subComponentGetterMethodsInAttemptOrder, i);
            if(component == null) continue;
            componentList.add(component);
            componentList.addAll(addSubComponents(component));
        }
        return componentList;
    }



    /**
     * Prints the currently identified components to standard out.
     */
    public void printIdentifiedComponents(){
        for(Object c : getComponents()){
            System.out.println(c.toString());
        }
    }

    /**
     * Goes through all identified elements in the GUI and collects the displayed text in these. Should probably only be used in debugging.
     *
     * @return Returns a list of the texts of all identified components.
     */
    public List<String> textsInComponents(){
        List<String> returnTexts = new ArrayList<>();
        GenericInteractionMethods jim = new GenericInteractionMethods(null);
        for(Object c : getComponents()){
            returnTexts.add(jim.getText(c));
        }
        return returnTexts;
    }

    /**
     * Tries to create element descriptions for the elements in the window, to be used in automation.
     *
     * @param outputFile The file to write the window element descriptors to.
     */
    public void mapWindowToDescriptionClass(String outputFile){
        waitForWindowToAppear(15);
        createElementDefinitions(getWindow(), outputFile);
    }

    private void createElementDefinitions(Object window, String path){
        Window theWindow = (Window) window;
        StringBuilder sb = new StringBuilder();
        GenericInteractionMethods i = new GenericInteractionMethods(null);
        JavaWindow javaWindow = new JavaWindow(theWindow);
        String title = javaWindow.getTitle();
        sb.append("public class ").append(StringManagement.methodNameWithOnlySafeCharacters(title)).append(" {").append(System.lineSeparator());
        sb.append(System.lineSeparator());
        List<Object> objects = getComponents();
        for(Object o : objects){
            String name = i.getName(o);
            String text = i.getText(o);
            String className = o.getClass().toString();
            className = className.substring(className.lastIndexOf("."));
            if(name != null){
                sb.append("   public static JavaGuiElement ").append(StringManagement.methodNameWithOnlySafeCharacters(name + "_" + className)).append("() {").append(System.lineSeparator());
                sb.append("      return new JavaGuiElement(\"").append(StringManagement.methodNameWithOnlySafeCharacters(name + className.replace("(", "").replace(")", ""))).append("\", \"").append(name).append("\", JavaGuiElement.IdType.ELEMENT_NAME);").append(System.lineSeparator());
                sb.append("   }").append(System.lineSeparator()).append(System.lineSeparator());
            } else if(text != null){
                sb.append("   public static JavaGuiElement ").append(StringManagement.methodNameWithOnlySafeCharacters(text + "_" + className)).append("() {").append(System.lineSeparator());
                sb.append("      return new JavaGuiElement(\"").append(StringManagement.methodNameWithOnlySafeCharacters(text + className.replace("(", "").replace(")", ""))).append("\", \"").append(text).append("\", JavaGuiElement.IdType.ELEMENT_TEXT);").append(System.lineSeparator());
                sb.append("   }").append(System.lineSeparator()).append(System.lineSeparator());
            } else {
                sb.append("/*").append(System.lineSeparator());
                sb.append("   Cannot create proper code for element of class '").append(o.getClass().toString()).append("'.").append(System.lineSeparator());
                sb.append("   An object toString() method call returns:").append(System.lineSeparator());
                sb.append("   ").append(o.toString()).append(System.lineSeparator());
                sb.append("*/").append(System.lineSeparator()).append(System.lineSeparator());
            }
        }
        sb.append("}").append(System.lineSeparator());
        SupportMethods.saveToFile(sb.toString(), path);
    }

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

    private void wait (int milliseconds){
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            System.out.println("Could not wait " + milliseconds + " milliseconds. Error: " + e.toString());
        }
    }

    public int getElementMatchCount(JavaGuiElement javaGuiElement) {
        int matchesCount = 0;
        for(Object subElement : getComponents()){
            if(javaGuiElement.by.isMatch(subElement))
                matchesCount++;
        }
        return matchesCount;
    }

    public List<Object> getMatchingComponents(JavaGuiElement javaGuiElement) {
        List<Object> matches = new ArrayList<>();
        for(Object subElement : getComponents()){
            if(javaGuiElement.by.isMatch(subElement))
                matches.add(subElement);
        }
        return matches;
    }
}
