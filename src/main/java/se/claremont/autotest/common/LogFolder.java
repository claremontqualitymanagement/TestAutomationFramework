package se.claremont.autotest.common;

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
    public static String testRunLogFolder = null;  //Used for test case logging
    @SuppressWarnings("FieldCanBeLocal")
    private static String baseLogFolder = null;     //Read from Settings

    /**
     * Sets the values to run time values.
     *
     * @param testSetName The name of the testSet. Normally used to get test case logs in correct folder
     */
    public static void setLogFolder(String testSetName){
        if(testRunLogFolder == null){
            baseLogFolder = TestRun.settings.getValueForProperty("baseLogFolder");
            if(!baseLogFolder.endsWith("\\")){ baseLogFolder = baseLogFolder + "\\"; }
            testRunLogFolder = baseLogFolder + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + "_" + testSetName + "\\";
        }
    }
}
