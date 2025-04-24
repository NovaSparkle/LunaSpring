package org.novasparkle.lunaspring.API.commands.annotations;

import org.novasparkle.lunaspring.API.commands.LunaSpringSubCommand;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SubCommand {
    int maxArgs();
    String[] commandIdentifiers();
    LunaSpringSubCommand.AccessFlag[] flags();
}
