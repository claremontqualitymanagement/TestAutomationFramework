package se.claremont.autotest.common.gui.plugins;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PluginLoader {

    String[] possibleGuiPluginClasses = {"se.claremont.autotest.javasupport.gui.JavaSupportTab"};

    public List<IGuiTab> getAccessibleGuiPluginTabs(){
        List<IGuiTab> panels = new ArrayList<>();
        for(String pluginGuiClassName : possibleGuiPluginClasses){
            IGuiTab panel = getJavaTabIfAvailable(pluginGuiClassName);
            if(panel == null) continue;
            panels.add(panel);
        }
        return panels;
    }


    public IGuiTab getJavaTabIfAvailable(String pluginClassName){
        Class panel = null;
        try {
            panel = PluginLoader.class.getClassLoader().loadClass(pluginClassName);
        } catch (ClassNotFoundException ignored) { }
        if(panel == null) return null;
        try {
            if(IGuiTab.class.isAssignableFrom(panel)){
                IGuiTab returnPanel = (IGuiTab) panel.newInstance();
                return returnPanel;
            }
        } catch (InstantiationException e) {
            return null;
        } catch (IllegalAccessException e) {
            return null;
        }
        return null;
    }
}
