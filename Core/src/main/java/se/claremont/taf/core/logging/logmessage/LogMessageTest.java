package se.claremont.taf.core.logging.logmessage;

import org.junit.Assert;
import org.junit.Test;
import se.claremont.taf.core.testset.UnitTestClass;

/**
 * Created by jordam on 2017-03-29.
 */
public class LogMessageTest extends UnitTestClass {

    @Test
    public void logMessageToHtmlShouldDecorateWithSpanTags(){
        LogMessage logMessage = new LogMessage(new LogMessagePartType[]{
                new TextLogMessagePart("This is a log message. Element name "),
                new ElementName("TestElement"),
                new TextLogMessagePart(" with value "),
                new DataValue("12345"),
                new TextLogMessagePart(".")
        });
        Assert.assertTrue(logMessage.toHtml().contains("<span class=\""));
    }

    @Test
    public void htmlStringToKnownErrorShouldSubstituteDataValuesWithRegexpGenerics(){
        LogMessage logMessage = new LogMessage(new LogMessagePartType[]{
                new TextLogMessagePart("This is a log message. Element name "),
                new ElementName("TestElement"),
                new TextLogMessagePart(" with value "),
                new DataValue("12345"),
                new TextLogMessagePart(".")
        });
        Assert.assertTrue(logMessage.toKnownErrorSuggestionString().equals("This is a log message. Element name 'TestElement' with value '.*'."));
    }

    @Test
    public void dataValueEnclosedInTicksInLogStringShouldBeDetectedAsDataValue(){
        String message = "This is a log line with a 'datavalue' in it.";
        LogMessage logMessage = new LogMessage(message);
        Assert.assertTrue(logMessage.logMessageParts.size() == 3);
        Assert.assertTrue(logMessage.logMessageParts.get(0).getClass().equals(TextLogMessagePart.class));
        Assert.assertTrue(logMessage.logMessageParts.get(1).getClass().equals(DataValue.class));
        Assert.assertTrue(logMessage.logMessageParts.get(2).getClass().equals(TextLogMessagePart.class));
        Assert.assertTrue(logMessage.toString().equals(message));
        Assert.assertTrue(logMessage.toHtml().contains("<span class="));
        Assert.assertTrue(logMessage.toKnownErrorSuggestionString().equals(message.replace("datavalue", ".*")));
    }
}
