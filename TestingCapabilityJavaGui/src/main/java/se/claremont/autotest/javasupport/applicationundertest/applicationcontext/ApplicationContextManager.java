package se.claremont.autotest.javasupport.applicationundertest.applicationcontext;

import se.claremont.autotest.common.testcase.TestCase;

public class ApplicationContextManager {
    public LibraryLoader loadedLibraries;
    public EnvironmentVariableManager environmentVariables;
    public JavaVmRuntimeChanger jvmSettings;
    public PropertiesManager properties;

    public ApplicationContextManager(TestCase testCase){
        loadedLibraries = new LibraryLoader(testCase);
        environmentVariables = new EnvironmentVariableManager(testCase);
        jvmSettings = new JavaVmRuntimeChanger(testCase);
        properties = new PropertiesManager(testCase);
    }
}
