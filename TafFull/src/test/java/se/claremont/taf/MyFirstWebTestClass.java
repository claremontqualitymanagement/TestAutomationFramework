package se.claremont.taf;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import se.claremont.taf.testset.TestSet;
import se.claremont.taf.websupport.webdrivergluecode.WebInteractionMethods;

public class MyFirstWebTestClass extends TestSet {

    private WebInteractionMethods web;

    @Before
    public void testSetup(){
        web = new WebInteractionMethods(currentTestCase());
    }

    @After
    public void testTearDown(){
        if(web == null)return;
        web.makeSureDriverIsClosed();
    }

    @Test
    public void myFirstTest(){
        web.navigate("https://github.com/claremontqualitymanagement/TestAutomationFramework/");
        web.verify().title(".*TAF.*", StringComparisonType.Regex);
    }

}
