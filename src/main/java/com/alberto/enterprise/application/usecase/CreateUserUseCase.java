package com.alberto.enterprise.application.usecase;

import com.alberto.enterprise.application.port.out.CacheService;
import com.alberto.enterprise.application.port.out.EventPublisher;
import com.alberto.enterprise.domain.model.User;
import com.alberto.enterprise.domain.repository.UserRepository;

public class CreateUserUseCase {

    private final UserRepository userRepository;
    private final EventPublisher eventPublisher;
    private final CacheService cacheService;

    public CreateUserUseCase(UserRepository userRepository, EventPublisher eventPublisher, CacheService cacheService) {
    	  this.userRepository = userRepository;
    	  this.eventPublisher = eventPublisher;
    	  this.cacheService= cacheService;
    	  
    	}

    	public User execute(String name, String email) {
    	  User saved = userRepository.save(new User(name, email));
    	  eventPublisher.publish("user.created", saved.getEmail());
    	  cacheService.put("user:" + saved.getId(), saved.getEmail(), 300);
    	  return saved;
    	}
}
