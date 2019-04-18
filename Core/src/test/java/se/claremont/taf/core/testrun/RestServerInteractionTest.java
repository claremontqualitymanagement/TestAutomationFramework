package se.claremont.taf.core.testrun;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import se.claremont.taf.core.logging.LogLevel;
import se.claremont.taf.core.logging.LogPost;
import se.claremont.taf.core.reporting.testrunreports.TafBackendServerTestRunReporter;
import se.claremont.taf.core.testcase.TestCase;
import se.claremont.taf.core.testrun.reportingengine.TestRunReporter;
import se.claremont.taf.core.testset.UnitTestClass;

/**
 * Tests interaction with REST server
 *
 * Created by jordam on 2017-03-19.
 */
public class RestServerInteractionTest extends UnitTestClass{

    @Test
    public void testCaseToJsonTest(){
        TestCase testCase = new TestCase(null, "New TestCase name");
        testCase.addKnownError("Known error description", "Known error regular expression pattern");
        testCase.addTestCaseData("Test case data parameter", "Test case data value");
        testCase.log(LogLevel.INFO, "Message 1");
        testCase.log(LogLevel.DEBUG, "Message 2");
        String json = testCase.toJson();
        Assert.assertNotNull(json);
        Assert.assertTrue(json, json.contains("Test case data parameter"));
        Assert.assertTrue(json, json.contains("Test case data value"));
        Assert.assertTrue(json, json.contains("Message 1"));
        Assert.assertTrue(json, json.contains("Message 2"));
        Assert.assertTrue(json, json.contains("New TestCase name"));
        Assert.assertTrue(json, json.contains("Known error description"));
        Assert.assertTrue(json, json.contains("Known error regular expression pattern"));
    }

    @Test
    @Ignore
    public void testRunResultToJsonTest(){
        TestRun.setSettingsValue(Settings.SettingParameters.URL_TO_TAF_BACKEND, "http://phonyaddress.org");
        TestCase testCase = new TestCase(null, "My testCase name");
        testCase.report();
        boolean runReporterFound = false;
        for(TestRunReporter testRunReporter : TestRun.getReporterFactory().reporters){
            if(testRunReporter.getClass() == TafBackendServerTestRunReporter.class){
                runReporterFound = true;
                String json = ((TafBackendServerTestRunReporter)testRunReporter).toJson();
                Assert.assertNotNull(json);
                Assert.assertTrue(json, json.contains("My testCase name"));
                Assert.assertTrue(json, json.contains("settings"));
            }
        }
        Assert.assertTrue(runReporterFound);
        TestRun.reportTestRun();

    }

    @Test
    public void logPostAsJsonTest(){
        LogPost logPost = new LogPost(LogLevel.INFO, "My message", "", "", "", "");
        Assert.assertNotNull(logPost);
        Assert.assertNotNull(logPost.toJson());
        Assert.assertTrue(logPost.toJson(), logPost.toJson().contains("My message"));
        Assert.assertTrue(logPost.toJson(), logPost.toJson().toLowerCase().contains("info"));
    }
}
