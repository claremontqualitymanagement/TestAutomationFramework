package se.claremont.autotest.javasupport.applicationundertest.applicationcontext;

import se.claremont.autotest.common.testcase.TestCase;

public class PropertiesManager {
    TestCase testCase;

    public PropertiesManager(TestCase testCase){
        this.testCase = testCase;
    }

    /**
     * Equivalent of java CLI start parameter -D
     *
     * @param propertyName Name of variable to set (the rest of the parameter after '-D' at the command prompt.
     * @param propertyValue The environment value to set.
     */
    public void setProperty(String propertyName, String propertyValue){
        System.setProperty(propertyName, propertyValue);
    }

    public String getPropertyValue(String propertyName){
        return System.getProperty(propertyName);
    }
}
