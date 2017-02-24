package se.claremont.autotest.javamethods;

import se.claremont.autotest.common.logging.LogLevel;
import se.claremont.autotest.common.testcase.TestCase;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
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

    public Object invokeTheFirstEncounteredMethod(Object component, List<String> methodNames){
        return invokeTheFirstEncounteredMethod(component, methodNames, (Object[]) null);
    }

    public static Object invokeTheFirstEncounteredMethod(TestCase testCase, Object component, String[] methodNames){
        MethodInvoker m = new MethodInvoker(testCase);
        return m.invokeTheFirstEncounteredMethod(component, methodNames);
    }

    public static List<String> getAvailableMethods(Object object){
        MethodInvoker m = new MethodInvoker();
        return m.getAvalableMethods(object);
    }

    public static Object invokeTheFirstEncounteredMethod(TestCase testCase, Object component, String[] methodNames, Object... args){
        MethodInvoker m = new MethodInvoker(testCase);
        return m.invokeTheFirstEncounteredMethod(component, methodNames, args);
    }

    public Object invokeTheFirstEncounteredMethod(Object component, String[] methodNames){
        List<String> methods = new ArrayList<>();
        for(String method : methodNames){
            methods.add(method);
        }
        return invokeTheFirstEncounteredMethod(component, methods, (Object[]) null);
    }

    public Object invokeTheFirstEncounteredMethod(Object component, String[] methodNames, Object... args){
        List<String> methods = new ArrayList<>();
        for(String method : methodNames){
            methods.add(method);
        }
        return invokeTheFirstEncounteredMethod(component, methods, args);
    }

    public Object invokeTheFirstEncounteredMethod(Object component, List<String> methodNames, Object... args){
        if(component == null || methodNames == null || methodNames.size() == 0){
            log(LogLevel.DEBUG, "Could not invoke any of the methods ('" + String.join("', '", methodNames) + "') since the object to invoke them on was null.");
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
                    String returnstring = "Invoking method '" + m.toString() + "' with arguments '" + args.toString() + "'";
                    try {
                        String returnString = ".";
                        Object returnObject = m.invoke(component, args);
                        if(returnObject != null){
                            returnString =  " and retrieved the object '" + returnObject.toString() + "'";
                        }
                        log(LogLevel.DEBUG, returnString + ".");
                        return returnObject;
                    } catch (IllegalAccessException e) {
                        log(LogLevel.DEBUG, "Error while trying to invoke method '" + m.toString() + "'. Error: " + e.toString());
                        return null;
                    } catch (InvocationTargetException e) {
                        log(LogLevel.DEBUG, "Error while trying to invoke method '" + m.toString() + "'. Error: " + e.toString());
                        return null;
                    }
                }
            }
        }
        log(LogLevel.FRAMEWORK_ERROR, "None of the suggested methods ('" + String.join("', '", methodNames) + "') were found to be suitable for element of class '" + component.getClass().toString() + "'. The methods available for this object are:" + System.lineSeparator() + String.join(System.lineSeparator(), getAvalableMethods(component)));
        return null;
    }

    public static Object invokeMethod(TestCase testCase, Object component, String methodName, Object... args){
        MethodInvoker m = new MethodInvoker(testCase);
        return m.invokeMethod(component, methodName, args);
    }

    public Object invokeMethod(Object component, String methodName, Object... args){
        if(args == null) return invokeMethod(component, methodName);
        if(component == null || methodName == null || methodName == ""){
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
                    log(LogLevel.EXECUTED, "Invoked method '" + methodName + "' on component of class '" + component.getClass().toString() + "'" + returnString);
                    return returnObject;
                } catch (IllegalAccessException e) {
                    log(LogLevel.EXECUTION_PROBLEM, "Could not invoke method '" + methodName + "' on component of class '" + component.getClass().toString() + "'. Error: " + e.getMessage());
                    return null;
                } catch (InvocationTargetException e) {
                    log(LogLevel.EXECUTION_PROBLEM, "Could not invoke method '" + methodName + "' on component of class '" + component.getClass().toString() + "'. Error: " + e.getMessage());
                    return null;
                }
            }
        }
        log(LogLevel.EXECUTION_PROBLEM, "Tried invoking method '" + methodName + "' on component, but that method could not be found for this component." + System.lineSeparator() +
                "Class is '" + component.getClass().toString() + "' and available methods are:" + System.lineSeparator() + String.join(System.lineSeparator(), getAvalableMethods(component)) +
                System.lineSeparator() + "Remember to cast any return object upon usage.");
        return null;
    }

    public Object invokeMethod(Object component, String methodName){
        if(component == null || methodName == null || methodName == ""){
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
                    log(LogLevel.EXECUTED, "Invoked method '" + methodName + "' on component of class '" + component.getClass().toString() + "'" + returnString);
                    return returnObject;
                } catch (IllegalAccessException e) {
                    log(LogLevel.EXECUTION_PROBLEM, "Could not invoke method '" + methodName + "' on component of class '" + component.getClass().toString() + "'. Error: " + e.getMessage());
                    return null;
                } catch (InvocationTargetException e) {
                    log(LogLevel.EXECUTION_PROBLEM, "Could not invoke method '" + methodName + "' on component of class '" + component.getClass().toString() + "'. Error: " + e.getMessage());
                    return null;
                }
            }
        }
        log(LogLevel.EXECUTION_PROBLEM, "Tried invoking method '" + methodName + "' on component, but that method could not be found for this component." + System.lineSeparator() +
                "Class is '" + component.getClass().toString() + "' and available methods are:" + System.lineSeparator() + String.join(System.lineSeparator(), getAvalableMethods(component)) +
                System.lineSeparator() + "Remember to cast any return object upon usage.");
        return null;
    }


    public Object tryInvokeMethod(Object component, String methodName){
        if(component == null || methodName == null || methodName == ""){
            log(LogLevel.DEBUG, "Cannot invoke method '" + methodName + "' on object since either of them are null and both are needed.");
            return null;
        }
        Class<?> c = component.getClass();
        for(Method method : c.getMethods()){
            if(method.toString().endsWith("." + methodName)) {
                try {
                    String logString = "Invoking method '" + method.toString() + "' on object of class '" + component.getClass().toString() + "'";
                    Object returnObject = method.invoke(component);
                    if (returnObject != null)
                        logString += " and received '" + returnObject.toString() + "' in response";
                    log(LogLevel.DEBUG, logString + ".");
                    return returnObject;
                } catch (IllegalAccessException e) {
                    log(LogLevel.DEBUG, "Encountered problems when invoking method '" + method.toString() + "' on element of class '" + component.getClass().toString() + "'. Error: " + e.toString());
                } catch (InvocationTargetException e) {
                    log(LogLevel.DEBUG, "Encountered problems when invoking method '" + method.toString() + "' on element of class '" + component.getClass().toString() + "'. Error: " + e.toString());
                }
            }
        }
        return null;
    }

    public ArrayList<String> getAvalableMethods(Object component){
        Class<?> c = component.getClass();
        ArrayList<String> methods = new ArrayList<>();
        for(Method m : c.getMethods()){
            methods. add(m.toString());
        }
        return methods;
    }

    public boolean objectHasMethod(Object component, String methodName){
        Class<?> c = component.getClass();
        for(Method m : c.getMethods()){
            if(m.toString().contains(methodName)){
                return true;
            }
        }
        return false;
    }

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
            String logString = "Invoking method '" + method.toString() + "' on object of class '" + object.getClass().toString() + "'";
            Object returnObject = method.invoke(object);
            if(returnObject != null) logString += " and received '" + returnObject.toString() + "' in response";
            log(LogLevel.DEBUG, logString + ".");
            return returnObject;
        } catch (IllegalAccessException e) {
            log(LogLevel.DEBUG, "Encountered problems when invoking method '" + method.toString() + "' on element of class '" + object.getClass().toString() + "'. Error: " + e.toString());
        } catch (InvocationTargetException e) {
            log(LogLevel.DEBUG, "Encountered problems when invoking method '" + method.toString() + "' on element of class '" + object.getClass().toString() + "'. Error: " + e.toString());
        }
        return null;
    }

    private void log(LogLevel logLevel, String message){
        if(testCase == null)return;
        testCase.log(logLevel, message);
    }

}
