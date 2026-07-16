package fr.wazenpotofeu.evolutionhoe.data.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * Modèle représentant une houe EvolutionHoe
 * Entité principale du plugin
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HoeModel {
    
    private int id;
    private String uuid;  // UUID du joueur
    private String playerName;
    private String dna;  // ADN unique (XXXX-XXXX-XXXX)
    private int level;
    private long xp;
    private int prestige;
    private double fortune;
    private double sellMultiplier;
    private long totalHarvested;
    private long totalEarned;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String nbtData;  // Données NBT sérialisées
    
    // Données temporaires (non sauvegardées)
    private transient String currentMutation;
    private transient String currentTrait;
    private transient String title;
    
    /**
     * Vérifie si la houe peut se prestige
     */
    public boolean canPrestige(long maxXpForLevel) {
        return level >= 100 && xp >= maxXpForLevel;
    }
    
    /**
     * Vérifie si la houe peut s'éveiller
     */
    public boolean canAwaken() {
        return prestige == 0 && level >= 100;
    }
    
    /**
     * Ajoute de l'XP à la houe
     */
    public void addXp(long amount) {
        this.xp += amount;
    }
    
    /**
     * Ajoute des niveaux
     */
    public void addLevel(int amount) {
        this.level += amount;
    }
    
    /**
     * Ajoute du prestige
     */
    public void addPrestige(int amount) {
        this.prestige += amount;
        this.level = 1;  // Reset niveau après prestige
        this.xp = 0;
    }
}
