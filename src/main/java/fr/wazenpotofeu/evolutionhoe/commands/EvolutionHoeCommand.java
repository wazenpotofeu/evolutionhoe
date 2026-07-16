package fr.wazenpotofeu.evolutionhoe.commands;

import fr.wazenpotofeu.evolutionhoe.EvolutionHoe;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Commande admin /evolutionhoe
 * Gère les commandes d'administration du plugin
 */
public class EvolutionHoeCommand implements CommandExecutor {

    private final EvolutionHoe plugin;

    public EvolutionHoeCommand(EvolutionHoe plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("evolutionhoe.admin")) {
            sender.sendMessage("§cVous n'avez pas la permission pour exécuter cette commande.");
            return true;
        }

        if (args.length == 0) {
            return showAdminHelp(sender);
        }

        String subCommand = args[0].toLowerCase();
        
        return switch (subCommand) {
            case "reload" -> reloadConfig(sender);
            case "status" -> showStatus(sender);
            case "version" -> showVersion(sender);
            case "help" -> showAdminHelp(sender);
            default -> {
                sender.sendMessage("§c/evolutionhoe help pour voir les commandes disponibles.");
                yield false;
            }
        };
    }

    /**
     * Recharge la configuration
     */
    private boolean reloadConfig(CommandSender sender) {
        sender.sendMessage("§6Rechargement de la configuration...");
        
        try {
            plugin.getConfigManager().loadConfigs();
            sender.sendMessage("§a✓ Configuration rechargée avec succès!");
            return true;
        } catch (Exception e) {
            sender.sendMessage("§c✗ Erreur lors du rechargement de la configuration.");
            plugin.getLogger().warning("Erreur lors du rechargement de la configuration");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Affiche le statut du plugin
     */
    private boolean showStatus(CommandSender sender) {
        sender.sendMessage("§6═══════════════════════════════════");
        sender.sendMessage("§6     Statut d'EvolutionHoe");
        sender.sendMessage("§6═══════════════════════════════════");
        sender.sendMessage("§f✓ Plugin activé");
        sender.sendMessage("§f✓ Base de données connectée");
        sender.sendMessage("§f✓ Managers initialisés");
        sender.sendMessage("§f✓ Listeners enregistrés");
        sender.sendMessage("§6═══════════════════════════════════");
        return true;
    }

    /**
     * Affiche la version du plugin
     */
    private boolean showVersion(CommandSender sender) {
        String version = plugin.getDescription().getVersion();
        String authors = String.join(", ", plugin.getDescription().getAuthors());
        
        sender.sendMessage("§6═══════════════════════════════════");
        sender.sendMessage("§6EvolutionHoe v" + version);
        sender.sendMessage("§6Auteurs: " + authors);
        sender.sendMessage("§6═══════════════════════════════════");
        return true;
    }

    /**
     * Affiche l'aide admin
     */
    private boolean showAdminHelp(CommandSender sender) {
        sender.sendMessage("§6═══════════════════════════════════");
        sender.sendMessage("§6     Commandes Admin");
        sender.sendMessage("§6═══════════════════════════════════");
        sender.sendMessage("§f/evolutionhoe reload §7- Recharge la config");
        sender.sendMessage("§f/evolutionhoe status §7- Affiche le statut");
        sender.sendMessage("§f/evolutionhoe version §7- Affiche la version");
        sender.sendMessage("§6═══════════════════════════════════");
        return true;
    }
}
