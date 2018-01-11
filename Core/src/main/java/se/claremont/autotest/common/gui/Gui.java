package se.claremont.autotest.common.gui;

import se.claremont.autotest.common.gui.abouttab.AboutTabPanel;
import se.claremont.autotest.common.gui.createtesttab.CreateTestTabPanel;
import se.claremont.autotest.common.gui.guistyle.TafFrame;
import se.claremont.autotest.common.gui.guistyle.TafGuiColor;
import se.claremont.autotest.common.gui.guistyle.TafGuiColorOriginal;
import se.claremont.autotest.common.gui.guistyle.TafLabel;
import se.claremont.autotest.common.gui.plugins.IGuiTab;
import se.claremont.autotest.common.gui.teststructure.TestStep;
import se.claremont.autotest.common.gui.teststructure.TestStepList;
import se.claremont.autotest.common.gui.userpreferences.Preferences;
import se.claremont.autotest.common.gui.userpreferences.SavePreferencesOnCloseWindowsListener;
import se.claremont.autotest.common.testrun.TestRun;
import se.claremont.autotest.common.gui.plugins.PluginLoader;
import se.claremont.autotest.common.gui.runtab.RunTestTabPanel;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;

public class Gui{

    public static TafFrame applicationWindow = new TafFrame("TAF - Application");
    private Font appFont;
    public static Preferences preferences = new Preferences();
    public static HashMap<String, String> defaultSettings;
    public static TafGuiColor colorTheme;
    private static TestStepList availableTestSteps = new TestStepList();
    JTabbedPane tabs = new JTabbedPane();

    public Gui() {
        defaultSettings = new HashMap<>(TestRun.getSettings());

        preferences.loadFromFile(Preferences.getPreferencesFile());
        applicationWindow.addWindowListener(new SavePreferencesOnCloseWindowsListener());

        colorTheme = new TafGuiColorOriginal();
        Container pane = applicationWindow.getContentPane();
        pane.setBackground(Gui.colorTheme.backgroundColor);
        applicationWindow.getRootPane().setBackground(Gui.colorTheme.backgroundColor);
        setFontSize();
        tabs.setBackground(Gui.colorTheme.backgroundColor);
        tabs.setFont(appFont);
        tabs.setForeground(Gui.colorTheme.textColor);
        PluginLoader pluginLoader = new PluginLoader();
        //java.util.List<JPanel> panels = new ArrayList<>();
        RunTestTabPanel runTestTabPanel = new RunTestTabPanel();
        tabs.addTab("1. " + runTestTabPanel.getName(), runTestTabPanel.getPanel());
        CreateTestTabPanel createTestTabPanel = new CreateTestTabPanel();
        tabs.addTab("2. " + createTestTabPanel.getName(), createTestTabPanel.getPanel());
        int i = 3;
        for(IGuiTab tab : pluginLoader.identifyGuiTabs()){
            tabs.addTab(i + ". " + tab.getName(), tab.getPanel());
            i++;
            //tabs.setTitleAt(tabs.getTabCount()-1, tab.getName());
        }
        AboutTabPanel aboutTabPanel = new AboutTabPanel();
        tabs.add(i + ". " + aboutTabPanel.getName(), aboutTabPanel.getPanel());
        TafLabel helpTextAboutJarFunctionality = new TafLabel("");
        if(isStartedFromJar()){
            helpTextAboutJarFunctionality.setText("If you start TAF from the compiled jar file addintional tabs with functinality from technology specific plugins are displayed.");
        }

        for(int tabCounter = 0; tabCounter < tabs.getTabCount(); tabCounter++){
            if(tabs.getComponentAt(tabCounter) == null) continue;
            tabs.setMnemonicAt(tabCounter, String.valueOf(tabCounter + 1).charAt(0));
        }
        pane.add(tabs);

        GroupLayout groupLayout = new GroupLayout(pane);
        pane.setLayout(groupLayout);
        groupLayout.setAutoCreateGaps(true);
        groupLayout.setAutoCreateContainerGaps(true);
        groupLayout.setHorizontalGroup(
                groupLayout.createSequentialGroup()
                        .addGroup(groupLayout.createParallelGroup()
                                .addComponent(helpTextAboutJarFunctionality)
                                .addComponent(tabs)
                        )
        );
        groupLayout.setVerticalGroup(
                groupLayout.createSequentialGroup()
                        .addComponent(helpTextAboutJarFunctionality)
                        .addComponent(tabs)
        );


        applicationWindow.pack();
        applicationWindow.setSize(3 * Toolkit.getDefaultToolkit().getScreenSize().width / 4, Toolkit.getDefaultToolkit().getScreenSize().height / 2);
        applicationWindow.setTitle("TAF - Test Automation Framework");
        applicationWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        applicationWindow.setVisible(true);
    }

    boolean isStartedFromJar(){
        return (tabs.getTabCount() < 4);
    }

    public static synchronized List<TestStep> getAvailableTestSteps(){
        return availableTestSteps.getTestSteps();
    }

    public static synchronized void addChangeListenerToListOfAvailableTestSteps(TestStepList.TestStepListChangeListener testStepListChangeListener){
        availableTestSteps.addChangeListener(testStepListChangeListener);
    }

    public static synchronized void addTestStepToListOfAvailableTestSteps(TestStep testStep){
        availableTestSteps.add(testStep);
    }

    public static synchronized void removeTestStepFromListOfAvailableTestSteps(TestStep testStep){
        availableTestSteps.remove(testStep);
    }

    public void activate() {
        applicationWindow.setVisible(true);
    }

    private void setFontSize() {
        appFont = new Font("serif", Font.PLAIN, Toolkit.getDefaultToolkit().getScreenSize().height / 50);
    }

}
