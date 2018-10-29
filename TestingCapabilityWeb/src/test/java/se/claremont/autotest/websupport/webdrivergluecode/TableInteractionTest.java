package se.claremont.autotest.websupport.webdrivergluecode;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import se.claremont.autotest.common.support.tableverification.CellMatchingType;
import se.claremont.autotest.common.testcase.TestCase;
import se.claremont.autotest.common.testcase.TestCaseResult;
import se.claremont.autotest.common.testset.UnitTestClass;
import se.claremont.autotest.websupport.DomElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests interaction with HTML tables
 *
 * Created by jordam on 2017-01-28.
 */
public class TableInteractionTest extends UnitTestClass {

    @Test
    /*
      This test case tries reading from a table that at first is not displayed.
     */
    public void delayedTableShouldBeWaitedFor(){
        HtmlUnitDriver driver = new HtmlUnitDriver();
        TestCase testCase = new TestCase();
        WebInteractionMethods web = new WebInteractionMethods(testCase, driver);
        web.navigate("file://" + TestHelper.getTestFileFromTestResourcesFolder("delayTest.html"));
        DomElement table = new DomElement("table", DomElement.IdentificationType.BY_ID);
        List<String> headlines = new ArrayList<>();
        headlines.add("Headline2");
        headlines.add("Headline1");
        web.verifiyElement(table).tableHeadlines(headlines);
        web.verifiyElement(table).tableHeadline("Headline1");
        web.verifiyElement(table).tableHeadline("Headline2");
        web.verifiyElement(table).tableRow("Headline1:Row 2 headline1", CellMatchingType.EXACT_MATCH);
        web.makeSureDriverIsClosed();
        testCase.testCaseResult.evaluateResultStatus();
        Assert.assertTrue(testCase.testCaseResult.resultStatus.equals(TestCaseResult.ResultStatus.PASSED));
    }

    @Test
    public void correctHtmlShouldGiveCorrectResults(){
        HtmlUnitDriver driver = new HtmlUnitDriver();
        TestCase testCase = new TestCase();
        WebInteractionMethods web = new WebInteractionMethods(testCase, driver);
        web.navigate("file://" + TestHelper.getTestFileFromTestResourcesFolder("tableTest.html"));
        DomElement table = new DomElement("correctHtml", DomElement.IdentificationType.BY_ID);
        List<String> headlines = new ArrayList<>();
        headlines.add("Heading1");
        headlines.add("Heading2");
        web.verifiyElement(table).tableHeadlines(headlines);
        web.verifiyElement(table).tableHeadline("Heading1");
        web.verifiyElement(table).tableHeadline("Heading2");
        web.verifiyElement(table).tableRow("Heading1:Row2Heading1", CellMatchingType.EXACT_MATCH);
        web.makeSureDriverIsClosed();
        testCase.testCaseResult.evaluateResultStatus();
        Assert.assertTrue(testCase.testCaseResult.resultStatus.equals(TestCaseResult.ResultStatus.PASSED));
    }

    @Test
    public void toManyCellsInSomeRowShouldWork(){
        HtmlUnitDriver driver = new HtmlUnitDriver();
        TestCase testCase = new TestCase();
        WebInteractionMethods web = new WebInteractionMethods(testCase, driver);
        web.navigate("file://" + TestHelper.getTestFileFromTestResourcesFolder("tableTest.html"));
        DomElement table = new DomElement("toManyCellsInOneRowHtml", DomElement.IdentificationType.BY_ID);
        List<String> headlines = new ArrayList<>();
        headlines.add("Heading1");
        headlines.add("Heading2");
        web.verifiyElement(table).tableHeadlines(headlines);
        web.verifiyElement(table).tableHeadline("Heading1");
        web.verifiyElement(table).tableHeadline("Heading2");
        web.verifiyElement(table).tableRow("Heading1:Row2Heading1", CellMatchingType.EXACT_MATCH);
        web.makeSureDriverIsClosed();
        testCase.testCaseResult.evaluateResultStatus();
        Assert.assertTrue(testCase.testCaseResult.resultStatus.equals(TestCaseResult.ResultStatus.PASSED));
    }

    @Test
    public void toManyHeadlines(){
        HtmlUnitDriver driver = new HtmlUnitDriver();
        TestCase testCase = new TestCase();
        WebInteractionMethods web = new WebInteractionMethods(testCase, driver);
        web.navigate("file://" + TestHelper.getTestFileFromTestResourcesFolder("tableTest.html"));
        DomElement table = new DomElement("toManyHeadlines", DomElement.IdentificationType.BY_ID);
        List<String> headlines = new ArrayList<>();
        headlines.add("Heading1");
        headlines.add("Heading2");
        web.verifiyElement(table).tableHeadlines(headlines);
        web.verifiyElement(table).tableHeadline("Heading1");
        web.verifiyElement(table).tableHeadline("Heading2");
        web.verifiyElement(table).tableRow("Heading1:Row2Heading1", CellMatchingType.EXACT_MATCH);
        web.makeSureDriverIsClosed();
        testCase.testCaseResult.evaluateResultStatus();
        Assert.assertTrue(testCase.testCaseResult.resultStatus.equals(TestCaseResult.ResultStatus.PASSED));
    }

    @Test
    public void colSpanTest(){
        HtmlUnitDriver driver = new HtmlUnitDriver();
        TestCase testCase = new TestCase();
        WebInteractionMethods web = new WebInteractionMethods(testCase, driver);
        web.navigate("file://" + TestHelper.getTestFileFromTestResourcesFolder("tableTest.html"));
        DomElement table = new DomElement("colSpanTest", DomElement.IdentificationType.BY_ID);
        List<String> headlines = new ArrayList<>();
        headlines.add("Heading1");
        headlines.add("Heading2");
        web.verifiyElement(table).tableHeadlines(headlines);
        web.verifiyElement(table).tableHeadline("Heading1");
        web.verifiyElement(table).tableHeadline("Heading2");
        web.verifiyElement(table).tableRow("Heading1:Row2Heading1", CellMatchingType.EXACT_MATCH);
        web.makeSureDriverIsClosed();
        testCase.testCaseResult.evaluateResultStatus();
        Assert.assertTrue(testCase.testCaseResult.resultStatus.equals(TestCaseResult.ResultStatus.PASSED));
    }

    @Test
    public void multipleThRowsTest(){
        HtmlUnitDriver driver = new HtmlUnitDriver();
        TestCase testCase = new TestCase();
        WebInteractionMethods web = new WebInteractionMethods(testCase, driver);
        web.navigate("file://" + TestHelper.getTestFileFromTestResourcesFolder("tableTest.html"));
        DomElement table = new DomElement("multipleThRowsTest", DomElement.IdentificationType.BY_ID);
        List<String> headlines = new ArrayList<>();
        headlines.add("Heading1");
        headlines.add("Heading2");
        web.verifiyElement(table).tableHeadlines(headlines);
        web.verifiyElement(table).tableHeadline( "Heading1");
        web.verifiyElement(table).tableHeadline("Heading2");
        web.verifiyElement(table).tableRow("Heading1:Row2Heading1", CellMatchingType.EXACT_MATCH);
        web.makeSureDriverIsClosed();
        testCase.testCaseResult.evaluateResultStatus();
        Assert.assertTrue(testCase.testCaseResult.resultStatus.equals(TestCaseResult.ResultStatus.PASSED));
    }

    @Test
    public void noThRow(){
        HtmlUnitDriver driver = new HtmlUnitDriver();
        TestCase testCase = new TestCase();
        WebInteractionMethods web = new WebInteractionMethods(testCase, driver);
        web.navigate("file://" + TestHelper.getTestFileFromTestResourcesFolder("tableTest.html"));
        DomElement table = new DomElement("noThRow", DomElement.IdentificationType.BY_ID);
        List<String> headlines = new ArrayList<>();
        headlines.add("Heading1");
        headlines.add("Heading2");
        web.verifiyElement(table).tableHeadlines(headlines);
        web.verifiyElement(table).tableHeadline("Heading1");
        web.verifiyElement(table).tableHeadline("Heading2");
        web.verifiyElement(table).tableRow("Heading1:Row2Heading1", CellMatchingType.EXACT_MATCH);
        web.makeSureDriverIsClosed();
        testCase.testCaseResult.evaluateResultStatus();
        Assert.assertTrue(testCase.testCaseResult.resultStatus.equals(TestCaseResult.ResultStatus.PASSED));
    }

    @Test
    public void trimmedSpacesTestInCellsAndMultipleSearchesToFulfillRowVerification(){
        HtmlUnitDriver driver = new HtmlUnitDriver();
        TestCase testCase = new TestCase();
        WebInteractionMethods web = new WebInteractionMethods(testCase, driver);
        web.navigate("file://" + TestHelper.getTestFileFromTestResourcesFolder("tableTest.html"));
        DomElement table = new DomElement("trimmedSpacesTest", DomElement.IdentificationType.BY_ID);
        List<String> headlines = new ArrayList<>();
        headlines.add("Heading1");
        headlines.add("Heading3");
        headlines.add("Heading2");
        web.verifiyElement(table).tableHeadlines(headlines);
        web.verifiyElement(table).tableHeadline("Heading1");
        web.verifiyElement(table).tableHeadline("Heading2");
        web.verifiyElement(table).tableHeadline("Heading3");
        web.verifiyElement(table).tableRow("Heading1:Row2Heading1", CellMatchingType.EXACT_MATCH);
        web.verifiyElement(table).tableRow("Heading2:Row3Heading2;Heading3:Row3Heading3", CellMatchingType.EXACT_MATCH);
        web.verifiyElement(table).tableRow("Heading1:Row3Heading1;Heading2:Row3Heading2;Heading3:Row3Heading3", CellMatchingType.EXACT_MATCH);
        web.verifiyElement(table).tableRow( "Heading2:Row3Heading2;Heading1:Row3Heading1;Heading3:Row3Heading3", CellMatchingType.EXACT_MATCH);
        web.makeSureDriverIsClosed();
        testCase.testCaseResult.evaluateResultStatus();
        Assert.assertTrue(testCase.testCaseResult.resultStatus.equals(TestCaseResult.ResultStatus.PASSED));
    }
}
