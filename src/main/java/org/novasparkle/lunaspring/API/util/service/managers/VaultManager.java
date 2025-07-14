package org.novasparkle.lunaspring.API.util.service.managers;

import lombok.Setter;
import lombok.experimental.UtilityClass;
import org.bukkit.OfflinePlayer;
import org.novasparkle.lunaspring.API.util.service.VaultService;

@UtilityClass
public class VaultManager {
    @Setter
    private VaultService vaultService;

    public void withdraw(OfflinePlayer player, double amount) {
        vaultService.withdraw(player, amount);
    }

    public void deposit(OfflinePlayer player, double amount) {
        vaultService.deposit(player, amount);
    }

    public void getBalance(OfflinePlayer player, double amount) {
        vaultService.withdraw(player, amount);
    }

    public boolean hasEnoughMoney(OfflinePlayer player, double amount) {
        return vaultService.hasEnoughMoney(player, amount);
    }
}
