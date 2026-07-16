package fr.wazenpotofeu.evolutionhoe.storage;

import fr.wazenpotofeu.evolutionhoe.data.models.UpgradeModel;
import fr.wazenpotofeu.evolutionhoe.database.DatabaseManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repository pour gérer les upgrades en base de données
 */
public class UpgradeRepository {

    private final JavaPlugin plugin;
    private final DatabaseManager databaseManager;

    public UpgradeRepository(JavaPlugin plugin, DatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
    }

    /**
     * Crée un nouvel upgrade
     */
    public int create(UpgradeModel upgrade) throws SQLException {
        String sql = "INSERT INTO upgrades (hoe_id, upgrade_name, level) VALUES (?, ?, ?)";
        
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, upgrade.getHoeId());
            pstmt.setString(2, upgrade.getUpgradeName());
            pstmt.setInt(3, upgrade.getLevel());
            
            pstmt.executeUpdate();
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        }
        return -1;
    }

    /**
     * Récupère un upgrade par son ID
     */
    public Optional<UpgradeModel> findById(int id) throws SQLException {
        String sql = "SELECT * FROM upgrades WHERE id = ?";
        
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToUpgrade(rs));
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Récupère tous les upgrades d'une houe
     */
    public List<UpgradeModel> findByHoeId(int hoeId) throws SQLException {
        String sql = "SELECT * FROM upgrades WHERE hoe_id = ?";
        List<UpgradeModel> upgrades = new ArrayList<>();
        
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, hoeId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    upgrades.add(mapResultSetToUpgrade(rs));
                }
            }
        }
        return upgrades;
    }

    /**
     * Récupère un upgrade spécifique d'une houe
     */
    public Optional<UpgradeModel> findByHoeIdAndName(int hoeId, String upgradeName) throws SQLException {
        String sql = "SELECT * FROM upgrades WHERE hoe_id = ? AND upgrade_name = ?";
        
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, hoeId);
            pstmt.setString(2, upgradeName);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToUpgrade(rs));
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Met à jour un upgrade
     */
    public boolean update(UpgradeModel upgrade) throws SQLException {
        String sql = "UPDATE upgrades SET level = ? WHERE id = ?";
        
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, upgrade.getLevel());
            pstmt.setInt(2, upgrade.getId());
            
            return pstmt.executeUpdate() > 0;
        }
    }

    /**
     * Supprime un upgrade
     */
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM upgrades WHERE id = ?";
        
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        }
    }

    /**
     * Mappe un ResultSet vers une UpgradeModel
     */
    private UpgradeModel mapResultSetToUpgrade(ResultSet rs) throws SQLException {
        return UpgradeModel.builder()
                .id(rs.getInt("id"))
                .hoeId(rs.getInt("hoe_id"))
                .upgradeName(rs.getString("upgrade_name"))
                .level(rs.getInt("level"))
                .createdAt(rs.getTimestamp("created_at"))
                .build();
    }
}
