package org.novasparkle.lunaspring.API.util.utilities;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.novasparkle.lunaspring.API.menus.items.Item;

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

    @UtilityClass
    public static class Time {
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
    }

    @UtilityClass
    public static class Luckperms {
        public String getHighestGroup(OfflinePlayer player) {
            return setPlaceholders(player, "%luckperms_highest_group_by_weight%");
        }

        public String getGroupTime(OfflinePlayer player) {
            String highestGroup = getHighestGroup(player);
            if (highestGroup == null || highestGroup.isEmpty()) return "∞";

            String timer = Utils.setPlaceholders(player, "%luckperms_group_expiry_time_" + highestGroup + "%");
            return timer == null || timer.isEmpty() ? "∞" : timer;
        }

        public String getFormatting(String text, TranslateType translateType, FormatType formatType) {
            if (text.length() == 1) return text;
            if (translateType == null) translateType = TranslateType.NONE;

            if (translateType != TranslateType.NONE) text = text
                        .replace("y", translateType.y)
                        .replace("mo", translateType.mo)
                        .replace("w", translateType.w)
                        .replace("d", translateType.d)
                        .replace("h", translateType.h)
                        .replace("m",translateType.m)
                        .replace("s", translateType.s);
            if (formatType == null || formatType == FormatType.NONE) return text;

            String[] parts = text.split(" ");
            long seconds = 0;
            for (String p : parts) {
                String part = p.replaceAll("\\D", "").replace(".", "");
                p = p.replaceAll("\\d", "").replace(".", "");

                long form = LunaMath.toInt(part);
                if (p.startsWith("с") || p.equalsIgnoreCase("s")) {
                    seconds += form;
                }
                else if ((p.startsWith("м") && !p.contains("с")) || p.startsWith("мин") || p.equalsIgnoreCase("m")) {
                    seconds += form * 60;
                }
                else if (p.startsWith("ч") || p.equalsIgnoreCase("h")) {
                    seconds += form * 60 * 60;
                }
                else if (p.startsWith("д") || p.equalsIgnoreCase("d")) {
                    seconds += form * 60 * 60 * 24;
                }
                else if (p.startsWith("н") || p.equalsIgnoreCase("w")) {
                    seconds += form * 60 * 60 * 24 * 7;
                }
                else if (p.startsWith("мес") || p.equalsIgnoreCase("mo")) {
                    seconds += form * 60 * 60 * 24 * 30;
                }
                else if (p.startsWith("г") || p.equalsIgnoreCase("y")) {
                    seconds += form * 60 * 60 * 24 * 30 * 12;
                }
            }

            switch (formatType) {
                case ONLY_DAYS -> {
                    int days = (int) (seconds / (60 * 60 * 24));
                    return (days > 0 ? days : "<1") + translateType.d;
                }
                case DAYS_OR_HOURS -> {
                    int days = (int) (seconds / (60 * 60 * 24));
                    int hours = (int) (seconds / 3600);
                    return days > 0 ? days + translateType.d : ((hours > 0 ? hours : "<1") + translateType.h);
                }
                case DAYS_WITH_HOURS -> {
                    int days = (int) (seconds / (60 * 60 * 24));
                    int hours = (int) ((seconds % (60 * 60 * 24)) / 3600);
                    return days > 0 ? days + translateType.d + (hours > 0 ? " " + hours + translateType.h : "") :
                            ((hours > 0 ? hours : "<1") + translateType.h);
                }
                case MAXIMAL_SC -> {
                    int years = (int) (seconds / (3600 * 24 * 30 * 12));
                    if (years > 0) return years + translateType.y;

                    int month = (int) (seconds / (3600 * 24 * 30));
                    if (month > 0) return month + translateType.mo;

                    int weeks = (int) (seconds / (3600 * 24 * 7));
                    if (weeks > 0) return weeks + translateType.w;

                    int days = (int) (seconds / (3600 * 24));
                    if (days > 0) return days + translateType.d;

                    int hours = (int) (seconds / 3600);
                    if (hours > 0) return hours + translateType.h;

                    int minutes = (int) (seconds / 60);
                    if (minutes > 0) return minutes + translateType.m;

                    return seconds + translateType.s;
                }
                case MAXIMAL_SC_DAYS -> {
                    int days = (int) (seconds / (3600 * 24));
                    if (days > 0) return days + translateType.d;

                    int hours = (int) (seconds / 3600);
                    if (hours > 0) return hours + translateType.h;

                    int minutes = (int) (seconds / 60);
                    if (minutes > 0) return minutes + translateType.m;

                    return seconds + translateType.s;
                }
            }
            return text;
        }

        public enum FormatType {
            NONE,
            ONLY_DAYS,
            DAYS_OR_HOURS,
            DAYS_WITH_HOURS,
            MAXIMAL_SC,
            MAXIMAL_SC_DAYS;
        }

        @Getter
        public enum TranslateType {
            NONE("y", "mo", "w", "d", "h", "m", "s"),
            ONLY_TRANSLATE("г", "мес", "нед", "д", "ч", "мин", "сек"),
            SHORT_TRANSLATE("г", "мес", "н", "д", "ч", "м", "с"),
            TRANSLATE_WITH_POINTS("г.", "мес.", "нед.", "д.", "ч.", "мин.", "сек."),
            SHORT_TRANSLATE_WITH_POINTS("г.", "мес.", "н.", "д.", "ч.", "м.", "с.");

            private final String y;
            private final String mo;
            private final String w;
            private final String d;
            private final String h;
            private final String m;
            private final String s;
            TranslateType(String y, String mo, String w, String d, String h, String m, String s) {
                this.y = y;
                this.mo = mo;
                this.w = w;
                this.d = d;
                this.h = h;
                this.m = m;
                this.s = s;
            }
        }
    }
}