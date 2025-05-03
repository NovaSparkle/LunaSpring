package org.novasparkle.lunaspring.API.util.utilities.reflection;

import lombok.SneakyThrows;
import org.novasparkle.lunaspring.LunaPlugin;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class AnnotationScanner {
    private final List<Class<?>> annotatedClasses;
    public AnnotationScanner() {
        this.annotatedClasses = new ArrayList<>();
    }
    @SneakyThrows
    public List<Class<?>> getAnnotatedClasses(LunaPlugin mainPlugin, Class<? extends Annotation> annotationClass) {
        try (JarFile jar = new JarFile(mainPlugin.getJar())) {
            Enumeration<JarEntry> e = jar.entries();
            while (e.hasMoreElements()) {
                JarEntry jarEntry = e.nextElement();
                if (jarEntry.getName().endsWith(".class")) {
                    String className = jarEntry.getName().replace(".class", "").replace('/', '.');
                    Class<?> clazz = Class.forName(className);
                    if (clazz.isAnnotationPresent(annotationClass)) {
                        this.annotatedClasses.add(clazz);
                    }
                }
            }
        }
        return this.annotatedClasses;
    }
}
