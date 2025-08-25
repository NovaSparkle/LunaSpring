package org.novasparkle.lunaspring.API.util.service.realized;

import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.Getter;
import org.checkerframework.checker.nullness.qual.PolyNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Getter
public class Cache<K, V> {
    private final com.github.benmanes.caffeine.cache.Cache<K, V> cache;
    private final long ttl;
    private final TimeUnit unit;
    private final long maximumSize;
    public Cache(long ttl, TimeUnit ttlUnit, long maximumSize) {
        this.cache = Caffeine.newBuilder()
                .expireAfterWrite(ttl, ttlUnit)
                .maximumSize(maximumSize)
                .build();
        this.ttl = ttl;
        this.unit = ttlUnit;
        this.maximumSize = maximumSize < 0 ? -1 : maximumSize;
    }

    public Cache(long ttl, TimeUnit ttlUnit) {
        this.cache = Caffeine.newBuilder()
                .expireAfterWrite(ttl, ttlUnit)
                .build();
        this.ttl = ttl;
        this.unit = ttlUnit;
        this.maximumSize = -1;
    }

    public Cache(Cache<K, V> forCloningCache) {
        this(forCloningCache.ttl, forCloningCache.unit, forCloningCache.maximumSize);
    }

    public ConcurrentMap<K, V> toMap() {
        return this.cache.asMap();
    }

    public @PolyNull V get(K key, Function<? super K, ? extends V> function) {
        return this.cache.get(key, function);
    }

    public @Nullable V getIfPresent(K key) {
        return this.cache.getIfPresent(key);
    }

    public void put(K key, V value) {
        this.cache.put(key, value);
    }

    public void cleanUp() {
        this.cache.cleanUp();
    }

    public Cache<K, V> duplicate() {
        Cache<K, V> cloned = new Cache<>(this.ttl, this.unit, this.maximumSize);
        for (Map.Entry<K, V> entry : this.cache.asMap().entrySet()) {
            cloned.put(entry.getKey(), entry.getValue());
        }
        return cloned;
    }

}
