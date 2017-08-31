package se.claremont.autotest.common.testrun;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import se.claremont.autotest.common.testset.UnitTestClass;

public class ParallelExecutionTest {

    @Test
    public void parallelExecutionTestCase1() {
        System.out.println("Starting test 1");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Ending test 1");
        Assert.assertTrue(true);
    }

    @Test
    public void parallelExecutionTestCase2() {
        System.out.println("Starting test 2");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Ending test 2");
        Assert.assertTrue(true);
    }
}
