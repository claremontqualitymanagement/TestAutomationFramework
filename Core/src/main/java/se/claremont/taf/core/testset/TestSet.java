package se.claremont.taf.core.testset;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import se.claremont.taf.core.junitcustomization.TafParallelTestCaseRunner;
import se.claremont.taf.core.logging.KnownError;
import se.claremont.taf.core.logging.KnownErrorsList;
import se.claremont.taf.core.reporting.testrunreports.TestRunReporterHtmlSummaryReportFile;
import se.claremont.taf.core.support.SupportMethods;
import se.claremont.taf.core.testcase.TestCase;
import se.claremont.taf.core.testrun.TestRun;

import java.util.ArrayList;
import java.util.List;

/**
 * A test set is a set of test cases
 * <p>
 * Created by jordam on 2016-08-17.
 */
@JsonIgnoreProperties({"currentTestCase", "currentTestCases"})
public abstract class TestSet {

    private final List<TestCase> currentTestCases = new ArrayList<>();

    @JsonProperty
    public String name;

    @JsonProperty
    public final KnownErrorsList knownErrorsList = new KnownErrorsList();

    @Rule
    public final TestRule traceTestWatcher = new TestWatcher() {
        @Override
        protected void starting(Description d) {
            makeSureTestSetNameIsInTestRunListOfTestSets();
            startUpTestCase(d.getMethodName(), d.getClassName());
        }
    };

    /**
     * Setting up a new test set instance, almost always representing a JUnit test class.
     */
    public TestSet() {
        name = SupportMethods.classNameAtStacktraceLevel(3);
        TestRun.addTestRunReporterIfNotAlreadyRegistered(new TestRunReporterHtmlSummaryReportFile());
    }

    //Todo: This could not possibly be 100% thread safe...?
    /**
     * Used to grep the current TAF TestCase at runtime.
     *
     * @return Returns the TAF TestCase instance for the current test case being executed.
     */
    public TestCase currentTestCase() {
        for (TestCase testCase : currentTestCases) {
            if (testCase.testCaseMethodName.equals(new TestName().getMethodName())) return testCase;
        }
        if (currentTestCases.size() == 1) return currentTestCases.get(0);
        return null;
    }

    private void makeSureTestSetNameIsInTestRunListOfTestSets() {
        boolean testSetRegisteredInRunner = TafParallelTestCaseRunner.testSetNames.contains(name);
        for (String testSetName : TafParallelTestCaseRunner.testSetNames) {
            if (testSetName.equals(this.name)) testSetRegisteredInRunner = true;
        }
        if (!testSetRegisteredInRunner) {
            TafParallelTestCaseRunner.testSetNames.add(name);
            TafParallelTestCaseRunner.testSets.add(this);
        }
    }

    /**
     * TAF internal test tear down method.
     */
    @After
    public void testSetClassInternalTearDown() {
        wrapUpTestCase();
    }

    /**
     * Some reporting mechanisms (for instance for Testlink test management system) uses the test
     * set name and the test name to identify what test case to report the results to. Normally
     * test name is derived from test method name. Since Java test method names does not allow
     * all characters, but most test management system does, the test name can be altered.
     *
     * @param name The new test name to use for reporters.
     */
    public void setCurrentTestCaseTestName(String name) {
        if (currentTestCase() == null) return;
        currentTestCase().setName(name);
    }

    /**
     * Known errors can be entered at a test set level, making them valid for all test cases in the test set.
     * All patterns entered must be found in a test case for the known error to match
     *
     * @param description                          The text string describing the error
     * @param regexPatternsForLogRowsThatMustOccur TestCaseLog patterns to find in the test case execution testCaseLog
     */
    @SuppressWarnings("unused")
    public void addKnownError(String description, String[] regexPatternsForLogRowsThatMustOccur) {
        knownErrorsList.add(new KnownError(description, regexPatternsForLogRowsThatMustOccur));
    }

    /**
     * Known errors can be entered at a test set level, making them valid for all test cases in the test set.
     * All patterns entered must be found in a test case for the known error to match
     *
     * @param description           The text string describing the error
     * @param regexPatternForLogRow TestCaseLog pattern to find in the test case execution testCaseLog
     */
    public void addKnownError(String description, String regexPatternForLogRow) {
        knownErrorsList.add(new KnownError(description, regexPatternForLogRow));
    }

    /**
     * Procedures common for all test cases
     *
     * @param testName    The name of the test, for reporting purposes.
     * @param testSetName The name of the class extending TestSet, to enable logging
     *                    in multi-threaded parallel execution.
     */
    public void startUpTestCase(String testName, String testSetName) {
        currentTestCases.add(new TestCase(knownErrorsList, testName, testSetName));
    }

    /**
     * Returns a JSON representation of this TestSet object.
     *
     * @return A plain text JSON representation of this TestSet object.
     */
    public String toJson() {
        ObjectMapper mapper = new ObjectMapper();
        String json = null;
        try {
            json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            System.out.println(e.toString());
        }
        return json;
    }

    /**
     * Closes down test case execution.
     * Test case tear down procedure at the test set level
     */
    void wrapUpTestCase() {
        if (currentTestCase() == null) return;
        currentTestCase().report();
        currentTestCases.remove(currentTestCase());
    }


}
