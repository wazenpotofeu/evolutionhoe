package fr.wazenpotofeu.evolutionhoe.storage;

import fr.wazenpotofeu.evolutionhoe.data.models.HoeModel;
import fr.wazenpotofeu.evolutionhoe.database.DatabaseManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repository pour gérer les houes en base de données
 */
public class HoeRepository {

    private final JavaPlugin plugin;
    private final DatabaseManager databaseManager;

    public HoeRepository(JavaPlugin plugin, DatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
    }

    /**
     * Crée une nouvelle houe
     */
    public int create(HoeModel hoe) throws SQLException {
        String sql = "INSERT INTO hoes (uuid, player_name, dna, level, xp, prestige, fortune, sell_multiplier, total_harvested, total_earned, nbt_data) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, hoe.getUuid());
            pstmt.setString(2, hoe.getPlayerName());
            pstmt.setString(3, hoe.getDna());
            pstmt.setInt(4, hoe.getLevel());
            pstmt.setLong(5, hoe.getXp());
            pstmt.setInt(6, hoe.getPrestige());
            pstmt.setDouble(7, hoe.getFortune());
            pstmt.setDouble(8, hoe.getSellMultiplier());
            pstmt.setLong(9, hoe.getTotalHarvested());
            pstmt.setLong(10, hoe.getTotalEarned());
            pstmt.setString(11, hoe.getNbtData());
            
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
     * Récupère une houe par son ID
     */
    public Optional<HoeModel> findById(int id) throws SQLException {
        String sql = "SELECT * FROM hoes WHERE id = ?";
        
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToHoe(rs));
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Récupère une houe par l'UUID du joueur
     */
    public Optional<HoeModel> findByUuid(String uuid) throws SQLException {
        String sql = "SELECT * FROM hoes WHERE uuid = ?";
        
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, uuid);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToHoe(rs));
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Récupère une houe par son ADN
     */
    public Optional<HoeModel> findByDna(String dna) throws SQLException {
        String sql = "SELECT * FROM hoes WHERE dna = ?";
        
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, dna);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToHoe(rs));
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Récupère toutes les houes triées par niveau
     */
    public List<HoeModel> findAllOrderedByLevel() throws SQLException {
        String sql = "SELECT * FROM hoes ORDER BY prestige DESC, level DESC LIMIT 100";
        List<HoeModel> hoes = new ArrayList<>();
        
        try (Connection conn = databaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                hoes.add(mapResultSetToHoe(rs));
            }
        }
        return hoes;
    }

    /**
     * Met à jour une houe
     */
    public boolean update(HoeModel hoe) throws SQLException {
        String sql = "UPDATE hoes SET player_name = ?, dna = ?, level = ?, xp = ?, prestige = ?, " +
                     "fortune = ?, sell_multiplier = ?, total_harvested = ?, total_earned = ?, " +
                     "nbt_data = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, hoe.getPlayerName());
            pstmt.setString(2, hoe.getDna());
            pstmt.setInt(3, hoe.getLevel());
            pstmt.setLong(4, hoe.getXp());
            pstmt.setInt(5, hoe.getPrestige());
            pstmt.setDouble(6, hoe.getFortune());
            pstmt.setDouble(7, hoe.getSellMultiplier());
            pstmt.setLong(8, hoe.getTotalHarvested());
            pstmt.setLong(9, hoe.getTotalEarned());
            pstmt.setString(10, hoe.getNbtData());
            pstmt.setInt(11, hoe.getId());
            
            return pstmt.executeUpdate() > 0;
        }
    }

    /**
     * Supprime une houe
     */
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM hoes WHERE id = ?";
        
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        }
    }

    /**
     * Mappe un ResultSet vers une HoeModel
     */
    private HoeModel mapResultSetToHoe(ResultSet rs) throws SQLException {
        return HoeModel.builder()
                .id(rs.getInt("id"))
                .uuid(rs.getString("uuid"))
                .playerName(rs.getString("player_name"))
                .dna(rs.getString("dna"))
                .level(rs.getInt("level"))
                .xp(rs.getLong("xp"))
                .prestige(rs.getInt("prestige"))
                .fortune(rs.getDouble("fortune"))
                .sellMultiplier(rs.getDouble("sell_multiplier"))
                .totalHarvested(rs.getLong("total_harvested"))
                .totalEarned(rs.getLong("total_earned"))
                .createdAt(rs.getTimestamp("created_at"))
                .updatedAt(rs.getTimestamp("updated_at"))
                .nbtData(rs.getString("nbt_data"))
                .build();
    }
}
