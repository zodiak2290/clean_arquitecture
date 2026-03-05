package com.alberto.enterprise.infrastructure.cache;

import java.time.Duration;
import java.util.Optional;

import org.springframework.data.redis.core.StringRedisTemplate;

import com.alberto.enterprise.application.port.out.CacheService;

public class RedisCacheService implements CacheService {

	  private final StringRedisTemplate redis;

	  public RedisCacheService(StringRedisTemplate redis) {
	    this.redis = redis;
	  }

	  @Override
	  public void put(String key, String value, long ttlSeconds) {
	    redis.opsForValue().set(key, value, Duration.ofSeconds(ttlSeconds));
	  }

	  @Override
	  public Optional<String> get(String key) {
	    return Optional.ofNullable(redis.opsForValue().get(key));
	  }

	  public void delete(String key) {
	    redis.delete(key);
	  }
}