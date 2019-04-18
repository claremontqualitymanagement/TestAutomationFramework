package se.claremont.taf.javasupport.gui.guirecordingwindow;

import se.claremont.taf.core.gui.guistyle.*;
import se.claremont.taf.javasupport.gui.guirecordingwindow.listeners.RecordingKeyBoardListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

public class RecordWindow{
    TafFrame frame;
    GroupLayout groupLayout;
    TafButton recordingOptionsButton = new TafButton("Options");
    TafButton closeButton;
    TafLabel headline = new TafLabel("Rich Java GUI recording");
    TafLabel scriptLabel = new TafLabel("Script");
    TafHtmlTextPane scriptTextPane = new TafHtmlTextPane("ScriptTextPane");
    public static java.util.List<RecordingKeyBoardListener.KeyPress> keysPressedSinceLastWriteCommand = new ArrayList<>();
    public static Component activeComponent;
    JScrollPane scriptScrollPane;

    public RecordWindow(){
        this.frame = new TafFrame("TAF - Script recorder");
        groupLayout = new GroupLayout(this.frame.getContentPane());
        groupLayout.setAutoCreateContainerGaps(true);
        groupLayout.setAutoCreateGaps(true);
        this.frame.setLayout(groupLayout);

        headline.setFont(new Font(AppFont.getInstance().getName(), AppFont.getInstance().getStyle(), AppFont.getInstance().getSize() *3/2));
        scriptScrollPane = new JScrollPane(scriptTextPane);

        recordingOptionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RecordingOptionsWindow recordingOptionsWindow = new RecordingOptionsWindow(frame);
                recordingOptionsWindow.dialog.addWindowListener(new WindowListener() {
                    @Override
                    public void windowOpened(WindowEvent e) {

                    }

                    @Override
                    public void windowClosing(WindowEvent e) {
                        stopRecording();
                        startRecording();
                    }

                    @Override
                    public void windowClosed(WindowEvent e) {

                    }

                    @Override
                    public void windowIconified(WindowEvent e) {

                    }

                    @Override
                    public void windowDeiconified(WindowEvent e) {

                    }

                    @Override
                    public void windowActivated(WindowEvent e) {

                    }

                    @Override
                    public void windowDeactivated(WindowEvent e) {

                    }
                });
            }
        });


        closeButton = new TafButton("Close");
        closeButton.setMnemonic('c');
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        groupLayout.setHorizontalGroup(
                groupLayout.createSequentialGroup()
                        .addGroup(groupLayout.createParallelGroup()
                                .addComponent(headline)
                                .addComponent(scriptLabel)
                                .addComponent(scriptScrollPane)
                                .addGroup(groupLayout.createSequentialGroup()
                                    .addComponent(recordingOptionsButton)
                                    .addComponent(closeButton)
                                )
                        )
        );
        groupLayout.setVerticalGroup(
                groupLayout.createSequentialGroup()
                        .addComponent(headline)
                        .addComponent(scriptLabel)
                        .addComponent(scriptScrollPane)
                        .addGroup(groupLayout.createParallelGroup()
                            .addComponent(recordingOptionsButton)
                            .addComponent(closeButton)
                        )
        );

        this.frame.pack();
        this.frame.setSize(Toolkit.getDefaultToolkit().getScreenSize().width / 3, Toolkit.getDefaultToolkit().getScreenSize().height / 2);
        this.frame.setVisible(true);
        this.frame.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
                startRecording();
            }

            @Override
            public void windowClosing(WindowEvent e) {
                stopRecording();
            }

            @Override
            public void windowClosed(WindowEvent e) {
            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {
            }
        });
    }

    private void stopRecording() {
        RecordingListenersManager.stopRecording();
    }


    private void startRecording() {
        RecordingListenersManager.setScriptArea(scriptTextPane);
        RecordingListenersManager.startRecording();
    }

    private static boolean componentsAreTheSame(Component component1, Component component2){
        if(component1.toString().equals(component2.toString()))return true;
        if(component1.getName() == null && component2.getName() != null)return false;
        if(component1.getName() != null && component2.getName() == null)return false;
        if(component1.getName() != null && component2.getName() != null && !component1.getName().equals(component2)) return false;
        if(!component1.getClass().getName().equals(component2.getClass().getName())) return false;
        return true;
    }

}
