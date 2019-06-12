package se.claremont.taf.dbinteraction;

import se.claremont.taf.core.logging.LogLevel;
import se.claremont.taf.core.testcase.TestCase;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySqlConnection implements DbConnection {

    private TestCase testCase;
    private String dbName;
    private String dbUrl;
    private Connection connection;
    private String username;
    private String password;

    public MySqlConnection(TestCase testCase, String dbUrl, String dbName, String username, String password){
        if(testCase == null) testCase = new TestCase();
        this.testCase = testCase;
        this.dbName = dbName;
        this.dbUrl = dbUrl;
        this.username = username;
        this.password = password;
        try{
            DriverManager.registerDriver((Driver)Class.forName("com.mysql.jdbc.Driver").newInstance());
        } catch (Exception e) {
            testCase.log(LogLevel.EXECUTION_PROBLEM, "No JDBC driver for MySql found in classpath. Make sure it exist. " + e.toString());
        }
    }

    @Override
    public Connection connect() {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            testCase.log(LogLevel.DEBUG, "Connecting to DB '" + dbUrl + dbName + "'.");
            connection = DriverManager.getConnection(dbUrl + dbName, username, password);
            testCase.log(LogLevel.DEBUG, "Connected successfully to DB '" + dbUrl + dbName + "' as '" + username + "'.");
        } catch(Exception e){
            testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not connect to DB '" + dbUrl + dbName + "' as '" + username + "'. Error: " + e.toString());
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
