package se.claremont.taf.mqsupport;

import com.ibm.mq.MQTopic;
import se.claremont.taf.core.testcase.TestCase;

public class Topic {
    public MQTopic mqTopic;
    private TestCase testCase;

    public Topic(TestCase testCase, MQTopic topic){
        this.testCase = testCase;
        this.mqTopic = topic;
    }

}
