package se.claremont.taf.mqsupport;

import com.ibm.mq.constants.CMQC;
import se.claremont.taf.core.logging.LogLevel;
import se.claremont.taf.core.testcase.TestCase;

import java.util.Hashtable;

public class MqInteraction {

    public TestCase testCase;

    public MqInteraction(TestCase testCase){
        if(testCase == null) testCase = new TestCase();
        this.testCase = testCase;
        testCase.log(LogLevel.DEBUG, "Creating a new MQ Interactions instanse.");
        testCase.log(LogLevel.DEBUG, "Check https://www.ibm.com/support/knowledgecenter/en/SSFKSJ_7.5.0/com.ibm.mq.dev.doc/q030680_.htm for instructions on how to configure MQs for client access.");
    }

    public QueueManager connectToQueueManager(String channelName, String queueManagerName, String hostName, Integer portNumber, String userId, String password){
        Hashtable<String, Object> queueOptions;
        queueOptions = new Hashtable<String, Object>();
        if(channelName != null)
            queueOptions.put(CMQC.CHANNEL_PROPERTY, channelName);

        if(hostName != null)
            queueOptions.put(CMQC.HOST_NAME_PROPERTY, hostName);

        queueOptions.put(CMQC.APPNAME_PROPERTY, "TAF");

        if(portNumber != null)
            queueOptions.put(CMQC.PORT_PROPERTY, portNumber);

        if(userId != null)
            queueOptions.put(CMQC.USER_ID_PROPERTY, userId);

        if(password != null)
            queueOptions.put(CMQC.PASSWORD_PROPERTY, password);

        return new QueueManager(testCase, queueManagerName, queueOptions);
    }

}
