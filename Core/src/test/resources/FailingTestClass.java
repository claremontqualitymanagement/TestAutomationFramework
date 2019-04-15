import org.junit.Assert;
import org.junit.Test;
import se.claremont.taf.core.testset.TestSet;

public class FailingTestClass extends TestSet{

    public FailingTestClass(){}

    @Test
    public void exceptionTest(){
        Assert.assertTrue(false);
    }
}
