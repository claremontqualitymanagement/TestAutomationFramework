package se.claremont.autotest.common.gui.abouttab;

import se.claremont.autotest.common.gui.Gui;
import se.claremont.autotest.common.gui.guistyle.*;
import se.claremont.autotest.common.gui.plugins.IGuiTab;
import se.claremont.autotest.common.gui.runtab.DiagnosticsRunWaitDialogue;
import se.claremont.autotest.common.gui.runtab.HelpDialogue;
import se.claremont.autotest.common.testrun.Settings;
import se.claremont.autotest.common.testrun.TestRun;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.metal.MetalLookAndFeel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class AboutTabPanel implements IGuiTab {
    public static java.util.List<JDialog> openDialogs = new ArrayList<>();
    TafPanel panel;
    private TafButton runDiagnosticsButton = new TafButton("Run diagnostics tests");
    private TafButton showHelpTextButton = new TafButton("Help");
    private TafFrame mainApplication;
    private TafLabel colorThemeLabel = new TafLabel("Execution mode");
    public static java.util.List<JFrame> openFrames = new ArrayList<>();
    private JSpinner colorThemeSpinner = new JSpinner();
    String[] spinnerOptions = {
            "TAF original",
            "Inverted"
    };

    public AboutTabPanel(TafFrame mainApplication){
        this.mainApplication = mainApplication;
        panel = new TafPanel("AboutTab");

        JTextPane textPane = new JTextPane();
        textPane.setName("AboutTextPane");
        textPane.setForeground(Gui.colorTheme.textColor);
        textPane.setBackground(Gui.colorTheme.backgroundColor);
        textPane.setFont(AppFont.getInstance());
        textPane.setText("Hej");

        prepareHelpButton();

        prepareDiagnosticsRunButton();
        prepareColorThemeSpinner();

        TafCloseButton closeButton = new TafCloseButton(mainApplication);
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        panel.add(textPane);
        panel.add(showHelpTextButton);
        panel.add(runDiagnosticsButton);
        //panel.add(colorThemeLabel);
        //panel.add(colorThemeSpinner);
        panel.add(closeButton);
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }

    private void prepareColorThemeSpinner(){
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
                switch (colorThemeSpinner.getValue().toString()){
                    case "TAF original":
                        Gui.colorTheme = new TafGuiColorOriginal();
                        break;
                    case "Inverted":
                        Gui.colorTheme = new TafGuiColorAlternative1();
                        break;
                }
                for(JFrame frame : openFrames){
                    for(Component c : frame.getComponents()){
                        c.repaint();
                    }
                }
                for(JDialog dialog : openDialogs){
                    for(Component c : dialog.getComponents()){
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
        runDiagnosticsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new DiagnosticsRunWaitDialogue(mainApplication);
            }
        });
    }

    private void prepareHelpButton() {
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
