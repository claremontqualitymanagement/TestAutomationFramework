package se.claremont.autotest.javasupport.applicationundertest.applicationcontext;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import se.claremont.autotest.common.logging.LogLevel;
import se.claremont.autotest.common.testcase.TestCase;

import java.lang.reflect.Field;
import java.util.*;

public class EnvironmentVariableManager {
    @JsonIgnore private TestCase testCase;
    @JsonProperty
    public List<String> appliedVariableChanges = new ArrayList<>();

    public EnvironmentVariableManager(TestCase testCase){
        this.testCase = testCase;
    }

    public void setEnvironmentVariables(Map<String, String> variables){
        ProcessBuilder pb = new ProcessBuilder();
        Map<String, String> env = pb.environment();
        for(String key : variables.keySet()){
            env.put(key, variables.get(key));
        }
        boolean success = setEnv(env);
        if(success) {
            StringBuilder sb = new StringBuilder();
            sb.append("The following environment variables were given the following values:").append(System.lineSeparator());
            for(String key : variables.keySet()){
                sb.append("   * '").append(key).append("' = '").append(variables.get(key)).append("'").append(System.lineSeparator());
            }
            log(LogLevel.EXECUTED, sb.toString());
        }
    }

    public void setEnvironmentVariable(String variableName, String variableValue){
        ProcessBuilder pb = new ProcessBuilder();
        Map<String, String> env = pb.environment();
        env.put(variableName, variableValue);
        boolean success = setEnv(env);
        if(success) log(LogLevel.EXECUTED, "Environment variable '" + variableName + "' set to '" + variableValue + "'.");
        appliedVariableChanges.add(variableName + "=" + variableValue);
    }

    public List<String> currentEnvironmentVariables(){
        ProcessBuilder pb = new ProcessBuilder();
        List<String> variables = new ArrayList<>();
        Map<String, String> env = pb.environment();
        for(String key : env.keySet()){
            variables.add("Environment variable: '" + key + "' = '" + env.get(key) + "'.");
        }
        return variables;
    }

    protected boolean setEnv(Map<String, String> newenv)
    {
        boolean success = false;
        try
        {
            Class<?> processEnvironmentClass = Class.forName("java.lang.ProcessEnvironment");
            Field theEnvironmentField = processEnvironmentClass.getDeclaredField("theEnvironment");
            theEnvironmentField.setAccessible(true);
            Map<String, String> env = (Map<String, String>) theEnvironmentField.get(null);
            env.putAll(newenv);
            Field theCaseInsensitiveEnvironmentField = processEnvironmentClass.getDeclaredField("theCaseInsensitiveEnvironment");
            theCaseInsensitiveEnvironmentField.setAccessible(true);
            Map<String, String> cienv = (Map<String, String>)     theCaseInsensitiveEnvironmentField.get(null);
            cienv.putAll(newenv);
            success = true;
        }
        catch (NoSuchFieldException e)
        {
            try {
                Class[] classes = Collections.class.getDeclaredClasses();
                Map<String, String> env = System.getenv();
                for(Class cl : classes) {
                    if("java.util.Collections$UnmodifiableMap".equals(cl.getName())) {
                        Field field = cl.getDeclaredField("m");
                        field.setAccessible(true);
                        Object obj = field.get(env);
                        Map<String, String> map = (Map<String, String>) obj;
                        map.clear();
                        map.putAll(newenv);
                    }
                }
                success = true;
            } catch (Exception e2) {
                log(LogLevel.EXECUTION_PROBLEM, "Could not set environment variable. Error: " + e2.toString());
            }
        } catch (Exception e1) {
            log(LogLevel.EXECUTION_PROBLEM, "Could not set environment variable. Error: " + e1.toString());
        }
        return success;
    }

    private void log(LogLevel logLevel, String message){
        if(testCase == null){
            System.out.println(logLevel.toString() + ": " + message);
        } else {
            testCase.log(logLevel, message);
        }
    }


}
