package se.claremont.autotest.common.testrun;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;


/**
 * Created by jordam on 2017-01-20.
 */
public class RunListener implements Runnable {
    Thread thread;

    public RunListener(Thread thread){
        this.thread = thread;
    }

    @Override
    public void run() {
        while(!classIsRunning("org.junit.runner.Runner")){
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Run started.");
        while(classIsRunning("org.junit.runner.Runner")){
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Run finished.");
    }

    public boolean classIsRunning(String className) {
        ClassLoader myCL = thread.getContextClassLoader();
        while (myCL != null) {
            try {
                for (Iterator iter = list(myCL); iter.hasNext();) {
                    if(iter.next().toString().toLowerCase().contains(className.toLowerCase())) return true;

                    //System.out.println("\t" + iter.next());
                }
            } catch (ConcurrentModificationException ignored) {
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            myCL = myCL.getParent();
        }
        return false;
    }

    private static Iterator list(ClassLoader CL)
            throws NoSuchFieldException, SecurityException,
            IllegalArgumentException, IllegalAccessException {
        Class CL_class = CL.getClass();
        while (CL_class != java.lang.ClassLoader.class) {
            CL_class = CL_class.getSuperclass();
        }
        java.lang.reflect.Field ClassLoader_classes_field = CL_class
                .getDeclaredField("classes");
        ClassLoader_classes_field.setAccessible(true);
        Vector classes = (Vector) ClassLoader_classes_field.get(CL);
        return classes.iterator();
    }

    public static class MultiThreadCompletionChecker implements Runnable{
        List<Thread> threads;

        public MultiThreadCompletionChecker(List<Thread> threads){
            this.threads = threads;
        }

        @Override
        public void run() {
            //Waiting for threads to finish
            for (int i = 0; i < threads.size(); i++)
                try {
                    threads.get(i).join();
                } catch (InterruptedException e) {
                }
        }
    }

}
