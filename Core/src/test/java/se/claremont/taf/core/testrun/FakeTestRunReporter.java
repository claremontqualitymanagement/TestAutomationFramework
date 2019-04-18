package se.claremont.taf.core.testrun;

import se.claremont.taf.core.testcase.TestCase;
import se.claremont.taf.core.testrun.reportingengine.TestRunReporter;
import se.claremont.taf.core.testset.TestSet;

import java.util.ArrayList;
import java.util.List;

public class FakeTestRunReporter implements TestRunReporter {
    int numberOfReportsPerformed = 0;
    int numberOfTestCaseEvaluationsPerformed = 0;
    private int numberOfTestSetEvaluationsPerformed = 0;
    private final List<String> testSetNames = new ArrayList<>();
    final List<String> testCaseNames = new ArrayList<>();

    public FakeTestRunReporter(){
    }

    @Override
    public void report() {
        numberOfReportsPerformed++;
    }

    @Override
    public void evaluateTestCase(TestCase testCase) {
        numberOfTestCaseEvaluationsPerformed++;
        System.out.println("Evaluating test case '" + testCase.testName + "'.");
        testCaseNames.add(testCase.testSetName + "." + testCase.testName);
    }

    @Override
    public void evaluateTestSet(TestSet testSet) {
        numberOfTestSetEvaluationsPerformed++;
        testSetNames.add(testSet.name);
    }

    @Override
    public String toString(){
        return "[FakeTestRunReporter:" + System.lineSeparator() +
                "   numberOfReportsPerformed=" + numberOfReportsPerformed + System.lineSeparator() +
                "   numberOfTestCaseEvaluationsPerformed=" + numberOfTestCaseEvaluationsPerformed + System.lineSeparator() +
                "   numberOfTestSetEvaluationsPerformed=" + numberOfTestSetEvaluationsPerformed + System.lineSeparator() +
                "   TestSets registered for evaluation:" + System.lineSeparator() +
                "      [" + String.join("]" + System.lineSeparator() + "      [", testSetNames) + "]" + System.lineSeparator() +
                "   TestCases registered for evaluation:" + System.lineSeparator() +
                "      [" + String.join("]" + System.lineSeparator() + "      [", testCaseNames) + "]" + System.lineSeparator() +
                "]";

    }
}
