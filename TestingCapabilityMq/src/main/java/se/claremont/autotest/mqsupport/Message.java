package se.claremont.autotest.mqsupport;

import com.ibm.mq.MQMessage;
import se.claremont.autotest.common.logging.LogLevel;
import se.claremont.autotest.common.testcase.TestCase;

import java.io.IOException;

public class Message {

    public MQMessage mqMessage;
    private TestCase testCase;

    public Message(TestCase testCase, MQMessage message){
        this.mqMessage = message;
        this.testCase = testCase;
    }

    public String messageDataAsString(){
        String messageData = null;
        int contentLength = 0;
        try {
            contentLength = mqMessage.getMessageLength();
            byte[] strData = new byte[contentLength];
            mqMessage.readFully(strData,0,contentLength);
            messageData = new String(strData, 0);
        } catch (IOException e) {
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not read message data. " + e);
        }
        return messageData;
    }

    @Override
    public String toString(){
        return mqMessage.applicationIdData+ ": " + messageDataAsString();
    }
}
