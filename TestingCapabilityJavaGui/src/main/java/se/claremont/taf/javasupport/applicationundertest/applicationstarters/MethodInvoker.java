package se.claremont.taf.javasupport.applicationundertest.applicationstarters;

import se.claremont.taf.logging.LogLevel;
import se.claremont.taf.testcase.TestCase;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class MethodInvoker {

    public static Object invokeMethodOfClass(ClassLoader classLoader, TestCase testCase, String className, String methodName, String[] args) {
        try{
            if(className == null) return null;
            Class c = classLoader.loadClass(className);
            if(testCase!= null) testCase.log(LogLevel.DEBUG, "Loaded class '" + className + "'.");
            @SuppressWarnings("unchecked") Method m = c.getMethod(methodName, args.getClass());
            if(testCase!= null) testCase.log(LogLevel.DEBUG, "Found method 'main'.");
            m.setAccessible(true);
            int mods = m.getModifiers();
            if (m.getReturnType() != void.class || !Modifier.isStatic(mods) ||
                    !Modifier.isPublic(mods)) {
                if(testCase != null) testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not find any '" + methodName + "' method in class '" + className + "'.");
                return null;
            }
            try {
                Object object = m.invoke(null, new Object[] { args });
                if(testCase != null)testCase.log(LogLevel.DEBUG, "Invoked method '" + methodName + "'.");
                return object;
            } catch (IllegalAccessException e) {
                // This should not happen, as we have disabled access checks
            }
        } catch (ClassNotFoundException e) {
            if(testCase != null) testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not identify class '" + className + "'. Error: " + e.getMessage());
        } catch (InvocationTargetException e) {
            if(testCase != null) testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not invoke class '" + className + "'. Error: " + e.getMessage());
        } catch (NoSuchMethodException e) {
            if(testCase != null) testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not find method '" + methodName + "' in class '" + className + "'. Error: " + e.getMessage());
        }
        return null;
    }
}
