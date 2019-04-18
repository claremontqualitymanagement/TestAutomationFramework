package se.claremont.taf.mqsupport;

import org.junit.Test;
import se.claremont.taf.common.logging.LogLevel;
import se.claremont.taf.common.logging.LogPost;
import se.claremont.taf.common.testcase.TestCase;
import se.claremont.taf.mqsupport.support.TestSupport;

public class MqInteractionTests {


    @Test
    public void instansiationWithNull(){
        MqInteraction mq = new MqInteraction(null);
    }

    @Test
    public void connectionInstansiationWithNulls(){ //Code 2495 - unable to connect
        TestCase testCase = new TestCase();
        MqInteraction mq = new MqInteraction(testCase);
        mq.connectToQueueManager(null, null, null, null, null, null);
        TestSupport.assertTestCaseLog(testCase, new LogPost(LogLevel.EXECUTION_PROBLEM, ".*2495.*", null, null, null, null));
    }

}
