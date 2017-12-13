package se.claremont.autotest.javasupport.applicationundertest.applicationcontext;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import se.claremont.autotest.common.testcase.TestCase;

import java.util.ArrayList;
import java.util.List;

public class PropertiesManager {
    @JsonIgnore private final TestCase testCase;
    @JsonProperty
    public List<String> appliedProperties = new ArrayList<>();


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
        appliedProperties.add(propertyName + "=" + propertyValue);
    }

    public String getPropertyValue(String propertyName){
        return System.getProperty(propertyName);
    }
}
