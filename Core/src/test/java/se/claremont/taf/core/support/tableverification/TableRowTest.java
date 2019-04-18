package se.claremont.taf.core.support.tableverification;

import org.junit.Assert;
import org.junit.Test;
import se.claremont.taf.core.testset.UnitTestClass;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests for TableRow class
 *
 * Created by jordam on 2017-01-28.
 */
public class TableRowTest extends UnitTestClass{

    @Test
    public void rowCreationTestEmtpyRow(){
        List<DataCell> cells = new ArrayList<>();
        TableRow tableRow = new TableRow(cells);
        Assert.assertTrue(tableRow.dataCells.size() == 0);
        Assert.assertTrue(tableRow.tableRowEvaluationStatus == TableRowEvaluationStatus.UNEVALUATED);
        cells.add(new DataCell("Content1", "Headline1"));
        TableRow tableRow2 = new TableRow(cells);
    }

    @Test
    public void rowCreationTestSingleCell(){
        List<DataCell> cells = new ArrayList<>();
        cells.add(new DataCell("Content1", "Headline1"));
        TableRow tableRow = new TableRow(cells);
        Assert.assertTrue(tableRow.dataCells.size() == 1);
        Assert.assertTrue(tableRow.tableRowEvaluationStatus == TableRowEvaluationStatus.UNEVALUATED);
    }

    @Test
    public void rowCreationTestMultiCell(){
        List<DataCell> cells = new ArrayList<>();
        cells.add(new DataCell("Content1", "Headline1"));
        cells.add(new DataCell("Content2", "Headline2"));
        cells.add(new DataCell("Content3", "Headline3"));
        TableRow tableRow = new TableRow(cells);
        Assert.assertTrue(tableRow.dataCells.size() == 3);
        Assert.assertTrue(tableRow.tableRowEvaluationStatus == TableRowEvaluationStatus.UNEVALUATED);
    }

    @Test
    public void toStringTest(){
        List<DataCell> cells = new ArrayList<>();
        cells.add(new DataCell("Content1", "Headline1"));
        cells.add(new DataCell("Content2", "Headline2"));
        cells.add(new DataCell("Content3", "Headline3"));
        TableRow tableRow = new TableRow(cells);
        Assert.assertTrue(tableRow.toString().equals("Content1; Content2; Content3"));
    }

    @Test
    public void evaluationTestExactMatch(){
        List<DataCell> cells = new ArrayList<>();
        cells.add(new DataCell("Content1", "Headline1"));
        cells.add(new DataCell("Content2", "Headline2"));
        cells.add(new DataCell("Content3", "Headline3"));
        TableRow tableRow = new TableRow(cells);
        tableRow.evaluate("Headline2:Content2", CellMatchingType.EXACT_MATCH);
        Assert.assertTrue(tableRow.tableRowEvaluationStatus.toString(), tableRow.tableRowEvaluationStatus.equals(TableRowEvaluationStatus.ONLY_MATCHES));
    }

    @Test
    public void evaluationTestCellTrimming(){
        List<DataCell> cells = new ArrayList<>();
        cells.add(new DataCell("Content1", "Headline1"));
        cells.add(new DataCell("Content2", "Headline2"));
        cells.add(new DataCell("Content3", "Headline3"));
        TableRow tableRow = new TableRow(cells);
        tableRow.evaluate(" Headline2 : Content2 ", CellMatchingType.EXACT_MATCH);
        Assert.assertTrue(tableRow.tableRowEvaluationStatus.toString(), tableRow.tableRowEvaluationStatus.equals(TableRowEvaluationStatus.ONLY_MATCHES));
    }

    @Test
    public void evaluationTestHeadlineNotFound(){
        List<DataCell> cells = new ArrayList<>();
        cells.add(new DataCell("Content1", "Headline1"));
        cells.add(new DataCell("Content2", "Headline2"));
        cells.add(new DataCell("Content3", "Headline3"));
        TableRow tableRow = new TableRow(cells);
        tableRow.evaluate("Headline0:Content2", CellMatchingType.EXACT_MATCH);
        Assert.assertTrue(tableRow.tableRowEvaluationStatus.toString(), tableRow.tableRowEvaluationStatus.equals(TableRowEvaluationStatus.UNEVALUATED));
    }

    @Test
    public void evaluationTestCellTrimmingContainsMatchWithExactMatch(){
        List<DataCell> cells = new ArrayList<>();
        cells.add(new DataCell("Content1", "Headline1"));
        cells.add(new DataCell("Content2", "Headline2"));
        cells.add(new DataCell("Content3", "Headline3"));
        TableRow tableRow = new TableRow(cells);
        tableRow.evaluate(" Headline2 : Content2 ", CellMatchingType.CONTAINS_MATCH);
        Assert.assertTrue(tableRow.tableRowEvaluationStatus.toString(), tableRow.tableRowEvaluationStatus.equals(TableRowEvaluationStatus.ONLY_MATCHES));
    }

    @Test
    public void evaluationTestCellTrimmingContainsMatchWithMultiplePartialMatch(){
        List<DataCell> cells = new ArrayList<>();
        cells.add(new DataCell("Content1", "Headline1"));
        cells.add(new DataCell("Content2", "Headline2"));
        cells.add(new DataCell("Content3", "Headline3"));
        TableRow tableRow = new TableRow(cells);
        tableRow.evaluate(" Headline : Content ", CellMatchingType.CONTAINS_MATCH);
        Assert.assertTrue(tableRow.tableRowEvaluationStatus.toString(), tableRow.tableRowEvaluationStatus.equals(TableRowEvaluationStatus.ONLY_MATCHES));
    }

    @Test
    public void evaluationTestCellTrimmingRegexMatchWithMultiplePartialMatch(){
        List<DataCell> cells = new ArrayList<>();
        cells.add(new DataCell("Content1", "Headline1"));
        cells.add(new DataCell("Content2", "Headline2"));
        cells.add(new DataCell("Content3", "Headline3"));
        TableRow tableRow = new TableRow(cells);
        tableRow.evaluate(" .*Headline.* : .*Content.* ", CellMatchingType.REGEX_MATCH);
        Assert.assertTrue(tableRow.tableRowEvaluationStatus.toString(), tableRow.tableRowEvaluationStatus.equals(TableRowEvaluationStatus.ONLY_MATCHES));
    }

    @Test
    public void negativeTestRegexMatchingBadContent(){
        List<DataCell> cells = new ArrayList<>();
        cells.add(new DataCell("Content1", "Headline1"));
        cells.add(new DataCell("Content2", "Headline2"));
        cells.add(new DataCell("Content3", "Headline3"));
        TableRow tableRow = new TableRow(cells);
        tableRow.evaluate(" .*Headline.* : .*content.* ", CellMatchingType.REGEX_MATCH);
        Assert.assertTrue(tableRow.tableRowEvaluationStatus.toString(), tableRow.tableRowEvaluationStatus.equals(TableRowEvaluationStatus.NO_MATCH));
    }

    @Test
    public void negativeTestRegexMatchingBadHeadline(){
        List<DataCell> cells = new ArrayList<>();
        cells.add(new DataCell("Content1", "Headline1"));
        cells.add(new DataCell("Content2", "Headline2"));
        cells.add(new DataCell("Content3", "Headline3"));
        TableRow tableRow = new TableRow(cells);
        tableRow.evaluate(" .*headline.* : .*Content.* ", CellMatchingType.REGEX_MATCH);
        Assert.assertTrue(tableRow.tableRowEvaluationStatus.toString(), tableRow.tableRowEvaluationStatus.equals(TableRowEvaluationStatus.UNEVALUATED));
    }

    @Test
    public void negativeTestRegexMatchingBadHeadlineAndBadContent(){
        List<DataCell> cells = new ArrayList<>();
        cells.add(new DataCell("Content1", "Headline1"));
        cells.add(new DataCell("Content2", "Headline2"));
        cells.add(new DataCell("Content3", "Headline3"));
        TableRow tableRow = new TableRow(cells);
        tableRow.evaluate(" .*headline.* : .*content.* ", CellMatchingType.REGEX_MATCH);
        Assert.assertTrue(tableRow.tableRowEvaluationStatus.toString(), tableRow.tableRowEvaluationStatus.equals(TableRowEvaluationStatus.UNEVALUATED));
    }

    @Test
    public void negativeTestContainsMatchingBadContent(){
        List<DataCell> cells = new ArrayList<>();
        cells.add(new DataCell("Content1", "Headline1"));
        cells.add(new DataCell("Content2", "Headline2"));
        cells.add(new DataCell("Content3", "Headline3"));
        TableRow tableRow = new TableRow(cells);
        tableRow.evaluate(" Headline : content ", CellMatchingType.CONTAINS_MATCH);
        Assert.assertTrue(tableRow.tableRowEvaluationStatus.toString(), tableRow.tableRowEvaluationStatus.equals(TableRowEvaluationStatus.NO_MATCH));
    }

    @Test
    public void negativeTestContainsMatchingBadHeadline(){
        List<DataCell> cells = new ArrayList<>();
        cells.add(new DataCell("Content1", "Headline1"));
        cells.add(new DataCell("Content2", "Headline2"));
        cells.add(new DataCell("Content3", "Headline3"));
        TableRow tableRow = new TableRow(cells);
        tableRow.evaluate(" headline : Content ", CellMatchingType.CONTAINS_MATCH);
        Assert.assertTrue(tableRow.tableRowEvaluationStatus.toString(), tableRow.tableRowEvaluationStatus.equals(TableRowEvaluationStatus.UNEVALUATED));
    }

    @Test
    public void negativeTestContainsMatchingBadHeadlineAndBadContent(){
        List<DataCell> cells = new ArrayList<>();
        cells.add(new DataCell("Content1", "Headline1"));
        cells.add(new DataCell("Content2", "Headline2"));
        cells.add(new DataCell("Content3", "Headline3"));
        TableRow tableRow = new TableRow(cells);
        tableRow.evaluate(" headline.* : content.* ", CellMatchingType.REGEX_MATCH);
        Assert.assertTrue(tableRow.tableRowEvaluationStatus.toString(), tableRow.tableRowEvaluationStatus.equals(TableRowEvaluationStatus.UNEVALUATED));
    }

    @Test
    public void negativeTestExactMatchingBadContent(){
        List<DataCell> cells = new ArrayList<>();
        cells.add(new DataCell("Content1", "Headline1"));
        cells.add(new DataCell("Content2", "Headline2"));
        cells.add(new DataCell("Content3", "Headline3"));
        TableRow tableRow = new TableRow(cells);
        tableRow.evaluate(" Headline1 : content ", CellMatchingType.EXACT_MATCH);
        Assert.assertTrue(tableRow.tableRowEvaluationStatus.toString(), tableRow.tableRowEvaluationStatus.equals(TableRowEvaluationStatus.NO_MATCH));
    }

    @Test
    public void negativeTestExactMatchingBadHeadline(){
        List<DataCell> cells = new ArrayList<>();
        cells.add(new DataCell("Content1", "Headline1"));
        cells.add(new DataCell("Content2", "Headline2"));
        cells.add(new DataCell("Content3", "Headline3"));
        TableRow tableRow = new TableRow(cells);
        tableRow.evaluate(" headline1 : Content1 ", CellMatchingType.EXACT_MATCH);
        Assert.assertTrue(tableRow.tableRowEvaluationStatus.toString(), tableRow.tableRowEvaluationStatus.equals(TableRowEvaluationStatus.UNEVALUATED));
    }

    @Test
    public void negativeTestExactMatchingBadHeadlineAndBadContent(){
        List<DataCell> cells = new ArrayList<>();
        cells.add(new DataCell("Content1", "Headline1"));
        cells.add(new DataCell("Content2", "Headline2"));
        cells.add(new DataCell("Content3", "Headline3"));
        TableRow tableRow = new TableRow(cells);
        tableRow.evaluate(" headline : content ", CellMatchingType.EXACT_MATCH);
        Assert.assertTrue(tableRow.tableRowEvaluationStatus.toString(), tableRow.tableRowEvaluationStatus.equals(TableRowEvaluationStatus.UNEVALUATED));
    }

    @Test
    public void multipleMatchesExactPositive(){
        List<DataCell> cells = new ArrayList<>();
        cells.add(new DataCell("Content1", "Headline1"));
        cells.add(new DataCell("Content2", "Headline2"));
        cells.add(new DataCell("Content3", "Headline3"));
        TableRow tableRow = new TableRow(cells);
        tableRow.evaluate("Headline1 : Content1 ; Headline2:Content2", CellMatchingType.EXACT_MATCH);
        Assert.assertTrue(tableRow.tableRowEvaluationStatus.toString(), tableRow.tableRowEvaluationStatus.equals(TableRowEvaluationStatus.ONLY_MATCHES));
    }

    @Test
    public void multipleMatchesContainsPositive(){
        List<DataCell> cells = new ArrayList<>();
        cells.add(new DataCell("Content1", "Headline1"));
        cells.add(new DataCell("Content2", "Headline2"));
        cells.add(new DataCell("Content3", "Headline3"));
        TableRow tableRow = new TableRow(cells);
        tableRow.evaluate("Headline1 : Content1 ; Headline2:Content2", CellMatchingType.CONTAINS_MATCH);
        Assert.assertTrue(tableRow.tableRowEvaluationStatus.toString(), tableRow.tableRowEvaluationStatus.equals(TableRowEvaluationStatus.ONLY_MATCHES));
    }

    @Test
    public void multipleMatchesRegexPositive(){
        List<DataCell> cells = new ArrayList<>();
        cells.add(new DataCell("Content1", "Headline1"));
        cells.add(new DataCell("Content2", "Headline2"));
        cells.add(new DataCell("Content3", "Headline3"));
        TableRow tableRow = new TableRow(cells);
        tableRow.evaluate("Headline.* : Content.* ; .*eadline2:.*ontent2", CellMatchingType.REGEX_MATCH);
        Assert.assertTrue(tableRow.tableRowEvaluationStatus.toString(), tableRow.tableRowEvaluationStatus.equals(TableRowEvaluationStatus.ONLY_MATCHES));
    }

    @Test
    public void multipleMatchesExactAmbiguous(){
        List<DataCell> cells = new ArrayList<>();
        cells.add(new DataCell("Content1", "Headline1"));
        cells.add(new DataCell("Content2", "Headline2"));
        cells.add(new DataCell("Content3", "Headline3"));
        TableRow tableRow = new TableRow(cells);
        tableRow.evaluate("Headline3 : content1 ; Headline2:Content2", CellMatchingType.EXACT_MATCH);
        Assert.assertTrue(tableRow.tableRowEvaluationStatus.toString(), tableRow.tableRowEvaluationStatus.equals(TableRowEvaluationStatus.BOTH_MATCHES_AND_NON_MATCHES));
    }

    @Test
    public void multipleMatchesContainsAmbiguous(){
        List<DataCell> cells = new ArrayList<>();
        cells.add(new DataCell("Content1", "Headline1"));
        cells.add(new DataCell("Content2", "Headline2"));
        cells.add(new DataCell("Content3", "Headline3"));
        TableRow tableRow = new TableRow(cells);
        tableRow.evaluate("Headline3 : content1 ; Headline2:Content2", CellMatchingType.CONTAINS_MATCH);
        Assert.assertTrue(tableRow.tableRowEvaluationStatus.toString(), tableRow.tableRowEvaluationStatus.equals(TableRowEvaluationStatus.BOTH_MATCHES_AND_NON_MATCHES));
    }

    @Test
    public void multipleMatchesRegexAmbiguous(){
        List<DataCell> cells = new ArrayList<>();
        cells.add(new DataCell("Content1", "Headline1"));
        cells.add(new DataCell("Content2", "Headline2"));
        cells.add(new DataCell("Content3", "Headline3"));
        TableRow tableRow = new TableRow(cells);
        tableRow.evaluate("Headline3.* : content.* ; .*eadline2:.*ontent2", CellMatchingType.REGEX_MATCH);
        Assert.assertTrue(tableRow.tableRowEvaluationStatus.toString(), tableRow.tableRowEvaluationStatus.equals(TableRowEvaluationStatus.BOTH_MATCHES_AND_NON_MATCHES));
    }

    @Test
    public void multipleMatchesExactNegative(){
        List<DataCell> cells = new ArrayList<>();
        cells.add(new DataCell("Content1", "Headline1"));
        cells.add(new DataCell("Content2", "Headline2"));
        cells.add(new DataCell("Content3", "Headline3"));
        TableRow tableRow = new TableRow(cells);
        tableRow.evaluate("Headline3 : content1 ; Headline2:content2", CellMatchingType.EXACT_MATCH);
        Assert.assertTrue(tableRow.tableRowEvaluationStatus.toString(), tableRow.tableRowEvaluationStatus.equals(TableRowEvaluationStatus.NO_MATCH));
    }

    @Test
    public void multipleMatchesContainsNegative(){
        List<DataCell> cells = new ArrayList<>();
        cells.add(new DataCell("Content1", "Headline1"));
        cells.add(new DataCell("Content2", "Headline2"));
        cells.add(new DataCell("Content3", "Headline3"));
        TableRow tableRow = new TableRow(cells);
        tableRow.evaluate("Headline3 : content1 ; Headline2:content2", CellMatchingType.CONTAINS_MATCH);
        Assert.assertTrue(tableRow.tableRowEvaluationStatus.toString(), tableRow.tableRowEvaluationStatus.equals(TableRowEvaluationStatus.NO_MATCH));
    }

    @Test
    public void multipleMatchesRegexNegative(){
        List<DataCell> cells = new ArrayList<>();
        cells.add(new DataCell("Content1", "Headline1"));
        cells.add(new DataCell("Content2", "Headline2"));
        cells.add(new DataCell("Content3", "Headline3"));
        TableRow tableRow = new TableRow(cells);
        tableRow.evaluate("Headline3.* : content.* ; .*eadline2:.*Ontent2", CellMatchingType.REGEX_MATCH);
        Assert.assertTrue(tableRow.tableRowEvaluationStatus.toString(), tableRow.tableRowEvaluationStatus.equals(TableRowEvaluationStatus.NO_MATCH));
    }}
