package se.claremont.autotest.gui;

import java.awt.*;

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
