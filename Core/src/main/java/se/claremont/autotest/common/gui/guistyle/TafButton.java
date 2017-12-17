package se.claremont.autotest.common.gui.guistyle;

import se.claremont.autotest.common.support.StringManagement;
import se.claremont.autotest.common.support.SupportMethods;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class TafButton extends TafButtonType {

    public TafButton(String label){
        super(label);
    }

    public TafButton(String label, Icon icon){
        super(label, icon);
    }

}
