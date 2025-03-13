package org.novasparkle.lunaspring.Util.managers;

import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.iface.ReadWriteItemNBT;
import lombok.experimental.UtilityClass;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.novasparkle.lunaspring.LunaSpring;
import org.novasparkle.lunaspring.Util.Service.LunaService;
import org.novasparkle.lunaspring.Util.Service.realized.NBTService;
import org.novasparkle.lunaspring.Util.Service.ServiceRegistrationException;

import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

@UtilityClass
public class NBTManager {
    private NBTService nbtService;
    public void init(LunaService service) {
        nbtService = (NBTService) service;
    }

    public ItemStack base64head(ItemStack head, OfflinePlayer player) {
        exceptionCheck();
        return nbtService.base64head(head, player);
    }

    public void base64head(ItemStack head, String value, UUID uuid) {
        exceptionCheck();
        nbtService.base64head(head, value, uuid);
    }

    public void base64head(ItemStack head, String value) {
        NBTManager.base64head(head, value, UUID.randomUUID());
    }

    public boolean hasTag(ItemStack item, String tag) {
        exceptionCheck();
        return nbtService.hasTag(item, tag);
    }

    public void set(ItemStack item, Consumer<ReadWriteItemNBT> consumer) {
        exceptionCheck();
        nbtService.set(item, consumer);
    }

    public void setString(ItemStack item, String tag, String value) {
        exceptionCheck();
        NBTManager.set(item, nbt -> nbt.setString(tag, value));
    }

    public void setInt(ItemStack item, String tag, int value) {
        exceptionCheck();
        NBTManager.set(item, nbt -> nbt.setInteger(tag, value));
    }

    public void setByte(ItemStack item, String tag, byte value) {
        exceptionCheck();
        NBTManager.set(item, nbt -> nbt.setByte(tag, value));
    }

    public void setLong(ItemStack item, String tag, long value) {
        exceptionCheck();
        NBTManager.set(item, nbt -> nbt.setLong(tag, value));
    }

    public void setDouble(ItemStack item, String tag, double value) {
        exceptionCheck();
        NBTManager.set(item, nbt -> nbt.setDouble(tag, value));
    }

    public void setBool(ItemStack item, String tag, boolean value) {
        exceptionCheck();
        NBTManager.set(item, nbt -> nbt.setBoolean(tag, value));
    }

    public void setUUID(ItemStack item, String tag, UUID value) {
        exceptionCheck();
        NBTManager.set(item, nbt -> nbt.setUUID(tag, value));
    }

    public void setItem(ItemStack item, String tag, ItemStack value) {
        exceptionCheck();
        NBTManager.set(item, nbt -> nbt.setItemStack(tag, value));
    }

    public String getString(ItemStack item, String tag) {
        exceptionCheck();
        return NBT.get(item, nbt -> (String) nbt.getString(tag));
    }

    public int getInt(ItemStack item, String tag) {
        exceptionCheck();
        return NBT.get(item, nbt -> (int) nbt.getInteger(tag));
    }

    public double getDouble(ItemStack item, String tag) {
        exceptionCheck();
        return NBT.get(item, nbt -> (double) nbt.getDouble(tag));
    }

    public byte getByte(ItemStack item, String tag) {
        exceptionCheck();
        return NBT.get(item, nbt -> (byte) nbt.getByte(tag));
    }

    public ItemStack getItemStack(ItemStack item, String tag) {
        exceptionCheck();
        return NBT.get(item, nbt -> (ItemStack) nbt.getItemStack(tag));
    }

    public long getLong(ItemStack item, String tag) {
        exceptionCheck();
        return NBT.get(item, nbt -> (long) nbt.getLong(tag));
    }

    public boolean getBoolean(ItemStack item, String tag) {
        exceptionCheck();
        return NBT.get(item, nbt -> (boolean) nbt.getBoolean(tag));
    }

    public ItemStack[] getItemStacks(ItemStack item, String tag) {
        exceptionCheck();
        return NBT.get(item, nbt -> (ItemStack[]) nbt.getItemStackArray(tag));
    }

    public float getFloat(ItemStack item, String tag) {
        exceptionCheck();
        return NBT.get(item, nbt -> (float) nbt.getFloat(tag));
    }

    public Set<String> getKeys(ItemStack item) {
        exceptionCheck();
        return NBT.get(item, nbt -> (Set<String>) nbt.getKeys());
    }

    public static UUID getUUID(ItemStack item, String tag) {
        exceptionCheck();
        return NBT.get(item, nbt -> (UUID) nbt.getUUID(tag));
    }

    private static void exceptionCheck() {
        if (nbtService == null || (!LunaSpring.getServiceProvider().isRegistered(nbtService.getClass())))
            throw new ServiceRegistrationException(NBTManager.class);
    }
}
