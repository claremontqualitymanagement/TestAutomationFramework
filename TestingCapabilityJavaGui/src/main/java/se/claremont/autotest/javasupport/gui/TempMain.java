package se.claremont.autotest.javasupport.gui;

import javax.swing.*;

public class TempMain {

    public static void main(String... arg){
        JFrame tempFrame = new JFrame();
        tempFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        tempFrame.getContentPane().add(new JavaSupportTab().panel);
        tempFrame.pack();
        tempFrame.setVisible(true);
    }
}
