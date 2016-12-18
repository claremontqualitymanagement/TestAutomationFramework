package se.claremont.applicationdescription;

import se.claremont.autotest.common.LogLevel;
import se.claremont.autotest.common.TestCase;
import se.claremont.autotest.guidriverpluginstructure.websupport.webdrivergluecode.WebInteractionMethods;

/**
 * Created by jordam on 2016-12-04.
 */
public class NavigationMechanisms {
    TestCase testCase;
    WebInteractionMethods web;

    public NavigationMechanisms(TestCase testCase){
        this.testCase = testCase;
        web = new WebInteractionMethods(this.testCase);
    }

    public void ensureLandingPageDisplayed(){
        if(web.pageTitleExistWithTimeout("Claremont", 1)){
            web.log(LogLevel.DEBUG, "Made sure the landing page was displayed. It already was.");
            return;
        }
        web.navigate("http://www.claremont.se");
        if(web.pageTitleExistWithTimeout("Claremont", 5)){
            web.log(LogLevel.EXECUTED, "Made sure the landing page got displayed.");
        } else {
            web.log(LogLevel.EXECUTION_PROBLEM, "Could not navigate to landing page.");
            web.saveScreenshot(null);
            web.saveDesktopScreenshot();
            web.saveHtmlContentOfCurrentPage();
            web.writeRunningProcessListDeviationsSinceTestCaseStart();
        }
    }

    public void ensureContactsPageDisplayed(){

    }
}
