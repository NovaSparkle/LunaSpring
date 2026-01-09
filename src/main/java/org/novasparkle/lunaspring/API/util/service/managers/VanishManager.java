package org.novasparkle.lunaspring.API.util.service.managers;

import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.novasparkle.lunaspring.API.util.service.realized.VanishService;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

@UtilityClass
public class VanishManager {
    private final VanishService service;
    static {
        service = new VanishService();
    }

    public boolean enable(Player player) {
        return service.enable(player);
    }

    public boolean disable(Player player) {
        return service.disable(player);
    }

    public boolean isVanished(UUID uuid) {
        return service.isVanished(uuid);
    }

    public boolean isVanished(@NotNull Player player) {
        return service.isVanished(player);
    }

    public List<UUID> vanished() {
        return service.getVanished();
    }

    public Player exact(Supplier<Player> supplier) {
        return service.exact(supplier);
    }

    public Player exact(String name) {
        return service.exact(name);
    }

    public Player exact(UUID uuid) {
        return service.exact(uuid);
    }
}
