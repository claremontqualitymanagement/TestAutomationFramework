package se.claremont.autotest.websupport.gui.recorder.capturemanagers;

import se.claremont.autotest.websupport.gui.recorder.captureinfrastructure.Javascripts;
import se.claremont.autotest.websupport.gui.recorder.captureinfrastructure.restserver.HttpServer;
import se.claremont.autotest.websupport.gui.recorder.captureinfrastructure.restserver.Settings;

/**
 * Makes notice if DOM nodes with the tag INPUT has attribute changes. Also adds relevant listeners to page DOM elements added at runtime.
 */
public class DomChangeCaptureManager {

    public DomChangeCaptureManager(){
        if(!CaptureInfrastructureManager.hasBeenRun)
            script = new CaptureInfrastructureManager().script + script;
    }

    public String script = domChangeListener;

    private static String domChangeListener =
            "var observer = new MutationObserver(function(mutations) {\n" +
                    "        mutations.forEach(function(mutation) {\n" +
                    "            for(var j=0; j<mutation.addedNodes.length; ++j) {\n" +
                    "                stateChangedCheckRegistration(mutation.addedNodes[j]);\n" +
                    "                addTafElementListener(mutation.addedNodes[j]);\n" +
                    "            }\n" +
                    "            if(mutation.type != 'attributes') continue;\n" +
                    "            if(mutation.target.tagName != 'input') continue;\n" +
                    "            var mutationDescription = { };\n" +
                    "            mutationDescription.type = mutation.type;\n" +
                    "            mutationDescription.targetTagName = mutation.target.tagName;\n" +
                    "            mutationDescription.targetClassName = mutation.target.getAttribute(\"class\");\n" +
                    "            mutationDescription.targetId = mutation.target.id;\n" +
                    "            mutationDescription.targetXpath = createXPathFromElement(mutation.target);\n" +
                    "            mutationDescription.attributeName = mutation.attributeName;\n" +
                    "            mutationDescription.oldValue = mutation.oldValue;\n" +
                    "            mutationDescription.newValue = mutation.target.getAttribute(mutation.attributeName);\n" +
                    "            mutationDescription.addedNodes = mutation.addedNodes;\n" +
                    "            mutationDescription.removedNodes = mutation.removedNodes;\n" +
                    "            post('http://" + HttpServer.getIPAddressesOfLocalMachine() + ":" + Settings.port + "/tafwebrecorder/v1/domchange', {mutation: JSON.stringify(mutationDescription)} ); \n" +
                    //"            console.log(mutation.type);\n" +
                    "        });\n" +
                    "});\n\n" +
                    "observer.observe(document.body, {\n" +
                    "        'subtree': true,\n" +
                    "        'attributes': true,\n" +
                    "        'attributeOldValue': true,\n" +
                    "        'characterDataOldValue': true,\n" +
                    "        'attributeFilter': ['value']\n" +
                    "});\n\n";;

}
