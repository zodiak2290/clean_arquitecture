package com.alberto.enterprise.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class CacheMetrics {

    private final Counter cacheHit;
    private final Counter cacheMiss;
    private final Counter cachePut;

    public CacheMetrics(MeterRegistry registry) {

        this.cacheHit = Counter.builder("cache_hit_total")
                .description("Total cache hits")
                .register(registry);

        this.cacheMiss = Counter.builder("cache_miss_total")
                .description("Total cache miss")
                .register(registry);

        this.cachePut = Counter.builder("cache_put_total")
                .description("Total cache puts")
                .register(registry);
    }

    public void hit() {
        cacheHit.increment();
    }

    public void miss() {
        cacheMiss.increment();
    }

    public void put() {
        cachePut.increment();
    }
}