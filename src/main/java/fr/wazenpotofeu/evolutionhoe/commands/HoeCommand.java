package fr.wazenpotofeu.evolutionhoe.commands;

import fr.wazenpotofeu.evolutionhoe.EvolutionHoe;
import fr.wazenpotofeu.evolutionhoe.data.models.HoeModel;
import fr.wazenpotofeu.evolutionhoe.managers.HoeManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Commande principale /hoe
 * Affiche les informations de la houe et gère les sous-commandes
 */
public class HoeCommand implements CommandExecutor {

    private final EvolutionHoe plugin;
    private final HoeManager hoeManager;

    public HoeCommand(EvolutionHoe plugin) {
        this.plugin = plugin;
        this.hoeManager = plugin.getHoeManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cCette commande ne peut être exécutée que par un joueur.");
            return true;
        }

        Player player = (Player) sender;
        
        if (args.length == 0) {
            return showHoeInfo(player);
        }

        String subCommand = args[0].toLowerCase();
        
        return switch (subCommand) {
            case "stats" -> showHoeStats(player);
            case "info" -> showHoeInfo(player);
            case "level" -> showHoeLevel(player);
            case "DNA" -> showHoeDNA(player);
            case "mutations" -> showHoeMutations(player);
            case "gems" -> showHoeGems(player);
            case "help" -> showHelp(player);
            default -> {
                player.sendMessage("§c/hoe help pour voir les commandes disponibles.");
                yield false;
            }
        };
    }

    /**
     * Affiche les informations de la houe
     */
    private boolean showHoeInfo(Player player) {
        HoeModel hoe = hoeManager.getOrCreateHoe(player.getUniqueId(), player.getName());
        if (hoe == null) {
            player.sendMessage("§cErreur lors du chargement de votre houe.");
            return true;
        }

        player.sendMessage("§6═══════════════════════════════════");
        player.sendMessage("§6     Informations de votre Houe");
        player.sendMessage("§6═══════════════════════════════════");
        player.sendMessage("§fNiveau: §a" + hoe.getLevel());
        player.sendMessage("§fPrestige: §c" + hoe.getPrestige());
        player.sendMessage("§fADN: §9" + hoe.getDna());
        player.sendMessage("§fFortune: §6" + String.format("%.2f", hoe.getFortune()));
        player.sendMessage("§fMultiplicateur: §6" + String.format("%.2f", hoe.getSellMultiplier()));
        player.sendMessage("§6═══════════════════════════════════");
        return true;
    }

    /**
     * Affiche les statistiques de la houe
     */
    private boolean showHoeStats(Player player) {
        HoeModel hoe = hoeManager.getOrCreateHoe(player.getUniqueId(), player.getName());
        if (hoe == null) {
            player.sendMessage("§cErreur lors du chargement de votre houe.");
            return true;
        }

        player.sendMessage("§6═══════════════════════════════════");
        player.sendMessage("§6     Statistiques de votre Houe");
        player.sendMessage("§6═══════════════════════════════════");
        player.sendMessage("§fBlocs récoltés: §a" + hoe.getTotalHarvested());
        player.sendMessage("§fTotal gagné: §6$" + hoe.getTotalEarned());
        player.sendMessage("§fXP actuel: §a" + hoe.getXp() + " / " + hoeManager.calculateMaxXpForLevel(hoe.getLevel()));
        player.sendMessage("§6═══════════════════════════════════");
        return true;
    }

    /**
     * Affiche le niveau et le progression XP
     */
    private boolean showHoeLevel(Player player) {
        HoeModel hoe = hoeManager.getOrCreateHoe(player.getUniqueId(), player.getName());
        if (hoe == null) {
            player.sendMessage("§cErreur lors du chargement de votre houe.");
            return true;
        }

        long maxXp = hoeManager.calculateMaxXpForLevel(hoe.getLevel());
        double progress = (double) hoe.getXp() / maxXp * 100;
        
        player.sendMessage("§6═══════════════════════════════════");
        player.sendMessage("§6     Progression de Niveau");
        player.sendMessage("§6═══════════════════════════════════");
        player.sendMessage("§fNiveau: §a" + hoe.getLevel() + " §f(Prestige: §c" + hoe.getPrestige() + "§f)");
        player.sendMessage("§fXP: §a" + hoe.getXp() + " §f/ §c" + maxXp);
        player.sendMessage("§fProgression: " + getProgressBar(progress));
        player.sendMessage("§6═══════════════════════════════════");
        return true;
    }

    /**
     * Affiche l'ADN de la houe
     */
    private boolean showHoeDNA(Player player) {
        HoeModel hoe = hoeManager.getOrCreateHoe(player.getUniqueId(), player.getName());
        if (hoe == null) {
            player.sendMessage("§cErreur lors du chargement de votre houe.");
            return true;
        }

        player.sendMessage("§6═══════════════════════════════════");
        player.sendMessage("§6     Génétique de votre Houe");
        player.sendMessage("§6═══════════════════════════════════");
        player.sendMessage("§fADN Unique: §9" + hoe.getDna());
        player.sendMessage("§fMutation actuelle: §6" + (hoe.getCurrentMutation() != null ? hoe.getCurrentMutation() : "Aucune"));
        player.sendMessage("§6═══════════════════════════════════");
        return true;
    }

    /**
     * Affiche les mutations de la houe
     */
    private boolean showHoeMutations(Player player) {
        HoeModel hoe = hoeManager.getOrCreateHoe(player.getUniqueId(), player.getName());
        if (hoe == null) {
            player.sendMessage("§cErreur lors du chargement de votre houe.");
            return true;
        }

        int mutationCount = plugin.getMutationManager().getMutationCount(hoe);
        player.sendMessage("§6═══════════════════════════════════");
        player.sendMessage("§6     Mutations de votre Houe");
        player.sendMessage("§6═══════════════════════════════════");
        player.sendMessage("§fNombre total de mutations: §a" + mutationCount);
        player.sendMessage("§fMutation actuelle: §6" + (hoe.getCurrentMutation() != null ? hoe.getCurrentMutation() : "Aucune"));
        player.sendMessage("§6═══════════════════════════════════");
        return true;
    }

    /**
     * Affiche les gemmes de la houe
     */
    private boolean showHoeGems(Player player) {
        HoeModel hoe = hoeManager.getOrCreateHoe(player.getUniqueId(), player.getName());
        if (hoe == null) {
            player.sendMessage("§cErreur lors du chargement de votre houe.");
            return true;
        }

        int gemCount = plugin.getGemManager().getGemCount(hoe);
        player.sendMessage("§6═══════════════════════════════════");
        player.sendMessage("§6     Gemmes de votre Houe");
        player.sendMessage("§6═══════════════════════════════════");
        player.sendMessage("§fNombre de gemmes: §a" + gemCount + " §f/ 4");
        player.sendMessage("§6═══════════════════════════════════");
        return true;
    }

    /**
     * Affiche l'aide des commandes
     */
    private boolean showHelp(Player player) {
        player.sendMessage("§6═══════════════════════════════════");
        player.sendMessage("§6     Commandes EvolutionHoe");
        player.sendMessage("§6═══════════════════════════════════");
        player.sendMessage("§f/hoe §7- Affiche les infos de la houe");
        player.sendMessage("§f/hoe stats §7- Affiche les statistiques");
        player.sendMessage("§f/hoe level §7- Affiche la progression XP");
        player.sendMessage("§f/hoe dna §7- Affiche l'ADN");
        player.sendMessage("§f/hoe mutations §7- Affiche les mutations");
        player.sendMessage("§f/hoe gems §7- Affiche les gemmes");
        player.sendMessage("§6═══════════════════════════════════");
        return true;
    }

    /**
     * Crée une barre de progression
     */
    private String getProgressBar(double progress) {
        int bars = (int) (progress / 10);
        StringBuilder bar = new StringBuilder("§a");
        for (int i = 0; i < 10; i++) {
            if (i < bars) {
                bar.append("█");
            } else {
                bar.append("§7█");
            }
        }
        bar.append("§f ").append(String.format("%.1f", progress)).append("%");
        return bar.toString();
    }
}
