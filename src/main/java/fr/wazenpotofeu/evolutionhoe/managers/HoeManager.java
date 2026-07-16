package fr.wazenpotofeu.evolutionhoe.managers;

import fr.wazenpotofeu.evolutionhoe.config.ConfigManager;
import fr.wazenpotofeu.evolutionhoe.data.models.HoeModel;
import fr.wazenpotofeu.evolutionhoe.storage.HoeRepository;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Gestionnaire des houes et de la progression XP
 * Gère les niveaux, l'XP, le prestige, et les statistiques
 */
public class HoeManager {

    private final JavaPlugin plugin;
    private final ConfigManager configManager;
    private final HoeRepository hoeRepository;
    private final Map<UUID, HoeModel> cachedHoes = new HashMap<>();

    public HoeManager(JavaPlugin plugin, ConfigManager configManager, HoeRepository hoeRepository) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.hoeRepository = hoeRepository;
    }

    /**
     * Crée une nouvelle houe pour un joueur
     */
    public HoeModel createHoe(UUID playerUuid, String playerName, String dna) {
        HoeModel hoe = HoeModel.builder()
                .uuid(playerUuid.toString())
                .playerName(playerName)
                .dna(dna)
                .level(1)
                .xp(0)
                .prestige(0)
                .fortune(0.0)
                .sellMultiplier(1.0)
                .totalHarvested(0)
                .totalEarned(0)
                .build();
        
        try {
            int id = hoeRepository.create(hoe);
            hoe.setId(id);
            cachedHoes.put(playerUuid, hoe);
            return hoe;
        } catch (SQLException e) {
            plugin.getLogger().warning("Erreur lors de la création d'une houe pour " + playerName);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Charge une houe pour un joueur
     */
    public HoeModel getOrCreateHoe(UUID playerUuid, String playerName) {
        // Vérifier le cache
        if (cachedHoes.containsKey(playerUuid)) {
            return cachedHoes.get(playerUuid);
        }
        
        try {
            Optional<HoeModel> hoe = hoeRepository.findByUuid(playerUuid.toString());
            if (hoe.isPresent()) {
                cachedHoes.put(playerUuid, hoe.get());
                return hoe.get();
            }
        } catch (SQLException e) {
            plugin.getLogger().warning("Erreur lors du chargement de la houe de " + playerName);
            e.printStackTrace();
        }
        
        // Créer une nouvelle houe
        String dna = generateDNA();
        return createHoe(playerUuid, playerName, dna);
    }

    /**
     * Ajoute de l'XP à une houe
     */
    public void addXp(HoeModel hoe, long amount) {
        hoe.addXp(amount);
        
        long maxXpForLevel = calculateMaxXpForLevel(hoe.getLevel());
        while (hoe.getXp() >= maxXpForLevel) {
            hoe.addXp(-maxXpForLevel);
            hoe.addLevel(1);
            maxXpForLevel = calculateMaxXpForLevel(hoe.getLevel());
        }
        
        saveHoe(hoe);
    }

    /**
     * Vérifie et applique le prestige si possible
     */
    public boolean tryPrestige(HoeModel hoe) {
        if (hoe.canPrestige(calculateMaxXpForLevel(hoe.getLevel()))) {
            hoe.addPrestige(1);
            saveHoe(hoe);
            return true;
        }
        return false;
    }

    /**
     * Calcule l'XP maximal pour un niveau
     */
    public long calculateMaxXpForLevel(int level) {
        return level * 100L;  // Formule par défaut: level * 100
    }

    /**
     * Génère un ADN unique
     */
    public String generateDNA() {
        String chars = "0123456789ABCDEF";
        StringBuilder dna = new StringBuilder();
        
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                dna.append(chars.charAt((int) (Math.random() * chars.length())));
            }
            if (i < 2) dna.append("-");
        }
        
        return dna.toString();
    }

    /**
     * Sauvegarde une houe
     */
    public void saveHoe(HoeModel hoe) {
        try {
            hoeRepository.update(hoe);
        } catch (SQLException e) {
            plugin.getLogger().warning("Erreur lors de la sauvegarde de la houe");
            e.printStackTrace();
        }
    }

    /**
     * Charge une houe depuis le cache ou la base de données
     */
    public Optional<HoeModel> getHoe(UUID playerUuid) {
        if (cachedHoes.containsKey(playerUuid)) {
            return Optional.of(cachedHoes.get(playerUuid));
        }
        
        try {
            return hoeRepository.findByUuid(playerUuid.toString());
        } catch (SQLException e) {
            plugin.getLogger().warning("Erreur lors du chargement d'une houe");
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * Invalide le cache d'une houe
     */
    public void invalidateCache(UUID playerUuid) {
        cachedHoes.remove(playerUuid);
    }
}
