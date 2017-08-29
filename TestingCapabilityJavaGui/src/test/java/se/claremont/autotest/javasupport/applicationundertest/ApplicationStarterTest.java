package se.claremont.autotest.javasupport.applicationundertest;

import org.junit.*;
import org.junit.runner.RunWith;
import se.claremont.autotest.common.logging.LogLevel;
import se.claremont.autotest.common.logging.LogPost;
import se.claremont.autotest.common.testcase.TestCase;
import se.claremont.autotest.common.testrun.TafTestRunner;
import se.claremont.autotest.common.testset.TestSet;
import se.claremont.autotest.javasupport.applicationundertest.applicationstarters.ApplicationStarter;
import se.claremont.autotest.javasupport.interaction.GenericInteractionMethods;
import se.claremont.autotest.javasupport.objectstructure.JavaGuiElement;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Tests starting of Java programs from TAF
 *
 * Created by jordam on 2017-02-11.
 */
@SuppressWarnings({"WeakerAccess", "ConstantConditions"})
@RunWith(TafTestRunner.class)
public class ApplicationStarterTest extends TestSet {
    @BeforeClass
    public static void classSetup(){ tempFolder = System.getProperty("java.io.tmpdir"); }

    @Before
    public void setup(){
        Assume.assumeTrue(Desktop.isDesktopSupported());
    }

    @After
    public void teardown(){
        ApplicationUnderTest.closeAllWindows();
    }

    @AfterClass
    public static void classTeardown(){
        try {
            Files.deleteIfExists(Paths.get(tempFolder + "JavaApp.jar"));
        } catch (IOException ignored) {
            System.out.println("Could not delete temporary file '" + tempFolder + "JavaApp.jar'. " + ignored.getMessage());
        }
    }

    static String tempFolder;

    private void makeSureJavaAppIsInTempFolder(){
        File f = new File(tempFolder);
        if(f.exists() && !f.isDirectory()) return;
        URL url = ClassLoader.getSystemClassLoader().getResource("JavaApp.jar");
        try {
            saveFile(url, tempFolder + "JavaApp.jar");
            Assume.assumeTrue("Warning: Could not save test file JavaApp.jar to temp folder. Hence cannot test application starter methods", Files.exists(Paths.get(tempFolder + "JavaApp.jar")));
        } catch (IOException e) {
            //noinspection ConstantConditions
            Assume.assumeTrue("Warning: Could not save test file JavaApp.jar to temp folder. Hence cannot test application starter methods. Error: " + System.lineSeparator() + e.toString(), false);
        }
    }

    public static void saveFile(URL url, String file) throws IOException {
        System.out.println("opening connection");
        InputStream in = url.openStream();
        FileOutputStream fos = new FileOutputStream(new File(file));

        System.out.println("Reading file from url " + url);
        int length = -1;
        byte[] buffer = new byte[1024];// buffer for portion of data from
        // connection
        while ((length = in.read(buffer)) > -1) {
            fos.write(buffer, 0, length);
        }


        fos.close();
        in.close();
        System.out.println("File " + url  + " was saved to " + file + ".");
    }

    @Test
    @Ignore
    public void startApp(){
        makeSureJavaAppIsInTempFolder();
        ApplicationStarter as = new ApplicationStarter(currentTestCase);
        try {
            as.startJar(new URL("file:///" + tempFolder.replace("\\", "/") + "JavaApp.jar"));
            Frame app = (Frame) as.getWindow();
            Assert.assertTrue(app != null);
            GenericInteractionMethods java = new GenericInteractionMethods(currentTestCase);
            JavaGuiElement button = new JavaGuiElement("OkButton", "Ok", JavaGuiElement.IdType.ELEMENT_TEXT);
            Object c = button.getRuntimeComponent();
            Assert.assertNotNull(c);
            currentTestCase.log(LogLevel.INFO, "Button text: '" + java.getText(button) + "'.");
            Assert.assertTrue(c.toString().contains("Ok"));
            java.verifyElementTextIsExactly(button, "Ok");
            java.verifyElementTextContains(button, "k");
            java.verifyElementTextIsRegexMatch(button, ".*k.*");
        } catch (MalformedURLException e) {
            Assume.assumeTrue("Could not start application for testing." + e.toString(), false);
        }
        ApplicationUnderTest.closeAllWindows();
    }

    @Test
    @Ignore
    public void appStartFromString(){
        try {
            makeSureJavaAppIsInTempFolder();
            ApplicationStarter as = new ApplicationStarter(currentTestCase);
            as.startJar(tempFolder + "JavaApp.jar");
            GenericInteractionMethods java = new GenericInteractionMethods(currentTestCase);
            JavaGuiElement button = new JavaGuiElement("OkButton", "Ok", JavaGuiElement.IdType.ELEMENT_TEXT);
            Object c = button.getRuntimeComponent();
            Assert.assertNotNull(c);
            Assert.assertTrue(c.toString().contains("Ok"));
            ApplicationUnderTest.closeAllWindows();
        }catch (Exception e){
            Assume.assumeTrue("Could not start application for testing." + e.toString(), false);
        }
    }

    @Test
    @Ignore
    public void listWindows() {
        try{
            makeSureJavaAppIsInTempFolder();
            TestCase tempTestCase = new TestCase(null, "dummy");
            ApplicationStarter as = new ApplicationStarter(tempTestCase);
            as.startJar(new URL("file:///" + tempFolder.replace("\\", "/") + "JavaApp.jar"));
            ApplicationUnderTest.logCurrentWindows(tempTestCase);
            boolean found = false;
            for(LogPost logPost : tempTestCase.testCaseResult.testCaseLog.logPosts){
                if(logPost.message.contains("Window title: 'Java test application'. Shown:true")){
                    found = true;
                }
            }
            Assert.assertTrue(tempTestCase.testCaseResult.testCaseLog.toString(), found);
        }catch (Exception e){
            Assume.assumeTrue("Could not start application for testing." + e.toString(), false);
        }
        ApplicationUnderTest.closeAllWindows();
    }

}
