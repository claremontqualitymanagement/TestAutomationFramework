package se.claremont.taf.reporting.testrunreports;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import se.claremont.taf.logging.LogLevel;
import se.claremont.taf.testcase.TestCase;
import se.claremont.taf.testcase.TestCaseResult;
import se.claremont.taf.testset.UnitTestClass;

/**
 * Tests for the KioskMode
 *
 * Created by jordam on 2016-12-22.
 */
public class KioskModeReportTest extends UnitTestClass{

    @Test
    public void evaluateTestCase(){
        KioskModeReport kioskDisplay = new KioskModeReport();
        TestCase testCase = new TestCase(null, "dummy");
        testCase.log(LogLevel.VERIFICATION_PASSED, "text");
        Assert.assertTrue(testCase.testCaseResult.resultStatus.equals(TestCaseResult.ResultStatus.UNEVALUATED));
        kioskDisplay.evaluateTestCase(testCase);
        Assert.assertTrue(testCase.testCaseResult.resultStatus.equals(TestCaseResult.ResultStatus.PASSED));
    }

    @Test
    @Ignore
    public void reportBeingCreated(){
        KioskModeReport kioskDisplay = new KioskModeReport();
        TestCase testCase = new TestCase(null, "dummy");
        testCase.log(LogLevel.VERIFICATION_PASSED, "text");
        Assert.assertTrue(testCase.testCaseResult.resultStatus.equals(TestCaseResult.ResultStatus.UNEVALUATED));
        kioskDisplay.evaluateTestCase(testCase);
        Assert.assertTrue(testCase.testCaseResult.resultStatus.equals(TestCaseResult.ResultStatus.PASSED));
        kioskDisplay.create("C:\\temp\\reportTestRun.html", "Dummy tests", 4);
        kioskDisplay.openInDefaultBrowser();
    }
}
