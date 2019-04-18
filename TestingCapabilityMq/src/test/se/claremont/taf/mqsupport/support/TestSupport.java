package se.claremont.taf.mqsupport.support;

import org.junit.Assert;
import se.claremont.taf.common.logging.LogPost;
import se.claremont.taf.common.support.SupportMethods;
import se.claremont.taf.common.testcase.TestCase;

public class TestSupport {

    public static void assertTestCaseLog(TestCase testCase, LogPost logPost){
        for(LogPost savedLogPost : testCase.testCaseResult.testCaseLog.logPosts){
            if(!savedLogPost.logLevel.equals(logPost.logLevel)) continue;
            if(!SupportMethods.isRegexMatch(savedLogPost.message, logPost.message)) continue;
            return;
        }
        Assert.assertTrue("LogPost not found '" + logPost.toString() + "' in log:" + System.lineSeparator() + testCase.testCaseResult.testCaseLog.toString(), false);
    }
}
