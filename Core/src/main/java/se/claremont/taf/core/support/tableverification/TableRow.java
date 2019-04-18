package se.claremont.taf.core.support.tableverification;

import se.claremont.taf.core.support.SupportMethods;

import java.util.ArrayList;
import java.util.List;

/**
 * A row in a table
 *
 * Created by jordam on 2017-01-28.
 */
@SuppressWarnings("WeakerAccess")
public class TableRow {
    List<DataCell> dataCells = new ArrayList<>();
    TableRowEvaluationStatus tableRowEvaluationStatus = TableRowEvaluationStatus.UNEVALUATED;

    public TableRow(List<DataCell> dataCells){
        this.dataCells = dataCells;
    }

    public String getValueForHeadline(String headline, CellMatchingType cellMatchingType){
        if(!headlineExist(headline.trim(), cellMatchingType)) return null;
        for(DataCell dataCell : dataCells){
            if(dataCell.correspontingHeadline.equals(headline.trim()) && (cellMatchingType.equals(CellMatchingType.EXACT_MATCH) || cellMatchingType.equals(CellMatchingType.CONTAINS_MATCH))){
                return dataCell.dataContent;
            } else if(dataCell.correspontingHeadline.contains(headline.trim()) && cellMatchingType.equals(CellMatchingType.CONTAINS_MATCH)){
                return dataCell.dataContent;
            } else if(SupportMethods.isRegexMatch(dataCell.correspontingHeadline, headline.trim())){
                return dataCell.dataContent;
            }
        }
        return null;
    }

    @Override
    public String toString(){
        List<String> returnStrings = new ArrayList<>();
        for(DataCell dataCell : dataCells){
            returnStrings.add(dataCell.dataContent);
        }
        return String.join("; ", returnStrings);
    }

    boolean headlineExist(String headline, CellMatchingType cellMatchingType){
        for(DataCell dataCell : dataCells){
            if(cellMatchingType.equals(CellMatchingType.EXACT_MATCH) && dataCell.correspontingHeadline.trim().equals(headline.trim())) return true;
            if(cellMatchingType.equals(CellMatchingType.CONTAINS_MATCH) && (dataCell.correspontingHeadline.trim().equals(headline.trim()) || dataCell.correspontingHeadline.trim().contains(headline))) return true;
            if(cellMatchingType.equals(CellMatchingType.REGEX_MATCH) && SupportMethods.isRegexMatch(dataCell.correspontingHeadline.trim(), headline.trim())) return true;
        }
        return false;
    }

    void evaluate(String headlineColonValueSemicolonSeparatedStringOfExpectedMatchesForRow, CellMatchingType cellMatchingType){
        String[] pairs = headlineColonValueSemicolonSeparatedStringOfExpectedMatchesForRow.split(";");
        for(String pair : pairs){
            String headline = pair.split(":")[0].trim();
            if(pair.split(":").length == 1){//Not valid
                tableRowEvaluationStatus = TableRowEvaluationStatus.UNEVALUATED;
                return;
            }
            String dataValue = pair.substring(pair.indexOf(":") + 1).trim();
            for(DataCell dataCell : dataCells){
                if(dataCell.correspontingHeadline.equals(headline) || dataCell.correspontingHeadline.contains(headline) || SupportMethods.isRegexMatch(dataCell.correspontingHeadline, headline)){
                    dataCell.performMatching(dataValue, headline);
                }
            }
        }
        setRowStatus(cellMatchingType);
    }

    void resetStatus(){
        tableRowEvaluationStatus = TableRowEvaluationStatus.UNEVALUATED;
        for(DataCell cell : dataCells){
            cell.resetStatus();
        }
    }

    String toHtml(){
        StringBuilder sb = new StringBuilder();
        sb.append("   <tr class=\"tableevaluationrow ").append(tableRowEvaluationStatus.toString().toLowerCase()).append("\">").append(System.lineSeparator());
        for(DataCell cell : dataCells){
            sb.append(cell.toHtml());
        }
        sb.append("   </tr>").append(System.lineSeparator());
        return sb.toString();
    }

    private void setRowStatus(CellMatchingType cellMatchingType){
        for(DataCell dataCell : dataCells){
            if(tableRowEvaluationStatus.equals(TableRowEvaluationStatus.UNEVALUATED)){
                if((dataCell.dataCellEvaluationStatus.equals(DataCellEvaluationStatus.EXACT_MATCH) && (cellMatchingType.equals(CellMatchingType.EXACT_MATCH) || cellMatchingType.equals(CellMatchingType.CONTAINS_MATCH))) ||
                        (dataCell.dataCellEvaluationStatus.equals(DataCellEvaluationStatus.CONTAINS_MATCH) && cellMatchingType.equals(CellMatchingType.CONTAINS_MATCH)) ||
                        (dataCell.dataCellEvaluationStatus.equals(DataCellEvaluationStatus.REGEX_MATCH) && cellMatchingType.equals(CellMatchingType.REGEX_MATCH))){
                    tableRowEvaluationStatus = TableRowEvaluationStatus.ONLY_MATCHES;
                } else if(dataCell.dataCellEvaluationStatus.equals(DataCellEvaluationStatus.NO_MATCH)){
                    tableRowEvaluationStatus = TableRowEvaluationStatus.NO_MATCH;
                }
            } else if(tableRowEvaluationStatus.equals(TableRowEvaluationStatus.NO_MATCH)){
                if((dataCell.dataCellEvaluationStatus.equals(DataCellEvaluationStatus.EXACT_MATCH) && (cellMatchingType.equals(CellMatchingType.EXACT_MATCH) || cellMatchingType.equals(CellMatchingType.CONTAINS_MATCH))) ||
                        (dataCell.dataCellEvaluationStatus.equals(DataCellEvaluationStatus.CONTAINS_MATCH) && cellMatchingType.equals(CellMatchingType.CONTAINS_MATCH))||
                        (dataCell.dataCellEvaluationStatus.equals(DataCellEvaluationStatus.REGEX_MATCH) && cellMatchingType.equals(CellMatchingType.REGEX_MATCH))){
                    tableRowEvaluationStatus = TableRowEvaluationStatus.BOTH_MATCHES_AND_NON_MATCHES;
                }
            } else if(tableRowEvaluationStatus.equals(TableRowEvaluationStatus.ONLY_MATCHES)){
                if(dataCell.dataCellEvaluationStatus.equals(DataCellEvaluationStatus.NO_MATCH)){
                    tableRowEvaluationStatus = TableRowEvaluationStatus.BOTH_MATCHES_AND_NON_MATCHES;
                }
            }
        }
    }
}
