package se.prv.minasidor;

import org.junit.*;
import org.junit.rules.TestName;
import se.claremont.autotest.common.CliTestRunner;
import se.claremont.autotest.common.TestSet;

/**
 * Tests for Mina sidor
 *
 * Created by jordam on 2016-08-31.
 */
public class MinaSidorTests extends TestSet {
    @SuppressWarnings("CanBeFinal")
    @Rule public TestName currentTestName = new TestName();
    private MinaSidorTestActions app;


    @Before
    public void testSetup(){
        startUpTestCase(currentTestName.getMethodName());
        app = new MinaSidorTestActions(currentTestCase);
        name = this.getClass().getSimpleName();
    }

    @After
    public void testTearDown(){
        app.web.makeSureDriverIsClosed();
        wrapUpTestCase();
    }

    @AfterClass
    public static void ClassTearDown(){
        CliTestRunner.testRun.summaryReport.evaluateTestSet(CliTestRunner.testRun.currentTestSet);
        CliTestRunner.testRun.summaryReport.writeReport();
    }

    @Test
    public void loginTestPositive(){
        app.logIn();
        app.logOut();
    }

    @Test
    public void listaMinaPatent(){
        app.listaPatent();
    }

    @Test
    public void listaMinaMeddelanden(){
        app.listaMinaMeddelanden();
    }

    @Test
    public void listaMinaVarumarken(){
        app.listaMinaVarumarken();
    }
}
