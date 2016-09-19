package se.se.prv.homepage;

import se.claremont.autotest.common.LogLevel;
import se.claremont.autotest.guidriverpluginstructure.websupport.webdrivergluecode.WebInteractionMethods;

/**
 * Navigation mechanisms for the test actions for PRV home page
 *
 * Created by jordam on 2016-08-17.
 */
class PrvWebNavigationMechanism {
    private final WebInteractionMethods web;

    PrvWebNavigationMechanism(WebInteractionMethods webb){
        this.web = webb;
    }

    void NavigateToPrvLandingPage(){
        web.log(LogLevel.INFO, "Navigating to 'PRV landing page'.");
        web.navigate("https://prv.se");
    }

    void NavigateToVåraTjänster(){
        web.log(LogLevel.INFO, "Navigating to 'Våra Tjänster'.");
        NavigateToPrvLandingPage();
        web.click(PrvWebLandingPage.buttonVaraTjanster());
    }
}
