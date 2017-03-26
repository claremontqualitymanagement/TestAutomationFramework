package se.claremont.autotest.common.testrun;

import se.claremont.autotest.common.testcase.TestCase;
import se.claremont.autotest.common.testset.TestSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Used for testing of test run reporting
 *
 * Created by jordam on 2017-03-17.
 */
public class FakeEmailTestRunReporter implements TestRunReporter {
    public List<TestCase> testCaseList = new ArrayList<>();
    public List<TestSet> testSetList = new ArrayList<>();
    public int reportingCounts = 0;

    @Override
    public void report() {
        System.out.println("Reporting performed. This is the time _summary.html should have been written and emails would have been sent.");
        reportingCounts++;
    }

    @Override
    public void evaluateTestCase(TestCase testCase) {
        this.testCaseList.add(testCase);
    }

    @Override
    public void evaluateTestSet(TestSet testSet) {
        this.testSetList.add(testSet);
    }
}
