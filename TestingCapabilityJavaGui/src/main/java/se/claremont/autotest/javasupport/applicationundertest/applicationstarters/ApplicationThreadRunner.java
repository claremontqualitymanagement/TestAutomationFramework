package se.claremont.autotest.javasupport.applicationundertest.applicationstarters;

import java.util.ArrayList;
import java.util.List;

public class ApplicationThreadRunner implements Runnable {

    ApplicationStartMechanism applicationStartMechanism;

    public ApplicationThreadRunner(ApplicationStartMechanism applicationStartMechanism) {
        this.applicationStartMechanism = applicationStartMechanism;
    }


    @Override
    public void run() {
        applicationStartMechanism.run();
    }

    public static Thread start(ApplicationStartMechanism applicationStartMechanism) {
        Thread thread = null;
        try {
            List<Thread> threadList = new ArrayList<>();
            thread = new Thread(new ApplicationThreadRunner(applicationStartMechanism));
            threadList.add(thread);
            thread.start();

        } catch (SecurityException e) {
            System.out.println("SUT application exited.");
        }
        return thread;
    }
}
