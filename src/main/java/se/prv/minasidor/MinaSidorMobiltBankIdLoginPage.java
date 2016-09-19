package se.prv.minasidor;

import se.claremont.autotest.guidriverpluginstructure.websupport.DomElement;

/**
 * Class to describe the GUI elements of the MobiltBankID page for Mina Sidor
 *
 * Created by jordam on 2016-09-02.
 */
@SuppressWarnings("unused")
class MinaSidorMobiltBankIdLoginPage {

    public static DomElement inputFieldPersonnummer(){
        return new DomElement("ssn", DomElement.IdentificationType.BY_NAME);
    }

    public static DomElement okButton(){
        return new DomElement("OK", DomElement.IdentificationType.BY_LINK_TEXT);
    }
}
