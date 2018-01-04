package se.claremont.autotest.eyeautomatesupport.gui;

import se.claremont.autotest.common.gui.Gui;
import se.claremont.autotest.common.gui.guistyle.AppFont;
import se.claremont.autotest.common.gui.guistyle.TafHeadline;
import se.claremont.autotest.common.gui.guistyle.TafLabel;
import se.claremont.autotest.common.gui.guistyle.TafPanel;
import se.claremont.autotest.common.gui.plugins.IGuiTab;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;

public class SmartImageRecognitionSupportTabPanel implements IGuiTab {
    TafPanel panel = null;
    TafHeadline headline = new TafHeadline("Smart image recognition support");
    TafLabel textParagraph1 = new TafLabel("Smart image recognition utilizes the libraries from EyeAutomate to identify and " +
            "interact with GUI elements based on their appearance.");
    TafLabel textParagraph2 = new TafLabel("The algorithms compensate for resolution, " +
            "color depth and normal font deviations.");
    TafLabel textParagraph3 = new TafLabel("Smart image recognition is supported through code, or from " +
            "EyeAutomate capturemanagers, for now.");

    public SmartImageRecognitionSupportTabPanel() {
        panel = new TafPanel("SmartImageRecognitionSupportTabPanel");

        GroupLayout groupLayout = new GroupLayout(panel);
        panel.setLayout(groupLayout);

        JTextPane textPane = new JTextPane();
        textPane.setName("AboutTextPane");
        textPane.setContentType("text/html");
        textPane.setForeground(Gui.colorTheme.textColor);
        textPane.setBackground(Gui.colorTheme.backgroundColor);
        textPane.setFont(AppFont.getInstance());
        textPane.setEditable(false);
        textPane.addHyperlinkListener(new HyperlinkListener() {
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    if (Desktop.isDesktopSupported()) {
                        try {
                            Desktop.getDesktop().browse(e.getURL().toURI());
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        } catch (URISyntaxException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }
        });
        textPane.setText(getBriefText());


        groupLayout.setAutoCreateGaps(true);
        groupLayout.setAutoCreateContainerGaps(true);
        groupLayout.setHorizontalGroup(
                groupLayout.createSequentialGroup()
                        .addGroup(groupLayout.createParallelGroup()
                                .addComponent(headline)
                                .addComponent(textParagraph1)
                                .addComponent(textParagraph2)
                                .addComponent(textParagraph3)
                                .addComponent(textPane)
                        )
        );
        groupLayout.setVerticalGroup(
                groupLayout.createSequentialGroup()
                        .addComponent(headline)
                        .addComponent(textParagraph1)
                        .addComponent(textParagraph2)
                        .addComponent(textParagraph3)
                        .addComponent(textPane)
        );

    }

    private String getBriefText() {
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append("<body style=\"color: grey; font-size: " + AppFont.getInstance().getSize() + "; \">");
        sb.append("<p>More info about EyeAutomate tool can be found at the product site for <a href=\"http://eyeautomate.com/");
        sb.append("\" target=\"_blank\">EyeAutomate</a>.</p>");
        sb.append("</body>");
        sb.append("</html>");
        return sb.toString();
    }

    @Override
    public JPanel getPanel() {
        if (panel == null) return new SmartImageRecognitionSupportTabPanel().panel;
        return panel;
    }

    @Override
    public String getName() {
        return "Smart image recognition";
    }
}
