package se.claremont.taf.core.reporting.testcasereports;

import org.junit.Assert;
import org.junit.Test;
import se.claremont.taf.core.logging.LogLevel;
import se.claremont.taf.core.testcase.TestCase;
import se.claremont.taf.core.testset.UnitTestClass;

public class TestCaseHtmlReportTests extends UnitTestClass {
    @Test
    public void initialize(){
        TestCase testCase = new TestCase();
        TestCaseLogReporterHtmlLogFile testCaseLogReporterHtmlLogFile = new TestCaseLogReporterHtmlLogFile(testCase.testCaseResult);
        Assert.assertNotNull(testCaseLogReporterHtmlLogFile);
        String content = testCaseLogReporterHtmlLogFile.createReport();
        System.out.println(content);
        Assert.assertNotNull(content);
        Assert.assertTrue(content.contains("Nameless test"));
    }

    @Test
    public void identifySections(){
        TestCase testCase = new TestCase();
        testCase.testCaseResult.testCaseLog.log(LogLevel.INFO, "Testing", "<br>Testing", "Noname testcase", "Step1", "TestClass");
        testCase.testCaseResult.testCaseLog.log(LogLevel.INFO, "Testing", "<br>Testing", "Noname testcase", "Step1", "TestClass");
        TestCaseLogReporterHtmlLogFile testCaseLogReporterHtmlLogFile = new TestCaseLogReporterHtmlLogFile(testCase.testCaseResult);
        testCaseLogReporterHtmlLogFile.testStepLogPostSections();
    }

    @Test
    public void testCaseLogSections(){
        TestCase testCase = new TestCase();
        testCase.testCaseResult.testCaseLog.log(LogLevel.INFO, "Testing", "<br>Testing", "Noname testcase", "Step1", "TestClass");
        testCase.testCaseResult.testCaseLog.log(LogLevel.INFO, "Testing", "<br>Testing", "Noname testcase", "Step1", "TestClass");
        Assert.assertTrue(testCase.testCaseResult.testCaseLog.toLogSections().size() == 2);
    }

}
