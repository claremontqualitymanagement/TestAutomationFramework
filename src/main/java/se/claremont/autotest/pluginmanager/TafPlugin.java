package se.claremont.autotest.pluginmanager;

/**
 * Created by jordam on 2016-10-28.
 */
public interface TafPlugin {
    public void run();
    public String pluginName();
    public String pluginDescription();
    public String version();
}
