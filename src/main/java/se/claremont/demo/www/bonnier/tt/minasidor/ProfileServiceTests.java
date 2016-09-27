package se.prv.minasidor;

import org.junit.*;
import org.junit.rules.TestName;
import se.claremont.autotest.common.CliTestRunner;
import se.claremont.autotest.common.TestSet;

/**
 * Tests for the Profile micro service of Mina Sidor
 *
 * Created by jordam on 2016-09-13.
 */
public class ProfileServiceTests extends TestSet {
    @SuppressWarnings("CanBeFinal")
    @Rule
    public TestName currentTestName = new TestName();
    private ProfileServiceTestActions app;

    @Before
    public void testSetup(){
        startUpTestCase(currentTestName.getMethodName());
        app = new ProfileServiceTestActions(currentTestCase);
        name = this.getClass().getSimpleName();
    }

    @After
    public void testTearDown(){
        wrapUpTestCase();
    }

    @AfterClass
    public static void ClassTearDown(){
        //CliTestRunner.testRun.summaryReport.evaluateTestSet(CliTestRunner.testRun.currentTestSet);
        //CliTestRunner.testRun.summaryReport.writeReport();
    }

    @Test
    public void createProfile(){ app.createProfile("200104235678", "EPO", "Pelle", "Persson", ""); }

    @Test
    public void deleteProfile(){ app.deleteProfile("200104235678"); }

    @Test
    public void getProfile(){
        app.verifyProfileContent("197701011234");
    }

    @Test
    public void getProfileFamilyName() { app.getProfileFamilyName("197701011234");}

    @Test
    public void updateProfile(){ app.updateProfile("200104235678", "EPO", "Pelle", "Pettersson"); }
}
