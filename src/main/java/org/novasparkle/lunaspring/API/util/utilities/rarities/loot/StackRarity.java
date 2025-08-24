package org.novasparkle.lunaspring.API.util.utilities.rarities.loot;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class StackRarity extends AdvancedRarity<ItemStack> {
    public StackRarity(double chance, Map<ItemStack, Double> lootMap) {
        super(chance, lootMap);
    }

    public StackRarity(double chance) {
        super(chance);
    }

    public StackRarity(double chance, @Nullable ConfigurationSection section) {
        this(chance);

        if (section == null) return;
        for (String key : section.getKeys(false)) {
            ItemStack itemStack = Loot.getStackFromSection(section, key);
            this.add(itemStack, section.getDouble(key + ".chance", 100));
        }
    }

    public StackRarity(ConfigurationSection raritySection) {
        this(raritySection.getDouble("chance"), raritySection.getConfigurationSection("items"));
    }
}
