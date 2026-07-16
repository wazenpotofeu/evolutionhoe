package fr.wazenpotofeu.evolutionhoe.managers;

import fr.wazenpotofeu.evolutionhoe.config.ConfigManager;
import fr.wazenpotofeu.evolutionhoe.data.models.HoeModel;
import fr.wazenpotofeu.evolutionhoe.data.models.MutationModel;
import fr.wazenpotofeu.evolutionhoe.storage.MutationRepository;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Gestionnaire des mutations génétiques
 * Crée et gère les mutations aléatoires des houes
 */
public class MutationManager {

    private final JavaPlugin plugin;
    private final ConfigManager configManager;
    private final MutationRepository mutationRepository;
    
    private static final List<String> MUTATION_TYPES = List.of(
            "Radiance",
            "Vitality",
            "Wealth",
            "Harmony",
            "Shadow"
    );

    public MutationManager(JavaPlugin plugin, ConfigManager configManager, MutationRepository mutationRepository) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.mutationRepository = mutationRepository;
    }

    /**
     * Vérifie si une houe peut muter
     */
    public boolean canMutate(HoeModel hoe) {
        if (!configManager.getMainConfig().getBoolean("dna.mutations.enabled", true)) {
            return false;
        }
        
        double chance = configManager.getMainConfig().getDouble("dna.mutations.chance", 0.1);
        return Math.random() * 100 < (chance * hoe.getLevel());
    }

    /**
     * Génère une mutation aléatoire
     */
    public MutationModel generateMutation(HoeModel hoe) {
        String mutationType = MUTATION_TYPES.get((int) (Math.random() * MUTATION_TYPES.size()));
        
        MutationModel mutation = MutationModel.builder()
                .hoeId(hoe.getId())
                .mutationType(mutationType)
                .build();
        
        applyMutationBonuses(mutation);
        return mutation;
    }

    /**
     * Applique les bonus d'une mutation
     */
    private void applyMutationBonuses(MutationModel mutation) {
        switch (mutation.getMutationType().toLowerCase()) {
            case "radiance" -> {
                mutation.setFortuneBonus(15.0);
                mutation.setXpBonus(10.0);
            }
            case "vitality" -> {
                mutation.setRepairBonus(50.0);
                mutation.setXpBonus(5.0);
            }
            case "wealth" -> {
                mutation.setSellMultiplierBonus(25.0);
                mutation.setTokenBonus(15.0);
            }
            case "harmony" -> {
                mutation.setFortuneBonus(10.0);
                mutation.setXpBonus(10.0);
                mutation.setSellMultiplierBonus(10.0);
            }
            case "shadow" -> {
                mutation.setTokenBonus(30.0);
                mutation.setXpBonus(5.0);
            }
        }
    }

    /**
     * Applique une mutation à une houe
     */
    public void applyMutation(HoeModel hoe, MutationModel mutation) {
        try {
            mutationRepository.create(mutation);
            hoe.setCurrentMutation(mutation.getMutationType());
            plugin.getLogger().info("✨ Houe mutée: " + mutation.getMutationType());
        } catch (SQLException e) {
            plugin.getLogger().warning("Erreur lors de l'application d'une mutation");
            e.printStackTrace();
        }
    }

    /**
     * Obtient la dernière mutation d'une houe
     */
    public Optional<MutationModel> getLatestMutation(HoeModel hoe) {
        try {
            return mutationRepository.findLatestByHoeId(hoe.getId());
        } catch (SQLException e) {
            plugin.getLogger().warning("Erreur lors de la récupération d'une mutation");
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * Compte les mutations d'une houe
     */
    public int getMutationCount(HoeModel hoe) {
        try {
            return mutationRepository.countByHoeId(hoe.getId());
        } catch (SQLException e) {
            plugin.getLogger().warning("Erreur lors du comptage des mutations");
            e.printStackTrace();
            return 0;
        }
    }
}
