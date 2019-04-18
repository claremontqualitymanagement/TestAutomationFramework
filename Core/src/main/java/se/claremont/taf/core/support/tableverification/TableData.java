package se.claremont.taf.core.support.tableverification;

import se.claremont.taf.core.logging.LogLevel;
import se.claremont.taf.core.support.SupportMethods;
import se.claremont.taf.core.testcase.TestCase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * TableData class for verification and analysis of table data.
 * TableData could be constructed in multiple ways and should be
 * useful for many types of two dimensional data, for example
 * HTML tables, CSV files, and result sets from SQL queries.
 *
 * Methods exist for script flow control and for verifications.
 * Verification methods return boolean to enhance possibilities
 * to log technology dependent debugging info if verifications
 * fail.
 *
 * Created by jordam on 2017-01-28.
 */
@SuppressWarnings({"SameParameterValue", "WeakerAccess"})
public class TableData {
    final String tableElementName;
    final String originalContent;
    final TestCase testCase;
    final List<TableRow> rows = new ArrayList<>();
    final List<String> headlines = new ArrayList<>();

    /**
     * Creates a table object with the first row as headings. Data read from the originalContent field is
     * interpreted by row separation from the lineSeparator character and cells on rows are separated by
     * the ';' character.
     *
     * @param testCase The test case to log to.
     * @param name The name of the element, for logging purposes only.
     * @param originalContent The data, with delimiters from rowDelimiter field and cellDelimiter fields
     */
    public TableData(TestCase testCase, String name, String originalContent){
        this.tableElementName = name;
        this.originalContent = originalContent;
        this.testCase = testCase;
        createTableDataFromContent(originalContent, null, System.lineSeparator(), ";");
        logDifferentlyToTextLogAndHtmlLog(LogLevel.DEBUG, "Creating table data for " + tableElementName + System.lineSeparator() + toString(),
                "Creating table data for " + tableElementName + "<br>" + toHtml());
    }

    /**
     * Creates a table object from raw data, with the headings for that data provided.
     *
     * @param testCase The test case to log to.
     * @param name The name of the element, for logging purposes only.
     * @param originalContent The table data, where data rows are separated by lineSeparator and cells on rows are separated by the ';' character.
     * @param headings The headings for the data
     */
    public TableData(TestCase testCase, String name, String originalContent, String[] headings){
        this.tableElementName = name;
        this.originalContent = originalContent;
        this.testCase = testCase;
        createTableDataFromContent(originalContent, headings, System.lineSeparator(), ";");
        logDifferentlyToTextLogAndHtmlLog(LogLevel.DEBUG, "Creating table data for " + tableElementName + System.lineSeparator() + toString(),
                "Creating table data for " + tableElementName + "<br>" + toHtml());
    }

    /**
     * Creates a table object with the first row as headings. Data read from the originalContent field is
     * interpreted by separation from the rowDelimiter and cellDelimiter field characters.
     *
     * @param testCase The test case to log to.
     * @param name The name of the element, for logging purposes only.
     * @param originalContent The data, with delimiters from rowDelimiter field and cellDelimiter fields
     * @param rowDelimiter The character string that separates rows in the original content.
     * @param cellDelimiter The character string that separates cells on data rows in the originalContent string data.
     */
    public TableData(TestCase testCase, String name, String originalContent, String rowDelimiter, String cellDelimiter){
        this.tableElementName = name;
        this.originalContent = originalContent;
        this.testCase = testCase;
        createTableDataFromContent(originalContent, null, rowDelimiter, cellDelimiter);
        logDifferentlyToTextLogAndHtmlLog(LogLevel.DEBUG, "Creating table data for " + tableElementName + System.lineSeparator() + toString(),
                "Creating table data for " + tableElementName + "<br>" + toHtml());
    }

    /**
     * Creates a table object from raw data, with the headings given. Data read from the originalContent field is
     * interpreted by separation from the rowDelimiter and cellDelimiter field characters.
     *
     * @param testCase The test case to log to.
     * @param name The name of the element, for logging purposes only.
     * @param originalContent The data, with delimiters from rowDelimiter field and cellDelimiter fields
     * @param headings The headings for the data
     * @param rowDelimiter The character string that separates rows in the original content.
     * @param cellDelimiter The character string that separates cells on data rows in the originalContent string data.
     */
    public TableData(TestCase testCase, String name, String originalContent, String[] headings, String rowDelimiter, String cellDelimiter){
        this.tableElementName = name;
        this.originalContent = originalContent;
        this.testCase = testCase;
        createTableDataFromContent(originalContent, headings, rowDelimiter, cellDelimiter);
        logDifferentlyToTextLogAndHtmlLog(LogLevel.DEBUG, "Creating table data for " + tableElementName + System.lineSeparator() + toString(),
                "Creating table data for " + tableElementName + "<br>" + toHtml());
    }

    /**
     * Checks if there is a match for the heading in the table. No logging performed.
     *
     * @param headline The heading to check for.
     * @param cellMatchingType The type of matching to perform.
     * @return Returns true if match is found, otherwise false.
     */
    public boolean headlineExist(String headline, CellMatchingType cellMatchingType){
        for(String heading : headlines){
            if(cellMatchingType.equals(CellMatchingType.EXACT_MATCH) && heading.trim().equals(headline.trim())) return true;
            if(cellMatchingType.equals(CellMatchingType.CONTAINS_MATCH) && (heading.trim().equals(headline.trim()) || heading.trim().contains(headline))) return true;
            if(cellMatchingType.equals(CellMatchingType.REGEX_MATCH) && SupportMethods.isRegexMatch(heading.trim(), headline.trim())) return true;
        }
        return false;
    }

    /**
     * Verification if at least one instance of a row matching the given search criteria exist in the table data.
     * Performs logging like a verification point.
     *
     * @param headlineColonValueSemicolonSeparatedStringForRowMatch String formatted as a search criteria:
     *              "Headline1:Content1;Headline2:Content2" would require a row with "Content1" as cell data for
     *              headline "Headline1" and "Content2" as cell data for "Headline2" to be a match.
     * @param cellMatchingType The type of matching to perform. Either an exact match is required, or the
     *              searched for value could be a partial match, or matching through regular expression.
     * @return Returns true only if a row matching the given pattern and matching type is found.
     */
    public boolean verifyRowExist(String headlineColonValueSemicolonSeparatedStringForRowMatch, CellMatchingType cellMatchingType){
        evaluate(headlineColonValueSemicolonSeparatedStringForRowMatch, cellMatchingType);
        for(TableRow tableRow : rows){
            if(tableRow.tableRowEvaluationStatus.equals(TableRowEvaluationStatus.ONLY_MATCHES)) {
                logDifferentlyToTextLogAndHtmlLog(LogLevel.VERIFICATION_PASSED, "Match found for '" + headlineColonValueSemicolonSeparatedStringForRowMatch + "' in " + tableElementName + ".",
                        "Match found for<br>" +searchCriteriaAsHtmlTable(headlineColonValueSemicolonSeparatedStringForRowMatch) + "<br>Found in " + tableElementName + ".<br>" + toHtml());
                return true;
            }
        }
        logDifferentlyToTextLogAndHtmlLog(LogLevel.VERIFICATION_FAILED, "Could not find any match for '" +
                        headlineColonValueSemicolonSeparatedStringForRowMatch + "' in " + tableElementName + ".",
                "Could not find any match for<br>" + searchCriteriaAsHtmlTable(headlineColonValueSemicolonSeparatedStringForRowMatch) +
                        "<br>The data of " + tableElementName + ":<br>" + toHtml());
        return false;
    }

    /**
     * Verification that no instance of a row matching the given search criteria exist in the table data exists.
     * Performs logging like a verification point.
     *
     * @param headlineColonValueSemicolonSeparatedStringForRowMatch String formatted as a search criteria:
     *              "Headline1:Content1;Headline2:Content2" would require a row with "Content1" as cell data for
     *              headline "Headline1" and "Content2" as cell data for "Headline2" to be a match.
     * @param cellMatchingType The type of matching to perform. Either an exact match is required, or the
     *              searched for value could be a partial match, or matching through regular expression.
     * @return Returns true only if no rows matching the row pattern and matching type is found.
     */
    @SuppressWarnings("UnusedReturnValue")
    public boolean verifyRowDoesNotExist(String headlineColonValueSemicolonSeparatedStringForRowMatch, CellMatchingType cellMatchingType){
        evaluate(headlineColonValueSemicolonSeparatedStringForRowMatch, cellMatchingType);
        boolean matchFound = false;
        for(TableRow tableRow : rows){
            if(tableRow.tableRowEvaluationStatus.equals(TableRowEvaluationStatus.ONLY_MATCHES)) {
                matchFound = true;
            }
        }
        if(matchFound){
            logDifferentlyToTextLogAndHtmlLog(LogLevel.VERIFICATION_FAILED, "Found match for '" + headlineColonValueSemicolonSeparatedStringForRowMatch + "' in table " + tableElementName + ". " + System.lineSeparator() + toString(),
                    "Found match for '" + headlineColonValueSemicolonSeparatedStringForRowMatch + "' in table " + tableElementName + ".<br>" + toHtml());
        } else {
            logDifferentlyToTextLogAndHtmlLog(LogLevel.VERIFICATION_PASSED, "Did not find any match for '" + headlineColonValueSemicolonSeparatedStringForRowMatch + "' in " + tableElementName + " as expected." + System.lineSeparator() + toString(),
                    "Did not find any match for '" + headlineColonValueSemicolonSeparatedStringForRowMatch + "' in " + tableElementName + " as expected.<br>" + toHtml());
        }
        return !matchFound;
    }

    /**
     * Verifies that the given heading exist as a heading in the table.
     *
     * @param heading The heading to verify existence of.
     * @return Returns true if the heading is found.
     */
    public boolean verifyHeadingExist(String heading){
        boolean verificationPassed = false;
        StringBuilder htmlHeadingsRepresentation = new StringBuilder("<table class=\"tableverificationresulttable\">" + System.lineSeparator() + "   <tr class=\"headlines\">" + System.lineSeparator());
        for(String headline : headlines){
            if(headline.equals(heading)){
                htmlHeadingsRepresentation.append("      <th class=\"found_heading\">").append(headline).append("</th>").append(System.lineSeparator());
            } else {
                htmlHeadingsRepresentation.append("      <th class=\"not_found_heading\">").append(headline).append("</th>").append(System.lineSeparator());
            }
        }
        htmlHeadingsRepresentation.append("   </tr>").append(System.lineSeparator()).append("</table>").append(System.lineSeparator());

        if(headlines.contains(heading.trim())){
            logDifferentlyToTextLogAndHtmlLog(LogLevel.VERIFICATION_PASSED, "Heading '" + heading + "' exist among the headlines '" + String.join("', '", headlines) + "' of " + tableElementName + ".",
                    "Heading '" + heading + "' found among the headings of " + tableElementName + ".<br>" + htmlHeadingsRepresentation);
            verificationPassed = true;
        } else {
            logDifferentlyToTextLogAndHtmlLog(LogLevel.VERIFICATION_FAILED, "Heading '" + heading + "' does not exist among the headlines '" + String.join("', '", headlines) + "' of " + tableElementName + ".",
                    "Heading '" + heading + "' could not be found among the headings of " + tableElementName + ".<br>" + htmlHeadingsRepresentation);
        }
        return verificationPassed;
    }

    /**
     * Verifies that the given headings exists in the set of headings for the table.
     *
     * @param headings Headings to find.
     * @return Returns true if all headings are found, otherwise false.
     */
    public boolean verifyHeadingsExist(List<String> headings){
        List<String> foundHeadings = new ArrayList<>();
        StringBuilder htmlHeadingsRepresentation = new StringBuilder("<table class=\"tableverificationresulttable\">" + System.lineSeparator() + "   <tr class=\"headlines\">" + System.lineSeparator());
        for(String headline : headlines){
            if(headings.contains(headline)){
                htmlHeadingsRepresentation.append("      <th class=\"found_heading\">").append(headline).append("</th>").append(System.lineSeparator());
                foundHeadings.add(headline);
            } else {
                htmlHeadingsRepresentation.append("      <th class=\"not_found_heading\">").append(headline).append("</th>").append(System.lineSeparator());
            }
        }
        htmlHeadingsRepresentation.append("   </tr>").append(System.lineSeparator()).append("</table>").append(System.lineSeparator());
        if(foundHeadings.size() == headings.size()){
            logDifferentlyToTextLogAndHtmlLog(LogLevel.VERIFICATION_PASSED, "Headings '" + String.join("', '", headings) + "' were found among the headlines '" + String.join("', '", headlines) + "' of " + tableElementName + ".",
                    "Headings '" + String.join("', '", headings) + "' were found among the headings of " + tableElementName + ".<br>" + htmlHeadingsRepresentation);
            return true;
        } else {
            List<String> missingHeadlines = new ArrayList<>();
            for(String heading : headings){
                if(foundHeadings.contains(heading)) continue;
                missingHeadlines.add(heading);
            }
            logDifferentlyToTextLogAndHtmlLog(LogLevel.VERIFICATION_FAILED, "Verified existence of headings '" + String.join("', '", headings) + "', but the heading(s) '" + String.join("', '", missingHeadlines) + "' could not be found among the headlines '" + String.join("', '", headlines) + "' of " + tableElementName + ".",
                    "Verified existence of headings '" + String.join("', '", headings) + "', but the heading(s) '" + String.join("', '", missingHeadlines) + "' could not be found among the headings of " + tableElementName + ".<br>" + htmlHeadingsRepresentation);
            return false;
        }
    }

    /**
     * Method intended for logging and script flow control. Identifies the first row to fit the given pattern,
     * and returns the value of that row for the headline given.
     *
     * @param headline The headline to return the row value for.
     * @param headlineColonValueSemicolonSeparatedStringThatIdentifiesTheTableRow The pattern to identify the row to search for.
     * @param cellMatchingType The type of matching to perform.
     * @return Returns the content of the data cell. If the cell is not found null is returned.
     */
    public String getValueForHeadlineForRow(String headline, String headlineColonValueSemicolonSeparatedStringThatIdentifiesTheTableRow, CellMatchingType cellMatchingType){
        evaluate(headlineColonValueSemicolonSeparatedStringThatIdentifiesTheTableRow, cellMatchingType);
        for(TableRow tableRow : rows){
            if(tableRow.tableRowEvaluationStatus.equals(TableRowEvaluationStatus.ONLY_MATCHES)){
                if(tableRow.headlineExist(headline, cellMatchingType)){
                    String value = tableRow.getValueForHeadline(headline, cellMatchingType);
                    log(LogLevel.DEBUG, "Identified value '" + value  + "' for headline '" + headline + "' for table row identified by '" + headlineColonValueSemicolonSeparatedStringThatIdentifiesTheTableRow + "' in table " + tableElementName + ".");
                    if(value != null) return value;
                }
            }
        }
        log(LogLevel.DEBUG, "Could not find any value for headline '" + headline + "' for table row identified by '" + headlineColonValueSemicolonSeparatedStringThatIdentifiesTheTableRow + "' in table " + tableElementName + ".");
        return null;
    }

    /**
     * Returns the number of data rows in the table data (exluding headings).
     *
     * @return Number of data rows.
     */
    public int dataRowCount(){
        return rows.size();
    }

    /**
     * Flow control method that respont to table data status.
     *
     * @return Returns true if table is empty, otherwise false.
     */
    public boolean tableIsEmpty(){
        return dataRowCount() == 0;
    }

    /**
     * Flow control method to steer test flow depending on the existance of a specified row in the table data.
     *
     * @param headlineColonValueSemicolonSeparatedStringForRowMatch The pattern for the row to search for.
     * @param cellMatchingType The type of matching to perform.
     * @return Returns false if the row is missing, and true is the row is present.
     */
    public boolean rowExist(String headlineColonValueSemicolonSeparatedStringForRowMatch, CellMatchingType cellMatchingType){
        evaluate(headlineColonValueSemicolonSeparatedStringForRowMatch, cellMatchingType);
        for(TableRow tableRow : rows){
            if(tableRow.tableRowEvaluationStatus.equals(TableRowEvaluationStatus.ONLY_MATCHES)) {
                log(LogLevel.DEBUG, "Match for '" + headlineColonValueSemicolonSeparatedStringForRowMatch + "' found in " + tableElementName + "'.");
                return true;
            }
        }
        log(LogLevel.DEBUG, "No match for '" + headlineColonValueSemicolonSeparatedStringForRowMatch + "' found in " + tableElementName + "'.");
        return false;
    }

    /**
     * Flow control method to steer test flow depending on the non-existance of a specified row in the table data.
     *
     * @param headlineColonValueSemicolonSeparatedStringForRowMatch The pattern for the row to search for.
     * @param cellMatchingType The type of matching to perform.
     * @return Returns true if the row is missing, and false is the row is present.
     */
    public boolean rowDoesNotExist(String headlineColonValueSemicolonSeparatedStringForRowMatch, CellMatchingType cellMatchingType){
        evaluate(headlineColonValueSemicolonSeparatedStringForRowMatch, cellMatchingType);
        for(TableRow tableRow : rows){
            if(tableRow.tableRowEvaluationStatus.equals(TableRowEvaluationStatus.ONLY_MATCHES)) {
                log(LogLevel.DEBUG, "Match for '" + headlineColonValueSemicolonSeparatedStringForRowMatch + "' found in " + tableElementName + "'.");
                return false;
            }
        }
        log(LogLevel.DEBUG, "No match for '" + headlineColonValueSemicolonSeparatedStringForRowMatch + "' found in " + tableElementName + "'.");
        return true;
    }

    /**
     * Verifies the existence of several rows in the same verification step.
     *
     * @param headlineColonValueSemicolonSeparatedStrings The strings identifying the rows to find matches for.
     * @param cellMatchingType The type of matching to perform.
     * @return Returns true if all verifications passes.
     */
    public boolean verifyRows(String[] headlineColonValueSemicolonSeparatedStrings, CellMatchingType cellMatchingType) {
        boolean someVerificationFailed = false;
        for(String searchCriteria : headlineColonValueSemicolonSeparatedStrings){
            if(!verifyRowExist(searchCriteria, cellMatchingType)) someVerificationFailed = true;
        }
        return !someVerificationFailed;
    }

    @Override
    public String toString(){
        StringBuilder returnString = new StringBuilder(tableElementName + System.lineSeparator() + String.join("; ", headlines) + System.lineSeparator());
        for(TableRow tableRow : rows){
            returnString.append(tableRow.toString()).append(System.lineSeparator());
        }
        return returnString.toString();
    }

    public String toHtml(){
        StringBuilder sb = new StringBuilder();
        //if(tableElementName != null) sb.append(tableElementName).append("<br>").append(System.lineSeparator());
        sb.append("<table class=\"tableverificationresulttable\">").append(System.lineSeparator());
        sb.append("   <tr class=\"headlines\">").append(System.lineSeparator());
        for(String headline : headlines){
            sb.append("      <th>").append(headline).append("</th>").append(System.lineSeparator());
        }
        sb.append("   </tr>").append(System.lineSeparator());
        for(TableRow row : rows){
            sb.append(row.toHtml());
        }
        sb.append("</table>");
        return sb.toString();
    }

    void resetEvaluationStatus(){
        for(TableRow tableRow : rows){
            tableRow.resetStatus();
        }
    }

    @SuppressWarnings("ConstantConditions")
    private int getMaxNumberOfCellsPerRow(String rowDelimiter, String cellDelimiter){
        String[] lines = originalContent.split(rowDelimiter);
        if(lines == null || lines.length < 1) return 0;
        int maxCells = 0;
        for(String line : lines){
            if(line.split(cellDelimiter).length > maxCells) maxCells = line.split(cellDelimiter).length;
        }
        if(this.headlines != null && headlines.size() > maxCells) {
            maxCells = headlines.size();
            log(LogLevel.DEBUG, "More headlines in table '" + tableElementName + "' than maximum number of cells in any row. Buffering up with empty cells trailing each data row to match headings count.");
        }
        log(LogLevel.DEBUG, "Maximum cell count for any row in the table '" + tableElementName + "' is found to be " + maxCells + ".");

        return maxCells;
    }


    private void createTableDataFromContent(String originalContent, String[] headings, String rowDelimiter, String cellDilimiter){
        log(LogLevel.DEBUG, "Creating a TableData object from content '" + originalContent + "'.");
        String[] lines = originalContent.split(rowDelimiter);
        int maxNumberOfCellsPerRow = getMaxNumberOfCellsPerRow(rowDelimiter, cellDilimiter);
        if(lines.length < 1) return;
        if(headings == null){
            log(LogLevel.DEBUG, "No headlines stated explicitly. Assuming first row is headlines.");
            String[] firstRowCells = lines[0].split(cellDilimiter);
            int headlinesCount = firstRowCells.length;
            for(String headline : firstRowCells){
                headlines.add(headline.trim());
            }
            if(headlinesCount < maxNumberOfCellsPerRow){
                log(LogLevel.DEBUG, "Cell count for headline cells is " + headlinesCount + " while maximum cell count in all the rows is " + maxNumberOfCellsPerRow + " adding empty headline cells to match.");
                for(int i = headlinesCount; i < maxNumberOfCellsPerRow; i++){
                    headlines.add("");
                }
            }
            if(lines.length < 2) return;
            for(int i = 1; i < lines.length;i++){
                if(lines[i].trim().length() == 0)continue;
                List<DataCell> cellContents = new ArrayList<>();
                String[] cells = lines[i].split(cellDilimiter);
                if(cells.length < maxNumberOfCellsPerRow){
                    List<String> cellList = new ArrayList<>();
                    Collections.addAll(cellList, cells);
                    for(int cellNumber = cells.length; cellNumber < maxNumberOfCellsPerRow; cellNumber++){
                        cellList.add("");
                    }
                    cells = new String[cellList.size()];
                    for(int counter = 0; counter < cellList.size(); counter++){
                        cells[counter] = cellList.get(counter);
                    }
                }
                for(int cellCount = 0; cellCount < cells.length; cellCount++){
                    cellContents.add(new DataCell(cells[cellCount].trim(), headlines.get(cellCount).trim()));
                }
                rows.add(new TableRow(cellContents));
            }
        } else {
            int headlinesCount = headings.length;
            for(String headline : headings){
                headlines.add(headline.trim());
            }
            if(headlinesCount < maxNumberOfCellsPerRow){
                log(LogLevel.DEBUG, "Cell count for headline cells is " + headlinesCount + " while maximum cell count in all the rows is " + maxNumberOfCellsPerRow + " adding empty headline cells to match.");
                for(int i = headlinesCount; i < maxNumberOfCellsPerRow; i++){
                    headlines.add("");
                }
            }
            //noinspection ConstantConditions
            if(lines.length < 1) return;
            for (String line : lines) {
                if (line.trim().length() == 0) continue;
                List<DataCell> cellContents = new ArrayList<>();
                String[] cells = line.split(cellDilimiter);
                if(cells.length < maxNumberOfCellsPerRow){
                    List<String> cellList = new ArrayList<>();
                    Collections.addAll(cellList, cells);
                    for(int cellNumber = cells.length; cellNumber < maxNumberOfCellsPerRow; cellNumber++){
                        cellList.add("");
                    }
                    cells = new String[cellList.size()];
                    for(int counter = 0; counter < cellList.size(); counter++){
                        cells[counter] = cellList.get(counter);
                    }
                }
                for (int cellCount = 0; cellCount < cells.length; cellCount++) {
                    cellContents.add(new DataCell(cells[cellCount].trim(), headlines.get(cellCount).trim()));
                }
                rows.add(new TableRow(cellContents));
            }
        }
        System.out.println(this.toString());
    }

    private void evaluate(String headlineColonValueSemicolonSeparatedStringForRowMatch, CellMatchingType cellMatchingType){
        resetEvaluationStatus();
        String[] pairs = headlineColonValueSemicolonSeparatedStringForRowMatch.split(";");
        for(String pair : pairs){
            String headline = pair.split(":")[0].trim();
            if(!headlineExist(headline, cellMatchingType)){
                log(LogLevel.VERIFICATION_PROBLEM, "Headline '" + headline + "' not found among '" + String.join("', '", headlines) + "' which are the headlines of " + tableElementName + ".");
                continue;
            }
            for(TableRow row : rows){
                row.evaluate(headlineColonValueSemicolonSeparatedStringForRowMatch, cellMatchingType);
            }
        }
    }

    private void logDifferentlyToTextLogAndHtmlLog(LogLevel logLevel, String message, String htmlMessage){
        if(this.testCase == null) return;
        testCase.logDifferentlyToTextLogAndHtmlLog(logLevel, message, htmlMessage);
    }

    private void log(LogLevel logLevel, String message){
        if(this.testCase == null) return;
        testCase.log(logLevel, message);
    }

    private String searchCriteriaAsHtmlTable(String headlineColonValueSemicolonSeparatedString){
        List<String> headlines = new ArrayList<>();
        List<String> values = new ArrayList<>();
        String[] pairs = headlineColonValueSemicolonSeparatedString.split(";");
        for(String pair: pairs){
            headlines.add(pair.split(":")[0]);
            String value = "";
            if(pair.split(":").length > 1) value = pair.substring(pair.indexOf(":") + 1);
            values.add(value);
        }
        TableData oracleTable = new TableData(null, null, String.join(";", headlines) + System.lineSeparator() + String.join(";", values));
        return oracleTable.toHtml();
    }

}
