package se.prv.minasidor;

import se.claremont.autotest.common.LogLevel;
import se.claremont.autotest.guidriverpluginstructure.websupport.webdrivergluecode.WebInteractionMethods;

/**
 * Navigational mechanisms for Mina sidor
 *
 * Created by jordam on 2016-08-31.
 */
@SuppressWarnings("unused")
class MinaSidorNavigationMechanism {
    private final WebInteractionMethods web;

    public MinaSidorNavigationMechanism(WebInteractionMethods web){
        this.web = web;
    }

    @SuppressWarnings("WeakerAccess")
    public void navigateToLoginPage(){
        web.log(LogLevel.INFO, "Navigating to 'Mina Sidor login page'.");
        web.navigate("http://172.16.13.49:8282/");
    }

    public void navigateToMobiltBankIdLoginPage(){
        navigateToLoginPage();
        web.click(MinaSidorLoginPage.mobiltBankIdButton());
    }

    public void loggedIn(){
        if(!web.exists(MinaSidorLoginPage.loggedInText())){
            navigateToLoginPage();
            web.click(MinaSidorLoginPage.loggaInButton());
            web.checkAlert();
            web.click(MinaSidorLoginPage.retrieveProfileButton());
            //web.log(LogLevel.INFO, "Logged in as '" + web.getText(MinaSidorLoggedInPage.loggedInPerson()) + "'.");
        }
    }

}
