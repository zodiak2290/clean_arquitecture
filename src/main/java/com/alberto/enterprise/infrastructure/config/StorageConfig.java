package com.alberto.enterprise.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alberto.enterprise.application.port.out.FileStorage;
import com.alberto.enterprise.infrastructure.storage.LocalFileStorage;
import com.alberto.enterprise.infrastructure.storage.S3FileStorage;

@Configuration
public class StorageConfig {
    @Bean
    FileStorage fileStorage(
            @Value("${app.storage.provider}") String provider,
            LocalFileStorage localFileStorage,
            S3FileStorage s3FileStorage
    ) {
        if ("s3".equalsIgnoreCase(provider)) {
            return s3FileStorage;
        }
        return localFileStorage;
    }

    @Bean
    LocalFileStorage localFileStorage() {
        return new LocalFileStorage();
    }
}
