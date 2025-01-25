package org.novasparkle.lunaspring;

import lombok.Getter;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.novasparkle.lunaspring.Events.MenuHandler;
import org.novasparkle.lunaspring.Util.Service.ServiceProvider;
import org.novasparkle.lunaspring.Util.managers.ColorManager;

public final class LunaSpring extends JavaPlugin {
    @Getter
    private static final ServiceProvider serviceProvider = new ServiceProvider();
    @Getter
    private static Plugin plugin = null;
    public static MenuHandler initialize(Plugin plugin) {
        LunaSpring.plugin = plugin;
        plugin.getLogger().info(ColorManager.color("&fEngined with &5&lLunaSpring &fby &cNovaSparkle"));
        return new MenuHandler();
    }
}
