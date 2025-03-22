package org.novasparkle.lunaspring.other;

import java.util.*;
import java.util.stream.Collectors;

public class LunaMap<S, E, X> {
    private final Map<S, Element<E, X>> map = new HashMap<>();

    public void put(S key, E value1, X value2) {
        this.map.put(key, new Element<>(value1, value2));
    }

    public void put(S key, Element<E, X> element) {
        this.map.put(key, element);
    }

    public Map<S, Element<E, X>> toHashMap() {
        return this.map;
    }

    public int size() {
        return this.map.size();
    }

    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    public boolean containsKey(S key) {
        return this.map.containsKey(key);
    }

    public boolean containsFirstValue(E firstValue) {
        return this.map.values().stream().anyMatch(e -> e.getFirst().equals(firstValue));
    }

    public boolean containsSecondValue(X secondValue) {
        return this.map.values().stream().anyMatch(e -> e.getSecond().equals(secondValue));
    }

    public boolean contains(S key, E firstValue, X secondValue) {
        return this.map.entrySet().stream().anyMatch((entry) -> {
            Element<E, X> el = entry.getValue();
            if (entry.getKey().equals(key)) {
                return el.getFirst().equals(firstValue) && el.getSecond().equals(secondValue);
            }
            return false;
        });
    }

    public boolean contains(S key, Element<E, X> element) {
        return this.contains(key, element.getFirst(), element.getSecond());
    }

    public void remove(S key) {
        this.map.remove(key);
    }

    public void clear() {
        this.map.clear();
    }

    public Set<S> keySet() {
        return this.map.keySet();
    }

    public Collection<E> firstValues() {
        return this.map.values().stream().map(Element::getFirst).collect(Collectors.toSet());
    }

    public Collection<X> secondValues() {
        return this.map.values().stream().map(Element::getSecond).collect(Collectors.toSet());
    }

    public E getFirstValue(S key) {
        return this.getElement(key).getFirst();
    }

    public X getSecondValue(S key) {
        return this.getElement(key).getSecond();
    }

    public void replace(S key, E firstValue, X secondValue) {
        if (this.contains(key, firstValue, secondValue)) {
            this.map.replace(key, new Element<>(firstValue, secondValue));
        }
        else this.put(key, firstValue, secondValue);
    }

    public Element<E, X> getElement(S key) {
        return this.map.get(key);
    }

    public record Element<E, X>(E e, X x) {
        public E getFirst() {
            return this.e;
        }

        public X getSecond() {
            return this.x;
        }
    }
}
