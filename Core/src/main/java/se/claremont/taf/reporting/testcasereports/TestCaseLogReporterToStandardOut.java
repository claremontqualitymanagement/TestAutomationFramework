package se.claremont.taf.reporting.testcasereports;

import se.claremont.taf.logging.LogPost;
import se.claremont.taf.testcase.TestCaseLogReporter;
import se.claremont.taf.testcase.TestCaseResult;

/**
 * Non-file based log reporter for test case logs. Writes the log to standard out.
 *
 * Created by jordam on 2016-09-18.
 */
public class TestCaseLogReporterToStandardOut implements TestCaseLogReporter {
    private final TestCaseResult testCaseResult;

    @SuppressWarnings("unused")
    public TestCaseLogReporterToStandardOut(TestCaseResult testCaseResult) {
        this.testCaseResult = testCaseResult;
    }

    @SuppressWarnings("unused")
    public void report(){
        for(LogPost logPost : testCaseResult.testCaseLog.logPosts){
            System.out.println(logPost.toString());
        }
    }
}
