package com.alberto.enterprise.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alberto.enterprise.application.usecase.CreateUserUseCase;
import com.alberto.enterprise.domain.repository.UserRepository;

@Configuration
public class BeanConfig {
	
    @Bean
    public CreateUserUseCase createUserUseCase(UserRepository userRepository) {
        return new CreateUserUseCase(userRepository);
    }
}
