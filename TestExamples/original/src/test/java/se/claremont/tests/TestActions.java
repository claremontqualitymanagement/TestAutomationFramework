package se.claremont.tests;

import se.claremont.applicationdescription.NavigationMechanisms;
import se.claremont.autotest.common.TestCase;
import se.claremont.autotest.guidriverpluginstructure.websupport.webdrivergluecode.WebInteractionMethods;

/**
 * Created by jordam on 2016-12-04.
 */
public class TestActions {
    private TestCase testCase;
    WebInteractionMethods web;
    NavigationMechanisms nav;

    public TestActions(TestCase testCase){
        this.testCase = testCase;
        web = new WebInteractionMethods(this.testCase);
        nav = new NavigationMechanisms(this.testCase);
    }

    public void checkLandingPageLinks() {
        nav.ensureLandingPageDisplayed();
        web.wait(5000);
    }

    public void checkLandingPageLayout() {
        nav.ensureLandingPageDisplayed();
        web.wait(5000);
    }

    public void checkLandingPageTexts() {
        nav.ensureLandingPageDisplayed();
        web.wait(5000);
    }

    public void checkLandingPageWithW3CValidator() {
        nav.ensureLandingPageDisplayed();
        web.wait(5000);
        web.verifyCurrentPageSourceWithW3validator(false);
    }
}
