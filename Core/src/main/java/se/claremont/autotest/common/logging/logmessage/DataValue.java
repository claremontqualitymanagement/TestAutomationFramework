package se.claremont.autotest.common.logging.logmessage;

/**
 * Created by jordam on 2017-03-29.
 */
public class DataValue implements LogMessagePartType{
    private String logMessage;

    public DataValue(String dataValue){
        logMessage = dataValue;
    }

    public String toString(){
        return "'" + logMessage + "'";
    }

    public String toHtml(){
        return "<span class=\"data\">" + logMessage + "</span>";
    }

    public String toKnownErrorSuggestionString(){
        return "'.*'";
    }
}
