package se.claremont.autotest.common.testrun;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

/**
 * A custom JUnit TestListener to enable fancier reporting
 *
 * Created by jordam on 2017-01-20.
 */
public class TafTestRunner extends BlockJUnit4ClassRunner
{
    public TafTestRunner(Class<?> klass) throws InitializationError
    {
        super(klass);
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
        //notifier.addListener(TestRun.tafRunListener);
        super.run(notifier);
    }
}