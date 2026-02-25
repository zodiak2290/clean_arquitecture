package com.alberto.enterprise.infrastructure.persistence;

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
}
