package se.claremont.autotest.websupport.webdrivergluecode;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import se.claremont.autotest.common.testcase.TestCase;
import se.claremont.autotest.common.testset.UnitTestClass;
import se.claremont.autotest.websupport.DomElement;

/**
 * Created by jordam on 2017-01-28.
 */
public class DropDownInteractionTest extends UnitTestClass{
    @Test
    @Ignore
    /*
      This test case tries clicking a button that at first is not displayed, and then is not enabled.
      When enabled the click is performed.
     */
    public void delayedDisplayOfElementShouldStillBeClickable(){
        WebInteractionMethods web = new WebInteractionMethods(new TestCase(null, "dummy"));
        web.navigate("file://" + TestHelper.getTestFileFromTestResourcesFolder("delayTest.html"));
        DomElement dropDown = new DomElement("dropdown", DomElement.IdentificationType.BY_ID);
        web.selectInDropdown(dropDown, "Saab");
        System.out.println(web.getSelectedValueFromDropdown(dropDown));
        Assert.assertTrue(web.getSelectedValueFromDropdown(dropDown).contains("Saab"));
        web.makeSureDriverIsClosed();
    }

}
