package se.claremont.autotest.javasupport.gui;

import se.claremont.autotest.common.gui.guistyle.TafFrame;

import javax.swing.*;
import java.awt.*;

public class TempMain {

    static TafFrame tempFrame = new TafFrame();

    public static void main(String... arg){
        tempFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        tempFrame.getContentPane().add(new JavaSupportTab(tempFrame).panel);
        tempFrame.setSize(Toolkit.getDefaultToolkit().getScreenSize().width /2, Toolkit.getDefaultToolkit().getScreenSize().height /2);
        tempFrame.pack();
        tempFrame.setVisible(true);
    }
}
