package org.novasparkle.lunaspring;

import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.novasparkle.lunaspring.API.commands.LunaExecutor;
import org.novasparkle.lunaspring.API.util.service.managers.ColorManager;
import org.novasparkle.lunaspring.API.util.service.managers.VaultManager;
import org.novasparkle.lunaspring.API.util.utilities.Color;
import org.novasparkle.lunaspring.API.util.utilities.Localization;
import org.novasparkle.lunaspring.API.util.utilities.Utils;
import org.novasparkle.lunaspring.self.PaidPlugin;
import org.novasparkle.lunaspring.self.lunaengine.LunaEngine;

import java.util.HashSet;
import java.util.Set;

public final class LunaSpring extends LunaPlugin {
    @Getter
    private static LunaSpring instance;
    @Getter
    private final Set<LunaPlugin> hookedPlugins = new HashSet<>();
    private LunaEngine LE;

    @Override
    public void onEnable() {
        instance = this;
        this.saveDefaultConfig();
        super.onEnable();
        this.LE = new LunaEngine();

        this.loadFile("localization.yml");
        this.processListeners();
        LunaExecutor.initialize(this);

        this.registerLunaPlaceholder();
        Bukkit.getScheduler().runTask(this, VaultManager::initialize);
    }

    private void registerLunaPlaceholder() {
        this.createPlaceholder(((offlinePlayer, params) -> {
            if (params.equalsIgnoreCase("hooked")) {
                return String.join(", ", this.hookedPlugins.stream().map(LunaPlugin::getName).toList()); // Список луна плагинов
            }

            if (params.startsWith("register-")) { // %lunaspring_register-SateChat%
                String[] split = params.split("-");
                if (split.length == 1) return null;

                LunaPlugin plugin = this.getLunaPlugin(split[1]);
                return plugin == null ? "no" : "yes";
            }

            if (params.startsWith("localize-")) { // %lunaspring_localize-world%
                String[] split = params.split("-");

                String placeholder = split.length >= 2 ? Localization.localize(split[1]) : null;
                return placeholder == null || placeholder.isEmpty() ? (split.length == 1 ? null : split[1]) : placeholder;
            }

            if (params.startsWith("color-")) {
                String[] split = params.split("-");

                if (split.length >= 2) {
                    Color color = ColorManager.getColor(split[1]);
                    if (color == null) color = ColorManager.getColorFromReplacer(split[1]);

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

    @SneakyThrows
    public void hookPlugin(LunaPlugin lunaPlugin) {
        if (lunaPlugin != instance) {
            Class<?> pluginClass = lunaPlugin.getClass();
            if (pluginClass.isAnnotationPresent(PaidPlugin.class)) {
                if (this.LE.getConnection() == null || this.LE.getConnection().isClosed()) this.LE.connect();

                if (!this.LE.checkPlugin(lunaPlugin)) {
                    this.getPluginLoader().disablePlugin(lunaPlugin);

                    this.warning("{E}Виртуальный ключ для плагина не найден!");
                    this.warning("{S}Для работы с ним, необходимо получить ключ у администрации -> https://t.me/LunaEngineBot");
                    return;
                }
            }

            this.hookedPlugins.add(lunaPlugin);
        }
    }

    public LunaPlugin getLunaPlugin(String name) {
        return Utils.find(this.hookedPlugins, pl -> pl.getName().equals(name)).orElse(null);
    }
}



