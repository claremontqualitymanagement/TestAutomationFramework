package se.claremont.autotest.common.backendserverinteraction;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import se.claremont.autotest.common.logging.LogLevel;
import se.claremont.autotest.common.logging.LogPost;
import se.claremont.autotest.common.testcase.TestCase;
import se.claremont.autotest.common.testrun.TestRunResult;
import se.claremont.autotest.common.testset.UnitTestClass;

import java.io.IOException;

/**
 * Created by jordam on 2017-03-19.
 */
public class JsonSerializationAndDeserializationTest extends UnitTestClass {

    @Test
    public void testCaseSerializationAndDeserialization(){
        TestCase testCase = new TestCase();
        testCase.log(LogLevel.INFO, "Message");
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
        Assert.assertNotNull(testCaseObject);
        testCaseObject.log(LogLevel.INFO, "Message2");
    }

    @Test
    public void logPostSerializationAndDeserialization(){
        LogPost logPost = new LogPost(LogLevel.INFO, "Message");
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
        TestRunResult testRunResult = new TestRunResult();
        TestCase testCase = new TestCase();
        testCase.log(LogLevel.INFO, "Message");
        testRunResult.addTestCaseResult(testCase);
        ObjectMapper mapper = new ObjectMapper();
        String json = null;
        try {
            json = mapper.writeValueAsString(testRunResult);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(json);
        TestRunResult testRunResultObject = null;
        try {
            testRunResultObject = mapper.readValue(json, TestRunResult.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(testRunResultObject);
        testRunResult.addTestCaseResult(new TestCase());
    }
}
