package se.claremont.autotest;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import se.claremont.autotest.common.LogLevel;
import se.claremont.autotest.common.TestCase;

/**
 * Created by jordam on 2016-12-22.
 */
public class KioskModeReportTest {

    @Test
    public void evaluateTestCase(){
        KioskModeReport kioskDisplay = new KioskModeReport();
        TestCase testCase = new TestCase(null, "dummy");
        testCase.log(LogLevel.VERIFICATION_PASSED, "text");
        Assert.assertTrue(testCase.resultStatus.equals(TestCase.ResultStatus.UNEVALUATED));
        kioskDisplay.evaluateTestCase(testCase);
        Assert.assertTrue(testCase.resultStatus.equals(TestCase.ResultStatus.PASSED));
    }

    @Test
    @Ignore
    public void reportBeingCreated(){
        KioskModeReport kioskDisplay = new KioskModeReport();
        TestCase testCase = new TestCase(null, "dummy");
        testCase.log(LogLevel.VERIFICATION_PASSED, "text");
        Assert.assertTrue(testCase.resultStatus.equals(TestCase.ResultStatus.UNEVALUATED));
        kioskDisplay.evaluateTestCase(testCase);
        Assert.assertTrue(testCase.resultStatus.equals(TestCase.ResultStatus.PASSED));
        kioskDisplay.create("C:\\temp\\report.html", "Dummy tests", 4);
        kioskDisplay.openInDefaultBrowser();
    }
}
