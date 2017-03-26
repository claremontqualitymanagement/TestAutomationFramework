package se.claremont.autotest.common.reporting.testrunreports;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import se.claremont.autotest.common.backendserverinteraction.TestlinkAdapterServerConnection;
import se.claremont.autotest.common.testcase.TestCase;
import se.claremont.autotest.common.testrun.Settings;
import se.claremont.autotest.common.testrun.TestRun;
import se.claremont.autotest.common.testrun.TestRunReporter;
import se.claremont.autotest.common.testset.TestSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jordam on 2017-03-19.
 */
public class TestlinkAdapterTestRunReporter implements TestRunReporter {
    @JsonProperty public List<String> testCasesJsonsList = new ArrayList<>();

    public TestlinkAdapterTestRunReporter(){
    }

    public void evaluateTestCase(TestCase testCase){
        if(testCase == null)return;
        if(TestRun.getSettingsValue(Settings.SettingParameters.URL_TO_TESTLINK_ADAPTER).equals(TestlinkAdapterServerConnection.defaultServerUrl)) return;
        testCasesJsonsList.add(testCase.toJson());
    }

    public void report(){
        if(TestRun.getSettingsValue(Settings.SettingParameters.URL_TO_TESTLINK_ADAPTER).equals(TestlinkAdapterServerConnection.defaultServerUrl)) return;
        TestlinkAdapterServerConnection testlinkAdapterServerConnection = new TestlinkAdapterServerConnection();
        testlinkAdapterServerConnection.postTestRunResult(toJson());
    }

    public void evaluateTestSet(TestSet testSet){
        //No need to evaluate test set for Testlink reporting.
    }

    public String toJson(){
        ObjectMapper mapper = new ObjectMapper();
        String json = null;
        try {
            json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            System.out.println(e.toString());
        }
        return json;
    }
}