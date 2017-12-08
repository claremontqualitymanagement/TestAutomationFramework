package se.claremont.autotest.javasupport.gui;

import javax.swing.*;

public class TempMain {

    static JFrame tempFrame = new JFrame();

    public static void main(String... arg){
        tempFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        tempFrame.getContentPane().add(new JavaSupportTab().panel);
        tempFrame.pack();
        tempFrame.setVisible(true);
    }
}
