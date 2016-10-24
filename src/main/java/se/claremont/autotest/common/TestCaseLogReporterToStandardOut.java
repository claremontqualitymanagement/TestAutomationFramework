package se.claremont.autotest.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.claremont.tools.Utils;

/**
 * Non-file based log reporter for test case logs. Writes the log to standard out.
 *
 * Created by jordam on 2016-09-18.
 */
class TestCaseLogReporterToStandardOut {
    private final static Logger logger = LoggerFactory.getLogger( TestCaseLogReporterToStandardOut.class );
    private final TestCase testCase;

    @SuppressWarnings("unused")
    TestCaseLogReporterToStandardOut(TestCase testCase){
        this.testCase = testCase;
    }

    @SuppressWarnings("unused")
    public void report(){
        logger.debug( "Log for test case '" + testCase.testName + "' in test set '" + testCase.testSetName + "':" );
        for(LogPost logPost : testCase.testCaseLog.logPosts){
            System.out.println(logPost.toString());
        }
    }
}
