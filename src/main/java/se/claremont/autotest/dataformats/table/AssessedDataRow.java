package se.claremont.autotest.dataformats.table;

import se.claremont.autotest.common.LogLevel;
import se.claremont.autotest.common.TestCase;
import se.claremont.autotest.support.SupportMethods;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by jordam on 2016-11-09.
 */
class AssessedDataRow {
    private Map<String, EvaluatedCell> dataCells = new LinkedHashMap<>();
    private int rowNumber;
    public RowMatchStatus rowAssessmentStatus = RowMatchStatus.ROW_IS_UNEVALUATED;
    private TestCase testCase;

    public AssessedDataRow(DataRow dataRow, TestCase testCase, Boolean isMatched) {
        this.testCase = testCase;
        rowNumber = dataRow.rowNumber;
        for (String headline : dataRow.dataCells.keySet()) {
            dataCells.put(headline, new EvaluatedCell(dataRow.value(headline), CellMatchStatus.UNEVALUATED));
        }
    }

    public Set<String> headlines(){
        return dataCells.keySet();
    }

    public String value(String headline) {
        String returnString = "";
        if (!headlines().contains(headline)) {
            testCase.logDifferentlyToTextLogAndHtmlLog(LogLevel.VERIFICATION_PROBLEM,
                    "Could not choose headline '" + headline + "'. Found headlines are '" + String.join("', '", dataCells.keySet()) + "'.",
                    "Could not choose headline '" + headline +
                    "'. Found headlines in the table:<br><table><tr><td>" +
                    String.join("</td><td>", dataCells.keySet()) + "</td></tr></table>");
        } else {
            try {
                returnString = dataCells.get(headline).cellData;
            } catch (Exception e) {
                testCase.log(LogLevel.VERIFICATION_PROBLEM, "Something went wrong: " + e.getMessage());
            }
        }
        return returnString;
    }

    public CellMatchStatus isMatched(String headline) {
        CellMatchStatus cellmatching = CellMatchStatus.UNEVALUATED;
        try {
            cellmatching = dataCells.get(headline).cellMatchStatus;
        } catch (Exception e) {
            testCase.logDifferentlyToTextLogAndHtmlLog(LogLevel.VERIFICATION_PROBLEM,
                    "Could not pick headline '" + headline + "'. Available headlines are '" + String.join("', '", dataCells.keySet() + "'."),
                    "Could not pick headline '" + headline +
                    "'. Available headlines in the table are:<br><table><tr><td>" +
                    String.join("</td><td>", dataCells.keySet()) + "</td></tr></table>");
        }
        return cellmatching;
    }

    public @Override String toString() {
        String returnString = "Row number: " + rowNumber + "<br><table><tr><td>" + String.join("</td><td>", headlines()) + "</td><tr></tr>";
        for (String headline : dataCells.keySet()) {
            returnString += "<td>" + headline + ":" + dataCells.get(headline).cellData + " (" + dataCells.get(headline).cellMatchStatus + ")</td>";
        }
        return returnString + "</tr></table>" + rowAssessmentStatus;
    }

    public RowMatchStatus Assess(String headlineColonValueSemicolonSeparatedString, boolean regex) {
        int correctMatches = 0;
        String[] pairsToFind = headlineColonValueSemicolonSeparatedString.trim().split(";");

        for (String pairToFind : pairsToFind) {
            if (headlineColonValueSemicolonSeparatedString == null || !headlineColonValueSemicolonSeparatedString.contains(":")) {
                testCase.log(LogLevel.VERIFICATION_FAILED,
                        "Cannot check '" + pairToFind + "' in the string '" +
                        headlineColonValueSemicolonSeparatedString + "'.");
            }
            if(pairToFind.split(":").length < 2) continue;
            String headline = pairToFind.split(":")[0].trim();
            String dataValue = pairToFind.split(":")[1].trim();
            if (!headline.equals("")) {
                if (regex) {
                    boolean match = SupportMethods.isRegexMatch(value(headline), dataValue);
                    if (match) {
                        correctMatches++;
                        dataCells.get(headline).cellMatchStatus = CellMatchStatus.CORRECT;
                    } else {
                        if (dataCells.containsKey(headline)) {
                            dataCells.get(headline).cellMatchStatus = CellMatchStatus.WRONG;
                        } else {
                            testCase.log(LogLevel.VERIFICATION_PROBLEM,
                                    "Cannot find headline '" + headline + "'. Found headlines: '" +
                                    String.join("', '", headlines()) + "'.");
                        }
                    }
                } else {
                    if (value(headline).trim().contains(dataValue)) {
                        correctMatches++;
                        dataCells.get(headline).cellMatchStatus = CellMatchStatus.CORRECT;
                    } else {
                        if (dataCells.containsKey(headline)) {
                            dataCells.get(headline).cellMatchStatus = CellMatchStatus.WRONG;
                        } else {
                            testCase.log(LogLevel.VERIFICATION_PROBLEM,
                                    "Cannot find headline '" + headline + "'. Found headlines: '" +
                                    String.join("', '", headlines()) + "'.");
                        }
                    }
                }
            }
        }
        if (rowContainsMatchedData() && !rowContainsMismatchedData() && correctMatches == pairsToFind.length) {
            rowAssessmentStatus = RowMatchStatus.ROW_HAS_ONLY_CORRECT_MATCHES;
        } else if (rowContainsMatchedData()) {
            rowAssessmentStatus = RowMatchStatus.ROW_HAS_BOTH_CORRECT_AND_ERRONEUS_DATA;
        } else {
            rowAssessmentStatus = RowMatchStatus.ROW_HAS_ONLY_MISMATCHED_DATA;
        }
        return rowAssessmentStatus;
    }


    public boolean rowContainsMismatchedData() {
        for (String headline : headlines()) {
            if (isMatched(headline) == CellMatchStatus.WRONG) {
                return true;
            }
        }
        return false;
    }

    public boolean rowContainsMatchedData() {
        for (String headline : headlines()) {
            if (isMatched(headline) == CellMatchStatus.CORRECT) {
                return true;
            }
        }
        return false;
    }

    public boolean noCellIsUnmatched() {
        for (String headline : headlines()) {
            if (isMatched(headline) == CellMatchStatus.WRONG) {
                return false;
            }
        }
        return true;
    }

}
