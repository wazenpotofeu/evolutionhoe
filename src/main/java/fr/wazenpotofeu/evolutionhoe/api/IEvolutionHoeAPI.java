package fr.wazenpotofeu.evolutionhoe.api;

/**
 * API principal pour accéder à tous les services d'EvolutionHoe
 * Point d'entrée unique pour les plugins externes
 */
public interface IEvolutionHoeAPI {
    
    /**
     * Obtient l'API des houes
     */
    IHoeAPI getHoeAPI();
    
    /**
     * Obtient l'API de la fortune
     */
    IFortuneAPI getFortuneAPI();
    
    /**
     * Obtient l'API des mutations
     */
    IMutationAPI getMutationAPI();
    
    /**
     * Obtient l'API des gemmes
     */
    IGemAPI getGemAPI();
}
