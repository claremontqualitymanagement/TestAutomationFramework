package se.claremont.taf.core.support.tableverification;

import org.junit.Assert;
import org.junit.Test;
import se.claremont.taf.core.testset.UnitTestClass;

/**
 * Tests for DataCell class.
 *
 * Created by jordam on 2017-01-28.
 */
public class DataCellTest extends UnitTestClass{

    @Test
    public void createDataCell(){
        DataCell dataCell = new DataCell("Content1", "Headline1");
        Assert.assertTrue(dataCell.dataContent.equals("Content1"));
        Assert.assertTrue(dataCell.correspontingHeadline.equals("Headline1"));
    }

    @Test
    public void createDataCellTrimming(){
        DataCell dataCell = new DataCell(" Content1  ", "   Headline1  ");
        Assert.assertTrue(dataCell.dataContent.equals("Content1"));
        Assert.assertTrue(dataCell.correspontingHeadline.equals("Headline1"));
    }

    @Test
    public void exactMatching(){
        DataCell dataCell = new DataCell(" Content1  ", "   Headline1  ");
        Assert.assertTrue(dataCell.isExactMatch("Content1", "Headline1"));
    }

    @Test
    public void exactMatchingTrimming(){
        DataCell dataCell = new DataCell(" Content1  ", "   Headline1  ");
        Assert.assertTrue(dataCell.isExactMatch("  Content1    ", "   Headline1   "));
    }

    @Test
    public void exactMatchingWrongCaseContent(){
        DataCell dataCell = new DataCell("Content1", "Headline1");
        Assert.assertFalse(dataCell.isExactMatch("content1", "Headline1"));
    }

    @Test
    public void exactMatchingWrongCaseHeadline(){
        DataCell dataCell = new DataCell("Content1", "Headline1");
        Assert.assertFalse(dataCell.isExactMatch("Content1", "headline1"));
    }

    @Test
    public void containsMatching(){
        DataCell dataCell = new DataCell("Content1", "Headline1");
        Assert.assertTrue(dataCell.isContainsMatch("Content1", "Headline1"));
    }

    @Test
    public void containsMatchingWorksOnPartialContentMatch(){
        DataCell dataCell = new DataCell("Content1", "Headline1");
        Assert.assertTrue(dataCell.isContainsMatch("Content", "Headline1"));
    }

    @Test
    public void containsMatchingWorksOnPartialHeadlineMatch(){
        DataCell dataCell = new DataCell("Content1", "Headline1");
        Assert.assertTrue(dataCell.isContainsMatch("Content1", "Headline"));
    }

    @Test
    public void containsMatchingOnPartialHeadlineAndContentMatch(){
        DataCell dataCell = new DataCell("Content1", "Headline1");
        Assert.assertTrue(dataCell.isContainsMatch("Content", "Headline"));
    }

    @Test
    public void containsMatchingTrimmingOfBothContentAndHeadlineWithPartialHeadlineMatch(){
        DataCell dataCell = new DataCell("Content1", "Headline1");
        Assert.assertTrue(dataCell.isContainsMatch(" Content1  ", "  Headline  "));
    }

    @Test
    public void containsMatchingPartialHeadlineWithTrimmingAndExactContent(){
        DataCell dataCell = new DataCell("Content1", "Headline1");
        Assert.assertTrue(dataCell.isContainsMatch("Content1", "  Headline   "));
    }

    @Test
    public void containsMatchingTrimmingOnBothHeadlineAndContentAndPartialHeadline(){
        DataCell dataCell = new DataCell("Content1", "Headline1");
        Assert.assertTrue(dataCell.isContainsMatch("  Content1 ", "  Headline  "));
    }

    @Test
    public void containsMatchingWrongCaseContent(){
        DataCell dataCell = new DataCell("Content1", "Headline1");
        Assert.assertFalse(dataCell.isContainsMatch("content1", "Headline"));
    }

    @Test
    public void containsMatchingWrongCaseHeadline(){
        DataCell dataCell = new DataCell("Content1", "Headline1");
        Assert.assertFalse(dataCell.isContainsMatch("Content1", "headline"));
    }

    @Test
    public void performMatchingExactMatchUnevaluated(){
        DataCell dataCell = new DataCell(" Content1 ", " Headline1 ");
        Assert.assertTrue(dataCell.dataCellEvaluationStatus.equals(DataCellEvaluationStatus.UNEVALUATED));
    }

    @Test
    public void performMatchingExactMatchPerfectMatch(){
        DataCell dataCell = new DataCell(" Content1 ", " Headline1 ");
        dataCell.performMatching("Content1", "Headline1");
        Assert.assertTrue(dataCell.dataCellEvaluationStatus.equals(DataCellEvaluationStatus.EXACT_MATCH));
    }

    @Test
    public void performMatchingExactMatchTrimming(){
        DataCell dataCell = new DataCell(" Content1 ", " Headline1 ");
        dataCell.performMatching(" Content1 ", " Headline1 ");
        Assert.assertTrue(dataCell.dataCellEvaluationStatus.equals(DataCellEvaluationStatus.EXACT_MATCH));
    }

    @Test
    public void performMatchingExactMatchWrongCaseInContent(){
        DataCell dataCell = new DataCell(" Content1 ", " Headline1 ");
        dataCell.performMatching("content1", "Headline1");
        Assert.assertTrue(dataCell.dataCellEvaluationStatus.equals(DataCellEvaluationStatus.NO_MATCH));
    }

    @Test
    public void performMatchingExactMatchWrongCaseInHeadline(){
        DataCell dataCell = new DataCell(" Content1 ", " Headline1 ");
        dataCell.performMatching("Content1", "headline1");
        Assert.assertTrue(dataCell.dataCellEvaluationStatus.equals(DataCellEvaluationStatus.NO_MATCH));
    }

    @Test
    public void performMatchingContainsMatchInHeadline(){
        DataCell dataCell = new DataCell(" Content1 ", " Headline1 ");
        dataCell.performMatching("Content1", "Headline");
        Assert.assertTrue(dataCell.dataCellEvaluationStatus.equals(DataCellEvaluationStatus.CONTAINS_MATCH));
    }

    @Test
    public void performMatchingContainsMatchInContent(){
        DataCell dataCell = new DataCell(" Content1 ", " Headline1 ");
        dataCell.performMatching("Content", "Headline1");
        Assert.assertTrue(dataCell.dataCellEvaluationStatus.equals(DataCellEvaluationStatus.CONTAINS_MATCH));
    }

    @Test
    public void performMatchingContainsMatchInContentAndHeadline(){
        DataCell dataCell = new DataCell(" Content1 ", " Headline1 ");
        dataCell.performMatching("Content", "Headline");
        Assert.assertTrue(dataCell.dataCellEvaluationStatus.equals(DataCellEvaluationStatus.CONTAINS_MATCH));
    }

    @Test
    public void performMatchingContainsMatchInContentAndHeadlineWithTrim(){
        DataCell dataCell = new DataCell(" Content1 ", " Headline1 ");
        dataCell.performMatching("  Content  ", "  Headline  ");
        Assert.assertTrue(dataCell.dataCellEvaluationStatus.equals(DataCellEvaluationStatus.CONTAINS_MATCH));
    }

    @Test
    public void performMatchingRegexMatchContentIsRegex(){
        DataCell dataCell = new DataCell(" Content1 ", " Headline1 ");
        dataCell.performMatching(".*Content.*", "Headline1");
        Assert.assertTrue(dataCell.dataCellEvaluationStatus.equals(DataCellEvaluationStatus.REGEX_MATCH));
    }

    @Test
    public void performMatchingRegexMatchHeadlineIsRegex(){
        DataCell dataCell = new DataCell(" Content1 ", " Headline1 ");
        dataCell.performMatching("Content1", ".*Headline.*");
        Assert.assertTrue(dataCell.dataCellEvaluationStatus.equals(DataCellEvaluationStatus.REGEX_MATCH));
    }

    @Test
    public void performMatchingRegexMatchHeadlineAndContentIsRegex(){
        DataCell dataCell = new DataCell(" Content1 ", " Headline1 ");
        dataCell.performMatching("Content.*", ".*Headline.*");
        Assert.assertTrue(dataCell.dataCellEvaluationStatus.equals(DataCellEvaluationStatus.REGEX_MATCH));
    }

    @Test
    public void performMatchingRegexMatchTrimming(){
        DataCell dataCell = new DataCell("Content1", "Headline1");
        dataCell.performMatching("   Content.*   ", "   .*Headline.*   ");
        Assert.assertTrue(dataCell.dataCellEvaluationStatus.equals(DataCellEvaluationStatus.REGEX_MATCH));
    }


}
