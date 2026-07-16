package fr.wazenpotofeu.evolutionhoe.storage;

import fr.wazenpotofeu.evolutionhoe.data.models.SalesHistoryModel;
import fr.wazenpotofeu.evolutionhoe.database.DatabaseManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository pour gérer l'historique des ventes en base de données
 */
public class SalesHistoryRepository {

    private final JavaPlugin plugin;
    private final DatabaseManager databaseManager;

    public SalesHistoryRepository(JavaPlugin plugin, DatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
    }

    /**
     * Crée un nouvel enregistrement de vente
     */
    public int create(SalesHistoryModel sale) throws SQLException {
        String sql = "INSERT INTO sales_history (hoe_id, amount, multiplier) VALUES (?, ?, ?)";
        
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, sale.getHoeId());
            pstmt.setLong(2, sale.getAmount());
            pstmt.setDouble(3, sale.getMultiplier());
            
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
     * Récupère l'historique de vente d'une houe
     */
    public List<SalesHistoryModel> findByHoeId(int hoeId, int limit) throws SQLException {
        String sql = "SELECT * FROM sales_history WHERE hoe_id = ? ORDER BY created_at DESC LIMIT ?";
        List<SalesHistoryModel> history = new ArrayList<>();
        
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, hoeId);
            pstmt.setInt(2, limit);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    history.add(mapResultSetToSalesHistory(rs));
                }
            }
        }
        return history;
    }

    /**
     * Obtient le montant total vendu par une houe
     */
    public long getTotalSalesByHoeId(int hoeId) throws SQLException {
        String sql = "SELECT COALESCE(SUM(amount * multiplier), 0) as total FROM sales_history WHERE hoe_id = ?";
        
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, hoeId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong("total");
                }
            }
        }
        return 0L;
    }

    /**
     * Compte le nombre de ventes d'une houe
     */
    public int countByHoeId(int hoeId) throws SQLException {
        String sql = "SELECT COUNT(*) as count FROM sales_history WHERE hoe_id = ?";
        
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
     * Mappe un ResultSet vers une SalesHistoryModel
     */
    private SalesHistoryModel mapResultSetToSalesHistory(ResultSet rs) throws SQLException {
        return SalesHistoryModel.builder()
                .id(rs.getInt("id"))
                .hoeId(rs.getInt("hoe_id"))
                .amount(rs.getLong("amount"))
                .multiplier(rs.getDouble("multiplier"))
                .createdAt(rs.getTimestamp("created_at"))
                .build();
    }
}
