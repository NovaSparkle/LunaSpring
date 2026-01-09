package org.novasparkle.lunaspring.API.events.vanish;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

@Setter @Getter
public class VanishEnableEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private boolean cancelled = false;
    private Predicate<Player> checkViewPredicate;
    public VanishEnableEvent(@NotNull Player who, @Nullable Predicate<Player> checkViewPermission) {
        super(who);
        this.checkViewPredicate = checkViewPermission;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static @NotNull HandlerList getHandlerList() {
        return handlers;
    }
}
