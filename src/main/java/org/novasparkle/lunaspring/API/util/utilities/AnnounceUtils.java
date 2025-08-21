package org.novasparkle.lunaspring.API.util.utilities;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.Arrays;

@UtilityClass
public class AnnounceUtils {
    public void broadcast(String message) {
        Utils.playersAction(p -> p.sendMessage(message));
    }

    public void sound(Player player, Sound sound, float volume) {
        player.playSound(player.getLocation(), sound, volume, 1);
    }

    public void sound(Player player, Sound sound) {
        sound(player, sound, 1);
    }

    public boolean sound(Player player, String sound, float volume) {
        if (Arrays.stream(Sound.values()).noneMatch(s -> s.name().equalsIgnoreCase(sound))) return false;

        sound(player, Sound.valueOf(sound), volume);
        return true;
    }

    public boolean sound(Player player, String sound) {
        return sound(player, sound, 1);
    }

    public void soundAll(Sound sound, float volume) {
        Utils.playersAction(p -> sound(p, sound, volume));
    }

    public void soundAll(Sound sound) {
        soundAll(sound, 1);
    }

    public boolean soundAll(String sound, float volume) {
        return Bukkit.getOnlinePlayers().stream().allMatch(p -> sound(p, sound, volume));
    }

    public boolean soundAll(String sound) {
        return soundAll(sound, 1);
    }

    public void title(Player player, String title, String subtitle, int inTicks, int easeTicks, int outTicks) {
        player.sendTitle(title, subtitle, inTicks, easeTicks, outTicks);
    }

    public void title(Player player, String title, String subtitle) {
        title(player, title, subtitle, 15, 20, 15);
    }

    public void title(Player player, String[] split, int inTicks, int easeTicks, int outTicks) {
        String title = split.length >= 1 ? split[0] : "";
        String subtitle = split.length >= 2 ? split[1] : "";
        inTicks = split.length >= 3 ? LunaMath.toInt(split[2], inTicks) : inTicks;
        easeTicks = split.length >= 4 ? LunaMath.toInt(split[3], easeTicks) : easeTicks;
        outTicks = split.length >= 5 ? LunaMath.toInt(split[4], outTicks) : outTicks;
        title(player, title, subtitle, inTicks, easeTicks, outTicks);
    }

    public void title(Player player, String[] title) {
        title(player, title, 15, 20, 15);
    }

    public void title(Player player, String title, int inTicks, int easeTicks, int outTicks) {
        title(player, title.split(" <S> "), inTicks, easeTicks, outTicks);
    }

    public void title(Player player, String title) {
        title(player, title, 15, 20, 15);
    }

    public void titleAll(String title, String subTitle, int inTicks, int easeTicks, int outTicks) {
        Utils.playersAction(p -> title(p, title, subTitle, inTicks, easeTicks, outTicks));
    }

    public void titleAll(String title, String subTitle) {
        titleAll(title, subTitle, 15, 20, 15);
    }

    public void titleAll(String[] title, int inTicks, int easeTicks, int outTicks) {
        Utils.playersAction(p -> title(p, title, inTicks, easeTicks, outTicks));
    }

    public void titleAll(String[] title) {
        titleAll(title, 15, 20, 15);
    }

    public void titleAll(String title, int inTicks, int easeTicks, int outTicks) {
        Utils.playersAction(p -> title(p, title, inTicks, easeTicks, outTicks));
    }

    public void titleAll(String title) {
        titleAll(title, 15, 20, 15);
    }

    public BossBar createBar(String title, BarColor barColor, BarStyle style) {
        return Bukkit.createBossBar(title, barColor, style);
    }

    public BossBar createBar(String title) {
        return createBar(title, BarColor.PURPLE, BarStyle.SEGMENTED_6);
    }

    public void addBarFlags(BossBar bar, BarFlag... flags) {
        Arrays.stream(flags).forEach(bar::addFlag);
    }

    public void removeBarFlags(BossBar bar, BarFlag... flags) {
        Arrays.stream(flags).forEach(bar::removeFlag);
    }

    public void addBarPlayers(BossBar bar, Player... players) {
        Arrays.stream(players).forEach(bar::addPlayer);
    }

    public void removeBarPlayers(BossBar bar, Player... players) {
        Arrays.stream(players).forEach(bar::removePlayer);
    }

    public void hideBar(BossBar bar) {
        bar.removeAll();
    }
}
