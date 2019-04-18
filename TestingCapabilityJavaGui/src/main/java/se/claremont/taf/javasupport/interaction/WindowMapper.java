package se.claremont.taf.javasupport.interaction;

import se.claremont.taf.guidriverpluginstructure.PositionBasedIdentification.PositionBasedGuiElement;
import se.claremont.taf.guidriverpluginstructure.PositionBasedIdentification.PositionBasedIdentificator;
import se.claremont.taf.support.StringManagement;
import se.claremont.taf.support.SupportMethods;
import se.claremont.taf.javasupport.objectstructure.JavaGuiElement;
import se.claremont.taf.javasupport.objectstructure.JavaWindow;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class WindowMapper {
    List<Component> unidentifiedComponents = new ArrayList<>();
    List<Component> identifiedComponents = new ArrayList<>();
    StringBuilder sb = new StringBuilder();
    GenericInteractionMethods gim = new GenericInteractionMethods(null);
    JavaWindow javaWindow;

    public static void Map(JavaWindow javaWindow, String outputFilePath){
        WindowMapper windowMapper = new WindowMapper();
        windowMapper.createElementDefinitions(javaWindow, outputFilePath);
    }

    private void createElementDefinitions(JavaWindow javaWindow, String outputFilePath){
        this.javaWindow = javaWindow;
        unidentifiedComponents = javaWindow.getComponents();
        addClassHeader();
        addWindowDeclaration();
        addElementsIdentifiedByName();
        addElementsIdentifiedByUniqueClass();
        addElementsIdentifiedByUniqueText();
        //addElementsIdentifiedByClassNameAndNameCombination();
        //addElementsIdentifiedByClassAndTextCombination();
        //addElementsIdentifiedByNameAndTextCombination();
        //addElementsIdentifiedByAncestorAndAnyCombination();
        addElementsToTheRightOfIdentifiedElement();
        addUnidentifiedElements();
        sb.append(System.lineSeparator()).append("}").append(System.lineSeparator());
        SupportMethods.saveToFile(sb.toString(), outputFilePath);
    }

    private void addElementsToTheRightOfIdentifiedElement() {
        Integer numberOfUnidentifiedComponents = null;
        while(numberOfUnidentifiedComponents == null || numberOfUnidentifiedComponents != unidentifiedComponents.size()){
            numberOfUnidentifiedComponents = unidentifiedComponents.size();
            ArrayList<PositionBasedGuiElement> javaGuiElements = new ArrayList<>();
            for(Component c : unidentifiedComponents){
                javaGuiElements.add(new JavaGuiElement(c));
            }
            for(Component potentialRightComponent : unidentifiedComponents){
                for(Component potentialLeftElement : identifiedComponents){
                    Component candidateRightComponent = (Component)PositionBasedIdentificator.fromAllThePositionBasedElements(javaGuiElements).elementImmediatelyToTheRightOf(new JavaGuiElement(potentialLeftElement));
                    if(candidateRightComponent == null) continue;
                    if(candidateRightComponent.toString().equals(potentialRightComponent.toString())){
                        System.out.println("Element '" + candidateRightComponent.toString() + "' is immediately to the right of element '" + potentialLeftElement + "'.");
                        sb.append("   public static JavaGuiElement = new JavaGuiElement((Component)(Component)PositionBasedIdentificator.fromAllThePositionBasedElements(javaGuiElements).elementImmediatelyToTheRightOf(new JavaGuiElement(potentialLeftElement)));").append(System.lineSeparator());
                    }
                }
            }
        }

    }

    private void addWindowDeclaration() {
        String title = javaWindow.getTitle();
        sb.append("   public static JavaWindow window = new JavaWindow(\"" + title + "\");").append(System.lineSeparator()).append(System.lineSeparator());
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
            Component o = unidentifiedComponents.get(i);
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
        List<Component> elementsToRemove = new ArrayList<>();
        for(int i = 0; i < unidentifiedComponents.size(); i++){
            Component o = unidentifiedComponents.get(i);
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
        List<Component> elementsToRemove = new ArrayList<>();
        for(int i = 0; i < unidentifiedComponents.size(); i++){
            Component o = unidentifiedComponents.get(i);
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

    private void addElementDefinition(Component o, String byStatement) {
        identifiedComponents.add(o);
        String elementName = identifyElementName(o);
        sb.append("   public static JavaGuiElement " + elementName + "(){").append(System.lineSeparator());
        sb.append("        return new JavaGuiElement(window, By").append(System.lineSeparator()).append("   ").append(byStatement).append(",").append(System.lineSeparator() + "            \"" + elementName + "\");").append(System.lineSeparator());
        sb.append("   }").append(System.lineSeparator());
        sb.append(System.lineSeparator());
    }

    private String identifyElementName(Object o) {
        String objectName = (String) MethodInvoker.invokeTheFirstEncounteredMethod(null, o, MethodDeclarations.componentNameGetterMethodsInAttemptOrder);
        if(objectName == null || objectName.length() == 0) 
            objectName = (String) MethodInvoker.invokeTheFirstEncounteredMethod(null, o, MethodDeclarations.textGettingMethodsInAttemptOrder);
        if (objectName == null || objectName.length() == 0) objectName = "NoNamedObject";
        objectName = StringManagement.safeVariableName(objectName);
        String className = o.getClass().getSimpleName();
        if(!objectName.endsWith(className) || !objectName.startsWith(className)){
            objectName = objectName + o.getClass().getSimpleName().replace(".", "_").replace(" ", "");
        }
        return objectName;
    }

    private void addClassHeader() {
        String title = javaWindow.getTitle();
        sb.append("import se.claremont.autotest.javasupport.interaction.elementidentification.By;").append(System.lineSeparator());
        sb.append("import se.claremont.autotest.javasupport.objectstructure.JavaGuiElement;").append(System.lineSeparator());
        sb.append("import se.claremont.autotest.javasupport.objectstructure.JavaWindow;").append(System.lineSeparator());
        sb.append(System.lineSeparator());
        String windowName = StringManagement.safeClassName(title) + "Window";
        sb.append("public class ").append(windowName).append(" {").append(System.lineSeparator());
        sb.append(System.lineSeparator());
    }

}
