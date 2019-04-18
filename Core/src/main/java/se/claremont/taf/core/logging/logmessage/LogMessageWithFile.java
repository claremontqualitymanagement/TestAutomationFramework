package se.claremont.taf.core.logging.logmessage;

import java.io.File;

/**
 * Used for screenshots and html source files for debugging.
 *
 * Created by jordam on 2017-03-31.
 */
@SuppressWarnings("WeakerAccess")
public class LogMessageWithFile extends LogMessage{
    private final File file;

    public LogMessageWithFile(String logMessage, File file) {
        super(logMessage);
        this.file = file;
    }

    public LogMessageWithFile(LogMessagePartType[] logMessageParts, File file){
        super(logMessageParts);
        this.file = file;
    }

    @SuppressWarnings({"SimplifiableIfStatement", "ConstantConditions"})
    public boolean equals(Object logMessage) {
        if (logMessage.getClass() != this.getClass()) return false;
        LogMessageWithFile logMessageWithFile = (LogMessageWithFile) logMessage;
        if (file == null && logMessageWithFile.file != null) return false;
        if (logMessageWithFile.file == null && file != null) return false;
        if (file == null && logMessageWithFile.file == null && this.logMessageParts.equals(logMessageWithFile.logMessageParts)) return true;
        return this.logMessageParts.equals(logMessageWithFile.logMessageParts) && logMessageWithFile.file.equals(file);
    }
}
