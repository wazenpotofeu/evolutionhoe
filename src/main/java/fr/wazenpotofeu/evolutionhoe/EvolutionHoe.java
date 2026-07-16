package fr.wazenpotofeu.evolutionhoe;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * EvolutionHoe - Plugin Spigot Premium de Farming Avancé
 */
public class EvolutionHoe extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("================================");
        getLogger().info("  EvolutionHoe v" + getDescription().getVersion());
        getLogger().info("  Plugin de farming premium");
        getLogger().info("================================");
        
        // TODO: Initialiser les configurations
        // TODO: Initialiser la base de données
        // TODO: Charger les managers
        // TODO: Enregistrer les listeners
        // TODO: Enregistrer les commandes
        // TODO: Charger les integrations (Nexo, PlaceholderAPI)
        
        getLogger().info("✓ Plugin activé avec succès");
    }

    @Override
    public void onDisable() {
        getLogger().info("✓ Plugin désactivé");
    }
}
