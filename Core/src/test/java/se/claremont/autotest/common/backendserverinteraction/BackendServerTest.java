package se.claremont.autotest.common.backendserverinteraction;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;
import se.claremont.autotest.common.logging.LogLevel;
import se.claremont.autotest.common.logging.LogPost;
import se.claremont.autotest.common.reporting.testrunreports.TafBackendServerTestRunReporter;
import se.claremont.autotest.common.testcase.TestCase;
import se.claremont.autotest.common.testrun.Settings;
import se.claremont.autotest.common.testrun.TestRun;
import se.claremont.autotest.common.testset.UnitTestClass;

/**
 * Tests for TAF Backend Server
 *
 * Created by jordam on 2017-03-19.
 */
public class BackendServerTest extends UnitTestClass{
    @SuppressWarnings("WeakerAccess")
    String serverPort = "2222";

    @Test
    public void testVersionGetting(){
        TestRun.setSettingsValue(Settings.SettingParameters.URL_TO_TAF_BACKEND, "http://127.0.0.1:" + serverPort + "/taf");
        TafBackendServerConnection tafBackendServerConnection = new TafBackendServerConnection();
        Assume.assumeTrue("No backend server running at '" + TestRun.getSettingsValue(Settings.SettingParameters.URL_TO_TAF_BACKEND), tafBackendServerConnection.isConnected());
        Assert.assertTrue(tafBackendServerConnection.getBackendVersion().contains("TAF"));
    }

    @Test
    public void testPostingTestCase(){
        TestRun.setSettingsValue(Settings.SettingParameters.URL_TO_TAF_BACKEND, "http://127.0.0.1:" + serverPort + "/taf");
        TafBackendServerConnection tafBackendServerConnection = new TafBackendServerConnection();
        Assume.assumeTrue("No backend server running at '" + TestRun.getSettingsValue(Settings.SettingParameters.URL_TO_TAF_BACKEND), tafBackendServerConnection.isConnected());
        Assert.assertTrue(tafBackendServerConnection.getBackendVersion(), tafBackendServerConnection.getBackendVersion().contains("TAF"));
        TestCase testCase = new TestCase();
        testCase.log(LogLevel.INFO, "My log message");
        String response = tafBackendServerConnection.postTestCase(testCase);
        Assert.assertTrue(response, response.toLowerCase().contains("ok"));
        Assert.assertFalse(response, response.toLowerCase().contains("not"));
    }

    @Test
    public void testPostingLogPost(){
        TestRun.setSettingsValue(Settings.SettingParameters.URL_TO_TAF_BACKEND, "http://127.0.0.1:" + serverPort + "/taf");
        TafBackendServerConnection tafBackendServerConnection = new TafBackendServerConnection();
        Assume.assumeTrue("No backend server running at '" + TestRun.getSettingsValue(Settings.SettingParameters.URL_TO_TAF_BACKEND), tafBackendServerConnection.isConnected());
        Assert.assertTrue(tafBackendServerConnection.getBackendVersion(), tafBackendServerConnection.getBackendVersion().contains("TAF"));
        LogPost logPost = new LogPost(LogLevel.INFO, "My log entry.");
        String response = tafBackendServerConnection.postLogPost(logPost);
        Assert.assertTrue(response, response.toLowerCase().contains("ok"));
        Assert.assertFalse(response, response.toLowerCase().contains("not"));
    }

    @Test
    public void testPostingTestRunResultPost(){
        TestRun.setSettingsValue(Settings.SettingParameters.URL_TO_TAF_BACKEND, "http://127.0.0.1:" + serverPort + "/taf");
        TafBackendServerConnection tafBackendServerConnection = new TafBackendServerConnection();
        Assume.assumeTrue("No backend server running at '" + TestRun.getSettingsValue(Settings.SettingParameters.URL_TO_TAF_BACKEND), tafBackendServerConnection.isConnected());
        Assert.assertTrue(tafBackendServerConnection.getBackendVersion(), tafBackendServerConnection.getBackendVersion().contains("TAF"));
        TafBackendServerTestRunReporter tafBackendServerTestRunReporter = new TafBackendServerTestRunReporter();
        TestCase testCase = new TestCase();
        testCase.log(LogLevel.INFO, "Message");
        testCase.addTestCaseData(" This Parameter", "This Value");
        tafBackendServerTestRunReporter.evaluateTestCase(testCase);
        String response = tafBackendServerConnection.postTestRunResult(tafBackendServerTestRunReporter.toJson());
        Assert.assertTrue(response, response.toLowerCase().contains("ok"));
        Assert.assertFalse(response, response.toLowerCase().contains("not"));
    }
}
