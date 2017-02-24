package se.claremont.autotest.common.support.tableverification;

import se.claremont.autotest.common.support.SupportMethods;

/**
 * Created by jordam on 2017-01-28.
 */
@SuppressWarnings("WeakerAccess")
class DataCell {
    String correspontingHeadline;
    String dataContent;
    DataCellEvaluationStatus dataCellEvaluationStatus = DataCellEvaluationStatus.UNEVALUATED;

    DataCell(String dataContent, String correspontingHeadline){
        this.correspontingHeadline = correspontingHeadline.trim();
        this.dataContent = dataContent.trim();
    }

    boolean isExactMatch(String dataContent, String correspontingHeadline){
        return (
                this.dataContent.equals(dataContent.trim()) &&
                this.correspontingHeadline.equals(correspontingHeadline.trim()));
    }

    boolean isContainsMatch(String dataContent, String correspontingHeadline){
        return (
                this.dataContent.contains(dataContent.trim()) &&
                this.correspontingHeadline.contains(correspontingHeadline.trim()));
    }

    boolean isRegexMatch(String dataContent, String correspontingHeadline){
        return (SupportMethods.isRegexMatch(this.dataContent, dataContent.trim()) &&
                SupportMethods.isRegexMatch(this.correspontingHeadline, correspontingHeadline.trim()));
    }

    String toHtml(){
        if(dataCellEvaluationStatus.equals(DataCellEvaluationStatus.NO_MATCH)){
            return "      <td class=\"no_match\">" + dataContent + "</td>" + System.lineSeparator();
        } else if(dataCellEvaluationStatus.equals(DataCellEvaluationStatus.CONTAINS_MATCH)){
            return "      <td class=\"contains_match\">" + dataContent + "</td>" + System.lineSeparator();
        } else if(dataCellEvaluationStatus.equals(DataCellEvaluationStatus.EXACT_MATCH)){
             return "      <td class=\"exact_match\">" + dataContent + "</td>" + System.lineSeparator();
        }else if(dataCellEvaluationStatus.equals(DataCellEvaluationStatus.REGEX_MATCH)){
            return "      <td class=\"regex_match\">" + dataContent + "</td>" + System.lineSeparator();
        } else if(dataCellEvaluationStatus.equals(DataCellEvaluationStatus.UNEVALUATED)) {
            return "      <td class=\"unevaluated\">" + dataContent + "</td>" + System.lineSeparator();
        }
        return "      <td class=\"other\">" + dataContent + "</td>" + System.lineSeparator();
    }

    void performMatching(String dataContent, String correspontingHeadline){
        if(isExactMatch(dataContent, correspontingHeadline)) {
            this.dataCellEvaluationStatus = DataCellEvaluationStatus.EXACT_MATCH;
            return;
        }
        if(isContainsMatch(dataContent, correspontingHeadline)){
            this.dataCellEvaluationStatus = DataCellEvaluationStatus.CONTAINS_MATCH;
            return;
        }
        if(isRegexMatch(dataContent, correspontingHeadline)){
            this.dataCellEvaluationStatus = DataCellEvaluationStatus.REGEX_MATCH;
            return;
        }
        this.dataCellEvaluationStatus = DataCellEvaluationStatus.NO_MATCH;
    }

    void resetStatus() {
        dataCellEvaluationStatus = DataCellEvaluationStatus.UNEVALUATED;
    }
}
