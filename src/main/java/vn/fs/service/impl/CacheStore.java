package vn.fs.service.impl;

import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import lombok.Getter;

@Getter
public class CacheStore<K, V> {
    private Cache<K, V> cache;

    //Constructor to build Cache Store
    public CacheStore(int expiryDuration, TimeUnit timeUnit) {
        cache = CacheBuilder.newBuilder()
                .expireAfterWrite(expiryDuration, timeUnit)
                .concurrencyLevel(Runtime.getRuntime().availableProcessors())
                .build();
    }

    public CacheStore(int maximumSize, int expiryDuration, TimeUnit timeUnit) {
        cache = CacheBuilder.newBuilder()
                .maximumSize(maximumSize)
                .expireAfterWrite(expiryDuration, timeUnit)
                .concurrencyLevel(Runtime.getRuntime().availableProcessors())
                .build();
    }
}
