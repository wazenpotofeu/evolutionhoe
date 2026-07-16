package fr.wazenpotofeu.evolutionhoe.database;

import fr.wazenpotofeu.evolutionhoe.config.ConfigManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Gestionnaire principal de la base de données
 * Initialise et gère les connexions à la BD
 */
public class DatabaseManager {

    private final JavaPlugin plugin;
    private final ConfigManager configManager;
    private IDatabase database;

    public DatabaseManager(JavaPlugin plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
    }

    /**
     * Initialise la base de données selon la configuration
     */
    public void initialize() {
        try {
            String type = configManager.getMainConfig().getString("database.type", "sqlite").toLowerCase();
            
            if (type.equals("mysql")) {
                initializeMysql();
            } else {
                initializeSqlite();
            }
            
            createTables();
            plugin.getLogger().info("✓ Base de données initialisée avec succès");
        } catch (SQLException e) {
            plugin.getLogger().severe("✗ Erreur lors de l'initialisation de la base de données");
            e.printStackTrace();
        }
    }

    /**
     * Initialise SQLite
     */
    private void initializeSqlite() throws SQLException {
        database = new SQLiteDatabase(plugin);
        database.connect();
    }

    /**
     * Initialise MySQL
     */
    private void initializeMysql() throws SQLException {
        String host = configManager.getMainConfig().getString("database.mysql.host", "localhost");
        int port = configManager.getMainConfig().getInt("database.mysql.port", 3306);
        String dbName = configManager.getMainConfig().getString("database.mysql.database", "evolutionhoe");
        String username = configManager.getMainConfig().getString("database.mysql.username", "root");
        String password = configManager.getMainConfig().getString("database.mysql.password", "password");
        boolean ssl = configManager.getMainConfig().getBoolean("database.mysql.ssl", false);
        
        database = new MySQLDatabase(plugin, host, port, dbName, username, password, ssl);
        database.connect();
    }

    /**
     * Crée toutes les tables nécessaires
     */
    private void createTables() throws SQLException {
        try (Connection conn = database.getConnection(); Statement stmt = conn.createStatement()) {
            
            // Table des joueurs et leurs houes
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS hoes (" +
                "  id INTEGER PRIMARY KEY AUTO_INCREMENT," +
                "  uuid VARCHAR(36) UNIQUE NOT NULL," +
                "  player_name VARCHAR(50) NOT NULL," +
                "  dna VARCHAR(20) UNIQUE NOT NULL," +
                "  level INTEGER DEFAULT 1," +
                "  xp BIGINT DEFAULT 0," +
                "  prestige INTEGER DEFAULT 0," +
                "  fortune DOUBLE DEFAULT 0.0," +
                "  sell_multiplier DOUBLE DEFAULT 1.0," +
                "  total_harvested BIGINT DEFAULT 0," +
                "  total_earned BIGINT DEFAULT 0," +
                "  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "  nbt_data LONGTEXT" +
                ")"
            );
            
            // Table des mutations
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS mutations (" +
                "  id INTEGER PRIMARY KEY AUTO_INCREMENT," +
                "  hoe_id INTEGER NOT NULL," +
                "  mutation_type VARCHAR(50) NOT NULL," +
                "  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "  FOREIGN KEY (hoe_id) REFERENCES hoes(id) ON DELETE CASCADE" +
                ")"
            );
            
            // Table des traits
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS traits (" +
                "  id INTEGER PRIMARY KEY AUTO_INCREMENT," +
                "  hoe_id INTEGER NOT NULL," +
                "  trait_type VARCHAR(50) NOT NULL," +
                "  bonus DOUBLE NOT NULL," +
                "  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "  FOREIGN KEY (hoe_id) REFERENCES hoes(id) ON DELETE CASCADE" +
                ")"
            );
            
            // Table des gemmes
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS gems (" +
                "  id INTEGER PRIMARY KEY AUTO_INCREMENT," +
                "  hoe_id INTEGER NOT NULL," +
                "  gem_type VARCHAR(50) NOT NULL," +
                "  slot INTEGER NOT NULL," +
                "  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "  FOREIGN KEY (hoe_id) REFERENCES hoes(id) ON DELETE CASCADE" +
                ")"
            );
            
            // Table des upgrades
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS upgrades (" +
                "  id INTEGER PRIMARY KEY AUTO_INCREMENT," +
                "  hoe_id INTEGER NOT NULL," +
                "  upgrade_name VARCHAR(50) NOT NULL," +
                "  level INTEGER DEFAULT 1," +
                "  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "  FOREIGN KEY (hoe_id) REFERENCES hoes(id) ON DELETE CASCADE" +
                ")"
            );
            
            // Table des statistiques
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS statistics (" +
                "  id INTEGER PRIMARY KEY AUTO_INCREMENT," +
                "  hoe_id INTEGER NOT NULL," +
                "  stat_name VARCHAR(100) NOT NULL," +
                "  stat_value DOUBLE NOT NULL," +
                "  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "  FOREIGN KEY (hoe_id) REFERENCES hoes(id) ON DELETE CASCADE" +
                ")"
            );
            
            // Table des titres
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS titles (" +
                "  id INTEGER PRIMARY KEY AUTO_INCREMENT," +
                "  hoe_id INTEGER NOT NULL," +
                "  title_name VARCHAR(100) NOT NULL," +
                "  color VARCHAR(20)," +
                "  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "  FOREIGN KEY (hoe_id) REFERENCES hoes(id) ON DELETE CASCADE" +
                ")"
            );
            
            // Table des historiques de vente
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS sales_history (" +
                "  id INTEGER PRIMARY KEY AUTO_INCREMENT," +
                "  hoe_id INTEGER NOT NULL," +
                "  amount BIGINT NOT NULL," +
                "  multiplier DOUBLE NOT NULL," +
                "  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "  FOREIGN KEY (hoe_id) REFERENCES hoes(id) ON DELETE CASCADE" +
                ")"
            );
            
            plugin.getLogger().info("✓ Toutes les tables ont été créées");
        }
    }

    /**
     * Récupère la connexion à la base de données
     */
    public Connection getConnection() throws SQLException {
        return database.getConnection();
    }

    /**
     * Vérifie si la base de données est connectée
     */
    public boolean isConnected() {
        return database != null && database.isConnected();
    }

    /**
     * Ferme la connexion à la base de données
     */
    public void shutdown() {
        try {
            if (database != null) {
                database.disconnect();
            }
        } catch (SQLException e) {
            plugin.getLogger().warning("Erreur lors de la fermeture de la base de données");
            e.printStackTrace();
        }
    }
}
