package se.claremont.taf.core.testrun.reportingengine;

import se.claremont.taf.core.testcase.TestCase;
import se.claremont.taf.core.testset.TestSet;

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
