package se.claremont.autotest.websupport.gui.recorder;

import se.claremont.autotest.websupport.gui.recorder.restserver.HttpServer;
import se.claremont.autotest.websupport.gui.recorder.restserver.Settings;

public class Javascripts {

    static String registerDocumentClickListener = "document.addEventListener('click', function(e) {\n" +
            "        e = e || window.event;\n" +
            "        var target = e.target || e.srcElement,\n" +
            "        text = target.textContent || target.innerText;\n" +
            "        post('http://" + HttpServer.getIPAddressesOfLocalMachine() + ":" + Settings.port + "/tafwebrecorder/v1/click', " +
            "           text, " +
            "           'post'); \n" +
            "        }, false);";

    static String domElementToString = "function domElementToString(target){\n" +
            "        var text = target.textContent || target.innerText;\n" +
            "        var tagName = target.tagName;\n" +
            "        var className = target.getAttribute('class');\n" +
            "        return {text: \"' + text + '\", tag: \"' + tagName + '\", class: \"' + className + '\"};";

    static String postHttpRequest = "function post(path, params, method) {\n" +
            "    method = method || \"post\"; // Set method to post by default if not specified.\n" +
            "\n" +
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
            "}";
}
