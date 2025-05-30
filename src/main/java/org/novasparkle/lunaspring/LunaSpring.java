package org.novasparkle.lunaspring;

import lombok.Getter;
import org.novasparkle.lunaspring.API.commands.LunaExecutor;
import org.novasparkle.lunaspring.API.util.service.managers.ColorManager;
import org.novasparkle.lunaspring.API.util.utilities.Color;
import org.novasparkle.lunaspring.API.util.utilities.Utils;
import org.novasparkle.lunaspring.self.LSConfig;
import org.novasparkle.lunaspring.self.PaidPlugin;
import org.novasparkle.lunaspring.self.lunaengine.LunaEngine;

import java.util.HashSet;
import java.util.Set;

public final class LunaSpring extends LunaPlugin {
    @Getter
    private static LunaSpring INSTANCE;
    @Getter
    private final Set<LunaPlugin> hookedPlugins = new HashSet<>();
    private LunaEngine LE;

    @Override
    public void onEnable() {
        if (INSTANCE != null) super.onEnable();
        INSTANCE = this;
        this.LE = new LunaEngine();
        this.saveDefaultConfig();
        this.loadFile("localization.yml");
        this.processListeners();
        LunaExecutor.initialize(this);

        this.registerLunaPlaceholder();
    }

    private void registerLunaPlaceholder() {
        this.createPlaceholder(((offlinePlayer, params) -> {
            if (params.equalsIgnoreCase("hooked")) {
                return String.join(", ", this.hookedPlugins.stream().map(LunaPlugin::getName).toList()); // Список луна плагинов
            }

            if (params.startsWith("world-")) { // %lunaspring_world-world%
                String[] split = params.split("-");

                String placeholder = split.length >= 2 ? LSConfig.getMessage(String.format("worlds.%s", split[1])) : null;
                return placeholder == null || placeholder.isEmpty() ? (split.length == 1 ? null : split[1]) : placeholder;
            }

            if (params.startsWith("color-")) {
                String[] split = params.split("-");

                if (split.length >= 2) {
                    Color color = ColorManager.getColor(split[1]);
                    return color != null ? color.getVariable() : null;
                }
                return "";
            }



            if (params.startsWith("lp-")) { // %lunaspring_lp-SHORT_TRANSLATE_WITH_POINTS-DAYS_OR_HOURS%
                String[] split = params.split("-");
                if (split.length < 2) return null;

                Utils.Luckperms.TranslateType translateType = Utils.Luckperms.TranslateType.valueOf(split[1]);
                Utils.Luckperms.FormatType formatType = Utils.Luckperms.FormatType.valueOf(split[2]);
                return Utils.Luckperms.getFormatting(Utils.Luckperms.getGroupTime(offlinePlayer), translateType, formatType);
            }
            return null;
        }));
    }

    public void hookPlugin(LunaPlugin lunaPlugin) {
        if (lunaPlugin != INSTANCE) {
            Class<?> pluginClass = lunaPlugin.getClass();
            if (pluginClass.isAnnotationPresent(PaidPlugin.class) && !this.LE.checkPlugin(lunaPlugin)) {
                this.getPluginLoader().disablePlugin(lunaPlugin);
                this.warning("{E}Виртуальный ключ для плагина не найден!");
                this.warning("{S}Для работы с ним, необходимо получить ключ у администрации -> https://t.me/LunaEngineBot");
            } else
                this.hookedPlugins.add(lunaPlugin);
        }
    }

    public LunaPlugin getLunaPlugin(String name) {
        return this.hookedPlugins.stream().filter(pl -> pl.getName().equals(name)).findFirst().orElse(null);
    }
}



