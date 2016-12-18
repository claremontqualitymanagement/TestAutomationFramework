package se.claremont.web;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import se.claremont.autotest.common.Settings;
import se.claremont.autotest.common.TestRun;
import se.claremont.autotest.common.TestRunReporterHtmlSummaryReportFile;
import se.claremont.autotest.common.TestSet;
import se.claremont.web.support.TestActions;


@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = Application.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebTest extends TestSet {
    @LocalServerPort
    private int port;
    private TestActions app;

    @Rule
    public TestName currentTestName = new TestName();

    @BeforeClass
    public static void classSetup(){
        TestRun.settings.setValue(Settings.SettingParameters.PATH_TO_LOGO, "http://46.101.193.212/TAF/images/claremontlogo.gif");

        String path = WebTest.class.getClassLoader().getResource(".").getFile();
        TestRun.settings.setValue(Settings.SettingParameters.BASE_LOG_FOLDER, path);

        TestRun.reporters.addTestRunReporter(new TestRunReporterHtmlSummaryReportFile());
    }

    @Before
    public void setup() {
        startUpTestCase(currentTestName.getMethodName());
        app = new TestActions(currentTestCase);
        name = this.getClass().getSimpleName();
    }

    @Test
    public void test_get_hellopage() {
        app.getWeb().navigate("http://localhost:" + port + "/hello");
        app.getWeb().verifyPageTitle("Hello world");
        //app.getWeb().verifyCurrentPageSourceWithW3validator(true);
    }

    @Test
    public void test_get_wrong_page() {
        app.getWeb().navigate("http://localhost:" + port + "/wrong");
        app.getWeb().verifyTextExistOnCurrentPage("There was an unexpected error");
        app.getWeb().verifyTextExistOnCurrentPage("404");
    }

    @After
    public void testTearDown(){
        app.getWeb().makeSureDriverIsClosed();
        wrapUpTestCase();
    }

    @AfterClass
    public static void ClassTearDown(){
        TestRun.reporters.evaluateTestSet(TestRun.currentTestSet);
        TestRun.reporters.report();
    }

}
