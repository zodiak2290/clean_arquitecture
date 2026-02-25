package com.alberto.enterprise.application.port.out;

import java.util.Optional;

public interface CacheService {
    void put(String key, String value, int ttlSeconds);
    Optional<String> get(String key);
}
