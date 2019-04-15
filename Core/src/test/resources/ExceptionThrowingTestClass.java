import org.junit.Test;
import se.claremont.taf.core.testset.TestSet;

public class ExceptionThrowingTestClass extends TestSet{

    public ExceptionThrowingTestClass(){}

    @Test
    public void exceptionTest() throws Exception{
        throw new Exception("Should fail");
    }
}