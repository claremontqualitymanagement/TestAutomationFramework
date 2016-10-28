package se.claremont.autotest.common;

import se.claremont.autotest.testmanagementtoolintegration.testlink.TestlinkReporter;

/**
 * A test run is the entity of every time some set(s) of test cases are run.
 * Created by jordam on 2016-08-17.
 */
public class TestRun {
    public int fileCounter = 0;
    public static int exitCode = 0;     //TODO: create exitCode table
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
