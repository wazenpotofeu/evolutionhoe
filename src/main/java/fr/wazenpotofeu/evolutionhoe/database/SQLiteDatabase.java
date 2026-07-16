package fr.wazenpotofeu.evolutionhoe.database;

import org.bukkit.plugin.java.JavaPlugin;
import org.sqlite.SQLiteDataSource;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Gestionnaire SQLite pour EvolutionHoe
 */
public class SQLiteDatabase implements IDatabase {

    private final JavaPlugin plugin;
    private final File databaseFile;
    private SQLiteDataSource dataSource;
    private Connection connection;

    public SQLiteDatabase(JavaPlugin plugin) {
        this.plugin = plugin;
        this.databaseFile = new File(plugin.getDataFolder(), "data.db");
    }

    @Override
    public void connect() throws SQLException {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        dataSource = new SQLiteDataSource();
        dataSource.setUrl("jdbc:sqlite:" + databaseFile.getAbsolutePath());
        connection = dataSource.getConnection();
        
        plugin.getLogger().info("✓ Connecté à la base de données SQLite");
    }

    @Override
    public void disconnect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            plugin.getLogger().info("✓ Déconnecté de la base de données SQLite");
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connect();
        }
        return connection;
    }

    @Override
    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public void createTables() throws SQLException {
        // Tables seront créées dans DatabaseManager
    }
}
