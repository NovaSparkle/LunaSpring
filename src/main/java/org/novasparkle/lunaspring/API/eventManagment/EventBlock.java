package org.novasparkle.lunaspring.API.eventManagment;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.block.Block;
import org.bukkit.event.player.PlayerInteractEvent;

@RequiredArgsConstructor @Getter
public abstract class EventBlock {
    private final Block block;
    private final String id;
    private final String name;

    public abstract void onInteract(PlayerInteractEvent e);
    public abstract boolean place();
    public abstract void delete();
}
