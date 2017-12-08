package se.claremont.autotest.javasupport.gui;

import se.claremont.autotest.common.gui.guistyle.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RecordingWindow extends TafFrame{

    JFrame parentWindow;
    GroupLayout groupLayout = new GroupLayout(this.getContentPane());
    TafLabel headline = new TafLabel("Rich Java GUI recording");
    TafButton recordButton = new TafButton("Record script");
    TafButton stopRecordingButton = new TafButton("Stop recording");
    TafLabel currentElementLabel = new TafLabel("Current element:");
    TafPanel currentElementPanel = new TafPanel("CurrentElementPanel");
    JTextArea currentElementText = new JTextArea(" < element description >");
    TafCloseButton closeButton = new TafCloseButton(this);

    public RecordingWindow(JFrame parentWindow){
        groupLayout.setAutoCreateContainerGaps(true);
        groupLayout.setAutoCreateGaps(true);
        this.setLayout(groupLayout);
        this.parentWindow = parentWindow;
        currentElementText.setFont(AppFont.getInstance());
        currentElementText.setName("CurrentElementText");
        currentElementPanel.add(currentElementText);

        recordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopRecordingButton.setEnabled(true);
                trackElement();
            }
        });

        stopRecordingButton.setEnabled(false);
        stopRecordingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopRecordingButton.setEnabled(false);
                stopRecording();
            }
        });

        groupLayout.setHorizontalGroup(
                groupLayout.createSequentialGroup()
                        .addGroup(groupLayout.createParallelGroup()
                                .addComponent(headline)
                                .addComponent(currentElementLabel)
                                .addComponent(currentElementPanel)
                                .addGroup(groupLayout.createSequentialGroup()
                                        .addComponent(recordButton)
                                        .addComponent(stopRecordingButton)
                                        .addComponent(closeButton)
                                )
                        )
        );
        groupLayout.setVerticalGroup(
                groupLayout.createSequentialGroup()
                        .addComponent(headline)
                        .addComponent(currentElementLabel)
                        .addComponent(currentElementPanel)
                        .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(recordButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(stopRecordingButton
                                )
                                .addComponent(closeButton)
                        )
        );

        this.pack();
        this.setSize(Toolkit.getDefaultToolkit().getScreenSize().width /3, Toolkit.getDefaultToolkit().getScreenSize().height / 2 );
        this.setVisible(true);

    }

    private void stopRecording() {
    }

    private class MousePosition implements Runnable{

        @Override
        public void run() {

        }

        public Point getMousePosition(){
            return MouseInfo.getPointerInfo().getLocation();
        }
    }

    private void trackElement() {
        stopRecordingButton.setEnabled(true);
        recordButton.setEnabled(false);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        MousePosition position = new MousePosition();
        new Thread(position).start();

        for(Window w : JavaSupportTab.applicationUnderTest.getWindows()){
            if(!w.isShowing())continue;
            try{
                Component component = identifyComponent(((JFrame)w).getContentPane(), position.getMousePosition());
                if(component != null){
                    currentElementText.setText(componentDeclarationString(component));
                }
            }catch (Exception ignored){ }
        }
        stopRecordingButton.setEnabled(false);
        recordButton.setEnabled(true);
    }

    private String componentDeclarationString(Component c){
        if(c == null) return null;
        String elementName = "Noname";
        StringBuilder sb = new StringBuilder();

        sb.append("      ").append(".byClassName(\"").append(c.getClass().getSimpleName()).append("\")").append(System.lineSeparator());
        if(c.getName() != null && c.getName().length() > 0){
            sb.append("      .andByName(\"").append(c.getName()).append("\")").append(System.lineSeparator());
            elementName = c.getName();
        }
        String text = null;
        try{
            text = (String)se.claremont.autotest.javasupport.interaction.MethodInvoker.invokeMethod(null, c, "getText", null);
        }catch (Exception ignored){}
        if(text != null && text.length() > 0){
            sb.append("      .andByExactText(\"").append(text).append("\")").append(System.lineSeparator());
        }
        return "   public static JavaGuiElement " + elementName + " = new JavaGuiElement(By" + System.lineSeparator() + sb.toString() + "   );";
    }

    private Component identifyComponent(Component w, Point mousePosition) {
        int x = w.getLocationOnScreen().x;
        int y = w.getLocationOnScreen().y;
        Component c = w.getComponentAt(mousePosition.x - x, mousePosition.y - y);
        Component returnElement = c;
        while(c != null){
            c = c.getComponentAt(mousePosition.x - c.getLocationOnScreen().x, mousePosition.y - c.getLocationOnScreen().y);
            if(c != null){
                if(c.equals(returnElement)) {
                    c = null;
                }
                else{
                    returnElement = c;
                }
            }
        }
        System.out.println(returnElement.toString());
        return returnElement;
    }
}
