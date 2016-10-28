package se.claremont;

import org.junit.*;
import org.junit.rules.TestName;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import se.claremont.autotest.common.*;
import se.claremont.autotest.guidriverpluginstructure.swingsupport.festswinggluecode.ApplicationManager;
import se.claremont.autotest.guidriverpluginstructure.swingsupport.festswinggluecode.SwingInteractionMethods;
import se.claremont.autotest.guidriverpluginstructure.websupport.DomElement;
import se.claremont.autotest.guidriverpluginstructure.websupport.ResponsiveAnalysis;
import se.claremont.autotest.guidriverpluginstructure.websupport.webdrivergluecode.WebDriverManager;
import se.claremont.autotest.guidriverpluginstructure.websupport.webdrivergluecode.WebInteractionMethods;
import se.claremont.autotest.restsupport.RestSupport;
import se.claremont.autotest.support.PerformanceTimer;
import se.claremont.autotest.support.SupportMethods;
//import se.claremont.autotest.testmanagementtoolintegration.testlink.TestlinkReporter;
//import se.claremont.autotest.testmanagementtoolintegration.testlink.TestlinkReporter2;
import se.claremont.tools.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * SandBox (UnitTest) containing various help tools for Test Automation Engineers.
 *
 * Created by magnusolsson on 2016-10-06.
 */
public class sandBoxTester extends TestSet{

    private static String OUTPUT_FILE_PATH = "";
    private static String ENDPOINT_TARGET_URL = "https://www.typeandtell.com/sv/";
    private static String LOCAL_MOCH_HTML_FILE = "";
    @SuppressWarnings("CanBeFinal")
    @Rule
    public TestName currentTestName = new TestName();

    @BeforeClass
    public static void classSetup(){
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
        CliTestRunner.testRun.reporters.evaluateTestSet(CliTestRunner.testRun.currentTestSet);
        CliTestRunner.testRun.reporters.report();
    }

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
        WebInteractionMethods web = new WebInteractionMethods(currentTestCase);
        web.navigate("https://validator.w3.org/nu/");
        DomElement dropdown = new DomElement("inputregion", DomElement.IdentificationType.BY_ID);
        web.selectInDropdown(dropdown, "text input");
        web.makeSureDriverIsClosed();
    }

    @Ignore
    @Test
    public void timerTests(){
        PerformanceTimer timer = new PerformanceTimer("testTimer", currentTestCase);
        try {
            Thread.sleep(500L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        timer.stopAndLogTime();
    }

    @Ignore
    @Test
    public void radioButtonTest(){
        WebInteractionMethods web = new WebInteractionMethods(currentTestCase);
        web.navigate( LOCAL_MOCH_HTML_FILE );
        DomElement radiobutton = new DomElement("radiobutton", DomElement.IdentificationType.BY_ID);
        web.chooseRadioButton(radiobutton, " Male");
        web.makeSureDriverIsClosed();
    }

    @Ignore
    @Test
    public void checkBoxTest(){
        WebInteractionMethods web = new WebInteractionMethods(currentTestCase);
        web.navigate( LOCAL_MOCH_HTML_FILE );
        DomElement checkbox = new DomElement("//input[@type='checkbox'][@value='Bike']", DomElement.IdentificationType.BY_X_PATH);
        web.manageCheckbox(checkbox, false);
        web.makeSureDriverIsClosed();
    }

    @Ignore //Takes to much time to run
    @Test
    public void sandboxPlayground(){
        WebInteractionMethods web = new WebInteractionMethods(currentTestCase);
        web.navigate( ENDPOINT_TARGET_URL );
        web.mapCurrentPage( OUTPUT_FILE_PATH );
        web.makeSureDriverIsClosed();
    }

    @Ignore
    @Test
    public void testResponsive(){
        WebDriverManager webDriverManager = new WebDriverManager(currentTestCase);
        WebDriver driver = webDriverManager.initializeWebDriver(WebDriverManager.WebBrowserType.CHROME);
        driver.get("http://www.claremont.se");
        List<Dimension> resolutions = new ArrayList<>();
        resolutions.add(new Dimension(750, 480));
        resolutions.add(new Dimension(1025, 650));
        resolutions.add(new Dimension(2028, 900));
        ResponsiveAnalysis responsiveAnalysis = new ResponsiveAnalysis(driver, resolutions, currentTestCase);
        responsiveAnalysis.performAnalysisAndReportResults();
        driver.close();
    }


    @Ignore
    @Test
    public void testDesktopScreenshot() {
        WebInteractionMethods web = new WebInteractionMethods(currentTestCase);
        web.navigate("https://www.claremont.se");
        web.saveScreenshot();
    }

    @Ignore
    @Test
    public void testSwingApplicationStart(){
        int javaProcessesBefore = numberOfJavaProcesses();
        SwingInteractionMethods s = new SwingInteractionMethods(currentTestCase);
        List<String> arguments = new ArrayList<>();
        arguments.add("java.exe");
        arguments.add("-jar");
        arguments.add("C:\\Users\\jordam\\OneDrive\\Documents\\Claremont-jobb\\Alster\\QtpUsageAnalysis.jar");
        s.startProgram(String.join(" ", arguments));
        currentTestCase.writeProcessListDeviationsFromSystemStartToLog();
        int javaProcessesAfter = numberOfJavaProcesses();
        currentTestCase.log(LogLevel.INFO, "Number of java processes before: " +javaProcessesBefore + ". Number of java processes after: " + javaProcessesAfter + ".");
    }


    private int numberOfJavaProcesses(){
        ApplicationManager am = new ApplicationManager(currentTestCase);
        List<String> processes = new ArrayList<>();
        int javaProcessCount = 0;
        processes.addAll(am.listActiveRunningProcessesOnLocalMachine());
        for(String string : processes){
            if(string.equals("java.exe")){
                javaProcessCount++;
            }
        }
        return javaProcessCount;

    }

    @Ignore
    @Test
    public void phantomJSdriverTest(){
        WebDriverManager wdm = new WebDriverManager(currentTestCase);
        WebDriver driver = wdm.initializeWebDriver(WebDriverManager.WebBrowserType.PHANTOMJS);
        driver.get("http://www.claremont.se");
        currentTestCase.log(LogLevel.INFO, driver.getTitle());
        currentTestCase.log(LogLevel.INFO, currentTestCase.toJson());
        driver.close();
    }

    @Ignore
    @Test
    public void testNewElementMethods(){
        WebInteractionMethods web = new WebInteractionMethods(currentTestCase);
        web.navigate("http://www.claremont.se");
        DomElement kontaktLink = new DomElement("KONTAKT", DomElement.IdentificationType.BY_LINK_TEXT);
        web.click(kontaktLink);
        web.verifyTextExistOnCurrentPage("Birger Jarlsgatan 7");
        web.makeSureDriverIsClosed();
    }


    @Ignore
    @Test
    public void testRestToTestlink(){
        RestSupport rest = new RestSupport(currentTestCase);
        rest.responseCodeFromGetRequest("http://172.16.13.49/testlink/lib/api/rest/v1/testprojects/Lekprojekt/testcases");
    }

    /*
    @Ignore
    @Test
    public void testTestlinkConnection(){
        //TestlinkReporter2 testlink2 = new TestlinkReporter2("50980a19f7180911b06cf34da66478c0", "http://172.16.13.49/testlink/lib/api/xmlrpc/v1/xmlrpc.php");
        //TestlinkReporter2.doIt("50980a19f7180911b06cf34da66478c0", "http://172.16.13.49/testlink/lib/api/xmlrpc/v1/xmlrpc.php", 1, 2, "1", 1);
        TestlinkReporter testlink = new TestlinkReporter("2a861343a3dca60b876ca5b6567568de", "http://127.0.0.1:81/testlink/lib/api/xmlrpc/v1/xmlrpc.php", currentTestCase.testCaseLog, "Mina sidor", "Autotest", "joda");
        //currentTestCase.log(LogLevel.INFO, testlink.testlinkProjectsAndPlansListing());

        //currentTestCase.log(LogLevel.INFO, testlink.testlinkProjectsAndPlansListing());

        //currentTestCase.log(LogLevel.INFO, testlink.setupInformation());
        testlink.evaluateTestCase("Mina sidor", "se.claremont.sandBoxTester", "se.claremont.sandBoxTester", "testTestlinkConnection", currentTestCase);
    }
    */

}
