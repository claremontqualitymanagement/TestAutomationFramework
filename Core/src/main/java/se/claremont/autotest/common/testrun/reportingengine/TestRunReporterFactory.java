package se.claremont.autotest.common.testrun.reportingengine;

import se.claremont.autotest.common.backendserverinteraction.TafBackendServerConnection;
import se.claremont.autotest.common.backendserverinteraction.TestlinkAdapterServerConnection;
import se.claremont.autotest.common.reporting.testrunreports.TafBackendServerTestRunReporter;
import se.claremont.autotest.common.reporting.testrunreports.TestRunReporterHtmlSummaryReportFile;
import se.claremont.autotest.common.reporting.testrunreports.TestlinkAdapterTestRunReporter;
import se.claremont.autotest.common.reporting.testrunreports.email.TestRunReporterEmailReport;
import se.claremont.autotest.common.testcase.TestCase;
import se.claremont.autotest.common.testrun.Settings;
import se.claremont.autotest.common.testrun.TestRun;
import se.claremont.autotest.common.testset.TestSet;

import java.util.ArrayList;

/**
 * Manages all active TestRunReporters for a test run
 *
 * Created by jordam on 2016-09-19.
 */
public class TestRunReporterFactory {
    public ArrayList<TestRunReporter> reporters = new ArrayList<>();

    public TestRunReporterFactory(){
        reporters.add(new TestRunReporterHtmlSummaryReportFile());
        addTestRunReporterIfNotAlreadyRegistered(new TafBackendServerTestRunReporter());
        addTestRunReporterIfNotAlreadyRegistered(new TestlinkAdapterTestRunReporter());
        addTestRunReporterIfNotAlreadyRegistered(new TestRunReporterEmailReport());
    }

    public void addTestRunReporterIfNotAlreadyRegistered(TestRunReporter testRunReporter){
        for(TestRunReporter reporter : reporters){
            if(reporter.getClass().equals(testRunReporter.getClass())){
                return;
            }
        }
        reporters.add(testRunReporter);
    }


    public void addTestRunReporter(TestRunReporter testRunReporter){
        reporters.add(testRunReporter);
    }

    public void evaluateTestCase(TestCase testCase){
        for(TestRunReporter testRunReporter : reporters){
            testRunReporter.evaluateTestCase(testCase);
        }
    }

    public void evaluateTestSet(TestSet testSet){
        for(TestRunReporter testRunReporter : reporters){
            testRunReporter.evaluateTestSet(testSet);
        }
    }

    public void reportTestRun(){
        for(TestRunReporter testRunReporter : reporters){
            testRunReporter.report();
        }
    }
}
