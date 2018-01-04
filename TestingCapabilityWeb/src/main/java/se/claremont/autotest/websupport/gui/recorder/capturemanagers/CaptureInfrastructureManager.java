package se.claremont.autotest.websupport.gui.recorder.capturemanagers;

import se.claremont.autotest.websupport.gui.recorder.captureinfrastructure.Javascripts;

import java.util.ArrayList;
import java.util.List;

public class CaptureInfrastructureManager {

    public static boolean hasBeenRun = false;

    public String script = postHttpRequest;

    public static List<String> appliedListeners = new ArrayList<>();

    public CaptureInfrastructureManager(){
        hasBeenRun = true;
    }

    private static String elementXpathExtractor =
            "function createXPathFromElement(elm) { \n" +
                    "    var allNodes = document.getElementsByTagName('*'); \n" +
                    "    for (var segs = []; elm && elm.nodeType == 1; elm = elm.parentNode) \n" +
                    "    { \n" +
                    "        if (elm.hasAttribute('id')) { \n" +
                    "                var uniqueIdCount = 0; \n" +
                    "                for (var n=0;n < allNodes.length;n++) { \n" +
                    "                    if (allNodes[n].hasAttribute('id') && allNodes[n].id == elm.id) uniqueIdCount++; \n" +
                    "                    if (uniqueIdCount > 1) break; \n" +
                    "                }; \n" +
                    "                if ( uniqueIdCount == 1) { \n" +
                    "                    segs.unshift('id(\"' + elm.getAttribute('id') + '\")'); \n" +
                    "                    return segs.join('/'); \n" +
                    "                } else { \n" +
                    "                    segs.unshift(elm.localName.toLowerCase() + '[@id=\"' + elm.getAttribute('id') + '\"]'); \n" +
                    "                } \n" +
                    "        } else if (elm.hasAttribute('class')) { \n" +
                    "            segs.unshift(elm.localName.toLowerCase() + '[@class=\"' + elm.getAttribute('class') + '\"]'); \n" +
                    "        } else { \n" +
                    "            for (i = 1, sib = elm.previousSibling; sib; sib = sib.previousSibling) { \n" +
                    "                if (sib.localName == elm.localName)  i++; }; \n" +
                    "                segs.unshift(elm.localName.toLowerCase() + '[' + i + ']'); \n" +
                    "        }; \n" +
                    "    }; \n" +
                    "    return segs.length ? '/' + segs.join('/') : null; \n" +
                    "};\n\n";

    private static String postHttpRequest =
            elementXpathExtractor +
                    "function post(path, params, method) {\n" +
                    "    method = method || \"post\"; // Set method to post by default if not specified.\n" +
                    "\n" +
                    "    // The rest of this code assumes you are not using a library.\n" +
                    "    // It can be made less wordy if you use one.\n" +
                    "    var form = document.createElement(\"form\");\n" +
                    "    form.setAttribute(\"method\", method);\n" +
                    "    form.setAttribute(\"action\", path);\n" +
                    "\n" +
                    "    for(var key in params) {\n" +
                    "        if(params.hasOwnProperty(key)) {\n" +
                    "            var hiddenField = document.createElement(\"input\");\n" +
                    "            hiddenField.setAttribute(\"type\", \"hidden\");\n" +
                    "            hiddenField.setAttribute(\"name\", key);\n" +
                    "            hiddenField.setAttribute(\"value\", params[key]);\n" +
                    "\n" +
                    "            form.appendChild(hiddenField);\n" +
                    "        }\n" +
                    "    }\n" +
                    "\n" +
                    "    document.body.appendChild(form);\n" +
                    "    form.submit();\n" +
                    "}\n\n";

}
