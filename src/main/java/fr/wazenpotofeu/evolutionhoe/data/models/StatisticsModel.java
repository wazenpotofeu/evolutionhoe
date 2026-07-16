package fr.wazenpotofeu.evolutionhoe.data.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * Modèle représentant les statistiques d'une houe
 * Suivre les performances du joueur
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatisticsModel {
    
    private int id;
    private int hoeId;  // Référence à la houe
    private String statName;  // Nom de la statistique
    private double statValue;  // Valeur de la statistique
    private Timestamp updatedAt;
    
    /**
     * Statistiques spéciales
     */
    public static class StatTypes {
        public static final String BLOCKS_HARVESTED = "blocks_harvested";
        public static final String TOTAL_XP_GAINED = "total_xp_gained";
        public static final String LUCKY_HARVESTS = "lucky_harvests";
        public static final String CRITICAL_HITS = "critical_hits";
        public static final String JACKPOTS = "jackpots";
        public static final String MUTATIONS_GAINED = "mutations_gained";
        public static final String PRESTIGE_COUNT = "prestige_count";
        public static final String GEMS_INSERTED = "gems_inserted";
        public static final String FUSIONS_MADE = "fusions_made";
    }
}
