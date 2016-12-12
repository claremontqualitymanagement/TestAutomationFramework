package se.claremont.autotest.common;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by magnusolsson on 2016-11-01.
 */
public class TestRunTest {

    @Test
    public void checkExitCodeTableForSuccessfullyExecution(){
        TestRun.exitCode = TestRun.ExitCodeTable.INIT_OK.getValue();
        assertEquals(TestRun.ExitCodeTable.INIT_OK.getValue(), TestRun.exitCode);
    }

}
