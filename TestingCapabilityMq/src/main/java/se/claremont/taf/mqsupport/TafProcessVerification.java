package se.claremont.taf.mqsupport;

import com.ibm.mq.MQProcess;
import se.claremont.taf.core.logging.LogLevel;
import se.claremont.taf.core.testcase.TestCase;

public class TafProcessVerification {

    private MQProcess process;
    private TestCase testCase;

    public TafProcessVerification(TestCase testCase, MQProcess process){
        this.process = process;
        this.testCase = testCase;
    }

    public TafProcessVerification exist(){
        if(process == null){
            testCase.log(LogLevel.VERIFICATION_FAILED, "Process did not exist, but was expected to.");
        } else {
            testCase.log(LogLevel.VERIFICATION_PASSED, "Process existed, as expected.");
        }
        return this;
    }

    public TafProcessVerification doesNotExist(){
        if(process != null){
            testCase.log(LogLevel.VERIFICATION_FAILED, "Process did exist, but was expected not to exist.");
        } else {
            testCase.log(LogLevel.VERIFICATION_PASSED, "Process did not exist, as expected.");
        }
        return this;
    }

}
