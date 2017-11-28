package se.claremont.autotest.common.gui;

import se.claremont.autotest.common.gui.abouttab.AboutTabPanel;
import se.claremont.autotest.common.gui.guistyle.TafFrame;
import se.claremont.autotest.common.gui.guistyle.TafGuiColor;
import se.claremont.autotest.common.gui.guistyle.TafGuiColorOriginal;
import se.claremont.autotest.common.gui.plugins.IGuiTab;
import se.claremont.autotest.common.testrun.TestRun;
import se.claremont.autotest.common.gui.plugins.PluginLoader;
import se.claremont.autotest.common.gui.runtab.RunTestTabPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Gui extends TafFrame{

    private Font appFont;
    public static HashMap<String, String> defaultSettings;
    public static TafGuiColor colorTheme;

    public Gui() {
        defaultSettings = new HashMap<>(TestRun.getSettings());
        colorTheme = new TafGuiColorOriginal();
        Container pane = this.getContentPane();
        pane.setBackground(Gui.colorTheme.backgroundColor);
        this.getRootPane().setBackground(Gui.colorTheme.backgroundColor);
        setFontSize();
        JTabbedPane tabs = new JTabbedPane();
        tabs.setBackground(Gui.colorTheme.backgroundColor);
        tabs.setFont(appFont);
        tabs.setForeground(Gui.colorTheme.textColor);
        PluginLoader pluginLoader = new PluginLoader();
        java.util.List<IGuiTab> panels = new ArrayList<>();
        panels.add(new RunTestTabPanel(this));
        panels.add(new CreateTestTabPanel());
        panels.addAll(pluginLoader.getAccessibleGuiPluginTabs());
        panels.add(new AboutTabPanel(this));
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
