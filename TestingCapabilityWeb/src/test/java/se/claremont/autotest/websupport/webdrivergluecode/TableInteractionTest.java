package se.claremont.autotest.websupport.webdrivergluecode;

import org.junit.*;
import org.junit.rules.TestName;
import se.claremont.autotest.common.support.tableverification.CellMatchingType;
import se.claremont.autotest.common.testrun.Settings;
import se.claremont.autotest.common.testrun.TestRun;
import se.claremont.autotest.common.testset.TestSet;
import se.claremont.autotest.websupport.DomElement;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jordam on 2017-01-28.
 */
public class TableInteractionTest extends TestSet {

    @Rule
    public TestName currentTestName = new TestName();


    @BeforeClass
    public static void classSetup(){
        TestRun.settings.setValue(Settings.SettingParameters.BASE_LOG_FOLDER, "//172.16.202.10/autotest");
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
        TestRun.reporters.evaluateTestSet(TestRun.currentTestSet);
        TestRun.reporters.reportTestRun();
    }

    @Test
    /*
      This test case tries reading from a table that at first is not displayed.
     */
    public void delayedTableShouldBeWaitedFor(){
        WebInteractionMethods web = new WebInteractionMethods(currentTestCase);
        web.navigate("file://" + TestHelper.getTestFileFromTestResourcesFolder("delayTest.html"));
        DomElement table = new DomElement("table", DomElement.IdentificationType.BY_ID);
        List<String> headlines = new ArrayList<>();
        headlines.add("Headline2");
        headlines.add("Headline1");
        web.verifyTableHeadlines(table, headlines);
        web.verifyTableHeadline(table, "Headline1");
        web.verifyTableHeadline(table, "Headline2");
        web.verifyTableRow(table, "Headline1:Row 2 headline1", CellMatchingType.EXACT_MATCH);
        web.makeSureDriverIsClosed();
    }

}
