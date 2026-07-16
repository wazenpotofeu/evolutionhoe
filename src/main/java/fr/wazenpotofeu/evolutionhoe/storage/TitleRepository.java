package fr.wazenpotofeu.evolutionhoe.storage;

import fr.wazenpotofeu.evolutionhoe.data.models.TitleModel;
import fr.wazenpotofeu.evolutionhoe.database.DatabaseManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repository pour gérer les titres en base de données
 */
public class TitleRepository {

    private final JavaPlugin plugin;
    private final DatabaseManager databaseManager;

    public TitleRepository(JavaPlugin plugin, DatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
    }

    /**
     * Crée un nouveau titre
     */
    public int create(TitleModel title) throws SQLException {
        String sql = "INSERT INTO titles (hoe_id, title_name, color) VALUES (?, ?, ?)";
        
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, title.getHoeId());
            pstmt.setString(2, title.getTitleName());
            pstmt.setString(3, title.getColor());
            
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
     * Récupère tous les titres d'une houe
     */
    public List<TitleModel> findByHoeId(int hoeId) throws SQLException {
        String sql = "SELECT * FROM titles WHERE hoe_id = ? ORDER BY created_at DESC";
        List<TitleModel> titles = new ArrayList<>();
        
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, hoeId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    titles.add(mapResultSetToTitle(rs));
                }
            }
        }
        return titles;
    }

    /**
     * Récupère le dernier titre d'une houe
     */
    public Optional<TitleModel> findLatestByHoeId(int hoeId) throws SQLException {
        String sql = "SELECT * FROM titles WHERE hoe_id = ? ORDER BY created_at DESC LIMIT 1";
        
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, hoeId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToTitle(rs));
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Supprime un titre
     */
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM titles WHERE id = ?";
        
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        }
    }

    /**
     * Mappe un ResultSet vers une TitleModel
     */
    private TitleModel mapResultSetToTitle(ResultSet rs) throws SQLException {
        return TitleModel.builder()
                .id(rs.getInt("id"))
                .hoeId(rs.getInt("hoe_id"))
                .titleName(rs.getString("title_name"))
                .color(rs.getString("color"))
                .createdAt(rs.getTimestamp("created_at"))
                .build();
    }
}
