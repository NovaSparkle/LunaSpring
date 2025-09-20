package org.novasparkle.lunaspring.API.util.service.managers.worldguard;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import org.novasparkle.lunaspring.LunaSpring;

import java.util.HashMap;
import java.util.Map;

@Getter
public class LunaFlags {
    @Accessors(fluent = true) private final Map<IState, StateFlag> states;
    public LunaFlags() {
        this.states = new HashMap<>();
    }

    public FlagRegistry getRegistry() {
        return WorldGuard.getInstance().getFlagRegistry();
    }

    public void register(IState state, StateFlag stateFlag) {
        this.states.put(state, stateFlag);
    }

    public @Nullable StateFlag register(FlagRegistry registry, IState state) {
        try {
            StateFlag stateFlag = new StateFlag(state.getId(), state.defaultValue());

            registry.register(stateFlag);
            this.register(state, stateFlag);

            return stateFlag;
        }
        catch (FlagConflictException e) {
            Flag<?> existing = registry.get(state.getId());
            if (existing instanceof StateFlag stateFlag) {
                this.register(state, stateFlag);
                return stateFlag;
            }

            LunaSpring.getInstance().warning(String.format("Флаг %s конфликтует с другим типом.", state.getId()));
            return null;
        }
    }

    public @Nullable StateFlag register(IState state) {
        return this.register(getRegistry(), state);
    }

    public @Nullable StateFlag flag(String id) {
        return this.states.entrySet()
                .stream()
                .filter(e -> e.getKey().getId().equalsIgnoreCase(id))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);
    }

    public @Nullable StateFlag flag(IState state) {
        return this.states.get(state);
    }

    public interface IState {
        String getId();
        boolean defaultValue();
    }

    @AllArgsConstructor @Getter
    public enum State implements IState {
        ELYTRA_GLIDE_FLAG("elytra_glide", true),
        ELYTRA_BOOST_FLAG("elytra_boost", true),
        ENABLE_FLY_FLAG("enable_fly", true),
        ENABLE_SPRINT_FLAG("enable_sprint", true),
        ENABLE_SNEAK_FLAG("enable_sneak", true),
        SHEAR_FLAG("shear_entities", true),
        BUCKET_FILL_FLAG("bukket_fill", true),
        BUCKET_EMPTY_FLAG("bukket_empty", true),
        PICKUP_EXP_FLAG("pickup_exp", true);

        private final String id;
        @Accessors(fluent = true) private final boolean defaultValue;

        public boolean check(Player player) {
            Location loc = player.getLocation();
            LunaFlags flags = GuardManager.flags();
            if (flags == null) return true;

            StateFlag stateFlag = flags.flag(State.this);
            return stateFlag == null || !GuardManager.checkState(loc, GuardManager.wrap(player), stateFlag);
        }
    }
}
