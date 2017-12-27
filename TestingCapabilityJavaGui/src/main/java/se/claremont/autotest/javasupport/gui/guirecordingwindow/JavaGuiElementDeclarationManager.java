package se.claremont.autotest.javasupport.gui.guirecordingwindow;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import se.claremont.autotest.common.support.StringManagement;
import se.claremont.autotest.common.testcase.TestCase;
import se.claremont.autotest.javasupport.interaction.GenericInteractionMethods;
import se.claremont.autotest.javasupport.interaction.MethodDeclarations;
import se.claremont.autotest.javasupport.interaction.MethodInvoker;
import se.claremont.autotest.javasupport.interaction.elementidentification.By;
import se.claremont.autotest.javasupport.objectstructure.JavaGuiElement;
import se.claremont.autotest.javasupport.objectstructure.JavaWindow;

import java.awt.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Used by TAF GUI to generate unique identification mechanisms for elements interacted with,
 * and corresponding code as string for code generation from recordings.
 */
public class JavaGuiElementDeclarationManager {

    @JsonProperty private static Set<String> usedElementNames = new HashSet<>();
    @JsonProperty private static Set<JavaGuiElement> declaredJavaGuiElements = new HashSet<>();

    @JsonIgnore private JavaGuiElement returnElement = null;
    @JsonIgnore private Component component;
    @JsonIgnore private By by = new By();
    @JsonIgnore private String javaGuiElementName;
    @JsonIgnore private JavaWindow javaWindow;
    @JsonIgnore private GenericInteractionMethods java = new GenericInteractionMethods(new TestCase());

    /**
     * Attempts to create a uniquely identifiable JavaGuiElement from the component.
     * Method meant to be used for GUI recording purposes.
     *
     * @param component The component to create a corresponding JavaGuiElement declaration for.
     * @return returns a JavaGuiElement if successful, null if not.
     */
    @JsonIgnore
    public static JavaGuiElement createJavaGuiElement(Component component) {
        JavaGuiElementDeclarationManager jgedm = new JavaGuiElementDeclarationManager();
        GenericInteractionMethods java = new GenericInteractionMethods(new TestCase());
        JavaGuiElement javaGuiElement = jgedm.getUniqueElementDescription(component);
        if (jgedm.isUniquelyIdentifiable(javaGuiElement))
            return javaGuiElement;
        jgedm.addUniquelyIdentifiableAncestorToby();
        return javaGuiElement;
    }

    /**
     * The usedElementNames is a set of known declared elements, to avoid naming elements the
     * same as already declared elements and thereby getting compilation errors.
     *
     * @param elementName JavaGuiElement elementName to add to list.
     */
    @JsonIgnore
    public static void addElementNameToSetOfAlreadyDeclaredElementNames(String elementName){
        usedElementNames.add(elementName);
    }

    /**
     * The list of declared elements is used for better code generation.
     *
     * @param javaGuiElement JavaGuiElement to add.
     */
    @JsonIgnore
    public static void addJavaGuiElementToDeclaredElementsList(JavaGuiElement javaGuiElement){
        declaredJavaGuiElements.add(javaGuiElement);
        usedElementNames.add(javaGuiElement.getName());
    }

    /**
     * The list of declared elements is used for better code generation.
     *
     * @param javaGuiElements JavaGuiElement to add.
     */
    @JsonIgnore
    public static void addJavaGuiElementsToDeclaredElementsList(Collection<JavaGuiElement> javaGuiElements){
        declaredJavaGuiElements.addAll(javaGuiElements);
        for(JavaGuiElement javaGuiElement : javaGuiElements){
            usedElementNames.add(javaGuiElement.getName());
        }
    }

    /**
     * The usedElementNames is a set of known declared elements, to avoid naming elements the
     * same as already declared elements and thereby getting compilation errors.
     *
     * @param elementNames JavaGuiElement elementNames to add to list.
     */
    @JsonIgnore
    public static void addElementNamesToSetOfAlreadyDeclaredElementNames(Collection<String> elementNames){
        usedElementNames.addAll(elementNames);
    }

    /**
     * Create a code reprsentation for a JavaGuiElement, to be used in code generation for recordings.
     *
     * @param javaGuiElement The JavaGuiElement to return the instansiation declaration for.
     * @return Returns a code section as a string.
     */
    @JsonIgnore
    public static String javaGuiElementAsCodeString(JavaGuiElement javaGuiElement) {
        if (javaGuiElement == null) return null;
        StringBuilder sb = new StringBuilder();
        sb.append("JavaGuiElement ").append(javaGuiElement.getName()).append(" = new JavaGuiElement(").append(javaGuiElement.by.asCode()).append(");");
        return sb.toString();
    }

    /**
     * Creates a HTML representation of the element declaration, for use in HTML or TextPanes.
     *
     * @param javaGuiElement The JavaGuiElement to represent
     * @return A String formatted as a HTML document.
     */
    @JsonIgnore
    public static String javaGuiElementAsCodeHtml(JavaGuiElement javaGuiElement){
        return  "<html><body><pre>" +
                javaGuiElementAsCodeString(javaGuiElement)
                        .replace(" ", "&nbsp;")
                        .replace(System.lineSeparator(), "<br>") +
                "</pre></bodt></html>";
    }

    /**
     * Makes sure duplicate names get number increments to make them unique.
     *
     * @param nameSuggestion The suggested element name,
     * @param className The class name of the element, to be used in the name.
     * @return Returns a string containing a name unique to the list of known element declarations.
     */
    @JsonIgnore
    private String getUniqueName(String nameSuggestion, String className) {
        nameSuggestion = StringManagement.methodNameWithOnlySafeCharacters(nameSuggestion);
        if(nameSuggestion.length() > 50) nameSuggestion = nameSuggestion.substring(0, 50);
        if (!usedElementNames.contains(nameSuggestion + className)) {
            usedElementNames.add(nameSuggestion + className);
            return nameSuggestion + className;
        }
        int counter = 2;
        while (usedElementNames.contains(nameSuggestion + String.valueOf(counter) + className)) {
            counter++;
        }
        usedElementNames.add(nameSuggestion + String.valueOf(counter) + className);
        return nameSuggestion + String.valueOf(counter) + className;
    }

    /**
     * Creates a JavaGuiElement with By search criteria matching current AWT/Swing component properties.
     *
     * @param component The component to use for JavaGuiElement creation.
     * @return Returns a JavaGuiElement from the component, and with an appropriate name.
     */
    @JsonIgnore
    private JavaGuiElement getUniqueElementDescription(Component component) {

        if (component == null || java == null) return null;
        this.component = component;

        String className = component.getClass().getSimpleName();
        String elementName = component.getName();
        String elementText = (String) MethodInvoker.invokeTheFirstEncounteredMethodFromListOfMethodNames(component, MethodDeclarations.textGettingMethodsInAttemptOrder);

        by = by.andByClass(className);

        if (elementName != null && elementName.length() > 0) {
            by = by.andByName(elementName);
            javaGuiElementName = getUniqueName(elementName, component.getClass().getSimpleName());
        }

        if (elementText != null) {
            by = by.andByExactText(elementText);
            if (javaGuiElementName == null)
                javaGuiElementName = getUniqueName(elementText, component.getClass().getSimpleName());
        }

        if (javaGuiElementName == null)
            javaGuiElementName = getUniqueName("NoNameElement", component.getClass().getSimpleName());

        javaWindow = getElementWindowComponent();

        returnElement = new JavaGuiElement(by, javaGuiElementName);

        return returnElement;
    }

    /**
     * Tests if the element description is enough to return exactly one match.
     *
     * @param javaGuiElement The JavaGuiElement to check identification of.
     * @return Returns true only if exactly one match.
     */
    @JsonIgnore
    private boolean isUniquelyIdentifiable(JavaGuiElement javaGuiElement) {
        return javaWindow.getElementMatchCount(javaGuiElement) == 1;
    }

    /**
     * Attempts to add reference to parent element if needed for unique identification,
     * and ordinal number if that is not enough.
     */
    @JsonIgnore
    private void addUniquelyIdentifiableAncestorToby() {
        Component parentElement = component.getParent();
        while (true && !Window.class.isAssignableFrom(parentElement.getClass())) {
            JavaGuiElement parentJavaGuiElement = getUniqueElementDescription(parentElement);
            if (isUniquelyIdentifiable(parentJavaGuiElement)) {
                declaredJavaGuiElements.add(parentJavaGuiElement);
                returnElement.by = returnElement.by.byBeingDescendantOf(parentJavaGuiElement);
                if (isUniquelyIdentifiable(returnElement)) {
                    declaredJavaGuiElements.add(returnElement);
                    return;
                } else {
                    java.util.List<Object> potentialSameComponent = javaWindow.getMatchingComponents(returnElement);
                    for(int match = 0; match < javaWindow.getElementMatchCount(returnElement); match++){
                        if(potentialSameComponent.get(match).toString().equals(component.toString())){
                            returnElement.by = returnElement.by.andByOrdinalNumber(match + 1);
                            declaredJavaGuiElements.add(returnElement);
                            return;
                        }
                    }
                }
            } else {
                parentElement = parentElement.getParent();
            }
        }
        System.out.println("Could not identify component " + component.toString() + " uniquely. Current By statement: " + returnElement.by.toString() + ". The top parent element reached is " + parentElement.toString());
        return;
    }

    /**
     * Finds the element AWT Window component holding the component.
     *
     * @return Returns the Window object (JDialog, JFrame, Frame, Dialog and so forth).
     */
    @JsonIgnore
    private JavaWindow getElementWindowComponent() {

        Component potentialParent = component;

        while (!Window.class.isAssignableFrom(potentialParent.getClass())) {
            potentialParent = potentialParent.getParent();
        }

        return new JavaWindow((Window) potentialParent);
    }
}

