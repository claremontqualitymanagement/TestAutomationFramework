package se.claremont.autotest.common;

import java.util.ArrayList;

/**
 * Created by jordam on 2016-09-19.
 */
public class TestRunReporterFactory {
    protected ArrayList<TestRunReporter> reporters = new ArrayList<>();

    public TestRunReporterFactory(){
        reporters.add(new TestRunReporterHtmlSummaryReportFile());
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
