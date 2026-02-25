package com.alberto.enterprise.application.usecase;

import com.alberto.enterprise.domain.model.User;
import com.alberto.enterprise.domain.repository.UserRepository;

public class CreateUserUseCase {

    private final UserRepository userRepository;

    public CreateUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User execute(String name, String email) {
        User user = new User(name, email);
        return userRepository.save(user);
    }
}
