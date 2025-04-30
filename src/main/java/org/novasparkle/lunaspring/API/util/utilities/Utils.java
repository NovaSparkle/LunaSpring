package org.novasparkle.lunaspring.API.util.utilities;

import com.google.common.collect.Lists;
import lombok.experimental.UtilityClass;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.novasparkle.lunaspring.API.menus.items.Item;
import org.novasparkle.lunaspring.API.util.service.managers.ColorManager;

import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@UtilityClass
public class Utils {

    /**
     * Покраска текста
     */
    public String color(String text) {
        if (text != null && !text.isEmpty())
            return ChatColor.translateAlternateColorCodes('&', text);
        return null;
    }

    /**
     * Получить случайную последовательность.
     * <p>
     * @param size Длина строки
     * @param hasDuplicates Дублирование символов
     * @param kit Список символов
     */
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
    public String getRKey(byte size) {
        return Utils.getRKey(size, true);
    }

    public String getRKey(byte size, boolean hasDuplicates) {
        String kit = "qwertyuiopasdfghjklzxcvbnm1234567890";
        return Utils.getRKey(size, kit, hasDuplicates);
    }

    /**
     * Получить случайную локацию.
     * @param world Мир
     * @param maxX Ограничение по оси X
     * @param maxZ Ограничение по оси Z
     */
    public Location findRandomLocation(World world, int maxX, int maxZ) {
        if (world == null) return null;
        if (maxX == 0 || maxZ == 0) return findRandomLocation(world);

        int x = LunaMath.getRandom().nextInt(maxX * 2) - maxX;
        int z = LunaMath.getRandom().nextInt(maxZ * 2) - maxZ;
        int y = world.getHighestBlockYAt(x, z);

        return new Location(world, x, y, z);
    }
    /**
     * Получить случайную локацию.
     * Ограницениями выступают границы указанного мира.
     */
    public Location findRandomLocation(World world) {
        int border = (int) (world.getWorldBorder().getSize() / 2);
        if (border == 0) return null;

        return findRandomLocation(world, border, border);
    }

    /**
     * Проверка на наличие плагина.
     */
    public boolean hasPlugin(String name) {
        return Bukkit.getPluginManager().getPlugin(name) != null;
    }

    /**
     * Проверка, запущен ли плагин.
     */
    public boolean isPluginEnabled(String name) {
        return Bukkit.getPluginManager().isPluginEnabled(name);
    }

    /**
     * Получение списка предметов, созданных по одному шаблону section во всех переданных слотах.
     * @param slots Список строк, перечисляющих слоты.
     *               slots:
     *                  - '10'
     *                  - '11'
     *                  - '15'
     *                  - '19'
     *                  - '24-32'
     */
    public Set<Item> getItems(ConfigurationSection section, Collection<String> slots) {
        return getSlotList(slots).stream().map(s -> new Item(section, s)).collect(Collectors.toSet());
    }

    /**
     * Получить следующую дату от текущей в коллекции.
     */
    public LocalTime getNextTime(Collection<LocalTime> times) {
        LocalTime now = LocalTime.now();
        return times.stream()
                .filter(time -> time.isAfter(now))
                .min(LocalTime::compareTo)
                .orElseGet(() -> times.stream()
                        .min(LocalTime::compareTo)
                        .orElse(null));
    }

    public LocalTime getNextTime(List<String> times) {
        return getNextTime(times.stream().map(LocalTime::parse).collect(Collectors.toSet()));
    }

    /**
     * Получить время оставшееся до указанного времени
     */
    public LocalTime getTimeBetween(LocalTime now, LocalTime targetTime) {
        int startInSeconds = now.toSecondOfDay();
        int endInSeconds = targetTime.toSecondOfDay();

        int differenceInSeconds;
        if (endInSeconds < startInSeconds) {
            differenceInSeconds = (24 * 60 * 60 - startInSeconds) + endInSeconds;
        } else {
            differenceInSeconds = endInSeconds - startInSeconds;
        }

        return parseTime(differenceInSeconds);
    }

    public LocalTime parseTime(long totalSeconds) {
        long minutes = (totalSeconds % 3600) / 60;
        long hours = totalSeconds / 3600;
        long seconds = Math.max(totalSeconds - (minutes * 60 + hours * 3600), 0);
        return LocalTime.of((int) hours, (int) minutes, (int) seconds);
    }

    public String timeToString(LocalTime localTime) {
        int hours = localTime.getHour();
        int minutes = localTime.getMinute();
        int seconds = localTime.getSecond();
        return String.format("%s:%s:%s",
                hours < 10 ? "0" + hours : hours,
                minutes < 10 ? "0" + minutes : minutes,
                seconds < 10 ? "0" + seconds : seconds);
    }

    /**
     * Получить время оставшееся до указанного времени в формате hh:mm.
     */
    public String getTimeBetween(LocalTime nextTime) {
        LocalTime nowTime = LocalTime.now();
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

    /**
     * Преобразование списка слотов формата ["1", "2-6", "7, 8, 10", "9"] в Set чисел формата [1, 2, 3, 4, 5, 6, ...]
     * @param slotList - начальный список слотов первого формата
     * @return List<Integer> list
     */
    public List<Integer> getSlotList(Collection<String> slotList) {
        List<Integer> set = Lists.newArrayList();
        for (String line : slotList) {
            if (line.contains("-")) {
                String[] split = line.split("-");
                for (int i = LunaMath.toInt(split[0]); i <= LunaMath.toInt(split[1]); i++) set.add(i);
            } else if (line.contains(",")) {
                String[] split = line.split(",");
                for (String string : split) set.add(LunaMath.toInt(string.replace(" ", "")));
            } else set.add(LunaMath.toInt(line));
        }
        return set;
    }

    /**
     * Возвращает список ников всех игроков на сервере, которые начинаются со значения фильтра
     * Пример:
     *      - Список игроков: [ProGiple, NovaSparkle, Siozy, Nova, NovaBot1]
     *      - Значение фильтра: "Nov"
     *      - Вернёт: ["NovaSparkle", "Nova", "NovaBot1"]
     *
     * @param tabCompleterValueFilter - значение фильтра
     * @return List<String> list
     */
    public List<String> getPlayerNicks(String tabCompleterValueFilter) {
        return tabCompleterFiltering(Bukkit.getOnlinePlayers()
                .stream()
                .map(Player::getName).toList(),
                tabCompleterValueFilter);
    }

    public List<String> tabCompleterFiltering(Collection<String> collection, String tabCompleterValueFilter) {
        return collection
                .stream()
                .filter(n -> n.toUpperCase().startsWith(tabCompleterValueFilter.toUpperCase()))
                .toList();
    }


    public void playersAction(Consumer<Player> playerConsumer) {
        Bukkit.getOnlinePlayers().forEach(playerConsumer);
    }

    public String applyReplacements(String starterLine, String... replacements) {
        byte index = 0;

        String line = starterLine;
        for (String replacement : replacements) {
            if (replacement.contains("-%-")) {
                String[] mass = replacement.split("-%-");
                if (mass.length >= 2) {
                    line = line.replace("{" + mass[0] + "}", mass[1]).replace("[" + mass[0] + "]", mass[1]);
                    continue;
                }
            }

            line = line.replace("{" + index + "}", replacement).replace("[" + index + "]", replacement);
            index++;
        }
        return line;
    }

    public EquipmentSlot getEquipmentSlot(Material material) {
        String name = material.name();
        if (name.endsWith("_HELMET")) return EquipmentSlot.HEAD;
        if (name.endsWith("_CHESTPLATE")) return EquipmentSlot.CHEST;
        if (name.endsWith("_LEGGINGS")) return EquipmentSlot.LEGS;
        if (name.endsWith("_BOOTS")) return EquipmentSlot.FEET;

        return EquipmentSlot.HAND;
    }

    public String setPlaceholders(OfflinePlayer offlinePlayer, String line) {
        return isPluginEnabled("PlaceholderAPI") ? PlaceholderAPI.setPlaceholders(offlinePlayer, line) : line;
    }
}