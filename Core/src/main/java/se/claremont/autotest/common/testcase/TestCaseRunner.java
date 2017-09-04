package se.claremont.autotest.common.testcase;

import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.Result;
import org.junit.runner.notification.RunNotifier;
import se.claremont.autotest.common.testrun.TestRun;

import java.util.concurrent.Callable;

public class TestCaseRunner implements Callable<Result> {
    Request request;
    JUnitCore jUnitCore;
    RunNotifier runNotifier;

    public TestCaseRunner(Request request, JUnitCore jUnitCore, RunNotifier runNotifier){
        this.request = request;
        this.jUnitCore = jUnitCore;
        this.runNotifier = runNotifier;
    }

    @Override
    public Result call(){
        JUnitCore jUnitCore = new JUnitCore();
        return jUnitCore.run(request);
        //request.getRunner().run(runNotifier);
        //runNotifier.fireTestFinished(request.getRunner().getDescription());
    }
}
