package se.claremont.taf.javasupport.applicationundertest.applicationcontext;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import se.claremont.taf.core.testcase.TestCase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PropertiesManager implements Serializable{
    @JsonIgnore private final transient TestCase testCase;
    @JsonProperty
    public List<String> appliedProperties = new ArrayList<>();

    private PropertiesManager(){ //For JSON parsing
        this.testCase = new TestCase();
    }

    public PropertiesManager(TestCase testCase){
        this.testCase = testCase;
    }

    /**
     * Equivalent of java CLI start parameter -D
     *
     * @param propertyName Name of variable to set (the rest of the parameter after '-D' at the command prompt.
     * @param propertyValue The environment value to set.
     */
    @JsonIgnore
    public void setProperty(String propertyName, String propertyValue){
        System.setProperty(propertyName, propertyValue);
        appliedProperties.add(propertyName + "=" + propertyValue);
    }

    @JsonIgnore
    public String getPropertyValue(String propertyName){
        return System.getProperty(propertyName);
    }
}
