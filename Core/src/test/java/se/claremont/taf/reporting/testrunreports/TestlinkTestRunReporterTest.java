package se.claremont.taf.reporting.testrunreports;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import se.claremont.taf.logging.LogLevel;
import se.claremont.taf.testrun.Settings;
import se.claremont.taf.testrun.TestRun;
import se.claremont.taf.testset.TestSet;

/**
 * Sandbox tests for Testlink interaction
 *
 * Created by jordam on 2017-03-26.
 */
public class TestlinkTestRunReporterTest extends TestSet{

    @BeforeClass
    public static void setup(){
        TestRun.setSettingsValue(Settings.SettingParameters.URL_TO_TESTLINK_ADAPTER, "http://127.0.0.1:2221/taftestlinkadapter");
    }

    @Test
    @Ignore
    public void reportTestCaseTest(){
        currentTestCase().log(LogLevel.INFO, "Hey! It works! ");
    }

    @Test
    @Ignore
    public void reportTestCaseTest2(){
        currentTestCase().log(LogLevel.INFO, "Hey! It works! ");
    }
}
