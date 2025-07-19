package org.novasparkle.lunaspring.API.util.service;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.novasparkle.lunaspring.API.util.utilities.Utils;
import org.novasparkle.lunaspring.LunaSpring;
import org.novasparkle.lunaspring.self.LSConfig;

import static org.bukkit.Bukkit.getServer;

public class VaultService implements LunaService {
    private Economy economy;

    public VaultService() {
        if (!Utils.hasPlugin("Vault") || !Utils.isPluginEnabled("Vault")) {
            LunaSpring.getInstance().warning(LSConfig.getMessage("noDependency").replace("[dependency]", "Vault"));
        } else {
            RegisteredServiceProvider<Economy> registeredServiceProvider = getServer().getServicesManager().getRegistration(Economy.class);
            if (registeredServiceProvider == null) {
                LunaSpring.getInstance().warning(LSConfig.getMessage("noVaultProvider"));
            } else {
                this.economy = registeredServiceProvider.getProvider();
            }
        }
    }

    public double getBalance(OfflinePlayer player) {
        return this.economy.getBalance(player);
    }

    public void deposit(OfflinePlayer player, double amount) {
        this.economy.depositPlayer(player, amount);
    }

    public void withdraw(OfflinePlayer player, double amount) {
        this.economy.withdrawPlayer(player, amount);
    }

    public boolean hasEnoughMoney(OfflinePlayer player, double amount) {
        return this.getBalance(player) >= amount;
    }
}