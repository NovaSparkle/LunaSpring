package org.novasparkle.lunaspring.API.util.utilities.localization;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;
import org.novasparkle.lunaspring.API.util.utilities.Utils;
import org.novasparkle.lunaspring.LunaSpring;

import java.io.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Supplier;

@Getter
public class LunaLang implements ILang {
    private final String lang;
    private final Map<String, String> translations = new ConcurrentHashMap<>();
    private Map<String, String> reversedTranslations;
    public LunaLang(String lang) {
        this.lang = lang;

        File file = new File(LunaSpring.getInstance().getDataFolder(), "localizeLangs/" + lang + ".json");
        if (file.exists()) {
            this.load(file);
        }
        else {
            String version = Bukkit.getMinecraftVersion();
            this.download(version)
                    .thenAccept(this::load)
                    .exceptionally(e -> {
                        Utils.debug("[LunaSpring Lang " + lang + "]",
                                "Скачивание версии языка " + version + " завершено ошибкой! ",
                                "Будет установлена версия: " + DEFAULT_LANG_VERSION);
                        download(DEFAULT_LANG_VERSION).thenAccept(this::load);
                        return null;
                    });
        }
    }

    @Override
    public void load(File file) {
        ILang.super.load(file);
        this.reversedTranslations = Utils.reverseMap(this.translations, ConcurrentHashMap::new);
    }

    public String translate(String path, Supplier<String> orElse) {
        var s = this.translations.get(path);
        return s == null ? orElse == null ? null : orElse.get() : s;
    }

    public String getPath(String key) {
        return key == null ? null : this.reversedTranslations.get(key);
    }
}
