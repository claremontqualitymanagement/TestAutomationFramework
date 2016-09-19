package se.prv.minasidor;

import se.claremont.autotest.guidriverpluginstructure.websupport.DomElement;
import se.claremont.autotest.guidriverpluginstructure.websupport.WebPage;

/**
 * Declaration of gui elements on the Mina sidor login page
 *
 * Created by jordam on 2016-08-31.
 */
@SuppressWarnings("unused")
class MinaSidorLoginPage extends WebPage {

    public static DomElement mobiltBankIdButton(){
        return new DomElement(" Mobilt BankID", DomElement.IdentificationType.BY_LINK_TEXT);
    }

    public static DomElement loggaInButton(){
        return new DomElement("//*[.='Login']", DomElement.IdentificationType.BY_X_PATH);
    }

    public static DomElement loggaUtButton(){
        return new DomElement("//*[.='Logout']", DomElement.IdentificationType.BY_X_PATH);
    }

    public static DomElement loggedInText(){
        return new DomElement("//*[.='Logged in']", DomElement.IdentificationType.BY_X_PATH);
    }

    public static DomElement loggedOutText(){
        return new DomElement("//*[.='Logged out']", DomElement.IdentificationType.BY_X_PATH);
    }

    public static DomElement retrieveProfileButton(){
        return new DomElement("//button[2]", DomElement.IdentificationType.BY_X_PATH);
    }
}
