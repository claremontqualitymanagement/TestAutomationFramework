package se.claremont.taf.core.testrun;

import org.junit.Assert;
import org.junit.Test;
import se.claremont.taf.core.testset.TestSet;

public class ParallelExecution_TestClass2 extends TestSet{

    @Test
    public void parallelExecutionTestCase3() {
        System.out.println("Starting class 2, test 1");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            System.out.println(e.toString());
        }
        System.out.println("Ending class 2, test 1");
        Assert.assertTrue(true);
    }

    @Test
    public void parallelExecutionTestCase4() {
        System.out.println("Starting class 2, test 2");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            System.out.println(e.toString());
        }
        System.out.println("Ending clas 2, test 2");
        Assert.assertTrue(true);
    }
}
