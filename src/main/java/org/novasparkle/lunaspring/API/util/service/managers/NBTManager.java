package org.novasparkle.lunaspring.API.util.service.managers;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.NBTBlock;
import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.iface.ReadWriteItemNBT;
import de.tr7zw.nbtapi.iface.ReadableNBT;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

@UtilityClass
public class NBTManager {

    public ItemStack base64head(ItemStack head, OfflinePlayer player) {
        if (!head.getType().equals(Material.PLAYER_HEAD)) throw new IllegalArgumentException("ItemStack должен иметь материал PLAYER_HEAD! Текущий: " + head.getType().name());
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.setOwningPlayer(player);
        head.setItemMeta(meta);
        return head;
    }

    public void base64head(ItemStack head, String value, UUID uuid) {
        if (value != null && !value.isEmpty()) {
            if (!head.getType().equals(Material.PLAYER_HEAD)) throw new IllegalArgumentException("ItemStack должен иметь материал PLAYER_HEAD! Текущий: " + head.getType().name());
            SkullMeta meta = (SkullMeta) head.getItemMeta();
            PlayerProfile playerProfile = Bukkit.createProfile(uuid);
            playerProfile.setProperty(new ProfileProperty("textures", value));
            meta.setPlayerProfile(playerProfile);
            head.setItemMeta(meta);
        }
    }

    public void base64head(ItemStack head, String value) {
        NBTManager.base64head(head, value, UUID.randomUUID());
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

    public void setString(Block block, String key, boolean value) {
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

    public void setList(ItemStack item, String tag, List<String> stringList) {
        setString(item, tag, String.join(" <]- ", stringList));
    }

    public List<String> getList(ItemStack item, String tag) {
        String value = getString(item, tag);

        List<String> list = new ArrayList<>();
        if (value != null && !value.isEmpty()) list.addAll(List.of(value.split(" <]- ")));
        return list;
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

    public static UUID getUUID(ItemStack item, String tag) {
        return getRoot(item).getUUID(tag);
    }

    public static boolean isSimilar(ItemStack item1, ItemStack item2) {
        return getRoot(item1).equals(getRoot(item2));
    }
}
