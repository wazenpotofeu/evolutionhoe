package fr.wazenpotofeu.evolutionhoe.api;

import fr.wazenpotofeu.evolutionhoe.data.models.HoeModel;

/**
 * API publique pour les calculs de fortune et multiplicateurs
 */
public interface IFortuneAPI {
    
    /**
     * Calcule la fortune totale d'une houe
     */
    double calculateFortune(HoeModel hoe);
    
    /**
     * Calcule le multiplicateur de vente d'une houe
     */
    double calculateSellMultiplier(HoeModel hoe);
    
    /**
     * Calcule le montant final de vente
     */
    long calculateFinalSaleAmount(HoeModel hoe, long baseAmount);
    
    /**
     * Vérifie si un Jackpot est remporté
     */
    boolean isJackpot();
    
    /**
     * Obtient le multiplicateur de Jackpot
     */
    double getJackpotMultiplier();
    
    /**
     * Ajoute de la fortune à une houe
     */
    void addFortune(HoeModel hoe, double amount);
    
    /**
     * Ajoute un multiplicateur de vente
     */
    void addSellMultiplier(HoeModel hoe, double amount);
}
