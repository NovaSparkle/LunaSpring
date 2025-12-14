package org.novasparkle.lunaspring.self;

import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.novasparkle.lunaspring.API.configuration.IConfig;
import org.novasparkle.lunaspring.API.util.service.managers.ColorManager;
import org.novasparkle.lunaspring.LunaSpring;
import org.novasparkle.lunaspring.self.configuration.Message;

import java.util.List;
import java.util.stream.Collectors;


public final class LSConfig {
    @Getter private static final IConfig config;
    @Getter private static boolean debugEnabled;
    static {
        config = new IConfig(LunaSpring.getInstance());
        ColorManager.init(config);
        debugEnabled = getBoolean("debug");
    }

    public static void reload() {
        config.reload(LunaSpring.getInstance());
        debugEnabled = getBoolean("debug");
    }

    public static String getMessage(String path) {
        return getString(String.format("messages.%s", path));
    }

    public static List<String> getStringList(String path) {
        return config.getStringList(String.format("messages.%s", path)).stream().map(ColorManager::color).collect(Collectors.toList());
    }

    public static String getString(String path) {
        return ColorManager.color(config.getString(path));
    }

    public static boolean getBoolean(String path) {
        return config.getBoolean(path);
    }

    public static void sendMessage(CommandSender sender, String id, String... rpl) {
        config.sendMessage(sender, id, rpl);
    }
    public static void sendMessage(CommandSender sender, Message message, String... rpl) {
        config.sendMessage(sender, message.getMessageId(), rpl);
    }
}
