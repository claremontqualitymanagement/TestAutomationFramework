package se.claremont.taf.mqsupport;

import com.ibm.mq.*;
import com.ibm.mq.constants.CMQC;
import se.claremont.taf.core.logging.LogLevel;
import se.claremont.taf.core.testcase.TestCase;

import java.util.Hashtable;

public class QueueManager {

    public MQQueueManager mqQueueManager;
    private TestCase testCase;

    public QueueManager(TestCase testCase, String queueManagerName, Hashtable<String, Object> options) {
        if(testCase == null) testCase = new TestCase();
        this.testCase = testCase;
        try {
            this.mqQueueManager = new MQQueueManager(queueManagerName, options);
            this.testCase.log(LogLevel.EXECUTED, "Connected to IBM MQ queue manager '" + queueManagerName + "'.");
        } catch (MQException e) {
            this.testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not connect to IBM MQ queue manager '" + queueManagerName + "'. " + e);
        }
    }

    public QueueManager(TestCase testCase, MQQueueManager queueManager){
        this.mqQueueManager = queueManager;
        if(testCase == null) testCase = new TestCase();
        this.testCase = testCase;
    }

    public void disconnect(){
        String name = "<un-identified queue name>";
        try {
            name = mqQueueManager.getName();
            testCase.log(LogLevel.DEBUG, "Attempting to disconnect MQ Queue named '" + name + "'.");
            mqQueueManager.disconnect();
            testCase.log(LogLevel.EXECUTED, "Disconnected the MQ Queue named '" + name + "'.");
        } catch (MQException e) {
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Exception while disconnecting MQ Queue named '" + name + "'. Exception: " + e.toString());
            e.printStackTrace();
        }
    }

    public Process getProcess(String name){
        MQProcess process = null;
        try {
            testCase.log(LogLevel.DEBUG, "Attempting to access process '" + name + "'.");
            process = mqQueueManager.accessProcess(name, CMQC.MQOO_FAIL_IF_QUIESCING);
            testCase.log(LogLevel.EXECUTED, "Accessing process '" + name + "'.");
        } catch (MQException e) {
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Attempting to access process '" + name + "'. Exception encountered: " + e.toString());
        }
        return new Process(testCase, process);
    }

    public Queue getQueue(String name){
        MQQueue queue = null;
        try {
            testCase.log(LogLevel.DEBUG, "Attempting to access queue '" + name + "'.");
            queue = mqQueueManager.accessQueue(name, CMQC.MQOO_BROWSE + CMQC.MQOO_FAIL_IF_QUIESCING + CMQC.MQOO_INQUIRE);
            testCase.log(LogLevel.EXECUTED, "Accessing queue '" + name + "'.");
        } catch (MQException e) {
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Attempting to access queue '" + name + "'. Exception encountered: " + e.toString());
        }
        return new Queue(testCase, queue, this);
    }

    public Topic registerTopicSubscriber(String topicString, String topicObject){
        MQTopic topic = null;
        try {
            testCase.log(LogLevel.DEBUG, "Attempting to access topic '" + topicString + "' to register subscription.");
            topic = mqQueueManager.accessTopic(topicString, topicObject, CMQC.MQTOPIC_OPEN_AS_SUBSCRIPTION, CMQC.MQSO_CREATE);
            testCase.log(LogLevel.EXECUTED, "Registering subscription for topic '" + topicString + "'.");
        } catch (MQException e) {
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Attempting to access topic '" + topicString + "' to register subscription. Exception encountered: " + e.toString());
        }
        return new Topic(testCase, topic);
    }

    public Topic registerTopicPublisher(String topicString, String topicObject){
        MQTopic topic = null;
        try {
            testCase.log(LogLevel.DEBUG, "Attempting to access topic '" + topicString + "' to register publisher.");
            topic = mqQueueManager.accessTopic(topicString, topicObject, CMQC.MQTOPIC_OPEN_AS_PUBLICATION, CMQC.MQOO_OUTPUT);
            testCase.log(LogLevel.EXECUTED, "Registering publisher for topic '" + topicString + "'.");
        } catch (MQException e) {
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Attempting to access topic '" + topicString + "' to register publisher. Exception encountered: " + e.toString());
        }
        return new Topic(testCase, topic);
    }


}
