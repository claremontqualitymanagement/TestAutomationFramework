package se.claremont.autotest.javasupport.gui.guispywindow;

import se.claremont.autotest.common.gui.guistyle.*;
import se.claremont.autotest.javasupport.gui.JavaSupportTab;
import se.claremont.autotest.javasupport.objectstructure.JavaWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GuiSpyingWindow {

    private TafFrame frame;
    static String elementProgramaticDescriptionFormattedForClipboard = "";
    private GroupLayout groupLayout;
    private final TafLabel headline = new TafLabel("GUI Spy");
    private final TafLabel hintText = new TafLabel("Press Ctrl+i to copy description to clipbard.");
    private final TafLabel currentElementLabel = new TafLabel("Current element:");
    private final TafPanel currentElementPanel = new TafPanel("CurrentElementPanel");
    private final JTextPane currentElementText = new JTextPane();
    private JScrollPane programaticDescriptionScrollPanel;
    private final TafPanel programaticDescriptionPanel = new TafPanel("ProgramaticDescriptionPanel");
    private final TafButton closeButton = new TafButton("Close");


    public GuiSpyingWindow() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                frame = new TafFrame();
                groupLayout = new GroupLayout(frame.getContentPane());
                groupLayout.setAutoCreateContainerGaps(true);
                groupLayout.setAutoCreateGaps(true);
                frame.setLayout(groupLayout);
                currentElementText.setFont(AppFont.getInstance());
                currentElementText.setName("CurrentElementText");
                currentElementText.setContentType("text/html");
                currentElementText.setFont(AppFont.getInstance());
                currentElementText.setForeground(TafGuiColor.textColor);
                currentElementPanel.setLayout(new GridLayout(1, 1));
                headline.setFont(new Font(AppFont.getInstance().getName(), AppFont.getInstance().getStyle(), AppFont.getInstance().getSize() * 3/2));

                TafLabel programaticDescriptionLabel = new TafLabel("Programatic element description");
                programaticDescriptionScrollPanel = new JScrollPane(currentElementText);

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
                                        .addComponent(currentElementLabel)
                                        .addComponent(currentElementPanel)
                                        .addComponent(programaticDescriptionLabel)
                                        .addComponent(programaticDescriptionScrollPanel)
                                        .addComponent(hintText)
                                        .addComponent(closeButton)
                                )
                );
                groupLayout.setVerticalGroup(
                        groupLayout.createSequentialGroup()
                                .addComponent(headline)
                                .addComponent(currentElementLabel)
                                .addComponent(currentElementPanel)
                                .addComponent(programaticDescriptionLabel)
                                .addComponent(programaticDescriptionScrollPanel)
                                .addComponent(hintText)
                                .addComponent(closeButton)
                );

                frame.pack();
                frame.setSize(Toolkit.getDefaultToolkit().getScreenSize().width / 3, Toolkit.getDefaultToolkit().getScreenSize().height / 2);
                frame.setVisible(true);

                JFrame keyBoardEventCatcher = new JFrame();
                keyBoardEventCatcher.setSize(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height);
                keyBoardEventCatcher.setUndecorated(true);
                keyBoardEventCatcher.setOpacity(0);
                keyBoardEventCatcher.setVisible(true);
                keyBoardEventCatcher.getRootPane().setOpaque(false);
                keyBoardEventCatcher.addKeyListener(new DescriptionToClipboardManager(hintText));

                for (Window window : JavaSupportTab.applicationUnderTest.getWindowsForSUT()) {
                    JavaWindow javaWindow = new JavaWindow(window);
                    for(Object object  :javaWindow.getComponents()){
                        Component component = (Component) object;
                        component.addMouseListener(new GuiSpyMouseListener(currentElementText, currentElementPanel));
                    }
                }
            }
        });
    }

}
