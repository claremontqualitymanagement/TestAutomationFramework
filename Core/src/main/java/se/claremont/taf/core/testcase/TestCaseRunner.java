package se.claremont.taf.core.testcase;

import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import se.claremont.taf.core.junitcustomization.TafResult;

import java.util.concurrent.Callable;

public class TestCaseRunner implements Callable<TafResult> {
    private final Request request;

    public TestCaseRunner(Request request) {
        this.request = request;
    }

    @Override
    public TafResult call() {
        JUnitCore jUnitCore = new JUnitCore();
        TafParallelTestListener tafParallelTestCaseRunner = new TafParallelTestListener();
        Description description = request.getRunner().getDescription();
        tafParallelTestCaseRunner.testStarted(description);
        jUnitCore.addListener(new TafParallelTestListener());
        TafResult tafResult = new TafResult();
        tafResult.addTestResult(jUnitCore.run(request));
        return tafResult;

        //request.getRunner().run(runNotifier);
        //runNotifier.fireTestFinished(request.getRunner().getDescription());
    }
}
