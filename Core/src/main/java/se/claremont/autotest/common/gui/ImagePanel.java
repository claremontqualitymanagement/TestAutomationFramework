package se.claremont.autotest.common.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImagePanel extends JLabel {

    private BufferedImage image;

    public ImagePanel(BufferedImage image) {
        this.image = image;
        this.setSize(new Dimension(image.getWidth(), image.getHeight()));

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this); // see javadoc for more info on the parameters
    }
}
