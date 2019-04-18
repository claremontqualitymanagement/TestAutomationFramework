package se.claremont.taf.websupport.gui.recorder.captureinfrastructure;


public class Javascripts {

    public static String textChangedOrCheckboxClickedElementListener =
            "function onInvocation(text){\n"+
            "   console.log(text);\n" +
            "}\n" +
            "\n" +
            "function changeEventTriggered(elm){\n" +
            "   if(elm == null)return;\n" +
            "   if(elm.tagName == 'input' && elm.getAttribute('name') == 'checkbox'){\n" +
            "      if(elm.checked){\n" +
            "         console.log('checked: ' + elm.tagName);\n"  +
            "      } else {\n" +
            "         console.log('unchecked: ' + elm.tagName);\n" +
            "      }\n" +
            "   }\n" +
            "}\n\n" +
            "function stateChangeCheck(elm, text){\n" +
            //"   console.log('initial text ' + text);\n" +
            "   var textWhenLeaving = elm.textContent || elm.innerText;\n" +
            //"   console.log('new text', textWhenLeaving);\n" +
            "   if(text !== textWhenLeaving){\n" +
            "      console.log('Text changed from ' + text + ' to ' + textWhenLeaving + '.');\n" +
            "   }\n" +
            //"   elm.removeEventListener(\"blur\", stateChangeCheck(elm, text));\n" +
            "}\n" +
            "\n" +
            "function stateChangedCheckRegistration(elm){\n" +
            "   var text = elm.textContent || elm.innerText;\n" +
            //"   console.log('initial text ' + text);" +
            "   elm.addEventListener(\"blur\", stateChangeCheck(elm, text));\n" +
            "}\n" +
            "\n" +
            "function addTafElementListener(elm){\n" +
            //"   elm.addEventListener(\"click\", onInvocation('clicked'));\n" +
            "   elm.addEventListener(\"dblclick\", onInvocation('double clicked'));\n" +
            "   elm.addEventListener(\"focus\", stateChangedCheckRegistration(elm));\n" +
            //"   elm.addEventListener(\"blur\", onInvocation('un-focused'));\n" +
            "   elm.addEventListener(\"change\", changeEventTriggered(elm));\n" +
            "   elm.addEventListener(\"select\", onInvocation('selected'));\n" +
            "   elm.addEventListener(\"submit\", onInvocation('submitted'));\n" +
            "}\n\n";

    public static String addListenersToAllDomElements =
            "function addTafListeners(){\n" +
            "   var all = document.getElementsByTagName(\"*\");\n" +
            "   for (var i=0, max=all.length; i < max; i++) {\n" +
            "     addTafElementListener(all[i]);\n" +
            "   }\n" +
            "}\n" +
            "addTafListeners();\n\n";

}

