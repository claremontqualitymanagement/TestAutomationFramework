package se.claremont.taf.core.reporting.testrunreports;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import se.claremont.taf.core.logging.LogLevel;
import se.claremont.taf.core.testcase.TestCase;
import se.claremont.taf.core.testrun.Settings;
import se.claremont.taf.core.testrun.TestRun;
import se.claremont.taf.core.testset.TestSet;
import se.claremont.taf.core.testset.UnitTestClass;
import se.claremont.taf.testhelpers.ResourceManager;

/**
 * Tests for interaction with a TAF Backend Server
 *
 * Created by jordam on 2017-03-20.
 */
@SuppressWarnings("unchecked")
public class TafBackendTestRunReporterTest extends UnitTestClass {

    @Before
    public void testSetup(){
        TestRun.setSettingsValue(Settings.SettingParameters.URL_TO_TAF_BACKEND, "http://anyserver:80/taf");
    }

    @Test
    public void testRunNameIsSetInJson() throws IllegalAccessException, InstantiationException {
        TafBackendServerTestRunReporter tafBackendServerTestRunReporter = new TafBackendServerTestRunReporter();
        Class<TestSet> testSet1 = ResourceManager.extractFileFromResourcesAndCompileAndLoadIt("TestSet1.java");
        tafBackendServerTestRunReporter.evaluateTestSet(testSet1.newInstance());
        Assert.assertTrue(tafBackendServerTestRunReporter.testSetNames.contains(testSet1.getName()));
        Class<TestSet> testSet2 = ResourceManager.extractFileFromResourcesAndCompileAndLoadIt("TestSet2.java");
        tafBackendServerTestRunReporter.evaluateTestSet(testSet2.newInstance());
        Assert.assertTrue(testSet2.getName(), String.join("", tafBackendServerTestRunReporter.testSetNames).contains(testSet2.getName()));
        tafBackendServerTestRunReporter.report();
        if(TestRun.getRunName() == null || TestRun.getRunName().length() == 0){
            Assert.assertTrue("Expected testRunName to include '" + testSet1.getName()+ "', but it was '" + tafBackendServerTestRunReporter.testRunName + "'.", tafBackendServerTestRunReporter.testRunName.contains(testSet1.getName()));
            Assert.assertTrue("Expected testRunName to include '" + testSet2.getName() + "', but it was '" + tafBackendServerTestRunReporter.testRunName + "'.", tafBackendServerTestRunReporter.testRunName.contains(testSet2.getName()));
        } else {
            Assert.assertTrue("Expected testRunName to include '" + TestRun.getRunName() + "', but it was '" + tafBackendServerTestRunReporter.testRunName + "'.", tafBackendServerTestRunReporter.testRunName.contains(TestRun.getRunName()));
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

    class TestSet1 extends TestSet{

    }
}
