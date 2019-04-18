import org.junit.Assert;
import org.junit.Test;
import se.claremont.taf.core.logging.GenericJavaObjectToHtml;
import se.claremont.taf.core.testcase.TestCase;
import se.claremont.taf.eyeautomatesupport.EyeAutomateDriver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GuiTestingTests {

    @Test
    public void initializationTest() throws IOException {
        TestCase testCase = new TestCase();
        EyeAutomateDriver driver = new EyeAutomateDriver(testCase);
        Files.write(Paths.get("C:\\temp\\test.html"), GenericJavaObjectToHtml.toHtmlPage(driver).getBytes());
        Assert.assertNotNull(driver.scriptRunner);
    }
}
