package se.claremont.autotest.javasupport.applicationundertest.applicationstarters;

import se.claremont.autotest.common.testcase.TestCase;

import java.util.ArrayList;
import java.util.List;

public class ApplicationStartMechanism {
    public String startUrlOrPathToJarFile;
    public String mainClass;
    TestCase testCase;
    public List<String> arguments = new ArrayList<>();
    ClassLoader classLoader;

    public ApplicationStartMechanism(TestCase testCase){
        this.testCase = testCase;
    }

    public void run(){

    }
}
