package fr.wazenpotofeu.evolutionhoe.api;

import fr.wazenpotofeu.evolutionhoe.data.models.HoeModel;
import java.util.Optional;
import java.util.UUID;

/**
 * API publique pour accéder aux données des houes
 * Interface pour les plugins externes et modules internes
 */
public interface IHoeAPI {
    
    /**
     * Obtient ou crée une houe pour un joueur
     */
    HoeModel getOrCreateHoe(UUID playerUuid, String playerName);
    
    /**
     * Obtient une houe pour un joueur
     */
    Optional<HoeModel> getHoe(UUID playerUuid);
    
    /**
     * Obtient une houe par son ADN
     */
    Optional<HoeModel> getHoeByDna(String dna);
    
    /**
     * Sauvegarde une houe
     */
    void saveHoe(HoeModel hoe);
    
    /**
     * Ajoute de l'XP à une houe
     */
    void addXp(HoeModel hoe, long amount);
    
    /**
     * Génère un nouvel ADN unique
     */
    String generateDNA();
}
