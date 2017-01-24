package se.claremont.autotest.common.testrun;

//import se.claremont.autotest.testmanagementtoolintegration.testlink.TestlinkReporter;

import se.claremont.autotest.common.logging.ConsoleLogLevel;
import se.claremont.autotest.common.reporting.testrunreports.BaseFolderHtmlIndexFile;
import se.claremont.autotest.common.testset.TestSet;

/**
 * A test run is the entity of every time some set(s) of test cases are run.
 * Created by jordam on 2016-08-17.
 */
public class TestRun {
    public static Settings settings = new Settings();
    public static int fileCounter = 0;
    public static String testRunName = "";
    public static int exitCode;
    public static TestSet currentTestSet;
    public static final TestRunReporterFactory reporters = new TestRunReporterFactory();
    public static boolean isInitialized = false;
    public static ConsoleLogLevel consoleLogLevel = ConsoleLogLevel.MODERATE;
    static TafRunListener runListener = null;

    /**
     * TAF and TA test(s) standard codes.
     */
    public enum ExitCodeTable {
        INIT_OK                     (0),
        RUN_TAF_ERROR_FATAL         (500),
        RUN_TAF_ERROR_MODERATE      (400),
        RUN_TEST_ERROR_FATAL        (501),
        RUN_TEST_ERROR_MODERATE     (401);

        private final int value;

        ExitCodeTable(final int newValue) {
            value = newValue;
        }

        public int getValue() {
            return value;
        }
    }

    public static void initializeIfNotInitialized() {
        if(!isInitialized){
            currentTestSet = null;
            settings = new Settings();
            exitCode = ExitCodeTable.INIT_OK.getValue();
            isInitialized = true;
        }
    }

    public static String reportLinkPrefix(){
        if(TestRun.settings.getValue(Settings.SettingParameters.HTML_REPORTS_LINK_PREFIX) == null ||
                TestRun.settings.getValue(Settings.SettingParameters.HTML_REPORTS_LINK_PREFIX).toLowerCase().equals("file")) return "file";
        if(TestRun.settings.getValue(Settings.SettingParameters.HTML_REPORTS_LINK_PREFIX).toLowerCase().equals("http")){
            return "http";
        } else if (TestRun.settings.getValue(Settings.SettingParameters.HTML_REPORTS_LINK_PREFIX).toLowerCase().equals("https")){
            return "https";
        }
        return TestRun.settings.getValue(Settings.SettingParameters.HTML_REPORTS_LINK_PREFIX);
    }

    public static void evaluateCurrentTestSet(){
        if(currentTestSet != null)
            reporters.evaluateTestSet(currentTestSet);
    }

    public static void reportTestRun(){
        reporters.reportTestRun();
        BaseFolderHtmlIndexFile baseFolderHtmlIndexFile = new BaseFolderHtmlIndexFile();
    }

    public static void manageRunListener(){
        if(runListener == null){
            runListener = new TafRunListener();
        }
    }
}
