package org.novasparkle.lunaspring.API.util.utilities.reflection;

import com.google.common.collect.Sets;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.novasparkle.lunaspring.LunaPlugin;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@UtilityClass
public class AnnotationScanner {

    @SneakyThrows
    @SuppressWarnings("unchecked")
    public <A extends Annotation> Set<Class<A>> getAnnotatedClasses(LunaPlugin mainPlugin, Class<A> annotationClass) {
        Set<Class<A>> annotatedClasses = new HashSet<>();

        try (JarFile jar = new JarFile(mainPlugin.getJar())) {
            Enumeration<JarEntry> e = jar.entries();

            while (e.hasMoreElements()) {
                JarEntry jarEntry = e.nextElement();

                if (jarEntry.getName().endsWith(".class")) {
                    String className = jarEntry.getName().replace(".class", "").replace('/', '.');
                    Class<?> clazz = Class.forName(className);

                    if (clazz.isAnnotationPresent(annotationClass)) {
                        annotatedClasses.add((Class<A>) clazz);
                    }
                }
            }
        } catch (IllegalStateException ignored) {}
        return annotatedClasses;
    }

    @SneakyThrows
    public <A extends Annotation> Set<ClassEntry<A>> findAnnotatedClasses(LunaPlugin plugin, Class<A> annotationClass) {
        Set<ClassEntry<A>> annotatedClasses = Sets.newHashSet();
        try (JarFile jar = new JarFile(plugin.getJar())) {

            Enumeration<JarEntry> e = jar.entries();

            while (e.hasMoreElements()) {
                JarEntry jarEntry = e.nextElement();

                if (jarEntry.getName().endsWith(".class")) {
                    String className = jarEntry.getName().replace(".class", "").replace('/', '.');
                    Class<?> clazz = Class.forName(className);
                    if (clazz.isAnnotationPresent(annotationClass)) {
                        ClassEntry<A> classEntry = new ClassEntry<>(clazz, annotationClass);
                        annotatedClasses.add(classEntry);
                    }
                }
            }
        } catch (IllegalStateException ignored) {}
        return annotatedClasses;
    }
}
