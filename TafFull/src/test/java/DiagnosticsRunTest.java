import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import se.claremont.autotest.common.testrun.DiagnosticsRun;
import se.claremont.autotest.common.testset.UnitTestClass;

import static org.junit.Assert.assertTrue;

public class DiagnosticsRunTest extends UnitTestClass {
    private DiagnosticsRun diagnosticsRun;

    @Before
    public void setup() {
        diagnosticsRun = new DiagnosticsRun(new JUnitCore());
    }

    @Test
    public void test() {
        diagnosticsRun.run();
        assertTrue("There should not be any errors in the diagnostics",
                diagnosticsRun.getResult().getFailures().isEmpty());
    }
}
