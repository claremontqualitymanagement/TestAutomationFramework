package se.claremont.taf.dbinteraction;

import org.junit.Ignore;
import org.junit.Test;
import se.claremont.taf.core.testset.TestSet;

import java.sql.ResultSet;

public class MsSqlDbInteractionTest extends TestSet {

    @Test
    @Ignore
    public void connectTest(){
        String connectString = "jdbc:sqlserver://127.0.0.1:1443;database=TestDatabase1;loginTimeout=5;";
        MsSqlConnection msSqlConnection = new MsSqlConnection(currentTestCase(), connectString);
        SqlInteractionManager sql = new SqlInteractionManager(currentTestCase(), msSqlConnection);
        ResultSet resultSet = sql.executeQuery("SELECT CITY FROM Addresses WHERE Name = 'Damberg'").resultSet;
    }
}
