package fr.wazenpotofeu.evolutionhoe.data.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * Modèle représentant un titre d'une houe
 * Les titres sont des récompenses visuelles
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TitleModel {
    
    private int id;
    private int hoeId;  // Référence à la houe
    private String titleName;  // Nom du titre
    private String color;  // Couleur du titre
    private Timestamp createdAt;
    
    /**
     * Titres spéciaux d'éveil
     */
    public static class AwakeningTitles {
        public static final String DIVINE_HARVESTER = "La Moissonneuse Divine";
        public static final String CROP_DESTROYER = "Le Fléau des Champs";
        public static final String GOLDEN_SICKLE = "La Faucille Dorée";
        public static final String ETERNAL_GARDENER = "Le Jardinier Éternel";
        public static final String SHADOW_HARVESTER = "La Moissonneuse Noire";
    }
}
