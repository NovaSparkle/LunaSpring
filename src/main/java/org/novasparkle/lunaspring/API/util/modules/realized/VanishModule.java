package org.novasparkle.lunaspring.API.util.modules.realized;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.novasparkle.lunaspring.API.events.vanish.VanishDisableEvent;
import org.novasparkle.lunaspring.API.events.vanish.VanishEnableEvent;
import org.novasparkle.lunaspring.API.util.modules.LunaModule;
import org.novasparkle.lunaspring.API.util.modules.realized.abs.IVanishModule;
import org.novasparkle.lunaspring.API.util.utilities.Utils;
import org.novasparkle.lunaspring.LunaPlugin;
import org.novasparkle.lunaspring.LunaSpring;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.function.Supplier;

@Getter
public class VanishModule extends BasicVanishModule {
    private final Set<UUID> vanished = new HashSet<>();
    public VanishModule(LunaPlugin ownPlugin, Predicate<Player> checkView, double[] radiusEntityCheck) {
        super(ownPlugin, checkView, radiusEntityCheck);
    }

    public VanishModule(LunaPlugin ownPlugin, Predicate<Player> checkView) {
        super(ownPlugin, checkView);
    }

    @Override
    public boolean enable(Player player) {
        if (super.enable(player)) {
            this.vanished.add(player.getUniqueId());
            return true;
        }

        return false;
    }

    @Override
    public boolean disable(Player player) {
        if (super.disable(player)) {
            this.vanished.remove(player.getUniqueId());
            return true;
        }

        return false;
    }

    public boolean isVanished(UUID uuid) {
        return this.vanished.contains(uuid) || super.isVanished(uuid);
    }

    public boolean isVanished(@NotNull OfflinePlayer player) {
        return this.vanished.contains(player.getUniqueId()) || super.isVanished(player);
    }
}
