package se.claremont.tools;

import org.junit.*;
import se.claremont.autotest.common.TestCase;
import se.claremont.autotest.guidriverpluginstructure.websupport.DomElement;
import se.claremont.autotest.guidriverpluginstructure.websupport.webdrivergluecode.WebInteractionMethods;
import se.claremont.autotest.support.PerformanceTimer;

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
        web.navigate("file://c:/temp/taf.html");
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
        web.navigate("file://c:/temp/taf.html");
        DomElement checkbox = new DomElement("//input[@type='checkbox'][@value='Bike']", DomElement.IdentificationType.BY_X_PATH);
        web.manageCheckbox(checkbox, false);
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