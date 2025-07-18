package org.novasparkle.lunaspring.API.configuration.builder;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.novasparkle.lunaspring.API.configuration.Configuration;
import org.novasparkle.lunaspring.API.configuration.IConfig;
import org.novasparkle.lunaspring.API.util.exceptions.InvalidAnnotationPresentException;
import org.novasparkle.lunaspring.API.util.utilities.reflection.AnnotationScanner;
import org.novasparkle.lunaspring.API.util.utilities.reflection.ClassEntry;
import org.novasparkle.lunaspring.LunaPlugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Set;
import java.util.function.BiConsumer;

@UtilityClass
public final class LunaConfigBuilder {
    public Configuration saveToConfig(LunaPlugin plugin, Class<?> configConstructedClass) {
        return LunaConfigBuilder.saveToConfig(plugin, configConstructedClass, true);
    }

    @SneakyThrows
    private Configuration saveToConfig(LunaPlugin plugin, Class<?> configConstructedClass, boolean setterCheck) {
        if (!configConstructedClass.isAnnotationPresent(ConfigConstructor.class)) {
            throw new InvalidAnnotationPresentException(configConstructedClass, ConfigConstructor.class);
        }

        ConfigConstructor annotation = configConstructedClass.getAnnotation(ConfigConstructor.class);
        if (setterCheck && !Configuration.class.isAssignableFrom(annotation.configType())) return null;

        File file = new File(plugin.getDataFolder(), annotation.filePath());
        if (!file.exists() || !file.isFile()) return null;

        Configuration config;

        IConfig loadedConfig = LunaConfigBuilder.getIConfig(configConstructedClass);
        if (!(loadedConfig instanceof Configuration)) config = new Configuration(file);
        else config = (Configuration) loadedConfig;

        Configuration finalConfig = config;
        LunaConfigBuilder.processFields(configConstructedClass, (f, p) -> {
            try {
                Object object = f.get(null);
                finalConfig.set(p, object);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });

        finalConfig.save();
        return finalConfig;
    }

    public void saveToConfigs(LunaPlugin plugin) {
        Set<ClassEntry<ConfigConstructor>> classList = AnnotationScanner.findAnnotatedClasses(plugin, ConfigConstructor.class);
        for (ClassEntry<ConfigConstructor> entry : classList) {
            LunaConfigBuilder.saveToConfig(plugin, entry.getClazz(), true);
        }
    }

    @SneakyThrows
    public IConfig loadFromConfig(LunaPlugin plugin, Class<?> configConstructedClass) {
        if (!configConstructedClass.isAnnotationPresent(ConfigConstructor.class)) {
            throw new InvalidAnnotationPresentException(configConstructedClass, ConfigConstructor.class);
        }

        ConfigConstructor annotation = configConstructedClass.getAnnotation(ConfigConstructor.class);
        File file = new File(plugin.getDataFolder(), annotation.filePath());
        if (!file.exists() || !file.isFile()) return null;

        IConfig config = LunaConfigBuilder.getIConfig(configConstructedClass);
        if (config == null) config = new IConfig(file);

        IConfig finalConfig = config;
        LunaConfigBuilder.processFields(configConstructedClass, (f, p) -> {
            Object object = finalConfig.getObject(p);
            if (object == null || f.getType().isAssignableFrom(object.getClass())) {
                try {
                    f.set(null, object);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        return config;
    }

    public void loadFromConfigs(LunaPlugin plugin) {
        Set<ClassEntry<ConfigConstructor>> classList = AnnotationScanner.findAnnotatedClasses(plugin, ConfigConstructor.class);
        for (ClassEntry<ConfigConstructor> entry : classList) {
            LunaConfigBuilder.loadFromConfig(plugin, entry.getClazz());
        }
    }

    @SneakyThrows
    public IConfig generateConfig(LunaPlugin plugin, Class<?> configConstructedClass, boolean mayReplaceExistsFile) {
        if (!configConstructedClass.isAnnotationPresent(ConfigConstructor.class)) {
            throw new InvalidAnnotationPresentException(configConstructedClass, ConfigConstructor.class);
        }

        ConfigConstructor annotation = configConstructedClass.getAnnotation(ConfigConstructor.class);
        File file = new File(plugin.getDataFolder(), annotation.filePath());
        if (file.exists() && !mayReplaceExistsFile) return null;

        if (Configuration.class.isAssignableFrom(annotation.configType())) {
            Configuration config;

            IConfig loadedConfig = LunaConfigBuilder.getIConfig(configConstructedClass);
            if (!(loadedConfig instanceof Configuration)) config = new Configuration(file);
            else config = (Configuration) loadedConfig;

            Configuration finalConfig = config;
            LunaConfigBuilder.processFields(configConstructedClass, (f, p) -> {
                try {
                    finalConfig.set(p, f.get(null));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            });

            finalConfig.save();
            return finalConfig;
        }

        if (file.exists() && !file.delete()) {
            IConfig config = LunaConfigBuilder.getIConfig(configConstructedClass);
            return config == null ? new IConfig(file) : config;
        }

        if (annotation.filePath().equals("config.yml") || annotation.filePath().equals("config")) {
            if (annotation.loadableFromResource()) {
                plugin.saveDefaultConfig();
                return LunaConfigBuilder.loadFromConfig(plugin, configConstructedClass);
            }

            IConfig config = generateConfig(file, plugin, configConstructedClass);
            plugin.reloadConfig();
            return config;
        }

        if (!annotation.loadableFromResource()) return generateConfig(file, plugin, configConstructedClass);
        try {
            plugin.loadFile(annotation.filePath());
            return LunaConfigBuilder.loadFromConfig(plugin, configConstructedClass);
        } catch (IllegalArgumentException e) {
            return generateConfig(file, plugin, configConstructedClass);
        }
    }

    private IConfig generateConfig(File file, LunaPlugin plugin, Class<?> configConstructedClass) {
        try {
            return file != null && (file.exists() || file.getParentFile().mkdirs() && file.createNewFile()) ?
                    LunaConfigBuilder.saveToConfig(plugin, configConstructedClass, false) : null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void generateConfigs(LunaPlugin plugin, boolean mayReplaceExistsFile) {
        Set<ClassEntry<ConfigConstructor>> classList = AnnotationScanner.findAnnotatedClasses(plugin, ConfigConstructor.class);
        for (ClassEntry<ConfigConstructor> entry : classList) {
            LunaConfigBuilder.generateConfig(plugin, entry.getClazz(), mayReplaceExistsFile);
        }
    }

    @SneakyThrows
    public IConfig getIConfig(Class<?> configConstructedClass) {
        if (!configConstructedClass.isAnnotationPresent(ConfigConstructor.class)) {
            throw new InvalidAnnotationPresentException(configConstructedClass, ConfigConstructor.class);
        }

        try {
            Field field = configConstructedClass.getDeclaredField("config");

            Object object = field.get(null);
            return IConfig.class.isAssignableFrom(field.getType()) && object != null ? (IConfig) object : null;
        } catch (NoSuchFieldException e) {
            return null;
        }
    }

    private void processFields(Class<?> clazz, BiConsumer<Field, String> consumer) {
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(IgnoredField.class)) continue;

            String name = field.isAnnotationPresent(ConfigBuilderName.class) ? field.getAnnotation(ConfigBuilderName.class).value() : field.getName().toLowerCase();
            String path = (field.isAnnotationPresent(ConfigBuilderPath.class) ? field.getAnnotation(ConfigBuilderPath.class).value() : "") + name;

            if (field.isAnnotationPresent(ConfigBuilderSection.class)) {
                LunaConfigBuilder.processFields(field.getType(), consumer);
                continue;
            }

            consumer.accept(field, path);
        }
    }
}
