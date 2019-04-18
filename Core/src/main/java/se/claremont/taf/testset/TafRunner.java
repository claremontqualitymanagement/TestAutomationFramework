package se.claremont.taf.testset;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import se.claremont.taf.logging.LogLevel;
import se.claremont.taf.support.StringManagement;
import se.claremont.taf.testcase.TestCase;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TafRunner extends Runner {

    private Class testClass;
    private Object testClassObject;
    private Description description;
    private List<Description> subDescriptions;

    public TafRunner(Class testClass) {
        super();
        this.testClass = testClass;
        description = Description.createSuiteDescription(testClass);
        subDescriptions = new ArrayList<>();
    }

    @Override
    public Description getDescription() {
        description = Description.createSuiteDescription(testClass);
        for(Description d : subDescriptions){
            description.addChild(d);
        }
        return description;
    }

    private TestCase getCurrentTestCase(){
        Method getCurrentTestCaseMethod = null;
        TestCase testCase = null;
        for (Method m : testClass.getMethods()){
            if(!m.getName().equals("currentTestCase")) continue;
            getCurrentTestCaseMethod = m;
        }
        if(getCurrentTestCaseMethod == null){
            return null;
        } else {
            try {
                testCase = (TestCase)getCurrentTestCaseMethod.invoke(testClassObject, (Object[])null);
            } catch (IllegalAccessException e1) {
                System.out.println(e1.toString());
            } catch (InvocationTargetException e1) {
                System.out.println(e1.toString());
            }
            return testCase;
        }
    }

    private boolean successfullyLoggedToTestCase(TestCase testCase, String e){
        if(testCase == null) return false;
        testCase.logDifferentlyToTextLogAndHtmlLog(LogLevel.EXECUTION_PROBLEM,
                "Encountered problem: " + e,
                "Encountered problem:<br><pre>" + StringManagement.htmlContentToDisplayableHtmlCode(e.replace(System.lineSeparator(), "<br>")) + "</pre>");
        return true;
    }

    @Override
    public void run(RunNotifier runNotifier) {
        runNotifier.fireTestRunStarted(getDescription());
        Description d = null;
        try {
            testClassObject = testClass.newInstance();
            for (Method method : testClass.getMethods()) {
                if (!method.isAnnotationPresent(Test.class)) continue;
                d = Description.createTestDescription(testClass, method.getName());
                if (method.isAnnotationPresent(Ignore.class)) {
                    runNotifier.fireTestAssumptionFailed(new Failure(d, new Exception(d.getAnnotations().toString())));
                    subDescriptions.add(d);
                    return;
                }
                runNotifier.fireTestStarted(d);
                method.invoke(testClassObject);
                runNotifier.fireTestFinished(d);
                subDescriptions.add(d);
            }
        } catch (Exception e) {
            subDescriptions.add(d);
            TestCase testCase = getCurrentTestCase();
            if(testCase == null) {
                System.out.println(e.toString());
                return;
            }
            successfullyLoggedToTestCase(testCase, e.toString());
            testCase.report();
        }
    }
}
