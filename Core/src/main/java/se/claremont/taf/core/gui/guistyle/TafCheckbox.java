package se.claremont.taf.core.gui.guistyle;

import se.claremont.taf.core.support.StringManagement;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class TafCheckbox extends JCheckBox {

    public TafCheckbox(String text){
        super(text);
        setName(StringManagement.stringToCapitalInitialCharacterForEachWordAndNoSpaces(text));
        setFont(AppFont.getInstance());
        scaleCheckBoxIcon();
        setForeground(TafGuiColor.textColor);
        setBackground(TafGuiColor.backgroundColor);
    }

    @Override
    public void setToolTipText(String tooltip){
        super.setToolTipText(Helper.tooltipStypeInfoHead + tooltip + Helper.tooltipStypeInfoTail);
    }

    private void scaleCheckBoxIcon(){
        boolean previousState = isSelected();
        setSelected(false);
        FontMetrics boxFontMetrics =  getFontMetrics(getFont());
        Icon boxIcon = UIManager.getIcon("CheckBox.icon");
        BufferedImage boxImage = new BufferedImage(
                boxIcon.getIconWidth(), boxIcon.getIconHeight(), BufferedImage.TYPE_INT_ARGB
        );
        Graphics graphics = boxImage.createGraphics();
        try{
            boxIcon.paintIcon(this, graphics, 0, 0);
        }finally{
            graphics.dispose();
        }
        ImageIcon newBoxImage = new ImageIcon(boxImage);
        Image finalBoxImage = newBoxImage.getImage().getScaledInstance(
                boxFontMetrics.getHeight(), boxFontMetrics.getHeight(), Image.SCALE_SMOOTH
        );
        setIcon(new ImageIcon(finalBoxImage));

        setSelected(true);
        Icon checkedBoxIcon = UIManager.getIcon("CheckBox.icon");
        BufferedImage checkedBoxImage = new BufferedImage(
                checkedBoxIcon.getIconWidth(),  checkedBoxIcon.getIconHeight(), BufferedImage.TYPE_INT_ARGB
        );
        Graphics checkedGraphics = checkedBoxImage.createGraphics();
        try{
            checkedBoxIcon.paintIcon(this, checkedGraphics, 0, 0);
        }finally{
            checkedGraphics.dispose();
        }
        ImageIcon newCheckedBoxImage = new ImageIcon(checkedBoxImage);
        Image finalCheckedBoxImage = newCheckedBoxImage.getImage().getScaledInstance(
                boxFontMetrics.getHeight(), boxFontMetrics.getHeight(), Image.SCALE_SMOOTH
        );
        setSelectedIcon(new ImageIcon(finalCheckedBoxImage));
        setSelected(false);
        setSelected(previousState);
    }

}
