package se.claremont.autotest.restsupport.gui;

import se.claremont.autotest.common.gui.guistyle.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreateRequestWindow {

    private String[] sendMethodTypes = new String[]{"GET", "PUT", "POST", "DELETE"};
    TafDialog window;
    TafHeadline headline = new TafHeadline("Create REST request");
    TafLabel endPointUrlLabel = new TafLabel("End-point URL");
    TafTextField endPointUrlTextField = new TafTextField(" < End-point URL >");
    TafLabel sendMethodLabel = new TafLabel("Send method");
    TafComboBox sendMethodCombobox = new TafComboBox("SendMethodComboBox", sendMethodTypes);
    TafPanel sendDataPanel = new TafPanel("SendDataPanel");
    TafLabel sendDataLabel = new TafLabel("Data");
    TafTextArea sendDataTextArea = new TafTextArea("SendDataTextArea");
    JScrollPane sendDataScrollPane = new JScrollPane(sendDataTextArea);
    TafLabel requestMediaContentLabel = new TafLabel("Request content type");
    String[] presetMediaTypeSuggestions = new String[]{"text/html", "application/json", "application/xml"};
    TafComboBox requestMediaContentComboBox = new TafComboBox("RequestMediaContentCombobox", presetMediaTypeSuggestions);
    TafLabel responseAcceptMediaContentLabel = new TafLabel("Response accept content type");
    TafComboBox responseMediaContentComboBox = new TafComboBox("ResponseAcceptMediaContentComboBox", presetMediaTypeSuggestions);
    TafButton addHeaderValueButton = new TafButton("Add http header value");
    TafButton tryRequestButton = new TafButton("Try request");
    TafButton saveRequestButton = new TafButton("Save");
    TafCloseButton closeButton;

    public CreateRequestWindow(TafFrame applicationFrame){
        window = new TafDialog(applicationFrame, "TAF - Create REST request", true);
        closeButton = new TafCloseButton(window);
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        sendMethodCombobox.setEditable(true);
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

        String requestContentTypeHelp = "Tells the server how to interprete the request we are sending.";
        requestMediaContentComboBox.setEditable(true);
        requestMediaContentComboBox.setSelectedIndex(1);
        requestMediaContentComboBox.setToolTipText(requestContentTypeHelp);
        requestMediaContentLabel.setToolTipText(requestContentTypeHelp);

        String responseAcceptHelp = "Tells the server in what data format we expect the response to be in.";
        responseMediaContentComboBox.setEditable(true);
        responseMediaContentComboBox.setSelectedIndex(0);
        responseMediaContentComboBox.setToolTipText(responseAcceptHelp);
        responseAcceptMediaContentLabel.setToolTipText(responseAcceptHelp);

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
                                        .addComponent(requestMediaContentLabel)
                                        .addComponent(requestMediaContentComboBox)
                                )
                                .addGroup(groupLayout.createSequentialGroup()
                                        .addComponent(responseAcceptMediaContentLabel)
                                        .addComponent(responseMediaContentComboBox)
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
                                .addComponent(requestMediaContentLabel)
                                .addComponent(requestMediaContentComboBox)
                        )
                        .addGroup(groupLayout.createParallelGroup()
                                .addComponent(responseAcceptMediaContentLabel)
                                .addComponent(responseMediaContentComboBox)
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
