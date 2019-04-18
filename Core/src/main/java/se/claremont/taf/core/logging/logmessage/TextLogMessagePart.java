package se.claremont.taf.core.logging.logmessage;

/**
 * Created by jordam on 2017-03-29.
 */
public class TextLogMessagePart implements LogMessagePartType{

    private final String logMessage;

    public TextLogMessagePart(String logMessage){
        this.logMessage = logMessage;
    }

    public String toString(){
        return logMessage;
    }

    public String toHtml(){
        return "<span class=\"logpostmessagetext\">" + logMessage + "</span>";
    }

    public String toKnownErrorSuggestionString(){
        return toString();
    }

}
