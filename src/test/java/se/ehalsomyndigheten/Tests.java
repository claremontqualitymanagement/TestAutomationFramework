package se.ehalsomyndigheten;

import org.junit.*;
import org.junit.rules.TestName;
import se.claremont.autotest.common.CliTestRunner;
import se.claremont.autotest.common.Settings;
import se.claremont.autotest.common.TestRun;
import se.claremont.autotest.common.TestSet;

import static se.claremont.autotest.common.TestRun.*;

/**
 * Created by jordam on 2016-11-07.
 */
public class Tests extends TestSet{
    @Rule
    public TestName currentTestName = new TestName();
    private TestActions app;

    @BeforeClass
    public static void classSetup(){
        TestRun.settings.setValue(Settings.SettingParameters.EMAIL_REPORT_RECIPIENTS_COMMA_SEPARATED_LIST_OF_ADDRESSES, "jorgen.damberg@claremont.se");
        TestRun.settings.setValue(Settings.SettingParameters.EMAIL_SERVER_ADDRESS, "mailrelay.prv.se");
        TestRun.settings.setValue(Settings.SettingParameters.EMAIL_SENDER_ADDRESS, "jorgen.damberg@gmail.com");
        TestRun.settings.setValue(Settings.SettingParameters.EMAIL_SERVER_PORT, "25");
        TestRun.settings.setValue(Settings.SettingParameters.EMAIL_SMTP_OR_GMAIL, "SMTP");
    }

    @Before
    public void testSetup(){
        startUpTestCase(currentTestName.getMethodName());
        app = new TestActions(currentTestCase);
        name = this.getClass().getSimpleName();
    }

    @After
    public void testTearDown(){
        app.web.makeSureDriverIsClosed();
        app.web.driver.quit();
        wrapUpTestCase();
    }

    @AfterClass
    public static void ClassTearDown(){
        TestRun.reporters.evaluateTestSet(TestRun.currentTestSet);
        TestRun.reporters.report();
    }

    @Test
    public void navigateHomePage(){
        app.checkHomePageMenu();
        //app.web.mapCurrentPage("C:\\temp\\ehalsomyndigheten.txt");
        app.acceptCookies();
        app.web.verifyObjectIsNotDisplayed(LandingPage.CookieBarAccept_Div());
    }

}
