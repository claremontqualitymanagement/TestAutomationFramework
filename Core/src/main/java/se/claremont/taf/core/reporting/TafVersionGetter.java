package se.claremont.taf.core.reporting;

import java.io.InputStream;
import java.util.Properties;

/**
 * Attempts to identify version of TAF code base for reporting to log
 *
 * Created by jordam on 2017-03-17.
 */
public class TafVersionGetter {

    public static String tafVersion(){
        TafVersionGetter tafVersionGetter = new TafVersionGetter();
        return tafVersionGetter.getVersion();
    }

    private synchronized String getVersion() {
        String version = null;

        // try to load from maven properties first
        try {
            Properties p = new Properties();
            InputStream is = getClass().getResourceAsStream("/META-INF/maven/com.github.claremontqualitymanagement.TestAutomationFramework/TafFull/pom.properties");
            if (is != null) {
                p.load(is);
                version = p.getProperty("version", "");
            }
        } catch (Exception e) {
            // ignore
        }

        // fallback to using Java API
        if (version == null) {
            Package aPackage = getClass().getPackage();
            if (aPackage != null) {
                version = aPackage.getImplementationVersion();
                if (version == null) {
                    version = aPackage.getSpecificationVersion();
                }
            }
        }

        return version;
    }
}
