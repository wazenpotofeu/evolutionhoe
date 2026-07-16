package fr.wazenpotofeu.evolutionhoe.data.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * Modèle représentant un trait d'une houe
 * Chaque houe naît avec un trait aléatoire
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TraitModel {
    
    private int id;
    private int hoeId;  // Référence à la houe
    private String traitType;  // Type de trait (Lucky, Wealth, Efficiency, etc)
    private double bonus;  // Valeur du bonus
    private Timestamp createdAt;
    
    /**
     * Obtient la description du trait
     */
    public String getDescription() {
        return switch(traitType.toLowerCase()) {
            case "lucky" -> "🍀 Chanceuse - +5% Clés";
            case "wealth" -> "💎 Cupidité - +8% Vente";
            case "efficiency" -> "⚡ Efficacité - +10% Vitesse";
            case "fortune" -> "💰 Récolte - +6% Fortune";
            case "resilience" -> "🛡️ Résilience - +12% Durabilité";
            case "sage" -> "🧠 Sage - +4% XP";
            default -> "Trait inconnu";
        };
    }
}
