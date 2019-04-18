package se.claremont.taf.core.support.tableverification;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import se.claremont.taf.core.reporting.HtmlStyles;
import se.claremont.taf.core.testcase.TestCase;
import se.claremont.taf.core.testset.UnitTestClass;

/**
 * Tests for verification of tables
 *
 * Created by jordam on 2017-01-28.
 */
public class TableVerificationTest extends UnitTestClass{

    @Test
    public void createTableData(){
        TestCase testCase = new TestCase(null, "dummy");
        TableData tableData = new TableData(testCase,
                "TestTable",
                " Heading1 ;Heading2; Heading3" + System.lineSeparator() +
                        "Row1Heading1;Row1Heading2;Row1Heading3 " + System.lineSeparator() +
                        " Row2Heading1;Row2Heading2 ; Row2Heading3" + System.lineSeparator() +
                        "Row3Heading1 ;Row3Heading2;Row3Heading3" + System.lineSeparator());
        String oracle = "TestTable" + System.lineSeparator() + "Heading1; Heading2; Heading3" + System.lineSeparator() +
                "Row1Heading1; Row1Heading2; Row1Heading3" + System.lineSeparator() +
                "Row2Heading1; Row2Heading2; Row2Heading3" + System.lineSeparator() +
                "Row3Heading1; Row3Heading2; Row3Heading3" + System.lineSeparator();
        Assert.assertTrue(tableData.toString(), tableData.toString().equals(oracle));
        Assert.assertTrue(tableData.tableElementName.equals("TestTable"));
        Assert.assertTrue(tableData.headlines.contains("Heading1"));
        Assert.assertTrue(tableData.headlines.contains("Heading2"));
        Assert.assertTrue(tableData.headlines.contains("Heading3"));
        Assert.assertTrue(tableData.rows.get(0).toString().equals("Row1Heading1; Row1Heading2; Row1Heading3"));
        Assert.assertTrue(tableData.rows.get(1).toString().equals("Row2Heading1; Row2Heading2; Row2Heading3"));
        Assert.assertTrue(tableData.rows.get(2).toString().equals("Row3Heading1; Row3Heading2; Row3Heading3"));
    }

    @Test
    public void createTableDataWithGivenHeadings(){
        TestCase testCase = new TestCase(null, "dummy");
        TableData tableData = new TableData(testCase,
                "TestTable",
                "Row1Heading1;Row1Heading2;Row1Heading3 " + System.lineSeparator() +
                        " Row2Heading1;Row2Heading2 ; Row2Heading3" + System.lineSeparator() +
                        "Row3Heading1 ;Row3Heading2;Row3Heading3" + System.lineSeparator(),
                new String[] {" Heading1 ", "Heading2", " Heading3"} );
        String oracle = "TestTable" + System.lineSeparator() + "Heading1; Heading2; Heading3" + System.lineSeparator() +
                "Row1Heading1; Row1Heading2; Row1Heading3" + System.lineSeparator() +
                "Row2Heading1; Row2Heading2; Row2Heading3" + System.lineSeparator() +
                "Row3Heading1; Row3Heading2; Row3Heading3" + System.lineSeparator();
        Assert.assertTrue(tableData.toString(), tableData.toString().equals(oracle));
        Assert.assertTrue(tableData.tableElementName.equals("TestTable"));
        Assert.assertTrue(tableData.headlines.contains("Heading1"));
        Assert.assertTrue(tableData.headlines.contains("Heading2"));
        Assert.assertTrue(tableData.headlines.contains("Heading3"));
        Assert.assertTrue(tableData.rows.get(0).toString().equals("Row1Heading1; Row1Heading2; Row1Heading3"));
        Assert.assertTrue(tableData.rows.get(1).toString().equals("Row2Heading1; Row2Heading2; Row2Heading3"));
        Assert.assertTrue(tableData.rows.get(2).toString().equals("Row3Heading1; Row3Heading2; Row3Heading3"));
    }

    @Test
    public void evaluationOfVerificationMethodsExactMatchPositive(){
        TestCase testCase = new TestCase(null, "dummy");
        TableData tableData = new TableData(testCase,
                "TestTable",
                " Heading1 ;Heading2; Heading3" + System.lineSeparator() +
                        "Row1Heading1;Row1Heading2;Row1Heading3 " + System.lineSeparator() +
                        " Row2Heading1;Row2Heading2 ; Row2Heading3" + System.lineSeparator() +
                        "Row3Heading1 ;Row3Heading2;Row3Heading3" + System.lineSeparator());
        tableData.verifyRowExist("Heading1:Row1Heading1", CellMatchingType.EXACT_MATCH);
        tableData.verifyRowDoesNotExist("Heading1:Row1Heading2", CellMatchingType.EXACT_MATCH);
        tableData.verifyHeadingExist("Heading1");
    }

    @Test
    public void evaluationRowExistanceExactMatchPositive(){
        TestCase testCase = new TestCase(null, "dummy");
        TableData tableData = new TableData(testCase,
                "TestTable",
                " Heading1 ;Heading2; Heading3" + System.lineSeparator() +
                        "Row1Heading1;Row1Heading2;Row1Heading3 " + System.lineSeparator() +
                        " Row2Heading1;Row2Heading2 ; Row2Heading3" + System.lineSeparator() +
                        "Row3Heading1 ;Row3Heading2;Row3Heading3" + System.lineSeparator());
        Assert.assertTrue(tableData.rowExist("Heading1:Row2Heading1", CellMatchingType.EXACT_MATCH));
    }

    @Test
    public void evaluationRowExistanceContainsMatchPositive(){
        TestCase testCase = new TestCase(null, "dummy");
        TableData tableData = new TableData(testCase,
                "TestTable",
                " Heading1 ;Heading2; Heading3" + System.lineSeparator() +
                        "Row1Heading1;Row1Heading2;Row1Heading3 " + System.lineSeparator() +
                        " Row2Heading1;Row2Heading2 ; Row2Heading3" + System.lineSeparator() +
                        "Row3Heading1 ;Row3Heading2;Row3Heading3" + System.lineSeparator());
        Assert.assertTrue(tableData.rowExist("Heading1:Row2Heading", CellMatchingType.CONTAINS_MATCH));
    }

    @Test
    public void evaluation2(){
        TestCase testCase = new TestCase(null, "dummy");
        TableData tableData = new TableData(testCase,
                "TestTable",
                " Heading1 ;Heading2; Heading3" + System.lineSeparator() +
                        "Row1Heading1;Row1Heading2;Row1Heading3 " + System.lineSeparator() +
                        " Row2Heading1;Row2Heading2 ; Row2Heading3" + System.lineSeparator() +
                        "Row3Heading1 ;Row3Heading2;Row3Heading3" + System.lineSeparator());
        Assert.assertTrue(tableData.rowExist(" Heading : Row2Heading ", CellMatchingType.CONTAINS_MATCH));
    }

    @Test
    public void evaluation3(){
        TestCase testCase = new TestCase(null, "dummy");
        TableData tableData = new TableData(testCase,
                "TestTable",
                " Heading1 ;Heading2; Heading3" + System.lineSeparator() +
                        "Row1Heading1;Row1Heading2;Row1Heading3 " + System.lineSeparator() +
                        " Row2Heading1;Row2Heading2 ; Row2Heading3" + System.lineSeparator() +
                        "Row3Heading1 ;Row3Heading2;Row3Heading3" + System.lineSeparator());
        Assert.assertTrue(tableData.rowExist(" Heading1 : Row2Heading1 ", CellMatchingType.CONTAINS_MATCH));
    }

    @Test
    public void evaluation4(){
        TestCase testCase = new TestCase(null, "dummy");
        TableData tableData = new TableData(testCase,
                "TestTable",
                " Heading1 ;Heading2; Heading3" + System.lineSeparator() +
                        "Row1Heading1;Row1Heading2;Row1Heading3 " + System.lineSeparator() +
                        " Row2Heading1;Row2Heading2 ; Row2Heading3" + System.lineSeparator() +
                        "Row3Heading1 ;Row3Heading2;Row3Heading3" + System.lineSeparator());
        Assert.assertTrue(tableData.rowExist("Hea.*ing1: Row2Heading.*", CellMatchingType.REGEX_MATCH));
    }

    @Ignore
    @Test
    //This test fails since the regexp matches multiple headings, hence both positive and negative matches for rows, so no row has only positive matches.
    //Should this be fixed?
    public void evaluation5(){
        TestCase testCase = new TestCase(null, "dummy");
        TableData tableData = new TableData(testCase,
                "TestTable",
                " Heading1 ;Heading2; Heading3" + System.lineSeparator() +
                        "Row1Heading1;Row1Heading2;Row1Heading3 " + System.lineSeparator() +
                        " Row2Heading1;Row2Heading2 ; Row2Heading3" + System.lineSeparator() +
                        "Row3Heading1 ;Row3Heading2;Row3Heading3" + System.lineSeparator());
        Assert.assertTrue(tableData.rowExist(" Hea.*ing.* : Row.*Heading1 ", CellMatchingType.REGEX_MATCH));
    }

    @Test
    public void resetStatus(){
        TestCase testCase = new TestCase(null, "dummy");
        TableData tableData = new TableData(testCase,
                "TestTable",
                " Heading1 ;Heading2; Heading3" + System.lineSeparator() +
                        "Row1Heading1;Row1Heading2;Row1Heading3 " + System.lineSeparator() +
                        " Row2Heading1;Row2Heading2 ; Row2Heading3" + System.lineSeparator() +
                        "Row3Heading1 ;Row3Heading2;Row3Heading3" + System.lineSeparator());
        Assert.assertTrue(tableData.rowExist("Hea.*ing1: Row2Heading.*", CellMatchingType.REGEX_MATCH));
        tableData.resetEvaluationStatus();
        for(TableRow tableRow : tableData.rows){
            Assert.assertTrue(tableRow.tableRowEvaluationStatus.equals(TableRowEvaluationStatus.UNEVALUATED));
            for(DataCell dataCell : tableRow.dataCells){
                Assert.assertTrue(dataCell.dataCellEvaluationStatus.equals(DataCellEvaluationStatus.UNEVALUATED));
            }
        }
    }

    @Test
    public void getValueTest(){
        TestCase testCase = new TestCase(null, "dummy");
        TableData tableData = new TableData(testCase,
                "TestTable",
                " Heading1 ;Heading2; Heading3" + System.lineSeparator() +
                        "Row1Heading1;Row1Heading2;Row1Heading3 " + System.lineSeparator() +
                        " Row2Heading1;Row2Heading2 ; Row2Heading3" + System.lineSeparator() +
                        "Row3Heading1 ;Row3Heading2;Row3Heading3" + System.lineSeparator());
        Assert.assertTrue(tableData.getValueForHeadlineForRow("Heading1", "Heading2:Row2Heading2", CellMatchingType.EXACT_MATCH).equals("Row2Heading1"));
    }

    @Test
    public void getValueTestContainsMatchWithExactMatch(){
        TestCase testCase = new TestCase(null, "dummy");
        TableData tableData = new TableData(testCase,
                "TestTable",
                " Heading1 ;Heading2; Heading3" + System.lineSeparator() +
                        "Row1Heading1;Row1Heading2;Row1Heading3 " + System.lineSeparator() +
                        " Row2Heading1;Row2Heading2 ; Row2Heading3" + System.lineSeparator() +
                        "Row3Heading1 ;Row3Heading2;Row3Heading3" + System.lineSeparator());
        Assert.assertTrue(tableData.getValueForHeadlineForRow("Heading1", "Heading2:Row2Heading2", CellMatchingType.CONTAINS_MATCH).equals("Row2Heading1"));
    }

    @Test
    public void getValueTestContainsMatchWithPartialMatch(){
        TestCase testCase = new TestCase(null, "dummy");
        TableData tableData = new TableData(testCase,
                "TestTable",
                " Heading1 ;Heading2; Heading3" + System.lineSeparator() +
                        "Row1Heading1;Row1Heading2;Row1Heading3 " + System.lineSeparator() +
                        " Row2Heading1;Row2Heading2 ; Row2Heading3" + System.lineSeparator() +
                        "Row3Heading1 ;Row3Heading2;Row3Heading3" + System.lineSeparator());
        Assert.assertTrue(tableData.getValueForHeadlineForRow("Heading1", "Heading2:Row2Heading", CellMatchingType.CONTAINS_MATCH).equals("Row2Heading1"));
    }

    @Test
    public void getValueTestRegexMatchWithPartialMatch(){
        TestCase testCase = new TestCase(null, "dummy");
        TableData tableData = new TableData(testCase,
                "TestTable",
                " Heading1 ;Heading2; Heading3" + System.lineSeparator() +
                        "Row1Heading1;Row1Heading2;Row1Heading3 " + System.lineSeparator() +
                        " Row2Heading1;Row2Heading2 ; Row2Heading3" + System.lineSeparator() +
                        "Row3Heading1 ;Row3Heading2;Row3Heading3" + System.lineSeparator());
        Assert.assertTrue(tableData.getValueForHeadlineForRow("Heading1", "Heading2:Row2Heading.*", CellMatchingType.REGEX_MATCH).equals("Row2Heading1"));
    }

    @Test
    public void getValueTestRegexMatchWrongHeading(){
        TestCase testCase = new TestCase(null, "dummy");
        TableData tableData = new TableData(testCase,
                "TestTable",
                " Heading1 ;Heading2; Heading3" + System.lineSeparator() +
                        "Row1Heading1;Row1Heading2;Row1Heading3 " + System.lineSeparator() +
                        " Row2Heading1;Row2Heading2 ; Row2Heading3" + System.lineSeparator() +
                        "Row3Heading1 ;Row3Heading2;Row3Heading3" + System.lineSeparator());
        Assert.assertTrue(tableData.getValueForHeadlineForRow("Heading4", "Heading2:Row2Heading.*", CellMatchingType.REGEX_MATCH) == null);
    }

    @Test
    public void getValueTestContainMatchWrongHeading(){
        TestCase testCase = new TestCase(null, "dummy");
        TableData tableData = new TableData(testCase,
                "TestTable",
                " Heading1 ;Heading2; Heading3" + System.lineSeparator() +
                        "Row1Heading1;Row1Heading2;Row1Heading3 " + System.lineSeparator() +
                        " Row2Heading1;Row2Heading2 ; Row2Heading3" + System.lineSeparator() +
                        "Row3Heading1 ;Row3Heading2;Row3Heading3" + System.lineSeparator());
        Assert.assertTrue(tableData.getValueForHeadlineForRow("Heading4", "Heading2:Row2Heading2", CellMatchingType.CONTAINS_MATCH) == null);
    }

    @Test
    public void getValueTestExactMatchWrongHeading(){
        TestCase testCase = new TestCase(null, "dummy");
        TableData tableData = new TableData(testCase,
                "TestTable",
                " Heading1 ;Heading2; Heading3" + System.lineSeparator() +
                        "Row1Heading1;Row1Heading2;Row1Heading3 " + System.lineSeparator() +
                        " Row2Heading1;Row2Heading2 ; Row2Heading3" + System.lineSeparator() +
                        "Row3Heading1 ;Row3Heading2;Row3Heading3" + System.lineSeparator());
        Assert.assertTrue(tableData.getValueForHeadlineForRow("Heading4", "Heading2:Row2Heading2", CellMatchingType.EXACT_MATCH) == null);
    }

    @Test
    public void getValueTestRegexMatchWrongRow(){
        TestCase testCase = new TestCase(null, "dummy");
        TableData tableData = new TableData(testCase,
                "TestTable",
                " Heading1 ;Heading2; Heading3" + System.lineSeparator() +
                        "Row1Heading1;Row1Heading2;Row1Heading3 " + System.lineSeparator() +
                        " Row2Heading1;Row2Heading2 ; Row2Heading3" + System.lineSeparator() +
                        "Row3Heading1 ;Row3Heading2;Row3Heading3" + System.lineSeparator());
        Assert.assertTrue(tableData.getValueForHeadlineForRow("Heading1", "Heading2:Row2Heading4", CellMatchingType.EXACT_MATCH) == null);
    }

    @Test
    public void getValueTestContainMatchWrongRow(){
        TestCase testCase = new TestCase(null, "dummy");
        TableData tableData = new TableData(testCase,
                "TestTable",
                " Heading1 ;Heading2; Heading3" + System.lineSeparator() +
                        "Row1Heading1;Row1Heading2;Row1Heading3 " + System.lineSeparator() +
                        " Row2Heading1;Row2Heading2 ; Row2Heading3" + System.lineSeparator() +
                        "Row3Heading1 ;Row3Heading2;Row3Heading3" + System.lineSeparator());
        Assert.assertTrue(tableData.getValueForHeadlineForRow("Heading1", "Heading2:Row2Heading4", CellMatchingType.CONTAINS_MATCH) == null);
    }

    @Test
    public void getValueTestExactMatchWrongRow(){
        TestCase testCase = new TestCase(null, "dummy");
        TableData tableData = new TableData(testCase,
                "TestTable",
                " Heading1 ;Heading2; Heading3" + System.lineSeparator() +
                        "Row1Heading1;Row1Heading2;Row1Heading3 " + System.lineSeparator() +
                        " Row2Heading1;Row2Heading2 ; Row2Heading3" + System.lineSeparator() +
                        "Row3Heading1 ;Row3Heading2;Row3Heading3" + System.lineSeparator());
        Assert.assertTrue(tableData.getValueForHeadlineForRow("Heading1", "Heading2:Row2Heading4", CellMatchingType.EXACT_MATCH) == null);
    }

    @Test
    public void getValueTestRegexMatchWrongHeadingForRow(){
        TestCase testCase = new TestCase(null, "dummy");
        TableData tableData = new TableData(testCase,
                "TestTable",
                " Heading1 ;Heading2; Heading3" + System.lineSeparator() +
                        "Row1Heading1;Row1Heading2;Row1Heading3 " + System.lineSeparator() +
                        " Row2Heading1;Row2Heading2 ; Row2Heading3" + System.lineSeparator() +
                        "Row3Heading1 ;Row3Heading2;Row3Heading3" + System.lineSeparator());
        Assert.assertTrue(tableData.getValueForHeadlineForRow("Heading1", "Heading4:Row2Heading2", CellMatchingType.EXACT_MATCH) == null);
    }

    @Test
    public void getValueTestContainMatchWrongHeadingForRow(){
        TestCase testCase = new TestCase(null, "dummy");
        TableData tableData = new TableData(testCase,
                "TestTable",
                " Heading1 ;Heading2; Heading3" + System.lineSeparator() +
                        "Row1Heading1;Row1Heading2;Row1Heading3 " + System.lineSeparator() +
                        " Row2Heading1;Row2Heading2 ; Row2Heading3" + System.lineSeparator() +
                        "Row3Heading1 ;Row3Heading2;Row3Heading3" + System.lineSeparator());
        Assert.assertTrue(tableData.getValueForHeadlineForRow("Heading1", "Heading4:Row2Heading2", CellMatchingType.CONTAINS_MATCH) == null);
    }

    @Test
    public void getValueTestExactMatchWrongHeadingForRow(){
        TestCase testCase = new TestCase(null, "dummy");
        TableData tableData = new TableData(testCase,
                "TestTable",
                " Heading1 ;Heading2; Heading3" + System.lineSeparator() +
                        "Row1Heading1;Row1Heading2;Row1Heading3 " + System.lineSeparator() +
                        " Row2Heading1;Row2Heading2 ; Row2Heading3" + System.lineSeparator() +
                        "Row3Heading1 ;Row3Heading2;Row3Heading3" + System.lineSeparator());
        Assert.assertTrue(tableData.getValueForHeadlineForRow("Heading1", "Heading4:Row2Heading2", CellMatchingType.EXACT_MATCH) == null);
    }

    @Test
    public void toHtmlTest(){
        TestCase testCase = new TestCase(null, "dummy");
        TableData tableData = new TableData(testCase,
                "TestTable",
                " Heading1 ;Heading2; Heading3" + System.lineSeparator() +
                        "Row1Heading1;Row1Heading2;Row1Heading3 " + System.lineSeparator() +
                        " Row2Heading1;Row2Heading2 ; Row2Heading3" + System.lineSeparator() +
                        "Row3Heading1 ;Row3Heading2;Row3Heading3" + System.lineSeparator());
        System.out.println("<html>\n" +
                "<head>\n" +
                "<style>\n");
        System.out.println(HtmlStyles.tableVerificationStyles());
        System.out.println("</style>\n" +
                "\n" +
                "</head>\n" +
                "<body>\n");
        System.out.println(tableData.toHtml());
        tableData.verifyRowExist("Heading1:Row2Heading1", CellMatchingType.EXACT_MATCH);
        System.out.println(tableData.toHtml());
        tableData.verifyRowExist("Heading1:Row2Heading", CellMatchingType.CONTAINS_MATCH);
        System.out.println(tableData.toHtml());
        tableData.verifyRowExist("Heading1:Row2Heading.*", CellMatchingType.REGEX_MATCH);
        System.out.println(tableData.toHtml());
        tableData.verifyRowExist("Heading1:Row2Heading1;Heading2:Row2", CellMatchingType.CONTAINS_MATCH);
        System.out.println(tableData.toHtml());
        tableData.verifyRowExist("Heading1:Row2Heading1;Heading2:Row3", CellMatchingType.CONTAINS_MATCH);
        System.out.println(tableData.toHtml());
        System.out.println("</body>\n" +
                "</html>\n");
        Assert.assertTrue(tableData.toHtml().contains("<table"));
    }
}
