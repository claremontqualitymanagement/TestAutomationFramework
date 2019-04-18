package se.claremont.taf.websupport.gui.recorder.capturemanagers;

import se.claremont.taf.websupport.gui.recorder.captureinfrastructure.restserver.HttpServer;
import se.claremont.taf.websupport.gui.recorder.captureinfrastructure.restserver.Settings;

/**
 * Document wide input listener
 */
public class InputCaptureManager {
    public String script = registerDocumentInputListener;

    public InputCaptureManager(){
        if(!CaptureInfrastructureManager.hasBeenRun)
            script = new CaptureInfrastructureManager().script + script;
    }

    private static String registerDocumentInputListener =
            "document.addEventListener('input', function(e) {\n" +
                    "        e = e || window.event;\n" +
                    "        var target = e.target || e.srcElement,\n" +
                    "        text = target.value;\n" +
                    "        var elementDescription = { };\n" +
                    "        elementDescription.text = '';\n" +
                    "        elementDescription.className = target.getAttribute(\"class\");\n" +
                    "        elementDescription.tagName = target.tagName;\n" +
                    "        elementDescription.id = target.id;\n" +
                    "        elementDescription.xpath = createXPathFromElement(target);\n" +
                    "        post('http://" + HttpServer.getIPAddressesOfLocalMachine() + ":" + Settings.port + "/tafwebrecorder/v1/input', {webElement: JSON.stringify(elementDescription)} ); \n" +
                    "     }, false);\n\n";
}
