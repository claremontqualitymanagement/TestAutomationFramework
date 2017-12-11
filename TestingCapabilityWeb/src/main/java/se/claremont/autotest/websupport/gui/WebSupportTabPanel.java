package se.claremont.autotest.websupport.gui;

import se.claremont.autotest.common.gui.guistyle.TafHeadline;
import se.claremont.autotest.common.gui.guistyle.TafLabel;
import se.claremont.autotest.common.gui.guistyle.TafPanel;
import se.claremont.autotest.common.gui.plugins.IGuiTab;

import javax.swing.*;

public class WebSupportTabPanel implements IGuiTab{
    TafPanel panel = null;
    TafHeadline headline = new TafHeadline("Web support");
    TafLabel text = new TafLabel("Web is supported through code, for now.");

    public WebSupportTabPanel(){
        panel = new TafPanel("WebSupportTabPanel");
        GroupLayout groupLayout = new GroupLayout(panel);
        panel.setLayout(groupLayout);
        groupLayout.setAutoCreateGaps(true);
        groupLayout.setAutoCreateContainerGaps(true);
        groupLayout.setHorizontalGroup(
                groupLayout.createSequentialGroup()
                        .addGroup(groupLayout.createParallelGroup()
                                .addComponent(headline)
                                .addComponent(text)
                        )
        );
        groupLayout.setVerticalGroup(
                groupLayout.createSequentialGroup()
                        .addComponent(headline)
                        .addComponent(text)
        );

    }

    @Override
    public JPanel getPanel() {
        if(panel == null) return new WebSupportTabPanel().panel;
        return panel;
    }

    @Override
    public String getName() {
        return "Web";
    }
}
