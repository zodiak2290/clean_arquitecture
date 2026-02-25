package com.alberto.enterprise.infrastructure.persistence;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.alberto.enterprise.domain.model.User;
import com.alberto.enterprise.domain.repository.UserRepository;
import com.alberto.enterprise.infrastructure.entity.UserEntity;

@Repository
public class JpaUserRepository implements UserRepository {
    private final SpringDataUserRepository repository;

    public JpaUserRepository(SpringDataUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User save(User user) {
        UserEntity entity = new UserEntity(user.getName(), user.getEmail());
        UserEntity saved = repository.save(entity);
        return new User(saved.getId(), saved.getName(), saved.getEmail());
    }
    
    @Override
    public Optional<User> findById(Long id) {
        return repository.findById(id)
                .map(e -> new User(e.getId(), e.getName(), e.getEmail())); // si luego agregas avatar al domain, lo mapeas
    }

    @Override
    public User updateAvatar(Long id, String avatarUrl) {
        UserEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));

        entity.setAvatarUrl(avatarUrl);
        UserEntity saved = repository.save(entity);

        // Si tu domain User aún no tiene avatarUrl, por ahora regresamos lo básico:
        return new User(saved.getId(), saved.getName(), saved.getEmail());
    }
}
