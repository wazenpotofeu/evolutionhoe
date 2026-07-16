package fr.wazenpotofeu.evolutionhoe.managers;

import fr.wazenpotofeu.evolutionhoe.config.ConfigManager;
import fr.wazenpotofeu.evolutionhoe.data.models.HoeModel;
import fr.wazenpotofeu.evolutionhoe.storage.HoeRepository;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

/**
 * Gestionnaire des fortunes et multiplicateurs
 * Calcule les bonus de fortune, multiplicateurs de vente, etc.
 */
public class FortuneManager {

    private final JavaPlugin plugin;
    private final ConfigManager configManager;
    private final HoeRepository hoeRepository;
    private final Map<String, Double> fortuneCache = new HashMap<>();

    public FortuneManager(JavaPlugin plugin, ConfigManager configManager, HoeRepository hoeRepository) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.hoeRepository = hoeRepository;
    }

    /**
     * Calcule la fortune totale d'une houe
     */
    public double calculateFortune(HoeModel hoe) {
        double baseFortune = 0.0;
        
        // Fortune de base par niveau
        baseFortune += hoe.getLevel() * configManager.getMainConfig().getDouble("farming.fortune.per-level", 0.5);
        
        // Fortune de la houe elle-même
        baseFortune += hoe.getFortune();
        
        // Cap
        double max = configManager.getMainConfig().getDouble("farming.fortune.max", 100.0);
        return Math.min(baseFortune, max);
    }

    /**
     * Calcule le multiplicateur de vente total
     */
    public double calculateSellMultiplier(HoeModel hoe) {
        double multiplier = 1.0;
        
        // Multiplicateur de base
        multiplier *= hoe.getSellMultiplier();
        
        // Multiplicateur par niveau
        multiplier += hoe.getLevel() * configManager.getMainConfig().getDouble("economy.multiplier.per-level", 0.02);
        
        // Multiplicateur par prestige
        multiplier += hoe.getPrestige() * configManager.getMainConfig().getDouble("prestige.per-prestige.base-multiplier", 0.1);
        
        // Cap
        double max = configManager.getMainConfig().getDouble("economy.multiplier.max", 10.0);
        return Math.min(multiplier, max);
    }

    /**
     * Calcule le montant final de vente avec tous les bonus
     */
    public long calculateFinalSaleAmount(HoeModel hoe, long baseAmount) {
        double multiplier = calculateSellMultiplier(hoe);
        return (long) (baseAmount * multiplier);
    }

    /**
     * Vérifie et applique un bonus Jackpot
     */
    public boolean isJackpot() {
        double chance = configManager.getMainConfig().getDouble("economy.bonus.jackpot-chance", 5.0);
        return Math.random() * 100 < chance;
    }

    /**
     * Obtient le multiplicateur de Jackpot
     */
    public double getJackpotMultiplier() {
        return configManager.getMainConfig().getDouble("economy.bonus.jackpot-multiplier", 2.5);
    }

    /**
     * Ajoute de la fortune à une houe
     */
    public void addFortune(HoeModel hoe, double amount) {
        hoe.setFortune(hoe.getFortune() + amount);
    }

    /**
     * Ajoute un multiplicateur de vente
     */
    public void addSellMultiplier(HoeModel hoe, double amount) {
        hoe.setSellMultiplier(hoe.getSellMultiplier() + amount);
    }
}
