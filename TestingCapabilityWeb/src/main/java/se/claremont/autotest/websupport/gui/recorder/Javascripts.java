package se.claremont.autotest.websupport.gui.recorder;

import se.claremont.autotest.websupport.gui.recorder.restserver.HttpServer;
import se.claremont.autotest.websupport.gui.recorder.restserver.Settings;

public class Javascripts {

    static String registerDocumentClickListener = "document.addEventListener('click', function(e) {\n" +
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

/*
    static String elementChangeListener = "(function (window) {\n" +
            "    var last = +new Date();\n" +
            "    var delay = 100; // default delay\n" +
            "\n" +
            "    // Manage event queue\n" +
            "    var stack = [];\n" +
            "\n" +
            "    function callback() {\n" +
            "        var now = +new Date();\n" +
            "        if (now - last > delay) {\n" +
            "            for (var i = 0; i < stack.length; i++) {\n" +
            "                stack[i]();\n" +
            "            }\n" +
            "            last = now;\n" +
            "        }\n" +
            "    }\n" +
            "\n" +
            "    // Public interface\n" +
            "    var onDomChange = function (fn, newdelay) {\n" +
            "        if (newdelay) delay = newdelay;\n" +
            "        stack.push(fn);\n" +
            "    };\n" +
            "\n" +
            "    // Naive approach for compatibility\n" +
            "    function naive() {\n" +
            "\n" +
            "        var last = document.getElementsByTagName('*');\n" +
            "        var lastlen = last.length;\n" +
            "        var timer = setTimeout(function check() {\n" +
            "\n" +
            "            // get current state of the document\n" +
            "            var current = document.getElementsByTagName('*');\n" +
            "            var len = current.length;\n" +
            "\n" +
            "            // if the length is different\n" +
            "            // it's fairly obvious\n" +
            "            if (len != lastlen) {\n" +
            "                // just make sure the loop finishes early\n" +
            "                last = [];\n" +
            "            }\n" +
            "\n" +
            "            // go check every element in order\n" +
            "            for (var i = 0; i < len; i++) {\n" +
            "                if (current[i] !== last[i]) {\n" +
            "                    callback();\n" +
            "                    last = current;\n" +
            "                    lastlen = len;\n" +
            "                    break;\n" +
            "                }\n" +
            "            }\n" +
            "\n" +
            "            // over, and over, and over again\n" +
            "            setTimeout(check, delay);\n" +
            "\n" +
            "        }, delay);\n" +
            "    }\n" +
            "\n" +
            "    //\n" +
            "    //  Check for mutation events support\n" +
            "    //\n" +
            "\n" +
            "    var support = {};\n" +
            "\n" +
            "    var el = document.documentElement;\n" +
            "    var remain = 3;\n" +
            "\n" +
            "    // callback for the tests\n" +
            "    function decide() {\n" +
            "        if (support.DOMNodeInserted) {\n" +
            "            window.addEventListener(\"DOMContentLoaded\", function () {\n" +
            "                if (support.DOMSubtreeModified) { // for FF 3+, Chrome\n" +
            "                    el.addEventListener('DOMSubtreeModified', callback, false);\n" +
            "                } else { // for FF 2, Safari, Opera 9.6+\n" +
            "                    el.addEventListener('DOMNodeInserted', callback, false);\n" +
            "                    el.addEventListener('DOMNodeRemoved', callback, false);\n" +
            "                }\n" +
            "            }, false);\n" +
            "        } else if (document.onpropertychange) { // for IE 5.5+\n" +
            "            document.onpropertychange = callback;\n" +
            "        } else { // fallback\n" +
            "            naive();\n" +
            "        }\n" +
            "    }\n" +
            "\n" +
            "    // checks a particular event\n" +
            "    function test(event) {\n" +
            "        el.addEventListener(event, function fn() {\n" +
            "            support[event] = true;\n" +
            "            el.removeEventListener(event, fn, false);\n" +
            "            if (--remain === 0) decide();\n" +
            "        }, false);\n" +
            "    }\n" +
            "\n" +
            "    // attach test events\n" +
            "    if (window.addEventListener) {\n" +
            "        test('DOMSubtreeModified');\n" +
            "        test('DOMNodeInserted');\n" +
            "        test('DOMNodeRemoved');\n" +
            "    } else {\n" +
            "        decide();\n" +
            "    }\n" +
            "\n" +
            "    // do the dummy test\n" +
            "    var dummy = document.createElement(\"div\");\n" +
            "    el.appendChild(dummy);\n" +
            "    el.removeChild(dummy);\n" +
            "\n" +
            "    // expose\n" +
            "    window.onDomChange = onDomChange;\n" +
            "})(window);\n\n" +
            "onDomChange(function(){ \n" +
            "    alert(\"The Times They Are a-Changin'\");\n" +
            "});\n\n";

    */

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

    /*
    static String postHttpRequest = "function post(path, param) {\n" +
            "        var form = document.createElement(\"form\");\n" +
            "        form.setAttribute(\"method\", \"post\");\n" +
            "        form.setAttribute(\"action\", path);\n" +
            "        form.setAttribute(\"target\", \"_blank\");\n" +
            "        var hiddenField = document.createElement(\"input\");\n" +
            "        hiddenField.setAttribute(\"name\", \"search\");\n" +
            "        hiddenField.setAttribute(\"value\", JSON.stringify(param) );\n" +
            "        hiddenField.setAttribute(\"type\", \"hidden\");\n" +
            "        form.appendChild(hiddenField);\n" +
            "        document.body.appendChild(form);\n" +
            "        form.submit();}";
*/
}

