package org.novasparkle.lunaspring.API.conditions;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.novasparkle.lunaspring.API.conditions.abs.Condition;
import org.novasparkle.lunaspring.API.conditions.abs.ConditionId;
import org.novasparkle.lunaspring.API.conditions.abs.PlayerCondition;
import org.novasparkle.lunaspring.API.util.utilities.Utils;
import org.novasparkle.lunaspring.API.util.utilities.reflection.AnnotationScanner;
import org.novasparkle.lunaspring.API.util.utilities.reflection.ClassEntry;
import org.novasparkle.lunaspring.LunaPlugin;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

@UtilityClass
public class Conditions {
    public final Map<String, Condition<?>> types = new HashMap<>();

    public Set<String> keys() {
        return types.keySet();
    }

    public boolean register(String id, Condition<?> condition) {
        if (types.containsKey(id)) return false;
        types.put(id, condition);
        return true;
    }

    public boolean register(@NotNull Condition<?> condition) {
        ConditionId conditionId = condition.getClass().getAnnotation(ConditionId.class);
        return conditionId != null && register(conditionId.value(), condition);
    }

    public void unregister(String id) {
        types.remove(id);
    }

    @SneakyThrows
    public void load(LunaPlugin plugin, String... packages) {
        Set<ClassEntry<ConditionId>> set = AnnotationScanner.findAnnotatedClasses(
                plugin,
                ConditionId.class,
                packages);
        for (ClassEntry<ConditionId> classEntry : set) {
            Class<?> clazz = classEntry.getClazz();
            if (!Condition.class.isAssignableFrom(clazz)) continue;

            Condition<?> condition = (Condition<?>) clazz.getDeclaredConstructor().newInstance();
            register(classEntry.getAnnotation().value(), condition);
        }
    }

    public @Nullable Condition<?> getCondition(String id) {
        return types.get(id);
    }

    public @Nullable Condition<?> getCondition(@NotNull ConfigurationSection section) {
        String type = section.getString("type");
        return getCondition(type);
    }

    public boolean checkCondition(@Nullable Player player, @NotNull ConfigurationSection section) {
        Condition<?> condition = getCondition(section);
        if (condition == null) return false;

        if (player != null && condition instanceof PlayerCondition playerCondition) {
            return playerCondition.check(player, playerCondition.generateObjects(section));
        }

        return condition.unknownCheck(section);
    }

    public boolean checkConditions(@Nullable Player player,
                                   @NotNull ConfigurationSection section,
                                   Operation operation) {
        Stream<ConfigurationSection> stream = section.getKeys(false)
                .stream()
                .map(section::getConfigurationSection)
                .filter(Objects::nonNull);

        Predicate<ConfigurationSection> predicate = s -> checkCondition(player, s);
        if (operation == null || operation == Operation.AND) {
            return stream.allMatch(predicate);
        }
        else {
            return stream.anyMatch(predicate);
        }
    }

    public boolean checkConditions(@Nullable Player player, @NotNull ConfigurationSection section) {
        Operation operation = Utils.getEnumValue(Operation.class, section.getString("operation"));
        return checkConditions(player, section, operation);
    }

    public Object[] generateObjects(ConfigurationSection section) {
        Condition<?> condition = getCondition(section);
        return condition instanceof PlayerCondition playerCondition ?
                playerCondition.generateObjects(section) :
                null;
    }

    public enum Operation {
        AND,
        OR
    }
}
