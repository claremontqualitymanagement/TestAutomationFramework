package se.claremont.autotest.javasupport.applicationundertest.applicationcontext;

import com.fasterxml.jackson.annotation.JsonProperty;
import se.claremont.autotest.common.testcase.TestCase;

public class ApplicationContextManager {
    @JsonProperty public LibraryLoader loadedLibraries;
    @JsonProperty public EnvironmentVariableManager environmentVariables;
    @JsonProperty public JavaVmRuntimeChanger jvmSettings;
    @JsonProperty public PropertiesManager properties;

    public ApplicationContextManager(TestCase testCase){
        loadedLibraries = new LibraryLoader(testCase);
        environmentVariables = new EnvironmentVariableManager(testCase);
        jvmSettings = new JavaVmRuntimeChanger(testCase);
        properties = new PropertiesManager(testCase);
    }
}
