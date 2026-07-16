package fr.wazenpotofeu.evolutionhoe.data.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * Modèle représentant une gemme insérée dans une houe
 * Les gemmes fournissent des bonus statistiques
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GemModel {
    
    private int id;
    private int hoeId;  // Référence à la houe
    private String gemType;  // Type de gemme (Ruby, Sapphire, Emerald, Obsidian)
    private int slot;  // Emplacement (1-4)
    private Timestamp createdAt;
    
    /**
     * Obtient les bonus de la gemme
     */
    public GemBonus getBonus() {
        return switch(gemType.toLowerCase()) {
            case "ruby" -> GemBonus.builder()
                    .fortune(5.0)
                    .xp(2.0)
                    .build();
            case "sapphire" -> GemBonus.builder()
                    .tokens(3.0)
                    .keys(2.0)
                    .build();
            case "emerald" -> GemBonus.builder()
                    .sellMultiplier(10.0)
                    .build();
            case "obsidian" -> GemBonus.builder()
                    .specialBonus(5.0)
                    .build();
            default -> GemBonus.builder().build();
        };
    }
    
    /**
     * Classe interne pour les bonus
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GemBonus {
        private double fortune = 0.0;
        private double xp = 0.0;
        private double tokens = 0.0;
        private double keys = 0.0;
        private double sellMultiplier = 0.0;
        private double specialBonus = 0.0;
    }
}
