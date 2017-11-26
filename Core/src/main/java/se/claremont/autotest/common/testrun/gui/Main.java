package se.claremont.autotest.common.testrun.gui;

import se.claremont.autotest.common.testrun.TestRun;

import java.awt.*;
import java.util.HashMap;

public class Main {


    public static void main(String... args){
        if(!Desktop.isDesktopSupported()){
            System.out.println("No desktop supported. Please use command line interface.");
            return;
        }
        Gui gui = new Gui();
        gui.activate();
    }
}
