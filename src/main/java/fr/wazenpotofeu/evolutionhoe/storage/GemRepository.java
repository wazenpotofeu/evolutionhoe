package fr.wazenpotofeu.evolutionhoe.storage;

import fr.wazenpotofeu.evolutionhoe.data.models.GemModel;
import fr.wazenpotofeu.evolutionhoe.database.DatabaseManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repository pour gérer les gemmes en base de données
 */
public class GemRepository {

    private final JavaPlugin plugin;
    private final DatabaseManager databaseManager;

    public GemRepository(JavaPlugin plugin, DatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
    }

    /**
     * Crée une nouvelle gemme
     */
    public int create(GemModel gem) throws SQLException {
        String sql = "INSERT INTO gems (hoe_id, gem_type, slot) VALUES (?, ?, ?)";
        
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, gem.getHoeId());
            pstmt.setString(2, gem.getGemType());
            pstmt.setInt(3, gem.getSlot());
            
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
     * Récupère une gemme par son ID
     */
    public Optional<GemModel> findById(int id) throws SQLException {
        String sql = "SELECT * FROM gems WHERE id = ?";
        
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToGem(rs));
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Récupère toutes les gemmes d'une houe
     */
    public List<GemModel> findByHoeId(int hoeId) throws SQLException {
        String sql = "SELECT * FROM gems WHERE hoe_id = ? ORDER BY slot";
        List<GemModel> gems = new ArrayList<>();
        
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, hoeId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    gems.add(mapResultSetToGem(rs));
                }
            }
        }
        return gems;
    }

    /**
     * Récupère une gemme d'une houe à un slot spécifique
     */
    public Optional<GemModel> findByHoeIdAndSlot(int hoeId, int slot) throws SQLException {
        String sql = "SELECT * FROM gems WHERE hoe_id = ? AND slot = ?";
        
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, hoeId);
            pstmt.setInt(2, slot);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToGem(rs));
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Compte les gemmes d'une houe
     */
    public int countByHoeId(int hoeId) throws SQLException {
        String sql = "SELECT COUNT(*) as count FROM gems WHERE hoe_id = ?";
        
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, hoeId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("count");
                }
            }
        }
        return 0;
    }

    /**
     * Supprime une gemme
     */
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM gems WHERE id = ?";
        
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        }
    }

    /**
     * Mappe un ResultSet vers une GemModel
     */
    private GemModel mapResultSetToGem(ResultSet rs) throws SQLException {
        return GemModel.builder()
                .id(rs.getInt("id"))
                .hoeId(rs.getInt("hoe_id"))
                .gemType(rs.getString("gem_type"))
                .slot(rs.getInt("slot"))
                .createdAt(rs.getTimestamp("created_at"))
                .build();
    }
}
