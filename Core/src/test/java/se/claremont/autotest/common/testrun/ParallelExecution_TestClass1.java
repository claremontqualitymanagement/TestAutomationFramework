package se.claremont.autotest.common.testrun;

import org.junit.Assert;
import org.junit.Test;
import se.claremont.autotest.common.testset.TestSet;

public class ParallelExecution_TestClass1 extends TestSet {

    @Test
    public void parallelExecutionTestCase1() {
        System.out.println("Starting class 1, test 1");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            System.out.println(e.toString());
        }
        System.out.println("Ending class 1, test 1");
        Assert.assertTrue(true);
    }

    @Test
    public void parallelExecutionTestCase2() {
        System.out.println("Starting class 1, test 2");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            System.out.println(e.toString());
        }
        System.out.println("Ending class 1, test 2");
        Assert.assertTrue(true);
    }
}
