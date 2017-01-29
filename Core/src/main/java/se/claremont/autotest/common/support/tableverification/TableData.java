package se.claremont.autotest.common.support.tableverification;

import se.claremont.autotest.common.logging.LogLevel;
import se.claremont.autotest.common.support.SupportMethods;
import se.claremont.autotest.common.testcase.TestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jordam on 2017-01-28.
 */
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
     */
    public void verifyRowExist(String headlineColonValueSemicolonSeparatedStringForRowMatch, CellMatchingType cellMatchingType){
        evaluate(headlineColonValueSemicolonSeparatedStringForRowMatch, cellMatchingType);
        for(TableRow tableRow : rows){
            if(tableRow.tableRowEvaluationStatus.equals(TableRowEvaluationStatus.ONLY_MATCHES)) {
                logDifferentlyToTextLogAndHtmlLog(LogLevel.VERIFICATION_PASSED, "Match found for '" + headlineColonValueSemicolonSeparatedStringForRowMatch + "' in " + tableElementName + ".", "Match found for<br>" +searchCriteriaAsHtmlTable(headlineColonValueSemicolonSeparatedStringForRowMatch) + "<br>Found in " + tableElementName + "'.<br>" + toHtml());
                return;
            }
        }
        logDifferentlyToTextLogAndHtmlLog(LogLevel.VERIFICATION_FAILED, "Could not find any match for '" +
                        headlineColonValueSemicolonSeparatedStringForRowMatch + "' in " + tableElementName + ".",
                "Could not find any match for<br>" + searchCriteriaAsHtmlTable(headlineColonValueSemicolonSeparatedStringForRowMatch) +
                        "<br>The data of " + tableElementName + ":<br>" + toHtml());
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
     */
    public void verifyRowDoesNotExist(String headlineColonValueSemicolonSeparatedStringForRowMatch, CellMatchingType cellMatchingType){
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
    }

    public void verifyHeadingExist(String heading){
        if(headlines.contains(heading.trim())){
            log(LogLevel.VERIFICATION_PASSED, "Heading '" + heading + "' exist among the headlines '" + String.join("', '", headlines) + "' of " + tableElementName + ".");
        } else {
            log(LogLevel.VERIFICATION_FAILED, "Heading '" + heading + "' does not exist among the headlines '" + String.join("', '", headlines) + "' of " + tableElementName + ".");
        }
    }

    public void verifyHeadingsExist(List<String> headings){
        for(String heading : headings){
            if(headlines.contains(heading.trim())){
                log(LogLevel.VERIFICATION_PASSED, "Heading '" + heading + "' exist among the headlines '" + String.join("', '", headlines) + "' of " + tableElementName + ".");
            } else {
                log(LogLevel.VERIFICATION_FAILED, "Heading '" + heading + "' does not exist among the headlines '" + String.join("', '", headlines) + "' of " + tableElementName + ".");
            }
        }
    }

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

    public int dataRowCount(){
        return rows.size();
    }

    public boolean tableIsEmpty(){
        return dataRowCount() == 0;
    }

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

    @Override
    public String toString(){
        String returnString = tableElementName + System.lineSeparator() + String.join("; ", headlines) + System.lineSeparator();
        for(TableRow tableRow : rows){
            returnString += tableRow.toString() + System.lineSeparator();
        }
        return returnString;
    }

    public String toHtml(){
        StringBuilder sb = new StringBuilder();
        if(tableElementName != null) sb.append(tableElementName).append("<br>").append(System.lineSeparator());
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

    private void createTableDataFromContent(String originalContent, String[] headings, String rowDelimiter, String cellDilimiter){
        String[] lines = originalContent.split(rowDelimiter);
        if(lines.length < 1) return;
        if(headings == null){
            String[] firstRowCells = lines[0].split(cellDilimiter);
            int headlinesCount = firstRowCells.length;
            for(String headline : firstRowCells){
                headlines.add(headline.trim());
            }
            if(lines.length < 2) return;
            for(int i = 1; i < lines.length;i++){
                List<DataCell> cellContents = new ArrayList<>();
                String[] cells = lines[i].split(cellDilimiter);
                if(headlinesCount != cells.length){
                    log(LogLevel.DEBUG, "Warning: The data row '" + lines[i] + "' does not have the same number of cells as the headline row. Skipping this line.");
                    continue;
                }
                if(lines[i].trim().length() == 0)continue;

                for(int cellCount = 0; cellCount < cells.length; cellCount++){
                    cellContents.add(new DataCell(cells[cellCount], firstRowCells[cellCount]));
                }
                rows.add(new TableRow(cellContents));
            }
        } else {
            int headlinesCount = headings.length;
            for(String headline : headings){
                headlines.add(headline.trim());
            }
            if(lines.length < 1) return;
            for(int i = 0; i < lines.length;i++){
                List<DataCell> cellContents = new ArrayList<>();
                String[] cells = lines[i].split(cellDilimiter);
                if(headlinesCount != cells.length){
                    log(LogLevel.DEBUG, "Warning: The data row '" + lines[i] + "' does not have the same number of cells as the headline row. Skipping this line.");
                    continue;
                }
                if(lines[i].trim().length() == 0)continue;

                for(int cellCount = 0; cellCount < cells.length; cellCount++){
                    cellContents.add(new DataCell(cells[cellCount], headings[cellCount]));
                }
                rows.add(new TableRow(cellContents));
            }
        }
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

    public void verifyRows(String[] headlineColonValueSemicolonSeparatedString, CellMatchingType cellMatchingType) {
        for(String searchCriteria : headlineColonValueSemicolonSeparatedString){
            verifyRowExist(searchCriteria, cellMatchingType);
        }
    }
}
