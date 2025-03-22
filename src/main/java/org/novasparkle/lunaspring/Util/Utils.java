package org.novasparkle.lunaspring.Util;

import lombok.experimental.UtilityClass;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.novasparkle.lunaspring.Menus.Items.Item;

import java.time.LocalTime;
import java.util.*;

@UtilityClass
public class Utils {
    public String color(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public String getRKey(byte size) {
        String kit = "qwertyuiopasdfghjklzxcvbnm1234567890";
        return Utils.getRKey(size, kit);
    }

    public String getRKey(byte size, String kit) {
        StringBuilder endValue = new StringBuilder();
        byte kitSize = (byte) kit.toCharArray().length;

        Random random = new Random();
        for (byte i = 0; i < size; i++) {
            endValue.append(kit.charAt(random.nextInt(kitSize)));
        }
        return endValue.toString();
    }

    public Location findRandomLocation(World world, int maxX, int maxZ) {
        if (world == null) return null;
        if (maxX == 0 || maxZ == 0) return findRandomLocation(world);

        int x = LunaMath.getRandom().nextInt(maxX * 2) - maxX;
        int z = LunaMath.getRandom().nextInt(maxZ * 2) - maxZ;
        int y = world.getHighestBlockYAt(x, z);

        return new Location(world, x, y, z);
    }

    public Location findRandomLocation(World world) {
        int border = (int) (world.getWorldBorder().getSize() / 2);
        if (border == 0) return null;

        return findRandomLocation(world, border, border);
    }

    public boolean hasPlugin(String name) {
        return Bukkit.getPluginManager().getPlugin(name) != null;
    }

    public LocalTime getNextTime(Collection<LocalTime> times) {
        LocalTime currentTime = LocalTime.now();
        for (LocalTime time : times) {
            if (time.isAfter(currentTime)) return time;
        }
        return null;
    }

    public static boolean isPluginEnabled(String name) {
        return Bukkit.getPluginManager().isPluginEnabled(name);
    }

    public Set<Item> getItems(ConfigurationSection example, Collection<String> slots) {
        Set<Item> list = new HashSet<>();
        if (!slots.isEmpty()) {
            slots.forEach(unsplitedSlots -> {
                String[] splitedSlots = unsplitedSlots.split("-");
                if (splitedSlots.length == 1) {
                    Item item = new Item(example, Byte.parseByte(splitedSlots[0]));
                    list.add(item);
                }
                else if (splitedSlots.length >= 2) {
                    for (byte slot = Byte.parseByte(splitedSlots[0]); slot <= Byte.parseByte(splitedSlots[1]); slot++) {
                        Item item = new Item(example, slot);
                        list.add(item);
                    }
                }
            });
        }
        return list;
    }
}