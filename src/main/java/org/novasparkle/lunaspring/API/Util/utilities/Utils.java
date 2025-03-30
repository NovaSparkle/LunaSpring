package org.novasparkle.lunaspring.API.Util.utilities;

import lombok.experimental.UtilityClass;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.novasparkle.lunaspring.API.Menus.Items.Item;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@UtilityClass
public class Utils {
    public String color(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public String getRKey(byte size) {
        return Utils.getRKey(size, true);
    }

    public String getRKey(byte size, boolean hasDuplicates) {
        String kit = "qwertyuiopasdfghjklzxcvbnm1234567890";
        return Utils.getRKey(size, kit, hasDuplicates);
    }

    public String getRKey(byte size, String kit, boolean hasDuplicates) {
        StringBuilder endValue = new StringBuilder();
        byte kitSize = (byte) kit.toCharArray().length;

        if (!hasDuplicates && size > kitSize) size = kitSize;
        for (byte i = 0; i < size;) {
            char c = kit.charAt(LunaMath.getRandom().nextInt(kitSize));
            if (!hasDuplicates && endValue.toString().contains(String.valueOf(c))) continue;

            endValue.append(c);
            i++;
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
    public LocalTime getNextTime(Collection<LocalTime> times) {
        return times.stream().filter(t -> t.isAfter(LocalTime.now())).findFirst().orElse(null);
    }
    private static String getTimeBetween(LocalTime nowTime, LocalTime nextTime) {
        long chrono = nowTime.until(nextTime, ChronoUnit.MINUTES);

        String string = String.format("%s:%s", (int) (chrono / 60), chrono % 60);
        if (chrono < 0) {
            int hours = (24 - nowTime.getHour()) + nextTime.getHour();
            int minutes = (60 - nowTime.getMinute()) + nextTime.getMinute();
            string = String.format("%s:%s", hours < 10 ? "0" + hours : hours,
                    minutes < 10 ? "0" + minutes : minutes);
        }
        return string;
    }
}