package fr.wazenpotofeu.evolutionhoe.data.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * Modèle représentant une mutation de houe
 * Les mutations changent les statistiques et les effets visuels
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MutationModel {
    
    private int id;
    private int hoeId;  // Référence à la houe
    private String mutationType;  // Type de mutation (Radiance, Vitality, Wealth, etc)
    private Timestamp createdAt;
    
    // Bonus de cette mutation
    private double fortuneBonus;
    private double xpBonus;
    private double sellMultiplierBonus;
    private double repairBonus;
    private double tokenBonus;
    
    /**
     * Obtient la description de la mutation
     */
    public String getDescription() {
        return switch(mutationType.toLowerCase()) {
            case "radiance" -> "✨ Rayonnement - +15% Fortune, +10% XP";
            case "vitality" -> "💚 Vitalité - +50% Réparation, +25% Durabilité";
            case "wealth" -> "💰 Richesse - +25% Vente, +15% Tokens";
            case "harmony" -> "🎵 Harmonie - +20% Tous les bonus";
            case "shadow" -> "🌑 Ombre - +30% Clés rares, +15% Essence";
            default -> "Mutation inconnue";
        };
    }
}
