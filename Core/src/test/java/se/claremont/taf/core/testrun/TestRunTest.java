package se.claremont.taf.core.testrun;

import org.junit.Test;
import se.claremont.taf.core.testset.UnitTestClass;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for TestRun class
 *
 * Created by magnusolsson on 2016-11-01.
 */
public class TestRunTest extends UnitTestClass{

    @Test
    public void checkExitCodeTableForSuccessfullyExecution(){
        TestRun.setExitCode(TestRun.ExitCodeTable.INIT_OK.getValue());
        assertEquals(TestRun.ExitCodeTable.INIT_OK.getValue(), TestRun.getExitCode());
    }

    @Test
    public void unsetCustomSettingsValueForHtmlReportsLinkPrefixShouldReturnFileLink(){
        Settings original = TestRun.getSettings();
        TestRun.reloadSettings();
        assertTrue("Expected 'file' but was '" + TestRun.reportLinkPrefix() + "'.", TestRun.reportLinkPrefix().equals("file"));
        TestRun.setSettings(original);
    }

    @Test
    public void customSettingsValueForHtmlReportsLinkPrefixSetToHttpShouldReturnHttpLink(){
        Settings original = TestRun.getSettings();
        Settings s = new Settings();
        s.setValue(Settings.SettingParameters.HTML_REPORTS_LINK_PREFIX, "http");
        TestRun.setSettings(s);
        assertTrue("Expected 'http' but was '" + TestRun.reportLinkPrefix() + "'.", TestRun.reportLinkPrefix().equals("http"));
        TestRun.setSettings(original);
    }

    @Test
    public void customSettingsValueForHtmlReportsLinkPrefixSetToHttpsShouldReturnHttpsLink(){
        Settings original = TestRun.getSettings();
        Settings s = new Settings();
        s.setValue(Settings.SettingParameters.HTML_REPORTS_LINK_PREFIX, "https");
        TestRun.setSettings(s);
        assertTrue("Expected 'https' but was '" + TestRun.reportLinkPrefix() + "'.", TestRun.reportLinkPrefix().equals("https"));
        TestRun.setSettings(original);
    }

    @Test
    public void customSettingsValueForHtmlReportsLinkPrefixSetToFileShouldReturnFileLink(){
        Settings original = TestRun.getSettings();
        Settings s = new Settings();
        s.setValue(Settings.SettingParameters.HTML_REPORTS_LINK_PREFIX, "file");
        TestRun.setSettings(s);
        assertTrue("Expected 'file' but was '" + TestRun.reportLinkPrefix() + "'.", TestRun.reportLinkPrefix().equals("file"));
        TestRun.setSettings(original);
    }

    @Test
    public void customSettingsValueForHtmlReportsLinkPrefixSetToNonManagedValueShouldReturnFileLink(){
        Settings original = TestRun.getSettings();
        Settings s = new Settings();
        s.setValue(Settings.SettingParameters.HTML_REPORTS_LINK_PREFIX, "customLinkType");
        TestRun.setSettings(s);

        assertTrue("Expected 'customLinkType' but was '" + TestRun.reportLinkPrefix() + "'.", TestRun.reportLinkPrefix().equals("customLinkType"));
        TestRun.setSettings(original);
    }

}
