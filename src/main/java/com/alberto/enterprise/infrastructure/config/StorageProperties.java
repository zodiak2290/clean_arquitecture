package com.alberto.enterprise.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.storage")
public class StorageProperties {

    private String provider;
    private String bucket;

    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }

    public String getBucket() { return bucket; }
    public void setBucket(String bucket) { this.bucket = bucket; }
}