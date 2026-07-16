package fr.wazenpotofeu.evolutionhoe.data.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * Modèle représentant un enregistrement de vente
 * Historique des ventes pour les statistiques
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesHistoryModel {
    
    private int id;
    private int hoeId;  // Référence à la houe
    private long amount;  // Montant de la vente
    private double multiplier;  // Multiplicateur appliqué
    private Timestamp createdAt;
    
    /**
     * Obtient le montant final après multiplicateur
     */
    public long getFinalAmount() {
        return (long) (amount * multiplier);
    }
}
