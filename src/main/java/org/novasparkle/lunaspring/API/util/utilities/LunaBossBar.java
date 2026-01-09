package org.novasparkle.lunaspring.API.util.utilities;

import lombok.Builder;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.KeyedBossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.novasparkle.lunaspring.API.util.service.managers.ColorManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
public class LunaBossBar {
    private final String defaultTitle;
    private final KeyedBossBar bar;
    private final List<BarFlag> flags = new ArrayList<>();
    private String title;
    private float progress = 1.0f;
    private BarColor barColor = BarColor.PURPLE;
    private BarStyle barStyle = BarStyle.SEGMENTED_6;

    @Builder
    public LunaBossBar(@NotNull String title, BarColor barColor, BarStyle barStyle, @NotNull NamespacedKey namespacedKey) {
        this.defaultTitle = ColorManager.color(title);
        this.title = this.defaultTitle;
        if (barStyle != null) this.barStyle = barStyle;
        if (barColor != null) this.barColor = barColor;
        this.bar = Bukkit.createBossBar(namespacedKey, this.defaultTitle, this.barColor, this.barStyle);
    }

    @Builder
    public LunaBossBar(@NotNull String title, BarColor barColor, BarStyle barStyle, @NotNull Plugin plugin) {
        this(title, barColor, barStyle, new NamespacedKey(plugin, "lunabar-" + Utils.getRKey((byte) 12)));
    }

    public LunaBossBar(@NotNull String title, @NotNull NamespacedKey namespacedKey) {
        this(title, (BarColor) null, null, namespacedKey);
    }

    @Builder
    public LunaBossBar(@NotNull String title, String strBarColor, String strBarStyle, @NotNull NamespacedKey namespacedKey) {
        this(title, strBarColor == null || strBarColor.isEmpty() ? null : BarColor.valueOf(strBarColor),
                strBarStyle == null || strBarStyle.isEmpty() ? null : BarStyle.valueOf(strBarStyle),
                namespacedKey);
    }

    @Builder
    public LunaBossBar(@NotNull String title, String strBarColor, String strBarStyle, @NotNull Plugin plugin) {
        this(title, strBarColor, strBarStyle, new NamespacedKey(plugin, "lunabar-" + Utils.getRKey((byte) 12)));
    }

    public LunaBossBar update() {
        return this.update(this.title, this.barColor, this.barStyle, this.progress);
    }

    public LunaBossBar update(String title, BarColor color, BarStyle style, float progress) {
        this.setProgress(progress);
        this.setColor(color);
        this.setStyle(style);
        this.updateTitle(title);
        return this;
    }

    public void delete() {
        this.bar.removeAll();
        Bukkit.removeBossBar(this.bar.getKey());
    }

    public final LunaBossBar setProgress(float value) {
        this.progress = value;
        this.bar.setProgress(Math.max(Math.min(this.progress, 1.0), 0));
        return this;
    }

    public final LunaBossBar setColor(BarColor barColor) {
        this.barColor = barColor;
        this.bar.setColor(this.barColor);
        return this;
    }

    public final LunaBossBar setStyle(BarStyle barStyle) {
        this.barStyle = barStyle;
        this.bar.setStyle(this.barStyle);
        return this;
    }

    public final LunaBossBar updateTitle(String title) {
        this.title = title;
        this.bar.setTitle(this.title);
        return this;
    }

    public LunaBossBar addPlayer(Player player) {
        this.bar.addPlayer(player);
        return this;
    }

    public LunaBossBar addPlayers(Collection<Player> players) {
        players.forEach(this::addPlayer);
        return this;
    }

    public LunaBossBar addPlayers(Player... players) {
        return this.addPlayers(List.of(players));
    }

    public LunaBossBar removePlayer(Player player) {
        this.bar.removePlayer(player);
        return this;
    }

    public LunaBossBar removePlayers(Collection<Player> players) {
        players.forEach(this::removePlayer);
        return this;
    }

    public LunaBossBar removePlayers(Player... players) {
        return this.removePlayers(List.of(players));
    }

    public List<Player> getPlayers() {
        return this.bar.getPlayers();
    }

    public final LunaBossBar addFlag(BarFlag barFlag) {
        if (this.flags.contains(barFlag)) return this;
        this.flags.add(barFlag);
        this.bar.addFlag(barFlag);
        return this;
    }

    public final LunaBossBar removeFlag(BarFlag barFlag) {
        this.flags.remove(barFlag);
        this.bar.removeFlag(barFlag);
        return this;
    }

    public NamespacedKey getKey() {
        return this.bar.getKey();
    }
}
