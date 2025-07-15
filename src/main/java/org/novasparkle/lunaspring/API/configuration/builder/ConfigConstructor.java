package org.novasparkle.lunaspring.API.configuration.builder;

import org.novasparkle.lunaspring.API.configuration.IConfig;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ConfigConstructor {
    Class<? extends IConfig> configType();
    String filePath();
}
