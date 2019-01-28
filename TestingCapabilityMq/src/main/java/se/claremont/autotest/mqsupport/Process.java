package se.claremont.autotest.mqsupport;

import com.ibm.mq.MQProcess;
import se.claremont.autotest.common.testcase.TestCase;

public class Process {
    public MQProcess mqProcess;
    private TestCase testCase;

    public Process(TestCase testCase, MQProcess mqProcess) {
        this.testCase = testCase;
        this.mqProcess = mqProcess;
    }

    public TafProcessVerification verify(){
        return new TafProcessVerification(testCase, mqProcess);
    }

}
