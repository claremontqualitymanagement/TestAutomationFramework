package se.claremont.autotest.common;

//import se.claremont.autotest.testmanagementtoolintegration.testlink.TestlinkReporter;

/**
 * A test run is the entity of every time some set(s) of test cases are run.
 * Created by jordam on 2016-08-17.
 */
public class TestRun {
    public static final Settings settings = new Settings();
    public static int fileCounter = 0;
    public static String testRunName = "";
    public static int exitCode;
    public static TestSet currentTestSet;
    public static final TestRunReporterFactory reporters = new TestRunReporterFactory();

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

    public static void initialize() {
        currentTestSet = null;
        exitCode = ExitCodeTable.INIT_OK.getValue();
    }

    public void report(){
        reporters.report();
    }

    public static String reportLinkPrefix(){
        if(TestRun.settings.getCustomValue("HtmlSummaryReportLinkPrefix").toLowerCase().contains("http")){
            return "http";
        } else if (TestRun.settings.getCustomValue("HtmlSummaryReportLinkPrefix").toLowerCase().contains("https")){
            return "https";
        }
        return "file";
    }

}
