package test;

import org.junit.Assert;
import org.junit.Test;
import se.claremont.autotest.common.testset.TestSet;

public class PassingTestClass extends TestSet {

    @Test
    public void test1() {
        Assert.assertTrue(true);
    }
}
