package se.claremont.autotest.common.reporting.testrunreports;

import org.junit.Assert;
import org.junit.Test;
import se.claremont.autotest.common.logging.LogLevel;
import se.claremont.autotest.common.testcase.TestCase;
import se.claremont.autotest.common.testset.TestSet;

/**
 * Created by jordam on 2017-04-09.
 */
public class TestRunHtmlSummaryReportFileTestInTestSet extends TestSet {

    @Test
    public void reportDesignTest(){
        TestRunReporterHtmlSummaryReportFile report = new TestRunReporterHtmlSummaryReportFile();
        TestCase testCase1 = new TestCase(null, "TestCase1");
        testCase1.log(LogLevel.INFO, "Text");
        //Framework error is the most severe type of error
        testCase1.log(LogLevel.VERIFICATION_FAILED, "SharedLogRow '234' found. Duration 123 milliseconds.");
        testCase1.log(LogLevel.VERIFICATION_FAILED, "UnSharedLogRow");
        testCase1.evaluateResultStatus();
        report.evaluateTestCase(testCase1);
        TestCase testCase2 = new TestCase(null, "TestCase2");
        testCase2.log(LogLevel.VERIFICATION_FAILED, "SharedLogRow '354' found. Duration 7643 milliseconds.");
        testCase2.evaluateResultStatus();
        report.evaluateTestCase(testCase2);
        String reportContent = report.htmlSummaryReport.createReport();
        System.out.println(reportContent);
        Assert.assertTrue(reportContent.contains("sharedlogposts"));
        Assert.assertTrue(reportContent.contains("TestCase1"));
        Assert.assertTrue(reportContent.contains("TestCase2"));
        report.report();
    }
}
