package se.claremont.autotest.common.reporting.testrunreports;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import se.claremont.autotest.common.logging.LogLevel;
import se.claremont.autotest.common.testcase.TestCase;
import se.claremont.autotest.common.testrun.Settings;
import se.claremont.autotest.common.testrun.TestRun;
import se.claremont.autotest.common.testrun.TestSet1;
import se.claremont.autotest.common.testrun.TestSet2;
import se.claremont.autotest.common.testset.UnitTestClass;

/**
 * Tests for interaction with a TAF Backend Server
 *
 * Created by jordam on 2017-03-20.
 */
public class TafBackendTestRunReporterTest extends UnitTestClass {

    @Before
    public void testSetup(){
        TestRun.setSettingsValue(Settings.SettingParameters.URL_TO_TAF_BACKEND, "http://anyserver:80/taf");
    }

    @Test
    public void testRunNameIsSetInJson(){
        TafBackendServerTestRunReporter tafBackendServerTestRunReporter = new TafBackendServerTestRunReporter();
        TestSet1 testSet1 = new TestSet1();
        tafBackendServerTestRunReporter.evaluateTestSet(testSet1);
        Assert.assertTrue(tafBackendServerTestRunReporter.testSetNames.contains(testSet1.name));
        TestSet2 testSet2 = new TestSet2();
        tafBackendServerTestRunReporter.evaluateTestSet(testSet2);
        Assert.assertTrue(tafBackendServerTestRunReporter.testSetNames.contains(testSet2.name));
        tafBackendServerTestRunReporter.report();
        if(TestRun.testRunName == null || TestRun.testRunName.length() == 0){
            Assert.assertTrue("Expected testRunName to include '" + testSet1.name + "', but it was '" + tafBackendServerTestRunReporter.testRunName + "'.", tafBackendServerTestRunReporter.testRunName.contains(testSet1.name));
            Assert.assertTrue("Expected testRunName to include '" + testSet2.name + "', but it was '" + tafBackendServerTestRunReporter.testRunName + "'.", tafBackendServerTestRunReporter.testRunName.contains(testSet2.name));
        } else {
            Assert.assertTrue("Expected testRunName to include '" + TestRun.testRunName + "', but it was '" + tafBackendServerTestRunReporter.testRunName + "'.", tafBackendServerTestRunReporter.testRunName.contains(TestRun.testRunName));
        }
    }

    @Test
    public void knownTestCaseErrorsReported(){
        TafBackendServerTestRunReporter tafBackendServerTestRunReporter = new TafBackendServerTestRunReporter();
        TestSet1 testSet1 = new TestSet1();
        TestCase testCase1 = new TestCase();
        testCase1.addKnownError("description", ".*pattern.*");
        testCase1.log(LogLevel.INFO, "dummy");
        testCase1.log(LogLevel.VERIFICATION_FAILED, "pattern");
        testCase1.log(LogLevel.INFO, "dummy");
        testCase1.testCaseResult.evaluateResultStatus();
        Assert.assertTrue("testCase1 json: " + testCase1.toJson(), testCase1.toJson().contains("identifiedToBePartOfKnownError\" : true"));
        tafBackendServerTestRunReporter.evaluateTestCase(testCase1);
        TestCase testCase2 = new TestCase();
        testCase2.addKnownError("description", ".*pattern.*");
        testCase2.log(LogLevel.INFO, "dummy");
        testCase1.testCaseResult.evaluateResultStatus();
        tafBackendServerTestRunReporter.evaluateTestCase(testCase2);
        tafBackendServerTestRunReporter.evaluateTestSet(testSet1);

    }
}
