package se.claremont.autotest.javasupport.gui.guirecordingwindow;

import se.claremont.autotest.common.gui.guistyle.*;
import se.claremont.autotest.javasupport.gui.JavaSupportTab;
import se.claremont.autotest.javasupport.interaction.MethodDeclarations;
import se.claremont.autotest.javasupport.interaction.MethodInvoker;
import se.claremont.autotest.javasupport.objectstructure.JavaWindow;

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

    public RecordWindow(){
        this.frame = new TafFrame("TAF - Script recorder");
        groupLayout = new GroupLayout(this.frame.getContentPane());
        groupLayout.setAutoCreateContainerGaps(true);
        groupLayout.setAutoCreateGaps(true);
        this.frame.setLayout(groupLayout);

        headline.setFont(new Font(AppFont.getInstance().getName(), AppFont.getInstance().getStyle(), AppFont.getInstance().getSize() *3/2));
        scriptScrollPane = new JScrollPane(scriptTextPane);

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
        for (Window w : JavaSupportTab.applicationUnderTest.getWindowsForSUT()) {
            if (!w.isShowing()) continue;
            removeListener(w);
        }
    }

    private void removeListener(Component component){
        MethodInvoker m = new MethodInvoker();
        System.out.println("Removing mouse listener from '" + component.getClass().getName() + " " + component.getName() + "'.");
        for(MouseListener ml : component.getMouseListeners()){
            if(ml.getClass().equals(RecordingMouseListener.class)){
                component.removeMouseListener(ml);
            }
        }
        Component[] subComponents = (Component[])m.invokeTheFirstEncounteredMethod(component, MethodDeclarations.subAllComponentsGettersMethodsInAttemptOrder);
        if(subComponents == null) return;
        for(Component c : subComponents){
            removeListener(c);
        }
    }

    private void startRecording() {
        for (Window w : JavaSupportTab.applicationUnderTest.getWindowsForSUT()) {
            if (!w.isShowing()) continue;
            JavaWindow jw = new JavaWindow(w);
            for(Object o : jw.getComponents()){
                Component c = (Component)o;
                if(o.getClass().getSimpleName().endsWith("Panel"))continue;
                ((Component) o).addMouseListener(new RecordingMouseListener(scriptTextPane));
                ((Component) o).addFocusListener(new RecordingKeyboardListener(scriptTextPane));
            }
        }
        /*
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
        */
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
