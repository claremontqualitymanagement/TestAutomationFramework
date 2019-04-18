package se.claremont.taf.websupport.gui.recorder.capturemanagers;

import se.claremont.taf.websupport.gui.recorder.captureinfrastructure.restserver.HttpServer;
import se.claremont.taf.websupport.gui.recorder.captureinfrastructure.restserver.Settings;

/**
 * Captures click events on page elements.
 */
public class ClickCaptureManager {
    public String script = registerDocumentClickListener;

    public ClickCaptureManager(){
        if(!CaptureInfrastructureManager.hasBeenRun)
            script = new CaptureInfrastructureManager().script + script;
    }

    private static String registerDocumentClickListener =
            "document.addEventListener('click', function(e) {\n" +
                    "        e = e || window.event;\n" +
                    "        var target = e.target || e.srcElement,\n" +
                    "        text = target.textContent || target.innerText;\n" +
                    "        var elementDescription = { };\n" +
                    "        elementDescription.text = text;\n" +
                    "        elementDescription.className = target.getAttribute(\"class\");\n" +
                    "        elementDescription.tagName = target.tagName;\n" +
                    "        elementDescription.id = target.id;\n" +
                    "        elementDescription.xpath = createXPathFromElement(target);\n" +
                    "        post('http://" + HttpServer.getIPAddressesOfLocalMachine() + ":" + Settings.port + "/tafwebrecorder/v1/click', {webElement: JSON.stringify(elementDescription)} ); \n" +
                    "     }, false);\n\n";

}
