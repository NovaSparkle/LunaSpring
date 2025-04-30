package org.novasparkle.lunaspring.API.eventManagment;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.novasparkle.lunaspring.API.eventManagment.managers.LunaEventManager;
import org.novasparkle.lunaspring.API.util.utilities.Utils;

@Getter
public class EventBar {
    private final String starterText;
    private final BossBar bossBar;
    private final LunaEvent lunaEvent;
    public EventBar(LunaEvent lunaEvent, String title, BarColor barColor, BarStyle barStyle) {
        this.bossBar = Bukkit.createBossBar(title, barColor, barStyle);
        Utils.playersAction(this.bossBar::addPlayer);

        this.starterText = title;
        this.lunaEvent = lunaEvent;

        this.update();
    }

    public EventBar(LunaEvent lunaEvent, String title) {
        this(lunaEvent, title, BarColor.PURPLE, BarStyle.SEGMENTED_6);
    }

    public void updateProgress() {
        this.bossBar.setProgress((double) this.lunaEvent.getDelay().getLeftSeconds() / this.lunaEvent.getDelay().getMax());
    }

    public void updateTitle() {
        String text = this.starterText;
        Player player = this.bossBar.getPlayers().getFirst();

        Location location = this.lunaEvent.getLocation();
        String x = location == null ? "---" : String.valueOf(location.getBlockX());
        String y = location == null ? "---" : String.valueOf(location.getBlockY());
        String z = location == null ? "---" : String.valueOf(location.getBlockZ());
        String world = location == null ? "---" : location.getWorld().getName();

        text = Utils.applyReplacements(text, x, y, z, "world-%-" + world, "left-%-" + this.lunaEvent.getDelay().getLeftSeconds(),
                "name-%-" + LunaEventManager.getManager(this.lunaEvent.getLunaPlugin()), "max-%-" + this.lunaEvent.getDelay().getMax());
        this.bossBar.setTitle(player == null ? text : Utils.setPlaceholders(player, text));
    }

    public void update() {
        this.updateTitle();
        this.updateProgress();
    }
}
