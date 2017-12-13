package se.claremont.autotest.common.gui;

import se.claremont.autotest.common.gui.abouttab.AboutTabPanel;
import se.claremont.autotest.common.gui.createtesttab.CreateTestTabPanel;
import se.claremont.autotest.common.gui.guistyle.TafFrame;
import se.claremont.autotest.common.gui.guistyle.TafGuiColor;
import se.claremont.autotest.common.gui.guistyle.TafGuiColorOriginal;
import se.claremont.autotest.common.gui.plugins.IGuiTab;
import se.claremont.autotest.common.gui.userpreferences.Preferences;
import se.claremont.autotest.common.testrun.TestRun;
import se.claremont.autotest.common.gui.plugins.PluginLoader;
import se.claremont.autotest.common.gui.runtab.RunTestTabPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.HashMap;

public class Gui{

    public static TafFrame applicationWindow = new TafFrame("TAF - Application");
    private Font appFont;
    public static Preferences preferences = new Preferences();
    public static HashMap<String, String> defaultSettings;
    public static TafGuiColor colorTheme;

    public Gui() {
        defaultSettings = new HashMap<>(TestRun.getSettings());
        preferences.loadFromFile(getPreferencesFile());
        applicationWindow.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("Saving user preferences.");
                preferences.saveToFile(getPreferencesFile());
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
        colorTheme = new TafGuiColorOriginal();
        Container pane = applicationWindow.getContentPane();
        pane.setBackground(Gui.colorTheme.backgroundColor);
        applicationWindow.getRootPane().setBackground(Gui.colorTheme.backgroundColor);
        setFontSize();
        JTabbedPane tabs = new JTabbedPane();
        tabs.setBackground(Gui.colorTheme.backgroundColor);
        tabs.setFont(appFont);
        tabs.setForeground(Gui.colorTheme.textColor);
        PluginLoader pluginLoader = new PluginLoader();
        //java.util.List<JPanel> panels = new ArrayList<>();
        RunTestTabPanel runTestTabPanel = new RunTestTabPanel();
        tabs.addTab(runTestTabPanel.getName(), runTestTabPanel.getPanel());
        CreateTestTabPanel createTestTabPanel = new CreateTestTabPanel();
        tabs.addTab(createTestTabPanel.getName(), createTestTabPanel.getPanel());
        for(IGuiTab tab : pluginLoader.identifyGuiTabs()){
            tabs.addTab(tab.getName(), tab.getPanel());
            //tabs.setTitleAt(tabs.getTabCount()-1, tab.getName());
        }
        AboutTabPanel aboutTabPanel = new AboutTabPanel();
        tabs.add(aboutTabPanel.getName(), aboutTabPanel.getPanel());
        pane.add(tabs);
        applicationWindow.setSize(3 * Toolkit.getDefaultToolkit().getScreenSize().width / 4, Toolkit.getDefaultToolkit().getScreenSize().height / 2);
        applicationWindow.setTitle("TAF - Test Automation Framework");
        applicationWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private String getPreferencesFile() {
        return "C:\\temp\\tafuser.prefs";
    }


    public void activate() {
        applicationWindow.setVisible(true);
    }

    private void setFontSize() {
        appFont = new Font("serif", Font.PLAIN, Toolkit.getDefaultToolkit().getScreenSize().height / 50);
    }

}
