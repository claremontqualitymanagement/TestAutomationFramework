package se.claremont.autotest.common.testrun;

import se.claremont.autotest.common.testcase.TestCase;
import se.claremont.autotest.common.testset.TestSet;

/**
 * Created by jordam on 2016-09-19.
 */
public interface TestRunReporter {
    void report();

    void evaluateTestCase(TestCase testCase);

    void evaluateTestSet(TestSet testSet);
}
