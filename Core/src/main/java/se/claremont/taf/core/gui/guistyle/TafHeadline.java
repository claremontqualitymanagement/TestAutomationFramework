package se.claremont.taf.core.gui.guistyle;

import java.awt.*;

public class TafHeadline extends TafLabel {

    public TafHeadline(String label) {
        super(label);
        setFont(new Font(AppFont.getInstance().getName(), AppFont.getInstance().getStyle(), AppFont.getInstance().getSize() *3/2));
    }

}
