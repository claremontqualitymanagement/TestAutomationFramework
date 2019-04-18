package se.claremont.taf.core.gui.abouttab;

import se.claremont.taf.core.gui.Gui;
import se.claremont.taf.core.gui.guistyle.*;
import se.claremont.taf.core.gui.plugins.IGuiTab;
import se.claremont.taf.core.gui.runtab.DiagnosticsRunWaitDialogue;
import se.claremont.taf.core.gui.runtab.HelpDialogue;
import se.claremont.taf.core.reporting.TafVersionGetter;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class AboutTabPanel implements IGuiTab {
    public static java.util.List<JDialog> openDialogs = new ArrayList<>();
    TafPanel panel;
    private TafButton runDiagnosticsButton = new TafButton("Run diagnostics tests");
    private TafButton showHelpTextButton = new TafButton("Help");
    private TafLabel versionLabel = new TafLabel("TafVersionText");
    private TafFrame mainApplication;
    private TafHtmlTextPane textPane = new TafHtmlTextPane("AboutTextPane");
    private TafLabel colorThemeLabel = new TafLabel("Execution mode");
    public static java.util.List<JFrame> openFrames = new ArrayList<>();
    private JSpinner colorThemeSpinner = new JSpinner();
    String[] spinnerOptions = {
            "TAF original",
            "Inverted"
    };

    public AboutTabPanel(){
        this(null);
    }

    public AboutTabPanel(TafFrame mainApplication) {
        this.mainApplication = mainApplication;
        panel = new TafPanel("AboutTab");
        panel.setBackground(TafGuiColor.backgroundColor);
        GroupLayout groupLayout = new GroupLayout(panel);
        groupLayout.setAutoCreateGaps(true);
        groupLayout.setAutoCreateContainerGaps(true);
        panel.setLayout(groupLayout);

        textPane.setEditable(false);
        textPane.setBackground(TafGuiColor.backgroundColor);
        textPane.addHyperlinkListener(new HyperlinkListener() {
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    if (Desktop.isDesktopSupported()) {
                        try {
                            Desktop.getDesktop().browse(e.getURL().toURI());
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        } catch (URISyntaxException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }
        });
        textPane.setText(getBriefText());

        prepareHelpButton();

        prepareDiagnosticsRunButton();
        prepareColorThemeSpinner();

        versionLabel.setText("");
        if (TafVersionGetter.tafVersion() != null)
            versionLabel.setText("TAF version: " + TafVersionGetter.tafVersion());

        /*
        panel.add(versionLabel);
        panel.add(textPane);
        panel.add(showHelpTextButton);
        panel.add(runDiagnosticsButton);
*/
        groupLayout.setHorizontalGroup(
                groupLayout.createSequentialGroup()
                        .addGroup(groupLayout.createParallelGroup()
                                .addComponent(textPane)
                                .addComponent(versionLabel)
                                .addGroup(groupLayout.createSequentialGroup()
                                        .addComponent(showHelpTextButton)
                                        .addComponent(runDiagnosticsButton)
                                )
                        )
        );
        groupLayout.setVerticalGroup(
                groupLayout.createSequentialGroup()
                        .addComponent(textPane)
                        .addComponent(versionLabel)
                        .addGroup(groupLayout.createParallelGroup()
                                .addComponent(showHelpTextButton)
                                .addComponent(runDiagnosticsButton)
                        )
        );

        //panel.add(colorThemeLabel);
        //panel.add(colorThemeSpinner);

    }

    @Override
    public JPanel getPanel() {
        return panel;
    }

    private String getBriefText() {
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append("<body style=\"color: grey; font-size: " + AppFont.getInstance().getSize() + "; \">");
        sb.append("<h1>This is TAF</h1>");
        sb.append("<p>TAF is a tool and a framework for test automation.</p>");
        sb.append("<p>TAF is created for use from your developer IDE, but this GUI could prove handy to get you going.</p>");
        sb.append("<p>More info can be found at the project site at <a href=\"https://github.com/claremontqualitymanagement/");
        sb.append("TestAutomationFramework\" target=\"_blank\">GitHub</a> or the project <a href=\"https://github.com/");
        sb.append("claremontqualitymanagement/TestAutomationFramework/wiki\" target=\"_blank\">wiki</a>.</p>");
        sb.append("</body>");
        sb.append("</html>");
        return sb.toString();
    }

    private void prepareColorThemeSpinner() {
        colorThemeLabel.setFont(AppFont.getInstance());

        colorThemeSpinner.setFont(AppFont.getInstance());
        colorThemeSpinner.setForeground(Gui.colorTheme.textColor);
        colorThemeSpinner.setName("ColorThemeSpinner");
        SpinnerListModel spinnerListModel = new SpinnerListModel(spinnerOptions);
        colorThemeSpinner.setModel(spinnerListModel);
        JFormattedTextField spinnerTextArea = ((JSpinner.DefaultEditor) colorThemeSpinner.getEditor()).getTextField();
        spinnerTextArea.setName("ColorThemeSpinnerTextArea");
        spinnerTextArea.setBackground(Color.white);
        spinnerTextArea.setForeground(Gui.colorTheme.textColor);
        spinnerTextArea.setEditable(false);
        colorThemeSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                //UIManager.setLookAndFeel(new MetalLookAndFeel());
                switch (colorThemeSpinner.getValue().toString()) {
                    case "TAF original":
                        Gui.colorTheme = new TafGuiColorOriginal();
                        break;
                    case "Inverted":
                        Gui.colorTheme = new TafGuiColorAlternative1();
                        break;
                }
                for (JFrame frame : openFrames) {
                    for (Component c : frame.getComponents()) {
                        c.repaint();
                    }
                }
                for (JDialog dialog : openDialogs) {
                    for (Component c : dialog.getComponents()) {
                        c.repaint();
                    }
                    SwingUtilities.updateComponentTreeUI(dialog);
                    dialog.invalidate();
                    dialog.validate();
                    dialog.repaint();
                    dialog.setVisible(false);
                    dialog.setVisible(true);
                }
            }
        });

    }

    private void prepareDiagnosticsRunButton() {
        runDiagnosticsButton.setMnemonic('d');
        runDiagnosticsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new DiagnosticsRunWaitDialogue(mainApplication);
            }
        });
    }

    private void prepareHelpButton() {
        showHelpTextButton.setMnemonic('h');
        showHelpTextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new HelpDialogue();
            }
        });
    }


    @Override
    public String getName() {
        return "About";
    }
}
