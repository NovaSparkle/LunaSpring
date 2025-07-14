package org.novasparkle.lunaspring.API.util.service.managers.worldguard;

import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import lombok.Getter;
import lombok.NonNull;
import org.novasparkle.lunaspring.API.util.exceptions.WGFlagGetException;

import java.lang.reflect.Field;

@Getter
public enum LFlag {
    PASSTHROUGH,
    BUILD,
    BLOCK_BREAK,
    BLOCK_PLACE,
    USE,
    INTERACT,
    DAMAGE_ANIMALS,
    PVP,
    SLEEP,
    RESPAWN_ANCHORS,
    TNT,
    CHEST_ACCESS,
    PLACE_VEHICLE,
    DESTROY_VEHICLE,
    LIGHTER,
    RIDE,
    POTION_SPLASH,
    ITEM_FRAME_ROTATE,
    TRAMPLE_BLOCKS,
    FIREWORK_DAMAGE,
    USE_ANVIL,
    ITEM_PICKUP,
    ITEM_DROP,
    EXP_DROPS,
    MOB_DAMAGE,
    CREEPER_EXPLOSION,
    ENDERDRAGON_BLOCK_DAMAGE,
    GHAST_FIREBALL,
    OTHER_EXPLOSION,
    WITHER_DAMAGE,
    ENDER_BUILD,
    SNOWMAN_TRAILS,
    RAVAGER_RAVAGE,
    ENTITY_PAINTING_DESTROY,
    ENTITY_ITEM_FRAME_DESTROY,
    MOB_SPAWNING,
    PISTONS,
    FIRE_SPREAD,
    LAVA_FIRE,
    LIGHTNING,
    SNOW_FALL,
    SNOW_MELT,
    ICE_FORM,
    ICE_MELT,
    FROSTED_ICE_MELT,
    FROSTED_ICE_FORM,
    MUSHROOMS,
    LEAF_DECAY,
    GRASS_SPREAD,
    MYCELIUM_SPREAD,
    VINE_GROWTH,
    CROP_GROWTH,
    SOIL_DRY,
    CORAL_FADE,
    WATER_FLOW,
    LAVA_FLOW,
    SEND_CHAT,
    RECEIVE_CHAT,
    INVINCIBILITY,
    FALL_DAMAGE,
    HEALTH_REGEN,
    HUNGER_DRAIN,
    ENTRY,
    EXIT,
    EXIT_VIA_TELEPORT,
    ENDERPEARL,
    CHORUS_TELEPORT;

    public @NonNull StateFlag getWGFlag() {
        return LFlag.getWGFlag(this.name());
    }

    public @NonNull static StateFlag getWGFlag(String id) {
        id = id.toUpperCase();
        try {
            Field field = Flags.class.getField(id);
            field.setAccessible(true);
            return (StateFlag) field.get(null);
        }
        catch (NoSuchFieldException e) {
            throw new WGFlagGetException("Не найдено поле " + id);
        }
        catch (IllegalAccessException e) {
            throw new WGFlagGetException("Отсутствует разрешение к полю " + id);
        }
        catch (ClassCastException e) {
            throw new WGFlagGetException("Невозможно преобразовать в StateFlag");
        }
    }
}
