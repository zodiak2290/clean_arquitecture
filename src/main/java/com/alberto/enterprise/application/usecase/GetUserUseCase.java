package com.alberto.enterprise.application.usecase;

import java.util.Optional;

import com.alberto.enterprise.application.port.out.CacheService;
import com.alberto.enterprise.application.port.out.EventPublisher;
import com.alberto.enterprise.domain.model.User;
import com.alberto.enterprise.domain.repository.UserRepository;

/**
 * Use case that attempts to retrieve a user by id.  If the user
 * is stored in the cache we deserialize it and return it directly,
 * otherwise we fall back to the repository and populate the cache.
 */
public class GetUserUseCase {

    private static final long CACHE_TTL_SECONDS = 300;

    private final UserRepository userRepository;
    private final CacheService cacheService;
    private final EventPublisher eventPublisher;

    public GetUserUseCase(UserRepository userRepository, CacheService cacheService, EventPublisher eventPublisher) {
        this.userRepository = userRepository;
        this.cacheService = cacheService;
        this.eventPublisher = eventPublisher;
    }

    public Optional<User> execute(Long userId) {
        String key = "user:" + userId;
        Optional<String> cached = cacheService.get(key);
        if (cached.isPresent()) {
        	eventPublisher.publish("Redis user.get", userId);
            return Optional.of(deserialize(userId, cached.get()));
        }
        eventPublisher.publish("DB user.get", userId);
        Optional<User> user = userRepository.findById(userId);
        user.ifPresent(u -> cacheService.put(key, serialize(u), CACHE_TTL_SECONDS));
        return user;
    }

    private String serialize(User u) {
        return u.getName() + "|" + u.getEmail();
    }

    private User deserialize(Long id, String payload) {
        String[] parts = payload.split("\\|", 2);
        String name = parts.length > 0 ? parts[0] : "";
        String email = parts.length > 1 ? parts[1] : "";
        return new User(id, name, email);
    }
}