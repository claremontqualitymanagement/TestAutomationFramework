package se.claremont.taf.core.backendserverinteraction;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import se.claremont.taf.core.junitcustomization.TafParallelTestCaseRunner;
import se.claremont.taf.core.logging.LogLevel;
import se.claremont.taf.core.logging.LogPost;
import se.claremont.taf.core.reporting.testrunreports.TafBackendServerTestRunReporter;
import se.claremont.taf.core.testcase.TestCase;
import se.claremont.taf.core.testset.TestSet;
import se.claremont.taf.core.testset.UnitTestClass;

import java.io.IOException;
import java.util.HashSet;

/**
 * Tests for JSON serialization and deserializations - to ensure correct information is included in JSON.
 *
 * Created by jordam on 2017-03-19.
 */
public class JsonSerializationAndDeserializationTest extends UnitTestClass{

    @Test
    public void testCaseSerializationAndDeserialization(){
        TestCase testCase = new TestCase();
        testCase.addTestCaseData("DataParameter", "DataValue");
        testCase.addKnownError("Description of known error", ".*Regexpattern.*");
        testCase.addKnownError("Description of known error 2", new String[]{".*Regexpattern.*", "Nope"});
        testCase.logDifferentlyToTextLogAndHtmlLog(LogLevel.INFO, "Message [in ]{pure] :text\\\"this\" time.", "html message");
        testCase.log(LogLevel.INFO, "Message");
        testCase.testCaseResult.evaluateResultStatus();
        ObjectMapper mapper = new ObjectMapper();
        String json = null;
        try {
            json = mapper.writeValueAsString(testCase);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(json);
        TestCase testCaseObject = null;
        try {
            testCaseObject = mapper.readValue(json, TestCase.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(json, testCaseObject);
        testCaseObject.log(LogLevel.INFO, "Message2");
    }

    @Test
    public void logPostSerializationAndDeserialization(){
        LogPost logPost = new LogPost(LogLevel.INFO, "Message", "", null, null, null);
        ObjectMapper mapper = new ObjectMapper();
        String json = null;
        try {
            json = mapper.writeValueAsString(logPost);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(json);
        LogPost logPostObject = null;
        try {
            logPostObject = mapper.readValue(json, LogPost.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(logPostObject);
        Assert.assertFalse(logPostObject.isFail());
    }

    @Test
    public void testRunResultSerializationAndDeserialization(){
        TafBackendServerTestRunReporter tafBackendServerTestRunReporter = new TafBackendServerTestRunReporter();
        TestCase testCase = new TestCase();
        testCase.log(LogLevel.INFO, "Message");
        tafBackendServerTestRunReporter.evaluateTestCase(testCase);
        ObjectMapper mapper = new ObjectMapper();
        String json = null;
        try {
            json = mapper.writeValueAsString(tafBackendServerTestRunReporter);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(json);
        TafBackendServerTestRunReporter tafBackendServerTestRunReporterObject = null;
        try {
            tafBackendServerTestRunReporterObject = mapper.readValue(json, TafBackendServerTestRunReporter.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(tafBackendServerTestRunReporterObject);
        tafBackendServerTestRunReporter.evaluateTestCase(new TestCase());
    }

    @Test
    public void testSetSerializationAndDeserialization(){
        FakeTestSet fakeTestSet = new FakeTestSet();
        fakeTestSet.startUpTestCase("mytest", this.getClass().getName());
        fakeTestSet.currentTestCase().log(LogLevel.INFO, "Message");
        fakeTestSet.addKnownError("KnownErrorDescription1", ".*Known error pattern1");
        fakeTestSet.addKnownError("KnownErrorDescription2", new String[]{".*Known error pattern2" } );
        fakeTestSet.name = "MyTestSetName";
        TafBackendServerTestRunReporter tafBackendServerTestRunReporter = new TafBackendServerTestRunReporter();
        tafBackendServerTestRunReporter.evaluateTestCase(fakeTestSet.currentTestCase());
        tafBackendServerTestRunReporter.evaluateTestSet(fakeTestSet);
        ObjectMapper mapper = new ObjectMapper();
        String json = null;
        try {
            json = mapper.writeValueAsString(tafBackendServerTestRunReporter);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(json);
        System.out.println(json);
        Assert.assertTrue(json.contains("Message"));
        Assert.assertTrue(json.contains("KnownErrorDescription1"));
        Assert.assertTrue(json.contains("KnownErrorDescription2"));
        Assert.assertTrue(json.contains(".*Known error pattern1"));
        Assert.assertTrue(json.contains(".*Known error pattern2"));
        Assert.assertTrue(json.contains("MyTestSetName"));
        TafBackendServerTestRunReporter tafBackendServerTestRunReporterObject = null;
        try {
            tafBackendServerTestRunReporterObject = mapper.readValue(json, TafBackendServerTestRunReporter.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(tafBackendServerTestRunReporterObject);
    }

    @SuppressWarnings("EmptyMethod")
    public static class FakeTestSet extends TestSet{

        public FakeTestSet(){
            super();
            TafParallelTestCaseRunner.testSets = new HashSet<>();
        }

        @Test
        public void dummyTest(){}

    }
}
