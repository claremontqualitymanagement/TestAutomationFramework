package se.claremont.autotest.common.gui;

import se.claremont.autotest.common.gui.plugins.IGuiTab;
import se.claremont.autotest.common.testrun.TestRun;
import se.claremont.autotest.common.gui.plugins.PluginLoader;
import se.claremont.autotest.common.gui.runtab.RunTestTabPanel;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class Gui extends JFrame{

    private Font appFont;
    public static HashMap<String, String> defaultSettings;

    public Gui() {
        defaultSettings = new HashMap<>(TestRun.getSettings());
        Container pane = this.getContentPane();
        setFontSize();
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(appFont);
        tabs.addTab("Run tests", new RunTestTabPanel(this));
        tabs.addTab("Create tests", new CreateTestTabPanel());
        PluginLoader pluginLoader = new PluginLoader();
        java.util.List<IGuiTab> panels = pluginLoader.getAccessibleGuiPluginTabs();
        for(IGuiTab pluginTab : panels){
            tabs.addTab(pluginTab.getName(), pluginTab.getPanel());
        }
        pane.add(tabs);
        this.setSize(3 * Toolkit.getDefaultToolkit().getScreenSize().width / 4, Toolkit.getDefaultToolkit().getScreenSize().height / 2);
        this.setTitle("TAF - Test Automation Framework");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    public void activate() {
        this.setVisible(true);
    }

    private void setFontSize() {
        appFont = new Font("serif", Font.PLAIN, Toolkit.getDefaultToolkit().getScreenSize().height / 50);
    }

}
