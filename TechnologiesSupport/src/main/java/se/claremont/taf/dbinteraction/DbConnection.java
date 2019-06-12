package se.claremont.taf.dbinteraction;

import se.claremont.taf.core.logging.LogLevel;
import se.claremont.taf.core.testcase.TestCase;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public abstract class DbConnection {

    public TestCase testCase;
    public Connection connection;
    private String fullClassNameExistingInDbDriver;
    public String dbUrl;
    public String password;
    public String userName;
    private Properties dbConnectionProperties;

    public DbConnection(TestCase testCase, String fullClassNameExistingInDbDriver){
        this.testCase = testCase;
        this.fullClassNameExistingInDbDriver = fullClassNameExistingInDbDriver;
        if(fullClassNameExistingInDbDriver == null || fullClassNameExistingInDbDriver.trim().equals("")){
            if(testCase == null){
                System.out.println("ERROR: The class '" + this.getClass().getName() + "' fails to provide a full class name of an identifying class representing the DB driver to make sure it's loaded.");
            }else {
                testCase.log(LogLevel.EXECUTION_PROBLEM, "The class '" + this.getClass().getName() + "' fails to provide a full class name of an identifying class representing the DB driver to make sure it's loaded.");
            }
            return;
        }
        registerExceptionIfDriverIsNotAccessible();
        try{
            DriverManager.registerDriver((Driver)Class.forName(fullClassNameExistingInDbDriver).newInstance());
        } catch (Exception e) {
            String msg = "No suitable JDBC driver with class '" + fullClassNameExistingInDbDriver + "' found in classpath. Make sure it exist (by for example maven, gradle, classpath). " + e.toString();
            if(testCase == null){
                System.out.println("ERROR: " + msg);
            } else {
                testCase.log(LogLevel.EXECUTION_PROBLEM, msg);
            }
        }

    }

    public void setDbConnectProperties(Properties dbConnectionProperties){
        this.dbConnectionProperties  =  dbConnectionProperties;
    }

    public void setDbUrl(String dbUrl){
        this.dbUrl = dbUrl;
    }

    public void setDbUser(String userName, String password){
        this.userName = userName;
        this.password = password;
    }

    public Connection connect() {
        try{
            if(testCase != null)
                testCase.log(LogLevel.DEBUG, "Connecting to DB '" + dbUrl + "'.");

            if(userName == null && password == null && dbConnectionProperties == null && dbUrl != null) {
                connection = DriverManager.getConnection(dbUrl);
            } else if(dbConnectionProperties != null){
                connection = DriverManager.getConnection(dbUrl, dbConnectionProperties);
            } else if(dbUrl == null){
                if(testCase == null){
                    System.out.println("ERROR: Need at least an URL to DB to connect.");
                } else {
                    testCase.log(LogLevel.EXECUTION_PROBLEM, "Need at least an URL to DB to connect.");
                    return null;
                }
            } else {
                connection = DriverManager.getConnection(dbUrl, userName, password);
            }

            if(testCase != null)
                testCase.log(LogLevel.DEBUG, "Connected successfully to DB '" + dbUrl + "' as '" + userName + "'.");

        } catch(Exception e){
            if(testCase != null){
                testCase.log(LogLevel.EXECUTION_PROBLEM, "Could not connect to DB '" + dbUrl + "'." +
                    System.lineSeparator() +
                    "Error: " + e.toString());
            } else {
                System.out.println("ERROR: Could not connect to DB '" + dbUrl + "'. " + e.toString());
            }
        }
        return connection;
    }

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

    private void registerExceptionIfDriverIsNotAccessible(){
        try{
            Class.forName(fullClassNameExistingInDbDriver);
        } catch (ClassNotFoundException e) {
            String msg = "Could not find suitable JDBC driver for DB connection. " + e.toString();
            if(testCase == null){
                System.out.println("ERROR: " + msg);
            } else {
                testCase.log(LogLevel.EXECUTION_PROBLEM, msg);
                testCase.report();
            }
        }
    }

}
