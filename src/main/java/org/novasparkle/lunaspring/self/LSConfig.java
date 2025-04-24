package org.novasparkle.lunaspring.self;

import org.novasparkle.lunaspring.API.configuration.IConfig;
import org.novasparkle.lunaspring.API.util.service.managers.ColorManager;
import org.novasparkle.lunaspring.LunaSpring;

import java.util.List;
import java.util.stream.Collectors;

public final class LSConfig extends ColorManager {
    private final static IConfig config;
    static {
        config = new IConfig(LunaSpring.getINSTANCE());
    }
    public static void reload() {
        config.reload(LunaSpring.getINSTANCE());
    }
    public static String getMessage(String path) {
        return getString(String.format("messages.%s", path));
    }

    public static List<String> getStringList(String path) {
        return config.getStringList(String.format("messages.%s", path)).stream().map(ColorManager::color).collect(Collectors.toList());
    }

    public static String getString(String path) {
        return color(config.getString(path));
    }

    public static boolean getBoolean(String path) {
        return config.getBoolean(path);
    }
}
