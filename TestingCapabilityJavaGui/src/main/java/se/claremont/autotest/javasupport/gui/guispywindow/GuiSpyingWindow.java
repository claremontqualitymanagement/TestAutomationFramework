package se.claremont.autotest.javasupport.gui.guispywindow;

import se.claremont.autotest.common.gui.guistyle.*;
import se.claremont.autotest.javasupport.gui.JavaSupportTab;
import se.claremont.autotest.javasupport.objectstructure.JavaWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GuiSpyingWindow {

    static String currentElementParametersForClipboard;
    private TafFrame frame;
    static String elementProgramaticDescriptionFormattedForClipboard = "";
    private GroupLayout groupLayout;
    private final TafLabel headline = new TafLabel("GUI Spy");
    private final TafLabel hintText = new TafLabel("Press Ctrl+i to copy programatic description to clipbard.");
    private final TafLabel hintText2 = new TafLabel("Press Ctrl+d to copy parameters to clipbard.");
    private final TafLabel hintText3 = new TafLabel("Press Ctrl+k to pause and un-pause.");
    private final TafLabel hintText4 = new TafLabel("Press Ctrl+j for super detailed info");
    private final TafLabel blank = new TafLabel(" ");
    private final TafLabel currentElementLabel = new TafLabel("Current element:");
    private final TafPanel currentElementPanel = new TafPanel("CurrentElementPanel");
    private final TafHtmlTextPane currentElementText = new TafHtmlTextPane("CurrentElementTextPanel");
    private JScrollPane programaticDescriptionScrollPanel;
    private final TafPanel programaticDescriptionPanel = new TafPanel("ProgramaticDescriptionPanel");
    private final TafButton closeButton = new TafButton("Close");
    static boolean executionIsPaused = false;

    public GuiSpyingWindow() {
        frame = new TafFrame("TAF - GUI Spy");

        groupLayout = new GroupLayout(frame.getContentPane());
        groupLayout.setAutoCreateContainerGaps(true);
        groupLayout.setAutoCreateGaps(true);
        frame.setLayout(groupLayout);

        currentElementPanel.setLayout(new GridLayout(1, 1));
        headline.setFont(new Font(AppFont.getInstance().getName(), AppFont.getInstance().getStyle(), AppFont.getInstance().getSize() * 3/2));
        Font hintsFont = new Font(AppFont.getInstance().getName(), Font.ITALIC, AppFont.getInstance().getSize() * 3/4);
        hintText.setFont(hintsFont);
        hintText2.setFont(hintsFont);
        hintText3.setFont(hintsFont);
        hintText4.setFont(hintsFont);


        TafLabel programaticDescriptionLabel = new TafLabel("Programatic element description");
        programaticDescriptionScrollPanel = new JScrollPane(currentElementText);

        closeButton.setMnemonic('C');
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
                                .addComponent(hintText)
                                .addComponent(hintText2)
                                .addComponent(hintText3)
                                .addComponent(hintText4)
                                .addComponent(blank)
                                .addComponent(currentElementLabel)
                                .addComponent(currentElementPanel)
                                .addComponent(blank)
                                .addComponent(programaticDescriptionLabel)
                                .addComponent(programaticDescriptionScrollPanel)
                                .addComponent(closeButton)
                        )
        );
        groupLayout.setVerticalGroup(
                groupLayout.createSequentialGroup()
                        .addComponent(headline)
                        .addComponent(hintText)
                        .addComponent(hintText2)
                        .addComponent(hintText3)
                        .addComponent(hintText4)
                        .addComponent(blank)
                        .addComponent(currentElementLabel)
                        .addComponent(currentElementPanel)
                        .addComponent(blank)
                        .addComponent(programaticDescriptionLabel)
                        .addComponent(programaticDescriptionScrollPanel)
                        .addComponent(closeButton)
        );

        frame.pack();
        frame.setSize(Toolkit.getDefaultToolkit().getScreenSize().width / 2, Toolkit.getDefaultToolkit().getScreenSize().height * 2/ 3);
        frame.setVisible(true);

        JFrame keyBoardEventCatcher = new JFrame();
        keyBoardEventCatcher.setSize(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height);
        keyBoardEventCatcher.setUndecorated(true);
        keyBoardEventCatcher.setOpacity(0);
        keyBoardEventCatcher.setVisible(true);
        keyBoardEventCatcher.getRootPane().setOpaque(false);
        keyBoardEventCatcher.addKeyListener(new GuiSpyKeyboardListener(frame));

        for (Window window : JavaSupportTab.applicationUnderTest.getWindowsForSUT()) {
            JavaWindow javaWindow = new JavaWindow(window);
            for(Object object  :javaWindow.getComponents()){
                Component component = (Component) object;
                component.addMouseListener(new GuiSpyMouseListener(currentElementText, currentElementPanel));
            }
        }
    }

}
