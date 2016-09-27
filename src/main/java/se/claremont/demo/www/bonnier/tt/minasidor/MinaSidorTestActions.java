package se.prv.minasidor;

import se.claremont.autotest.common.TestCase;
import se.claremont.autotest.guidriverpluginstructure.websupport.webdrivergluecode.WebInteractionMethods;

/**
 * Test actions for Mina sidor
 *
 * Created by jordam on 2016-08-31.
 */
@SuppressWarnings("unused")
class MinaSidorTestActions {
    final WebInteractionMethods web;
    private final MinaSidorNavigationMechanism nav;
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final TestCase testCase;


    public MinaSidorTestActions(TestCase testCase){
        this.testCase = testCase;
        web = new WebInteractionMethods(testCase);
        nav = new MinaSidorNavigationMechanism(web);
    }

    public void setLanguageToSwedish(){
        if(web.exists(MinaSidorLoggedInPage.languageSelectorEnglish())) {
            web.click(MinaSidorLoggedInPage.languageSelectorEnglish());
            web.click(MinaSidorLoggedInPage.dropDownChoiceSwedish());
        }
    }

    public void logIn(){
        nav.loggedIn();
        //web.verifyTextOnCurrentPage("Logged in");
        //web.log(LogLevel.INFO, "Logged in as '" + web.getText(MinaSidorLoggedInPage.loggedInPerson()) + "'.");
    }

    public void logOut(){
        nav.loggedIn();
        web.click(MinaSidorLoginPage.loggaUtButton());
        //web.verifyTextOnCurrentPage("Logged out");
    }

    public void listaPatent(){
        nav.loggedIn();
        web.click(MinaSidorLoggedInPage.minaPatentLink());
        //web.verifyTextOnCurrentPage("Mina Patent");
    }

    public void listaMinaMeddelanden(){
        nav.loggedIn();
        web.click(MinaSidorLoggedInPage.meddelandenLink());
        //web.verifyTextOnCurrentPage("Mina meddelanden");
    }

    public void listaMinaVarumarken(){
        nav.loggedIn();
        web.click(MinaSidorLoggedInPage.minaVarumärkenLink());
        web.verifyObjectExistence(MinaSidorLoggedInPage.minaVarumarkenHeadline());
    }

    public void markeraPatent(String subject){
        listaPatent();

    }
}
