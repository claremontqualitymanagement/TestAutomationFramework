package se.claremont.autotest.common.junitcustomization;

import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import se.claremont.autotest.common.testcase.TestCase;
import se.claremont.autotest.common.testcase.TestCaseRunner;
import se.claremont.autotest.common.testrun.TestRun;
import se.claremont.autotest.common.testset.TestSet;
import se.claremont.autotest.common.testset.UnitTestClass;

import java.lang.reflect.InvocationTargetException;
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
        Set<Future<Result>> set = new HashSet<Future<Result>>();
        RunNotifier runNotifier = new RunNotifier();
        runListener.testRunStarted(null);
        for(Class<?> c : testClasses){
            if(!c.isAssignableFrom(TestSet.class) || !c.isAssignableFrom(UnitTestClass.class)){
                System.out.println("WARNING: Class " + c.getName() + " does not seem to extend TestSet or UnitTestClass. Running it antway,");
            }
            Method[] methodsInClass = c.getDeclaredMethods();
            for(Method method : methodsInClass){
                TestCase testCase = new TestCase(null, method.getName(), c.getName());
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
        return tafResult;
    }

    private void setTestSetNameIfApplicable(Class<?> c){
        if(!c.isAssignableFrom(TestSet.class))return;
        Method[] methodsInClass = c.getDeclaredMethods();
        for(Method method : methodsInClass){
            if(method.getName().equals("setName")){
                try {
                    method.invoke(c.getName());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
