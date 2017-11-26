package se.claremont.autotest.common.testrun.gui.plugins;

import javax.swing.*;
import java.util.HashMap;

public class PluginLoader {

    String[] possibleGuiPluginClasses = {"se.claremont.autotest.javasupport.gui.JavaSupportTab"};

    public HashMap<String, JPanel> getAccessibleGuiPluginTabs(){
        HashMap<String, JPanel> panels = new HashMap<>();
        for(String pluginGuiClassName : possibleGuiPluginClasses){
            JPanel panel = getJavaTabIfAvailable(pluginGuiClassName);
            if(panel == null) continue;
            String pluginName = pluginGuiClassName;
            if(pluginGuiClassName.contains(".")){
                pluginName = pluginGuiClassName.substring(pluginGuiClassName.lastIndexOf(".")+1);
                if(pluginName.length() == 0) pluginName = pluginGuiClassName;
            }
            panels.put(pluginName, panel);
        }
        return panels;
    }


    public JPanel getJavaTabIfAvailable(String pluginClassName){
        Class panel = null;
        try {
            panel = PluginLoader.class.getClassLoader().loadClass(pluginClassName);
        } catch (ClassNotFoundException ignored) { }
        if(panel == null) return null;
        try {
            JPanel returnPanel = (JPanel)panel.newInstance();
            return returnPanel;
        } catch (InstantiationException e) {
            return null;
        } catch (IllegalAccessException e) {
            return null;
        }
    }
}
