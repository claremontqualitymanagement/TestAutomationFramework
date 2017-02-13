package se.claremont.autotest.common.testrun;

import org.junit.Test;
import se.claremont.autotest.common.testrun.Settings;
import se.claremont.autotest.common.testrun.TestRun;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for TestRun class
 *
 * Created by magnusolsson on 2016-11-01.
 */
public class TestRunTest {

    @Test
    public void checkExitCodeTableForSuccessfullyExecution(){
        TestRun.exitCode = TestRun.ExitCodeTable.INIT_OK.getValue();
        assertEquals(TestRun.ExitCodeTable.INIT_OK.getValue(), TestRun.exitCode);
    }

    @Test
    public void unsetCustomSettingsValueForHtmlReportsLinkPrefixShouldReturnFileLink(){
        Settings original = TestRun.settings;
        TestRun.settings = new Settings();
        assertTrue("Expected 'file' but was '" + TestRun.reportLinkPrefix() + "'.", TestRun.reportLinkPrefix().equals("file"));
        TestRun.settings = original;
    }

    @Test
    public void customSettingsValueForHtmlReportsLinkPrefixSetToHttpShouldReturnHttpLink(){
        Settings original = TestRun.settings;
        Settings s = new Settings();
        s.setValue(Settings.SettingParameters.HTML_REPORTS_LINK_PREFIX, "http");
        TestRun.settings = s;
        assertTrue("Expected 'http' but was '" + TestRun.reportLinkPrefix() + "'.", TestRun.reportLinkPrefix().equals("http"));
        TestRun.settings = original;
    }

    @Test
    public void customSettingsValueForHtmlReportsLinkPrefixSetToHttpsShouldReturnHttpsLink(){
        Settings original = TestRun.settings;
        Settings s = new Settings();
        s.setValue(Settings.SettingParameters.HTML_REPORTS_LINK_PREFIX, "https");
        TestRun.settings = s;
        assertTrue("Expected 'https' but was '" + TestRun.reportLinkPrefix() + "'.", TestRun.reportLinkPrefix().equals("https"));
        TestRun.settings = original;
    }

    @Test
    public void customSettingsValueForHtmlReportsLinkPrefixSetToFileShouldReturnFileLink(){
        Settings original = TestRun.settings;
        Settings s = new Settings();
        s.setValue(Settings.SettingParameters.HTML_REPORTS_LINK_PREFIX, "file");
        TestRun.settings = s;
        assertTrue("Expected 'file' but was '" + TestRun.reportLinkPrefix() + "'.", TestRun.reportLinkPrefix().equals("file"));
        TestRun.settings = original;
    }

    @Test
    public void customSettingsValueForHtmlReportsLinkPrefixSetToNonManagedValueShouldReturnFileLink(){
        Settings original = TestRun.settings;
        Settings s = new Settings();
        s.setValue(Settings.SettingParameters.HTML_REPORTS_LINK_PREFIX, "customLinkType");
        TestRun.settings = s;

        assertTrue("Expected 'customLinkType' but was '" + TestRun.reportLinkPrefix() + "'.", TestRun.reportLinkPrefix().equals("customLinkType"));
        TestRun.settings = original;
    }


}
