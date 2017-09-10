package se.claremont.autotest.common.testset;

import org.junit.Assert;
import org.junit.Test;
import se.claremont.autotest.common.logging.LogLevel;
import se.claremont.autotest.common.testcase.TestCase;

public class ParallelExecution extends TestSet{

    private void failingNonTestPrivateMethodBeingCallable(){
        System.out.println("Private non-test method executed");
    }

    public void failingNonTestPublicMethodBeingCallable(){
        System.out.println("Public non-test method executed");
    }

    @Test
    public void parallelExecutionTestCaseStockholm(){
        currentTestCase().log(LogLevel.INFO, "Stockholm");
        currentTestCase().log(LogLevel.INFO, "Stockholm");
        currentTestCase().log(LogLevel.INFO, "Stockholm");
        currentTestCase().log(LogLevel.INFO, "Stockholm");
        currentTestCase().log(LogLevel.INFO, "Stockholm");
        currentTestCase().log(LogLevel.INFO, "Stockholm");
        currentTestCase().log(LogLevel.INFO, "Stockholm");
        currentTestCase().log(LogLevel.INFO, "Stockholm");
        currentTestCase().log(LogLevel.INFO, "Stockholm");
        currentTestCase().log(LogLevel.INFO, "Stockholm");
        currentTestCase().log(LogLevel.INFO, "Stockholm");
        currentTestCase().log(LogLevel.INFO, "Stockholm");
        currentTestCase().log(LogLevel.INFO, "Stockholm");
        currentTestCase().log(LogLevel.INFO, "Stockholm");
        currentTestCase().log(LogLevel.INFO, "Stockholm");
        currentTestCase().log(LogLevel.INFO, "Stockholm");
        currentTestCase().log(LogLevel.INFO, "Stockholm");
        currentTestCase().log(LogLevel.INFO, "Stockholm");
        currentTestCase().log(LogLevel.INFO, "Stockholm");
        currentTestCase().log(LogLevel.INFO, "Stockholm");
        currentTestCase().log(LogLevel.INFO, "Stockholm");
        currentTestCase().log(LogLevel.INFO, "Stockholm");
        currentTestCase().log(LogLevel.INFO, "Stockholm");
        currentTestCase().log(LogLevel.INFO, "Stockholm");
        currentTestCase().log(LogLevel.INFO, "Stockholm");
        currentTestCase().log(LogLevel.INFO, "Stockholm");
        currentTestCase().log(LogLevel.INFO, "Stockholm");
        currentTestCase().log(LogLevel.INFO, "Stockholm");
        currentTestCase().log(LogLevel.INFO, "Stockholm");
        currentTestCase().log(LogLevel.INFO, "Stockholm");
        currentTestCase().log(LogLevel.INFO, "Stockholm");
        currentTestCase().log(LogLevel.INFO, "Stockholm");
        currentTestCase().log(LogLevel.INFO, "Stockholm");
        currentTestCase().log(LogLevel.INFO, "Stockholm");
        currentTestCase().log(LogLevel.INFO, "Stockholm");
        currentTestCase().log(LogLevel.INFO, "Stockholm");
        currentTestCase().log(LogLevel.INFO, "Stockholm");
        currentTestCase().log(LogLevel.INFO, "Stockholm");
        currentTestCase().log(LogLevel.INFO, "Stockholm");
        currentTestCase().log(LogLevel.INFO, "Stockholm");
        currentTestCase().log(LogLevel.INFO, "Stockholm");
        currentTestCase().log(LogLevel.INFO, "Stockholm");
        currentTestCase().log(LogLevel.INFO, "Stockholm");
        currentTestCase().log(LogLevel.INFO, "Stockholm");
    }

    @Test
    public void parallelExecutionTestCaseMalmoe(){
        currentTestCase().log(LogLevel.INFO, "Malmoe");
        currentTestCase().log(LogLevel.INFO, "Malmoe");
        currentTestCase().log(LogLevel.INFO, "Malmoe");
        currentTestCase().log(LogLevel.INFO, "Malmoe");
        currentTestCase().log(LogLevel.INFO, "Malmoe");
        currentTestCase().log(LogLevel.INFO, "Malmoe");
        currentTestCase().log(LogLevel.INFO, "Malmoe");
        currentTestCase().log(LogLevel.INFO, "Malmoe");
        currentTestCase().log(LogLevel.INFO, "Malmoe");
        currentTestCase().log(LogLevel.INFO, "Malmoe");
        currentTestCase().log(LogLevel.INFO, "Malmoe");
        currentTestCase().log(LogLevel.INFO, "Malmoe");
        currentTestCase().log(LogLevel.INFO, "Malmoe");
        currentTestCase().log(LogLevel.INFO, "Malmoe");
        currentTestCase().log(LogLevel.INFO, "Malmoe");
        currentTestCase().log(LogLevel.INFO, "Malmoe");
        currentTestCase().log(LogLevel.INFO, "Malmoe");
        currentTestCase().log(LogLevel.INFO, "Malmoe");
        currentTestCase().log(LogLevel.INFO, "Malmoe");
        currentTestCase().log(LogLevel.INFO, "Malmoe");
        currentTestCase().log(LogLevel.INFO, "Malmoe");
        currentTestCase().log(LogLevel.INFO, "Malmoe");
        currentTestCase().log(LogLevel.INFO, "Malmoe");
        currentTestCase().log(LogLevel.INFO, "Malmoe");
        currentTestCase().log(LogLevel.INFO, "Malmoe");
        currentTestCase().log(LogLevel.INFO, "Malmoe");
        currentTestCase().log(LogLevel.INFO, "Malmoe");
        currentTestCase().log(LogLevel.INFO, "Malmoe");
        currentTestCase().log(LogLevel.INFO, "Malmoe");
        currentTestCase().log(LogLevel.INFO, "Malmoe");
        currentTestCase().log(LogLevel.INFO, "Malmoe");
        currentTestCase().log(LogLevel.INFO, "Malmoe");
        currentTestCase().log(LogLevel.INFO, "Malmoe");
        currentTestCase().log(LogLevel.INFO, "Malmoe");
        currentTestCase().log(LogLevel.INFO, "Malmoe");
        currentTestCase().log(LogLevel.INFO, "Malmoe");
        currentTestCase().log(LogLevel.INFO, "Malmoe");
        currentTestCase().log(LogLevel.INFO, "Malmoe");
        currentTestCase().log(LogLevel.INFO, "Malmoe");
        currentTestCase().log(LogLevel.INFO, "Malmoe");
        currentTestCase().log(LogLevel.INFO, "Malmoe");
        currentTestCase().log(LogLevel.INFO, "Malmoe");
        currentTestCase().log(LogLevel.INFO, "Malmoe");
        currentTestCase().log(LogLevel.INFO, "Malmoe");
        currentTestCase().log(LogLevel.INFO, "Malmoe");
        currentTestCase().log(LogLevel.INFO, "Malmoe");
        currentTestCase().log(LogLevel.INFO, "Malmoe");
        currentTestCase().log(LogLevel.INFO, "Malmoe");
        currentTestCase().log(LogLevel.INFO, "Malmoe");
        currentTestCase().log(LogLevel.INFO, "Malmoe");
        currentTestCase().log(LogLevel.INFO, "Malmoe");
    }

    @Test
    public void parallelExecutionTestCaseGothenburg(){
        currentTestCase().log(LogLevel.INFO, "Gothenburg");
        currentTestCase().log(LogLevel.INFO, "Gothenburg");
        currentTestCase().log(LogLevel.INFO, "Gothenburg");
        currentTestCase().log(LogLevel.INFO, "Gothenburg");
        currentTestCase().log(LogLevel.INFO, "Gothenburg");
        currentTestCase().log(LogLevel.INFO, "Gothenburg");
        currentTestCase().log(LogLevel.INFO, "Gothenburg");
        currentTestCase().log(LogLevel.INFO, "Gothenburg");
        currentTestCase().log(LogLevel.INFO, "Gothenburg");
        currentTestCase().log(LogLevel.INFO, "Gothenburg");
        currentTestCase().log(LogLevel.INFO, "Gothenburg");
        currentTestCase().log(LogLevel.INFO, "Gothenburg");
        currentTestCase().log(LogLevel.INFO, "Gothenburg");
        currentTestCase().log(LogLevel.INFO, "Gothenburg");
        currentTestCase().log(LogLevel.INFO, "Gothenburg");
        currentTestCase().log(LogLevel.INFO, "Gothenburg");
        currentTestCase().log(LogLevel.INFO, "Gothenburg");
        currentTestCase().log(LogLevel.INFO, "Gothenburg");
        currentTestCase().log(LogLevel.INFO, "Gothenburg");
        currentTestCase().log(LogLevel.INFO, "Gothenburg");
        currentTestCase().log(LogLevel.INFO, "Gothenburg");
        currentTestCase().log(LogLevel.INFO, "Gothenburg");
        currentTestCase().log(LogLevel.INFO, "Gothenburg");
        currentTestCase().log(LogLevel.INFO, "Gothenburg");
        currentTestCase().log(LogLevel.INFO, "Gothenburg");
        currentTestCase().log(LogLevel.INFO, "Gothenburg");
        currentTestCase().log(LogLevel.INFO, "Gothenburg");
        currentTestCase().log(LogLevel.INFO, "Gothenburg");
        currentTestCase().log(LogLevel.INFO, "Gothenburg");
        currentTestCase().log(LogLevel.INFO, "Gothenburg");
        currentTestCase().log(LogLevel.INFO, "Gothenburg");
        currentTestCase().log(LogLevel.INFO, "Gothenburg");
        currentTestCase().log(LogLevel.INFO, "Gothenburg");
        currentTestCase().log(LogLevel.INFO, "Gothenburg");
        currentTestCase().log(LogLevel.INFO, "Gothenburg");
        currentTestCase().log(LogLevel.INFO, "Gothenburg");
        currentTestCase().log(LogLevel.INFO, "Gothenburg");
        currentTestCase().log(LogLevel.INFO, "Gothenburg");
        currentTestCase().log(LogLevel.INFO, "Gothenburg");
        currentTestCase().log(LogLevel.INFO, "Gothenburg");
        currentTestCase().log(LogLevel.INFO, "Gothenburg");
        currentTestCase().log(LogLevel.INFO, "Gothenburg");
        currentTestCase().log(LogLevel.INFO, "Gothenburg");
    }

    @Test
    public void parallelExecutionTestCaseUppsala(){
        currentTestCase().log(LogLevel.INFO, "Uppsala");
        currentTestCase().log(LogLevel.INFO, "Uppsala");
        currentTestCase().log(LogLevel.INFO, "Uppsala");
        currentTestCase().log(LogLevel.INFO, "Uppsala");
        currentTestCase().log(LogLevel.INFO, "Uppsala");
        currentTestCase().log(LogLevel.INFO, "Uppsala");
        currentTestCase().log(LogLevel.INFO, "Uppsala");
        currentTestCase().log(LogLevel.INFO, "Uppsala");
        currentTestCase().log(LogLevel.INFO, "Uppsala");
        currentTestCase().log(LogLevel.INFO, "Uppsala");
        currentTestCase().log(LogLevel.INFO, "Uppsala");
        currentTestCase().log(LogLevel.INFO, "Uppsala");
        currentTestCase().log(LogLevel.INFO, "Uppsala");
        currentTestCase().log(LogLevel.INFO, "Uppsala");
        currentTestCase().log(LogLevel.INFO, "Uppsala");
        currentTestCase().log(LogLevel.INFO, "Uppsala");
        currentTestCase().log(LogLevel.INFO, "Uppsala");
        currentTestCase().log(LogLevel.INFO, "Uppsala");
        currentTestCase().log(LogLevel.INFO, "Uppsala");
        currentTestCase().log(LogLevel.INFO, "Uppsala");
        currentTestCase().log(LogLevel.INFO, "Uppsala");
        currentTestCase().log(LogLevel.INFO, "Uppsala");
        currentTestCase().log(LogLevel.INFO, "Uppsala");
        currentTestCase().log(LogLevel.INFO, "Uppsala");
        currentTestCase().log(LogLevel.INFO, "Uppsala");
        currentTestCase().log(LogLevel.INFO, "Uppsala");
        currentTestCase().log(LogLevel.INFO, "Uppsala");
        currentTestCase().log(LogLevel.INFO, "Uppsala");
        currentTestCase().log(LogLevel.INFO, "Uppsala");
        currentTestCase().log(LogLevel.INFO, "Uppsala");
        currentTestCase().log(LogLevel.INFO, "Uppsala");
        currentTestCase().log(LogLevel.INFO, "Uppsala");
        currentTestCase().log(LogLevel.INFO, "Uppsala");
        currentTestCase().log(LogLevel.INFO, "Uppsala");
        currentTestCase().log(LogLevel.INFO, "Uppsala");
        currentTestCase().log(LogLevel.INFO, "Uppsala");
        currentTestCase().log(LogLevel.INFO, "Uppsala");
        currentTestCase().log(LogLevel.INFO, "Uppsala");
        currentTestCase().log(LogLevel.INFO, "Uppsala");
        currentTestCase().log(LogLevel.INFO, "Uppsala");
        currentTestCase().log(LogLevel.INFO, "Uppsala");
        currentTestCase().log(LogLevel.INFO, "Uppsala");
        currentTestCase().log(LogLevel.INFO, "Uppsala");
    }
}
