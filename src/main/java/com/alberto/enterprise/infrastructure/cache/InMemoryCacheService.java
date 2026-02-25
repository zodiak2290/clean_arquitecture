package com.alberto.enterprise.infrastructure.cache;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.alberto.enterprise.application.port.out.CacheService;

@Component
public class InMemoryCacheService implements CacheService {

    private final Map<String, String> map = new ConcurrentHashMap<>();

    @Override
    public void put(String key, String value, int ttlSeconds) {
        map.put(key, value); // MVP: sin TTL real
    }

    @Override
    public Optional<String> get(String key) {
        return Optional.ofNullable(map.get(key));
    }
}
