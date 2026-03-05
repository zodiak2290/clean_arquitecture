package com.alberto.enterprise.infrastructure.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.alberto.enterprise.application.port.out.CacheService;
import com.alberto.enterprise.infrastructure.cache.InMemoryCacheService;
import com.alberto.enterprise.infrastructure.cache.RedisCacheService;

@Configuration
public class CacheConfig {

  @Bean
  @Primary
  @ConditionalOnProperty(name = "app.cache.provider", havingValue = "redis")
  CacheService redisCacheService(StringRedisTemplate redisTemplate) {
    return new RedisCacheService(redisTemplate);
  }

  @Bean
  @ConditionalOnProperty(name = "app.cache.provider", havingValue = "inmemory", matchIfMissing = true)
  CacheService inMemoryCacheService() {
    return new InMemoryCacheService();
  }
} 