package se.claremont.taf.dbinteraction;

import se.claremont.taf.core.testcase.TestCase;

public class MsSqlConnection extends DbConnection{

    private String dbConnectString;

    public MsSqlConnection(TestCase testCase, String dbUrl, String dbName, String userName, String password){
        super(testCase,"com.microsoft.sqlserver.jdbc.SQLServerDriver" );
        setDbUrl("jdbc:sqlserver://" + dbUrl + ";"
                        + "database=" + dbName +";"
                        + "user=" + userName +";"
                        + "password=" + password +";"
                        + "trustServerCertificate=false;"
                        + "loginTimeout=30;");
    }

    public MsSqlConnection(TestCase testCase, String dbConnectString){
        super(testCase, "com.microsoft.sqlserver.jdbc.SQLServerDriver");
        setDbUrl(dbConnectString);
    }

}
