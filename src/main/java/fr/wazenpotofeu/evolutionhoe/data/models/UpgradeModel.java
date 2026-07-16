package fr.wazenpotofeu.evolutionhoe.data.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * Modèle représentant un upgrade appliqué à une houe
 * Les upgrades améliorent les capacités de la houe
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpgradeModel {
    
    private int id;
    private int hoeId;  // Référence à la houe
    private String upgradeName;  // Nom de l'upgrade
    private int level;  // Niveau de l'upgrade (certains peuvent être améliorés)
    private Timestamp createdAt;
    
    /**
     * Vérifie si cet upgrade peut être amélioré
     */
    public boolean canUpgrade(int maxLevel) {
        return level < maxLevel;
    }
    
    /**
     * Améliore l'upgrade
     */
    public void upgrade() {
        this.level++;
    }
}
