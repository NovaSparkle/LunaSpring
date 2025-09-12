package org.novasparkle.lunaspring.API.util.utilities;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.novasparkle.lunaspring.API.menus.items.Item;
import org.novasparkle.lunaspring.API.util.exceptions.SerializerException;
import org.novasparkle.lunaspring.API.util.service.managers.ColorManager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@UtilityClass
public class Utils {

    /**
     * Покраска текста
     */
    public String color(String text) {
        if (text != null && !text.isEmpty()) return ChatColor.translateAlternateColorCodes('&', text);
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

    public Material getMaterial(@Nullable String string) {
        if (string == null || string.isEmpty()) return null;
        return Material.getMaterial(string);
    }

    public <E extends Enum<E>> E getEnumValue(@NotNull Class<E> clazz, @Nullable String string, E defaultValue) {
        if (string == null || string.isEmpty()) return defaultValue;
        try {
            return Enum.valueOf(clazz, string);
        } catch (IllegalArgumentException e) {
            return defaultValue;
        }
    }

    public <E extends Enum<E>> E getEnumValue(@NotNull Class<E> clazz, @Nullable String string) {
        return getEnumValue(clazz, string, null);
    }

    public <T> @NotNull Optional<T> find(Stream<T> collection, Predicate<T> filterPredicate) {
        return collection.filter(filterPredicate).findFirst();
    }

    public <T> @NotNull Optional<T> find(Collection<T> collection, Predicate<T> filterPredicate) {
        return Utils.find(collection.stream(), filterPredicate);
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
                    line = line.replace("[" + mass[0] + "]", mass[1]);
                    continue;
                }
            }

            line = line.replace("[" + index + "]", replacement);
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

    public String setBracketPlaceholders(OfflinePlayer player, String line) {
        if (line.contains("{") && line.contains("}")) {
            Matcher matcher = Pattern.compile("\\{([^}]+)}").matcher(line);

            StringBuilder builder = new StringBuilder();
            while (matcher.find()) {
                String replacement = setNakedPlaceholders(player, matcher.group(1));
                matcher.appendReplacement(builder, Matcher.quoteReplacement(replacement));
            }
            matcher.appendTail(builder);
        }

        return Utils.setPlaceholders(player, line);
    }

    public String setNakedPlaceholders(OfflinePlayer player, String line) {
        return setPlaceholders(player, "%" + line + "%");
    }

    public CompassDirection getCompassDirection(double angle) {
        angle = (angle + 360) % 360;

        if (angle >= 337.5 || angle < 22.5) return CompassDirection.EAST;
        if (angle >= 22.5 && angle < 67.5) return CompassDirection.NORTH_EAST;
        if (angle >= 67.5 && angle < 112.5) return CompassDirection.NORTH;
        if (angle >= 112.5 && angle < 157.5) return CompassDirection.NORTH_WEST;
        if (angle >= 157.5 && angle < 202.5) return CompassDirection.WEST;
        if (angle >= 202.5 && angle < 247.5) return CompassDirection.SOUTH_WEST;
        if (angle >= 247.5 && angle < 292.5) return CompassDirection.SOUTH;
        return CompassDirection.SOUTH_EAST;
    }

    public CompassDirection getCompassDirection(Location from, Location to) {
        double dx = to.getX() - from.getX();
        double dz = to.getZ() - from.getZ();

        double angle = Math.toDegrees(Math.atan2(-dz, dx));
        return getCompassDirection(angle);
    }

    @UtilityClass
    public static class Time {
        public String getFormattingTime(long millis, String datePattern) {
            LocalDateTime dateTime = Instant.ofEpochMilli(millis).atZone(ZoneId.of("Europe/Moscow")).toLocalDateTime();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern);

            return dateTime.format(formatter);
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

            if (nextTime.isBefore(nowTime)) {
                nextTime = nextTime.plusHours(24);
            }

            Duration duration = Duration.between(nowTime, nextTime);
            long totalMinutes = duration.toMinutes();

            long hours = totalMinutes / 60;
            long minutes = totalMinutes % 60;

            return String.format("%02d:%02d", hours, minutes);
        }
    }

    @UtilityClass
    public static class Luckperms {
        public String getHighestGroup(OfflinePlayer player) {
            return setPlaceholders(player, "%luckperms_highest_group_by_weight%");
        }

        public String getGroupTime(OfflinePlayer player) {
            String highestGroup = getHighestGroup(player);
            if (highestGroup == null || highestGroup.isEmpty() || highestGroup.contains("%luckperms_")) return "∞";

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

        @Getter @AllArgsConstructor
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
        }
    }

    @UtilityClass
    public static class Base64 {
        public <E> String serialize(E object) throws SerializerException {
            if (object == null) return null;

            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                 BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream)) {

                dataOutput.writeObject(object);
                return java.util.Base64.getEncoder().encodeToString(outputStream.toByteArray());
            } catch (IOException e) {
                throw new SerializerException(String.format("Объект %s невозможно сериализовать!", object.getClass().getSimpleName()));
            }
        }

        public <E> E deserialize(Class<E> dataClass, String data) throws SerializerException {
            if (data == null || data.isEmpty()) return null;

            try (ByteArrayInputStream inputStream = new ByteArrayInputStream(java.util.Base64.getDecoder().decode(data));
                 BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream)) {

                return dataClass.cast(dataInput.readObject());
            } catch (IOException | ClassNotFoundException | ClassCastException e) {
                throw new SerializerException(String.format("Строку невозможно десериализовать в %s!", dataClass.getSimpleName()));
            }
        }

        public String serializeItemStack(ItemStack itemStack) throws SerializerException {
            return serialize(itemStack);
        }

        public ItemStack deserializeItemStack(String data) throws SerializerException {
            return deserialize(ItemStack.class, data);
        }

        public <E> String serializeList(Collection<E> items) throws SerializerException {
            if (items == null) return null;
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                 BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream)) {

                dataOutput.writeInt(items.size());
                for (E item : items) {
                    dataOutput.writeObject(item);
                }
                return java.util.Base64.getEncoder().encodeToString(outputStream.toByteArray());

            } catch (IOException e) {
                throw new SerializerException("Данную коллекцию объектов невозможно сериализовать!");
            }
        }

        public <E> List<E> deserializeList(Class<E> dataClass, String data) throws SerializerException {
            try (ByteArrayInputStream inputStream = new ByteArrayInputStream(java.util.Base64.getDecoder().decode(data));
                 BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream)) {

                int size = dataInput.readInt();
                List<E> items = new ArrayList<>();
                for (int i = 0; i < size; i++) {
                    items.add(dataClass.cast(dataInput.readObject()));
                }

                return items;

            } catch (IOException | ClassNotFoundException | ClassCastException e) {
                throw new SerializerException(String.format("Строку невозможно десериализовать в список %s!", dataClass.getSimpleName()));
            }
        }
    }

    @UtilityClass
    public static class Items {
        public ItemStack[] getStorage(Inventory inventory) {
            return inventory.getContents();
        }

        public ItemStack[] getStorage(Collection<ItemStack> collection) {
            return collection.toArray(new ItemStack[0]);
        }

        @SuppressWarnings("all")
        public void remove(@NotNull ItemStack[] storage, Function<ItemStack, Boolean> checkFunction, final int amount) {
            int left = amount;
            for (ItemStack itemStack : storage) {
                if (left <= 0) break;

                if (itemStack == null) continue;
                if (checkFunction == null || checkFunction.apply(itemStack)) {
                    int different = left - itemStack.getAmount();
                    left -= different > 0 ? itemStack.getAmount() : left;

                    itemStack.setAmount(different >= 0 ? 0 : Math.abs(different));
                }
            }
        }

        public void remove(@NotNull ItemStack[] storage, @NotNull ItemStack similarItem, final int amount) {
            remove(storage, i -> i != null && i.isSimilar(similarItem), amount);
        }

        public void remove(@NotNull ItemStack[] storage, @NotNull Material material, final int amount) {
            remove(storage, i -> i != null && i.getType() == material, amount);
        }

        public Stream<ItemStack> get(@NotNull ItemStack[] storage, Predicate<ItemStack> predicate) {
            return Arrays.stream(storage).filter(predicate);
        }

        public Stream<ItemStack> get(@NotNull ItemStack[] storage, ItemStack similarItem) {
            return get(storage, i -> i != null && i.isSimilar(similarItem));
        }

        public Stream<ItemStack> get(@NotNull ItemStack[] storage, Material material) {
            return get(storage, i -> i != null && i.getType() == material);
        }

        public int getAmount(@NotNull ItemStack[] storage, Predicate<ItemStack> predicate) {
            return get(storage, predicate).mapToInt(ItemStack::getAmount).sum();
        }

        public int getAmount(@NotNull ItemStack[] storage, ItemStack similarItem) {
            return getAmount(storage, i -> i != null && i.isSimilar(similarItem));
        }

        public int getAmount(@NotNull ItemStack[] storage, Material material) {
            return getAmount(storage, i -> i != null && i.getType() == material);
        }

        public void give(@NotNull Player player, Location dropLocation, Collection<ItemStack> itemList, boolean putOnArmor) {
            PlayerInventory inventory = player.getInventory();
            for (ItemStack itemStack : itemList) {
                EquipmentSlot equipmentSlot = Utils.getEquipmentSlot(itemStack.getType());
                if (putOnArmor && equipmentSlot != EquipmentSlot.HAND) {
                    ItemStack armor = inventory.getItem(equipmentSlot);
                    if (armor == null || armor.getType().isAir()) {
                        inventory.setItem(equipmentSlot, itemStack.clone());
                        continue;
                    }
                }

                if (inventory.firstEmpty() > -1) {
                    inventory.addItem(itemStack.clone());
                    continue;
                }

                HashMap<Integer, ? extends ItemStack> finds = inventory.all(itemStack);
                int leftAmount = itemStack.getAmount();
                for (ItemStack value : finds.values()) {
                    int maxStack = value.getType().getMaxStackSize();
                    if (value.getAmount() >= maxStack) continue;

                    int different = maxStack - value.getAmount();
                    if (different >= leftAmount) {
                        value.setAmount(value.getAmount() + leftAmount);
                        leftAmount = 0;
                        break;
                    } else {
                        leftAmount -= different;
                        value.setAmount(maxStack);
                    }
                }

                if (leftAmount > 0) {
                    itemStack = itemStack.clone();
                    itemStack.setAmount(leftAmount);
                    player.getWorld().dropItem(dropLocation, itemStack);
                }
            }
        }

        public void give(@NotNull Player player, Collection<ItemStack> itemList, boolean putOnArmor) {
            give(player, player.getLocation(), itemList, putOnArmor);
        }

        public void give(@NotNull Player player, Collection<ItemStack> itemList) {
            give(player, itemList, true);
        }

        public void give(@NotNull Player player, Location dropLocation, boolean putOnArmor, ItemStack... itemStacks) {
            give(player, dropLocation, List.of(itemStacks), putOnArmor);
        }

        public void give(@NotNull Player player, boolean putOnArmor, ItemStack... itemStacks) {
            give(player, player.getLocation(), putOnArmor, itemStacks);
        }

        public void give(@NotNull Player player, ItemStack... itemStacks) {
            give(player, true, itemStacks);
        }
    }
}