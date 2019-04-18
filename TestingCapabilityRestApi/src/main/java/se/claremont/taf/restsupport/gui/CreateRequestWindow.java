package se.claremont.taf.restsupport.gui;

import se.claremont.taf.core.gui.Gui;
import se.claremont.taf.core.gui.guistyle.*;
import se.claremont.taf.core.testcase.TestCase;
import se.claremont.taf.restsupport.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class CreateRequestWindow {

    private String[] sendMethodTypes = new String[]{"GET", "PUT", "POST", "DELETE"};
    RestRequest restRequest;

    private java.util.List<HeaderPair> headerValues = new ArrayList<>();

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

    public CreateRequestWindow(TafFrame applicationFrame) {
        window = new TafDialog(applicationFrame, "TAF - Create REST request", true);
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        sendMethodCombobox.setEditable(true);
        sendMethodCombobox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (sendMethodCombobox.getItemAt(sendMethodCombobox.getSelectedIndex()).toString().equals("GET")) {
                    sendDataTextArea.setEnabled(false);
                } else {
                    sendDataTextArea.setEnabled(true);
                }
            }
        });

        if (sendMethodCombobox.getItemAt(sendMethodCombobox.getSelectedIndex()).toString().equals("GET")) {
            sendDataTextArea.setEnabled(false);
        }

        endPointUrlTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if(endPointUrlTextField.isChangedFromDefault() && endPointUrlTextField.getText().length() > 0){
                    saveRequestButton.setEnabled(true);
                    tryRequestButton.setEnabled(true);
                } else {
                    saveRequestButton.setEnabled(false);
                    tryRequestButton.setEnabled(false);
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if(endPointUrlTextField.isChangedFromDefault() && endPointUrlTextField.getText().length() > 0){
                    saveRequestButton.setEnabled(true);
                    tryRequestButton.setEnabled(true);
                } else {
                    saveRequestButton.setEnabled(false);
                    tryRequestButton.setEnabled(false);
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                if(endPointUrlTextField.isChangedFromDefault() && endPointUrlTextField.getText().length() > 0){
                    saveRequestButton.setEnabled(true);
                    tryRequestButton.setEnabled(true);
                } else {
                    saveRequestButton.setEnabled(false);
                    tryRequestButton.setEnabled(false);
                }
            }
        });


        sendDataTextArea.setSize(Toolkit.getDefaultToolkit().getScreenSize().width / 3, Toolkit.getDefaultToolkit().getScreenSize().height / 3);
        sendDataTextArea.setLineWrap(true);
        sendDataTextArea.setWrapStyleWord(true);
        sendDataTextArea.setBackground(new TafTextField("dummy").getBackground());


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
        sendDataPanel.add(sendDataScrollPane);

        tryRequestButton.setEnabled(false);
        tryRequestButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                makeRequestAndDisplayResult();
            }
        });

        addHeaderValueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TafDialog frame = new TafDialog(window, "TAF - Add HTTP header value", true);
                frame.getContentPane().setLayout(new BorderLayout());

                TafHeadline headline = new TafHeadline("Add HTTP header value");
                TafPanel propertiesPanel = new TafPanel("HeaderPanel");
                propertiesPanel.setLayout(new GridLayout(3, 2));
                TafTextField propertyNameText = new TafTextField(" < Header entry name > ");

                TafLabel propertyNameLabel = new TafLabel("Name");
                propertyNameLabel.setLabelFor(propertyNameText);
                propertiesPanel.add(propertyNameLabel);
                propertiesPanel.add(propertyNameText);
                TafButton saveButton = new TafButton("Save");

                TafLabel propertyValueLabel = new TafLabel("Value");
                TafTextField propertyValueText = new TafTextField(" < Header entry value > ");
                propertyValueLabel.setLabelFor(propertyValueText);
                propertiesPanel.add(propertyValueLabel);
                propertiesPanel.add(propertyValueText);
                propertyValueText.getDocument().addDocumentListener(new DocumentListener() {
                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        if (propertyValueText.isChangedFromDefault() && propertyValueText.getText().length() > 0 && propertyNameText.getText().length() > 0 && propertyNameText.isChangedFromDefault()) {
                            saveButton.setEnabled(true);
                        } else {
                            saveButton.setEnabled(false);
                        }
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        if (propertyValueText.isChangedFromDefault() && propertyValueText.getText().length() > 0 && propertyNameText.getText().length() > 0 && propertyNameText.isChangedFromDefault()) {
                            saveButton.setEnabled(true);
                        } else {
                            saveButton.setEnabled(false);
                        }
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {
                        if (propertyValueText.isChangedFromDefault() && propertyValueText.getText().length() > 0 && propertyNameText.getText().length() > 0 && propertyNameText.isChangedFromDefault()) {
                            saveButton.setEnabled(true);
                        } else {
                            saveButton.setEnabled(false);
                        }
                    }
                });
                propertyNameText.getDocument().addDocumentListener(new DocumentListener() {
                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        if (propertyValueText.isChangedFromDefault() && propertyValueText.getText().length() > 0 && propertyNameText.getText().length() > 0 && propertyNameText.isChangedFromDefault()) {
                            saveButton.setEnabled(true);
                        } else {
                            saveButton.setEnabled(false);
                        }
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        if (propertyValueText.isChangedFromDefault() && propertyValueText.getText().length() > 0 && propertyNameText.getText().length() > 0 && propertyNameText.isChangedFromDefault()) {
                            saveButton.setEnabled(true);
                        } else {
                            saveButton.setEnabled(false);
                        }
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {
                        if (propertyValueText.isChangedFromDefault() && propertyValueText.getText().length() > 0 && propertyNameText.getText().length() > 0 && propertyNameText.isChangedFromDefault()) {
                            saveButton.setEnabled(true);
                        } else {
                            saveButton.setEnabled(false);
                        }
                    }
                });

                TafCloseButton cancelButton = new TafCloseButton(frame);
                cancelButton.setText("Cancel");
                propertiesPanel.add(cancelButton);

                saveButton.setEnabled(false);
                propertiesPanel.add(saveButton);
                saveButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        headerValues.add(new HeaderPair(propertyNameText.getText(), propertyValueText.getText()));
                        frame.dispose();
                    }
                });

                frame.getContentPane().add(headline, BorderLayout.PAGE_START);
                frame.getContentPane().add(propertiesPanel, BorderLayout.PAGE_END);

                frame.pack();
                frame.setVisible(true);

            }
        });

        closeButton = new TafCloseButton(window);
        closeButton.setText("Cancel");

        saveRequestButton.setEnabled(false);
        saveRequestButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String description;
                if(sendDataTextArea.getText() != null && sendDataTextArea.getText().length() > 0){
                    description = sendMethodCombobox.getItemAt(sendMethodCombobox.getSelectedIndex()).toString() + " request to " +
                            endPointUrlTextField.getText() + " with data: '" + sendDataTextArea.getText() + "'.";
                } else {
                    description = sendMethodCombobox.getItemAt(sendMethodCombobox.getSelectedIndex()).toString() + " request to " +
                            endPointUrlTextField.getText() + ".";
                }
                RestTestStep testStep = new RestTestStep(sendMethodCombobox.getItemAt(sendMethodCombobox.getSelectedIndex()).toString() + " to " + endPointUrlTextField.getText(),
                        description);
                for(HeaderPair headerPair : headerValues){
                    testStep.addHeaderValue(headerPair.name, headerPair.value);
                }
                testStep.setMediaType(requestMediaContentComboBox.getItemAt(requestMediaContentComboBox.getSelectedIndex()).toString());
                testStep.actionName = sendMethodCombobox.getItemAt(sendMethodCombobox.getSelectedIndex()).toString();
                testStep.elementName = endPointUrlTextField.getText();
                testStep.data = sendDataTextArea.getText();
                Gui.addTestStepToListOfAvailableTestSteps(testStep);
                window.dispose();
            }
        });

        GroupLayout groupLayout = new GroupLayout(window.getContentPane());
        window.getContentPane().setLayout(groupLayout);


        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup()
                        .addComponent(headline)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(sendMethodLabel)
                                        .addComponent(endPointUrlLabel)
                                        .addComponent(requestMediaContentLabel)
                                        .addComponent(responseAcceptMediaContentLabel)
                                        .addComponent(sendDataLabel)
                                )
                                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(sendMethodCombobox)
                                        .addComponent(endPointUrlTextField)
                                        .addComponent(requestMediaContentComboBox)
                                        .addComponent(responseMediaContentComboBox)
                                        .addComponent(sendDataPanel)
                                ))
                        .addGroup(groupLayout.createSequentialGroup()
                                .addComponent(tryRequestButton)
                                .addComponent(addHeaderValueButton)
                                .addComponent(saveRequestButton)
                                .addComponent(closeButton)
                        )
        );
        groupLayout.setVerticalGroup(
                groupLayout.createSequentialGroup()
                        .addComponent(headline)
                        .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(sendMethodLabel)
                                .addComponent(sendMethodCombobox)
                        )
                        .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(endPointUrlLabel)
                                .addComponent(endPointUrlTextField)
                        )
                        .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(requestMediaContentLabel)
                                .addComponent(requestMediaContentComboBox)
                        )
                        .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(responseAcceptMediaContentLabel)
                                .addComponent(responseMediaContentComboBox)
                        )
                        .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(sendDataLabel)
                                .addComponent(sendDataPanel)
                        )
                        .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(tryRequestButton)
                                .addComponent(addHeaderValueButton)
                                .addComponent(saveRequestButton)
                                .addComponent(closeButton)
                        )
        );
        /*
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
        */
        groupLayout.setAutoCreateContainerGaps(true);
        groupLayout.setAutoCreateGaps(true);
        window.pack();
        window.setVisible(true);
    }

    private void makeRequestAndDisplayResult() {
        TestCase testCase = new TestCase("REST request attempt");
        RestSupport restSupport = new RestSupport(testCase);
        String sendMethod = sendMethodCombobox.getItemAt(sendMethodCombobox.getSelectedIndex()).toString().toUpperCase();
        String mediaType = requestMediaContentComboBox.getItemAt(requestMediaContentComboBox.getSelectedIndex()).toString();
        String endpointUrl;
        if (endPointUrlTextField.isChangedFromDefault()) {
            endpointUrl = endPointUrlTextField.getText();
        } else {
            endpointUrl = "";
        }
        RestResponse response = null;
        String responseError = null;

        try {
            switch (sendMethod) {
                case "PUT":
                    restRequest = new RestPutRequest(endpointUrl, mediaType, sendDataTextArea.getText(), testCase);
                    for(HeaderPair headerPair : headerValues){
                        restRequest.addHeaderValue(headerPair.name, headerPair.value);
                    }
                    response = restRequest.execute(restSupport.client);
                    break;
                case "GET":
                    restRequest = new RestGetRequest(endpointUrl, testCase);
                    for(HeaderPair headerPair : headerValues){
                        restRequest.addHeaderValue(headerPair.name, headerPair.value);
                    }
                    response = restRequest.execute(restSupport.client);
                    break;
                case "POST":
                    restRequest = new RestPostRequest(endpointUrl, mediaType, sendDataTextArea.getText(), testCase);
                    for(HeaderPair headerPair : headerValues){
                        restRequest.addHeaderValue(headerPair.name, headerPair.value);
                    }
                    response = restRequest.execute(restSupport.client);
                    break;
                case "DELETE":
                    restRequest = new RestDeleteRequest(endpointUrl, testCase);
                    for(HeaderPair headerPair : headerValues){
                        restRequest.addHeaderValue(headerPair.name, headerPair.value);
                    }
                    response = restRequest.execute(restSupport.client);
                    break;
                default:
                    break;
            }
        } catch (Exception ex) {
            responseError = ex.getMessage();
        }

        TafDialog responseWindow = new TafDialog(window, "TAF - REST Response", true);
        String responseText = responseError;
        if (response != null)
            responseText = "Response code: " + response.responseCode + " (" + response.message + ")";
        TafLabel statusText = new TafLabel(responseText);
        statusText.setName("ResponseStatusText");
        TafLabel dataLabel = new TafLabel("Data:");
        TafTextArea responseTextPane = new TafTextArea("ResponseTextPanel");
        if(response != null && response.body != null)
            responseTextPane.setText(response.body.toString());
        responseTextPane.setWrapStyleWord(false);
        responseTextPane.setLineWrap(false);
        responseTextPane.setBackground(new TafTextField("dummy").getBackground());

        JScrollPane scrollPane = new JScrollPane(responseTextPane);

        TafHeadline responseHeadline = new TafHeadline("REST Response");
        TafCloseButton responseWindowCloseButton = new TafCloseButton(responseWindow);

        if(response == null || response.body == null){
            dataLabel.setVisible(false);
            scrollPane.setVisible(false);
        }

        GroupLayout groupLayout = new GroupLayout(responseWindow.getContentPane());
        responseWindow.setLayout(groupLayout);
        groupLayout.setAutoCreateContainerGaps(true);
        groupLayout.setAutoCreateGaps(true);


        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup()
                        .addComponent(responseHeadline)
                        .addComponent(statusText)
                        .addComponent(dataLabel)
                        .addComponent(scrollPane)
                        .addComponent(responseWindowCloseButton, GroupLayout.Alignment.TRAILING)
        );
        groupLayout.setVerticalGroup(
                groupLayout.createSequentialGroup()
                        .addComponent(responseHeadline)
                        .addComponent(statusText)
                        .addComponent(dataLabel)
                        .addComponent(scrollPane)
                        .addComponent(responseWindowCloseButton)
        );


        responseWindow.pack();
        if(responseWindow.getSize().height > Toolkit.getDefaultToolkit().getScreenSize().height)
            responseWindow.setSize(responseWindow.getWidth(), Toolkit.getDefaultToolkit().getScreenSize().height * 4/5);
        if(responseWindow.getSize().width > Toolkit.getDefaultToolkit().getScreenSize().width)
            responseWindow.setSize(Toolkit.getDefaultToolkit().getScreenSize().width * 4/5, responseWindow.getHeight());
        responseWindow.setVisible(true);
    }

    class HeaderPair {
        String name;
        String value;

        HeaderPair(String name, String value) {
            this.name = name;
            this.value = value;
        }
    }
}
