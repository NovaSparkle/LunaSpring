package org.novasparkle.lunaspring.API.events;

import com.destroystokyo.paper.event.player.PlayerElytraBoostEvent;
import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import com.destroystokyo.paper.event.player.PlayerPickupExperienceEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;
import org.novasparkle.lunaspring.API.util.service.managers.worldguard.LunaFlags;
import org.novasparkle.lunaspring.API.util.utilities.Utils;

public class WorldGuardHandler implements Listener {
    private boolean isDisabled() {
        return !Utils.isPluginEnabled("WorldGuard");
    }
    
    @EventHandler
    public void onGliding(PlayerMoveEvent event) {
        if (this.isDisabled()) return;

        Player player = event.getPlayer();
        if (!player.isGliding()) return;

        if (!LunaFlags.State.ELYTRA_GLIDE_HARD_FLAG.check(player)) {
            player.setGliding(false);
            event.setCancelled(true);
            return;
        }

        if (!LunaFlags.State.ELYTRA_GLIDE_FLAG.check(player)) player.setGliding(false);
    }

    @EventHandler
    public void onBoost(PlayerElytraBoostEvent event) {
        if (this.isDisabled()) return;

        Player player = event.getPlayer();
        if (!LunaFlags.State.ELYTRA_BOOST_FLAG.check(player)) event.setCancelled(true);
    }

    @EventHandler
    public void onFlight(PlayerToggleFlightEvent event) {
        if (this.isDisabled()) return;

        Player player = event.getPlayer();
        if (!event.isFlying()) return;

        if (!LunaFlags.State.ENABLE_FLY_FLAG.check(player)) event.setCancelled(true);
    }

    @EventHandler
    public void onSprint(PlayerToggleSprintEvent event) {
        if (this.isDisabled()) return;

        Player player = event.getPlayer();
        if (!event.isSprinting()) return;

        if (!LunaFlags.State.ENABLE_SPRINT_FLAG.check(player)) event.setCancelled(true);
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event) {
        if (this.isDisabled()) return;

        Player player = event.getPlayer();
        if (!event.isSneaking()) return;

        if (!LunaFlags.State.ENABLE_SNEAK_FLAG.check(player)) event.setCancelled(true);
    }

    @EventHandler
    public void onShear(PlayerShearEntityEvent event) {
        if (this.isDisabled()) return;

        Player player = event.getPlayer();
        if (!LunaFlags.State.SHEAR_FLAG.check(player)) event.setCancelled(true);
    }

    @EventHandler
    public void onBucketFill(PlayerBucketFillEvent event) {
        if (this.isDisabled()) return;

        Player player = event.getPlayer();
        if (!LunaFlags.State.BUCKET_FILL_FLAG.check(player)) event.setCancelled(true);
    }

    @EventHandler
    public void onBucketEmpty(PlayerBucketEmptyEvent event) {
        if (this.isDisabled()) return;

        Player player = event.getPlayer();
        if (!LunaFlags.State.BUCKET_EMPTY_FLAG.check(player)) event.setCancelled(true);
    }

    @EventHandler
    public void onPickupExp(PlayerPickupExperienceEvent event) {
        if (this.isDisabled()) return;

        Player player = event.getPlayer();
        if (!LunaFlags.State.PICKUP_EXP_FLAG.check(player)) event.setCancelled(true);
    }

    @EventHandler
    public void onJump(PlayerJumpEvent e) {
        if (this.isDisabled()) return;

        Player player = e.getPlayer();
        if (!LunaFlags.State.JUMP_FLAG.check(player)) e.setCancelled(true);
    }

    @EventHandler
    public void onItemDamage(PlayerItemDamageEvent e) {
        if (this.isDisabled()) return;

        Player player = e.getPlayer();
        if (!LunaFlags.State.ITEM_DAMAGE_FLAG.check(player)) e.setCancelled(true);
    }

    @EventHandler
    public void onPrepareCraft(CraftItemEvent e) {
        if (this.isDisabled() || !(e.getWhoClicked() instanceof Player player)) return;

        if (!LunaFlags.State.CRAFT_FLAG.check(player)) e.setCancelled(true);
    }

    @EventHandler
    public void getDamage(EntityDamageEvent e) {
        if (this.isDisabled()) return;

        if (e.getEntity() instanceof Player player) {
            if (!LunaFlags.State.GET_DAMAGE_FLAG.check(player)) {
                e.setDamage(0);
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void usePortal(PlayerPortalEvent e) {
        if (this.isDisabled()) return;

        Player player = e.getPlayer();
        if (!LunaFlags.State.USE_PORTALS_FLAG.check(player)) e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void openInventory(InventoryOpenEvent e) {
        if (this.isDisabled()) return;

        Player player = (Player) e.getPlayer();
        if (!LunaFlags.State.OPEN_INVENTORY_FLAG.check(player)) e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void clickInventory(InventoryClickEvent e) {
        if (this.isDisabled()) return;

        Player player = (Player) e.getWhoClicked();
        if (!LunaFlags.State.CLICK_INVENTORY_FLAG.check(player)) e.setCancelled(true);
    }
}
