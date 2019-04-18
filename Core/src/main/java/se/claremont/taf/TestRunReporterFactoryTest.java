package se.claremont.taf;

import org.junit.Assert;
import org.junit.Test;
import se.claremont.taf.reporting.testrunreports.TestRunReporterHtmlSummaryReportFile;
import se.claremont.taf.testrun.reportingengine.TestRunReporter;
import se.claremont.taf.testrun.reportingengine.TestRunReporterFactory;
import se.claremont.taf.testset.UnitTestClass;

/**
 * Tests the test run reporter factory
 *
 * Created by jordam on 2017-01-04.
 */
public class TestRunReporterFactoryTest extends UnitTestClass{

    @Test
    public void duplicateTestRunReporterAddedUsingUniqueMethodShouldNotCreateDuplicate(){
        TestRunReporterFactory t = new TestRunReporterFactory();
        t.addTestRunReporterIfNotAlreadyRegistered(new TestRunReporterHtmlSummaryReportFile());
        for(TestRunReporter testRunReporter : t.reporters){
            System.out.println(testRunReporter.getClass().getName());
        }
        Assert.assertTrue("Expected 4 items (default reporters counted for) but found " + t.reporters.size(), t.reporters.size() == 4);

    }
}
