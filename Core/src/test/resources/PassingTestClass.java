package testresources;

import org.junit.Assert;
import org.junit.Test;
import se.claremont.taf.core.testset.TestSet;

public class PassingTestClass extends TestSet {

    @Test
    public void test1() {
        Assert.assertTrue(true);
    }
}
