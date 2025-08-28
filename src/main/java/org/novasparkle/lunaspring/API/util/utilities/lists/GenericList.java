package org.novasparkle.lunaspring.API.util.utilities.lists;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.novasparkle.lunaspring.API.util.utilities.Utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class GenericList<E> extends ArrayList<E> {
    public GenericList(int initialCapacity) {
        super(initialCapacity);
    }

    public GenericList(@NotNull Collection<? extends E> c) {
        super(c);
    }

    public GenericList() {
        super();
    }

    public GenericList(Class<E> itemClass, String serializableData) {
        super(Utils.Base64.deserializeList(itemClass, serializableData));
    }

    @SafeVarargs
    public GenericList(E... elements) {
        this(List.of(elements));
    }

    public GenericList<E> filter(Predicate<E> predicate) {
        this.removeIf(predicate);
        return this;
    }

    public Stream<E> s() {
        return this.stream();
    }

    public E first(Predicate<E> predicate, E orElse) {
        return Utils.find(this.s(), predicate).orElse(orElse);
    }

    public @Nullable E first(Predicate<E> predicate) {
        return this.first(predicate, null);
    }

    public @Nullable E first() {
        return this.isEmpty() ? null : this.get(0);
    }

    public @Nullable E last() {
        return this.isEmpty() ? null : this.get(this.size() - 1);
    }

    public String serialize() {
        return Utils.Base64.serializeList(this);
    }

    public boolean contains(Predicate<E> predicate) {
        return this.first(predicate) != null;
    }
}
