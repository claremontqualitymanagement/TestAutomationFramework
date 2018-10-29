package se.claremont.autotest.websupport;

import se.claremont.autotest.common.guidriverpluginstructure.GuiElement;
import se.claremont.autotest.websupport.webdrivergluecode.BrowserVerificationMethods;
import se.claremont.autotest.websupport.webdrivergluecode.ElementVerificationMethods;
import se.claremont.autotest.websupport.webdrivergluecode.WebInteractionMethods;

public class ActionResult {

    public boolean wasSuccess;
    private GuiElement domElement;
    private WebInteractionMethods web;

    public ActionResult(boolean status, GuiElement usedElement, WebInteractionMethods web){
        this.wasSuccess = status;
        this.domElement = usedElement;
        this.web = web;
    }

    public BrowserVerificationMethods verify(){
        if(domElement == null) return new BrowserVerificationMethods(web);
        return new ElementVerificationMethods(domElement, web);
    }

    public WebInteractionMethods then(){
        return web;
    }
}
