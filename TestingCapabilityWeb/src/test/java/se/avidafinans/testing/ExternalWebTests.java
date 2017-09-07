package se.avidafinans.testing;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import se.avidafinans.testing.applicationdescription.LandingPage;
import se.avidafinans.testing.applicationdescription.NavigationMechanism;
import se.claremont.autotest.common.testset.TestSet;
import se.claremont.autotest.websupport.webdrivergluecode.WebInteractionMethods;

public class ExternalWebTests extends TestSet{
    WebInteractionMethods web;
    NavigationMechanism nav;

    @Before
    public void setup(){
        web = new WebInteractionMethods(currentTestCase());
        nav = new NavigationMechanism(web, currentTestCase());
    }

    @After
    public void tearDown(){
        web.makeSureDriverIsClosed();
    }

    @Test
    public void pageMapper(){
        nav.landingPage();
        web.mapCurrentPage("C:\\Temp\\landingPage.txt");
    }


    @Test
    public void landingPageTest(){
        nav.landingPage();
        web.verifyPageTitleAsRegexWithTimeout(".*Lån och kredit.*", 15);
        web.verifyElementIsNotDisplayed(LandingPage.dropDownMenuItemKredit());
        web.click(LandingPage.menuButton());
        web.verifyElementIsDisplayed(LandingPage.dropDownMenuItemKredit());
    }

    @Test
    public void inkassoLoginTest(){
        nav.inkassoLoginLandingPage();
    }
}
