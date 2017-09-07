package test;
import org.junit.Assert;
import org.junit.Test;
import se.claremont.autotest.common.testset.TestSet;

public class FailingTestClass extends TestSet{

    public FailingTestClass(){}

    @Test
    public void exceptionTest(){
        Assert.assertTrue(false);
    }
}
