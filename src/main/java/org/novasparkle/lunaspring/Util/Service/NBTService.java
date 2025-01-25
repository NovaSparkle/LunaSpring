package org.novasparkle.lunaspring.Util.Service;

import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.iface.ReadWriteItemNBT;
import de.tr7zw.nbtapi.iface.ReadWriteNBT;
import de.tr7zw.nbtapi.iface.ReadableNBT;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.UUID;
import java.util.function.Consumer;

public final class NBTService implements LunaService {
    public ItemStack generatePlayerHead(OfflinePlayer player) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.setOwningPlayer(player);
        head.setItemMeta(meta);
        return head;
    }
    public void base64head(ItemStack head, String value, UUID uuid) {
        NBT.modify(head, nbt -> {
            ReadWriteNBT skullOwnerCompound = nbt.getOrCreateCompound("SkullOwner");
            skullOwnerCompound.setUUID("Id", uuid);
            skullOwnerCompound.getOrCreateCompound("Properties")
                    .getCompoundList("textures")
                    .addCompound()
                    .setString("Value", value);
        });
    }
    public boolean hasTag(ItemStack item, String tag) {
        ReadableNBT readableNBT = NBT.readNbt(item);
        return readableNBT.hasTag(tag);
    }

    public void set(ItemStack item, Consumer<ReadWriteItemNBT> consumer) {
        NBT.modify(item, consumer);
    }

    public Object get(ItemStack item, String tag) {
        return NBT.get(item, nbt -> tag);
    }
}