package se.claremont.autotest.pluginmanager;

/**
 * Created by jordam on 2016-10-28.
 */
public interface TafPlugin {
    void run();
    String pluginName();
    String pluginDescription();
    String version();
}
