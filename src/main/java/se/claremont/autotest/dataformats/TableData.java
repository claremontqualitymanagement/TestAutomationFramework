package se.claremont.autotest.dataformats;

import se.claremont.autotest.common.LogLevel;
import se.claremont.autotest.common.TestCase;
import se.claremont.autotest.common.UxColors;
import se.claremont.autotest.guidriverpluginstructure.swingsupport.robotswinggluecode.RobotSwingInteractionMethods;
import se.claremont.autotest.support.SupportMethods;

import java.util.*;

/**
 * This class is for verifying table structured data, in tables with a headline.
 * Some limitations exist, but could probably be coded away:
 * Since row delimiter is newline character, cell contents with linebreaks might ruin results.
 * In the same manner: Since cell delimiter is semicolon, cell content with semicolon in it will break results.
 * Creating a more generic structure, and escaping these characters in construction, would fix this. However,
 * this has not yet proven needed.
 *
 * Created by jordam on 2016-11-09.
 */
public class TableData {
    String tableData;
    String guiElementName;
    List<DataRow> dataRows = new ArrayList<>();
    TestCase testCase;
    List<String> headLines;

    /**
     * A table is defined with table rows separated by linebreak and cells separated by ';' (semicolon).
     * The first row will contain the headings used.
     *
     * @param tableData Table data in the format of
     *                  'Heading1;Heading2;Heading3\n
     *                  DataRow1Cell1;DataRow1Cell2;DataRow1Cell3\n
     *                  DataRow2Cell1;DataRow2Cell2;DataRow3Cell3'
     *                  and so forth.
     * @param guiElementName The name of the GUI element, for logging purposes.
     * @param testCase The test case to log to.
     * @param log Boolean parameter to state if logging should occur.
     */
    public TableData(String tableData, String guiElementName, TestCase testCase, boolean log) {
        this.testCase = testCase;
        this.tableData = tableData;
        this.guiElementName = guiElementName;
        reportInitially(log);
        String[] rows = tableData.split(SupportMethods.LF);
        if (rows.length == 0) return;
        createHeadLineRow(rows[0]);
        if (rows.length == 1) return;
        for (int rowNumber = 1; rowNumber < rows.length; rowNumber++) {
            createDataRow(rows[rowNumber], rowNumber);
        }
        testCase.logDifferentlyToTextLogAndHtmlLog(LogLevel.DEBUG, toString(), toHtml(LogLevel.DEBUG));
    }


    /**
     * The table in html format output.
     *
     * @param logLevel Logging level
     * @return HTML formatted table as string
     */
    public String toHtml(LogLevel logLevel)
    {
        AssessedTableData assessedTableData = new AssessedTableData(this);
        String html = assessedTableData.convertAssessedTableDataToHtml(null, false, logLevel);
        if (assessedTableData.takeScreenshot()) {
            RobotSwingInteractionMethods r = new RobotSwingInteractionMethods(testCase);
            r.captureScreenshot();
        }
        return html;
    }

    public @Override String toString() {
        List<String> returnRows = new ArrayList<>();
        if (headLines == null || headLines.size() == 0) return "";
        returnRows.add(String.join(";", headLines));
        if (dataRows != null) {
            for (DataRow dataRow : dataRows) {
                List<String> cells = new ArrayList<>();
                for (String headline : headLines){
                    cells.add(dataRow.value(headline));
                }
                returnRows.add(String.join(";", cells));
            }
        }
        return String.join(SupportMethods.LF, returnRows);
    }

    /**
     * Checks if row with information matching request is found in table data.
     *
     * @param headlineColonValueSemicolonSeparatedString The data to match, formatted according to: 'Headline:DataToFind;OtherHeadline:CorrespondingDataToFind'.
     * @param regex Set to true for regular expression pattern, false will check if cells containing the data is found.
     * @param expectedMatchesCount If more than one match is expected.
     * @return Returns true if data is found.
     */
    public boolean rowExists(String headlineColonValueSemicolonSeparatedString, boolean regex, Integer expectedMatchesCount) {
        boolean rowFound = false;
        AssessedTableData assessedData = assess(headlineColonValueSemicolonSeparatedString, regex, false);
        if (expectedMatchesCount != null) {
            if (assessedData.numberOfRowsWithRowStatus(AssessedTableData.AssessedDataRow.RowMatchStatus.ROW_HAS_ONLY_CORRECT_MATCHES) == expectedMatchesCount) {
                rowFound = true;
            }
        } else if ( assessedData.numberOfRowsWithRowStatus(AssessedTableData.AssessedDataRow.RowMatchStatus.ROW_HAS_ONLY_CORRECT_MATCHES) > 0) {
            rowFound = true;
        }
        testCase.logDifferentlyToTextLogAndHtmlLog(LogLevel.DEBUG, "Examined if the data '" + headlineColonValueSemicolonSeparatedString + "' existed in the table in " + guiElementName +
            SupportMethods.LF + headlineColonValueSemicolonSeparatedStringToTableData(headlineColonValueSemicolonSeparatedString,
            guiElementName, testCase).toString() + SupportMethods.LF + "Results: '" +
            Boolean.toString(rowFound).toLowerCase().replace("true", "Yes, it exists.").replace("false", "No, it does not exist."),
            "Examined if the data below existed in the table in " + guiElementName + "<br>" +
            headlineColonValueSemicolonSeparatedStringToTableData(headlineColonValueSemicolonSeparatedString,
            guiElementName, testCase).toHtml(LogLevel.DEBUG) + "<br>Results: '" +
            Boolean.toString(rowFound).toLowerCase().replace("true", "Yes, it exists.").replace("false", "No, it does not exist."));
        return rowFound;
    }


    /**
     * Getting the value for the headline among the row data.
     *
     * @param headlineName The name of the headline.
     * @param headlineColonValueSemicolonSeparatedStringThatIdentifiesTheTableRow The identifying pattern to identify the string.
     * @param regex True if the matching pattern for data value is a regular expression, othervice it will be checked for cells containing this data.
     * @return Returns the content of the cell matching the row identiyer and the headline.
     */
    public String getValue(String headlineName,
                           String headlineColonValueSemicolonSeparatedStringThatIdentifiesTheTableRow,
                           boolean regex) {
        String returnString = null;
        if (headlineName == null || headlineName.length() == 0) {
            testCase.log(LogLevel.INFO,
                    "Skips TableData.getValue() as the given headline is " +
                    Boolean.toString(headlineName == null).toLowerCase().replace("true", "null").
                    replace("false", "empty") + ".");
            return returnString;
        }
        AssessedTableData assessedData = assess(headlineColonValueSemicolonSeparatedStringThatIdentifiesTheTableRow, regex, false);
        if (assessedData.numberOfRowsWithRowStatus(AssessedTableData.AssessedDataRow.RowMatchStatus.ROW_HAS_ONLY_CORRECT_MATCHES) != 1) {
            testCase.log(LogLevel.VERIFICATION_FAILED,
                "Tried getting the value for headline '" + headlineName + "' in the table row that could be identified by '" +
                headlineColonValueSemicolonSeparatedStringThatIdentifiesTheTableRow +
                "' , and expected exactly one match, but found " +
                assessedData.numberOfRowsWithRowStatus(AssessedTableData.AssessedDataRow.RowMatchStatus.ROW_HAS_ONLY_CORRECT_MATCHES) +
                " matches.");
        } else {
            if (headLines.contains(headlineName)) {
                try {
                    returnString = dataRows.get(assessedData.rowNumbersForRowsWithMatches().get(0)).value(headlineName);
                } catch (Exception e) {
                    testCase.log(LogLevel.VERIFICATION_PROBLEM, "Something went wrong in TableData.getValue(): " + e.getMessage());
                }
            } else {
                testCase.log(LogLevel.VERIFICATION_PROBLEM,
                    "Could not pick the headline '" + headlineName + "' in the table of " + guiElementName + ". Headlines found were '" + String.join("', '", headLines) + "'.");
            }
        }
        return returnString;
    }

    /**
     * Checks if the table data corresponds with an empty table. A table with headlines is not empty.
     *
     * @return Returns true if the table is empty, othervice false.
     */
    public boolean tableIsEmpty() {
        if (guiElementName == null || guiElementName.length() == 0) {
            testCase.log(LogLevel.DEBUG,
                "Checked if the table in " + guiElementName + " was empty and " +
                Boolean.toString(dataRows.size() == 0).toLowerCase().replace("true", "it was.").replace("false", "it was not."));
        }
        return dataRows.size() == 0;
    }

    /**
     * Returns the value for the stated headline. Assuming only one data row in table.
     *
     * @param headlineName The name of the headline.
     * @return Returns the data cell content.
     */
    public String getValueForHeadlineInTableData(String headlineName) {
        String returnValue = null;
        if (dataRows.size() != 1) {
            testCase.log(LogLevel.VERIFICATION_PROBLEM,
                "Cannot get data for headline '" + headlineName + "' in the table of " + guiElementName +
                " since it un-expectedly matched with multiple rows.");
            testCase.logDifferentlyToTextLogAndHtmlLog(LogLevel.DEVIATION_EXTRA_INFO, "The table that triggered the error had the following content:" +
                SupportMethods.LF + toString(),
                "The table that triggered the error had the following content:<br>" + toHtml(LogLevel.DEVIATION_EXTRA_INFO));
        } else {
            if (headLines.contains(headlineName)) {
                try {
                    returnValue = dataRows.get(0).value(headlineName);
                    testCase.log(LogLevel.DEBUG,
                        "getting value '" + returnValue + "' for headline '" + headlineName + "' in the table of " +
                        guiElementName + ".");
                } catch (Exception e) {
                    testCase.log(LogLevel.VERIFICATION_PROBLEM,
                        "Something went wrong while trying to get the value of headline '" + headlineName +
                        "' in TableData.getValueForHeadlineInTableData(): " + e.getMessage());
                }
            } else {
                testCase.log(LogLevel.VERIFICATION_PROBLEM,
                    "Could not get the value of headline '" + headlineName + "' in the table of " + guiElementName +
                    " since the headline did not exist. Headlines were '" + String.join("', '", headLines) + "'.");
            }
        }
        return returnValue;
    }


    /**
     * Verify that the table data containst the sought after values.
     *
     * @param headlineColonValueSemicolonSeparatedString The values to find, in a format of 'Heading1:Value1;Heading3:Value3' if all values are matched the test is passed.
     * @param regex Boolean value to state if the value is a regex value or a if the data should be matched with a 'contains' instead.
     */
    public void verifyRow(String headlineColonValueSemicolonSeparatedString, boolean regex) {
        assess(headlineColonValueSemicolonSeparatedString, regex, true);
    }

    /**
     * Logs an error if stated headline does not exist.
     *
     * @param expectedHeadline Headline name.
     */
    public void verifyHeadlineExist(String expectedHeadline){
        if(headLines.contains(expectedHeadline)){
            String tableHeadlines = "<table class=\"table padding\"><tr>";
            for(String headLine : headLines){
                if(headLine.contains(expectedHeadline)){
                    tableHeadlines += "<td style=\"background-color: lightgreen; color: black; font-weight: bold;\">" + headLine + "</td>";
                } else {
                    tableHeadlines += "<td style=\"background-color: lightgrey; color: darkgrey; font-weight: bold;\">" + headLine + "</td>";
                }
            }
            tableHeadlines += "</tr>";
            tableHeadlines += "<tr><td style=\"background-color: cornsilk;\" colspan=\"" + headLines.size() + "\"><i>Table data</i></td></tr></table>";
            testCase.logDifferentlyToTextLogAndHtmlLog(LogLevel.VERIFICATION_PASSED, "The headline '" + expectedHeadline + "' exist in the table " + guiElementName + ". All headlines '" + String.join("', '", headLines) + "'.",
                    "Verified that headline '" + expectedHeadline + "' existed amoing the table headlines.<br>" + tableHeadlines);
        } else{
            String tableHeadlines = "<table class=\"table padding\"><tr>";
            for(String headLine : headLines){
                tableHeadlines += "<td style=\"background-color: lightgrey; color: darkgrey; font-weight: bold;\">" + headLine + "</td>";
            }
            tableHeadlines += "</tr>";
            tableHeadlines += "<tr><td style=\"background-color: cornsilk;\" colspan=\"" + headLines.size() + "\"><i>Table data</i></td></tr></table>";
            testCase.logDifferentlyToTextLogAndHtmlLog(LogLevel.VERIFICATION_FAILED, "The headline '" + expectedHeadline + "' does not exist in the table " + guiElementName + ". Found headlines are '" + String.join("', '", headLines) +"'.",
                    "Could not find the headline '" + expectedHeadline + "' among the headlines of " + guiElementName + "<br>" + tableHeadlines);
        }
    }

    /**
     * Checking table for multiple headlines.
     *
     * @param headLinesToFind List of headlines expected to be present.
     */
    public void verifyHeadlinesExist(List<String> headLinesToFind){
        List<String> foundHeadlines = new ArrayList<>();
        List<String> notFoundHeadlines = new ArrayList<>();
        for(String headline : headLinesToFind){
            if(headLines.contains(headline)){
                foundHeadlines.add(headline);
            } else {
                notFoundHeadlines.add(headline);
            }
        }
        String message = "The actual headlines in " + guiElementName + " were '" + String.join("', '", headLines) + "'.";
        if(notFoundHeadlines.size() > 0){
            message += "Cannot find headline(s) '" + String.join("', '", notFoundHeadlines) + "'. ";
        }
        if(foundHeadlines.size() > 0){
            message += "The headline(s) '" + String.join("', ", foundHeadlines) + "' could be found. ";
        }
        if(notFoundHeadlines.size() > 0){
            testCase.log(LogLevel.VERIFICATION_FAILED, message);
        } else {
            testCase.log(LogLevel.VERIFICATION_PASSED, message);
        }
    }

    /**
     * Verify that the table data containst the sought after values.
     *
     * @param headlineColonValueSemicolonSeparatedStrings The values to find, in a format of 'Heading1:Value1;Heading3:Value3' if all strings are matched the test is passed.
     * @param regex Boolean value to state if the value is a regex value or a if the data should be matched with a 'contains' instead.
     */
    public void verifyRows(String[] headlineColonValueSemicolonSeparatedStrings, boolean regex) {
        for (String headlineColonValueSemicolonSeparatedString : headlineColonValueSemicolonSeparatedStrings) {
            assess(headlineColonValueSemicolonSeparatedString, regex, true);
        }
    }

    /**
     * Formatting info
     *
     * @param headlineColonValueSemicolonSeparatedString
     * @param guiElementName
     * @param testCase
     * @param logLevel
     * @return A string formatted for HTML
     */
    static String headlineColonValueSemicolonSeparatedStringToHtml(
            String headlineColonValueSemicolonSeparatedString,
            String guiElementName,
            TestCase testCase,
            LogLevel logLevel) {
        return
                headlineColonValueSemicolonSeparatedStringToTableData(
                        headlineColonValueSemicolonSeparatedString,
                        guiElementName,
                        testCase).toHtml(logLevel);
    }

    private void reportInitially(boolean log){
        if (guiElementName != null && !guiElementName.trim().equals("") && log) {
            testCase.logDifferentlyToTextLogAndHtmlLog(LogLevel.DEBUG, "Creating TableData for " + guiElementName  + " from data: " + SupportMethods.LF + tableData,
                    "Creating TableData for " + guiElementName + " from data:<br>" +
                            tableData.replace(SupportMethods.LF, "<br>").replace("\n", "<br>"));
        }
        if (tableData == null) {
            testCase.log(LogLevel.DEBUG, "TableData for " + guiElementName + " is null. Making it ''.");
            tableData = "";
        }
    }

    private void createHeadLineRow(String topRow) {
        String[] headLines = topRow.split(";");
        List<String> headLineList = new ArrayList<>();
        for (String headLine : headLines) {
            if (headLine.length() < 1) continue;
            headLineList.add(headLine.trim());
        }
        this.headLines = headLineList;
    }

    private void createDataRow(String dataRow, int rowNumber) {
        String[] rowData = dataRow.split(";");
        if (rowData.length < 2 && rowData[0].trim().equals("")) return; //Empty row (blank last row)
        List<String> cells = new ArrayList<>();
        for (String cell : rowData) {
            cells.add(cell.trim());
        }

        if (headLines.size() <= cells.size()) {
            dataRows.add(new DataRow(headLines, cells, rowNumber));
        } else {
            testCase.logDifferentlyToTextLogAndHtmlLog(LogLevel.DEBUG,
                "public TableData():" + SupportMethods.LF + "It seems like the headline row ['" + String.join("', '", headLines) +
                "'] has " + headLines.size() + " items while the row data ['" + String.join("', '", cells) +
                "'] has " + cells.size()+
                " items. Not equal. Confusing. Skipping this row.",
                "public TableData():<br>It seems like the head line count for head line row<br>['" + String.join("', '", headLines) +
                "']<br>has " + headLines.size() + " items, while the row data<br>['" + String.join("', '", cells) +
                "']<br>has " + cells.size() +
                " items. Not equal. Confusing. Skipping this row.");
        }
    }


    private TableData createTableDataObjectFromDataRow(DataRow dataRow, String guiElementName, TestCase testCase) {
        Set<String> headlines = dataRow.dataCells.keySet();
        String tableDataString = String.join(";", headlines) + SupportMethods.LF + dataRow.toString();
        return new TableData(tableDataString, guiElementName, testCase, false);
    }



    private static TableData headlineColonValueSemicolonSeparatedStringToTableData(
            String headlineColonValueSemicolonSeparatedString, String guiElementName, TestCase testCase) {
        List<String> headlines = new ArrayList<>();
        List<String> datavalues = new ArrayList<>();
        String[] thePairs = headlineColonValueSemicolonSeparatedString.split(";");
        for (String pair : thePairs) {
            if(pair.split(":").length > 1){
                headlines.add(pair.split(":")[0]);
                datavalues.add(pair.split(":")[1]);
            } else {
                testCase.log(LogLevel.DEBUG, "Could not create TableData for pair '" + pair + "'.");
            }
        }
        return new TableData(String.join(";", headlines) + SupportMethods.LF + String.join(";", datavalues),
                guiElementName, testCase, false);
    }

    private boolean headlinesFromHeadlineColonValueSemicolonSeparatedStringExists(
            String headlineColonValueSemicolonSeparatedString) {
        boolean allHeadlinesExists = true;
        if (headlineColonValueSemicolonSeparatedString == null)  return false;
        String[] pairsToFind = headlineColonValueSemicolonSeparatedString.split(";");
        for (String pairToFind : pairsToFind) {
            boolean headlineExist = false;
            if (pairToFind.indexOf(':') > 0) {
                String soughtAfterHeadline = pairToFind.split(":")[0];
                for (String headlineInActualTable : headLines) {
                    if (soughtAfterHeadline.equals(headlineInActualTable)) {
                        headlineExist = true;
                        break;
                    }
                }
                if (!headlineExist) {
                    allHeadlinesExists = false;
                    testCase.log(LogLevel.INFO,
                        "No match for headline '" + soughtAfterHeadline + "' in the table of " + guiElementName +
                        ". Headlines found: '" + String.join("', '", headLines) + "'.");
                }
            }
        }
        return allHeadlinesExists;
    }

    private AssessedTableData assess(String headlineColonValueSemicolonSeparatedString, boolean regex, boolean logResults) {
        if (headlineColonValueSemicolonSeparatedString == null || headlineColonValueSemicolonSeparatedString.length() == 0) {
            testCase.log(LogLevel.INFO,
                "Attempting assessing the content of the table against the headlineColonValueSemicolonSeparatedString that is " +
                Boolean.toString(headlineColonValueSemicolonSeparatedString == null)
                .toLowerCase().replace("true", "null").replace("false", "empty") + ". Hence attempting assessing top row.");
            headlineColonValueSemicolonSeparatedString = "Rowcount:1";
        }
        AssessedTableData assessableData = new AssessedTableData(this);
        headlinesFromHeadlineColonValueSemicolonSeparatedStringExists(headlineColonValueSemicolonSeparatedString);
        for (AssessedTableData.AssessedDataRow rowToAssess : assessableData.dataRows) {
            rowToAssess.Assess(headlineColonValueSemicolonSeparatedString, regex);
        }
        if (logResults) {
            assessableData.convertAssessedTableDataToHtml(headlineColonValueSemicolonSeparatedString, regex, LogLevel.DEBUG);
        }
        return assessableData;
    }

    /**
     * Created by jordam on 2016-11-09.
     */
    public static class TableVerifierLoggingHtmlStyles {

        public static String styles(){
            String htmlStyles = "      table.table.padding                               { background-color: " + UxColors.MID_GREY.getHtmlColorCode()+ "; border: 8px solid " + UxColors.MID_GREY.getHtmlColorCode() + "; }" + SupportMethods.LF;
            htmlStyles += "      table.table.data                                  { background-color: white; border: 1px solid " + UxColors.MID_GREY.getHtmlColorCode() + "; }" + SupportMethods.LF;
            htmlStyles += "      tr.table.headline                                 { background-color: " + UxColors.MID_GREY.getHtmlColorCode() + "; border: 1px solid " + UxColors.MID_GREY.getHtmlColorCode() + "; }" + SupportMethods.LF;
            htmlStyles += "      td.table                                          { border: 3px solid " + UxColors.MID_GREY.getHtmlColorCode() + "; border-collapse: collapse; }" + SupportMethods.LF;
            htmlStyles += "      th.table                                          { border: 3px solid " + UxColors.DARK_GREY.getHtmlColorCode() + "; border-collapse: collapse; }" + SupportMethods.LF;
            htmlStyles += "      table.table                                          { border: 3px solid " + UxColors.DARK_GREY.getHtmlColorCode() + "; border-collapse: collapse; }" + SupportMethods.LF;
            htmlStyles += "      td.table.headline.soughtafter                     { font-weight: bold; color: " + UxColors.BLACK.getHtmlColorCode() + "; }" + SupportMethods.LF;
            htmlStyles += "      td.table.headline.ignored                         { font-weight: bold; color: " + UxColors.DARK_GREY.getHtmlColorCode() + "; }" + SupportMethods.LF;
            htmlStyles += "      tr.table.dataRow.perfectmatch                     { background-color: " + UxColors.GREEN.getHtmlColorCode() + "; }" + SupportMethods.LF;
            htmlStyles += "      tr.table.dataRow.imperfectmatch                   { background-color: " + UxColors.YELLOW.getHtmlColorCode() + "; }" + SupportMethods.LF;
            htmlStyles += "      td.table.datacell.matchedcellonrowbutnotthiscell  { background-color: " + UxColors.YELLOW.getHtmlColorCode() + "; }" + SupportMethods.LF;
            htmlStyles += "      td.table.datacell.matchedcellsonrowbutnotthiscell { background-color: " + UxColors.YELLOW.getHtmlColorCode() + "; }" + SupportMethods.LF;
            htmlStyles += "      td.table.datacell.mismatched                      { background-color: " + UxColors.RED.getHtmlColorCode() + "; }" + SupportMethods.LF;
            htmlStyles += "      td.table.datacell.matchedcell                     { background-color: " + UxColors.GREEN.getHtmlColorCode() + "; font-weight: bold; }" + SupportMethods.LF;
            htmlStyles += "      td.table.datacell.matchedignored                  { background-color: white; color: " + UxColors.MID_GREY.getHtmlColorCode() + "; }" + SupportMethods.LF;
            return htmlStyles;
        }
    }

    /**
     * Created by jordam on 2016-11-09.
     */
    static class DataRow {
        public Map<String, String> dataCells;
        public int rowNumber;

        public DataRow(List<String> headLines, List<String> dataCells, int rowNumber) {
            this.rowNumber = rowNumber;
            this.dataCells = new LinkedHashMap<>();
            if (dataCells != null && headLines != null && dataCells.size() >= headLines.size())
            {
                for (int cellNumber = 0; cellNumber < headLines.size(); cellNumber++)
                {
                    this.dataCells.put(headLines.get(cellNumber), dataCells.get(cellNumber).trim());
                }
            }
        }

        public @Override String toString() {
            List<String> dataRowString = new ArrayList<>();
            Set<String> headlines = dataCells.keySet();
            for (String headline : headlines) {
                dataRowString.add(dataCells.get(headline));
            }
            return String.join(";", dataRowString);
        }

        public String value(String headline) {
            String returnValue = "";
            try {
                returnValue = dataCells.get(headline);
            } catch (Exception e) {
                returnValue = "--- VARNING: No headline '" + headline + "' found for row --- " + e.getMessage();
            }
            return returnValue;
        }
    }

    /**
     * Created by jordam on 2016-11-09.
     */
    static class AssessedTableData {
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
            boolean matchExist = numberOfRowsWithRowStatus(AssessedDataRow.RowMatchStatus.ROW_HAS_ONLY_CORRECT_MATCHES) > 0;
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
                    if (dataRow.rowAssessmentStatus == AssessedDataRow.RowMatchStatus.ROW_HAS_ONLY_CORRECT_MATCHES && matchExist) {
                        html += "<tr class=\"table dataRow perfectmatch " + classNameExtension + "\">";
                    } else {
                        html += "<tr class=\"table dataRow imperfectmatch " + classNameExtension + "\">";
                    }
                    for (String headline : dataRow.headlines()) {
                        if (!headline.equals("")) {
                            String cellContent = dataRow.value(headline);

                            if ((matchExist && dataRow.rowAssessmentStatus == AssessedDataRow.RowMatchStatus.ROW_HAS_ONLY_CORRECT_MATCHES &&
                                    dataRow.isMatched(headline) == AssessedDataRow.CellMatchStatus.CORRECT) ||
                                    (!matchExist &&
                                            dataRow.isMatched(headline) == AssessedDataRow.CellMatchStatus.CORRECT &&
                                            dataRow.rowAssessmentStatus != AssessedDataRow.RowMatchStatus.ROW_HAS_ONLY_MISMATCHED_DATA)) {
                                if (cellContent.equals("")) {
                                    html +=
                                            "<td class=\"table datacell matched emptycell " +
                                                    classNameExtension + "\">-</td>";
                                } else {
                                    html += "<td class=\"table datacell matchedcell " +
                                            classNameExtension + "\">" + cellContent + "</td>";
                                }
                            }
                            else if (dataRow.rowAssessmentStatus == AssessedDataRow.RowMatchStatus.ROW_HAS_BOTH_CORRECT_AND_ERRONEUS_DATA &&
                                    dataRow.isMatched(headline) == AssessedDataRow.CellMatchStatus.WRONG &&
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
                            else if ((matchExist && dataRow.rowAssessmentStatus == AssessedDataRow.RowMatchStatus.ROW_HAS_ONLY_CORRECT_MATCHES &&
                                    dataRow.isMatched(headline) == AssessedDataRow.CellMatchStatus.UNEVALUATED) ||
                                    (!matchExist && dataRow.rowAssessmentStatus != AssessedDataRow.RowMatchStatus.ROW_HAS_ONLY_MISMATCHED_DATA &&
                                            dataRow.isMatched(headline) == AssessedDataRow.CellMatchStatus.UNEVALUATED)) {
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
                            headlineColonValueSemicolonSeparatedStringToHtml(soughtAfterData, guiElementName, testCase, LogLevel.VERIFICATION_PASSED) +
                            "<br>Found data in " + guiElementName +
                            ":<br>" + html);
                    } else {
                        testCase.logDifferentlyToTextLogAndHtmlLog(LogLevel.VERIFICATION_PASSED,
                                "Found sought after data in " + guiElementName + ". Sought after data (regex=false): " + soughtAfterData + ".",
                                "Found sought after data in " + guiElementName + ".<br>Sought after data (regex='false'):<br>" +
                                headlineColonValueSemicolonSeparatedStringToHtml(soughtAfterData, guiElementName,
                                        testCase, LogLevel.VERIFICATION_PASSED) + "<br>Found data in " + guiElementName +
                                ":<br>" + html);
                    }
                } else {
                    testCase.logDifferentlyToTextLogAndHtmlLog(LogLevel.VERIFICATION_FAILED,
                            "Could not find sought after data '" + soughtAfterData + "' in " + guiElementName + ". ",
                            "Could not find sought after data in " + guiElementName + ".<br>Sought after data (regex='" +
                            Boolean.toString(regex).toLowerCase() + "'):<br>" +
                            headlineColonValueSemicolonSeparatedStringToHtml(soughtAfterData, guiElementName, testCase,
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

        int numberOfRowsWithRowStatus(AssessedDataRow.RowMatchStatus rowMatchStatus) {
            int matchCount = 0;
            for (AssessedDataRow dataRow : dataRows) {
                if (dataRow.rowAssessmentStatus == rowMatchStatus) {
                    matchCount++;
                }
            }
            return matchCount;
        }

        /**
         * Created by jordam on 2016-11-09.
         */
        static class AssessedDataRow {
            private Map<String, EvaluatedCell> dataCells = new LinkedHashMap<>();
            private int rowNumber;
            public RowMatchStatus rowAssessmentStatus = RowMatchStatus.ROW_IS_UNEVALUATED;
            private TestCase testCase;

            public AssessedDataRow(DataRow dataRow, TestCase testCase, Boolean isMatched) {
                this.testCase = testCase;
                rowNumber = dataRow.rowNumber;
                for (String headline : dataRow.dataCells.keySet()) {
                    dataCells.put(headline, new EvaluatedCell(dataRow.value(headline), AssessedDataRow.CellMatchStatus.UNEVALUATED));
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

            /**
             * Created by jordam on 2016-11-09.
             */
            static enum CellMatchStatus {
                UNEVALUATED,
                CORRECT,
                WRONG
            }

            /**
             * Created by jordam on 2016-11-09.
             */
            public static class EvaluatedCell {
                public String cellData;
                public CellMatchStatus cellMatchStatus = CellMatchStatus.UNEVALUATED;

                public EvaluatedCell(String dataValue, CellMatchStatus cellMatchStatus) {
                    cellData = dataValue;
                    this.cellMatchStatus = cellMatchStatus;
                }
            }

            /**
             * Created by jordam on 2016-11-09.
             */
            static enum RowMatchStatus {
                ROW_IS_UNEVALUATED,
                ROW_HAS_ONLY_MISMATCHED_DATA,
                ROW_HAS_BOTH_CORRECT_AND_ERRONEUS_DATA,
                ROW_HAS_ONLY_CORRECT_MATCHES
            }
        }
    }
}
