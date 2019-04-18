package se.claremont.taf.core.gui.runtab;

import se.claremont.taf.core.gui.guistyle.AppFont;
import se.claremont.taf.core.gui.guistyle.TafCloseButton;
import se.claremont.taf.core.gui.guistyle.TafFrame;
import se.claremont.taf.core.gui.guistyle.TafHtmlTextPane;

import javax.swing.*;
import java.awt.*;

public class HelpDialogue {

    String htmlHead = "";
    String htmlHelpText = "This is the current version of the help text. ";

    public HelpDialogue(){

        TafFrame helpFrame = new TafFrame("TAF - Help");
        helpFrame.setName("HelpDialogueWindow");
        helpFrame.setTitle("TAF - Help");

        Container pane = helpFrame.getContentPane();
        pane.setName("HelpDialogueContentPane");

        GroupLayout groupLayout = new GroupLayout(pane);
        pane.setLayout(groupLayout);

        TafHtmlTextPane textPane = new TafHtmlTextPane("HelpDialogueTextArea");
        textPane.setEditable(false);
        textPane.setText(createHtmlPage());
        pane.add(textPane);

        TafCloseButton closeButton = new TafCloseButton(helpFrame);
        pane.add(closeButton);

        groupLayout.setAutoCreateGaps(true);
        groupLayout.setAutoCreateContainerGaps(true);

        groupLayout.setHorizontalGroup(
                groupLayout.createSequentialGroup()
                        .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                .addComponent(textPane)
                                .addComponent(closeButton)
                        )
        );
        groupLayout.setVerticalGroup(
                groupLayout.createSequentialGroup()
                        .addComponent(textPane)
                        .addComponent(closeButton)
        );

        helpFrame.pack();
        helpFrame.getRootPane().setDefaultButton(closeButton);
        closeButton.setMnemonic('c');
        closeButton.requestFocus();
        helpFrame.setSize(new Dimension(1 * Toolkit.getDefaultToolkit().getScreenSize().width/3, 2 * Toolkit.getDefaultToolkit().getScreenSize().height /3));
        helpFrame.setVisible(true);

    }

    private String createHtmlPage() {
        return "<html>" + System.lineSeparator() +
                "   <head>" + System.lineSeparator() +
                "     <style>" + System.lineSeparator() +
                "        body   { color: blue; font-size: " + AppFont.getInstance().getSize() + "; }" + System.lineSeparator() +
                "     </style>" + System.lineSeparator() +
                "   </head>" + System.lineSeparator() +
                "   <body>" + System.lineSeparator() +
                "      <p>" + System.lineSeparator() +
                htmlHelpText +
                "      <p>" + System.lineSeparator() +
                "   </body>" + System.lineSeparator() +
                "</html>"  + System.lineSeparator();
    }
}
