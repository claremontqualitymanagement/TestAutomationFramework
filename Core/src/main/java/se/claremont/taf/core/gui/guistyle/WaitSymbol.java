package se.claremont.taf.core.gui.guistyle;

//import com.sun.javafx.tk.Toolkit;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class WaitSymbol extends JDialog {

    public WaitSymbol(Frame parentFrame){
        super(parentFrame);
        URL url = WaitSymbol.class.getResource("/waiting.gif");
        this.getRootPane().setOpaque(false);
        ImageIcon imageIcon = new ImageIcon(url);
        Image image = imageIcon.getImage();
        int newHeight = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height /20;
        int newWidth = imageIcon.getIconWidth()/(imageIcon.getIconHeight()/newHeight);
        imageIcon.setImage(image.getScaledInstance(newWidth, newHeight, Image.SCALE_DEFAULT));
        JLabel label = new JLabel(imageIcon);
        label.setOpaque(false);
        this.getContentPane().add(label);
        this.setUndecorated(true);
        this.setLocationRelativeTo(parentFrame);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(imageIcon.getIconWidth(), imageIcon.getIconHeight());
        this.setVisible(true);
    }

}
