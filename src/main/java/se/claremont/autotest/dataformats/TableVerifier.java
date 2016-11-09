package se.claremont.autotest.dataformats;

import se.claremont.autotest.common.LogLevel;
import se.claremont.autotest.common.TestCase;
import se.claremont.autotest.support.SupportMethods;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * @deprecated Don't use this class. Use the TableData class instead, for better logging and more robust scenarios.
 * Created by jordam on 2016-10-18.
 */
@Deprecated
public class TableVerifier {
    TestCase testCase;
    Table table;

    public TableVerifier(Table table, TestCase testCase){
        this.table = table;
        this.testCase = testCase;
        testCase.logDifferentlyToTextLogAndHtmlLog(LogLevel.DEBUG, "Registering table for verification. Initial content:" + SupportMethods.LF + table.toString(), "Registering table for verification.<br>" + table.toHtmlTable());
    }

    public void verifyHeadlineExist(String headlineName) {
        if(table.headLineExist(headlineName)){
            testCase.log(LogLevel.VERIFICATION_PASSED, "Found the headline '" + headlineName + "' among the table headlines '" + table.headlineRow.toString() + "'.");
        } else {
            testCase.log(LogLevel.VERIFICATION_FAILED, "Could not find the headline '" + headlineName + "'. Found headlines '" + table.headlineRow.toString() + "'.");
        }
    }



    public void verifyRowExist(String[] headlines, String[] regexpPatternsInSameOrderAsGivenHeadlines){
        List<Table.Row> rowsMatching = new ArrayList<>();
        if(headlines == null || regexpPatternsInSameOrderAsGivenHeadlines == null) {
            testCase.log(LogLevel.VERIFICATION_PROBLEM, "Cannot verify table against null headline or regexpPattern.");
            return;
        }
        Table tempTable = new Table(table.headlineRow, table.rows);
        for(int i = 0; i < headlines.length; i++){
            tempTable = new Table(table.headlineRow, tempTable.rowsWithMatchingValueForHeadline(headlines[i], regexpPatternsInSameOrderAsGivenHeadlines[i]));
        }
        if(tempTable.rows.size() > 0){
            String textResult = "Searched for the headline(s) '" + String.join("', '", headlines) +"', with the corresponding regular expressions patterns '" + String.join("', '", regexpPatternsInSameOrderAsGivenHeadlines) + "' and found the following results:" + SupportMethods.LF + tempTable.toString();
            String htmlResult = "Searched for the headline(s) '" + String.join("', '", headlines) +"', with the corresponding regular expressions patterns '" + String.join("', '", regexpPatternsInSameOrderAsGivenHeadlines) + "' and found the following results:<br>" + SupportMethods.LF + tempTable.toHtmlTable();
            testCase.logDifferentlyToTextLogAndHtmlLog(LogLevel.VERIFICATION_PASSED, textResult, htmlResult);
        } else {
            testCase.logDifferentlyToTextLogAndHtmlLog(LogLevel.VERIFICATION_FAILED,
                "Searched for the headline(s) '" + String.join("', '", headlines) +"', with the corresponding regular expressions patterns '" + String.join("', '", regexpPatternsInSameOrderAsGivenHeadlines) + "', but couldn't find it in:" + SupportMethods.LF + table.toString(),
                "Searched for the headline(s) '" + String.join("', '", headlines) +"', with the corresponding regular expressions patterns '" + String.join("', '", regexpPatternsInSameOrderAsGivenHeadlines) + "', but couldn't find it in:<br>" + table.toHtmlTable());
        }
    }
    enum RowAssessmentStatus{
        UNEVALUATED,
        NO_MATCH,
        ALL_MATCH,
        PARTIAL_MATCH
    }

    class TableAssessor{
        TestCase testCase;
        Table table;
        List<RowAssessor> rowAssessorList = new ArrayList<>();

        public TableAssessor(Table table, TestCase testCase){
            this.table = table;
            this.testCase = testCase;
        }


        class RowAssessor{
            RowAssessmentStatus rowAssessmentStatus = RowAssessmentStatus.UNEVALUATED;
            Table.Row row;

            public RowAssessor(Table.Row row){
                this.row = row;
            }
        }
    }

}
