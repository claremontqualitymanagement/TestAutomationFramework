package se.claremont.taf.core.testrun;

import se.claremont.taf.core.junitcustomization.TafParallelTestCaseRunner;
import se.claremont.taf.core.logging.ConsoleLogLevel;
import se.claremont.taf.core.testrun.reportingengine.TestRunReporter;
import se.claremont.taf.core.testrun.reportingengine.TestRunReporterFactory;
import se.claremont.taf.core.testset.TestSet;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 * A test run is the entity of every time some set(s) of test cases are run.
 * Created by jordam on 2016-08-17.
 */
@SuppressWarnings("WeakerAccess")
public class TestRun {
    public Settings settings = new Settings();
    public int fileCounter = 0;
    public String testRunName = "TAFTestRun";
    public int exitCode = ExitCodeTable.INIT_OK.getValue();
    public List<TestSet> currentTestSets = new ArrayList<>();
    public final TestRunReporterFactory reporters = new TestRunReporterFactory();
    @SuppressWarnings("CanBeFinal")
    public ConsoleLogLevel consoleLogLevel = ConsoleLogLevel.MODERATE;
    public final Date startTime = new Date();
    public Date stopTime;

    private static TestRun instance;

    private TestRun(){
        TafParallelTestCaseRunner.testSets = new HashSet<>();
    }

    static TestRun getInstance(){
        if(instance == null){
            synchronized (TestRun.class) {
                if(instance == null){
                    instance = new TestRun();
                }
            }
        }
        return instance;
    }

    public static Settings getSettings() {
        return getInstance().settings;
    }

    public static String getRunName(){
        return getInstance().testRunName;
    }

    public static int getExitCode() {
        return getInstance().exitCode;
    }

    public static void addTestRunReporterIfNotAlreadyRegistered(TestRunReporter reporter) {
        getInstance().reporters.addTestRunReporterIfNotAlreadyRegistered(reporter);
    }

    public static void addTestRunReporter(TestRunReporter reporter) {
        getInstance().reporters.addTestRunReporter(reporter);
    }

    public static void clearReporterFactory(){
        getInstance().reporters.reporters.clear();
    }

    //For testing purposes
    public static void reloadSettings() {
        getInstance().settings = new Settings();
    }

    //For testing purposes
    public static void setSettings(Settings settings) {
        getInstance().settings = settings;
    }

    public static int getFileCounter() {
        return getInstance().fileCounter;
    }

    public static void increaseFileCounter(){
        getInstance().fileCounter++;
    }

    private static class TestRunSingletonHelper{
        public static final TestRun INSTANCE = new TestRun();
    }

    public static ConsoleLogLevel getConsoleLogLevel(){
        return getInstance().consoleLogLevel;
    }

    public static TestRunReporterFactory getReporterFactory(){
        return getInstance().reporters;
    }

    public static void setExitCode(int exitCodeValue){
        getInstance().exitCode = exitCodeValue;
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
        return getInstance().settings.getValue(parameter);
    }

    @SuppressWarnings("SameParameterValue")
    public static String getCustomSettingsValue(String parameter){
        return getInstance().settings.getCustomValue(parameter);
    }

    public static Date getStartTime() {
        return getInstance().startTime;
    }

    public static Date getStopTime(){
        return getInstance().stopTime;
    }

    public static void setStopTime(Date stopTime){
        getInstance().stopTime = stopTime;
    }

    public static void setSettingsValue(Settings.SettingParameters parameter, String value){
        getInstance().settings.setValue(parameter, value);
    }

    public static void setCustomSettingsValue(String parameter, String value){
        getInstance().settings.setCustomValue(parameter, value);
    }

    public static String reportLinkPrefix(){
        if(getInstance().settings.getValue(Settings.SettingParameters.HTML_REPORTS_LINK_PREFIX) == null ||
                getInstance().settings.getValue(Settings.SettingParameters.HTML_REPORTS_LINK_PREFIX).toLowerCase().equals("file")) return "file";
        if(getInstance().settings.getValue(Settings.SettingParameters.HTML_REPORTS_LINK_PREFIX).toLowerCase().equals("http")){
            return "http";
        } else if (getInstance().settings.getValue(Settings.SettingParameters.HTML_REPORTS_LINK_PREFIX).toLowerCase().equals("https")){
            return "https";
        }
        return getInstance().settings.getValue(Settings.SettingParameters.HTML_REPORTS_LINK_PREFIX);
    }

    public static void reportTestRun(){
        for(TestSet testSet : TafParallelTestCaseRunner.testSets){
            getInstance().reporters.evaluateTestSet(testSet);
        }
        TafParallelTestCaseRunner.testSets = new HashSet<>();
        getInstance().stopTime = new Date();
        getInstance().reporters.reportTestRun();
        //BaseFolderHtmlIndexFile baseFolderHtmlIndexFile = new BaseFolderHtmlIndexFile();
    }

}
