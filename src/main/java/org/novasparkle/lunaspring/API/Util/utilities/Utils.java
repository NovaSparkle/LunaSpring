package org.novasparkle.lunaspring.API.Util.utilities;

import lombok.experimental.UtilityClass;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.novasparkle.lunaspring.API.Menus.Items.Item;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@UtilityClass
public class Utils {

    /**
     * Покраска текста
     */
    public String color(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
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
        return times.stream().filter(t -> t.isAfter(LocalTime.now())).findFirst().orElse(null);
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
     * @return Set<Integer> set
     */
    public Set<Integer> getSlotList(Collection<String> slotList) {
        Set<Integer> set = new HashSet<>();
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
        return Bukkit.getOnlinePlayers()
                .stream()
                .map(Player::getName)
                .filter(n -> n.toUpperCase().startsWith(tabCompleterValueFilter.toUpperCase()))
                .toList();
    }
}