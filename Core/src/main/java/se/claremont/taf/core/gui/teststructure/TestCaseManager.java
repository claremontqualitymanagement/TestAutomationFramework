package se.claremont.taf.core.gui.teststructure;

import se.claremont.taf.core.gui.teststructure.codegeneration.TestSetTemplate;
import se.claremont.taf.core.testcase.TestCase;

public class TestCaseManager {
    private static TestCase testCase;
    private static boolean partOfSubProcedureTestStep = false;
    public static TestSetTemplate testSetCode = new TestSetTemplate("TempTestClassName");

    public static void setToBePartOfSubProcedureTestStep(){
        partOfSubProcedureTestStep = true;
    }

    public static void startTestStep(){
        if(!partOfSubProcedureTestStep) {
            testCase = new TestCase("Trial run of test");
        }
    }

    public static void wrapUpTestCase(){
        if(!partOfSubProcedureTestStep) {
            testCase.report();
            testCase = null;
        }
    }

    public static TestCase getTestCase() {
        return testCase;
    }

    public static void setNotToBePartOfSubProcedureTestStep() {
        partOfSubProcedureTestStep = false;
    }
}
