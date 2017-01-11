package se.claremont.tools;

/**
 * Created by magnusolsson on 2016-12-15.
 */
public interface TafUserInfo {

    String getUserAccountName();
    String getUserHomeDirectory();
    String getUserWorkingDirectory();
    String getOperatingSystemArchitecture();
    String getOperatingSystemName();
    String getOperatingSystemVersion();
    String getJavaVersion();
    String getJavaHome();

    String getHostName();
    String getCanonicalHostName();
    String getHostAdress();

}
