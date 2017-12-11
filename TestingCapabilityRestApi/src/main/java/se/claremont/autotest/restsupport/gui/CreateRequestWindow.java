package se.claremont.autotest.restsupport.gui;

import se.claremont.autotest.common.gui.guistyle.*;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreateRequestWindow {

    private String[] sendMethodTypes = new String[]{"GET", "PUT", "POST", "DELETE"};
    TafFrame window = new TafFrame("TAF - Create REST request");
    TafHeadline headline = new TafHeadline("Create REST request");
    TafLabel endPointUrlLabel = new TafLabel("End-point URL");
    TafTextField endPointUrlTextField = new TafTextField(" < End-point URL >");
    TafLabel sendMethodLabel = new TafLabel("Send method");
    JComboBox sendMethodCombobox = new JComboBox(sendMethodTypes);
    TafPanel sendDataPanel = new TafPanel("SendDataPanel");
    TafLabel sendDataLabel = new TafLabel("Data");
    TafTextArea sendDataTextArea = new TafTextArea("SendDataTextArea");
    JScrollPane sendDataScrollPane = new JScrollPane(sendDataTextArea);
    TafLabel mediaContentLabel = new TafLabel("Media content type");
    String[] presetMediaTypeSuggestions = new String[]{"text/html"};
    JComboBox mediaContentComboBox = new JComboBox(presetMediaTypeSuggestions);
    TafButton addHeaderValueButton = new TafButton("Add http header value");
    TafButton tryRequestButton = new TafButton("Try request");
    TafButton saveRequestButton = new TafButton("Save");
    TafCloseButton closeButton = new TafCloseButton(window);

    public CreateRequestWindow(){
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        sendMethodCombobox.setEditable(false);
        sendMethodCombobox.setFont(AppFont.getInstance());
        sendMethodCombobox.setForeground(TafGuiColor.textColor);
        sendMethodCombobox.setBackground(TafGuiColor.backgroundColor);
        sendMethodCombobox.getEditor().getEditorComponent().setBackground(TafGuiColor.backgroundColor);
        sendMethodCombobox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(sendMethodCombobox.getItemAt(sendMethodCombobox.getSelectedIndex()).toString().equals("GET")){
                    sendDataTextArea.setEnabled(false);
                } else {
                    sendDataTextArea.setEnabled(true);
                }
            }
        });
        if(sendMethodCombobox.getItemAt(sendMethodCombobox.getSelectedIndex()).toString().equals("GET")){
            sendDataTextArea.setEnabled(false);
        }

        mediaContentComboBox.setEditable(true);
        mediaContentComboBox.setSelectedIndex(0);
        mediaContentComboBox.setFont(AppFont.getInstance());
        mediaContentComboBox.setForeground(TafGuiColor.textColor);
        mediaContentComboBox.setBackground(TafGuiColor.backgroundColor);

        sendDataPanel.setSize(Toolkit.getDefaultToolkit().getScreenSize().width/3, Toolkit.getDefaultToolkit().getScreenSize().height /3);
        sendDataPanel.add(sendDataLabel);
        sendDataPanel.add(sendDataScrollPane);

        GroupLayout groupLayout = new GroupLayout(window.getContentPane());
        window.getContentPane().setLayout(groupLayout);
        groupLayout.setHorizontalGroup(
                groupLayout.createSequentialGroup()
                        .addGroup(groupLayout.createParallelGroup()
                                .addComponent(headline)
                                .addGroup(groupLayout.createSequentialGroup()
                                        .addComponent(sendMethodLabel)
                                        .addComponent(sendMethodCombobox)
                                )
                                .addGroup(groupLayout.createSequentialGroup()
                                    .addComponent(endPointUrlLabel)
                                    .addComponent(endPointUrlTextField)
                                )
                                .addGroup(groupLayout.createSequentialGroup()
                                        .addComponent(mediaContentLabel)
                                        .addComponent(mediaContentComboBox)
                                )
                                .addComponent(sendDataPanel)
                                .addGroup(groupLayout.createSequentialGroup()
                                        .addComponent(tryRequestButton)
                                        .addComponent(addHeaderValueButton)
                                        .addComponent(saveRequestButton)
                                        .addComponent(closeButton)
                                )
                        )
        );
        groupLayout.setVerticalGroup(
                groupLayout.createSequentialGroup()
                        .addComponent(headline)
                        .addGroup(groupLayout.createParallelGroup()
                                .addComponent(sendMethodLabel)
                                .addComponent(sendMethodCombobox)
                        )
                        .addGroup(groupLayout.createParallelGroup()
                                .addComponent(endPointUrlLabel)
                                .addComponent(endPointUrlTextField)
                        )
                        .addGroup(groupLayout.createParallelGroup()
                                .addComponent(mediaContentLabel)
                                .addComponent(mediaContentComboBox)
                        )
                        .addComponent(sendDataPanel)
                        .addGroup(groupLayout.createParallelGroup()
                                .addComponent(tryRequestButton)
                                .addComponent(addHeaderValueButton)
                                .addComponent(saveRequestButton)
                                .addComponent(closeButton)
                        )
        );
        groupLayout.setAutoCreateContainerGaps(true);
        groupLayout.setAutoCreateGaps(true);
        window.pack();
        window.setVisible(true);
    }
}
