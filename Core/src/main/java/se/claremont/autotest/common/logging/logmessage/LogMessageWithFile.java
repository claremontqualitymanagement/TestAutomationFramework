package se.claremont.autotest.common.logging.logmessage;

import java.io.File;

/**
 * Used for screenshots and html source files for debugging.
 *
 * Created by jordam on 2017-03-31.
 */
public class LogMessageWithFile extends LogMessage{
    File file;

    public LogMessageWithFile(String logMessage, File file) {
        super(logMessage);
        this.file = file;
    }

    public LogMessageWithFile(LogMessagePartType[] logMessageParts, File file){
        super(logMessageParts);
        this.file = file;
    }

    public boolean equals(LogMessage logMessage){
        if(logMessage.getClass() != LogMessageWithFile.class) return false;
        LogMessageWithFile logMessageWithFile = (LogMessageWithFile) logMessage;
        return this.logMessageParts.equals(logMessage.logMessageParts) && ((logMessageWithFile.file == null && file == null) || logMessageWithFile.file.equals(file));
    }
}
