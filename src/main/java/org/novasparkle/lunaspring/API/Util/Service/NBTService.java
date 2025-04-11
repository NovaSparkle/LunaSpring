package org.novasparkle.lunaspring.API.Util.Service;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.NBTBlock;
import de.tr7zw.nbtapi.handler.NBTHandlers;
import de.tr7zw.nbtapi.iface.NBTHandler;
import de.tr7zw.nbtapi.iface.ReadWriteItemNBT;
import de.tr7zw.nbtapi.iface.ReadWriteNBT;
import de.tr7zw.nbtapi.iface.ReadableNBT;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.novasparkle.lunaspring.API.Util.Service.LunaService;
import org.novasparkle.lunaspring.LunaPlugin;

import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

public final class NBTService implements LunaService {
    public ItemStack base64head(ItemStack head, OfflinePlayer player) {
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.setOwningPlayer(player);
        head.setItemMeta(meta);
        return head;
    }

    public void base64head(ItemStack head, String value, UUID uuid) {
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        PlayerProfile playerProfile = Bukkit.createProfile(uuid);
        playerProfile.setProperty(new ProfileProperty("textures", value));
        meta.setPlayerProfile(playerProfile);
        head.setItemMeta(meta);
    }

    public boolean hasTag(ItemStack item, String tag) {
        ReadableNBT readableNBT = NBT.readNbt(item);
        return readableNBT.hasTag(tag);
    }

    public void set(ItemStack item, Consumer<ReadWriteItemNBT> consumer) {
        NBT.modify(item, consumer);
    }

    public String getString(ItemStack item, String tag) {
        return NBT.get(item, nbt -> (String) nbt.getString(tag));
    }

    public int getInt(ItemStack item, String tag) {
        return NBT.get(item, nbt -> (int) nbt.getInteger(tag));
    }

    public double getDouble(ItemStack item, String tag) {
        return NBT.get(item, nbt -> (double) nbt.getDouble(tag));
    }

    public byte getByte(ItemStack item, String tag) {
        return NBT.get(item, nbt -> (byte) nbt.getByte(tag));
    }

    public ItemStack getItemStack(ItemStack item, String tag) {
        return NBT.get(item, nbt -> (ItemStack) nbt.getItemStack(tag));
    }

    public long getLong(ItemStack item, String tag) {
        return NBT.get(item, nbt -> (long) nbt.getLong(tag));
    }

    public boolean getBoolean(ItemStack item, String tag) {
        return NBT.get(item, nbt -> (boolean) nbt.getBoolean(tag));
    }

    public ItemStack[] getItemStacks(ItemStack item, String tag) {
        return NBT.get(item, nbt -> (ItemStack[]) nbt.getItemStackArray(tag));
    }

    public float getFloat(ItemStack item, String tag) {
        return NBT.get(item, nbt -> (float) nbt.getFloat(tag));
    }

    public Set<String> getKeys(ItemStack item) {
        return NBT.get(item, nbt -> (Set<String>) nbt.getKeys());
    }

    public UUID getUUID(ItemStack item, String tag) {
        return NBT.get(item, nbt -> (UUID) nbt.getUUID(tag));
    }
}