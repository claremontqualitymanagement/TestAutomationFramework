package se.claremont.autotest.websupport.gui.recorder;

import se.claremont.autotest.websupport.gui.recorder.restserver.HttpServer;
import se.claremont.autotest.websupport.gui.recorder.restserver.Settings;

public class Javascripts {

    static String registerDocumentClickListener =
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


    static String domChangeListener =
            "var observer = new MutationObserver(function(mutations) {\n" +
            "        mutations.forEach(function(mutation) {\n" +
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
            "            console.log(mutation.type);\n" +
            "        });\n" +
            "});\n\n" +
            "observer.observe(document.body, {\n" +
            "        subtree: true,\n" +
            "        attributes: true,\n" +
            "        attributeOldValue: true,\n" +
            "        characterDataOldValue : true\n" +
            "});\n\n";;

    static String textChangedElementListener =
            "function onInvocation(text){\n"+
            "   console.log(text);\n" +
            "}\n" +
            "\n" +
            "function stateChangeCheck(elm, text){\n" +
            "   var textWhenLeaving = elm.textContent || elm.innerText;\n" +
            "   if(text != textWhenLeaving){\n" +
            "      console.log('Text changed from ' + text + ' to ' + textWhenLeaving + '.');\n" +
            "   }\n" +
            //"   elm.removeEventListener(\"blur\", stateChangeCheck(elm, text));\n" +
            "}\n" +
            "\n" +
            "function stateChangedCheckRegistration(elm){\n" +
            "   var text = elm.textContent || elm.innerText;\n" +
            "   elm.addEventListener(\"blur\", stateChangeCheck(elm, text));\n" +
            "}\n" +
            "\n" +
            "function addTafElementListener(elm){\n" +
            //"   elm.addEventListener(\"click\", onInvocation('clicked'));\n" +
            "   elm.addEventListener(\"dblclick\", onInvocation('double clicked'));\n" +
            "   elm.addEventListener(\"focus\", stateChangedCheckRegistration(elm));\n" +
            //"   elm.addEventListener(\"blur\", onInvocation('un-focused'));\n" +
            "   elm.addEventListener(\"select\", stateChangedCheckRegistration(elm));\n" +
            "   elm.addEventListener(\"submit\", stateChangedCheckRegistration(elm));\n" +
            "}\n\n";

    static String addListenersToAllDomElements =
            "function addTafListeners(){\n" +
            "   var all = document.getElementsByTagName(\"*\");\n" +
            "   for (var i=0, max=all.length; i < max; i++) {\n" +
            "     addTafElementListener(all[i]);\n" +
            "   }\n" +
            "}\n" +
            "addTafListeners();\n\n";

    static String elementXpathExtractor =
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

    static String postHttpRequest = "function post(path, params, method) {\n" +
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

