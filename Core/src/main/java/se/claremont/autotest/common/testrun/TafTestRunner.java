package se.claremont.autotest.common.testrun;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerScheduler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * A custom JUnit TestListener to enable fancier reporting
 *
 * Created by jordam on 2017-01-20.
 */
public class TafTestRunner extends BlockJUnit4ClassRunner
{
    Integer parallelThreads = 1;
    static ExecutorService threadPool;

    public TafTestRunner(Class<?> klass) throws InitializationError
    {
        super(klass);
        parallelThreads = (TestRun.getSettingsValue(Settings.SettingParameters.PARALLEL_TEST_EXECUTION_MODE) == null || TestRun.getSettingsValue(Settings.SettingParameters.PARALLEL_TEST_EXECUTION_MODE).length() == 0) ? 1 : Integer.parseInt(TestRun.getSettingsValue(Settings.SettingParameters.PARALLEL_TEST_EXECUTION_MODE));
        TestRun.setSettingsValue(Settings.SettingParameters.PARALLEL_TEST_EXECUTION_MODE, parallelThreads.toString());
        System.out.println("-----------------------");
        System.out.println("parallelThreads: " + parallelThreads);
        System.out.println("PARALLEL_TEST_EXECUTION_MODE: " + TestRun.getSettingsValue(Settings.SettingParameters.PARALLEL_TEST_EXECUTION_MODE));
        if(parallelThreads == null || parallelThreads < 1) parallelThreads = 1;
        int testCaseCount = computeTestMethods().size();
        if(parallelThreads > testCaseCount) parallelThreads = testCaseCount;
        System.out.println("-----------------------");
        System.out.println("testCaseCount: " + testCaseCount);
        System.out.println("parallelThreads: " + parallelThreads);
        System.out.println("PARALLEL_TEST_EXECUTION_MODE: " + TestRun.getSettingsValue(Settings.SettingParameters.PARALLEL_TEST_EXECUTION_MODE));
        System.out.println("-----------------------");
        this.threadPool = Executors.newFixedThreadPool((int)parallelThreads);
        setScheduler(new ParallelScheduler(threadPool));
    }

    public static ExecutorService getThreadPool(){
        return threadPool;
    }

    public int getParallelThreadCount(){
        return parallelThreads;
    }


    @Override
    public void run(RunNotifier notifier)
    {
        TestRun.initializeIfNotInitialized();
        try{
            notifier.removeListener(TestRun.tafRunListener);
        }catch (Exception e){
            System.out.println("Warning: Trying to remove run listener that doesn't exist.");
        }
        notifier.addFirstListener(TestRun.tafRunListener);

    }

    public class ParallelScheduler implements RunnerScheduler {
        private ExecutorService threadPool;

        public ParallelScheduler(ExecutorService threadPool){
            this.threadPool = threadPool;
        }

        public ExecutorService getThreadPool(){
            return threadPool;
        }

        @Override
        public void schedule(Runnable childStatement) {
            threadPool.submit(childStatement);
        }

        @Override
        public void finished() {
        }
    }
}