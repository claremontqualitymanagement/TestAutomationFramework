package se.claremont.taf.javasupport.gui.guirecordingwindow.guimap;

import java.util.HashMap;
import java.util.Map;

public class GuiMap {

    Map<String, GuiWindow> guiWindowSet = new HashMap<>();

    public void addWindow(String windowTitleAsRegex, GuiWindow guiWindow){
        this.guiWindowSet.put(windowTitleAsRegex, guiWindow);
    }

}