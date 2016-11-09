package se.claremont.autotest.dataformats.table;

import se.claremont.autotest.common.LogLevel;
import se.claremont.autotest.common.TestCase;
import se.claremont.autotest.support.SupportMethods;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jordam on 2016-11-09.
 */
class AssessedTableData {
    private String guiElementName;
    private List<String> headLines;
    private TestCase testCase;
    public List<AssessedDataRow> dataRows;
    boolean shouldTakeScreenshot = false;

    public AssessedTableData(TableData tableData) {
        testCase = tableData.testCase;
        headLines = tableData.headLines;
        dataRows = new ArrayList<AssessedDataRow>();
        guiElementName = tableData.guiElementName;
        if (tableData != null && tableData.dataRows != null) {
            for (DataRow datarad : tableData.dataRows) {
                dataRows.add(new AssessedDataRow(datarad, testCase, null));
            }
        }
    }

    public boolean takeScreenshot() {
        return shouldTakeScreenshot;
    }

    public void debug() {
        String html = "<table>";
        html += "<tr><td>Radstatus</td><td>noCellIsUnmatched</td><td>RadenHarFunnetData</td><td>RadenHarOFunnetData</td><td>Assess() returnerar</td></tr>";
        for (AssessedDataRow dataRow : dataRows) {
            html += "<tr>";
            html += "<td>" + dataRow.rowAssessmentStatus + "</td>";
            html += "<td>" + dataRow.noCellIsUnmatched() + "</td>";
            html += "<td>" + dataRow.rowContainsMatchedData() + "</td>";
            html += "<td>" + dataRow.rowContainsMismatchedData() + "</td>";
            html += "</tr>";
            html += "<tr><td colspan=\"4\"><table>";
            html += "<tr>";
            for (String headline : dataRow.headlines()) {
                html += "<th>" + headline + "</td>";
            }
            html += "</tr><tr>";
            for (String headline : dataRow.headlines()) {
                if (!headline.equals("")) {
                    html += "<td>" + dataRow.value(headline) + ":" + dataRow.isMatched(headline) + "</td>";
                }
            }
            html += "</tr></table></td></tr></table>";
        }
        testCase.logDifferentlyToTextLogAndHtmlLog(LogLevel.DEBUG, html.replace("<br>", SupportMethods.LF), html);
    }

    private String createHtmlForTableHeadlines(String soughtAfterData, String classExtension) {
        String html = "";
        List<String> soughtAfterHeadlines = new ArrayList<>();
        if (soughtAfterData != null) {
            for (String soughtAfterPair : soughtAfterData.split(";")) {
                soughtAfterHeadlines.add(soughtAfterPair.split(":")[0]);
            }
        }

        if (headLines != null && headLines.size() != 0) {
            html += "<tr class=\"table headline " + classExtension + "\">";
            for (String headline : headLines) {
                if (soughtAfterHeadlines.contains(headline) || soughtAfterData == null) {
                    html += "<td class=\"table headline soughtafter" + classExtension + "\">" + headline + "</td>";
                } else {
                    html += "<td class=\"table headline ignored" + classExtension + "\">" + headline + "</td>";
                }
            }
            html += "</tr>";
        }
        return html;
    }


    public String convertAssessedTableDataToHtml(String soughtAfterData, boolean regex, LogLevel logLevel) {
        String classNameExtension = "";
        if (logLevel != null) {
            classNameExtension += " " + logLevel;
        }
        boolean matchExist = numberOfRowsWithRowStatus(RowMatchStatus.ROW_HAS_ONLY_CORRECT_MATCHES) > 0;
        String html = "<table class=\"table padding\">";
        html += "<tr>";
        html += "<td>";
        html += "<table class=\"table data " + classNameExtension + "\">";
        html += createHtmlForTableHeadlines(soughtAfterData, classNameExtension);
        if (dataRows.size() == 0) {
            html += "<tr class=\"table datarow " + classNameExtension +
                    "\"><td class=\"table empty\" colspan=\"" + headLines.size() +
                    "\">Empty table</td></tr>";
        } else {
            for (AssessedDataRow dataRow : dataRows) {
                if (dataRow.rowAssessmentStatus == RowMatchStatus.ROW_HAS_ONLY_CORRECT_MATCHES && matchExist) {
                    html += "<tr class=\"table dataRow perfectmatch " + classNameExtension + "\">";
                } else {
                    html += "<tr class=\"table dataRow imperfectmatch " + classNameExtension + "\">";
                }
                for (String headline : dataRow.headlines()) {
                    if (!headline.equals("")) {
                        String cellContent = dataRow.value(headline);

                        if ((matchExist && dataRow.rowAssessmentStatus == RowMatchStatus.ROW_HAS_ONLY_CORRECT_MATCHES &&
                                dataRow.isMatched(headline) == CellMatchStatus.CORRECT) ||
                                (!matchExist &&
                                        dataRow.isMatched(headline) == CellMatchStatus.CORRECT &&
                                        dataRow.rowAssessmentStatus != RowMatchStatus.ROW_HAS_ONLY_MISMATCHED_DATA)) {
                            if (cellContent.equals("")) {
                                html +=
                                        "<td class=\"table datacell matched emptycell " +
                                                classNameExtension + "\">-</td>";
                            } else {
                                html += "<td class=\"table datacell matchedcell " +
                                        classNameExtension + "\">" + cellContent + "</td>";
                            }
                        }
                        else if (dataRow.rowAssessmentStatus == RowMatchStatus.ROW_HAS_BOTH_CORRECT_AND_ERRONEUS_DATA &&
                                dataRow.isMatched(headline) == CellMatchStatus.WRONG &&
                                !matchExist) {
                            if (cellContent.equals("")) {
                                html +=
                                        "<td class=\"table datacell mismatched emptycell " +
                                                classNameExtension + "\">-</td>";
                            } else {
                                html += "<td class=\"table datacell mismatched " +
                                        classNameExtension + "\">" + cellContent + "</td>";
                            }
                        }
                        else if ((matchExist && dataRow.rowAssessmentStatus == RowMatchStatus.ROW_HAS_ONLY_CORRECT_MATCHES &&
                                dataRow.isMatched(headline) == CellMatchStatus.UNEVALUATED) ||
                                (!matchExist && dataRow.rowAssessmentStatus != RowMatchStatus.ROW_HAS_ONLY_MISMATCHED_DATA &&
                                        dataRow.isMatched(headline) == CellMatchStatus.UNEVALUATED)) {
                            if (cellContent.equals("")) {
                                html +=
                                        "<td class=\"table datacell matchedcellsonrowbutnotthiscell emptycell " +
                                                classNameExtension + "\">_Tom_cell_</td>";
                            } else {
                                html += "<td class=\"table datacell matchedcellonrowbutnotthiscell " +
                                        classNameExtension + "\">" + cellContent + "</td>";
                            }
                        } else {
                            if (cellContent.equals("")) {
                                html += "<td bgcolor=\"white\" class=\"table datacell matchedignored emptycell " +
                                        classNameExtension + "\">-</td>";
                            } else {
                                html += "<td bgcolor=\"white\" class=\"table datacell matchedignored " + classNameExtension +
                                        "\">" + cellContent + "</td>";
                            }
                        }
                    }
                }
                html += "</tr>";
            }
        }
        html += "</table></td></tr></table>";
        if (testCase != null && soughtAfterData != null) {
            if (matchExist) {
                if (regex) {
                    testCase.logDifferentlyToTextLogAndHtmlLog(LogLevel.VERIFICATION_PASSED,
                        "Found sought after data in " + guiElementName + ". Sought after data (regex=true): " + soughtAfterData + ".",
                        "Found sought after data in " + guiElementName + ".<br>Sought after data (regex='true'):<br>" +
                        TableData.headlineColonValueSemicolonSeparatedStringToHtml(soughtAfterData, guiElementName, testCase, LogLevel.VERIFICATION_PASSED) +
                        "<br>Found data in " + guiElementName +
                        ":<br>" + html);
                } else {
                    testCase.logDifferentlyToTextLogAndHtmlLog(LogLevel.VERIFICATION_PASSED,
                            "Found sought after data in " + guiElementName + ". Sought after data (regex=false): " + soughtAfterData + ".",
                            "Found sought after data in " + guiElementName + ".<br>Sought after data (regex='false'):<br>" +
                            TableData.headlineColonValueSemicolonSeparatedStringToHtml(soughtAfterData, guiElementName,
                                    testCase, LogLevel.VERIFICATION_PASSED) + "<br>Found data in " + guiElementName +
                            ":<br>" + html);
                }
            } else {
                testCase.logDifferentlyToTextLogAndHtmlLog(LogLevel.VERIFICATION_FAILED,
                        "Could not find sought after data '" + soughtAfterData + "' in " + guiElementName + ". ",
                        "Could not find sought after data in " + guiElementName + ".<br>Sought after data (regex='" +
                        Boolean.toString(regex).toLowerCase() + "'):<br>" +
                        TableData.headlineColonValueSemicolonSeparatedStringToHtml(soughtAfterData, guiElementName, testCase,
                        LogLevel.VERIFICATION_FAILED) + "<br>Found data in " + guiElementName + ":<br>" + html);
            }
        }
        return html;
    }

    public List<Integer> rowNumbersForRowsWithMatches() {
        List<Integer> rowNumberList = new ArrayList<>();
        for (AssessedDataRow assessedDataRow : dataRows) {
            if (assessedDataRow.noCellIsUnmatched()) {
                rowNumberList.add(dataRows.indexOf(assessedDataRow));
            }
        }
        return rowNumberList;
    }

    int numberOfRowsWithRowStatus(RowMatchStatus rowMatchStatus) {
        int matchCount = 0;
        for (AssessedDataRow dataRow : dataRows) {
            if (dataRow.rowAssessmentStatus == rowMatchStatus) {
                matchCount++;
            }
        }
        return matchCount;
    }

}
