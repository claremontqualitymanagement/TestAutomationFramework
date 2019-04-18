package se.claremont.taf.core.gui.guistyle;

import java.awt.*;

public class AppFont {

    private static Font appFont;

    public static Font getInstance(){
        if(appFont == null){
            setFontSize();
        }
        return appFont;
    }

    private static void setFontSize() {
        appFont = new Font("serif", Font.PLAIN, Toolkit.getDefaultToolkit().getScreenSize().height / 50);
    }

}
