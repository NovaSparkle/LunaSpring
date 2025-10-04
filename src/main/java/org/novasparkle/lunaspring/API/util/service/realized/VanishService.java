package org.novasparkle.lunaspring.API.util.service.realized;

import com.google.common.collect.Lists;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.novasparkle.lunaspring.API.events.vanish.VanishDisableEvent;
import org.novasparkle.lunaspring.API.events.vanish.VanishEnableEvent;
import org.novasparkle.lunaspring.API.util.service.LunaService;
import org.novasparkle.lunaspring.API.util.utilities.Utils;
import org.novasparkle.lunaspring.LunaSpring;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;

@Getter
public class VanishService implements LunaService {
    private final List<UUID> vanished = Lists.newArrayList();

    public boolean enable(Player player) {
        VanishEnableEvent event = new VanishEnableEvent(player, null);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) return false;

        this.vanished.add(player.getUniqueId());
        Utils.playersAction(p -> {
            if (event.getCheckViewPredicate() == null || event.getCheckViewPredicate().test(p)) p.hidePlayer(LunaSpring.getInstance(), player);
        });

        for (Entity e : player.getNearbyEntities(16, 32, 16)) {
            if (e instanceof Mob mob && Objects.equals(mob.getTarget(), player)) mob.setTarget(null);
        }

        return true;
    }

    public boolean disable(Player player) {
        VanishDisableEvent event = new VanishDisableEvent(player);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) return false;

        this.vanished.remove(player.getUniqueId());
        Utils.playersAction(p -> p.showPlayer(LunaSpring.getInstance(), player));

        return true;
    }

    public boolean isVanished(UUID uuid) {
        return this.vanished.contains(uuid);
    }

    public boolean isVanished(@NotNull Player player) {
        return this.isVanished(player.getUniqueId());
    }

    public Player exact(Supplier<Player> supplier) {
        Player player = supplier.get();
        return player != null && this.isVanished(player) ? null : player;
    }

    public Player exact(String name) {
        return exact(() -> Bukkit.getPlayer(name));
    }

    public Player exact(UUID uuid) {
        return exact(() -> Bukkit.getPlayer(uuid));
    }
}
