package se.claremont.autotest.common.junitcustomization;

import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.util.ArrayList;
import java.util.List;

public class TafResult {
    private int count;
    private int ignoreCount;
    private int failureCount;
    private List<Failure> failures;
    private final Long startTime;

    public TafResult() {
        this.count = 0;
        this.ignoreCount = 0;
        this.failures = new ArrayList<>();
        this.failureCount = 0;
        this.startTime = System.currentTimeMillis();
    }

    public void addTestResult(Result junitResult){
        this.count += junitResult.getRunCount();
        this.ignoreCount += junitResult.getIgnoreCount();
        this.failureCount += junitResult.getFailureCount();
        for(Failure failure : junitResult.getFailures()){
            this.failures.add(failure);
        }
    }

    public void addTestResult(TafResult tafResult){
        this.count += tafResult.getRunCount();
        this.ignoreCount += tafResult.getIgnoreCount();
        this.failureCount += tafResult.getFailureCount();
        for(Failure failure : tafResult.getFailures()){
            this.failures.add(failure);
        }
    }

    public int getIgnoreCount(){
        return ignoreCount;
    }
    public int getRunCount(){
        return count;
    }

    public int getFailureCount(){
        return failureCount;
    }

    public List<Failure> getFailures(){
        return failures;
    }

    public long getRunTime(){
        return System.currentTimeMillis() - startTime;
    }

    public boolean wasSuccessful(){
        return failureCount == 0;
    }

    @Override
    public String toString(){
        String returnString = "[TafResult: Tests run = " + count + ", ignored = " + ignoreCount + ", failed = " + failureCount + "]";
        if(failureCount > 0){
            returnString += "Failures:" + System.lineSeparator();
            for(Failure f : failures){
                returnString += f.toString() + System.lineSeparator();
            }
        }
        return returnString;
    }
}
