package org.novasparkle.lunaspring.API.util.exceptions;

public class InvalidImplementation extends LunaException {
    public InvalidImplementation(Class<?> clazz, Class<?> interfaze) {
        super(String.format("Класс %s должен быть наследником/реализацией %s", clazz.getSimpleName(), interfaze.getSimpleName()));
    }
}
