package se.claremont.autotest.common.testcase;

import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.Result;
import org.junit.runner.notification.RunNotifier;
import se.claremont.autotest.common.junitcustomization.TafParallelTestCaseRunner;
import se.claremont.autotest.common.junitcustomization.TafResult;
import se.claremont.autotest.common.testrun.TestRun;

import java.util.concurrent.Callable;

public class TestCaseRunner implements Callable<TafResult> {
    Request request;
    JUnitCore jUnitCore;
    RunNotifier runNotifier;
    String testName;

    public TestCaseRunner(Request request, JUnitCore jUnitCore, RunNotifier runNotifier, String testName){
        this.request = request;
        this.jUnitCore = jUnitCore;
        this.runNotifier = runNotifier;
        this.testName = testName;
    }

    @Override
    public TafResult call(){
        JUnitCore jUnitCore = new JUnitCore();
        TafParallelTestListener tafParallelTestCaseRunner = new TafParallelTestListener();
        Description description = request.getRunner().getDescription();
        tafParallelTestCaseRunner.testStarted(description);
        jUnitCore.addListener(new TafParallelTestListener());
        TafResult tafResult = new TafResult();
        tafResult.addTestResult(jUnitCore.run(request));
        tafResult.setTestMethodName(testName);
        return tafResult;

        //request.getRunner().run(runNotifier);
        //runNotifier.fireTestFinished(request.getRunner().getDescription());
    }
}
