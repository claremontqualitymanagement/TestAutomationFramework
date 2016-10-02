package se.claremont.autotest.common;

/**
 * Created by jordam on 2016-09-19.
 */
interface TestRunReporter {
    void report();

    void evaluateTestCase(TestCase testCase);

    void evaluateTestSet(TestSet testSet);
}
