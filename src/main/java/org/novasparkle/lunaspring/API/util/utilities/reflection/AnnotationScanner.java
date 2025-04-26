package org.novasparkle.lunaspring.API.util.utilities.reflection;

import lombok.NonNull;
import lombok.SneakyThrows;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AnnotationScanner {
    private final Package pack;
    private final List<Class<?>> annotatedClasses;
    public AnnotationScanner(@NonNull Package pack) {
        this.pack = pack;
        this.annotatedClasses = new ArrayList<>();
    }
    @SneakyThrows
    public List<Class<?>> getAnnotatedClasses(@NonNull Class<? extends Annotation> annotation) {
        String path = this.pack.getName().replace('.', '/');

        URL resource = Thread.currentThread().getContextClassLoader().getResource(path);
        if (resource != null) {
            File directory = new File(resource.toURI());
            if (directory.exists()) {
                scanDirectory(directory, this.pack.getName(), annotation);
            }
        }

        return this.annotatedClasses;
    }

    @SneakyThrows
    private void scanDirectory(File directory, String packageName, Class<? extends Annotation> annotation) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    String subPackageName = String.format("%s.%s", this.pack.getName(), file.getName());
                    scanDirectory(file, subPackageName, annotation);

                } else if (file.isFile() && file.getName().endsWith(".class")) {
                    String className = packageName + '.' + file.getName().replace(".class", "");
                    Class<?> clazz = Class.forName(className);
                    if (clazz.isAnnotationPresent(annotation)) this.annotatedClasses.add(clazz);
                }
            }
        }
    }
}
