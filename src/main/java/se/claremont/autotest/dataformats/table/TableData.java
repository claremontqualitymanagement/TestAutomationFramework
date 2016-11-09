package se.claremont.autotest.dataformats.table;

import se.claremont.autotest.common.LogLevel;
import se.claremont.autotest.common.TestCase;
import se.claremont.autotest.guidriverpluginstructure.swingsupport.robotswinggluecode.RobotSwingInteractionMethods;
import se.claremont.autotest.support.SupportMethods;

import java.util.*;

/**
 * Created by jordam on 2016-11-09.
 */
public class TableData {
    String tableData;
    String guiElementName;
    List<DataRow> dataRows;
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
        if (guiElementName != null && guiElementName.trim() != "" && log) {
            testCase.logDifferentlyToTextLogAndHtmlLog(LogLevel.DEBUG, "Reading TableData for " + guiElementName + SupportMethods.LF + tableData,
                    "Reading TableData for " + guiElementName + ":<br>" +
                            tableData.replace(SupportMethods.LF, "<br>").replace("\n", "<br>"));
        }
        if (tableData == null) {
            testCase.log(LogLevel.INFO, "TableData for " + guiElementName + " is null. Making it ''.");
            tableData = "";
        }
        this.testCase = testCase;
        this.tableData = tableData;
        this.guiElementName = guiElementName;
        dataRows = new ArrayList<>();
        String[] rows = tableData.split(SupportMethods.LF);
        if (rows.length < 1) return;

        headLines = createHeadLineRow(rows[0]);
        if (rows.length < 2) return;

        for (int rowNumber = 1; rowNumber < rows.length; rowNumber++)
        {
            createDataRow(rows[rowNumber], rowNumber);
        }
    }

    /**
     * The table in html format output.
     *
     * @param logLevel Logging level
     * @return HTML formatted table as string
     */
    public String toHtml(LogLevel logLevel)
    {
        AssessedTableData tableData = new AssessedTableData(this);
        String html = tableData.convertAssessedTableDataToHtml(null, false, logLevel);
        if (tableData.takeScreenshot()) {
            RobotSwingInteractionMethods r = new RobotSwingInteractionMethods(testCase);
            r.captureScreenshot();
        }
        return html;
    }

    public @Override String toString()
    {
        List<String> returnString = new ArrayList<String>();
        if (headLines != null && headLines.size() > 0)
        {
            returnString.add(String.join(";", headLines));
            if (dataRows != null)
            {
                for (DataRow dataRow : dataRows)
                {
                    List<String> cells = new ArrayList<>();
                    for (String headline : dataRow.dataCells.keySet())
                    {
                        cells.add(dataRow.value(headline));
                    }
                    returnString.add(String.join(";", cells));
                }
            }
        }
        return String.join(SupportMethods.LF, returnString);
    }

    public static String headlineColonValueSemicolonSeparatedStringToHtml(
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

    public boolean rowExists(String headlineColonValueSemicolonSeparatedString, boolean regex, Integer expectedMatchesCount) {
        boolean rowExist = false;
        AssessedTableData assessedData = assess(headlineColonValueSemicolonSeparatedString, regex, false);
        if (expectedMatchesCount != null) {
            if (assessedData.numberOfRowsWithRowStatus(RowMatchStatus.ROW_HAS_ONLY_CORRECT_MATCHES) == expectedMatchesCount) {
                rowExist = true;
            }
        } else {
            if ( assessedData.numberOfRowsWithRowStatus(RowMatchStatus.ROW_HAS_ONLY_CORRECT_MATCHES) > 0) {
                rowExist = true;
            }
        }
        testCase.logDifferentlyToTextLogAndHtmlLog(LogLevel.DEBUG, "Examined if the data below existed in the table in " + guiElementName + SupportMethods.LF +
                        headlineColonValueSemicolonSeparatedStringToTableData(headlineColonValueSemicolonSeparatedString,
                                guiElementName, testCase).toHtml(LogLevel.DEBUG) + "<br>Results: '" +
                        Boolean.toString(rowExist).toLowerCase().replace("true", "Yes, it exists.").replace("false", "No, it does not exist."),
                "Examined if the data below existed in the table in " + guiElementName + "<br>" +
                        headlineColonValueSemicolonSeparatedStringToTableData(headlineColonValueSemicolonSeparatedString,
                                guiElementName, testCase).toHtml(LogLevel.DEBUG) + "<br>Results: '" +
                        Boolean.toString(rowExist).toLowerCase().replace("true", "Yes, it exists.").replace("false", "No, it does not exist."));
        return rowExist;
    }


    public String getValue(String headlineName, String headlineColonValueSemicolonSeparatedStringThatIdentifiesTheTableRow,
                           boolean regex)
    {
        String returnString = null;
        if (headlineName == null || headlineName.length() == 0)
        {
            testCase.log(LogLevel.INFO,
                    "Skips TableData.getValue() as the given headline is " +
                            Boolean.toString(headlineName == null).toLowerCase().replace("true", "null").replace("false", "empty") + ".");
            return returnString;
        }
        AssessedTableData assessedData = assess(headlineColonValueSemicolonSeparatedStringThatIdentifiesTheTableRow, regex, false);
        if (assessedData.numberOfRowsWithRowStatus(RowMatchStatus.ROW_HAS_ONLY_CORRECT_MATCHES) != 1)
        {
            testCase.log(LogLevel.VERIFICATION_FAILED,
                    "Tried getting the value for headline '" + headlineName + "' in the table row that could be identified by '" +
                            headlineColonValueSemicolonSeparatedStringThatIdentifiesTheTableRow +
                            "' , and expected exactly one match, but found " +
                            assessedData.numberOfRowsWithRowStatus(RowMatchStatus.ROW_HAS_ONLY_CORRECT_MATCHES) +
                            " matches.");
        }
        else
        {
            if (headLines.contains(headlineName))
            {
                try
                {
                    returnString = dataRows.get(assessedData.rowNumbersForRowsWithMatches().get(0)).value(headlineName);
                }
                catch (Exception e)
                {
                    testCase.log(LogLevel.VERIFICATION_PROBLEM,
                            "Something went wrong in TableData.getValue(): " + e.getMessage());
                }
            }
            else
            {
                testCase.log(LogLevel.VERIFICATION_PROBLEM,
                        "Could not pick the headline '" + headlineName + "' in the table of " + guiElementName +
                                ". Headlines found were '" + String.join("', '", headLines) + "'.");
            }
        }
        return returnString;
    }

    public boolean tableIsEmpty()
    {
        if (guiElementName == null || guiElementName.length() == 0)
        {
            testCase.log(LogLevel.DEBUG,
                    "Checked if the table in " + guiElementName + " was empty and " +
                            Boolean.toString(dataRows.size() == 0).toLowerCase().replace("true", "it was.").replace("false", "it was not."));
        }
        return dataRows.size() == 0;
    }

    /**
     * Returns the value for the stated headline. Assuming only one data row in table.
     * @param headlineName The name of the headline.
     * @return Returns the data cell content.
     */
    public String getValueForHeadlineInTableData(String headlineName)
    {
        String returnValue = null;
        if (dataRows.size() != 1)
        {
            testCase.log(LogLevel.VERIFICATION_PROBLEM,
                    "Cannot get data for headline '" + headlineName + "' in the table of " + guiElementName +
                            " since it un-expectedly matched with multiple rows.");
            testCase.logDifferentlyToTextLogAndHtmlLog(LogLevel.DEVIATION_EXTRA_INFO, "The table that triggered the error had the following content:" +
                            SupportMethods.LF + toString(),
                    "The table that triggered the error had the following content:<br>" + toHtml(LogLevel.DEVIATION_EXTRA_INFO));
        }
        else
        {
            if (headLines.contains(headlineName))
            {
                try
                {
                    returnValue = dataRows.get(0).value(headlineName);
                    testCase.log(LogLevel.DEBUG,
                            "getting value '" + returnValue + "' for headline '" + headlineName + "' in the table of " +
                                    guiElementName + ".");
                }
                catch (Exception e)
                {
                    testCase.log(LogLevel.VERIFICATION_PROBLEM,
                            "Something went wrong while trying to get the value of headline '" + headlineName +
                                    "' in TableData.getValueForHeadlineInTableData(): " + e.getMessage());
                }
            }
            else
            {
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
    public void verifyRow(String headlineColonValueSemicolonSeparatedString, boolean regex)
    {
        assess(headlineColonValueSemicolonSeparatedString, regex, true);
    }

    /**
     * Verify that the table data containst the sought after values.
     *
     * @param headlineColonValueSemicolonSeparatedStrings The values to find, in a format of 'Heading1:Value1;Heading3:Value3' if all strings are matched the test is passed.
     * @param regex Boolean value to state if the value is a regex value or a if the data should be matched with a 'contains' instead.
     */
    public void verifyRows(String[] headlineColonValueSemicolonSeparatedStrings, boolean regex)
    {
        for (String headlineColonValueSemicolonSeparatedString : headlineColonValueSemicolonSeparatedStrings)
        {
            assess(headlineColonValueSemicolonSeparatedString, regex, true);
        }
    }

    private List<String> createHeadLineRow(String topRow)
    {
        String[] headLines = topRow.split(";");
        List<String> headLineList = new ArrayList<>();
        for (String headLine : headLines)
        {
            if (headLine.length() < 1) continue;
            headLineList.add(headLine.trim());
        }
        this.headLines = headLineList;
        return this.headLines;
    }

    private void createDataRow(String dataRow, int rowNumber)
    {
        String[] rowData = dataRow.split(";");
        if (rowData.length == 1 && rowData[0].trim().equals("")) // if last row is blank (end with newline)
        {
        }
        else
        {
            List<String> cells = new ArrayList<>();
            for (String cell : rowData)
            {
                cells.add(cell.trim());
            }
            rowData = cells.toArray(new String[cells.size()]);
            if (headLines.size() <= rowData.length)
            {
                dataRows.add(new DataRow(headLines, rowData, rowNumber));
            }
            else
            {
                testCase.logDifferentlyToTextLogAndHtmlLog(LogLevel.DEBUG,
                        "public TableData():" + SupportMethods.LF + "It seems like the headline row ['" + String.join("', '", headLines) +
                                "'] has " + headLines.size() + " items while the row data ['" + String.join("', '", rowData) +
                                "'] has " + rowData.length +
                                " items. Not equal. Confusing. Skipping this row.",
                "public TableData():<br>It seems like the head line count for head line row<br>['" + String.join("', '", headLines) +
                        "']<br>has " + headLines.size() + " items, while the row data<br>['" + String.join("', '", rowData) +
                        "']<br>has " + rowData.length +
                        " items. Not equal. Confusing. Skipping this row.");
            }
        }
    }


    private TableData createTableDataObjectFromDataRow(DataRow dataRow, String guiElementName, TestCase testCase)
    {
        Set<String> headlines = dataRow.dataCells.keySet();
        String tableDataString = String.join(";", headlines) + SupportMethods.LF + dataRow.toString();
        return new TableData(tableDataString, guiElementName, testCase, false);
    }



    private static TableData headlineColonValueSemicolonSeparatedStringToTableData(
            String headlineColonValueSemicolonSeparatedString, String guiElementName, TestCase testCase)
    {
        List<String> headlines = new ArrayList<>();
        List<String> datavalues = new ArrayList<>();
        String[] thePairs = headlineColonValueSemicolonSeparatedString.split(";");
        for (String pair : thePairs)
        {
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
            String headlineColonValueSemicolonSeparatedString)
    {
        boolean allHeadlinesExists = true;
        if (headlineColonValueSemicolonSeparatedString == null)  return false;
        String[] pairsToFind = headlineColonValueSemicolonSeparatedString.split(";");
        for (String pairToFind : pairsToFind)
        {
            boolean headlineExist = false;
            if (pairToFind.indexOf(':') > 0)
            {
                String soughtAfterHeadline = pairToFind.split(":")[0];
                for (String headlineInActualTable : headLines)
                {
                    if (soughtAfterHeadline.equals(headlineInActualTable))
                    {
                        headlineExist = true;
                        break;
                    }
                }
                if (!headlineExist)
                {
                    allHeadlinesExists = false;
                    testCase.log(LogLevel.INFO,
                            "No match for headline '" + soughtAfterHeadline + "' in the table of " + guiElementName +
                                    ". Headlines found: '" + String.join("', '", headLines) + "'.");
                }
            }
        }
        return allHeadlinesExists;
    }

    private AssessedTableData assess(String headlineColonValueSemicolonSeparatedString, boolean regex, boolean logResults)
    {
        if (headlineColonValueSemicolonSeparatedString == null || headlineColonValueSemicolonSeparatedString.length() == 0)
        {
            testCase.log(LogLevel.INFO,
                    "Attempting assessing the content of the table against the headlineColonValueSemicolonSeparatedString that is " +
                            Boolean.toString(headlineColonValueSemicolonSeparatedString == null)
                                    .toLowerCase()
                                    .replace("true", "null")
                                    .replace("false", "empty") + ". Hence attempting assessing top row.");
            headlineColonValueSemicolonSeparatedString = "Rowcount:1";
        }
        AssessedTableData assessableData = new AssessedTableData(this);
        headlinesFromHeadlineColonValueSemicolonSeparatedStringExists(headlineColonValueSemicolonSeparatedString);
        for (AssessedDataRow rowToAssess : assessableData.dataRows)
        {
            rowToAssess.Assess(headlineColonValueSemicolonSeparatedString, regex);
        }
        if (logResults)
        {
            assessableData.convertAssessedTableDataToHtml(headlineColonValueSemicolonSeparatedString, regex, LogLevel.DEBUG);
        }
        return assessableData;
    }
}
