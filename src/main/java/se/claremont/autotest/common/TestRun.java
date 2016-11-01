package se.claremont.autotest.common;

//import se.claremont.autotest.testmanagementtoolintegration.testlink.TestlinkReporter;

/**
 * A test run is the entity of every time some set(s) of test cases are run.
 * Created by jordam on 2016-08-17.
 */
public class TestRun {
    public int fileCounter = 0;

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
    public static int exitCode = ExitCodeTable.INIT_OK.getValue();

    public TestSet currentTestSet = null;
    public static final Settings settings = new Settings();
    public TestRunReporterFactory reporters = new TestRunReporterFactory();

    TestRun(){
        reporters.addTestRunReporter(new TestRunReporterHtmlSummaryReportFile());
        reporters.addTestRunReporter(new TestRunReporterEmailReport());
        //reporters.addTestRunReporter(new TestlinkReporter());
        //RestServer.start();
    }

}
