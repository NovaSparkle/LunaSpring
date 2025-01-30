package org.novasparkle.lunaspring;

import lombok.Getter;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.novasparkle.lunaspring.Events.MenuHandler;
import org.novasparkle.lunaspring.Util.Service.ServiceProvider;
import org.novasparkle.lunaspring.Util.Utils;

import java.util.List;

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
        List<String> startMessage = INSTANCE.getConfig().getStringList("startMessage");
        startMessage.replaceAll(m -> m.replace("[pluginName]", plugin.getName()).replace("[pluginVersion]", plugin.getDescription().getVersion()).replace("[LSVersion]", INSTANCE.getDescription().getVersion()));
        startMessage.forEach(Utils::info);
        return new MenuHandler();
    }
}



