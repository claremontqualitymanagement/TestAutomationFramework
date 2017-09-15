package se.claremont.autotest.javasupport.applicationundertest.applicationstarters;

import se.claremont.autotest.common.support.StringManagement;
import se.claremont.autotest.common.testcase.TestCase;

import java.net.MalformedURLException;
import java.net.URL;
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
        ApplicationStarter as = new ApplicationStarter(testCase);
        try {
            as.startJar(new URL(startUrlOrPathToJarFile), mainClass, arguments.toArray(new String[0]));
        } catch (MalformedURLException e) {
            System.out.println("Could not start program '" + startUrlOrPathToJarFile + "', with start class '" + mainClass + "' and arguments '" + StringManagement.join("', '", arguments) + "'. Error:" + e.toString());
        }
    }
}
