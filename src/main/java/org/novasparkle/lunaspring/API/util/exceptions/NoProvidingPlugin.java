package org.novasparkle.lunaspring.API.util.exceptions;

public class NoProvidingPlugin extends RuntimeException {
    public NoProvidingPlugin(String plugin) {
        super(String.format("Для работы, сервис требует плагин %s, но он отсутствует!", plugin));
    }
}
