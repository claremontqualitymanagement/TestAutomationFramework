package se.claremont.autotest.common.testrun;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.notification.Failure;
import se.claremont.autotest.common.testset.UnitTestClass;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class DiagnosticsRunTest extends UnitTestClass{
    private DiagnosticsRun diagnosticsRun;

    @Before
    public void setup() {
        diagnosticsRun = new DiagnosticsRun(new JUnitCore());
    }

    @Test
    public void test_RunTests() {
        try {
            SuppressTestOutputFromConsoleOutput.redirectOutputChannel();
            diagnosticsRun.run();
            SuppressTestOutputFromConsoleOutput.restoreOutputChannel();
            for(Failure testFailure: diagnosticsRun.getResult().getFailures()) {
                System.out.println(testFailure.toString());
            }
            assertTrue("There should not be any failures from diagnosticsrun: " + diagnosticsRun.getResult().getFailures(), diagnosticsRun.getResult().getFailures().isEmpty());
        } catch (Exception e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }
}
