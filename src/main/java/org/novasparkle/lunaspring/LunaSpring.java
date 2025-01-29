package org.novasparkle.lunaspring;

import lombok.Getter;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.novasparkle.lunaspring.Events.MenuHandler;
import org.novasparkle.lunaspring.Util.Service.ServiceProvider;
import org.novasparkle.lunaspring.Util.Utils;

import java.util.Arrays;

public final class LunaSpring extends JavaPlugin {
    @Getter
    private static LunaSpring INSTANCE;
    @Getter
    private static final ServiceProvider serviceProvider = new ServiceProvider();
    @Getter
    private static Plugin plugin = null;

    public void onEnable() {
        INSTANCE = this;
    }

    public static MenuHandler initialize(Plugin plugin) {
        LunaSpring.plugin = plugin;
        Arrays.asList("", Utils.color(String.format("    &b%s &e%s", plugin.getName(), plugin.getDescription().getVersion())), Utils.color(String.format("                    &cEngined with LunaSpring %s", INSTANCE.getDescription().getVersion())), Utils.color("                    &8Author: &cNova Sparkle"), "").forEach(System.out::println);
        return new MenuHandler();
    }
}



