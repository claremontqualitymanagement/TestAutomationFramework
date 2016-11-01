package se.claremont.autotest.common;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by magnusolsson on 2016-11-01.
 */
public class TestRun_Tests {

    @Test
    public void checkExitCodeTableForSuccessfullyExecution(){
        TestRun.exitCode = TestRun.ExitCodeTable.INIT_OK.getValue();
        Assert.assertTrue("ExitCode status expected to be '" + String.valueOf( TestRun.ExitCodeTable.INIT_OK.getValue() ) + "', but was " + String.valueOf( TestRun.exitCode ), TestRun.exitCode == TestRun.ExitCodeTable.INIT_OK.getValue());
    }

}
