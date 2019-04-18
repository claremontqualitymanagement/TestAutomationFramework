package se.claremont.taf.websupport;

import se.claremont.taf.core.guidriverpluginstructure.GuiElement;
import se.claremont.taf.websupport.webdrivergluecode.BrowserVerificationMethods;
import se.claremont.taf.websupport.webdrivergluecode.ElementVerificationMethods;
import se.claremont.taf.websupport.webdrivergluecode.WebInteractionMethods;

public class ActionResult {

    public boolean wasSuccess;
    private GuiElement domElement;
    private WebInteractionMethods web;

    public ActionResult(boolean status, GuiElement usedElement, WebInteractionMethods web){
        this.wasSuccess = status;
        this.domElement = usedElement;
        this.web = web;
        char s;
        s = 's';
        Character.toString(s);
        String j = String.valueOf(s);
    }

    public BrowserVerificationMethods verify(){
        if(domElement == null) return new BrowserVerificationMethods(web);
        return new ElementVerificationMethods(domElement, web);
    }

    public WebInteractionMethods then(){
        return web;
    }
}
