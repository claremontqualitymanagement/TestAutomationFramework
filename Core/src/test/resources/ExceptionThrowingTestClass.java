package test;
import org.junit.Test;
import se.claremont.autotest.common.testset.TestSet;

public class ExceptionThrowingTestClass extends TestSet{

    public ExceptionThrowingTestClass(){}

    @Test
    public void exceptionTest() throws Exception{
        throw new Exception("Should fail");
    }
}