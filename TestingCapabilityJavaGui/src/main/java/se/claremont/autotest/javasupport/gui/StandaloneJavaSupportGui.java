package se.claremont.autotest.javasupport.gui;

import se.claremont.autotest.common.gui.guistyle.TafFrame;

import javax.swing.*;
import java.awt.*;

public class StandaloneJavaSupportGui {

    static TafFrame window = new TafFrame();

    public static void main(String... arg){
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.getContentPane().add(new JavaSupportTab(window).panel);
        window.setSize(Toolkit.getDefaultToolkit().getScreenSize().width /2, Toolkit.getDefaultToolkit().getScreenSize().height /2);
        window.pack();
        window.setVisible(true);
    }
}
