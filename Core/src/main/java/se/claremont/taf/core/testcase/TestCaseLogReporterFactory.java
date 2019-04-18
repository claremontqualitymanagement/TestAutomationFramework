package se.claremont.taf.core.testcase;

import java.util.ArrayList;

/**
 * Class for dynamic allocation of testCaseLogReporters to runs
 *
 * Created by jordam on 2016-08-31.
 */
@SuppressWarnings("WeakerAccess")
public class TestCaseLogReporterFactory {
    @SuppressWarnings("WeakerAccess")
    ArrayList<TestCaseLogReporter> testCaseLogReporters;

    public TestCaseLogReporterFactory() {
        testCaseLogReporters = new ArrayList<>();
    }

    public void setTestCaseLogReporters(ArrayList<TestCaseLogReporter> testCaseLogReporters){
        this.testCaseLogReporters = testCaseLogReporters;
    }

}
