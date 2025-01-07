package org.novasparkle.lunaspring.Util.Service;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.novasparkle.lunaspring.Util.Color;

import java.util.ArrayList;
import java.util.List;

public class ColorService implements LunaService {
    protected final List<Color> colorList = new ArrayList<>();
    public ColorService(FileConfiguration configuration) {
        ConfigurationSection section = configuration.getConfigurationSection("colors");
        if (section == null) throw new RuntimeException("Секция с цветами не найдена, нужная секция: colors");
        for (String key : section.getKeys(false)) {
            ConfigurationSection colorSection = section.getConfigurationSection(key);
            assert colorSection != null;
            colorList.add(new Color(colorSection.getString("abbr"), colorSection.getString("variable")));
        }
    }
    public String color(String text) {
        return this.colorList.stream().map(e -> text.replaceAll(e.getAbbr(), e.getVariable())).findFirst().orElseThrow();
    }
    public Color getColor(String abbr) {
        return this.colorList.stream().filter(c -> c.getAbbr().equals(abbr)).findFirst().orElseThrow();
    }
}
