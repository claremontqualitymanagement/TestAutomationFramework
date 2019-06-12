package se.claremont.taf.dbinteraction;

import se.claremont.taf.core.logging.LogLevel;
import se.claremont.taf.core.support.tableverification.CellMatchingType;
import se.claremont.taf.core.support.tableverification.TableData;
import se.claremont.taf.core.testcase.TestCase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * Created by jordam on 2016-11-29.
 */
@SuppressWarnings("WeakerAccess")
public class SqlInteractionManager {
    private TestCase testCase;
    private DbConnection dbConnection;

    public SqlInteractionManager(TestCase testCase, DbConnection dbConnection){
        this.testCase = testCase;
        this.dbConnection = dbConnection;
    }

    public String executeAndRetrieveGeneratedKey(String sql){
        ResultSet resultSet = null;
        PreparedStatement prepsInsertProduct = null;
        String generatedKey = null;
        try {
            Connection connection = dbConnection.connect();
            testCase.log(LogLevel.DEBUG, "Executing SQL statement '" + sql + "'.");
            prepsInsertProduct = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            prepsInsertProduct.execute();
            resultSet = prepsInsertProduct.getGeneratedKeys();
            while (resultSet.next()) {
                generatedKey = resultSet.getString(1);
                testCase.log(LogLevel.DEBUG, "Retreived generated key '" + generatedKey + "' from executing SQL statement '" + sql + "'.");
            }
        } catch (SQLException e) {
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not get result set from executing SQL statement '" + sql + "'. " + e.toString());
            System.out.println(e.toString());
        } finally {
            dbConnection.closeConnection();
        }
        return  generatedKey;
    }

    public DbResponse executeQuery(String query){
        Statement statement = null;
        ResultSet resultSet = null;
        DbResponse dbResponse = null;
        long responseTimeStart = System.currentTimeMillis();
        try {
            Connection connection = dbConnection.connect();
            if(connection == null){
                testCase.log(LogLevel.EXECUTION_PROBLEM, "Cannot connect to DB. Halting further execution. Check DEBUG log for details.");
                testCase.report();
            }
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            if(resultSet == null) return null;
            StringBuilder sb = new StringBuilder();
            int columnCount = resultSet.getMetaData().getColumnCount();
            while(resultSet.next()){
                for(int i = 1;i<=columnCount;i++){
                    sb.append(resultSet.getString(i)).append(";");
                }
                sb.append(System.lineSeparator());
            }
            testCase.log(LogLevel.DEBUG, "Executed query '" + query + "' and received the following result set:" + System.lineSeparator() + sb.toString());
        } catch (SQLException e) {
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not receive result set from query '" + query + "'. " + e.toString());
            System.out.println(e.toString());
        } finally {
            dbConnection.closeConnection();
            dbResponse = new DbResponse(testCase, resultSet, System.currentTimeMillis() - responseTimeStart);
        }
        return dbResponse;
    }

    public static int getColumnCountFromResultSet(ResultSet resultSet){
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
            } catch (SQLException ignored) {
            }
            if(content == null){
                continueToNextColumn = false;
            }
        }
        return  columnCount;
    }

    public static String resultSetToString(ResultSet resultSet){
        StringBuilder returnString = new StringBuilder();
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
        returnString.append(String.join(";", headlineRow)).append(System.lineSeparator());
        try{
            resultSet.beforeFirst();
            while (resultSet.next()) {
                List<String> row = new ArrayList<>();
                for(int i = 0; i < columnCount-1; i++){
                    row.add(resultSet.getString(i+1).replace("\n", ","));
                }
                returnString.append(String.join(";", row)).append(System.lineSeparator());
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        System.out.println(returnString);
        return returnString.toString();
    }

}
