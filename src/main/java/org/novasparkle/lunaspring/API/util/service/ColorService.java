package org.novasparkle.lunaspring.API.util.service;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.novasparkle.lunaspring.API.util.utilities.Color;
import org.novasparkle.lunaspring.API.util.utilities.Utils;

import java.util.ArrayList;
import java.util.List;

public final class ColorService implements LunaService {
    private final List<Color> colorList = new ArrayList<>();
    public ColorService(FileConfiguration configuration) {
        ConfigurationSection section = configuration.getConfigurationSection("colors");
        if (section == null) throw new RuntimeException("Секция с цветами не найдена, нужная секция: colors");
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

    public String colorHex(String text) {
        if (text == null || text.isEmpty()) return "";
        for (Color color : this.colorList) {
            text = text.replaceAll(color.abbr(), color.toHex());
        }
        return text;
    }

    public Color getColor(String abbr) {
        return this.colorList.stream().filter(c -> c.abbr().equals(abbr)).findFirst().orElseThrow();
    }

    public boolean addColor(Color color) {
        if (this.getColor(color.getAbbr()) == null) return this.colorList.add(color);
        return false;
    }

    @Override
    public String toString() {
        return "ColorService{" +
                "colorList=" + colorList +
                '}';
    }
}