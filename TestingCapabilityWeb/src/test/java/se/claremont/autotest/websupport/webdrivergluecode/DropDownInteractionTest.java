package se.claremont.autotest.websupport.webdrivergluecode;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Ignore;
import org.junit.Test;
import se.claremont.autotest.common.testcase.TestCase;
import se.claremont.autotest.websupport.DomElement;

import java.io.File;
import java.net.URL;

/**
 * Created by jordam on 2017-01-28.
 */
public class DropDownInteractionTest {
    private String getTestFileFromTestResourcesFolder(String fileName){
        URL url = Thread.currentThread().getContextClassLoader().getResource(fileName);
        File file = new File(url.getPath());
        Assume.assumeNotNull(file);
        return file.getAbsolutePath();
    }

    @Test
    @Ignore
    /*
      This test case tries clicking a button that at first is not displayed, and then is not enabled.
      When enabled the click is performed.
     */
    public void delayedDisplayOfElementShouldStillBeClickable(){
        WebInteractionMethods web = new WebInteractionMethods(new TestCase(null, "dummy"));
        web.navigate("file://" + getTestFileFromTestResourcesFolder("delayTest.html"));
        DomElement dropDown = new DomElement("dropdown", DomElement.IdentificationType.BY_ID);
        web.selectInDropdown(dropDown, "Saab");
        System.out.println(web.getSelectedValueFromDropdown(dropDown));
        Assert.assertTrue(web.getSelectedValueFromDropdown(dropDown).contains("Saab"));
        web.makeSureDriverIsClosed();
    }

}
