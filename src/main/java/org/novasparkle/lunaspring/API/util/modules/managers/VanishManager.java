package org.novasparkle.lunaspring.API.util.modules.managers;

import lombok.experimental.UtilityClass;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;
import org.novasparkle.lunaspring.API.util.modules.realized.VanishModule;
import org.novasparkle.lunaspring.LunaPlugin;

import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.function.Supplier;

@UtilityClass
public class VanishManager {
    private VanishModule module;

    public Set<UUID> getVanished() {
        return module.getVanished();
    }

    public LunaPlugin getOwnPlugin() {
        return module.getOwnPlugin();
    }

    public Predicate<CommandSender> getCheckView() {
        return module.getCheckView();
    }

    public double[] getRadiusEntityCheck() {
        return module.getRadiusEntityCheck();
    }

    public boolean enable(Player player) {
        return module.enable(player);
    }

    public boolean disable(Player player) {
        return module.disable(player);
    }

    public boolean isVanished(UUID uuid) {
        return module.isVanished(uuid);
    }

    public boolean isVanished(@NotNull OfflinePlayer player) {
        return module.isVanished(player);
    }

    public boolean view(CommandSender viewer, @NotNull OfflinePlayer player) {
        return module.view(viewer, player);
    }

    public boolean view(CommandSender viewer, UUID uuid) {
        return module.view(viewer, uuid);
    }

    public Player exact(Supplier<Player> supplier) {
        return module.exact(supplier);
    }

    public Player exact(String name) {
        return module.exact(name);
    }

    public Player exact(UUID uuid) {
        return module.exact(uuid);
    }

    public Player exact(CommandSender viewer, Supplier<Player> supplier) {
        return module.exact(viewer, supplier);
    }

    public Player exact(CommandSender viewer, String name) {
        return module.exact(viewer, name);
    }

    public Player exact(CommandSender viewer, UUID uuid) {
        return module.exact(viewer, uuid);
    }

    public void register(VanishModule vanishModule) {
        module = vanishModule;
    }

    public void safeRegister(VanishModule vanishModule) {
        if (module != null) register(vanishModule);
    }

    public void processJoinHandler(PlayerJoinEvent event) {
        module.processJoinHandler(event);
    }

    public boolean mayUse() {
        return module != null;
    }
}
