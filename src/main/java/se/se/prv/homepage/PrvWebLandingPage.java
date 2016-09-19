package se.se.prv.homepage;

import se.claremont.autotest.guidriverpluginstructure.websupport.DomElement;
import se.claremont.autotest.guidriverpluginstructure.websupport.WebPage;

/**
 * Declaration of GUI elements on the PRV landing page (http://www.prv.se)
 *
 * Created by jordam on 2016-08-17.
 */

class PrvWebLandingPage extends WebPage{

    static DomElement buttonVaraTjanster(){
      return new DomElement("Våra tjänster", DomElement.IdentificationType.BY_LINK_TEXT);
    }

    static DomElement Button_Search(){
        return new DomElement("search-button", DomElement.IdentificationType.BY_ID);
    }

    static DomElement Input_SearchText(){
        return new DomElement("quickSearch", DomElement.IdentificationType.BY_ID);
    }
}
