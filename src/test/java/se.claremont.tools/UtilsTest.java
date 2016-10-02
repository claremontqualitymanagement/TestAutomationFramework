package se.claremont.tools;

import org.junit.*;
import se.claremont.autotest.common.TestCase;
import se.claremont.autotest.guidriverpluginstructure.websupport.DomElement;
import se.claremont.autotest.guidriverpluginstructure.websupport.webdrivergluecode.WebInteractionMethods;

import java.io.File;

/**
 * Created by magnusolsson on 2016-09-23.
 */
public class UtilsTest{

    @Test
    public void getInstance(){
        Assert.assertTrue( Utils.getInstance() != null );
    }

    @Test
    public void getOS() {
        Assert.assertTrue( !Utils.getInstance().getOS().equalsIgnoreCase("") );
        Assert.assertTrue( Utils.getInstance().getOS().toLowerCase().contains("mac") ||
                Utils.getInstance().getOS().toLowerCase().contains("linux") ||
                Utils.getInstance().getOS().toLowerCase().contains("win") );
    }

    @Ignore
    @Test
    public void w3cValidationTest(){
        TestCase testCase = new TestCase(null, "dummyName");
        WebInteractionMethods web = new WebInteractionMethods(testCase);
        web.navigate("https://www.typeandtell.com/sv/");
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

    @Ignore //Takes to much time to run
    @Test
    public void sandboxPlayground(){
        WebInteractionMethods web = new WebInteractionMethods(new TestCase(null, "dummyName"));

        web.navigate("https://www.typeandtell.com/sv/");
        if( Utils.getInstance().amIMacOS() )
            web.mapCurrentPage( Utils.getInstance().getUserWorkingDirectory() + File.separator + "TAF" + File.separator + "Temp" + File.separator + "Output.txt" );
        else
            web.mapCurrentPage("C:\\Temp\\Output.txt");

        web.makeSureDriverIsClosed();
    }

}