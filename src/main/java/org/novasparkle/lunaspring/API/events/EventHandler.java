package org.novasparkle.lunaspring.API.events;

import org.bukkit.block.Block;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.novasparkle.lunaspring.API.drops.managers.LunaEventManager;

public class EventHandler implements Listener {
    @org.bukkit.event.EventHandler
    public void onBreak(BlockBreakEvent e) {
        Block block = e.getBlock();
        if (LunaEventManager.getActiveEvents().stream().anyMatch(ev -> ev.getEventBlock().getBlock().equals(block))) e.setCancelled(true);
    }

    @org.bukkit.event.EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Block block = e.getClickedBlock();
        if (block == null || block.getType().isAir()) return;

        LunaEventManager.getActiveEvents().forEach(ev -> {
            if (ev.getEventBlock().getBlock().equals(block)) ev.getEventBlock().onInteract(e);
        });
    }
}
