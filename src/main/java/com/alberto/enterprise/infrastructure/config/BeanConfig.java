package com.alberto.enterprise.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alberto.enterprise.application.port.out.CacheService;
import com.alberto.enterprise.application.port.out.EventPublisher;
import com.alberto.enterprise.application.port.out.FileStorage;
import com.alberto.enterprise.application.usecase.CreateUserUseCase;
import com.alberto.enterprise.application.usecase.UploadUserAvatarUseCase;
import com.alberto.enterprise.domain.repository.UserRepository;

@Configuration
public class BeanConfig {
	
    @Bean
    CreateUserUseCase createUserUseCase(UserRepository userRepository, EventPublisher eventPublisher, CacheService cacheService) {
        return new CreateUserUseCase(userRepository, eventPublisher, cacheService);
    }
    
    @Bean
    public UploadUserAvatarUseCase uploadUserAvatarUseCase(
            UserRepository userRepository,
            FileStorage fileStorage,
            EventPublisher eventPublisher,
            CacheService cacheService
    ) {
        return new UploadUserAvatarUseCase(userRepository, fileStorage, eventPublisher, cacheService);
    }
}
