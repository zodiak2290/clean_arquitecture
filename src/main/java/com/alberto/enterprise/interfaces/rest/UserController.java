package com.alberto.enterprise.interfaces.rest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alberto.enterprise.application.usecase.CreateUserUseCase;
import com.alberto.enterprise.domain.model.User;

@RestController
@RequestMapping("/users")
public class UserController {
    private final CreateUserUseCase createUserUseCase;

    public UserController(CreateUserUseCase createUserUseCase) {
        this.createUserUseCase = createUserUseCase;
    }

    @PostMapping
    public User create(@RequestBody CreateUserRequest request) {
        return createUserUseCase.execute(request.name(), request.email());
    }

    record CreateUserRequest(String name, String email) {}
}
