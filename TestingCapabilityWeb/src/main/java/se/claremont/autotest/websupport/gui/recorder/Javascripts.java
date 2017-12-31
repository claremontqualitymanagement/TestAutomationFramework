package se.claremont.autotest.websupport.gui.recorder;

import se.claremont.autotest.websupport.gui.recorder.restserver.HttpServer;
import se.claremont.autotest.websupport.gui.recorder.restserver.Settings;

public class Javascripts {

    static String registerDocumentClickListener = "document.addEventListener('click', function(e) {\n" +
            "        e = e || window.event;\n" +
            "        var target = e.target || e.srcElement,\n" +
            "        text = target.textContent || target.innerText;\n" +
            "        post('http://" + HttpServer.getIPAddressesOfLocalMachine() + ":" + Settings.port + "/tafwebrecorder/v1/click', text, 'post'); \n" +
            "        }, false);";

    static String domElementToString = "function domElementToString(target){\n" +
            "        var text = target.textContent || target.innerText;\n" +
            "        var tagName = target.tagName;\n" +
            "        var className = target.getAttribute('class');\n" +
            "        return {text: \"' + text + '\", tag: \"' + tagName + '\", class: \"' + className + '\"};";

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
}
