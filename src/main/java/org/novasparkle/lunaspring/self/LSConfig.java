package org.novasparkle.lunaspring.self;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.novasparkle.lunaspring.API.configuration.IConfig;
import org.novasparkle.lunaspring.API.util.service.managers.ColorManager;
import org.novasparkle.lunaspring.LunaSpring;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public final class LSConfig {
    private final IConfig config;
    static {
        config = new IConfig(LunaSpring.getINSTANCE());
    }

    public void reload() {
        config.reload(LunaSpring.getINSTANCE());
    }

    public String getMessage(String path) {
        return getString(String.format("messages.%s", path));
    }

    public List<String> getStringList(String path) {
        return config.getStringList(String.format("messages.%s", path)).stream().map(ColorManager::color).collect(Collectors.toList());
    }

    public @NotNull String getString(String path) {
        return ColorManager.color(config.getString(path));
    }

    public boolean getBoolean(String path) {
        return config.getBoolean(path);
    }
}
