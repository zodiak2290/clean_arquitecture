package com.alberto.enterprise.application.usecase;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.alberto.enterprise.application.port.out.EventPublisher;
import com.alberto.enterprise.infrastructure.cache.InMemoryCacheService;
import com.alberto.enterprise.domain.model.User;
import com.alberto.enterprise.domain.repository.UserRepository;

class GetUserUseCaseTest {
    private InMemoryCacheService cache;
    private CountingRepository repo;
    private GetUserUseCase useCase;
    private EventPublisher eventPublisher;
    @BeforeEach
    void setup() {
        cache = new InMemoryCacheService();
        repo = new CountingRepository();
        eventPublisher = new NoOpEventPublisher();
        useCase = new GetUserUseCase(repo, cache, eventPublisher);
    }

    @Test
    void whenNotCached_fetchesFromRepo_and_caches() {
        User created = repo.save(new User("Alice", "alice@example.com"));
        Optional<User> result = useCase.execute(created.getId());

        assertTrue(result.isPresent());
        assertEquals(created.getName(), result.get().getName());
        assertEquals(created.getEmail(), result.get().getEmail());
        assertEquals(1, repo.findCalls);

        // verify something was stored in cache
        String key = "user:" + created.getId();
        Optional<String> cached = cache.get(key);
        assertTrue(cached.isPresent());
        assertEquals(created.getName() + "|" + created.getEmail(), cached.get());
    }

    @Test
    void whenCached_returnsWithoutRepoCall() {
        User created = repo.save(new User("Bob", "bob@example.com"));
        String key = "user:" + created.getId();
        cache.put(key, created.getName() + "|" + created.getEmail(), 300);

        Optional<User> result = useCase.execute(created.getId());
        assertTrue(result.isPresent());
        assertEquals("Bob", result.get().getName());
        assertEquals("bob@example.com", result.get().getEmail());
        assertEquals(0, repo.findCalls);
    }

    private static class CountingRepository implements UserRepository {
        Map<Long, User> map = new HashMap<>();
        long nextId = 1;
        int findCalls = 0;

        @Override
        public User save(User user) {
            User u = new User(nextId++, user.getName(), user.getEmail());
            map.put(u.getId(), u);
            return u;
        }

        @Override
        public Optional<User> findById(Long id) {
            findCalls++;
            return Optional.ofNullable(map.get(id));
        }

        @Override
        public User updateAvatar(Long id, String avatarUrl) {
            User u = map.get(id);
            if (u == null) {
                throw new RuntimeException("not found");
            }
            User updated = new User(u.getId(), u.getName(), u.getEmail());
            map.put(id, updated);
            return updated;
        }
    }
    
    static class NoOpEventPublisher implements EventPublisher {
        @Override
        public void publish(String topic, Object payload) {
            // no-op
        }
    }
}