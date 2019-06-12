package se.claremont.taf.dbinteraction;

import java.sql.Connection;

public interface DbConnection {

    Connection connect();
    void closeConnection();

}
