package se.claremont.autotest.javasupport.gui.guirecordingwindow;

import se.claremont.autotest.common.gui.guistyle.*;
import se.claremont.autotest.javasupport.gui.JavaSupportTab;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RecordWindow{
    TafFrame frame;
    GroupLayout groupLayout;
    TafButton closeButton;
    TafLabel headline = new TafLabel("Rich Java GUI recording");
    TafLabel scriptLabel = new TafLabel("Script");
    TafHtmlTextPane scriptTextPane = new TafHtmlTextPane("ScriptTextPane");
    JScrollPane scriptScrollPane;
    MousePosition position = new MousePosition();
    Thread positionTracker = new Thread(position);
    boolean performRecording = true;

    public RecordWindow(){
        this.frame = new TafFrame("TAF - Script recorder");
        groupLayout = new GroupLayout(this.frame.getContentPane());
        groupLayout.setAutoCreateContainerGaps(true);
        groupLayout.setAutoCreateGaps(true);
        this.frame.setLayout(groupLayout);

        headline.setFont(new Font(AppFont.getInstance().getName(), AppFont.getInstance().getStyle(), AppFont.getInstance().getSize() *3/2));
        scriptScrollPane = new JScrollPane(scriptTextPane);
        closeButton = new TafButton("Close");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performRecording = false;
                frame.dispose();
            }
        });

        groupLayout.setHorizontalGroup(
                groupLayout.createSequentialGroup()
                        .addGroup(groupLayout.createParallelGroup()
                                .addComponent(headline)
                                .addComponent(scriptLabel)
                                .addComponent(scriptScrollPane)
                                .addComponent(closeButton)
                        )
        );
        groupLayout.setVerticalGroup(
                groupLayout.createSequentialGroup()
                        .addComponent(headline)
                        .addComponent(scriptLabel)
                        .addComponent(scriptScrollPane)
                        .addComponent(closeButton)
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

            }

            @Override
            public void windowClosed(WindowEvent e) {
                positionTracker.destroy();
                performRecording = false;
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

    private class MousePosition implements Runnable {
        PointerInfo pointerInfo;

        @Override
        public void run() {
            pointerInfo = MouseInfo.getPointerInfo();
        }

        public Point getMousePosition() {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(pointerInfo == null)return null;
            return pointerInfo.getLocation();
        }

        public void stopTrack(){
            pointerInfo = null;
        }
    }


    private void startRecording() {
        positionTracker.start();
        while (performRecording) {
            for (Window w : JavaSupportTab.applicationUnderTest.getWindowsForSUT()) {
                if (!w.isShowing()) continue;
                try {
                    Component component = identifyComponent(((JFrame) w).getContentPane(), position.getMousePosition());
                    if (component == null){
                        Thread.sleep(10);
                        continue;
                    }
                    if(!TafMouseListener.isApplied(component))
                        component.addMouseListener(new TafMouseListener(scriptTextPane));
                } catch (Exception ignored) {
                }

            }
        }
    }

    private static boolean componentsAreTheSame(Component component1, Component component2){
        if(component1.toString().equals(component2.toString()))return true;
        if(component1.getName() == null && component2.getName() != null)return false;
        if(component1.getName() != null && component2.getName() == null)return false;
        if(component1.getName() != null && component2.getName() != null && !component1.getName().equals(component2)) return false;
        if(!component1.getClass().getName().equals(component2.getClass().getName())) return false;
        return true;
    }

    private Component identifyComponent(Component w, Point mousePosition) {
        int x = w.getLocationOnScreen().x;
        int y = w.getLocationOnScreen().y;
        Component c = w.getComponentAt(mousePosition.x - x, mousePosition.y - y);
        Component returnElement = c;
        while (c != null) {
            c = c.getComponentAt(mousePosition.x - c.getLocationOnScreen().x, mousePosition.y - c.getLocationOnScreen().y);
            if (c != null) {
                if (c.equals(returnElement)) {
                    c = null;
                } else {
                    returnElement = c;
                }
            }
        }
        System.out.println(returnElement.toString());
        return returnElement;
    }
}
