package org.novasparkle.lunaspring;

import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.novasparkle.lunaspring.API.commands.CommandInitializer;
import org.novasparkle.lunaspring.API.events.ItemComponentsHandler;
import org.novasparkle.lunaspring.API.events.MarkedItemEraserHandler;
import org.novasparkle.lunaspring.API.events.MenuHandler;
import org.novasparkle.lunaspring.API.events.WorldGuardHandler;
import org.novasparkle.lunaspring.API.util.service.managers.ColorManager;
import org.novasparkle.lunaspring.API.util.service.managers.TaskManager;
import org.novasparkle.lunaspring.API.util.service.managers.VaultManager;
import org.novasparkle.lunaspring.API.util.service.managers.worldguard.GuardManager;
import org.novasparkle.lunaspring.API.util.service.managers.worldguard.LunaFlags;
import org.novasparkle.lunaspring.API.util.utilities.AnnounceUtils;
import org.novasparkle.lunaspring.API.util.utilities.Color;
import org.novasparkle.lunaspring.API.util.utilities.Localization;
import org.novasparkle.lunaspring.API.util.utilities.Utils;
import org.novasparkle.lunaspring.API.util.utilities.reflection.AnnotationScanner;
import org.novasparkle.lunaspring.API.util.utilities.reflection.ClassEntry;
import org.novasparkle.lunaspring.self.messageActions.abs.MessageAction;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class LunaSpring extends LunaPlugin {
    @Getter private static LunaSpring instance;
    @Getter private final Set<LunaPlugin> hookedPlugins = new HashSet<>();

    @Override
    public void onLoad() {
        this.registerFlags();
    }

    @Override
    public void onEnable() {
        instance = this;
        this.saveDefaultConfig();
        super.onEnable();

        this.loadFile("localization.yml");
        this.registerListeners(new MenuHandler(), new MarkedItemEraserHandler(), new ItemComponentsHandler());
        CommandInitializer.initialize(this, "#.self.commands");

        this.registerLunaPlaceholder();
        this.registerDefaultMessageActions();

        if (Utils.isPluginEnabled("WorldGuard")) this.registerListeners(new WorldGuardHandler());
        Bukkit.getScheduler().runTask(this, VaultManager::initialize);
    }

    private void registerFlags() {
        if (!Utils.hasPlugin("WorldGuard")) return;

        LunaFlags flags = GuardManager.flags();
        if (flags != null) {
            for (LunaFlags.State value : LunaFlags.State.values()) {
                flags.register(value);
            }
        }
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
                if (split.length < 2) return null;

                String splittedPath = split[1];
                splittedPath = Utils.setBracketPlaceholders(offlinePlayer, splittedPath);

                String placeholder = Localization.localize(splittedPath);
                return placeholder == null || placeholder.isEmpty() ? splittedPath : placeholder;
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

                Utils.Luckperms.TranslateType translateType = Utils.Luckperms.TranslateType.valueOf(split[1].toUpperCase());
                Utils.Luckperms.FormatType formatType = Utils.Luckperms.FormatType.valueOf(split[2].toUpperCase());
                return Utils.Luckperms.getFormatting(Utils.Luckperms.getGroupTime(offlinePlayer), translateType, formatType);
            }

            return null;
        }));
    }

    @SneakyThrows
    private void registerDefaultMessageActions() {
        Set<ClassEntry<MessageAction>> set = AnnotationScanner.findAnnotatedClasses(this, MessageAction.class, "#.self.messageActions");
        for (ClassEntry<MessageAction> messageActionClassEntry : set) {
            AnnounceUtils.registerAction((AnnounceUtils.IMessageAction<? extends CommandSender>)
                    messageActionClassEntry.getClazz().getDeclaredConstructor().newInstance());
        }
    }

    public void hookPlugin(LunaPlugin lunaPlugin) {
        if (lunaPlugin != instance) this.hookedPlugins.add(lunaPlugin);
    }

    public LunaPlugin getLunaPlugin(String name) {
        return Utils.find(this.hookedPlugins, pl -> pl.getName().equals(name)).orElse(null);
    }

    @Override
    public void onDisable() {
        TaskManager.stopAll();
        super.onDisable();
    }
}



