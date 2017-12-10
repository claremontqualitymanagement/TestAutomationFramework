package se.claremont.autotest.javasupport.gui;

import se.claremont.autotest.common.gui.guistyle.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Set;

public class GuiSpyingWindow {

    TafFrame frame;
    GroupLayout groupLayout;
    TafLabel headline = new TafLabel("GUI Spy");
    TafLabel currentElementLabel = new TafLabel("Current element:");
    TafPanel currentElementPanel = new TafPanel("CurrentElementPanel");
    JTextPane currentElementText = new JTextPane();
    JScrollPane programaticDescriptionScrollPanel;
    TafPanel programaticDescriptionPanel = new TafPanel("ProgramaticDescriptionPanel");
    TafButton closeButton = new TafButton("Close");
    MousePosition position = new MousePosition();
    Thread positionTracker = new Thread(position);


    public GuiSpyingWindow() {
        frame = new TafFrame();
        groupLayout = new GroupLayout(this.frame.getContentPane());
        groupLayout.setAutoCreateContainerGaps(true);
        groupLayout.setAutoCreateGaps(true);
        this.frame.setLayout(groupLayout);
        currentElementText.setFont(AppFont.getInstance());
        currentElementText.setName("CurrentElementText");
        currentElementText.setContentType("text/html");
        currentElementText.setFont(AppFont.getInstance());
        currentElementText.setForeground(TafGuiColor.textColor);

        currentElementPanel.setLayout(new GridLayout(1, 1));
        currentElementPanel.add(currentElementText);

        programaticDescriptionPanel.setLayout(new GridLayout(2, 1));
        programaticDescriptionScrollPanel = new JScrollPane(programaticDescriptionPanel);

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                position.stopTrack();
                positionTracker.destroy();
                frame.dispose();
            }
        });

        groupLayout.setHorizontalGroup(
                groupLayout.createSequentialGroup()
                        .addGroup(groupLayout.createParallelGroup()
                                .addComponent(headline)
                                .addComponent(currentElementLabel)
                                .addComponent(currentElementPanel)
                                .addComponent(programaticDescriptionScrollPanel)
                                .addComponent(closeButton)
                        )
        );
        groupLayout.setVerticalGroup(
                groupLayout.createSequentialGroup()
                        .addComponent(headline)
                        .addComponent(currentElementLabel)
                        .addComponent(currentElementPanel)
                        .addComponent(programaticDescriptionScrollPanel)
                        .addComponent(closeButton)
        );

        this.frame.pack();
        this.frame.setSize(Toolkit.getDefaultToolkit().getScreenSize().width / 3, Toolkit.getDefaultToolkit().getScreenSize().height / 2);
        this.frame.setVisible(true);
        this.frame.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
                trackElement();
            }

            @Override
            public void windowClosing(WindowEvent e) {

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
        positionTracker.start();
    }

    private class MousePosition implements Runnable {
        PointerInfo pointerInfo;

        @Override
        public void run() {
            pointerInfo = MouseInfo.getPointerInfo();
        }

        public Point getMousePosition() {
            if(pointerInfo == null)return null;
            return pointerInfo.getLocation();
        }

        public void stopTrack(){
            pointerInfo = null;
        }
    }

    private void trackElement() {
        Set<String> touchedComponents = new HashSet<>();
        long timer = System.currentTimeMillis();
        while (System.currentTimeMillis() - timer < 10000) {

            for (Window w : JavaSupportTab.applicationUnderTest.getWindowsForSUT()) {
                if (!w.isShowing()) continue;
                try {
                    Component component = identifyComponent(((JFrame) w).getContentPane(), position.getMousePosition());
                    if (component != null) {
                        if (!touchedComponents.contains(component.toString())) {
                            //Recorder recorder = new Recorder(component);
                            //new Thread(recorder).start();
                            touchedComponents.add(component.toString());
                            component.addMouseListener(new MouseListener() {
                                @Override
                                public void mouseClicked(MouseEvent e) {
                                    if (e.getComponent() != null)
                                        System.out.println("java.click(new JavaGuiElement(" + e.getComponent().toString() + ");");
                                }

                                @Override
                                public void mousePressed(MouseEvent e) {
                                    if (e.getComponent() != null)
                                        System.out.println("java.pressMouse(new JavaGuiElement(" + e.getComponent().toString() + ");");
                                }

                                @Override
                                public void mouseReleased(MouseEvent e) {
                                    System.out.println("java.mouseReleased(" + e.getComponent().toString() + ");");
                                }

                                @Override
                                public void mouseEntered(MouseEvent e) {
                                    System.out.println("java.mouseEntered(" + e.getComponent().toString() + ");");
                                }

                                @Override
                                public void mouseExited(MouseEvent e) {

                                }
                            });
                        }
                        updateComponentDescriptionPanel(component);
                    }
                } catch (Exception ignored) {
                }
            }
        }
    }

    private void updateComponentDescriptionPanel(Component component) {
        int parameterCount = 1;
        currentElementPanel.removeAll();
        if (component == null) currentElementPanel.repaint();

        currentElementPanel.add(new TafLabel("Class name"));
        currentElementPanel.add(new TafLabel(component.getClass().getSimpleName()));

        if (component.getName() != null && component.getName().length() > 0) {
            currentElementPanel.add(new TafLabel("Name"));
            currentElementPanel.add(new TafLabel(component.getName()));
            parameterCount++;
        }

        String text = null;
        try {
            text = (String) se.claremont.autotest.javasupport.interaction.MethodInvoker.invokeMethod(null, component, "getText", null);
        } catch (Exception ignored) {
        }
        if (text != null && text.length() > 0) {
            currentElementPanel.add(new TafLabel("Text"));
            currentElementPanel.add(new TafLabel(text));
            parameterCount++;
        }

        programaticDescriptionPanel.removeAll();
        programaticDescriptionPanel.add(new TafLabel("Programatic description"));
        currentElementText.setText(componentDeclarationString(component).replace(" ", "&nbsp;"));
        programaticDescriptionPanel.add(currentElementText);
        programaticDescriptionPanel.revalidate();
        programaticDescriptionPanel.repaint();
        programaticDescriptionScrollPanel.revalidate();
        programaticDescriptionScrollPanel.repaint();

        currentElementPanel.setLayout(new GridLayout(parameterCount, 2));
        currentElementPanel.revalidate();
        currentElementPanel.repaint();
        currentElementPanel.setVisible(true);
        this.frame.revalidate();
        this.frame.repaint();
    }

    private String componentDeclarationString(Component c) {
        if (c == null) return null;
        String elementName = "Noname";
        StringBuilder sb = new StringBuilder();

        sb.append("      ").append(".byClassName(\"").append(c.getClass().getSimpleName()).append("\")<br>");
        if (c.getName() != null && c.getName().length() > 0) {
            sb.append("      .andByName(\"").append(c.getName()).append("\")<br>");
            elementName = c.getName();
        }
        String text = null;
        try {
            text = (String) se.claremont.autotest.javasupport.interaction.MethodInvoker.invokeMethod(null, c, "getText", null);
        } catch (Exception ignored) {
        }
        if (text != null && text.length() > 0) {
            sb.append("      .andByExactText(\"").append(text).append("\")<br>");
        }
        return "<html><body><div style=\"white-space: pre; \" ><font size=\"" + (AppFont.getInstance().getSize() * 2 / 3) + "\" color=\"darkgrey\">   public static JavaGuiElement " + elementName + " = new JavaGuiElement(By<br>" + sb.toString() + "   );</font></div></body></html>";
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
        positionTracker.interrupt();
        System.out.println(returnElement.toString());
        return returnElement;
    }
}
