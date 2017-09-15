package se.claremont.autotest.common.junitcustomization;

import org.junit.runner.Description;
import org.junit.runner.notification.RunListener;
import se.claremont.autotest.common.testcase.TestCase;

public class TafParallelTestListener extends RunListener {
    TestCase testCase;

    @Override
    public void testStarted(Description description){
        //testCase = new TestCase(null, description.getMethodName(), description.getClassName());
        System.out.println("Starting: " +description.getClassName() + "." + description.getMethodName());
    }

    @Override
    public void testFinished(Description description){
        //testCase.report();
        System.out.println("Ending: " +description.getClassName() + "." + description.getMethodName());
    }
}
