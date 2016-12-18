package se.claremont.tests;

import org.junit.*;
import org.junit.rules.TestName;
import se.claremont.autotest.common.Settings;
import se.claremont.autotest.common.TestRun;
import se.claremont.autotest.common.TestRunReporterHtmlSummaryReportFile;
import se.claremont.autotest.common.TestSet;


/**
 * Created by jordam on 2016-12-04.
 */
public class HomePageTest extends TestSet {
    @Rule public TestName currentTestName = new TestName();
    private TestActions app;

    @BeforeClass
    public static void classSetup(){
        TestRun.settings.setValue(Settings.SettingParameters.EMAIL_REPORT_RECIPIENTS_COMMA_SEPARATED_LIST_OF_ADDRESSES, "no.no@no.no");
        TestRun.settings.setValue(Settings.SettingParameters.EMAIL_SERVER_ADDRESS, "smtp.google.com");
        TestRun.settings.setValue(Settings.SettingParameters.EMAIL_SENDER_ADDRESS, "mailrelay@gmail.com");
        TestRun.settings.setValue(Settings.SettingParameters.EMAIL_SERVER_PORT, "25");
        TestRun.settings.setValue(Settings.SettingParameters.EMAIL_SMTP_OR_GMAIL, "SMTP");

        TestRun.settings.setValue(Settings.SettingParameters.PATH_TO_LOGO, "http://46.101.193.212/TAF/images/claremontlogo.gif");

        //Base log folder is a TAF folder under user home folder
        //TestRun.settings.setValue(Settings.SettingParameters.BASE_LOG_FOLDER, "C:\\TEMP\\");

        TestRun.reporters.addTestRunReporter(new TestRunReporterHtmlSummaryReportFile());
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
        wrapUpTestCase();
    }

    @AfterClass
    public static void ClassTearDown(){
        TestRun.reporters.evaluateTestSet(TestRun.currentTestSet);
        TestRun.reporters.report();
    }

    @Test
    public void landingPageTest(){
        app.nav.ensureLandingPageDisplayed(); //Making sure we're on the landing page

        //Line below is writing element descriptors for landing page to file, to be copy->pasted into page class.
        //app.web.mapCurrentPage("C:\\Temp\\landingpage.txt");

        app.checkLandingPageLinks();
        app.checkLandingPageLayout();
        app.checkLandingPageTexts();
        //app.checkLandingPageWithW3CValidator();
    }


}
