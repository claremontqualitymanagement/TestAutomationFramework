package se.claremont.autotest.websupport.gui.recorder;

import org.openqa.selenium.chrome.ChromeDriver;
import se.claremont.autotest.common.testcase.TestCase;
import se.claremont.autotest.websupport.webdrivergluecode.WebInteractionMethods;

public class RecorderWindow {

    public RecorderWindow(){
        WebInteractionMethods web = new WebInteractionMethods(new TestCase());
        web.navigate("http://claremont.se");
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        web.executeJavascript("document.addEventListener('click', function(e) {\n" +
                "        e = e || window.event;\n" +
                "        var target = e.target || e.srcElement,\n" +
                "        text = target.textContent || text.innerText;\n" +
                "        window.alert('Clicked element with text: ' + text);\n" +
                "        }, false);");
    }
}
