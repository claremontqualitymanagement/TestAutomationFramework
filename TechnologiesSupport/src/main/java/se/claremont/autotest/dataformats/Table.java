package se.claremont.autotest.dataformats;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.claremont.autotest.common.support.SupportMethods;

import java.util.ArrayList;
import java.util.List;

/**
 * @deprecated
 * Use the TableData class instead. It has far better output since it color codes in the HTML report for ease of debugging.
 *
 * Created by jordam on 2016-10-18.
 */
@Deprecated
public class Table {
    private final static Logger logger = LoggerFactory.getLogger( Table.class );
    public Row headlineRow;
    List<Row> rows = new ArrayList<>();

    public Table(List<String[]> values){
        for(String[] row : values){
            rows.add(new Row(row));
        }
        if(hasHeadlines()){
            headlineRow = rows.get(0);
            rows.remove(0);
        }
    }

    public Table(Row headlineRow, List<Row> dataRows){
        this.headlineRow = headlineRow;
        this.rows = dataRows;
    }

    public Table(String[] headlines, List<String[]> dataRows){
        headlineRow = new Row(headlines);
        for(String[] row : dataRows){
            rows.add(new Row(row));
        }
    }

    public Table(String csvFileContent, String rowDelimiter, String fieldDelimiter){
        String[] tableRows = csvFileContent.split(rowDelimiter);
        for(String row : tableRows){
            if(row.split(fieldDelimiter).length == 0) continue;
            rows.add(new Row(row, fieldDelimiter));
        }
        if(hasHeadlines()){
            headlineRow = rows.get(0);
            rows.remove(0);
        }
    }

    public boolean headLineExist(String headline){
        for(String dataValue : headlineRow.dataValueList){
            if(dataValue.equals(headline)) return true;
        }
        return false;
    }


    public int headLineColumnNumber(String headLine){
        int i = 0;
        for(String headline : headlineRow.dataValueList){
            if(headline.equals(headLine)){
                return i;
            }
            i++;
        }
        return -1;
    }

    public List<Row> rowsWithMatchingValueForHeadline(String headlineName, String regexValueToMatch){
        if(!headLineExist(headlineName)) {
            logger.debug( "Cannot find headline '" + headlineName + "'. Registered headlines are '" + headlineRow.toString() + "'." );
            return null;
        }
        List<Row> matches = new ArrayList<>();
        int columnNumber = headLineColumnNumber(headlineName);
        for(Row row : rows){
            if(SupportMethods.isRegexMatch(row.getValueForColumn(columnNumber), regexValueToMatch)){
                matches.add(row);
            }
        }
        return matches;
    }

    private String headlineToHtmlIfExist(){
        StringBuilder sb = new StringBuilder();
        if(headlineRow != null){
            sb.append("   <tr>").append(SupportMethods.LF);
            for(String dataValue : headlineRow.dataValueList){
                sb.append("      <th>").append(dataValue).append("</th>").append(SupportMethods.LF);
            }
            sb.append("   </tr>").append(SupportMethods.LF);
        }
        return sb.toString();
    }

    private String tableRowsToHtml(){
        StringBuilder sb = new StringBuilder();
        for(Row row : rows){
            sb.append("   <tr>").append(SupportMethods.LF);
            for(String dataValue : row.dataValueList){
                sb.append("      <td>").append(dataValue).append("</td>").append(SupportMethods.LF);
            }
            sb.append("   </tr>").append(SupportMethods.LF);
        }
        sb.toString();
        return sb.toString();
    }

    public String toHtmlTable(){
        StringBuilder sb = new StringBuilder();
        sb.append("<table>").append(SupportMethods.LF);
        sb.append(headlineToHtmlIfExist());
        sb.append(tableRowsToHtml());
        sb.append("</table>").append(SupportMethods.LF);
        return sb.toString();
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        List<Integer> columnCharacterCounts = columnCharacterCount();
        try {
            if (headlineRow != null) {
                for (int i = 0; i < headlineRow.dataValueList.size(); i++) {
                    sb.append(bufferStringWithBlanksToLength(headlineRow.dataValueList.get(i), columnCharacterCounts.get(i)));
                }
                sb.append(SupportMethods.LF);
            }
            if (rows != null) {
                for (Row row : rows) {
                    for (int i = 0; i < row.dataValueList.size(); i++) {
                        sb.append(bufferStringWithBlanksToLength(row.dataValueList.get(i), columnCharacterCounts.get(i)));
                    }
                    sb.append(SupportMethods.LF);
                }
            }
        }
        catch (NullPointerException ne) {
            logger.error( ne.getMessage(), ne);
        }
        return sb.toString();
    }

    private List<Integer> columnCharacterCount(){
        List<Integer> columnCharacterCounts = new ArrayList<>();
        if(headlineRow != null){
            for(String dataValue : headlineRow.dataValueList){
                columnCharacterCounts.add(0);
            }
        }else if(rows.size() > 0){
            for(String dataValue : rows.get(0).dataValueList){
                columnCharacterCounts.add(0);
            }
        } else {
            return null;
        }
        if(headlineRow != null){
            for(int i = 0; i < headlineRow.dataValueList.size(); i++){
                if(headlineRow.dataValueList.get(i).length() > columnCharacterCounts.get(i)){
                    columnCharacterCounts.set(i, headlineRow.dataValueList.get(i).length());
                }
            }
        }
        if(rows.size() > 0){
            for(Row row : rows){
                for(int i = 0; i < row.dataValueList.size(); i++){
                    if(row.dataValueList.get(i).length() > columnCharacterCounts.get(i)){
                        columnCharacterCounts.set(i, row.dataValueList.get(i).length());
                    }
                }
            }
        }
        return columnCharacterCounts;
    }

    private String bufferStringWithBlanksToLength(String string, int length){
        StringBuilder sb = new StringBuilder(string);
        for(int i = 0; i < length - string.length() + 1; i++){
            sb.append(" ");
        }
        return sb.toString();
    }


    /**
     * A table is considered to have a headline if none of the data fields of the first
     * row consists solely of a number, or if the second row consists of a field with
     * only numbers while the first row doesn't.
     *
     * @return Return true if analysis finds probable cause for a headline
     */
    private boolean hasHeadlines(){
        if(rows.size() == 0) return false;
        for(int i = 0; i < rows.get(0).dataValueList.size(); i++){
            if(onlyIsNumbers(rows.get(0).dataValueList.get(i))) return false;
            if(rows.size() > 1 && onlyIsNumbers(rows.get(1).dataValueList.get(i))) return true;
        }
        return true;
    }

    /**
     * Checks if the string is a number string.
     *
     * @param instring The string to analyze.
     * @return Return true if it is a number (including solely digits, commas, spaces, or dots)
     */
    private boolean onlyIsNumbers(String instring){
        return SupportMethods.isRegexMatch(instring, "^[0-9|\\.|,|\\x020]*$");
    }

    public class Row{
        List<String> dataValueList = new ArrayList<>();

        public Row(String line, String delimiter){
            for(String value : line.split(delimiter)){
                dataValueList.add(value);
            }

        }

        public Row(String[] values){
            for(String value : values){
                this.dataValueList.add(value);
            }
        }

        @Override
        public String toString(){
            return String.join(",", dataValueList);
        }

        public String getValueForColumn(int columnNumber){
            return dataValueList.get(columnNumber);
        }


    }

}
