package se.claremont.autotest.common.junitcustomization;

import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.Result;
import org.junit.runner.notification.RunNotifier;
import se.claremont.autotest.common.testcase.TestCaseRunner;
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
        TafResult tafResult = new TafResult();
        JUnitCore jUnitCore = new JUnitCore();
        TafRunListener runListener = new TafRunListener();
        Set<Future<TafResult>> set = new HashSet<Future<TafResult>>();
        RunNotifier runNotifier = new RunNotifier();
        runListener.testRunStarted(null);
        for(Class<?> c : testClasses){
            Method[] methodsInClass = c.getDeclaredMethods();
            for(Method method : methodsInClass){
                Request testMethodRequest = Request.method(c, method.getName());
                String testName = method.getName();
                set.add(threadPoolExecutor.submit(new TestCaseRunner(testMethodRequest, jUnitCore, runNotifier, testName)));
            }
        }
        threadPoolExecutor.shutdown();
        threadPoolExecutor.awaitTermination(60, TimeUnit.MINUTES);
        if(!threadPoolExecutor.isTerminated()) threadPoolExecutor.shutdownNow();
        for(Future<TafResult> future : set){
            tafResult.addTestResult(future.get());
        }
        runListener.testRunFinished(tafResult);
        return tafResult;
    }


}
