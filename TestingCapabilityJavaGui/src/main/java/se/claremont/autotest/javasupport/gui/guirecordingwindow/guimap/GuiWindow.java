package se.claremont.autotest.javasupport.gui.guirecordingwindow.guimap;

import se.claremont.autotest.javasupport.objectstructure.JavaGuiElement;
import se.claremont.autotest.javasupport.objectstructure.JavaWindow;

import java.awt.*;
import java.util.HashMap;

/**
 * Each window in a system under test is a GUI window.
 */
public class GuiWindow extends HashMap<String, Component> {

    String titleAsRegex;
    Window windowComponent;

    public GuiWindow(String titleAsRegex, Window component){
        this.titleAsRegex = titleAsRegex;
        this.windowComponent = component;
    }

    public void addComponent(String componentDeclarationCode, Component component){
        this.put(componentDeclarationCode, component);
    }

    public JavaWindow asJavaWindow(){
        if(windowComponent != null){
            return new JavaWindow(windowComponent);
        }
        return new JavaWindow(titleAsRegex);
    }
}
