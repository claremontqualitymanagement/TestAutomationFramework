package se.claremont.taf.core.logging;

import com.fasterxml.jackson.annotation.JsonProperty;
import se.claremont.taf.core.support.StringManagement;
import se.claremont.taf.core.testrun.Settings;
import se.claremont.taf.core.testrun.TestRun;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The LogFolder class maintains the test run log folder for logging purposes of test based logs,
 * html based logs, summary log, screen shots and other assets that has to do with test resources
 * being saved along a test execution.
 *
 * Created by jordam on 2016-09-05.
 */
public class LogFolder {
    @JsonProperty public static String testRunLogFolder = null;  //Used for test case logging
    @SuppressWarnings("FieldCanBeLocal")
    @JsonProperty private static String baseLogFolder = null;     //Read from Settings
    @JsonProperty public static String urlToBlobStorage = null;  //Read from Settings

    /**
     * Sets the values to run time values.
     *
     * @param testSetName The name of the testSet. Normally used to get test case logs in correct folder
     */
    public static void setLogFolder(String testSetName){
        if(testRunLogFolder == null){
            if(TestRun.getRunName() != null && TestRun.getRunName().length() > 0){
                baseLogFolder = TestRun.getSettingsValue(Settings.SettingParameters.BASE_LOG_FOLDER);
                if(!(baseLogFolder.endsWith("\\") || baseLogFolder.endsWith("/"))){
                    baseLogFolder = baseLogFolder + File.separator;
                }
                testRunLogFolder = StringManagement.filePathToCurrentOsAdaptedFormat(baseLogFolder) + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + "_" + TestRun.getRunName() + File.separator;
            } else {
                baseLogFolder = TestRun.getSettingsValue(Settings.SettingParameters.BASE_LOG_FOLDER);
                if(!(baseLogFolder.endsWith("\\") || baseLogFolder.endsWith("/"))){
                    baseLogFolder = baseLogFolder + File.separator;
                }
                testRunLogFolder = StringManagement.filePathToCurrentOsAdaptedFormat(baseLogFolder) + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + "_" + testSetName + File.separator;
            }
        }
    }

    /**
     * Sets the values for cloud storage where the log has been published.
     * Read from Settings
     */
    public static void setCloudLogStorageUrl(){
        urlToBlobStorage = TestRun.getSettingsValue(Settings.SettingParameters.CLOUD_LOG_URL);
    }
}
