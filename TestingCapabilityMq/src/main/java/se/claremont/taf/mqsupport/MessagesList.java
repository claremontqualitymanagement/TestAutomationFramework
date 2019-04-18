package se.claremont.taf.mqsupport;

import com.ibm.mq.MQQueue;
import se.claremont.taf.core.support.SupportMethods;
import se.claremont.taf.core.testcase.TestCase;

import java.util.LinkedList;

public class MessagesList extends LinkedList<Message> {

    private TestCase testCase;
    MQQueue queue;

    public MessagesList(TestCase testCase, MQQueue queue) {
        this.testCase = testCase;
        this.queue = queue;
    }

    public boolean containstMessageThatContainsDataString(String mqMessageDataString){
        for (Message message : this){
            if(message.messageDataAsString().contains(mqMessageDataString))return true;
        }
        return false;
    }

    public boolean containstMessageThatMatchesRegexPattern(String mqMessageDataRegexPattern){
        for (Message message : this){
            if(SupportMethods.isRegexMatch(message.messageDataAsString(), mqMessageDataRegexPattern)) return true;
        }
        return false;
    }

    public MessagesListVerification verify(){
        return new MessagesListVerification(testCase, this);
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(Message message : this){
            sb.append(message.toString()).append(System.lineSeparator());
        }
        return sb.toString();
    }
}
