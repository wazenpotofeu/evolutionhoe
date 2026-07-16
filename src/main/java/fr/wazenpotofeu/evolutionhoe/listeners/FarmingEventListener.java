package fr.wazenpotofeu.evolutionhoe.listeners;

import fr.wazenpotofeu.evolutionhoe.EvolutionHoe;
import fr.wazenpotofeu.evolutionhoe.data.models.HoeModel;
import fr.wazenpotofeu.evolutionhoe.managers.HoeManager;
import fr.wazenpotofeu.evolutionhoe.managers.MutationManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Listener pour les événements de farming
 * Gère les récoltes avec les houes EvolutionHoe
 */
public class FarmingEventListener implements Listener {

    private final EvolutionHoe plugin;
    private final HoeManager hoeManager;
    private final MutationManager mutationManager;

    public FarmingEventListener(EvolutionHoe plugin) {
        this.plugin = plugin;
        this.hoeManager = plugin.getHoeManager();
        this.mutationManager = plugin.getMutationManager();
    }

    /**
     * Événement de casse de bloc
     */
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        
        // Vérifier si le joueur tient une houe EvolutionHoe
        if (!isEvolutionHoe(itemInHand)) {
            return;
        }
        
        // Charger la houe du joueur
        HoeModel hoe = hoeManager.getOrCreateHoe(player.getUniqueId(), player.getName());
        if (hoe == null) {
            return;
        }
        
        // Ajouter les statistiques
        hoe.setTotalHarvested(hoe.getTotalHarvested() + 1);
        
        // Ajouter de l'XP
        long xpGain = calculateXpGain(hoe);
        hoeManager.addXp(hoe, xpGain);
        
        // Vérifier les mutations
        if (mutationManager.canMutate(hoe)) {
            mutationManager.applyMutation(hoe, mutationManager.generateMutation(hoe));
            player.sendMessage("§6[EvolutionHoe] ✨ Votre houe a muté!");
        }
        
        // Sauvegarder
        hoeManager.saveHoe(hoe);
    }

    /**
     * Vérifie si un item est une houe EvolutionHoe
     */
    private boolean isEvolutionHoe(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }
        
        // Vérifier si le displayName contient "EvolutionHoe"
        String displayName = item.getItemMeta().getDisplayName();
        return displayName != null && displayName.contains("EvolutionHoe");
    }

    /**
     * Calcule l'XP gagné lors d'une récolte
     */
    private long calculateXpGain(HoeModel hoe) {
        long baseXp = plugin.getConfigManager().getMainConfig().getLong("farming.xp.base", 10L);
        double multiplier = 1.0 + (hoe.getLevel() * 0.01);  // +1% par niveau
        return (long) (baseXp * multiplier);
    }
}
