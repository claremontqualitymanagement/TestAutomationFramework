package se.claremont.autotest.common;

import org.junit.Assert;
import org.junit.Test;
import se.claremont.autotest.common.reporting.testrunreports.TestRunReporterHtmlSummaryReportFile;
import se.claremont.autotest.common.testrun.TestRun;
import se.claremont.autotest.common.testrun.TestRunReporter;
import se.claremont.autotest.common.testrun.TestRunReporterFactory;
import se.claremont.autotest.common.testset.UnitTestClass;

/**
 * Tests the test run reporter factory
 *
 * Created by jordam on 2017-01-04.
 */
public class TestRunReporterFactoryTest extends UnitTestClass{

    @Test
    public void duplicateTestRunReporterAddedUsingUniqeMethodShouldNotCreateDuplicate(){
        TestRun.initializeIfNotInitialized();
        TestRunReporterFactory t = new TestRunReporterFactory();
        t.addTestRunReporterIfNotAlreadyRegistered(new TestRunReporterHtmlSummaryReportFile());
        for(TestRunReporter testRunReporter : t.reporters){
            System.out.println(testRunReporter.getClass().getName());
        }
        Assert.assertTrue("Expected 4 items (default reporters counted for) but found " + t.reporters.size(), t.reporters.size() == 4);

    }
}
