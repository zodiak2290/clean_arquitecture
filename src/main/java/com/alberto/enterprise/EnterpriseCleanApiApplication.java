package com.alberto.enterprise;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.alberto.enterprise.infrastructure.config.StorageProperties;

@EnableConfigurationProperties(StorageProperties.class)
@SpringBootApplication
public class EnterpriseCleanApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(EnterpriseCleanApiApplication.class, args);
	}

}
