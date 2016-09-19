package se.se.prv.homepage;

import org.junit.*;
import org.junit.rules.TestName;
import se.claremont.autotest.common.CliTestRunner;
import se.claremont.autotest.common.LogLevel;
import se.claremont.autotest.common.TestSet;
import se.claremont.autotest.guidriverpluginstructure.websupport.webdrivergluecode.WebInteractionMethods;

/**
 * Tests for PRV home page
 *
 * Created by jordam on 2016-08-17.
 */
public class PrvWebTests extends TestSet {
    @SuppressWarnings("CanBeFinal")
    @Rule public TestName currentTestName = new TestName();
    private PrvWebTestActions prv;

    @Before
    public void testSetup(){
        System.setProperty("webdriver.chrome.driver", "c:/temp/chromedriver.exe");
        startUpTestCase(currentTestName.getMethodName());
        prv = new PrvWebTestActions(new WebInteractionMethods(currentTestCase));
    }

    @After
    public void testTearDown(){
        prv.web.makeSureDriverIsClosed();
        wrapUpTestCase();
    }

    @AfterClass
    public static void classTearDown(){
        CliTestRunner.testRun.summaryReport.writeReport();
    }

    @Test
    public void dummyTest1(){
        prv.web.log(LogLevel.DEBUG, "Test");
        prv.web.log(LogLevel.EXECUTED, "Test");
        prv.web.log(LogLevel.DEBUG, "Test");
    }

    @Test
    public void dummyTest2(){
        prv.web.log(LogLevel.DEBUG, "Test");
        prv.web.log(LogLevel.EXECUTION_PROBLEM, "Execution problem");
        prv.web.log(LogLevel.DEBUG, "Test");
    }

    @Test
    public void dummyTest3(){
        currentTestCase.addKnownError("Failed verification is being tested", ".*Failed verification.*");
        prv.web.log(LogLevel.DEBUG, "Test");
        prv.web.log(LogLevel.VERIFICATION_FAILED, "Failed verification");
        prv.web.log(LogLevel.DEBUG, "Test");
    }

    @Test
    public void dummyTest4(){
        prv.web.log(LogLevel.DEBUG, "Test");
        currentTestCase.addKnownError("Fixed error", "String that is not encountered.");
        prv.web.log(LogLevel.FRAMEWORK_ERROR, "Framework error");
        prv.web.log(LogLevel.DEBUG, "Test");
    }

    @Test
    public void prvLink1() {
        currentTestCase.testName = "prvLink1";
        currentTestCase.addKnownError("Verifieringsfel i sidtitel på Våra sidor", ".*The current page title was expected to be .*");
        prv.GoToVåraTjänster();
        prv.web.closeBrowser();
    }

    @Test
    public void prvSearch() {
        currentTestCase.testName = "prvSearch";
        currentTestCase.addKnownError("Inne på PRV", ".*Could not click on.*");
        prv.search("Patent");
        prv.web.closeBrowser();
    }
}
