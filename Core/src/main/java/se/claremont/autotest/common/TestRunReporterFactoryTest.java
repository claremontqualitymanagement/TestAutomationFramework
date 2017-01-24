package se.claremont.autotest.common;

import org.junit.Assert;
import org.junit.Test;
import se.claremont.autotest.common.reporting.testrunreports.TestRunReporterHtmlSummaryReportFile;
import se.claremont.autotest.common.testrun.TestRunReporterFactory;

/**
 * Created by jordam on 2017-01-04.
 */
public class TestRunReporterFactoryTest {

    @Test
    public void duplicateTestRunReporterAddedUsingUniqeMethodShouldNotCreateDuplicate(){
        TestRunReporterFactory t = new TestRunReporterFactory();
        t.addTestRunReporterIfNotAlreadyRegistered(new TestRunReporterHtmlSummaryReportFile());
        Assert.assertTrue("Expected 2 items (default reporters counted for) but found " + t.reporters.size(), t.reporters.size() == 2);
    }
}
