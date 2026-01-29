package org.novasparkle.lunaspring.API.util.service.realized.nbt;

import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.NBTBlock;
import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.iface.ReadWriteItemNBT;
import de.tr7zw.nbtapi.iface.ReadableNBT;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.novasparkle.lunaspring.API.util.service.PluginService;

import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

public class NBTService extends PluginService implements INBTService<ReadableNBT> {
    public NBTService() {
        super("NBTAPI");
    }

    public ReadableNBT getRoot(ItemStack item) {
        return NBT.readNbt(item);
    }

    public boolean hasTag(ItemStack item, String tag) {
        return getRoot(item).hasTag(tag);
    }

    public void set(ItemStack item, Consumer<ReadWriteItemNBT> consumer) {
        NBT.modify(item, consumer);
    }

    public NBTCompound getBlockData(Block block) {
        return new NBTBlock(block).getData();
    }

    public boolean hasTag(Block block, String key) {
        return getBlockData(block).hasTag(key);
    }

    public void setInt(Block block, String key, int value) {
        getBlockData(block).setInteger(key, value);
    }

    public void setLong(Block block, String key, long value) {
        getBlockData(block).setLong(key, value);
    }

    public void setDouble(Block block, String key, double value) {
        getBlockData(block).setDouble(key, value);
    }

    public void setByte(Block block, String key, byte value) {
        getBlockData(block).setByte(key, value);
    }

    public void setString(Block block, String key, String value) {
        getBlockData(block).setString(key, value);
    }

    public void setBoolean(Block block, String key, boolean value) {
        getBlockData(block).setBoolean(key, value);
    }

    public void setUUID(Block block, String key, UUID value) {
        getBlockData(block).setUUID(key, value);
    }

    public void setFloat(Block block, String key, float value) {
        getBlockData(block).setFloat(key, value);
    }

    public void setItemStack(Block block, String key, ItemStack value) {
        getBlockData(block).setItemStack(key, value);
    }

    public String getType(ItemStack item, String key) {
        return getRoot(item).getType(key).name();
    }

    public void removeKey(ItemStack item, String key) {
        this.set(item, nbt -> nbt.removeKey(key));
    }

    public void removeKey(Block block, String key) {
        this.getBlockData(block).removeKey(key);
    }

    public void setString(ItemStack item, String tag, String value) {
        this.set(item, nbt -> nbt.setString(tag, value));
    }
    public void setInt(ItemStack item, String tag, int value) {
        this.set(item, nbt -> nbt.setInteger(tag, value));
    }

    public void setByte(ItemStack item, String tag, byte value) {
        this.set(item, nbt -> nbt.setByte(tag, value));
    }

    public void setLong(ItemStack item, String tag, long value) {
        this.set(item, nbt -> nbt.setLong(tag, value));
    }

    public void setFloat(ItemStack item, String tag, float value) {
        this.set(item, nbt -> nbt.setFloat(tag, value));
    }

    public void setDouble(ItemStack item, String tag, double value) {
        this.set(item, nbt -> nbt.setDouble(tag, value));
    }

    public void setBool(ItemStack item, String tag, boolean value) {
        this.set(item, nbt -> nbt.setBoolean(tag, value));
    }

    public void setUUID(ItemStack item, String tag, UUID value) {
        this.set(item, nbt -> nbt.setUUID(tag, value));
    }

    public void setItem(ItemStack item, String tag, ItemStack value) {
        this.set(item, nbt -> nbt.setItemStack(tag, value));
    }

    public String getString(Block block, String tag) {
        return getBlockData(block).getString(tag);
    }

    public int getInt(Block block, String tag) {
        return getBlockData(block).getInteger(tag);
    }

    public double getDouble(Block block, String tag) {
        return getBlockData(block).getDouble(tag);
    }

    public float getFloat(Block block, String tag) {
        return getBlockData(block).getFloat(tag);
    }

    public long getLong(Block block, String tag) {
        return getBlockData(block).getLong(tag);
    }

    public byte getByte(Block block, String tag) {
        return getBlockData(block).getByte(tag);
    }

    public UUID getUUID(Block block, String tag) {
        return getBlockData(block).getUUID(tag);
    }

    public String getString(ItemStack item, String tag) {
        return getRoot(item).getString(tag);
    }

    public int getInt(ItemStack item, String tag) {
        return getRoot(item).getInteger(tag);
    }

    public double getDouble(ItemStack item, String tag) {
        return getRoot(item).getDouble(tag);
    }

    public byte getByte(ItemStack item, String tag) {
        return getRoot(item).getByte(tag);
    }

    public ItemStack getItemStack(ItemStack item, String tag) {
        return getRoot(item).getItemStack(tag);
    }

    public long getLong(ItemStack item, String tag) {
        return getRoot(item).getLong(tag);
    }

    public boolean getBoolean(ItemStack item, String tag) {
        return getRoot(item).getBoolean(tag);
    }

    public ItemStack[] getItemStacks(ItemStack item, String tag) {
        return getRoot(item).getItemStackArray(tag);
    }

    public float getFloat(ItemStack item, String tag) {
        return getRoot(item).getFloat(tag);
    }

    public Set<String> getKeys(ItemStack item) {
        return getRoot(item).getKeys();
    }

    public UUID getUUID(ItemStack item, String tag) {
        return getRoot(item).getUUID(tag);
    }

    public boolean isSimilar(ItemStack item1, ItemStack item2) {
        return getRoot(item1).equals(getRoot(item2));
    }
}
