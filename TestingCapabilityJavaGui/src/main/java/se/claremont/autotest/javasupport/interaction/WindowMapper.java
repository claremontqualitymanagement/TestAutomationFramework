package se.claremont.autotest.javasupport.interaction;

import se.claremont.autotest.common.support.StringManagement;
import se.claremont.autotest.common.support.SupportMethods;
import se.claremont.autotest.javasupport.objectstructure.JavaWindow;

import java.util.ArrayList;
import java.util.List;

public class WindowMapper {
    List<Object> unidentifiedComponents = new ArrayList<>();
    List<Object> identifiedComponents = new ArrayList<>();
    StringBuilder sb = new StringBuilder();
    GenericInteractionMethods gim = new GenericInteractionMethods(null);
    JavaWindow javaWindow;

    private void createElementDefinitions(JavaWindow javaWindow, String outputFilePath){
        this.javaWindow = javaWindow;
        unidentifiedComponents = javaWindow.getComponents();
        addClassHeader();
        addElementsIdentifiedByName();
        addElementsIdentifiedByUniqueClass();
        addElementsIdentifiedByUniqueText();
        //addElementsIdentifiedByClassNameAndNameCombination();
        //addElementsIdentifiedByClassAndTextCombination();
        //addElementsIdentifiedByNameAndTextCombination();
        //addElementsIdentifiedByAncestorAndAnyCombination();
        addUnidentifiedElements();
        SupportMethods.saveToFile(sb.toString(), outputFilePath);
    }

    private void addUnidentifiedElements() {
        sb.append(System.lineSeparator());
        sb.append("// Unidentified elements").append(System.lineSeparator());
        sb.append("// =======================").append(System.lineSeparator()).append(System.lineSeparator());
        for(Object o : unidentifiedComponents){
            sb.append("// ").append(o.toString()).append(System.lineSeparator()).append(System.lineSeparator());
        }
    }

    private void addElementsIdentifiedByUniqueText() {
        List<Object> elementsToRemove = new ArrayList<>();
        for(int i = 0; i < unidentifiedComponents.size(); i++){
            Object o = unidentifiedComponents.get(i);
            String text = gim.getText(o);
            if(text == null || text.length() == 0) continue;
            if(i == unidentifiedComponents.size()){
                addElementDefinition(o,"         .byExactText(\"" + text + "\")");
                elementsToRemove.add(o);
            } else {
                boolean duplicateFound = false;
                for(int j = i+1; j < unidentifiedComponents.size(); j++){
                    String elementText = gim.getText(unidentifiedComponents.get(j));
                    if(elementText != null && elementText.equals(text)){
                        duplicateFound = true;
                        break;
                    }
                }
                if(!duplicateFound){
                    addElementDefinition(o,"         .byExactText(\"" + text + "\")");
                    elementsToRemove.add(o);
                }
            }
        }
        unidentifiedComponents.removeAll(elementsToRemove);
    }
    
    private void addElementsIdentifiedByName() {
        List<Object> elementsToRemove = new ArrayList<>();
        for(int i = 0; i < unidentifiedComponents.size(); i++){
            Object o = unidentifiedComponents.get(i);
            String name = gim.getName(o);
            if(name == null || name.length() == 0) continue;
            if(i == unidentifiedComponents.size()){
                addElementDefinition(o,"         .byName(\"" + name + "\")");
                elementsToRemove.add(o);
            } else {
                boolean duplicateFound = false;
                for(int j = i+1; j < unidentifiedComponents.size(); j++){
                    String elementName = gim.getName(unidentifiedComponents.get(j));
                    if(elementName != null && elementName.equals(name)){
                        duplicateFound = true;
                        break;
                    }
                }
                if(!duplicateFound){
                    addElementDefinition(o,"         .byName(\"" + name + "\")");
                    elementsToRemove.add(o);
                }
            }
        }
        unidentifiedComponents.removeAll(elementsToRemove);
    }

    private void addElementsIdentifiedByUniqueClass() {
        List<Object> elementsToRemove = new ArrayList<>();
        for(int i = 0; i < unidentifiedComponents.size(); i++){
            Object o = unidentifiedComponents.get(i);
            String className = o.getClass().getSimpleName();
            if(className == null || className.length() == 0) continue;
            if(i == unidentifiedComponents.size()){
                addElementDefinition(o,"         .byClass(\"" + className + "\")");
                elementsToRemove.add(o);
            } else {
                boolean duplicateFound = false;
                for(int j = i+1; j < unidentifiedComponents.size(); j++){
                    String elementClassName = gim.getName(unidentifiedComponents.get(j));
                    if(elementClassName != null && elementClassName.equals(className)){
                        duplicateFound = true;
                        break;
                    }
                }
                if(!duplicateFound){
                    addElementDefinition(o,"         .byClass(\"" + className + "\")");
                    elementsToRemove.add(o);
                }
            }
        }
        unidentifiedComponents.removeAll(elementsToRemove);
    }

    private void addElementDefinition(Object o, String byStatement) {
        identifiedComponents.add(o);
        sb.append("   public static JavaGuiElement " + identifyElementName(o) + "(){").append(System.lineSeparator());
        sb.append("        return new JavaGuiElement(By" + System.lineSeparator() + byStatement).append(";").append(System.lineSeparator());
        sb.append("     }").append(System.lineSeparator());
        sb.append(System.lineSeparator());
    }

    private String identifyElementName(Object o) {
        String objectName = (String) MethodInvoker.invokeTheFirstEncounteredMethod(null, o, MethodDeclarations.componentNameGetterMethodsInAttemptOrder);
        if(objectName == null || objectName.length() == 0) 
            objectName = (String) MethodInvoker.invokeTheFirstEncounteredMethod(null, o, MethodDeclarations.textGettingMethodsInAttemptOrder);
        if (objectName == null || objectName.length() == 0) objectName = "NoNamedObject";
        String name = o.getClass().getSimpleName().replace(".", "_").replace(" ", "") + "_" + objectName.replace(" ", "");
        return name;        
    }

    private void addClassHeader() {
        String title = javaWindow.getTitle();
        sb.append("public class ").append(StringManagement.methodNameWithOnlySafeCharacters(title)).append(" {").append(System.lineSeparator());
        sb.append(System.lineSeparator());
    }

}
