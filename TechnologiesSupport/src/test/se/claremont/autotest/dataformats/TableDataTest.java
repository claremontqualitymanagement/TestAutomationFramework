package se.claremont.autotest.dataformats;

import org.junit.Assert;
import org.junit.Test;
import se.claremont.autotest.common.logging.LogLevel;
import se.claremont.autotest.common.support.SupportMethods;
import se.claremont.autotest.common.testcase.TestCase;

/**
 * Created by jordam on 2016-11-09.
 */
public class TableDataTest {

    @Test
    public void emptyTableTest(){
        TestCase testCase = new TestCase(null, "dummy");
        String tableName = "UnitTestTable";
        String expectedHtml = "<table class=\"table padding\"><tr><td><table class=\"table data  INFO\"><tr class=\"table datarow  INFO\"><td class=\"table empty\" colspan=\"0\">Empty table</td></tr></table></td></tr></table>";
        String tableContent = "";
        TableData tableData = new TableData(tableContent, tableName, testCase, true);
        Assert.assertTrue("Table: " + tableData.toHtml(LogLevel.INFO), tableData.toHtml(LogLevel.INFO).equals(expectedHtml));
        Assert.assertTrue("Table: " + tableData.toString(), tableData.toString().equals(""));
    }

    @Test
    public void tableCreationTestWithTrailingLinebreak(){
        TestCase testCase = new TestCase(null, "dummy");
        String tableName = "UnitTestTable";
        String expectedHtml = "<table class=\"table padding\"><tr><td><table class=\"table data  INFO\"><tr class=\"table headline  INFO\"><td class=\"table headline soughtafter INFO\">Heading1</td><td class=\"table headline soughtafter INFO\">Heading2</td><td class=\"table headline soughtafter INFO\">Heading3</td></tr><tr class=\"table dataRow imperfectmatch  INFO\"><td class=\"table datacell matchedcellonrowbutnotthiscell  INFO\">DataValue1</td><td class=\"table datacell matchedcellonrowbutnotthiscell  INFO\">DataValue2</td><td class=\"table datacell matchedcellonrowbutnotthiscell  INFO\">DataValue3</td></tr></table></td></tr></table>";
        String tableContent = "Heading1;Heading2;Heading3" + SupportMethods.LF + "DataValue1;DataValue2;DataValue3" + SupportMethods.LF;
        String expectedString = "Heading1;Heading2;Heading3" + SupportMethods.LF +
                "DataValue1;DataValue2;DataValue3";
        TableData tableData = new TableData(tableContent, tableName, testCase, true);
        Assert.assertTrue("Table: " + tableData.toHtml(LogLevel.INFO), tableData.toHtml(LogLevel.INFO).equals(expectedHtml));
        Assert.assertTrue("Table: " + tableData.toString(), tableData.toString().equals(expectedString));
    }

    @Test
    public void tableCreationTestWithOutTrailingLinebreak(){
        TestCase testCase = new TestCase(null, "dummy");
        String tableName = "UnitTestTable";
        String expectedHtml = "<table class=\"table padding\"><tr><td><table class=\"table data  INFO\"><tr class=\"table headline  INFO\"><td class=\"table headline soughtafter INFO\">Heading1</td><td class=\"table headline soughtafter INFO\">Heading2</td><td class=\"table headline soughtafter INFO\">Heading3</td></tr><tr class=\"table dataRow imperfectmatch  INFO\"><td class=\"table datacell matchedcellonrowbutnotthiscell  INFO\">DataValue1</td><td class=\"table datacell matchedcellonrowbutnotthiscell  INFO\">DataValue2</td><td class=\"table datacell matchedcellonrowbutnotthiscell  INFO\">DataValue3</td></tr></table></td></tr></table>";
        String expectedString = "Heading1;Heading2;Heading3" + SupportMethods.LF + "DataValue1;DataValue2;DataValue3";
        String tableContent = "Heading1;Heading2;Heading3" + SupportMethods.LF + "DataValue1;DataValue2;DataValue3";
        TableData tableData = new TableData(tableContent, tableName, testCase, true);
        Assert.assertTrue("Table: " + tableData.toHtml(LogLevel.INFO), tableData.toHtml(LogLevel.INFO).equals(expectedHtml));
        Assert.assertTrue("Table: " + tableData.toString(), tableData.toString().equals(expectedString));
    }

    @Test
    public void tableCreationTestCellDataTrim(){
        TestCase testCase = new TestCase(null, "dummy");
        String tableName = "UnitTestTable";
        String expectedHtml = "<table class=\"table padding\"><tr><td><table class=\"table data  INFO\"><tr class=\"table headline  INFO\"><td class=\"table headline soughtafter INFO\">Heading1</td><td class=\"table headline soughtafter INFO\">Heading2</td><td class=\"table headline soughtafter INFO\">Heading3</td></tr><tr class=\"table dataRow imperfectmatch  INFO\"><td class=\"table datacell matchedcellonrowbutnotthiscell  INFO\">DataValue1</td><td class=\"table datacell matchedcellonrowbutnotthiscell  INFO\">DataValue2</td><td class=\"table datacell matchedcellonrowbutnotthiscell  INFO\">DataValue3</td></tr></table></td></tr></table>";
        String expectedString = "Heading1;Heading2;Heading3" + SupportMethods.LF +
                "DataValue1;DataValue2;DataValue3";
        String tableContent = "Heading1 ; Heading2;Heading3 " + SupportMethods.LF + "DataValue1 ; DataValue2;DataValue3 ";
        TableData tableData = new TableData(tableContent, tableName, testCase, true);
        Assert.assertTrue("Table: " + tableData.toHtml(LogLevel.INFO), tableData.toHtml(LogLevel.INFO).equals(expectedHtml));
        Assert.assertTrue("Table: " + tableData.toString(), tableData.toString().equals(expectedString));
    }

    @Test
    public void getValueForHeadlinedataValueTest(){
        TestCase testCase = new TestCase(null, "dummy");
        String tableName = "UnitTestTable";
        String expectedHtml = "<table class=\"table padding\"><tr><td><table class=\"table data  INFO\"><tr class=\"table headline  INFO\"><td class=\"table headline soughtafter INFO\">Heading1</td><td class=\"table headline soughtafter INFO\">Heading2</td><td class=\"table headline soughtafter INFO\">Heading3</td></tr><tr class=\"table dataRow imperfectmatch  INFO\"><td class=\"table datacell matchedcellonrowbutnotthiscell  INFO\">DataValue2</td><td class=\"table datacell matchedcellonrowbutnotthiscell  INFO\">DataValue3</td><td class=\"table datacell matchedcellonrowbutnotthiscell  INFO\">DataValue1</td></tr></table></td></tr></table>";
        String expectedString = "Heading1;Heading2;Heading3" + SupportMethods.LF +
                "DataValue2;DataValue3;DataValue1";
        String tableContent = "Heading1 ; Heading2;Heading3 " + SupportMethods.LF + "DataValue1 ; DataValue2;DataValue3 ";
        TableData tableData = new TableData(tableContent, tableName, testCase, true);
        Assert.assertTrue(tableData.getValueForHeadlineInTableData("Heading2").equals("DataValue2"));
    }

    @Test
    public void verifyDataValueNonRegexPositive(){
        TestCase testCase = new TestCase(null, "dummy");
        String tableName = "UnitTestTable";
        String tableContent = "Heading1 ; Heading2;Heading3 " + SupportMethods.LF + "DataValue1 ; DataValue2;DataValue3 ";
        TableData tableData = new TableData(tableContent, tableName, testCase, true);
        tableData.verifyRow("Heading2:DataValue2", false);
        Assert.assertTrue(testCase.testCaseLog.toString(), testCase.testCaseLog.toString().contains("Verification passed"));
    }

    @Test
    public void verifyDataValueNonRegexPartialPositive(){
        TestCase testCase = new TestCase(null, "dummy");
        String tableName = "UnitTestTable";
        String tableContent = "Heading1 ; Heading2;Heading3 " + SupportMethods.LF + "DataValue1 ; DataValue2;DataValue3 ";
        TableData tableData = new TableData(tableContent, tableName, testCase, true);
        tableData.verifyRow("Heading2:ataValue2", false);
        Assert.assertTrue(testCase.testCaseLog.toString(), testCase.testCaseLog.toString().contains("Verification passed"));
    }

    @Test
    public void verifyDataValueRegexNegative(){
        TestCase testCase = new TestCase(null, "dummy");
        String tableName = "UnitTestTable";
        String tableContent = "Heading1 ; Heading2;Heading3 " + SupportMethods.LF + "DataValue1 ; DataValue2;DataValue3 ";
        TableData tableData = new TableData(tableContent, tableName, testCase, true);
        tableData.verifyRow("Heading2:DataV.*7", true);
        Assert.assertTrue(testCase.testCaseLog.toString(), testCase.testCaseLog.toString().contains("Verification failed"));
    }

    @Test
    public void verifyDataValuesNonRegexNegative(){
        TestCase testCase = new TestCase(null, "dummy");
        String tableName = "UnitTestTable";
        String tableContent = "Heading1 ; Heading2;Heading3 " + SupportMethods.LF + "DataValue1 ; DataValue2;DataValue3 " + SupportMethods.LF +
                "DataValue4;DataValue5;DataValue6";
        TableData tableData = new TableData(tableContent, tableName, testCase, true);
        tableData.verifyRows(new String[] {"Heading2:DataValue7", "Heading1:DataValue4"}, false);
        Assert.assertTrue(testCase.testCaseLog.toString(), testCase.testCaseLog.toString().contains("Verification failed"));
    }

    @Test
    public void verifyDataValuesRegexNegative(){
        TestCase testCase = new TestCase(null, "dummy");
        String tableName = "UnitTestTable";
        String tableContent = "Heading1 ; Heading2;Heading3 " + SupportMethods.LF + "DataValue1 ; DataValue2;DataValue3 " + SupportMethods.LF +
                "DataValue4;DataValue5;DataValue6";
        TableData tableData = new TableData(tableContent, tableName, testCase, true);
        tableData.verifyRows(new String[] {"Heading2:DataV.*2", "Heading1:Data.*7"}, true);
        Assert.assertTrue(testCase.testCaseLog.toString(), testCase.testCaseLog.toString().contains("Verification failed"));
    }

    @Test
    public void verifyDataValueRegexPositive(){
        TestCase testCase = new TestCase(null, "dummy");
        String tableName = "UnitTestTable";
        String tableContent = "Heading1 ; Heading2;Heading3 " + SupportMethods.LF + "DataValue1 ; DataValue2;DataValue3 ";
        TableData tableData = new TableData(tableContent, tableName, testCase, true);
        tableData.verifyRow("Heading2:DataV.*2", true);
        Assert.assertTrue(testCase.testCaseLog.toString(), testCase.testCaseLog.toString().contains("Verification passed"));
    }

    @Test
    public void verifyDataValuesNonRegexPositive(){
        TestCase testCase = new TestCase(null, "dummy");
        String tableName = "UnitTestTable";
        String tableContent = "Heading1 ; Heading2;Heading3 " + SupportMethods.LF + "DataValue1 ; DataValue2;DataValue3 " + SupportMethods.LF +
                "DataValue4;DataValue5;DataValue6";
        TableData tableData = new TableData(tableContent, tableName, testCase, true);
        tableData.verifyRows(new String[] {"Heading2:DataValue2", "Heading1:DataValue4"}, false);
        Assert.assertTrue(testCase.testCaseLog.toString(), testCase.testCaseLog.toString().contains("Verification passed"));
    }

    @Test
    public void verifyDataValuesRegexPositive(){
        TestCase testCase = new TestCase(null, "dummy");
        String tableName = "UnitTestTable";
        String tableContent = "Heading1 ; Heading2;Heading3 " + SupportMethods.LF + "DataValue1 ; DataValue2;DataValue3 " + SupportMethods.LF +
                "DataValue4;DataValue5;DataValue6";
        TableData tableData = new TableData(tableContent, tableName, testCase, true);
        tableData.verifyRows(new String[] {"Heading2:DataV.*2", "Heading1:Data.*4"}, true);
        Assert.assertTrue(testCase.testCaseLog.toString(), testCase.testCaseLog.toString().contains("Verification passed"));
    }

    @Test
    public void dataValueTestGetValueForHeadlineWithMoreThanOneRow(){
        TestCase testCase = new TestCase(null, "dummy");
        String tableName = "UnitTestTable";
        String tableContent = "Heading1 ; Heading2;Heading3 " + SupportMethods.LF + "DataValue1 ; DataValue2;DataValue3 " + SupportMethods.LF + "DataValue4;DataValue5;DataValue6";
        TableData tableData = new TableData(tableContent, tableName, testCase, true);
        Assert.assertNull(tableData.getValueForHeadlineInTableData("Heading2"));
        Assert.assertTrue(testCase.testCaseLog.toString(), testCase.testCaseLog.toString().contains("Verification problem"));
        Assert.assertTrue(testCase.testCaseLog.toString(), testCase.testCaseLog.toString().contains("Cannot get data for headline 'Heading2' in the table of UnitTestTable since it un-expectedly matched with multiple rows."));
        Assert.assertTrue(testCase.testCaseLog.toString(), testCase.testCaseLog.toString().contains("Deviation extra info"));
        Assert.assertTrue(testCase.testCaseLog.toString(), testCase.testCaseLog.toString().contains("The table that triggered the error had the following content:"));
    }

    @Test
    public void tableIsEmptyPositive(){
        TestCase testCase = new TestCase(null, "dummy");
        String tableName = "UnitTestTable";
        String tableContent = "";
        TableData tableData = new TableData(tableContent, tableName, testCase, true);
        Assert.assertTrue(tableData.tableIsEmpty());
    }

    @Test
    public void tableIsEmptyNegative(){
        TestCase testCase = new TestCase(null, "dummy");
        String tableName = "UnitTestTable";
        String tableContent = "Heading1;Heading2" + SupportMethods.LF + "DataValue1;DataValue2";
        TableData tableData = new TableData(tableContent, tableName, testCase, true);
        Assert.assertFalse(tableData.tableIsEmpty());
    }

    @Test
    public void tableWithOnlyHeadlingesIsNotEmpty(){
        TestCase testCase = new TestCase(null, "dummy");
        String tableName = "UnitTestTable";
        String tableContent = "Heading1;Heading2" + SupportMethods.LF;
        TableData tableData = new TableData(tableContent, tableName, testCase, true);
        Assert.assertTrue(tableData.tableIsEmpty());
    }
}
