package fr.wazenpotofeu.evolutionhoe.database;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Interface pour l'abstraction de la base de données
 */
public interface IDatabase {
    void connect() throws SQLException;
    void disconnect() throws SQLException;
    Connection getConnection() throws SQLException;
    boolean isConnected();
    void createTables() throws SQLException;
}
