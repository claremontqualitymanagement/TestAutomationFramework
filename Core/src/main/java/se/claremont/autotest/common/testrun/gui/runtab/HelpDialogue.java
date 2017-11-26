package se.claremont.autotest.common.testrun.gui.runtab;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HelpDialogue {

    String helpText = "This is the current version of the help text. ";

    public HelpDialogue(Font appFont){
        JFrame helpFrame = new JFrame();
        helpFrame.setName("HelpDialogueWindow");
        helpFrame.setTitle("TAF - Help");
        Container pane = helpFrame.getContentPane();
        pane.setName("HelpDialogueContentPane");
        GroupLayout groupLayout = new GroupLayout(pane);
        pane.setLayout(groupLayout);
        JTextArea textArea = new JTextArea();
        textArea.setName("HelpDialogueTextArea");
        textArea.setEditable(false);
        textArea.setFont(appFont);
        textArea.setText(helpText);
        textArea.setLineWrap(true);
        pane.add(textArea);
        JButton closeButton = new JButton("Close");
        closeButton.setFont(appFont);
        closeButton.setName("HelpDialogueCloseButton");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                helpFrame.dispose();
            }
        });
        pane.add(closeButton);

        groupLayout.setHorizontalGroup(
                groupLayout.createSequentialGroup()
                        .addGroup(groupLayout.createParallelGroup()
                                .addComponent(textArea)
                                .addComponent(closeButton)
                        )
        );
        groupLayout.setVerticalGroup(
                groupLayout.createSequentialGroup()
                        .addComponent(textArea)
                        .addComponent(closeButton)
        );

        helpFrame.pack();
        helpFrame.setSize(new Dimension(1 * Toolkit.getDefaultToolkit().getScreenSize().width/3, 2 * Toolkit.getDefaultToolkit().getScreenSize().height /3));
        //helpFrame.pack();
        helpFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        helpFrame.setVisible(true);

    }
}
