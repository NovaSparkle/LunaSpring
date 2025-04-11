package org.novasparkle.lunaspring.API.Util.Service.managers;

import de.tr7zw.nbtapi.NBTBlock;
import de.tr7zw.nbtapi.iface.ReadWriteItemNBT;
import lombok.experimental.UtilityClass;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.novasparkle.lunaspring.API.Util.Service.NBTService;


import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

@UtilityClass
public class NBTManager {
    private final NBTService nbtService;
    static {
        nbtService = new NBTService();
    }
    public ItemStack base64head(ItemStack head, OfflinePlayer player) {
        return nbtService.base64head(head, player);
    }

    public void base64head(ItemStack head, String value, UUID uuid) {
        nbtService.base64head(head, value, uuid);
    }

    public void base64head(ItemStack head, String value) {
        NBTManager.base64head(head, value, UUID.randomUUID());
    }

    public boolean hasTag(ItemStack item, String tag) {
        return nbtService.hasTag(item, tag);
    }

    public void set(ItemStack item, Consumer<ReadWriteItemNBT> consumer) {
        nbtService.set(item, consumer);
    }

    public void setInt(Block block, String key, int value) {
        new NBTBlock(block).getData().setInteger(key, value);
    }

    public void setLong(Block block, String key, long value) {
        new NBTBlock(block).getData().setLong(key, value);
    }

    public void setDouble(Block block, String key, double value) {
        new NBTBlock(block).getData().setDouble(key, value);
    }

    public void setByte(Block block, String key, byte value) {
        new NBTBlock(block).getData().setByte(key, value);
    }

    public void setString(Block block, String key, String value) {
        new NBTBlock(block).getData().setString(key, value);
    }

    public void setString(Block block, String key, boolean value) {
        new NBTBlock(block).getData().setBoolean(key, value);
    }

    public void setUUID(Block block, String key, UUID value) {
        new NBTBlock(block).getData().setUUID(key, value);
    }

    public void setFloat(Block block, String key, float value) {
        new NBTBlock(block).getData().setFloat(key, value);
    }

    public void setItemStack(Block block, String key, ItemStack value) {
        new NBTBlock(block).getData().setItemStack(key, value);
    }

    public void setString(ItemStack item, String tag, String value) {
        NBTManager.set(item, nbt -> nbt.setString(tag, value));
    }

    public void setInt(ItemStack item, String tag, int value) {
        NBTManager.set(item, nbt -> nbt.setInteger(tag, value));
    }

    public void setByte(ItemStack item, String tag, byte value) {
        NBTManager.set(item, nbt -> nbt.setByte(tag, value));
    }

    public void setLong(ItemStack item, String tag, long value) {
        NBTManager.set(item, nbt -> nbt.setLong(tag, value));
    }

    public void setDouble(ItemStack item, String tag, double value) {
        NBTManager.set(item, nbt -> nbt.setDouble(tag, value));
    }

    public void setBool(ItemStack item, String tag, boolean value) {
        NBTManager.set(item, nbt -> nbt.setBoolean(tag, value));
    }

    public void setUUID(ItemStack item, String tag, UUID value) {
        NBTManager.set(item, nbt -> nbt.setUUID(tag, value));
    }

    public void setItem(ItemStack item, String tag, ItemStack value) {
        NBTManager.set(item, nbt -> nbt.setItemStack(tag, value));
    }

    public String getString(ItemStack item, String tag) {
        return nbtService.getString(item, tag);
    }

    public int getInt(ItemStack item, String tag) {
        return nbtService.getInt(item, tag);
    }

    public double getDouble(ItemStack item, String tag) {
        return nbtService.getDouble(item, tag);
    }

    public byte getByte(ItemStack item, String tag) {
        return nbtService.getByte(item, tag);
    }

    public ItemStack getItemStack(ItemStack item, String tag) {
        return nbtService.getItemStack(item, tag);
    }

    public long getLong(ItemStack item, String tag) {
        return nbtService.getLong(item, tag);
    }

    public boolean getBoolean(ItemStack item, String tag) {
        return nbtService.getBoolean(item, tag);
    }

    public ItemStack[] getItemStacks(ItemStack item, String tag) {
        return nbtService.getItemStacks(item, tag);
    }

    public float getFloat(ItemStack item, String tag) {
        return nbtService.getFloat(item, tag);
    }

    public Set<String> getKeys(ItemStack item) {
        return nbtService.getKeys(item);
    }

    public static UUID getUUID(ItemStack item, String tag) {
        return nbtService.getUUID(item, tag);
    }
}
