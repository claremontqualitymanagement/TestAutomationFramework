package se.claremont.autotest.common;

/**
 * Non-file based log reporter for test case logs. Writes the log to standard out.
 *
 * Created by jordam on 2016-09-18.
 */
class TestCaseLogReporterToStandardOut {
    private final TestCase testCase;

    @SuppressWarnings("unused")
    TestCaseLogReporterToStandardOut(TestCase testCase){
        this.testCase = testCase;
    }

    @SuppressWarnings("unused")
    public void report(){
        System.out.println("Log for test case '" + testCase.testName + "' in test set '" + testCase.testSetName + "':");
        for(LogPost logPost : testCase.testCaseLog.logPosts){
            System.out.println(logPost.toString());
        }
    }
}
