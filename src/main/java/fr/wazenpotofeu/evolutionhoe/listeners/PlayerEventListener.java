package fr.wazenpotofeu.evolutionhoe.listeners;

import fr.wazenpotofeu.evolutionhoe.EvolutionHoe;
import fr.wazenpotofeu.evolutionhoe.data.models.HoeModel;
import fr.wazenpotofeu.evolutionhoe.managers.HoeManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Listener pour les événements des joueurs
 * Gère la connexion/déconnexion et le chargement des houes
 */
public class PlayerEventListener implements Listener {

    private final EvolutionHoe plugin;
    private final HoeManager hoeManager;

    public PlayerEventListener(EvolutionHoe plugin) {
        this.plugin = plugin;
        this.hoeManager = plugin.getHoeManager();
    }

    /**
     * Événement de connexion d'un joueur
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        
        // Charger la houe du joueur de manière asynchrone
        plugin.getServer().getScheduler().runTaskAsyncDelayed(plugin, () -> {
            HoeModel hoe = hoeManager.getOrCreateHoe(player.getUniqueId(), player.getName());
            
            if (hoe != null) {
                plugin.getLogger().info("✓ Houe chargée pour " + player.getName() + " (ADN: " + hoe.getDna() + ")");
            } else {
                plugin.getLogger().warning("✗ Erreur lors du chargement de la houe pour " + player.getName());
            }
        }, 5L);
    }

    /**
     * Événement de déconnexion d'un joueur
     */
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        
        // Invalider le cache pour libérer la mémoire
        hoeManager.invalidateCache(player.getUniqueId());
        plugin.getLogger().info("✓ Houe sauvegardée et cache vidé pour " + player.getName());
    }
}
