package org.novasparkle.lunaspring.Util.managers;

import de.tr7zw.nbtapi.iface.ReadWriteItemNBT;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.novasparkle.lunaspring.LunaSpring;
import org.novasparkle.lunaspring.Util.Service.LunaService;
import org.novasparkle.lunaspring.Util.Service.NBTService;
import org.novasparkle.lunaspring.Util.Service.ServiceRegistrationException;

import java.util.UUID;
import java.util.function.Consumer;

public class NBTManager {
    private static NBTService nbtService;
    public static void init(LunaService service) {
        nbtService = (NBTService) service;
    }

    public static ItemStack base64head(OfflinePlayer player) {
        if (nbtService == null || (!LunaSpring.getProvider().isRegistered(nbtService.getClass()))) {
            throw new ServiceRegistrationException(ColorManager.class);
        }
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.setOwningPlayer(player);
        head.setItemMeta(meta);
        return head;

    }
    public static void base64head(ItemStack head, String value, UUID uuid) {
        if (nbtService == null || (!LunaSpring.getProvider().isRegistered(nbtService.getClass()))) {
            throw new ServiceRegistrationException(ColorManager.class);
        }
        nbtService.base64head(head, value, uuid);
    }

    public static void base64head(ItemStack head, String value) {
        NBTManager.base64head(head, value, UUID.randomUUID());
    }

    public static boolean hasTag(ItemStack item, String tag) {
        if (nbtService == null || (!LunaSpring.getProvider().isRegistered(nbtService.getClass()))) {
            throw new ServiceRegistrationException(ColorManager.class);
        }
        return nbtService.hasTag(item, tag);
    }

    public static void set(ItemStack item, Consumer<ReadWriteItemNBT> consumer) {
        if (nbtService == null || (!LunaSpring.getProvider().isRegistered(nbtService.getClass()))) {
            throw new ServiceRegistrationException(ColorManager.class);
        }
        nbtService.set(item, consumer);
    }

    public static void setString(ItemStack item, String tag, String value) {
        NBTManager.set(item, nbt -> nbt.setString(tag, value));
    }

    public static void setInt(ItemStack item, String tag, int value) {
        NBTManager.set(item, nbt -> nbt.setInteger(tag, value));
    }

    public static void setByte(ItemStack item, String tag, byte value) {
        NBTManager.set(item, nbt -> nbt.setByte(tag, value));
    }

    public static void setLong(ItemStack item, String tag, long value) {
        NBTManager.set(item, nbt -> nbt.setLong(tag, value));
    }

    public static void setDouble(ItemStack item, String tag, double value) {
        NBTManager.set(item, nbt -> nbt.setDouble(tag, value));
    }

    public static void setBool(ItemStack item, String tag, boolean value) {
        NBTManager.set(item, nbt -> nbt.setBoolean(tag, value));
    }

    public static void setUUID(ItemStack item, String tag, UUID value) {
        NBTManager.set(item, nbt -> nbt.setUUID(tag, value));
    }

    public static void setItem(ItemStack item, String tag, ItemStack value) {
        NBTManager.set(item, nbt -> nbt.setItemStack(tag, value));
    }

    public static Object get(ItemStack item, String tag) {
        return nbtService.get(item, tag);
    }
}
