package org.novasparkle.lunaspring.API.events;

import com.destroystokyo.paper.event.player.PlayerElytraBoostEvent;
import com.destroystokyo.paper.event.player.PlayerPickupExperienceEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.novasparkle.lunaspring.API.util.service.managers.worldguard.GuardManager;
import org.novasparkle.lunaspring.API.util.service.managers.worldguard.LunaFlags;

public class WorldGuardHandler implements Listener {
    @EventHandler
    public void onGliding(PlayerMoveEvent event) {
        if (!GuardManager.isEnabled()) return;

        Player player = event.getPlayer();
        if (!player.isGliding()) return;

        if (!LunaFlags.State.ELYTRA_GLIDE_FLAG.check(player)) event.setCancelled(true);
    }

    @EventHandler
    public void onBoost(PlayerElytraBoostEvent event) {
        if (!GuardManager.isEnabled()) return;

        Player player = event.getPlayer();
        if (!LunaFlags.State.ELYTRA_BOOST_FLAG.check(player)) event.setCancelled(true);
    }

    @EventHandler
    public void onFlight(PlayerToggleFlightEvent event) {
        if (!GuardManager.isEnabled()) return;

        Player player = event.getPlayer();
        if (!event.isFlying()) return;

        if (!LunaFlags.State.ENABLE_FLY_FLAG.check(player)) event.setCancelled(true);
    }

    @EventHandler
    public void onSprint(PlayerToggleSprintEvent event) {
        if (!GuardManager.isEnabled()) return;

        Player player = event.getPlayer();
        if (!event.isSprinting()) return;

        if (!LunaFlags.State.ENABLE_SPRINT_FLAG.check(player)) event.setCancelled(true);
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event) {
        if (!GuardManager.isEnabled()) return;

        Player player = event.getPlayer();
        if (!event.isSneaking()) return;

        if (!LunaFlags.State.ENABLE_SNEAK_FLAG.check(player)) event.setCancelled(true);
    }

    @EventHandler
    public void onShear(PlayerShearEntityEvent event) {
        if (!GuardManager.isEnabled()) return;

        Player player = event.getPlayer();
        if (!LunaFlags.State.SHEAR_FLAG.check(player)) event.setCancelled(true);
    }

    @EventHandler
    public void onBucketFill(PlayerBucketFillEvent event) {
        if (!GuardManager.isEnabled()) return;

        Player player = event.getPlayer();
        if (!LunaFlags.State.BUCKET_FILL_FLAG.check(player)) event.setCancelled(true);
    }

    @EventHandler
    public void onBucketEmpty(PlayerBucketEmptyEvent event) {
        if (!GuardManager.isEnabled()) return;

        Player player = event.getPlayer();
        if (!LunaFlags.State.BUCKET_EMPTY_FLAG.check(player)) event.setCancelled(true);
    }

    @EventHandler
    public void onPickupExp(PlayerPickupExperienceEvent event) {
        if (!GuardManager.isEnabled()) return;

        Player player = event.getPlayer();
        if (!LunaFlags.State.PICKUP_EXP_FLAG.check(player)) event.setCancelled(true);
    }
}
