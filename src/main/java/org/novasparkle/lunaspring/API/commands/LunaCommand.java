package org.novasparkle.lunaspring.API.commands;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface LunaCommand {
    int maxArgs();
    String[] commandIdentifiers();
    LunaSpringSubCommand.AccessFlag[] flags();
}
