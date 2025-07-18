package org.novasparkle.lunaspring.API.util.utilities;

import lombok.Builder;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.KeyedBossBar;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.novasparkle.lunaspring.API.util.service.managers.ColorManager;

@Getter
public class LunaBossBar {
    private final String defaultTitle;
    private final KeyedBossBar bar;
    private String title;
    private double progress = 1.0;
    private BarColor barColor = BarColor.PURPLE;
    private BarStyle barStyle = BarStyle.SEGMENTED_6;
    public LunaBossBar(@NotNull String title, String strColor, String strStyle, @NotNull NamespacedKey key) {
        this.defaultTitle = ColorManager.color(title);
        this.title = this.defaultTitle;
        if (strStyle != null && !strStyle.isEmpty()) this.barStyle = BarStyle.valueOf(strStyle);
        if (strColor != null && !strColor.isEmpty()) this.barColor = BarColor.valueOf(strColor);
        this.bar = Bukkit.createBossBar(key, this.defaultTitle, this.barColor, this.barStyle);
    }

    @Builder
    public LunaBossBar(@NotNull String title, String strColor, String strStyle, @NotNull Plugin plugin) {
        this(title, strColor, strStyle, new NamespacedKey(plugin, "lunabar-" + Utils.getRKey((byte) 12)));
    }

    @Builder
    public LunaBossBar(@NotNull String title, @NotNull NamespacedKey key) {
        this(title, null, null, key);
    }

    @Builder
    public LunaBossBar(@NotNull String title, @NotNull Plugin plugin) {
        this(title, null, null, plugin);
    }

    public LunaBossBar update() {
        return this.update(this.title, this.barColor, this.barStyle, this.progress);
    }

    public LunaBossBar update(String title, BarColor color, BarStyle style, double progress) {
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

    public final LunaBossBar setProgress(double value) {
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
}
