package se.claremont;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import se.claremont.autotest.common.LogLevel;
import se.claremont.autotest.common.TestCase;
import se.claremont.autotest.guidriverpluginstructure.websupport.DomElement;
import se.claremont.autotest.guidriverpluginstructure.websupport.webdrivergluecode.WebDriverManager;
import se.claremont.autotest.guidriverpluginstructure.websupport.webdrivergluecode.WebInteractionMethods;
import se.claremont.autotest.support.PerformanceTimer;
import se.claremont.tools.Utils;

import java.io.File;

/**
 * SandBox (UnitTest) containing various help tools for Test Automation Engineers.
 *
 * Created by magnusolsson on 2016-10-06.
 */
public class sandBoxTester {

    private static String OUTPUT_FILE_PATH = "";
    private static String ENDPOINT_TARGET_URL = "https://www.typeandtell.com/sv/";
    private static String LOCAL_MOCH_HTML_FILE = "";

    @Before
    public void whoIam() {
        if( Utils.getInstance().amIMacOS() ) {
            OUTPUT_FILE_PATH = Utils.getInstance().getUserWorkingDirectory() + File.separator + "TAF" + File.separator + "Temp" + File.separator + "Output.txt";
            LOCAL_MOCH_HTML_FILE = "";
        } else {
            OUTPUT_FILE_PATH = "C:\\Temp\\Output.txt";
            LOCAL_MOCH_HTML_FILE = "file://c:/temp/taf.html";
        }
    }

    @Ignore
    @Test
    public void w3cValidationTest(){
        TestCase testCase = new TestCase(null, "dummyName");
        WebInteractionMethods web = new WebInteractionMethods(testCase);
        web.navigate( ENDPOINT_TARGET_URL );
        web.verifyCurrentPageSourceWithW3validator(false);
        web.makeSureDriverIsClosed();
        testCase.report();
    }

    @Ignore
    @Test
    public void dropdownTest(){
        TestCase testCase = new TestCase(null, "dummyName");
        WebInteractionMethods web = new WebInteractionMethods(testCase);
        web.navigate("https://validator.w3.org/nu/");
        DomElement dropdown = new DomElement("docselect", DomElement.IdentificationType.BY_ID);
        web.selectInDropdown(dropdown, "text input");
        web.makeSureDriverIsClosed();
        testCase.report();
    }

    @Ignore
    @Test
    public void dropdownTestValueDowsNotExist(){
        TestCase testCase = new TestCase(null, "dummyName");
        WebInteractionMethods web = new WebInteractionMethods(testCase);
        web.navigate("https://validator.w3.org/nu/");
        DomElement dropdown = new DomElement("docselect", DomElement.IdentificationType.BY_ID);
        web.selectInDropdown(dropdown, "nonexistingChoice");
        web.makeSureDriverIsClosed();
        testCase.report();
    }

    @Ignore
    @Test
    public void dropdownTestNoSelectorElement(){
        TestCase testCase = new TestCase(null, "dummyName");
        WebInteractionMethods web = new WebInteractionMethods(testCase);
        web.navigate("https://validator.w3.org/nu/");
        DomElement dropdown = new DomElement("inputregion", DomElement.IdentificationType.BY_ID);
        web.selectInDropdown(dropdown, "text input");
        web.makeSureDriverIsClosed();
        testCase.report();
    }

    @Ignore
    @Test
    public void timerTests(){
        TestCase testCase = new TestCase(null, "dummyName");
        PerformanceTimer timer = new PerformanceTimer("testTimer", testCase);
        try {
            Thread.sleep(500L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        timer.stopAndLogTime();
        testCase.report();
    }

    @Ignore
    @Test
    public void radioButtonTest(){
        TestCase testCase = new TestCase(null, "dummyName");
        WebInteractionMethods web = new WebInteractionMethods(testCase);
        web.navigate( LOCAL_MOCH_HTML_FILE );
        DomElement radiobutton = new DomElement("radiobutton", DomElement.IdentificationType.BY_ID);
        web.chooseRadioButton(radiobutton, " Male");
        web.makeSureDriverIsClosed();
        testCase.report();
    }

    @Ignore
    @Test
    public void checkBoxTest(){
        TestCase testCase = new TestCase(null, "dummyName");
        WebInteractionMethods web = new WebInteractionMethods(testCase);
        web.navigate( LOCAL_MOCH_HTML_FILE );
        DomElement checkbox = new DomElement("//input[@type='checkbox'][@value='Bike']", DomElement.IdentificationType.BY_X_PATH);
        web.manageCheckbox(checkbox, false);
        web.makeSureDriverIsClosed();
        testCase.report();
    }

    //@Ignore //Takes to much time to run
    @Test
    public void sandboxPlayground(){
        WebInteractionMethods web = new WebInteractionMethods(new TestCase(null, "dummyName"));

        web.navigate( ENDPOINT_TARGET_URL );
        web.mapCurrentPage( OUTPUT_FILE_PATH );

        web.makeSureDriverIsClosed();
    }

    @Ignore
    @Test
    public void phantomJSdriverTest(){
        TestCase testCase = new TestCase(null, "dummyName");
        WebDriver driver = WebDriverManager.initializeWebDriver(WebDriverManager.WebBrowserType.PHANTOMJS, testCase);
        driver.get("http://www.claremont.se");
        testCase.log(LogLevel.INFO, driver.getTitle());
        testCase.report();
    }


}
