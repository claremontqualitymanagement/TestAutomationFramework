package se.claremont.autotest.common.reporting.testrunreports;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.junit.Assert;
import org.junit.Test;
import se.claremont.autotest.common.testcase.TestCase;
import se.claremont.autotest.common.testrun.TestRun;
import se.claremont.autotest.common.testrun.TestSet1;
import se.claremont.autotest.common.testrun.TestSet2;

/**
 * Created by jordam on 2017-03-20.
 */
public class TafBackendTestRunReporterTest {

    @Test
    public void testRunIdIsSetInJson(){
        TafBackendServerTestRunReporter tafBackendServerTestRunReporter = new TafBackendServerTestRunReporter();
        TestSet1 testSet1 = new TestSet1();
        tafBackendServerTestRunReporter.evaluateTestSet(testSet1);
        TestCase testCase = new TestCase();
        tafBackendServerTestRunReporter.evaluateTestCase(testCase);
        String testRunId = TestRun.testRunId.toString();
        Assert.assertTrue(tafBackendServerTestRunReporter.toJson().contains(testRunId));
        tafBackendServerTestRunReporter.report();
        Assert.assertTrue(tafBackendServerTestRunReporter.testRunId.equals(testRunId));
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
        Assert.assertTrue(tafBackendServerTestRunReporter.testRunName.contains(testSet1.name));
        Assert.assertTrue(tafBackendServerTestRunReporter.testRunName.contains(testSet2.name));
    }
}
