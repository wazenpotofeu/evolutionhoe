package fr.wazenpotofeu.evolutionhoe.managers;

import fr.wazenpotofeu.evolutionhoe.config.ConfigManager;
import fr.wazenpotofeu.evolutionhoe.data.models.HoeModel;
import fr.wazenpotofeu.evolutionhoe.data.models.GemModel;
import fr.wazenpotofeu.evolutionhoe.storage.GemRepository;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Gestionnaire des gemmes
 * Gère l'insertion et les bonus des gemmes dans les houes
 */
public class GemManager {

    private final JavaPlugin plugin;
    private final ConfigManager configManager;
    private final GemRepository gemRepository;
    
    private static final int MAX_GEMS = 4;
    private static final List<String> GEM_TYPES = List.of("Ruby", "Sapphire", "Emerald", "Obsidian");

    public GemManager(JavaPlugin plugin, ConfigManager configManager, GemRepository gemRepository) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.gemRepository = gemRepository;
    }

    /**
     * Insére une gemme dans une houe
     */
    public boolean insertGem(HoeModel hoe, String gemType, int slot) {
        if (!isValidSlot(slot) || !isValidGemType(gemType)) {
            return false;
        }
        
        try {
            // Vérifier que le slot est libre
            if (gemRepository.findByHoeIdAndSlot(hoe.getId(), slot).isPresent()) {
                return false;  // Slot occupé
            }
            
            GemModel gem = GemModel.builder()
                    .hoeId(hoe.getId())
                    .gemType(gemType)
                    .slot(slot)
                    .build();
            
            gemRepository.create(gem);
            plugin.getLogger().info("💎 Gemme " + gemType + " insérée au slot " + slot);
            return true;
        } catch (SQLException e) {
            plugin.getLogger().warning("Erreur lors de l'insertion d'une gemme");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retire une gemme d'une houe
     */
    public boolean removeGem(HoeModel hoe, int slot) {
        try {
            Optional<GemModel> gem = gemRepository.findByHoeIdAndSlot(hoe.getId(), slot);
            if (gem.isPresent()) {
                gemRepository.delete(gem.get().getId());
                return true;
            }
        } catch (SQLException e) {
            plugin.getLogger().warning("Erreur lors du retrait d'une gemme");
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Obtient toutes les gemmes d'une houe
     */
    public List<GemModel> getGemsForHoe(HoeModel hoe) {
        try {
            return gemRepository.findByHoeId(hoe.getId());
        } catch (SQLException e) {
            plugin.getLogger().warning("Erreur lors de la récupération des gemmes");
            e.printStackTrace();
            return List.of();
        }
    }

    /**
     * Calcule les bonus totaux des gemmes
     */
    public GemModel.GemBonus calculateTotalGemBonus(HoeModel hoe) {
        GemModel.GemBonus total = GemModel.GemBonus.builder().build();
        
        try {
            List<GemModel> gems = gemRepository.findByHoeId(hoe.getId());
            for (GemModel gem : gems) {
                GemModel.GemBonus bonus = gem.getBonus();
                total.setFortune(total.getFortune() + bonus.getFortune());
                total.setXp(total.getXp() + bonus.getXp());
                total.setTokens(total.getTokens() + bonus.getTokens());
                total.setKeys(total.getKeys() + bonus.getKeys());
                total.setSellMultiplier(total.getSellMultiplier() + bonus.getSellMultiplier());
                total.setSpecialBonus(total.getSpecialBonus() + bonus.getSpecialBonus());
            }
        } catch (SQLException e) {
            plugin.getLogger().warning("Erreur lors du calcul des bonus des gemmes");
            e.printStackTrace();
        }
        
        return total;
    }

    /**
     * Vérifie si un slot est valide
     */
    public boolean isValidSlot(int slot) {
        return slot >= 1 && slot <= MAX_GEMS;
    }

    /**
     * Vérifie si un type de gemme est valide
     */
    public boolean isValidGemType(String gemType) {
        return GEM_TYPES.contains(gemType);
    }

    /**
     * Obtient le nombre de gemmes d'une houe
     */
    public int getGemCount(HoeModel hoe) {
        try {
            return gemRepository.countByHoeId(hoe.getId());
        } catch (SQLException e) {
            plugin.getLogger().warning("Erreur lors du comptage des gemmes");
            e.printStackTrace();
            return 0;
        }
    }
}
