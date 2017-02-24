package se.claremont.autotest.javamethods;

import se.claremont.autotest.common.support.StringManagement;
import se.claremont.autotest.common.support.SupportMethods;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
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

    public String getTitle(){
        return (String) methodInvoker.invokeTheFirstEncounteredMethod(getWindow(), MethodDeclarations.titleGetterMethodsInAttemptOrder);
    }

    public Object getWindow(){
        ArrayList<Window> windows = ApplicationStarter.getWindows();
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
     * Goes through all identified elements in the GUI and collects the displayed text in these.
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

    public void createWindowDescriptionClass(String outputFile){
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
                sb.append("   public static JavaGuiElement " + StringManagement.methodNameWithOnlySafeCharacters(name + "_" + className)).append("() {").append(System.lineSeparator());
                sb.append("      return new JavaGuiElement(\"" + StringManagement.methodNameWithOnlySafeCharacters(name + className.replace("(", "").replace(")", "")) + "\", \"" + name + "\", JavaGuiElement.IdType.ELEMENT_NAME);").append(System.lineSeparator());
                sb.append("   }").append(System.lineSeparator()).append(System.lineSeparator());
            } else if(text != null){
                sb.append("   public static JavaGuiElement " + StringManagement.methodNameWithOnlySafeCharacters(text + "_" + className)).append("() {").append(System.lineSeparator());
                sb.append("      return new JavaGuiElement(\"" + StringManagement.methodNameWithOnlySafeCharacters(text + className.replace("(", "").replace(")", "")) + "\", \"" + text + "\", JavaGuiElement.IdType.ELEMENT_TEXT);").append(System.lineSeparator());
                sb.append("   }").append(System.lineSeparator()).append(System.lineSeparator());
            } else {
                sb.append("/*").append(System.lineSeparator());
                sb.append("   Cannot create proper code for element of class '" + o.getClass().toString() + "'.").append(System.lineSeparator());
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



}
