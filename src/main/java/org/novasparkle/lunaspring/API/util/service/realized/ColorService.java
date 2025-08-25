package org.novasparkle.lunaspring.API.util.service.realized;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.novasparkle.lunaspring.API.configuration.IConfig;
import org.novasparkle.lunaspring.API.util.service.LunaService;
import org.novasparkle.lunaspring.API.util.utilities.Color;
import org.novasparkle.lunaspring.API.util.utilities.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ColorService implements LunaService {
    private final List<Color> colorList = new ArrayList<>();
    private final IConfig config;
    public ColorService(IConfig configuration) {
        this.config = configuration;
        this.reload();
    }

    public void reload() {
        ConfigurationSection section = this.config.getSection("colors");
        if (section == null) throw new RuntimeException("Секция с цветами не найдена, нужная секция: colors");

        this.colorList.clear();
        for (String key : section.getKeys(false)) {
            ConfigurationSection colorSection = section.getConfigurationSection(key);
            assert colorSection != null;
            this.colorList.add(new Color(colorSection.getString("abbr"), colorSection.getString("variable")));
        }
    }

    public String color(String text) {
        if (text == null || text.isEmpty()) return "";
        for (Color color : this.colorList) {
            text = text.replaceAll(color.abbr(), color.variable());
        }
        return Utils.color(text);
    }

    public Color getColor(String abbr) {
        return Utils.find(this.colorList, c -> c.abbr().equals(abbr)).orElse(null);
    }

    public Color getColorFromReplacer(String replacerAbbr) {
        return Utils.find(this.colorList, c -> c.getAbbr().replace("\\", "").equals(replacerAbbr)).orElse(null);
    }

    public boolean addColor(Color color) {
        if (this.getColor(color.getAbbr()) == null) return this.colorList.add(color);
        return false;
    }

    public String parseHexColors(String text) {
        Matcher matcher = Pattern.compile("(&x(&[0-9a-fA-F]){6})").matcher(text);
        StringBuilder buffer = new StringBuilder();

        while (matcher.find()) {
            String hexCode = matcher.group(1)
                    .replace("&x", "")
                    .replace("&", "");
            matcher.appendReplacement(buffer, ChatColor.of("#" + hexCode).toString());
        }
        matcher.appendTail(buffer);

        return buffer.toString();
    }
}