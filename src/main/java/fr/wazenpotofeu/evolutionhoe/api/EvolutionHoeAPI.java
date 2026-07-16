package fr.wazenpotofeu.evolutionhoe.api;

import fr.wazenpotofeu.evolutionhoe.EvolutionHoe;
import fr.wazenpotofeu.evolutionhoe.managers.*;
import java.util.Optional;

/**
 * Implémentation de l'API principale
 * Fournit accès à tous les services d'EvolutionHoe
 */
public class EvolutionHoeAPI implements IEvolutionHoeAPI, IHoeAPI, IFortuneAPI, IMutationAPI, IGemAPI {

    private final EvolutionHoe plugin;
    private final HoeManager hoeManager;
    private final FortuneManager fortuneManager;
    private final MutationManager mutationManager;
    private final GemManager gemManager;

    public EvolutionHoeAPI(EvolutionHoe plugin) {
        this.plugin = plugin;
        this.hoeManager = plugin.getHoeManager();
        this.fortuneManager = plugin.getFortuneManager();
        this.mutationManager = plugin.getMutationManager();
        this.gemManager = plugin.getGemManager();
    }

    // IEvolutionHoeAPI
    @Override
    public IHoeAPI getHoeAPI() { return this; }

    @Override
    public IFortuneAPI getFortuneAPI() { return this; }

    @Override
    public IMutationAPI getMutationAPI() { return this; }

    @Override
    public IGemAPI getGemAPI() { return this; }

    // IHoeAPI
    @Override
    public fr.wazenpotofeu.evolutionhoe.data.models.HoeModel getOrCreateHoe(java.util.UUID playerUuid, String playerName) {
        return hoeManager.getOrCreateHoe(playerUuid, playerName);
    }

    @Override
    public Optional<fr.wazenpotofeu.evolutionhoe.data.models.HoeModel> getHoe(java.util.UUID playerUuid) {
        return hoeManager.getHoe(playerUuid);
    }

    @Override
    public Optional<fr.wazenpotofeu.evolutionhoe.data.models.HoeModel> getHoeByDna(String dna) {
        try {
            return plugin.getHoeRepository().findByDna(dna);
        } catch (java.sql.SQLException e) {
            return Optional.empty();
        }
    }

    @Override
    public void saveHoe(fr.wazenpotofeu.evolutionhoe.data.models.HoeModel hoe) {
        hoeManager.saveHoe(hoe);
    }

    @Override
    public void addXp(fr.wazenpotofeu.evolutionhoe.data.models.HoeModel hoe, long amount) {
        hoeManager.addXp(hoe, amount);
    }

    @Override
    public String generateDNA() {
        return hoeManager.generateDNA();
    }

    // IFortuneAPI
    @Override
    public double calculateFortune(fr.wazenpotofeu.evolutionhoe.data.models.HoeModel hoe) {
        return fortuneManager.calculateFortune(hoe);
    }

    @Override
    public double calculateSellMultiplier(fr.wazenpotofeu.evolutionhoe.data.models.HoeModel hoe) {
        return fortuneManager.calculateSellMultiplier(hoe);
    }

    @Override
    public long calculateFinalSaleAmount(fr.wazenpotofeu.evolutionhoe.data.models.HoeModel hoe, long baseAmount) {
        return fortuneManager.calculateFinalSaleAmount(hoe, baseAmount);
    }

    @Override
    public boolean isJackpot() {
        return fortuneManager.isJackpot();
    }

    @Override
    public double getJackpotMultiplier() {
        return fortuneManager.getJackpotMultiplier();
    }

    @Override
    public void addFortune(fr.wazenpotofeu.evolutionhoe.data.models.HoeModel hoe, double amount) {
        fortuneManager.addFortune(hoe, amount);
    }

    @Override
    public void addSellMultiplier(fr.wazenpotofeu.evolutionhoe.data.models.HoeModel hoe, double amount) {
        fortuneManager.addSellMultiplier(hoe, amount);
    }

    // IMutationAPI
    @Override
    public boolean canMutate(fr.wazenpotofeu.evolutionhoe.data.models.HoeModel hoe) {
        return mutationManager.canMutate(hoe);
    }

    @Override
    public fr.wazenpotofeu.evolutionhoe.data.models.MutationModel generateMutation(fr.wazenpotofeu.evolutionhoe.data.models.HoeModel hoe) {
        return mutationManager.generateMutation(hoe);
    }

    @Override
    public void applyMutation(fr.wazenpotofeu.evolutionhoe.data.models.HoeModel hoe, fr.wazenpotofeu.evolutionhoe.data.models.MutationModel mutation) {
        mutationManager.applyMutation(hoe, mutation);
    }

    @Override
    public Optional<fr.wazenpotofeu.evolutionhoe.data.models.MutationModel> getLatestMutation(fr.wazenpotofeu.evolutionhoe.data.models.HoeModel hoe) {
        return mutationManager.getLatestMutation(hoe);
    }

    @Override
    public int getMutationCount(fr.wazenpotofeu.evolutionhoe.data.models.HoeModel hoe) {
        return mutationManager.getMutationCount(hoe);
    }

    // IGemAPI
    @Override
    public boolean insertGem(fr.wazenpotofeu.evolutionhoe.data.models.HoeModel hoe, String gemType, int slot) {
        return gemManager.insertGem(hoe, gemType, slot);
    }

    @Override
    public boolean removeGem(fr.wazenpotofeu.evolutionhoe.data.models.HoeModel hoe, int slot) {
        return gemManager.removeGem(hoe, slot);
    }

    @Override
    public java.util.List<fr.wazenpotofeu.evolutionhoe.data.models.GemModel> getGemsForHoe(fr.wazenpotofeu.evolutionhoe.data.models.HoeModel hoe) {
        return gemManager.getGemsForHoe(hoe);
    }

    @Override
    public fr.wazenpotofeu.evolutionhoe.data.models.GemModel.GemBonus calculateTotalGemBonus(fr.wazenpotofeu.evolutionhoe.data.models.HoeModel hoe) {
        return gemManager.calculateTotalGemBonus(hoe);
    }

    @Override
    public boolean isValidSlot(int slot) {
        return gemManager.isValidSlot(slot);
    }

    @Override
    public boolean isValidGemType(String gemType) {
        return gemManager.isValidGemType(gemType);
    }

    @Override
    public int getGemCount(fr.wazenpotofeu.evolutionhoe.data.models.HoeModel hoe) {
        return gemManager.getGemCount(hoe);
    }
}
