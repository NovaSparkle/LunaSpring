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
public class VanishModule implements IVanishModule {
    private final Set<UUID> vanished = new HashSet<>();
    private final LunaPlugin ownPlugin;
    private final Predicate<CommandSender> checkView;
    private final double[] radiusEntityCheck;
    public VanishModule(LunaPlugin ownPlugin, Predicate<Player> checkView, double[] radiusEntityCheck) {
        this.radiusEntityCheck = radiusEntityCheck;
        this.ownPlugin = ownPlugin;
        this.checkView = s -> checkView == null ||
                (!(s instanceof Player p) || checkView.test(p));
    }

    public VanishModule(LunaPlugin ownPlugin, Predicate<Player> checkView) {
        this(ownPlugin, checkView, new double[]{48, 48, 48});
    }

    public boolean enable(Player player) {
        VanishEnableEvent event = new VanishEnableEvent(player, checkView);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) return false;

        this.vanished.add(player.getUniqueId());
        Utils.playersAction(p -> {
            if (event.getCheckViewPredicate() == null || event.getCheckViewPredicate().test(p)) p.hidePlayer(ownPlugin, player);
        });

        player.getNearbyEntities(radiusEntityCheck[0], radiusEntityCheck[1], radiusEntityCheck[2])
                .stream()
                .filter(e -> e instanceof Mob mob && Objects.equals(mob.getTarget(), player))
                .forEach(e -> ((Mob) e).setTarget(null));

        return true;
    }

    public boolean disable(Player player) {
        VanishDisableEvent event = new VanishDisableEvent(player);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) return false;

        this.vanished.remove(player.getUniqueId());
        Utils.playersAction(p -> p.showPlayer(ownPlugin, player));

        return true;
    }

    public boolean isVanished(UUID uuid) {
        return this.vanished.contains(uuid);
    }

    public boolean isVanished(@NotNull OfflinePlayer player) {
        return this.isVanished(player.getUniqueId());
    }

    public boolean view(CommandSender viewer, UUID uuid) {
        return this.vanished.contains(uuid) || this.checkView.test(viewer);
    }

    public boolean view(CommandSender viewer, @NotNull OfflinePlayer player) {
        return view(viewer, player.getUniqueId());
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

    public Player exact(CommandSender viewer, Supplier<Player> supplier) {
        Player player = supplier.get();
        return player != null && this.isVanished(player) ? null : player;
    }

    public Player exact(CommandSender viewer, String name) {
        return exact(viewer, () -> Bukkit.getPlayer(name));
    }

    public Player exact(CommandSender viewer, UUID uuid) {
        return exact(viewer, () -> Bukkit.getPlayer(uuid));
    }

    public void processJoinHandler(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        boolean isVanished = this.isVanished(player);
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (this.isVanished(onlinePlayer) && !this.checkView.test(player))
                player.hidePlayer(ownPlugin, onlinePlayer);
            if (isVanished && !this.checkView.test(onlinePlayer))
                onlinePlayer.hidePlayer(ownPlugin, player);
        }
    }
}
