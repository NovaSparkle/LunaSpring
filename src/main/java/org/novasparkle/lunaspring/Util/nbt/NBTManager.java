package org.novasparkle.lunaspring.Util.nbt;

import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.iface.ReadWriteItemNBT;
import de.tr7zw.nbtapi.iface.ReadWriteNBT;
import de.tr7zw.nbtapi.iface.ReadableNBT;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.UUID;
import java.util.function.Consumer;

@UtilityClass
public class NBTManager {
    @Getter private final boolean enabled;
    static {
        enabled = (Bukkit.getServer().getPluginManager().getPlugin("NBTAPI") == null);
    }
    public ItemStack base64head(OfflinePlayer player) {
        if (NBTManager.isEnabled()) {
            ItemStack head = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) head.getItemMeta();
            meta.setOwningPlayer(player);
            head.setItemMeta(meta);
            return head;
        } else System.out.println("NBTManager is disabled!");
        return null;
    }
    public void base64head(ItemStack head, String value, UUID uuid) {
        if (NBTManager.isEnabled() && head.getType() == Material.PLAYER_HEAD)
            NBT.modify(head, nbt -> {
            ReadWriteNBT skullOwnerCompound = nbt.getOrCreateCompound("SkullOwner");
            skullOwnerCompound.setUUID("Id", uuid);
            skullOwnerCompound.getOrCreateCompound("Properties")
                    .getCompoundList("textures")
                    .addCompound()
                    .setString("Value", value);
            });
    }

    public void base64head(ItemStack head, String value) {
        NBTManager.base64head(head, value, UUID.randomUUID());
    }

    public boolean hasTag(ItemStack item, String tag) {
        if (!NBTManager.isEnabled()) return false;
        ReadableNBT readableNBT = NBT.readNbt(item);
        return readableNBT.hasTag(tag);
    }

    public void set(ItemStack item, Consumer<ReadWriteItemNBT> consumer) {
        if (!NBTManager.isEnabled()) return;
        NBT.modify(item, consumer);
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

    public Object get(ItemStack item, String tag) {
        return NBTManager.isEnabled() ? NBT.get(item, nbt -> tag) : null;
    }
}
