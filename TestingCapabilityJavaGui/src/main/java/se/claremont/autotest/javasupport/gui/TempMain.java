package se.claremont.autotest.javasupport.gui;

import javax.swing.*;

public class TempMain {

    public static void main(String... arg){
        JFrame tempFrame = new JFrame();
        tempFrame.getContentPane().add(new JavaSupportTab().panel);
        tempFrame.pack();
        tempFrame.setVisible(true);
    }
}
