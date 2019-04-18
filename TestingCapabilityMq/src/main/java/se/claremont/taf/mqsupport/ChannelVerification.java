package se.claremont.taf.mqsupport;

import se.claremont.taf.core.logging.LogLevel;
import se.claremont.taf.core.testcase.TestCase;

public class ChannelVerification {

    private TestCase testCase;
    private Channel channel;

    public ChannelVerification(TestCase testCase, Channel channel){
        this.testCase = testCase;
        this.channel = channel;
    }

    public ChannelVerification isOpen(){
        try{
            String connectionName = channel.channel.connectionName;
            testCase.log(LogLevel.VERIFICATION_PASSED, "Channel '" + channel.channel.channelName + "' is open.");
        }catch (Exception e){
            testCase.log(LogLevel.VERIFICATION_FAILED, "Channel is closed while expected to be open." );
        }
        return this;
    }

    public ChannelVerification isClosed(){
        try{
            String connectionName = channel.channel.connectionName;
            testCase.log(LogLevel.VERIFICATION_FAILED, "Channel is closed, as expected." );
        }catch (Exception e){
            testCase.log(LogLevel.VERIFICATION_PASSED, "Channel is closed while expected to be open.");
        }
        return this;
    }
}
