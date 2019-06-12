package se.claremont.taf.dbinteraction;

import se.claremont.taf.core.logging.LogLevel;
import se.claremont.taf.core.testcase.TestCase;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MsSqlConnection implements DbConnection{

    private TestCase testCase;
    private String dbConnectString;
    private Connection connection;

    public MsSqlConnection(TestCase testCase, String dbUrl, String dbName, String userName, String password){
        if(testCase == null) testCase = new TestCase();
        this.testCase = testCase;
        dbConnectString =
                "jdbc:sqlserver://" + dbUrl + ";"
                        + "database=" + dbName +";"
                        + "user=" + userName +";"
                        + "password=" + password +";"
                        + "trustServerCertificate=false;"
                        + "loginTimeout=30;";
        registerExceptionIfDriverIsNotAccessible();
    }

    public MsSqlConnection(TestCase testCase, String dbConnectString){
        if(testCase == null) testCase = new TestCase();
        this.testCase = testCase;
        this.dbConnectString = dbConnectString;
        registerExceptionIfDriverIsNotAccessible();
    }

    private void registerExceptionIfDriverIsNotAccessible(){
        try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            String msg = "Could not find suitable JDBC driver for Ms SQL. " + e.toString();
            testCase.log(LogLevel.EXECUTION_PROBLEM, msg);
            System.out.println(msg);
            testCase.report();
        }
    }

    @Override
    public Connection connect() {
        try {
            testCase.log(LogLevel.DEBUG, "Connecting to DB with connect string '" + dbConnectString + "'.");
            Object o = Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
            DriverManager.registerDriver((Driver)o);
            connection = DriverManager.getConnection(dbConnectString);
        } catch (Exception e) {
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not connect to DB with connect string '" + dbConnectString + "'. " + e.toString());
            System.out.println(e.toString());
        }
        return connection;
    }

    @Override
    public void closeConnection() {
        if(connection == null)return;
        try {
            testCase.log(LogLevel.DEBUG, "Closing connection to DB.");
            connection.close();
        } catch (SQLException e) {
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not close DB connection. " + e.toString());
            System.out.println(e.toString());
        }
    }
}
