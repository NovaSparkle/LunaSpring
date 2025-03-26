package org.novasparkle.lunaspring.API.Util.managers;

import de.tr7zw.nbtapi.iface.ReadWriteItemNBT;
import lombok.experimental.UtilityClass;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.novasparkle.lunaspring.LunaSpring;
import org.novasparkle.lunaspring.API.Util.Service.LunaService;
import org.novasparkle.lunaspring.API.Util.Service.realized.NBTService;
import org.novasparkle.lunaspring.API.Util.Service.ServiceRegistrationException;

import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

@UtilityClass
public class NBTManager {
    private NBTService nbtService;
    public void init(NBTService service) {
        nbtService = service;
        nbtService.register();
    }

    public ItemStack base64head(ItemStack head, OfflinePlayer player) {
        nbtService.exceptionCheck(nbtService, NBTManager.class);
        return nbtService.base64head(head, player);
    }

    public void base64head(ItemStack head, String value, UUID uuid) {
        nbtService.exceptionCheck(nbtService, NBTManager.class);
        nbtService.base64head(head, value, uuid);
    }

    public void base64head(ItemStack head, String value) {
        NBTManager.base64head(head, value, UUID.randomUUID());
    }

    public boolean hasTag(ItemStack item, String tag) {
        nbtService.exceptionCheck(nbtService, NBTManager.class);
        return nbtService.hasTag(item, tag);
    }

    public void set(ItemStack item, Consumer<ReadWriteItemNBT> consumer) {
        nbtService.exceptionCheck(nbtService, NBTManager.class);
        nbtService.set(item, consumer);
    }

    public void setString(ItemStack item, String tag, String value) {
        nbtService.exceptionCheck(nbtService, NBTManager.class);
        NBTManager.set(item, nbt -> nbt.setString(tag, value));
    }

    public void setInt(ItemStack item, String tag, int value) {
        nbtService.exceptionCheck(nbtService, NBTManager.class);
        NBTManager.set(item, nbt -> nbt.setInteger(tag, value));
    }

    public void setByte(ItemStack item, String tag, byte value) {
        nbtService.exceptionCheck(nbtService, NBTManager.class);
        NBTManager.set(item, nbt -> nbt.setByte(tag, value));
    }

    public void setLong(ItemStack item, String tag, long value) {
        nbtService.exceptionCheck(nbtService, NBTManager.class);
        NBTManager.set(item, nbt -> nbt.setLong(tag, value));
    }

    public void setDouble(ItemStack item, String tag, double value) {
        nbtService.exceptionCheck(nbtService, NBTManager.class);
        NBTManager.set(item, nbt -> nbt.setDouble(tag, value));
    }

    public void setBool(ItemStack item, String tag, boolean value) {
        nbtService.exceptionCheck(nbtService, NBTManager.class);
        NBTManager.set(item, nbt -> nbt.setBoolean(tag, value));
    }

    public void setUUID(ItemStack item, String tag, UUID value) {
        nbtService.exceptionCheck(nbtService, NBTManager.class);
        NBTManager.set(item, nbt -> nbt.setUUID(tag, value));
    }

    public void setItem(ItemStack item, String tag, ItemStack value) {
        nbtService.exceptionCheck(nbtService, NBTManager.class);
        NBTManager.set(item, nbt -> nbt.setItemStack(tag, value));
    }

    public String getString(ItemStack item, String tag) {
        nbtService.exceptionCheck(nbtService, NBTManager.class);
        return nbtService.getString(item, tag);
    }

    public int getInt(ItemStack item, String tag) {
        nbtService.exceptionCheck(nbtService, NBTManager.class);
        return nbtService.getInt(item, tag);
    }

    public double getDouble(ItemStack item, String tag) {
        nbtService.exceptionCheck(nbtService, NBTManager.class);
        return nbtService.getDouble(item, tag);
    }

    public byte getByte(ItemStack item, String tag) {
        nbtService.exceptionCheck(nbtService, NBTManager.class);
        return nbtService.getByte(item, tag);
    }

    public ItemStack getItemStack(ItemStack item, String tag) {
        nbtService.exceptionCheck(nbtService, NBTManager.class);
        return nbtService.getItemStack(item, tag);
    }

    public long getLong(ItemStack item, String tag) {
        nbtService.exceptionCheck(nbtService, NBTManager.class);
        return nbtService.getLong(item, tag);
    }

    public boolean getBoolean(ItemStack item, String tag) {
        nbtService.exceptionCheck(nbtService, NBTManager.class);
        return nbtService.getBoolean(item, tag);
    }

    public ItemStack[] getItemStacks(ItemStack item, String tag) {
        nbtService.exceptionCheck(nbtService, NBTManager.class);
        return nbtService.getItemStacks(item, tag);
    }

    public float getFloat(ItemStack item, String tag) {
        nbtService.exceptionCheck(nbtService, NBTManager.class);
        return nbtService.getFloat(item, tag);
    }

    public Set<String> getKeys(ItemStack item) {
        nbtService.exceptionCheck(nbtService, NBTManager.class);
        return nbtService.getKeys(item);
    }

    public static UUID getUUID(ItemStack item, String tag) {
        nbtService.exceptionCheck(nbtService, NBTManager.class);
        return nbtService.getUUID(item, tag);
    }
}
