package se.claremont.autotest.pluginmanager;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.Policy;
import java.util.ArrayList;
import java.util.List;

/**
 * The class of the plugin to load must have the same name as the jar file to be loaded.
 *
 * Created by jordam on 2016-10-28.
 */
public class PluginLoader {
    List<File> jarFiles = new ArrayList<>();

    public PluginLoader(){
        Policy.setPolicy(new PluginPolicy());
        System.setSecurityManager(new SecurityManager());
    }

    public void loadPlugins(){
        getListOfJarFilesInPluginDirectoryAndSubdirectories();
        loadAndStartPlugins();
    }


    private void getListOfJarFilesInPluginDirectoryAndSubdirectories(){
        List<String> filePatterns = new ArrayList<>();
        filePatterns.add("*.jar");
        //jarFiles = FileTester.searchForSpecificFiles(TestRun.settings.getValue(SettingParameters.PLUGIN_FOLDER), filePatterns);
    }

    private void loadAndStartPlugins(){
        for(File pluginJarFile : jarFiles){
            ClassLoader loader = null;
            try {
                loader = URLClassLoader.newInstance(new URL[] { new URL(pluginJarFile.getAbsolutePath())});
                TafPlugin plugin = null;
                plugin = (TafPlugin) loader.loadClass(pluginJarFile.getName()).newInstance();
                plugin.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
