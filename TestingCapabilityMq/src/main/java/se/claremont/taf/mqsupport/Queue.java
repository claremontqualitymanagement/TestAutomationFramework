package se.claremont.taf.mqsupport;

import com.ibm.mq.*;
import com.ibm.mq.constants.CMQC;
import se.claremont.taf.core.logging.LogLevel;
import se.claremont.taf.core.support.SupportMethods;
import se.claremont.taf.core.testcase.TestCase;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Queue {

    public MQQueue mqQueue;
    private TestCase testCase;
    private QueueManager queueManager;
    private String queueName = null;

    public Queue(TestCase testCase, MQQueue mqQueue, QueueManager queueManager){
        this.mqQueue = mqQueue;
        this.testCase = testCase;
        this.queueManager = queueManager;
        try {
            queueName = mqQueue.getName();
        }catch (Exception ignored){

        }
    }

    public TafQueueVerification verify(){
        return new TafQueueVerification(testCase, mqQueue);
    }

    public void putMessageFromFileContent(String filePath){
        try {
            testCase.log(LogLevel.DEBUG, "Attempting to read message content from file '" + filePath + "' to place it on queue.");
            putMessage(String.join(System.lineSeparator(), Files.readAllLines(Paths.get(filePath))));
        } catch (IOException e) {
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not read message content from file '" + filePath + "'. Exception: " + e);
            testCase.report();
        }
    }

    public void deleteMessagesWithSpecifiedApplicationName(String applicationName){
        MQQueue outputQueue = null;
        testCase.log(LogLevel.DEBUG, "Attempting to delete message(s) with origin application '" + applicationName + "'.");
        int openInputOptions = CMQC.MQOO_FAIL_IF_QUIESCING +
                CMQC.MQOO_INPUT_SHARED + CMQC.MQOO_BROWSE + CMQC.MQOO_INQUIRE;

        try {
            outputQueue = queueManager.mqQueueManager.accessQueue(queueName, openInputOptions);
        } catch (MQException e) {
            e.printStackTrace();
        }
        MQMessage retrievedMessage = new MQMessage();
        MQGetMessageOptions gmo = new MQGetMessageOptions();
        gmo.options = CMQC.MQGMO_WAIT | CMQC.MQGMO_BROWSE_FIRST;
        gmo.matchOptions = CMQC.MQMO_NONE;
        gmo.waitInterval=10000;
        int c = 0;
        try {
            c = outputQueue.getCurrentDepth();
        } catch (MQException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < c ; i++) {
            testCase.log(LogLevel.EXECUTED, String.valueOf(i));

            if(i > 0)
                gmo.options = CMQC.MQGMO_WAIT | CMQC.MQGMO_BROWSE_NEXT;

            try {
                outputQueue.get(retrievedMessage, gmo);
                if(retrievedMessage.applicationOriginData == applicationName){
                    testCase.log(LogLevel.EXECUTED, "Deleted messag from application '" + applicationName + "'");
                    retrievedMessage.clearMessage();
                }
            }catch (Exception e){
                testCase.log(LogLevel.EXECUTION_PROBLEM, "Oups: " + e);
            }
        }
        try {
            if(outputQueue != null) { outputQueue.close(); }
        }
        catch (MQException ex) {
            ex.printStackTrace();
        }
    }

    public void putMessage(String content){
        String name = null;
        int openOptions = CMQC.MQOO_OUTPUT + CMQC.MQOO_FAIL_IF_QUIESCING;
        try
        {
            testCase.log(LogLevel.DEBUG, "Attempting to place message on queue '" + queueName + "'. Content: " + System.lineSeparator() + content);
                MQQueue queue = queueManager.mqQueueManager.accessQueue(mqQueue.getName(),
                    openOptions,
                    null,           // default q manager
                    null,           // no dynamic q name
                    null );         // no alternate user id

            // Define a simple MQ message, and write some text in UTF format..
            MQMessage sendmsg               = new MQMessage();
            sendmsg.format                  = CMQC.MQFMT_STRING;
            sendmsg.feedback                = CMQC.MQFB_NONE;
            sendmsg.messageType             = CMQC.MQMT_DATAGRAM;
            sendmsg.replyToQueueName        = mqQueue.getName();
            sendmsg.replyToQueueManagerName = queueManager.mqQueueManager.getName();

            MQPutMessageOptions pmo = new MQPutMessageOptions();  // accept the defaults, same

            sendmsg.clearMessage();
            sendmsg.messageId     = CMQC.MQMI_NONE;
            sendmsg.correlationId = CMQC.MQCI_NONE;
            sendmsg.writeString(content);

            queue.put(sendmsg, pmo);
            testCase.log(LogLevel.EXECUTED, "Placed message on queue '" + name + "':" + System.lineSeparator() + content);
            queue.close();
        }
        catch (Exception mqex)
        {
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not put message on queue '" + name + "'. Exception: " + mqex);
            testCase.report();
        }
    }




    public void deleteMessageByContentMatchingRegex( String contentPattern){



        MQQueue outputQueue = null;
        testCase.log(LogLevel.DEBUG, "Attempting to delete message(s) with content matching the regular expression pattern '" + contentPattern + "'.");
        int openInputOptions = CMQC.MQOO_FAIL_IF_QUIESCING +
                CMQC.MQOO_INPUT_SHARED + CMQC.MQOO_BROWSE + CMQC.MQOO_INQUIRE;

        try {
            outputQueue = queueManager.mqQueueManager.accessQueue(queueName, openInputOptions);
        } catch (MQException e) {
            e.printStackTrace();
        }
        MQMessage retrievedMessage = new MQMessage();
        MQGetMessageOptions gmo = new MQGetMessageOptions();
        gmo.options = CMQC.MQGMO_WAIT | CMQC.MQGMO_BROWSE_FIRST;
        gmo.matchOptions = CMQC.MQMO_NONE;
        gmo.waitInterval=10000;

        boolean moreMessages = true;

        if (outputQueue == null)
            return;

        do{
            try {
                outputQueue.get(retrievedMessage, gmo);
                String msg = new Message(testCase, retrievedMessage).messageDataAsString();
                if(SupportMethods.isRegexMatch(msg, contentPattern)){
                    testCase.log(LogLevel.EXECUTED, "Deleted message with content matching pattern '" + contentPattern + "'");
                    gmo.options = CMQC.MQGMO_MSG_UNDER_CURSOR;
                    outputQueue.get(retrievedMessage, gmo);
                }
            }catch (MQException  e){
                if(e.completionCode == 2 && e.reasonCode == CMQC.MQRC_NO_MSG_AVAILABLE){
                    moreMessages = false;
                }else {
                    testCase.log(LogLevel.EXECUTION_PROBLEM, "Exception encountered: " + e);
                }
            }
            gmo.options = CMQC.MQGMO_WAIT | CMQC.MQGMO_BROWSE_NEXT;
        }while (moreMessages);

        try {
            outputQueue.close();
        }
        catch (MQException ex) {
            testCase.log(LogLevel.EXECUTION_PROBLEM, ex.toString());
        }

    }



    public MessagesList getCopyOfMessages()
    {
        MessagesList messages = new MessagesList(testCase, mqQueue);
        ArrayList<String> logMessage = new ArrayList<String>();
        int depth = 0;
        try {
            logMessage.add("Retrieving messages from MQ queue '" + mqQueue.getName() + "':");

            depth = mqQueue.getCurrentDepth();
            testCase.log(LogLevel.DEBUG, "Connected to queue '" + mqQueue.getName() + "'. Current depth is " + depth + ".");
        } catch (MQException e) {
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not retrieve message queue depth. Exception: " + e);
        }

        if (depth == 0)
        {
            return messages;
        }

        MQGetMessageOptions getOptions = new MQGetMessageOptions();
        getOptions.options = CMQC.MQGMO_NO_WAIT + CMQC.MQGMO_FAIL_IF_QUIESCING + CMQC.MQGMO_CONVERT + CMQC.MQGMO_BROWSE_NEXT;

        while(true)
        {
            MQMessage message = new MQMessage();
            try
            {
                mqQueue.get(message, getOptions);
                byte[] b = new byte[message.getMessageLength()];
                message.readFully(b);
                messages.add(new Message(testCase, message));
                logMessage.add(new String(b));
                message.clearMessage();
            }
            catch (IOException e)
            {
                logMessage.add("WARNING: Queue reading was interrupted. See exception message below.");
                testCase.log(LogLevel.EXECUTED, String.join(System.lineSeparator(), logMessage));
                testCase.log(LogLevel.EXECUTION_PROBLEM, "IOException during GET: " + e.getMessage());
                break;
            }
            catch (MQException e)
            {
                if (e.completionCode == 2 && e.reasonCode == CMQC.MQRC_NO_MSG_AVAILABLE) {
                    if (depth > 0)
                    {
                        logMessage.add("All messages read.");
                        testCase.log(LogLevel.EXECUTED, String.join(System.lineSeparator(), logMessage));
                    }
                }
                else
                {
                    logMessage.add("WARNING: Queue reading was interrupted. See exception message below.");
                    testCase.log(LogLevel.EXECUTED, String.join(System.lineSeparator(), logMessage));
                    testCase.log(LogLevel.EXECUTION_PROBLEM, "GET Exception encountered: " + e);
                }
                break;
            }
            try {
                mqQueue.close();
            } catch (MQException e) {
                e.printStackTrace();
            }
        }
        return messages;
    }

}
