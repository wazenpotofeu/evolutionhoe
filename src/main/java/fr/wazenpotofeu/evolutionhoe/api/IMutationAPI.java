package fr.wazenpotofeu.evolutionhoe.api;

import fr.wazenpotofeu.evolutionhoe.data.models.HoeModel;
import fr.wazenpotofeu.evolutionhoe.data.models.MutationModel;
import java.util.Optional;

/**
 * API publique pour gérer les mutations génétiques
 */
public interface IMutationAPI {
    
    /**
     * Vérifie si une houe peut muter
     */
    boolean canMutate(HoeModel hoe);
    
    /**
     * Génère une mutation aléatoire
     */
    MutationModel generateMutation(HoeModel hoe);
    
    /**
     * Applique une mutation à une houe
     */
    void applyMutation(HoeModel hoe, MutationModel mutation);
    
    /**
     * Obtient la dernière mutation d'une houe
     */
    Optional<MutationModel> getLatestMutation(HoeModel hoe);
    
    /**
     * Compte les mutations d'une houe
     */
    int getMutationCount(HoeModel hoe);
}
