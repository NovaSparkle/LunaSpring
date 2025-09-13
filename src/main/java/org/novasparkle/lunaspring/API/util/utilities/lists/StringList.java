package org.novasparkle.lunaspring.API.util.utilities.lists;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class StringList extends LunaList<String> {
    public StringList(int initialCapacity) {
        super(initialCapacity);
    }

    public StringList(@NotNull Collection<? extends String> c) {
        super(c);
    }

    public StringList() {
    }

    public StringList(String serializableData) {
        super(String.class, serializableData);
    }

    public StringList(String... elements) {
        super(elements);
    }
}
