package se.claremont.autotest.common.testrun;

import se.claremont.autotest.common.junitcustomization.TafParallelTestCaseRunner;
import se.claremont.autotest.common.junitcustomization.TafRunListener;
import se.claremont.autotest.common.logging.ConsoleLogLevel;
import se.claremont.autotest.common.reporting.testrunreports.TestRunReporterHtmlSummaryReportFile;
import se.claremont.autotest.common.testrun.reportingengine.TestRunReporterFactory;
import se.claremont.autotest.common.testset.TestSet;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * A test run is the entity of every time some set(s) of test cases are run.
 * Created by jordam on 2016-08-17.
 */
@SuppressWarnings("WeakerAccess")
public class TestRun {
    public static Settings settings = new Settings();
    public static int fileCounter = 0;
    public static String testRunName = "";
    public static int exitCode = ExitCodeTable.INIT_OK.getValue();
    public static List<TestSet> currentTestSets = new ArrayList<>();
    public static final TestRunReporterFactory reporters = new TestRunReporterFactory();
    public static boolean isInitialized = false;
    public static ConsoleLogLevel consoleLogLevel = ConsoleLogLevel.MODERATE;
    public static final Date startTime = new Date();
    public static Date stopTime;

    public TestRun(){
        initializeIfNotInitialized();
        testRunName = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
    }
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

    public static String getSettingsValue(Settings.SettingParameters parameter){
        initializeIfNotInitialized();
        return settings.getValue(parameter);
    }

    @SuppressWarnings("SameParameterValue")
    public static String getCustomSettingsValue(String parameter){
        initializeIfNotInitialized();
        return settings.getCustomValue(parameter);
    }

    public static void setSettingsValue(Settings.SettingParameters parameter, String value){
        initializeIfNotInitialized();
        settings.setValue(parameter, value);
    }

    public static void setCustomSettingsValue(String parameter, String value){
        initializeIfNotInitialized();
        settings.setCustomValue(parameter, value);
    }

    public static void initializeIfNotInitialized() {
        if(!isInitialized){
            exitCode = ExitCodeTable.INIT_OK.getValue();
            TafParallelTestCaseRunner.testSets = new HashSet<>();
            isInitialized = true;
        }
    }

    public static String reportLinkPrefix(){
        initializeIfNotInitialized();
        if(settings.getValue(Settings.SettingParameters.HTML_REPORTS_LINK_PREFIX) == null ||
                settings.getValue(Settings.SettingParameters.HTML_REPORTS_LINK_PREFIX).toLowerCase().equals("file")) return "file";
        if(settings.getValue(Settings.SettingParameters.HTML_REPORTS_LINK_PREFIX).toLowerCase().equals("http")){
            return "http";
        } else if (settings.getValue(Settings.SettingParameters.HTML_REPORTS_LINK_PREFIX).toLowerCase().equals("https")){
            return "https";
        }
        return settings.getValue(Settings.SettingParameters.HTML_REPORTS_LINK_PREFIX);
    }

    public static void reportTestRun(){
        initializeIfNotInitialized();
        for(TestSet testSet : TafParallelTestCaseRunner.testSets){
            reporters.evaluateTestSet(testSet);
        }
        TafParallelTestCaseRunner.testSets = new HashSet<>();
        stopTime = new Date();
        reporters.reportTestRun();
        //BaseFolderHtmlIndexFile baseFolderHtmlIndexFile = new BaseFolderHtmlIndexFile();
    }

}
