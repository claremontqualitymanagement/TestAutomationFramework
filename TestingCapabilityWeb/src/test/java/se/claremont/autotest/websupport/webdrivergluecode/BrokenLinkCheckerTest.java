package se.claremont.autotest.websupport.webdrivergluecode;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import se.claremont.autotest.common.testcase.TestCase;
import se.claremont.autotest.common.testset.UnitTestClass;

import static se.claremont.autotest.common.testcase.TestCase.ResultStatus.FAILED_WITH_ONLY_NEW_ERRORS;
import static se.claremont.autotest.common.testcase.TestCase.ResultStatus.PASSED;

/**
 * Test class for checkning broken links
 *
 * Created by jordam on 2017-03-31.
 */
public class BrokenLinkCheckerTest extends UnitTestClass {
    TestCase testCase;
    TestActionsForBrokenLinkChecker testActionsForBrokenLinkChecker = new TestActionsForBrokenLinkChecker();
    WebInteractionMethods web;

    @Before
    public void setup(){
        testCase = new TestCase();
        HtmlUnitDriver driver = new HtmlUnitDriver();
        driver.setJavascriptEnabled(true);
        web = new WebInteractionMethods(testCase, driver);
    }

    @After
    public void teardown(){
        web.makeSureDriverIsClosed();
    }

    @Test
    public void linkReportsShouldBeProducedNegative(){
        web.navigate("file://" + TestHelper.getTestFileFromTestResourcesFolder("brokenLinkCheckTest.html"));
        web.reportBrokenLinksOnCurrentPage();
        testCase.evaluateResultStatus();
        Assert.assertTrue(testCase.resultStatus.equals(FAILED_WITH_ONLY_NEW_ERRORS));
    }

    @Test
    public void linkReportsShouldBeProducedPositive(){
        web.navigate("file://" + TestHelper.getTestFileFromTestResourcesFolder("brokenLinkCheckTestPositive.html"));
        web.reportBrokenLinksOnCurrentPage();
        testCase.evaluateResultStatus();
        Assert.assertTrue(testCase.resultStatus.equals(PASSED));
    }

    @Test
    public void linkReportsShouldBeProducedWithHiddenLinks(){
        web.navigate("file://" + TestHelper.getTestFileFromTestResourcesFolder("brokenLinkCheckTest.html"));
        web.reportBrokenLinksOnCurrentPage_IncludeAllLinksAlsoNonDisplayedLinks();
        testCase.evaluateResultStatus();
        Assert.assertTrue(testCase.resultStatus.equals(FAILED_WITH_ONLY_NEW_ERRORS));
    }

}
