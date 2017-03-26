package se.claremont.autotest.common.testrun;

import se.claremont.autotest.common.testcase.TestCase;
import se.claremont.autotest.common.testset.TestSet;

/**
 * Interface for different types of TestRunReports,
 * for example to HTML files, to TAF Backend Server
 * via REST/JSON, to Testlink via TAF Testlink
 * Adapter Server, or via Email.
 *
 * Created by jordam on 2016-09-19.
 */
public interface TestRunReporter {
    void report();

    void evaluateTestCase(TestCase testCase);

    void evaluateTestSet(TestSet testSet);
}
