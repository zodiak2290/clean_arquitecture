package com.alberto.enterprise.interfaces.rest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alberto.enterprise.infrastructure.security.JwtService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtService jwtService;

    public AuthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public TokenResponse login(@RequestBody LoginRequest req) {
        // MVP: acepta cualquier user/pass; luego lo conectamos a Users/DB
        String token = jwtService.generateToken(req.username());
        return new TokenResponse(token);
    }

    record LoginRequest(String username, String password) {}
    record TokenResponse(String token) {}
}
