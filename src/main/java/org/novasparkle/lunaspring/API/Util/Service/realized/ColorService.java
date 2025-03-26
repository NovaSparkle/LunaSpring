package org.novasparkle.lunaspring.API.Util.Service.realized;

import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.novasparkle.lunaspring.API.Util.utilities.Color;
import org.novasparkle.lunaspring.API.Util.Service.LunaService;
import org.novasparkle.lunaspring.API.Util.utilities.Utils;
import org.novasparkle.lunaspring.LunaPlugin;
import org.novasparkle.lunaspring.LunaSpring;

import java.util.ArrayList;
import java.util.List;

public final class ColorService implements LunaService {
    @Getter
    private final LunaPlugin providingPlugin;
    private final List<Color> colorList = new ArrayList<>();
    public ColorService(FileConfiguration configuration, LunaPlugin plugin) {
        this.providingPlugin = plugin;
        ConfigurationSection section = configuration.getConfigurationSection("colors");
        if (section == null) throw new RuntimeException("Секция с цветами не найдена, нужная секция: colors");
        for (String key : section.getKeys(false)) {
            ConfigurationSection colorSection = section.getConfigurationSection(key);
            assert colorSection != null;
            this.colorList.add(new Color(colorSection.getString("abbr"), colorSection.getString("variable")));
        }
    }
    public String color(String text) {
        for (Color color : this.colorList) {
            text = text.replaceAll(color.getAbbr(), color.getVariable());
        }
        return Utils.color(text);
    }
    public Color getColor(String abbr) {
        return this.colorList.stream().filter(c -> c.getAbbr().equals(abbr)).findFirst().orElseThrow();
    }
    @Override
    public String toString() {
        return "ColorService{" +
                "colorList=" + colorList +
                '}';
    }
}