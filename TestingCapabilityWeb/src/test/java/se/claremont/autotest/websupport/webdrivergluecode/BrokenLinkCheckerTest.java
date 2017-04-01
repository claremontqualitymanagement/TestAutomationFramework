package se.claremont.autotest.websupport.webdrivergluecode;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import se.claremont.autotest.common.testset.TestSet;

/**
 * Test class for checkning broken links
 *
 * Created by jordam on 2017-03-31.
 */
public class BrokenLinkCheckerTest extends TestSet {

    TestActionsForBrokenLinkChecker testActionsForBrokenLinkChecker = new TestActionsForBrokenLinkChecker();
    WebInteractionMethods web;

    @Before
    public void setup(){
        web = new WebInteractionMethods(currentTestCase);
    }

    @After
    public void teardown(){
        web.makeSureDriverIsClosed();
    }

    @Test
    @Ignore //Needs browser and internet connection
    public void testClaremontLandingPage(){
        web.navigate("http://www.claremont.se");
        testActionsForBrokenLinkChecker.checkBrokenLinks(web);
    }

}
