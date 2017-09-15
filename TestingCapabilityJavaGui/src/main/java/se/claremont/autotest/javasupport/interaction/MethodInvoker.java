package se.claremont.autotest.javasupport.interaction;

import se.claremont.autotest.common.logging.LogLevel;
import se.claremont.autotest.common.support.StringManagement;
import se.claremont.autotest.common.testcase.TestCase;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * This is a helper to invoke methods on any object at runtime in a generic fashion.
 *
 * Created by jordam on 2017-02-10.
 */
@SuppressWarnings("WeakerAccess")
public class MethodInvoker {

    TestCase testCase = null;

    public MethodInvoker(TestCase testCase){
        this.testCase = testCase;
    }

    public MethodInvoker(){
        this.testCase = null;
    }

    /**
     * Tries to invoke the methods given upon the object given, in order of declaration.
     * If it is possible to invoke a method it returns the result, and otherwise it will return null.
     *
     * @param component The object to invoke the method on.
     * @param methodNames List of method names in order of invokation attempts.
     * @return Returns whatever the method invoked would return.
     */
    public Object invokeTheFirstEncounteredMethod(Object component, List<String> methodNames){
        return invokeTheFirstEncounteredMethod(component, methodNames, (Object[]) null);
    }

    /**
     * Tries to invoke the methods given upon the object given, in order of declaration.
     * If it is possible to invoke a method it returns the result, and otherwise it will return null.
     * If a test case is provided the progress is being logged, but as DEBUG log level comments.
     *
     * @param testCase The test case to log to. If no test case is provided output is to console.
     * @param component The object to invoke the method on.
     * @param methodNames List of method names in order of invokation attempts.
     * @return Returns whatever the method invoked would return.
     */
    public static Object invokeTheFirstEncounteredMethod(TestCase testCase, Object component, String[] methodNames){
        MethodInvoker m = new MethodInvoker(testCase);
        return m.invokeTheFirstEncounteredMethod(component, methodNames);
    }

    public static List<String> getAvailableMethods(Object object){
        MethodInvoker m = new MethodInvoker();
        return m.getAvalableMethods(object);
    }

    /**
     * Tries to invoke the methods given upon the object given, in order of declaration.
     * If it is possible to invoke a method it returns the result, and otherwise it will return null.
     * If a test case is provided the progress is being logged, but as DEBUG log level comments.
     *
     * @param testCase The test case to log to. If no test case is provided output is to console.
     * @param component The object to invoke the method on.
     * @param methodNames List of method names in order of invokation attempts.
     * @param args Method arguments to use.
     * @return Returns whatever the method invoked would return.
     */
    public static Object invokeTheFirstEncounteredMethod(TestCase testCase, Object component, String[] methodNames, Object... args){
        MethodInvoker m = new MethodInvoker(testCase);
        return m.invokeTheFirstEncounteredMethod(component, methodNames, args);
    }

    /**
     * Tries to invoke the methods given upon the object given, in order of declaration.
     * If it is possible to invoke a method it returns the result, and otherwise it will return null.
     *
     * @param component The object to invoke the method on.
     * @param methodNames List of method names in order of invokation attempts.
     * @return Returns whatever the method invoked would return.
     */
    public static Object invokeTheFirstEncounteredMethodFromListOfMethodNames(Object component, String[] methodNames){
        MethodInvoker m = new MethodInvoker(null);
        List<String> methods = new ArrayList<>();
        Collections.addAll(methods, methodNames);
        return m.invokeTheFirstEncounteredMethod(component, methods, (Object[]) null);
    }
    /**
     * Tries to invoke the methods given upon the object given, in order of declaration.
     * If it is possible to invoke a method it returns the result, and otherwise it will return null.
     *
     * @param component The object to invoke the method on.
     * @param methodNames List of method names in order of invokation attempts.
     * @return Returns whatever the method invoked would return.
     */
    public Object invokeTheFirstEncounteredMethod(Object component, String[] methodNames){
        List<String> methods = new ArrayList<>();
        Collections.addAll(methods, methodNames);
        return invokeTheFirstEncounteredMethod(component, methods, (Object[]) null);
    }

    /**
     * Tries to invoke the methods given upon the object given, in order of declaration.
     * If it is possible to invoke a method it returns the result, and otherwise it will return null.
     *
     * @param component The object to invoke the method on.
     * @param methodNames List of method names in order of invokation attempts.
     * @param args Method arguments to use.
     * @return Returns whatever the method invoked would return.
     */
    public Object invokeTheFirstEncounteredMethod(Object component, String[] methodNames, Object... args){
        List<String> methods = new ArrayList<>();
        Collections.addAll(methods, methodNames);
        return invokeTheFirstEncounteredMethod(component, methods, args);
    }

    /**
     * Tries to invoke the methods given upon the object given, in order of declaration.
     * If it is possible to invoke a method it returns the result, and otherwise it will return null.
     *
     * @param component The object to invoke the method on.
     * @param methodNames List of method names in order of invokation attempts.
     * @param args Method arguments to use.
     * @return Returns whatever the method invoked would return.
     */
    public static Object invokeTheFirstEncounteredMethodFromListOfMethodNames(Object component, String[] methodNames, Object... args){
        MethodInvoker m = new MethodInvoker(null);
        return m.invokeTheFirstEncounteredMethod(component, methodNames, args);
    }

    /**
     * Tries to invoke the methods given upon the object given, in order of declaration.
     * If it is possible to invoke a method it returns the result, and otherwise it will return null.
     *
     * @param component The object to invoke the method on.
     * @param methodNames List of method names in order of invokation attempts.
     * @param args Method arguments to use.
     * @return Returns whatever the method invoked would return.
     */
    public Object invokeTheFirstEncounteredMethod(Object component, List<String> methodNames, Object... args){
        if(component == null || methodNames == null || methodNames.size() == 0){
            log(LogLevel.DEBUG, "Could not invoke any of the methods ('" + StringManagement.join("', '", methodNames) + "') since the object to invoke them on was null.");
            return null;
        }
        for(String methodName : methodNames){
            if(!objectHasMethod(component, methodName)) continue;
            if(args == null){
                return tryInvokeMethod(component, methodName);
            }
            Class<?> c = component.getClass();
            for(Method m : c.getMethods()){
                if(m.toString().contains(methodName)){
                    String returnstring = "Invoking method '" + m.toString() + "' with arguments '" + Arrays.toString(args) + "'";
                    try {
                        String returnString = ".";
                        Object returnObject = m.invoke(component, args);
                        if(returnObject != null){
                            returnString =  " and retrieved the object '" + returnObject.toString() + "'";
                        }
                        log(LogLevel.DEBUG, returnString + ".");
                        return returnObject;
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        log(LogLevel.DEBUG, "Error while trying to invoke method '" + m.toString() + "'. Error: " + e.toString());
                        return null;
                    }
                }
            }
        }
        log(LogLevel.FRAMEWORK_ERROR, "None of the suggested methods ('" + StringManagement.join("', '", methodNames) + "') were found to be suitable for element of class '" + getClassName(component) + "'. The methods available for this object are:" + System.lineSeparator() + StringManagement.join(System.lineSeparator(), getAvalableMethods(component)));
        return null;
    }

    /**
     * Tries to invoke the method given upon the object given.
     * If it is possible to invoke the method it returns the result, and otherwise it will return null.
     * If a test case is provided the progress is being logged, but as DEBUG log level comments.
     *
     * @param testCase The test case to log to. If no test case is provided output is to console.
     * @param component The object to invoke the method on.
     * @param methodName Method name for method to invoke.
     * @param args The arguments to pass to the method invoked.
     * @return Returns whatever the method invoked would return.
     */
    public static Object invokeMethod(TestCase testCase, Object component, String methodName, Object... args){
        MethodInvoker m = new MethodInvoker(testCase);
        return m.invokeMethod(component, methodName, args);
    }

    /**
     * Tries to invoke the method given upon the object given.
     * If it is possible to invoke the method it returns the result, and otherwise it will return null.
     *
     * @param component The object to invoke the method on.
     * @param methodName Method name for method to invoke.
     * @param args Method arguments to use.
     * @return Returns whatever the method invoked would return.
     */
    public Object invokeMethod(Object component, String methodName, Object... args){
        if(args == null) return invokeMethod(component, methodName);
        if(component == null || methodName == null || methodName.equals("")){
            log(LogLevel.EXECUTION_PROBLEM, "Could not invoke method '" + methodName + "' on null element.");
            return null;
        }
        Class<?> c = component.getClass();
        for(Method m : c.getMethods()){
            if(m.toString().contains("." + methodName)){
                try {
                    Object returnObject = m.invoke(component, args);
                    String returnString = ".";
                    if(returnObject != null){
                        returnString =  " and retrieved the object '" + returnObject.toString() + "'.";
                    }
                    log(LogLevel.EXECUTED, "Invoked method '" + methodName + "' on component of class '" + getClassName(component) + "'" + returnString);
                    return returnObject;
                } catch (IllegalAccessException | InvocationTargetException e) {
                    log(LogLevel.EXECUTION_PROBLEM, "Could not invoke method '" + methodName + "' on component of class '" + getClassName(component) + "'. Error: " + e.getMessage());
                    return null;
                }
            }
        }
        log(LogLevel.EXECUTION_PROBLEM, "Tried invoking method '" + methodName + "' on component, but that method could not be found for this component." + System.lineSeparator() +
                "Class is '" + getClassName(component) + "' and available methods are:" + System.lineSeparator() + StringManagement.join(System.lineSeparator(), getAvalableMethods(component)) +
                System.lineSeparator() + "Remember to cast any return object upon usage.");
        return null;
    }

    /**
     * Tries to invoke the method given upon the object given.
     * If it is possible to invoke the method it returns the result, and otherwise it will return null.
     *
     * @param component The object to invoke the method on.
     * @param methodName Method name for method to invoke.
     * @return Returns whatever the method invoked would return.
     */
    public Object invokeMethod(Object component, String methodName){
        if(component == null || methodName == null || methodName.equals("")){
            log(LogLevel.EXECUTION_PROBLEM, "Could not invoke method '" + methodName + "' on null element.");
            return null;
        }
        Class<?> c = component.getClass();
        for(Method m : c.getMethods()){
            if(m.toString().contains("." + methodName)){
                try {
                    Object returnObject = m.invoke(component);
                    String returnString = ".";
                    if(returnObject != null){
                        returnString =  " and retrieved the object '" + returnObject.toString() + "'.";
                    }
                    log(LogLevel.EXECUTED, "Invoked method '" + methodName + "' on component of class '" + getClassName(component) + "'" + returnString);
                    return returnObject;
                } catch (IllegalAccessException | InvocationTargetException e) {
                    log(LogLevel.EXECUTION_PROBLEM, "Could not invoke method '" + methodName + "' on component of class '" + getClassName(component) + "'. Error: " + e.getMessage());
                    return null;
                }
            }
        }
        log(LogLevel.EXECUTION_PROBLEM, "Tried invoking method '" + methodName + "' on component, but that method could not be found for this component." + System.lineSeparator() +
                "Class is '" + getClassName(component) + "' and available methods are:" + System.lineSeparator() + StringManagement.join(System.lineSeparator(), getAvalableMethods(component)) +
                System.lineSeparator() + "Remember to cast any return object upon usage.");
        return null;
    }


    private Object tryInvokeMethod(Object component, String methodName){
        if(component == null || methodName == null || methodName.equals("")){
            log(LogLevel.DEBUG, "Cannot invoke method '" + methodName + "' on object since either of them are null and both are needed.");
            return null;
        }
        Class<?> c = component.getClass();
        for(Method method : c.getMethods()){
            if(method.toString().endsWith("." + methodName)) {
                try {
                    String logString = "Invoking method '" + method.toString() + "' on object of class '" + getClassName(component) + "'";
                    Object returnObject = method.invoke(component);
                    if (returnObject != null)
                        logString += " and received '" + returnObject.toString() + "' in response";
                    log(LogLevel.DEBUG, logString + ".");
                    return returnObject;
                } catch (IllegalAccessException | InvocationTargetException e) {
                    log(LogLevel.DEBUG, "Encountered problems when invoking method '" + method.toString() + "' on element of class '" + getClassName(component) + "'. Error: " + e.toString());
                }
            }
        }
        return null;
    }

    /**
     * Returns a list of available method for the object provided.
     *
     * @param component The object to list the available method for.
     * @return Returns a list of method names with their arguments types.
     */
    public ArrayList<String> getAvalableMethods(Object component){
        Class<?> c = component.getClass();
        ArrayList<String> methods = new ArrayList<>();
        for(Method m : c.getMethods()){
            methods. add(m.toString());
        }
        return methods;
    }

    /**
     * A check if the object seem to have the method provided.
     *
     * @param component Object.
     * @param methodName Method name.
     * @return Returns true if the method name is found among the methods of the object, otherwise false.
     */
    public boolean objectHasMethod(Object component, String methodName){
        if(component == null) return false;
        Class<?> c = component.getClass();
        for(Method m : c.getMethods()){
            if(m.toString().contains(methodName)){
                return true;
            }
        }
        return false;
    }

    /**
     * A check if the object seem to have any of the methods provided.
     *
     * @param component Object.
     * @param methodNames Method names.
     * @return Returns true if any of the method names are found among the methods of the object, otherwise false.
     */
    public boolean objectHasAnyOfTheMethods(Object component, String[] methodNames){
        for(String methodName : methodNames){
            if(objectHasMethod(component, methodName)) {
                return true;
            }
        }
        return false;
    }

    private Object tryInvokeMethod(Object object, Method method){
        if(object == null || method == null){
            log(LogLevel.DEBUG, "Cannot invoke method since method or object is null and both are needed.");
            return null;
        }
        try {
            String logString = "Invoking method '" + method.toString() + "' on object of class '" + getClassName(object) + "'";
            Object returnObject = method.invoke(object);
            if(returnObject != null) logString += " and received '" + returnObject.toString() + "' in response";
            log(LogLevel.DEBUG, logString + ".");
            return returnObject;
        } catch (IllegalAccessException | InvocationTargetException e) {
            log(LogLevel.DEBUG, "Encountered problems when invoking method '" + method.toString() + "' on element of class '" + getClassName(object) + "'. Error: " + e.toString());
        }
        return null;
    }

    private void log(LogLevel logLevel, String message){
        if(testCase == null)return;
        testCase.log(logLevel, message);
    }

    private String getClassName(Object object){
        if(object == null) return "[null]";
        String className = object.getClass().toString();
        String[] nameParts = className.split(" ");
        if(nameParts.length > 1){
            className = nameParts[nameParts.length-1];
        }
        return className;
    }

}
