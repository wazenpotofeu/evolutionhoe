package fr.wazenpotofeu.evolutionhoe.storage;

import fr.wazenpotofeu.evolutionhoe.data.models.MutationModel;
import fr.wazenpotofeu.evolutionhoe.database.DatabaseManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repository pour gérer les mutations en base de données
 */
public class MutationRepository {

    private final JavaPlugin plugin;
    private final DatabaseManager databaseManager;

    public MutationRepository(JavaPlugin plugin, DatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
    }

    /**
     * Crée une nouvelle mutation
     */
    public int create(MutationModel mutation) throws SQLException {
        String sql = "INSERT INTO mutations (hoe_id, mutation_type) VALUES (?, ?)";
        
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, mutation.getHoeId());
            pstmt.setString(2, mutation.getMutationType());
            
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
     * Récupère une mutation par son ID
     */
    public Optional<MutationModel> findById(int id) throws SQLException {
        String sql = "SELECT * FROM mutations WHERE id = ?";
        
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToMutation(rs));
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Récupère toutes les mutations d'une houe
     */
    public List<MutationModel> findByHoeId(int hoeId) throws SQLException {
        String sql = "SELECT * FROM mutations WHERE hoe_id = ? ORDER BY created_at DESC";
        List<MutationModel> mutations = new ArrayList<>();
        
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, hoeId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    mutations.add(mapResultSetToMutation(rs));
                }
            }
        }
        return mutations;
    }

    /**
     * Récupère la dernière mutation d'une houe
     */
    public Optional<MutationModel> findLatestByHoeId(int hoeId) throws SQLException {
        String sql = "SELECT * FROM mutations WHERE hoe_id = ? ORDER BY created_at DESC LIMIT 1";
        
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, hoeId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToMutation(rs));
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Compte les mutations d'une houe
     */
    public int countByHoeId(int hoeId) throws SQLException {
        String sql = "SELECT COUNT(*) as count FROM mutations WHERE hoe_id = ?";
        
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
     * Supprime une mutation
     */
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM mutations WHERE id = ?";
        
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        }
    }

    /**
     * Mappe un ResultSet vers une MutationModel
     */
    private MutationModel mapResultSetToMutation(ResultSet rs) throws SQLException {
        return MutationModel.builder()
                .id(rs.getInt("id"))
                .hoeId(rs.getInt("hoe_id"))
                .mutationType(rs.getString("mutation_type"))
                .createdAt(rs.getTimestamp("created_at"))
                .build();
    }
}
