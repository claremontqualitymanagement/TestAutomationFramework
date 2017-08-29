package se.claremont.autotest.javasupport.applicationundertest.applicationstarters;

import se.claremont.autotest.common.logging.LogLevel;
import se.claremont.autotest.common.testcase.TestCase;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class MethodInvoker {

    public static Object invokeMethodOfClass(ClassLoader classLoader, TestCase testCase, String className, String methodName, String[] args) {
        try{
            Class c = classLoader.loadClass(className);
            testCase.log(LogLevel.DEBUG, "Loaded class '" + className + "'.");
            @SuppressWarnings("unchecked") Method m = c.getMethod(methodName, args.getClass());
            testCase.log(LogLevel.DEBUG, "Found method 'main'.");
            m.setAccessible(true);
            int mods = m.getModifiers();
            if (m.getReturnType() != void.class || !Modifier.isStatic(mods) ||
                    !Modifier.isPublic(mods)) {
                testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not find any '" + methodName + "' method in class '" + className + "'.");
                return null;
            }
            try {
                Object object = m.invoke(null, new Object[] { args });
                testCase.log(LogLevel.DEBUG, "Invoked method '" + methodName + "'.");
                return object;
            } catch (IllegalAccessException e) {
                // This should not happen, as we have disabled access checks
            }
        } catch (ClassNotFoundException e) {
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not identify class '" + className + "'. Error: " + e.getMessage());
        } catch (InvocationTargetException e) {
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not invoke class '" + className + "'. Error: " + e.getMessage());
        } catch (NoSuchMethodException e) {
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not find method '" + methodName + "' in class '" + className + "'. Error: " + e.getMessage());
        }
        return null;
    }
}
