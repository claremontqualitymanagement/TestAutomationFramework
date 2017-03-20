package se.claremont.autotest.common.testrun;

import se.claremont.autotest.common.reporting.testrunreports.TafBackendServerTestRunReporter;
import se.claremont.autotest.common.reporting.testrunreports.TestRunReporterHtmlSummaryReportFile;
import se.claremont.autotest.common.reporting.testrunreports.email.TestRunReporterEmailReport;
import se.claremont.autotest.common.testcase.TestCase;
import se.claremont.autotest.common.testrun.Settings;
import se.claremont.autotest.common.testrun.TestRun;
import se.claremont.autotest.common.testrun.TestRunReporter;
import se.claremont.autotest.common.testset.TestSet;

import java.util.ArrayList;

/**
 * Created by jordam on 2016-09-19.
 */
public class TestRunReporterFactory {
    public ArrayList<TestRunReporter> reporters = new ArrayList<>();

    public TestRunReporterFactory(){
        TestRun.initializeIfNotInitialized();
        reporters.add(new TestRunReporterHtmlSummaryReportFile());
        addTestRunReporterIfNotAlreadyRegistered(new TafBackendServerTestRunReporter());
        if(TestRun.settings.getValue(Settings.SettingParameters.EMAIL_SERVER_ADDRESS) != null){
            addTestRunReporterIfNotAlreadyRegistered(new TestRunReporterEmailReport());
        }
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
        reporters.forEach(TestRunReporter::report);
    }
}
