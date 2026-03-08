package com.alberto.enterprise.infrastructure.cache;

import java.time.Duration;
import java.util.Optional;

import org.springframework.data.redis.core.StringRedisTemplate;

import com.alberto.enterprise.application.port.out.CacheService;
import com.alberto.enterprise.infrastructure.metrics.CacheMetrics;

public class RedisCacheService implements CacheService {

	  private final StringRedisTemplate redis;
 	  private final CacheMetrics metrics;

	  public RedisCacheService(StringRedisTemplate redis, CacheMetrics metrics) {
	    this.redis = redis;
        this.metrics = metrics;
	  }

	  @Override
	  public void put(String key, String value, long ttlSeconds) {
	    redis.opsForValue().set(key, value, Duration.ofSeconds(ttlSeconds));

		metrics.put();
	  }

	  @Override
	  public Optional<String> get(String key) {

		String value = redis.opsForValue().get(key);

		if (value != null) {
			metrics.hit();    // 📊 cache hit
			return Optional.of(value);
		}

		metrics.miss();     // 📊 cache miss
		return Optional.empty();

	  }

	  public void delete(String key) {
	    redis.delete(key);
	  }
}