package se.claremont.autotest.javasupport.gui.guirecordingwindow;

import se.claremont.autotest.javasupport.objectstructure.JavaGuiElement;

import java.awt.Window;
import java.util.ArrayList;
import java.util.List;

public class PageObject {
    Window w;
    List<JavaGuiElement> elements = new ArrayList<>();

    @Override
    public String toString(){
        return null;
    }
}
