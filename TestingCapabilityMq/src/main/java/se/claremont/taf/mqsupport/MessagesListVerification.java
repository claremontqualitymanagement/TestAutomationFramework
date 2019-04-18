package se.claremont.taf.mqsupport;

import se.claremont.taf.core.logging.LogLevel;
import se.claremont.taf.core.testcase.TestCase;

public class MessagesListVerification {

    private TestCase testCase;
    private MessagesList messagesList;

    public MessagesListVerification(TestCase testCase, MessagesList messagesList){
        if(messagesList == null) messagesList = new MessagesList(testCase, null);
        this.testCase = testCase;
        this.messagesList = messagesList;
    }

    public MessagesListVerification isNotEmpty(){
        String queueName = "";
        try{
            queueName = messagesList.queue.getName();
        } catch (Exception e){
            testCase.log(LogLevel.DEBUG, "Could not retreive name from queue. Exception: " + e);
        }
        if(messagesList.size() == 0){
            testCase.log(LogLevel.VERIFICATION_FAILED, "Expected '" + queueName + "' messages list to not be empty, but it was.");
        } else {
            testCase.log(LogLevel.VERIFICATION_PASSED, "Expected '" + queueName + "' messages list to contain items and it did.");
        }
        return this;
    }

    public MessagesListVerification receivesMessage(String messageContent, int timeoutInSeconds){
        long timer = System.currentTimeMillis();
        do{

        }while ((System.currentTimeMillis() - timer) * 1000 < timeoutInSeconds);
        return this;
    }

    public MessagesListVerification containsMessage(String message, MatchingType matchingType){
        boolean success = false;
        switch (matchingType){
            case Equals:
                success = messagesList.containstMessageThatMatchesRegexPattern(message);
                break;
            case Contains:
                success = messagesList.containstMessageThatMatchesRegexPattern(".*" + message + ".*");
                break;
            case IsRegexMatch:
                success = messagesList.containstMessageThatMatchesRegexPattern(message);
                break;
                default:
                    testCase.log(LogLevel.EXECUTION_PROBLEM, "No implementation for matching type '" + matchingType + "' in MessagesListVerification.containsMessage().");
                    break;
        }
        if(success){
            testCase.log(LogLevel.VERIFICATION_PASSED, "Message with data matching '" + message + "' found (" + matchingType + ").");
        } else {
            testCase.log(LogLevel.VERIFICATION_FAILED, "Could not identify message with data matching '" + message + "' (" + matchingType + "). Messages: " + System.lineSeparator() + messagesList.toString());
        }
        return this;
    }

    public MessagesListVerification isEmpty(){
        String queueName = "";
        try{
            queueName = messagesList.queue.getName();
        }catch (Exception e){
            testCase.log(LogLevel.DEBUG, "Could not retreive name from queue. Exception: " + e);
        }
        if(messagesList.size() != 0){
            testCase.log(LogLevel.VERIFICATION_FAILED, "Expected '" + queueName + "' messages list to be empty, but it was not.");
        } else {
            testCase.log(LogLevel.VERIFICATION_PASSED, "Expected '" + queueName + "' messages list to be empty and it was.");
        }
        return this;
    }

}
