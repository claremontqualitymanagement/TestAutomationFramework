package se.se.prv.homepage;

import se.claremont.autotest.guidriverpluginstructure.websupport.webdrivergluecode.WebInteractionMethods;

/**
 * Test actions for PRV home page
 * Created by jordam on 2016-08-17.
 */
@SuppressWarnings("SameParameterValue")
class PrvWebTestActions {
    final WebInteractionMethods web;
    private final PrvWebNavigationMechanism nav;

    PrvWebTestActions(WebInteractionMethods web){
        this.web = web;
        this.nav = new PrvWebNavigationMechanism(web);
    }

    /**
     * Navigation mechanism for Våra Tjänster
     */
    void GoToVåraTjänster(){
        nav.NavigateToVåraTjänster();
        web.verifyPageTitleWithTimeout("Mina tjänster - PRV", 15);
    }

    /**
     * Navigates to PRV home page landing page and searches for search string, then verifying the search term exist in received page
     * @param searchText the string to enter in the search field of the page
     */
    void search(String searchText){
        nav.NavigateToPrvLandingPage();
        web.click(PrvWebLandingPage.Button_Search());
        web.submitText(PrvWebLandingPage.Input_SearchText(), searchText);
        web.verifyTextOnCurrentPage(searchText);
    }
}
