package se.claremont.taf.mqsupport;

import com.ibm.mq.MQChannelDefinition;
import se.claremont.taf.core.testcase.TestCase;

public class Channel {

    private TestCase testCase;
    private String name;
    MQChannelDefinition channel;

    public Channel(TestCase testCase, String name, MQChannelDefinition channel){
        if(testCase == null) testCase = new TestCase();
        this.testCase = testCase;
        this.name = name;
        this.channel = channel;
    }

    public ChannelVerification verify(){
        return new ChannelVerification(testCase, this);
    }
}
