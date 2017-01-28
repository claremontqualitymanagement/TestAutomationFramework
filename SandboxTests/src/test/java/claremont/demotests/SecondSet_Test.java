package claremont.demotests;

import org.junit.*;
import org.junit.rules.TestName;
import se.claremont.autotest.common.logging.LogLevel;
import se.claremont.autotest.common.testrun.TestRun;
import se.claremont.autotest.common.testset.TestSet;

/**
 * Created by jordam on 2016-11-22.
 */
public class SecondSet_Test extends TestSet {
    @Rule
    public TestName currentTestName = new TestName();

    @BeforeClass
    public static void classSetup(){
        //TestRun.settings.setValue(Settings.SettingParameters.PATH_TO_LOGO, "https://www.mycompany.se/logo.png");
    }

    @Before
    public void testSetup(){
        startUpTestCase(currentTestName.getMethodName());
        name = this.getClass().getSimpleName();
    }

    @After
    public void testTearDown(){
        wrapUpTestCase();
    }

    @AfterClass
    public static void ClassTearDown(){
        TestRun.evaluateCurrentTestSet();
        TestRun.reportTestRun();
    }

    @Test
    public void startupTestLandingPageAssessment(){

    }

    @Test
    public void accountCreation(){

    }

    @Test
    public void loggedInAsFirstTimeUser(){

    }

    @Test
    public void userProfileEditing(){

    }

    @Test
    public void returningUser(){

    }

    @Test
    public void administratorLinksForAdministrators(){

    }

    @Test
    public void noAdministratorLinksForNonAdministrators(){

    }

    @Test
    public void updatingIssues(){

    }

    @Test
    public void updatingArchivedIssues(){
        currentTestCase.addKnownError("Problems with issues archive.", ".*Could not find issue.*");
        currentTestCase.log(LogLevel.EXECUTION_PROBLEM, "Could not find issue 'Issue#12345' in issuesTable.");
    }

    @Test
    public void searchIssues(){

    }

    @Test
    public void filterIssuesByUser(){
        currentTestCase.addKnownError("Cannot filter issues by users yet.", "fasdghf");
    }

    @Test
    public void createIssue(){

    }

    @Test
    public void closeIssue(){

    }







}
