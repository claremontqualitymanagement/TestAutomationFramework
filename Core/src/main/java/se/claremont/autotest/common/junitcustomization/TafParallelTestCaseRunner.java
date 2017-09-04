package se.claremont.autotest.common.junitcustomization;

import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import se.claremont.autotest.common.testcase.TestCaseRunner;
import se.claremont.autotest.common.testrun.TestRun;
import se.claremont.autotest.common.testset.TestSet;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

public class TafParallelTestCaseRunner {

    ExecutorService executorService;
    ThreadPoolExecutor threadPoolExecutor;
    List<Class<?>> testClasses = new ArrayList<>();
    JUnitCore jUnitCore;
    public static Set<TestSet> testSets = new HashSet<>();
    public static Set<String> testSetNames = new HashSet<>();

    public TafParallelTestCaseRunner(int threadCount, JUnitCore jUnitCore){
        executorService = Executors.newFixedThreadPool(threadCount);
        threadPoolExecutor = (ThreadPoolExecutor)Executors.newFixedThreadPool(threadCount);
        this.jUnitCore = jUnitCore;
    }

    public void addTestClasses(Class<?> testClass){
        testClasses.add(testClass);
    }

    public TafResult run() throws ExecutionException, InterruptedException {
        jUnitCore.removeListener(TestRun.tafRunListener);
        TafResult tafResult = new TafResult();
        JUnitCore jUnitCore = new JUnitCore();
        TafRunListener runListener = new TafRunListener();
        Set<Future<Result>> set = new HashSet<Future<Result>>();
        RunNotifier runNotifier = new RunNotifier();
        runListener.testRunStarted(null);
        for(Class<?> c : testClasses){
            Method[] methodsInClass = c.getDeclaredMethods();
            for(Method method : methodsInClass){
                Request testMethodRequest = Request.method(c, method.getName());
                set.add(threadPoolExecutor.submit(new TestCaseRunner(testMethodRequest, jUnitCore, runNotifier)));
            }
        }
        threadPoolExecutor.shutdown();
        threadPoolExecutor.awaitTermination(60, TimeUnit.MINUTES);
        if(!threadPoolExecutor.isTerminated()) threadPoolExecutor.shutdownNow();
        try {
            runListener.testRunFinished(new Result());
        } catch (Exception e) {
            e.printStackTrace();
        }
        for(Future<Result> future : set){
            tafResult.addTestResult(future.get());
        }
        jUnitCore.removeListener(TestRun.tafRunListener);
        return tafResult;
    }


}
