package com.alberto.enterprise;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;

import com.alberto.enterprise.infrastructure.config.StorageProperties;

@EnableConfigurationProperties(StorageProperties.class)
@SpringBootApplication
@EnableCaching
public class EnterpriseCleanApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(EnterpriseCleanApiApplication.class, args);
	}

}
