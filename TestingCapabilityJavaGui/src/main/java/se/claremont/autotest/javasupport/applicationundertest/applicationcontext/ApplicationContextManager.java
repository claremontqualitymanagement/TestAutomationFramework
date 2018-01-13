package se.claremont.autotest.javasupport.applicationundertest.applicationcontext;

import com.fasterxml.jackson.annotation.JsonProperty;
import se.claremont.autotest.common.testcase.TestCase;

import java.io.Serializable;

public class ApplicationContextManager implements Serializable{
    @JsonProperty public LibraryLoader loadedLibraries;
    @JsonProperty public EnvironmentVariableManager environmentVariables;
    @JsonProperty public JavaVmRuntimeChanger jvmSettings;
    @JsonProperty public PropertiesManager properties;

    private ApplicationContextManager(){//For JSON parsing to work
        TestCase testCase = new TestCase();
        loadedLibraries = new LibraryLoader(testCase);
        environmentVariables = new EnvironmentVariableManager(testCase);
        jvmSettings = new JavaVmRuntimeChanger(testCase);
        properties = new PropertiesManager(testCase);
    }

    public ApplicationContextManager(TestCase testCase){
        loadedLibraries = new LibraryLoader(testCase);
        environmentVariables = new EnvironmentVariableManager(testCase);
        jvmSettings = new JavaVmRuntimeChanger(testCase);
        properties = new PropertiesManager(testCase);
    }
}
