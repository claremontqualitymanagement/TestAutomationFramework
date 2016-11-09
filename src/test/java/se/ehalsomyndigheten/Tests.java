package se.ehalsomyndigheten;

import org.junit.*;
import org.junit.experimental.categories.Category;
import org.junit.rules.TestName;
import se.claremont.autotest.common.*;

/**
 * Created by jordam on 2016-11-07.
 */
public class Tests extends TestSet{
    public interface CookieTests {}

    @Rule
    public TestName currentTestName = new TestName();
    private TestActions app;

    @BeforeClass
    public static void classSetup(){
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
        app.web.driver.quit();
        wrapUpTestCase();
    }

    @AfterClass
    public static void ClassTearDown(){
        TestRun.reporters.evaluateTestSet(TestRun.currentTestSet);
        TestRun.reporters.report();
    }

    @Category(CookieTests.class)
    @Test
    public void navigateHomePage(){
        app.checkHomePageMenu();
        app.acceptCookies();
    }

    @Test
    public void navigateSatsningar(){
        app.nav.ensureApplicationState_Satsningar();
    }


    /**
     * This test ensure that direkt short-links to non-landing-page
     * pages also creates the cookie acceptance question.
     */
    @Category(CookieTests.class)
    @Test
    public void directNavigationToSatsningarShouldHaveCookieAcceptance(){
        app.web.navigate("https://www.ehalsomyndigheten.se/satsningar/");
        //app.web.mapCurrentPage("C:\\temp\\ehalsomyndigheten.txt");
        app.web.verifyObjectIsDisplayed(SatsningarPage.CookieBarAccept_Div());
        app.web.click(SatsningarPage.CookieBarAccept_Div());
        app.web.verifyObjectIsNotDisplayed(SatsningarPage.CookieBarAccept_Div());
    }


}
