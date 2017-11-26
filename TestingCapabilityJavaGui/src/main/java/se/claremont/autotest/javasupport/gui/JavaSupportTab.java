package se.claremont.autotest.javasupport.gui;

import se.claremont.autotest.common.gui.plugins.IGuiTab;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JavaSupportTab implements IGuiTab{

    private Font appFont;
    JPanel panel;

    public JavaSupportTab(){
        panel = new JPanel();
        setFontSize();

        JLabel text = new JLabel("Hej");
        text.setFont(appFont);
        text.setName("MainText");

        JButton sutDeclarationButton = new JButton("Declare SUT");
        sutDeclarationButton.setFont(appFont);
        sutDeclarationButton.setName("SutDeclarationButton");
        sutDeclarationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new DeclareSUTWindow(appFont);
            }
        });

        panel.add(text);
        panel.add(sutDeclarationButton);

        panel.setVisible(true);
    }

    public JPanel getPanel(){
        return panel;
    }

    public String getName(){
        return "Java";
    }

    private void setFontSize() {
        appFont = new Font("serif", Font.PLAIN, Toolkit.getDefaultToolkit().getScreenSize().height / 50);
    }

}
