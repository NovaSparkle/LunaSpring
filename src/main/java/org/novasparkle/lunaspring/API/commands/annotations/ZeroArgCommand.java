package org.novasparkle.lunaspring.API.commands.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ZeroArgCommand {
    /**
     * Обработка команды без подкоманд и без аргументов
     * @return
     */
    String value();
}
