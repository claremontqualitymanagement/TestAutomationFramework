package se.claremont.autotest.dbinteraction;

import se.claremont.autotest.common.logging.LogLevel;
import se.claremont.autotest.common.support.tableverification.CellMatchingType;
import se.claremont.autotest.common.support.tableverification.TableData;
import se.claremont.autotest.common.testcase.TestCase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * Created by jordam on 2016-11-29.
 */
public class SqlInteractionManager {
    Connection connection = null;
    TestCase testCase;
    String dbName;
    String dbUrl;

    public SqlInteractionManager(TestCase testCase, String dbUrl, String dbName, String username, String password){
        this.testCase = testCase;
        this.dbName = dbName;
        this.dbUrl = dbUrl;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            testCase.log(LogLevel.DEBUG, "Connecting to DB '" + dbUrl + dbName + "'.");
            connection = DriverManager.getConnection(dbUrl + dbName, username, password);
            testCase.log(LogLevel.DEBUG, "Connected successfully to DB '" + dbUrl + dbName + "' as '" + username + "'.");
        } catch(Exception e){
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not connect to DB '" + dbUrl + dbName + "' as '" + username + "'. Error: " + e.getMessage() );
        }
    }

    private static int getColumnCountFromResultSet(ResultSet resultSet){
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
                columnCount++;
            } catch (SQLException e) {
            }
            if(content == null){
                continueToNextColumn = false;
            }
        }
        return  columnCount;
    }

    private static String resultSetToString(ResultSet resultSet){
        String returnString = "";
        if(resultSet == null) return "";
        int columnCount = getColumnCountFromResultSet(resultSet);
        List<String> headlineRow = new ArrayList<>();
        for(int i = 1; i < columnCount ; i++){
            try {
                headlineRow.add(resultSet.getMetaData().getColumnName(i));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        returnString += String.join(";", headlineRow) + System.lineSeparator();
        try{
            resultSet.beforeFirst();
            while (resultSet.next()) {
                List<String> row = new ArrayList<>();
                for(int i = 0; i < columnCount-1; i++){
                    row.add(resultSet.getString(i+1).replace("\n", ","));
                }
                returnString += String.join(";", row) + System.lineSeparator();
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        System.out.println(returnString);
        return returnString;
    }

    private TableData execute(String sql) {
        Statement stmt = null;
        ResultSet resultSet = null;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            stmt = connection.createStatement();

            testCase.log(LogLevel.DEBUG, "Executing SQL '" + sql + "' to DB '" + dbName + "' at server '" + dbUrl + "'.");
            boolean success = stmt.execute(sql);
            testCase.log(LogLevel.EXECUTED, "Executed SQL '" + sql + "' to DB + '" + dbName + "' at server '" + dbUrl + "'.");
            if(success){
                resultSet = stmt.getResultSet();
            }
        }catch(SQLException se){
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not execute SQL '" + sql + "' to DB '" + dbName + "' at server '" + dbUrl + "'. Error: " + se.getMessage());
        }catch(Exception e){
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not execute SQL '" + sql + "' to DB '" + dbName + "' at server '" + dbUrl + "'. Error: " + e.getMessage());
        }
        String returnString = resultSetToString(resultSet);
        TableData tableData = new TableData(testCase, "SQL query result set", returnString);
        testCase.logDifferentlyToTextLogAndHtmlLog(LogLevel.DEBUG, "Results from query:" + System.lineSeparator() + tableData.toString(), "Results from query:<br>" + tableData.toHtml());
        return tableData;
    }

    public void verifyRowExistInResultsFromQuery(String sqlQuery, String headlineColonValueSemicolonSeparatedString, boolean regex){
        TableData tableData = execute(sqlQuery);
        if(regex){
            tableData.verifyRowExist(headlineColonValueSemicolonSeparatedString, CellMatchingType.REGEX_MATCH);
        }else {
            tableData.verifyRowExist(headlineColonValueSemicolonSeparatedString, CellMatchingType.CONTAINS_MATCH);
        }
    }

    public void verifyRowExistInResultsFromQuery(String sqlQuery, String headlineColonValueSemicolonSeparatedString, CellMatchingType cellMatchingType){
        TableData tableData = execute(sqlQuery);
        tableData.verifyRowExist(headlineColonValueSemicolonSeparatedString, cellMatchingType);
    }

    public void verifyRowExistInResultsFromQuery(String tableName, String headlineColonValueSemicolonSeparatedString){
        String[] pairs = headlineColonValueSemicolonSeparatedString.split(";");
        List<String> searchString = new ArrayList<>();
        for(String pair : pairs){
            if(pair.contains("=")){
                searchString.add(pair.split("=")[0] + " = '" + pair.split("=")[pair.split("=").length] + "'");
            }
        }
        String sql = "SELECT * FROM " + tableName + " WHERE " + String.join(" AND ", searchString) + ";";
        TableData td = execute(sql);
        if(td.dataRowCount() == 0){
            sql = "SELECT * FROM " + tableName + " WHERE " + String.join(" OR ", searchString) + ";";
            td = execute(sql);
            td.verifyRowExist(headlineColonValueSemicolonSeparatedString, CellMatchingType.CONTAINS_MATCH);
        } else {
            testCase.log(LogLevel.VERIFICATION_PASSED, "Found '" + headlineColonValueSemicolonSeparatedString + "' in " + tableName + ".");
        }

    }

    public void closeConnection() {
        if (connection == null) {
            return;
        }
        try {
            connection.close();
            testCase.log(LogLevel.DEBUG, "Closed connection to DB '" + dbName + "' at server '" + dbUrl + "'.");
        } catch (SQLException e) {
            testCase.log(LogLevel.DEBUG, "Could not close connection to DB '" + dbName + "' at server '" + dbUrl + "'. Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
