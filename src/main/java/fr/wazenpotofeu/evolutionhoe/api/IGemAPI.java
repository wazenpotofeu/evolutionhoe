package fr.wazenpotofeu.evolutionhoe.api;

import fr.wazenpotofeu.evolutionhoe.data.models.HoeModel;
import fr.wazenpotofeu.evolutionhoe.data.models.GemModel;
import java.util.List;
import java.util.Optional;

/**
 * API publique pour gérer les gemmes
 */
public interface IGemAPI {
    
    /**
     * Insère une gemme dans une houe
     */
    boolean insertGem(HoeModel hoe, String gemType, int slot);
    
    /**
     * Retire une gemme d'une houe
     */
    boolean removeGem(HoeModel hoe, int slot);
    
    /**
     * Obtient toutes les gemmes d'une houe
     */
    List<GemModel> getGemsForHoe(HoeModel hoe);
    
    /**
     * Calcule les bonus totaux des gemmes
     */
    GemModel.GemBonus calculateTotalGemBonus(HoeModel hoe);
    
    /**
     * Vérifie si un slot est valide
     */
    boolean isValidSlot(int slot);
    
    /**
     * Vérifie si un type de gemme est valide
     */
    boolean isValidGemType(String gemType);
    
    /**
     * Obtient le nombre de gemmes d'une houe
     */
    int getGemCount(HoeModel hoe);
}
