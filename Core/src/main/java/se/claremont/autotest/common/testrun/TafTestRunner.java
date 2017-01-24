package se.claremont.autotest.common.testrun;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

/**
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
        notifier.addListener(new TafRunListener());
        super.run(notifier);
    }
}