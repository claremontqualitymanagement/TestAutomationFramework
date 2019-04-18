package se.claremont.taf.core.junitcustomization;

import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.util.ArrayList;
import java.util.List;

public class TafResult {
    private int count;
    private int ignoreCount;
    private int failureCount;
    private final List<Failure> failures;
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
        this.failures.addAll(junitResult.getFailures());
    }

    public void addTestResult(TafResult tafResult){
        this.count += tafResult.getRunCount();
        this.ignoreCount += tafResult.getIgnoreCount();
        this.failureCount += tafResult.getFailureCount();
        this.failures.addAll(tafResult.getFailures());
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

    @SuppressWarnings("unused")
    public long getRunTime(){
        return System.currentTimeMillis() - startTime;
    }

    public boolean wasSuccessful(){
        return failureCount == 0;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("[TafResult: Tests run = ").append(count).append(", ignored = ").append(ignoreCount).append(", failed = ").append(failureCount).append("]");
        if(failureCount > 0){
            sb.append("Failures:").append(System.lineSeparator());
            for(Failure f : failures){
                sb.append(f.toString()).append(System.lineSeparator());
            }
        }
        return sb.toString();
    }
}
