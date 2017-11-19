package se.claremont.autotest.common.junitcustomization;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Request;

import se.claremont.autotest.common.testcase.TestCaseRunner;
import se.claremont.autotest.common.testset.TestSet;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

public class TafParallelTestCaseRunner {

    private final ThreadPoolExecutor threadPoolExecutor;
    private final List<Class<?>> testClasses = new ArrayList<>();
    public static Set<TestSet> testSets = new HashSet<>();
    public static final Set<String> testSetNames = new HashSet<>();

    public TafParallelTestCaseRunner(int threadCount){
        threadPoolExecutor = (ThreadPoolExecutor)Executors.newFixedThreadPool(threadCount);
    }

    public void addTestClasses(Class<?> testClass){
        testClasses.add(testClass);
    }

    public TafResult run() throws ExecutionException, InterruptedException {
        TafResult tafResult = new TafResult();
        Set<Future<TafResult>> set = new HashSet<>();
        for(Class<?> c : testClasses){
            Method[] methodsInClass = c.getDeclaredMethods();
            for(Method method : methodsInClass){
                if(!method.isAnnotationPresent(Test.class))continue;
                Request testMethodRequest = Request.method(c, method.getName());
                set.add(threadPoolExecutor.submit(new TestCaseRunner(testMethodRequest)));
            }
        }
        threadPoolExecutor.shutdown();
        threadPoolExecutor.awaitTermination(60, TimeUnit.MINUTES);
        if(!threadPoolExecutor.isTerminated()) threadPoolExecutor.shutdownNow();
        for(Future<TafResult> future : set){
            tafResult.addTestResult(future.get());
        }
        return tafResult;
    }


}
