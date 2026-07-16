package fr.wazenpotofeu.evolutionhoe.listeners;

import fr.wazenpotofeu.evolutionhoe.EvolutionHoe;
import fr.wazenpotofeu.evolutionhoe.data.models.HoeModel;
import fr.wazenpotofeu.evolutionhoe.data.models.SalesHistoryModel;
import fr.wazenpotofeu.evolutionhoe.managers.FortuneManager;
import fr.wazenpotofeu.evolutionhoe.managers.HoeManager;
import fr.wazenpotofeu.evolutionhoe.storage.SalesHistoryRepository;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * Listener pour les événements d'économie
 * Gère les ventes et les récompenses
 */
public class EconomyEventListener implements Listener {

    private final EvolutionHoe plugin;
    private final HoeManager hoeManager;
    private final FortuneManager fortuneManager;
    private final SalesHistoryRepository salesHistoryRepository;

    public EconomyEventListener(EvolutionHoe plugin) {
        this.plugin = plugin;
        this.hoeManager = plugin.getHoeManager();
        this.fortuneManager = plugin.getFortuneManager();
        this.salesHistoryRepository = plugin.getSalesHistoryRepository();
    }

    /**
     * Traite une vente avec une houe EvolutionHoe
     * À appeler depuis les événements de vente du plugin économique
     */
    public void onSale(Player player, long baseAmount) {
        HoeModel hoe = hoeManager.getOrCreateHoe(player.getUniqueId(), player.getName());
        if (hoe == null) {
            return;
        }
        
        // Calculer le multiplicateur
        double multiplier = fortuneManager.calculateSellMultiplier(hoe);
        
        // Vérifier les Jackpots
        if (fortuneManager.isJackpot()) {
            multiplier *= fortuneManager.getJackpotMultiplier();
            player.sendMessage("§6[EvolutionHoe] 🎉 JACKPOT! Vos gains ont été multipliés!");
        }
        
        // Calculer le montant final
        long finalAmount = (long) (baseAmount * multiplier);
        
        // Mettre à jour les statistiques
        hoe.setTotalEarned(hoe.getTotalEarned() + finalAmount);
        
        // Enregistrer l'historique de vente
        SalesHistoryModel sale = SalesHistoryModel.builder()
                .hoeId(hoe.getId())
                .amount(baseAmount)
                .multiplier(multiplier)
                .build();
        
        try {
            salesHistoryRepository.create(sale);
        } catch (java.sql.SQLException e) {
            plugin.getLogger().warning("Erreur lors de l'enregistrement de la vente");
            e.printStackTrace();
        }
        
        // Sauvegarder la houe
        hoeManager.saveHoe(hoe);
        
        // Donner l'argent au joueur
        // TODO: Intégrer avec l'API économique (Vault, Economy, etc)
        player.sendMessage("§6[EvolutionHoe] 💰 Vous avez gagné " + finalAmount + " (x" + String.format("%.2f", multiplier) + ")");
    }
}
