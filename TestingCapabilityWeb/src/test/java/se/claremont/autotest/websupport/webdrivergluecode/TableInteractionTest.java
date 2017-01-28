package se.claremont.autotest.websupport.webdrivergluecode;

import org.junit.Assume;
import org.junit.Ignore;
import org.junit.Test;
import se.claremont.autotest.common.testcase.TestCase;
import se.claremont.autotest.websupport.DomElement;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jordam on 2017-01-28.
 */
public class TableInteractionTest {

    private String getTestFileFromTestResourcesFolder(String fileName){
        URL url = Thread.currentThread().getContextClassLoader().getResource(fileName);
        File file = new File(url.getPath());
        Assume.assumeNotNull(file);
        return file.getAbsolutePath();
    }

    @Test
    @Ignore
    /*
      This test case tries reading from a table that at first is not displayed.
     */
    public void delayedTableShouldBeWaitedFor(){
        TestCase testCase = new TestCase(null, "dummy");
        WebInteractionMethods web = new WebInteractionMethods(testCase);
        web.navigate("file://" + getTestFileFromTestResourcesFolder("delayTest.html"));
        DomElement table = new DomElement("table", DomElement.IdentificationType.BY_ID);
        List<String> headlines = new ArrayList<>();
        headlines.add("Headline2");
        headlines.add("Headline1");
        web.verifyTableHeadlines(table, headlines);
        web.verifyTableHeadline(table, "Headline1");
        web.verifyTableHeadline(table, "Headline2");
        web.verifyTableRow(table, "Headline1:Row2 headline1", false);
        web.makeSureDriverIsClosed();
    }

}
