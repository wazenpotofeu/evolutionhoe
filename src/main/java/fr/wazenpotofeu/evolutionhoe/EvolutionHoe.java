package fr.wazenpotofeu.evolutionhoe;

import fr.wazenpotofeu.evolutionhoe.config.ConfigManager;
import fr.wazenpotofeu.evolutionhoe.database.DatabaseManager;
import fr.wazenpotofeu.evolutionhoe.managers.*;
import fr.wazenpotofeu.evolutionhoe.storage.*;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * EvolutionHoe - Plugin Spigot Premium de Farming Avancé
 * 
 * Gestionnaire principal du plugin
 * Initialise tous les composants et gère le cycle de vie
 */
public class EvolutionHoe extends JavaPlugin {

    // Managers
    private ConfigManager configManager;
    private DatabaseManager databaseManager;
    private HoeManager hoeManager;
    private FortuneManager fortuneManager;
    private MutationManager mutationManager;
    private GemManager gemManager;
    
    // Repositories
    private HoeRepository hoeRepository;
    private MutationRepository mutationRepository;
    private GemRepository gemRepository;
    private UpgradeRepository upgradeRepository;
    private TitleRepository titleRepository;
    private SalesHistoryRepository salesHistoryRepository;

    @Override
    public void onEnable() {
        printBanner();
        
        try {
            // 1. Initialiser la configuration
            getLogger().info("⚙️  Initialisation de la configuration...");
            initializeConfig();
            
            // 2. Initialiser la base de données
            getLogger().info("💾 Initialisation de la base de données...");
            initializeDatabase();
            
            // 3. Initialiser les repositories
            getLogger().info("📦 Initialisation des repositories...");
            initializeRepositories();
            
            // 4. Initialiser les managers
            getLogger().info("⚡ Initialisation des managers...");
            initializeManagers();
            
            // 5. Enregistrer les listeners (à implémenter)
            getLogger().info("👂 Enregistrement des listeners...");
            registerListeners();
            
            // 6. Enregistrer les commandes (à implémenter)
            getLogger().info("📝 Enregistrement des commandes...");
            registerCommands();
            
            // 7. Enregistrer les crochets externes (à implémenter)
            getLogger().info("🔗 Initialisation des crochets externes...");
            registerHooks();
            
            printSuccessMessage();
            
        } catch (Exception e) {
            getLogger().severe("❌ Erreur critique lors du démarrage du plugin!");
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("⏹️  Arrêt du plugin EvolutionHoe...");
        
        // Sauvegarder et nettoyer
        if (databaseManager != null) {
            databaseManager.shutdown();
        }
        
        getLogger().info("✓ Plugin désactivé proprement");
    }

    /**
     * Initialise la configuration
     */
    private void initializeConfig() {
        configManager = new ConfigManager(this);
        configManager.loadConfigs();
    }

    /**
     * Initialise la base de données
     */
    private void initializeDatabase() {
        databaseManager = new DatabaseManager(this, configManager);
        databaseManager.initialize();
    }

    /**
     * Initialise les repositories
     */
    private void initializeRepositories() {
        hoeRepository = new HoeRepository(this, databaseManager);
        mutationRepository = new MutationRepository(this, databaseManager);
        gemRepository = new GemRepository(this, databaseManager);
        upgradeRepository = new UpgradeRepository(this, databaseManager);
        titleRepository = new TitleRepository(this, databaseManager);
        salesHistoryRepository = new SalesHistoryRepository(this, databaseManager);
    }

    /**
     * Initialise les managers
     */
    private void initializeManagers() {
        hoeManager = new HoeManager(this, configManager, hoeRepository);
        fortuneManager = new FortuneManager(this, configManager, hoeRepository);
        mutationManager = new MutationManager(this, configManager, mutationRepository);
        gemManager = new GemManager(this, configManager, gemRepository);
    }

    /**
     * Enregistre les listeners d'événements
     */
    private void registerListeners() {
        // TODO: Enregistrer les listeners
        // getServer().getPluginManager().registerEvents(new PlayerEventListener(...), this);
    }

    /**
     * Enregistre les commandes
     */
    private void registerCommands() {
        // TODO: Enregistrer les commandes
        // getCommand("hoe").setExecutor(new HoeCommand(...));
    }

    /**
     * Enregistre les crochets externes (Nexo, PlaceholderAPI, etc)
     */
    private void registerHooks() {
        // TODO: Enregistrer les crochets externes
        // if (getServer().getPluginManager().getPlugin("Nexo") != null) { ... }
        // if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) { ... }
    }

    /**
     * Affiche la bannière de démarrage
     */
    private void printBanner() {
        getLogger().info("================================");
        getLogger().info("  🌾 EvolutionHoe v" + getDescription().getVersion());
        getLogger().info("  Plugin de farming premium");
        getLogger().info("================================");
    }

    /**
     * Affiche le message de succès
     */
    private void printSuccessMessage() {
        getLogger().info("================================");
        getLogger().info("✓ Plugin activé avec succès!");
        getLogger().info("✓ Tous les systèmes sont opérationnels");
        getLogger().info("================================");
    }

    // Getters pour accéder aux managers et repositories
    public ConfigManager getConfigManager() { return configManager; }
    public DatabaseManager getDatabaseManager() { return databaseManager; }
    public HoeManager getHoeManager() { return hoeManager; }
    public FortuneManager getFortuneManager() { return fortuneManager; }
    public MutationManager getMutationManager() { return mutationManager; }
    public GemManager getGemManager() { return gemManager; }
    
    public HoeRepository getHoeRepository() { return hoeRepository; }
    public MutationRepository getMutationRepository() { return mutationRepository; }
    public GemRepository getGemRepository() { return gemRepository; }
    public UpgradeRepository getUpgradeRepository() { return upgradeRepository; }
    public TitleRepository getTitleRepository() { return titleRepository; }
    public SalesHistoryRepository getSalesHistoryRepository() { return salesHistoryRepository; }
}
