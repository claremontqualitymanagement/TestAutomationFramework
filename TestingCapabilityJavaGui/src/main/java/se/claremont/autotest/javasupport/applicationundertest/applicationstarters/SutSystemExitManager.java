package se.claremont.autotest.javasupport.applicationundertest.applicationstarters;

public class SutSystemExitManager extends SecurityManager {
    @Override public void checkExit(int status) {
        throw new SecurityException();
    }
}