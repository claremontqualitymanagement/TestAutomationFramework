package se.claremont.taf.dbinteraction;

import se.claremont.taf.core.support.tableverification.TableData;
import se.claremont.taf.core.testcase.TestCase;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DbResponse {
    public ResultSet resultSet;
    public long responseTimeInMilliseconds;
    private TestCase testCase;
    public TableData resultDataTable;


    protected DbResponse(TestCase testCase, ResultSet resultSet, long responseTimeInMilliseconds){
        this.resultSet = resultSet;
        this.testCase = testCase;
        this.responseTimeInMilliseconds = responseTimeInMilliseconds;
        String returnString = SqlInteractionManager.resultSetToString(resultSet);
        resultDataTable = new TableData(testCase, "SQL query result set", returnString);
    }

    public String[] getColumnNames(){
        if(resultSet == null) return new String[0];
        List<String> columnNames = new ArrayList<>();
        int columnCount = 1;
        boolean continueToNextColumn = true;
        try {
            resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        while (continueToNextColumn){
            String content = null;
            try {
                content = resultSet.getString(columnCount);
                columnNames.add(content);
                columnCount++;
            } catch (SQLException ignored) {
            }
            if(content == null){
                continueToNextColumn = false;
            }
        }
        return  columnNames.toArray(new String[0]);
    }



    @Override
    public String toString(){
        return SqlInteractionManager.resultSetToString(resultSet) +
                System.lineSeparator() +
                "(Response time was " + responseTimeInMilliseconds + " ms.)";
    }

    public DbVerificationMethods verify(){
        return new DbVerificationMethods(testCase, this);
    }
}
