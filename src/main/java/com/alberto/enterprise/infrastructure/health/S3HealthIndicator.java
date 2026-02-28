package com.alberto.enterprise.infrastructure.health;

import org.springframework.stereotype.Component;

import com.alberto.enterprise.infrastructure.config.StorageProperties;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;

@Component
public class S3HealthIndicator implements HealthIndicator {

    private final S3Client s3Client;
    private final StorageProperties storageProperties; // tu clase de props

    public S3HealthIndicator(S3Client s3Client, StorageProperties storageProperties) {
        this.s3Client = s3Client;
        this.storageProperties = storageProperties;
    }

    @Override
    public Health health() {
        // Si no estás en provider=s3, no bloquees el readiness
        if (!"s3".equalsIgnoreCase(storageProperties.getProvider())) {
            return Health.up().withDetail("provider", storageProperties.getProvider()).build();
        }

        String bucket = storageProperties.getBucket();
        if (bucket == null || bucket.isBlank()) {
            return Health.down().withDetail("reason", "APP_STORAGE_BUCKET is empty").build();
        }

        try {
            s3Client.headBucket(HeadBucketRequest.builder().bucket(bucket).build());
            return Health.up().withDetail("bucket", bucket).build();
        } catch (Exception e) {
            return Health.down(e).withDetail("bucket", bucket).build();
        }
    }
}
