package se.claremont.taf.dbinteraction;

import org.junit.Ignore;
import org.junit.Test;
import se.claremont.taf.core.support.tableverification.CellMatchingType;
import se.claremont.taf.core.support.tableverification.TableData;
import se.claremont.taf.core.testset.TestSet;

import java.sql.ResultSet;

public class MsSqlDbInteractionTest extends TestSet {

    @Test
    @Ignore
    public void connectTest(){

        //Man anger parametrar för uppkoppling mot MS SQL. Detta är googlingsbart
        String connectString = "jdbc:sqlserver://192.168.100.22:1442;" +
                "database=TestDb;" +
                "user=secretUserName;" +
                "password=topSecret;" +
                "trustServerCertificate=true;" +
                "loginTimeout=30;";

        //Man initierar en databasinteraktion med lämplig drivrutinstyp
        SqlInteractionManager sql = new SqlInteractionManager(
                currentTestCase(),
                new MsSqlConnection(
                        currentTestCase(),
                        connectString));

        //Med den angivna uppkopplingen kan man exekvera SQL och få tillbaka svar
        DbResponse response = sql.executeQuery("SELECT CITY FROM Addresses WHERE Name = 'Damberg'");

        //Svaren kan verifieras på lite olika sätt, och med builder pattern om man så önskar
        response.verify()
                .columnCount(3)
                .responseTime(1000) //MaximumAllowedResponseTimeInMilliseconds
                .recordExist(
                        "City:Sollentuna;PostalCode:1278",
                        CellMatchingType.EXACT_MATCH)
                .recordDoesNotExist(
                        "ShoeSize:99",
                        CellMatchingType.CONTAINS_MATCH);

        //Man kan komma åt result set från SQL query direkt
        ResultSet resultSet = response.resultSet;

        //Om man vill kan man också t.ex. kolla hur lång tid det tog att få svar, eller kolla på resultatet som en TAF Tabell
        TableData tableData = response.resultDataTable;
        long responseTime = response.responseTimeInMilliseconds;

        //Och t.ex. loopa igenom de kolumnnamn som hittades i ett svar:
        System.out.println("Column names in result set:" + String.join(", ", response.getColumnNames()));
    }
}
