package se.claremont.rest;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import se.claremont.autotest.common.Settings;
import se.claremont.autotest.common.TestRun;
import se.claremont.autotest.common.TestRunReporterHtmlSummaryReportFile;
import se.claremont.autotest.common.TestSet;
import se.claremont.autotest.restsupport.RestResponse;
import se.claremont.autotest.restsupport.RestSupport;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = Application.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestIntegrationTest extends TestSet {
    @LocalServerPort
    private int port;

    @Rule
    public TestName currentTestName = new TestName();
    private RestSupport restSupport;

    @BeforeClass
    public static void classSetup(){
        TestRun.settings.setValue(Settings.SettingParameters.PATH_TO_LOGO, "http://46.101.193.212/TAF/images/claremontlogo.gif");

        //Base log folder is a TAF folder under user home folder
        String path = RestIntegrationTest.class.getClassLoader().getResource(".").getFile();
        TestRun.settings.setValue(Settings.SettingParameters.BASE_LOG_FOLDER, path);

        TestRun.reporters.addTestRunReporter(new TestRunReporterHtmlSummaryReportFile());
    }

    @Before
    public void setup() {
        startUpTestCase(currentTestName.getMethodName());
        restSupport = new RestSupport(currentTestCase);
    }

    @Test
    public void whenPostHello_ExpectResponseWorld() {
        RestResponse response = restSupport.responseFromPostRequest(
                "http://localhost:" + port + "/hello",
                MediaType.ALL_VALUE,
                "");


        assertEquals(String.valueOf(HttpStatus.OK.value()), response.responseCode);
        assertEquals("world", response.body);
    }

    @Test
    public void whenGetHello_ExpectResponseWorld() {
        RestResponse response = restSupport.responseFromGetRequest(
                "http://localhost:" + port + "/hello");

        assertFalse(response.isSuccessful());
        assertEquals(String.valueOf(HttpStatus.METHOD_NOT_ALLOWED.value()), response.responseCode);
    }

    @Test
    public void whenPutHello_ExpectResponseWorld() {
        RestResponse response = restSupport.responseFromPutRequest(
                "http://localhost:" + port + "/hello",
                MediaType.ALL_VALUE,
                "");

        assertFalse(response.isSuccessful());
        assertEquals(String.valueOf(HttpStatus.METHOD_NOT_ALLOWED.value()), response.responseCode);
    }

    @Test
    public void whenDeleteHello_ExpectResponseWorld() {
        RestResponse response = restSupport.responseFromDeleteRequest("http://localhost:" + port + "/hello");

        assertFalse(response.isSuccessful());
        assertEquals(String.valueOf(HttpStatus.METHOD_NOT_ALLOWED.value()), response.responseCode);
    }

    @After
    public void testTearDown(){
        wrapUpTestCase();
    }

    @AfterClass
    public static void ClassTearDown(){
        TestRun.reporters.evaluateTestSet(TestRun.currentTestSet);
        TestRun.reporters.report();
    }

}
