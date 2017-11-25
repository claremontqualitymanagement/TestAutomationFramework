package se.claremont.autotest.gui;

import se.claremont.autotest.common.testrun.TestRun;

import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;

public class Main {

    public static HashMap<String, String> defaultSettings;

    public static void main(String... args){
        defaultSettings = new HashMap<>(TestRun.getSettings());
        if(!Desktop.isDesktopSupported()){
            System.out.println("No desktop supported. Please use command line interface.");
            return;
        }
        Gui gui = new Gui();
        gui.activate();
    }
}
