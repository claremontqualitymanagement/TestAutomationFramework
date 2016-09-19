package se.prv.minasidor;

import se.claremont.autotest.guidriverpluginstructure.websupport.DomElement;
import se.claremont.autotest.guidriverpluginstructure.websupport.WebPage;

/**
 * Class to describe the elements in the Logged-in page of Mina Sidor
 *
 * Created by jordam on 2016-09-16.
 */
@SuppressWarnings("unused")
class MinaSidorLoggedInPage extends WebPage {

    public static DomElement tjänsterPatentLink(){
        return new DomElement("Tjänster patent", DomElement.IdentificationType.BY_LINK_TEXT);
    }

    public static DomElement minaPatentLink(){
        return new DomElement("Mina Patent", DomElement.IdentificationType.BY_LINK_TEXT);
    }

    public static DomElement meddelandenLink(){
        return new DomElement("Meddelanden", DomElement.IdentificationType.BY_LINK_TEXT);
    }

    public static DomElement minaVarumärkenLink(){
        return new DomElement("Mina varumärken", DomElement.IdentificationType.BY_LINK_TEXT);
    }

    public static DomElement tjänsterVarumärkenLink(){
        return new DomElement("Tjänster varumärken", DomElement.IdentificationType.BY_LINK_TEXT);
    }

    public static DomElement despositionssaldoLink(){
        return new DomElement("Despositionssaldo", DomElement.IdentificationType.BY_LINK_TEXT);
    }

    public static DomElement adminLink(){
        return new DomElement("Admin", DomElement.IdentificationType.BY_LINK_TEXT);
    }

    public static DomElement adminUsersLink(){
        return new DomElement("Användare", DomElement.IdentificationType.BY_LINK_TEXT);
    }

    public static DomElement adminAccountsLink(){
        return new DomElement("Konton", DomElement.IdentificationType.BY_LINK_TEXT);
    }
    public static DomElement adminMonitoringLink(){
        return new DomElement("Övervakning", DomElement.IdentificationType.BY_LINK_TEXT);
    }

    public static DomElement adminLoggingLink(){
        return new DomElement("Loggning", DomElement.IdentificationType.BY_LINK_TEXT);
    }

    public static DomElement searchLinks(){
        return new DomElement("Söktjänster", DomElement.IdentificationType.BY_LINK_TEXT);
    }

    public static DomElement varumarkenLink(){
        return new DomElement("Varumärken", DomElement.IdentificationType.BY_LINK_TEXT);
    }

    public static DomElement patentsLink(){
        return new DomElement("Patent", DomElement.IdentificationType.BY_LINK_TEXT);
    }

    public static DomElement settingsLink(){
        return new DomElement("Inställningar", DomElement.IdentificationType.BY_LINK_TEXT);
    }

    public static DomElement contactAndSupportLink(){
        return new DomElement("Kontakt & Support", DomElement.IdentificationType.BY_LINK_TEXT);
    }

    public static DomElement logOutLink(){
        return new DomElement("Logga ut", DomElement.IdentificationType.BY_LINK_TEXT);
    }

    public static DomElement languageSelectorEnglish(){
        return new DomElement("English", DomElement.IdentificationType.BY_LINK_TEXT);
    }

    public static DomElement languageSelectorSwedish(){
        return new DomElement("Svenska", DomElement.IdentificationType.BY_LINK_TEXT);
    }

    public static DomElement dropDownChoiceSwedish(){
        return new DomElement("//*/li/*[.='Svenska']", DomElement.IdentificationType.BY_X_PATH);
    }

    public static DomElement loggedInPerson(){
        return new DomElement ("//p[contains(test(), 'Inloggad som ')]/*", DomElement.IdentificationType.BY_X_PATH);
    }

    public static DomElement minaVarumarkenHeadline(){
        return new DomElement("//trademarks/h3[contains(text(),'Mina varumärken')]", DomElement.IdentificationType.BY_X_PATH);
    }
}
