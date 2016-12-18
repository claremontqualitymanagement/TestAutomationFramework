package se.claremont.autotest.guidriverpluginstructure.swingsupport;

import se.claremont.autotest.support.SupportMethods;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static se.claremont.autotest.support.StringManagement.methodNameWithOnlySafeCharacters;

/**
 * Created by jordam on 2016-09-18.
 */
@SuppressWarnings("WeakerAccess")
public class SwingWindow {
    Window window;
    Container rootContainer;
    SwingApplication swingApplication;
    String name;

    public SwingWindow(SwingApplication application, String windowTitle){
        this.swingApplication = application;
        this.name = stringToMethodName(windowTitle);

        for(Frame frame : Frame.getFrames()){
            if(frame.getTitle().equals(windowTitle)){
                window = new Window(frame);
                this.rootContainer = frame;
                break;
            }
        }
    }

    public ArrayList<Component> getChildren(){
        return getChildrenRecursive(rootContainer);
    }

    private ArrayList<Component> getChildrenRecursive(Container container){
        ArrayList<Component> componentList = new ArrayList<>();
        for(Component child : container.getComponents()){
            componentList.add(child);
            try{
                componentList.addAll(getChildrenRecursive((Container) child));
            }catch (Exception ignored){
                System.out.println("Could not containerize '" + child.toString() + "'.");
            }
        }
        return componentList;
    }

    //System.out.println(child.getClass().toString() + ", name: '" + child.getName() + "'");
    //if(child.getClass().getName().toLowerCase().contains("label")){
    //    JLabel label = (JLabel) child;
    //    System.out.println(label.getText());
    //}
    //System.out.println(child.toString());

    private static String elementClassToFrameworkClass(String elementClass){
        switch (elementClass){
            case ("JButton"):
                return "Button";
            case ("JLabel"):
                return "Label";
            case "JTextField":
                return "TextField";
            default:
                return elementClass;
        }
    }

    private static String stringToMethodName(String string){
        return methodNameWithOnlySafeCharacters(string);
    }

    public String map(){
        ArrayList<String> elementsDescriptions = new ArrayList<>();
        for(Component component : getChildren()){
            String elementDeclarationAttempt = "";
            ArrayList<String> description = new ArrayList<>();
            description.add("Class: '" + component.getClass().getSimpleName() + "'");
            if(component.getName() != null && component.getName().length() > 0){
                description.add("Name: '" + component.getName() + "'");
                elementDeclarationAttempt = elementClassToFrameworkClass(component.getClass().getSimpleName()) + " " + stringToMethodName(component.getName()) + " = new " + elementClassToFrameworkClass(component.getClass().getSimpleName()) + "(\"" + this.name + "\", \"" + component.getName() + "\");";
            }
            if(elementDeclarationAttempt != null && elementDeclarationAttempt.length() > 0) {
                System.out.println(String.join(", ", description));
                if(elementDeclarationAttempt != null && elementDeclarationAttempt.length() > 0){
                    elementsDescriptions.add(elementDeclarationAttempt);
                }
                continue;
            }
            switch (component.getClass().getSimpleName()){
                case "JButton":
                    JButton jButton = (JButton) component;
                    if(jButton.getText() == null) break;
                    description.add("Text: '" + jButton.getText() + "'");
                    elementDeclarationAttempt = "public static Button " + stringToMethodName(jButton.getText()) + " = new Button(\"" + this.name + "\", \"" + jButton.getText() + "\")" + SupportMethods.LF ;
                    break;
                case "JLabel":
                    JLabel jLabel = (JLabel)component;
                    if(jLabel.getText() == null) break;
                    elementDeclarationAttempt = "public static Label " + stringToMethodName(jLabel.getText()) + " = new Label(\"" + this.name + "\", \"" + jLabel.getText() + "\")" + SupportMethods.LF ;
                    description.add("Text: '" + jLabel.getText() + "'");
                    break;
                case "JList":
                    JList jList = (JList)component;
                    if(jList == null) return null;
                    List selected = jList.getSelectedValuesList();
                    if(selected == null)return null;
                    description.add("Selected values: '" + String.join("', '", selected) + "'");
                    break;
                case "JTextField":
                    JTextField jTextField = (JTextField) component;
                    description.add("Text: '" + jTextField.getText() + "'");
                    if(jTextField != null && jTextField.getToolTipText() != null && jTextField.getToolTipText().length() > 0){
                        description.add("Tooltip: '" + jTextField.getToolTipText() + "'");
                    }
                    if(jTextField.getText() == null) break;
                    elementDeclarationAttempt = "public static TextField " + stringToMethodName(jTextField.getText()) + " = new TextField(\"" + this.name + "\", \"" + jTextField.getText() + "\")" + SupportMethods.LF ;
                    break;
                default:
                    break;
            }
            System.out.println(String.join(", ", description));
            if(elementDeclarationAttempt != null && elementDeclarationAttempt.length() > 0){
                elementsDescriptions.add(elementDeclarationAttempt);
            }
        }
        System.out.println(String.join(SupportMethods.LF, elementsDescriptions));
        return String.join(SupportMethods.LF, elementsDescriptions);
    }

}
