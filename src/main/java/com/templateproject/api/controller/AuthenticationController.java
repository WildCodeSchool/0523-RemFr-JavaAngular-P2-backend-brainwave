package com.templateproject.api.controller;

import com.templateproject.api.entity.Role;
import com.templateproject.api.entity.User;
import com.templateproject.api.repository.UserRepository;
import com.templateproject.api.service.TokenService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final UserRepository userRepository;
    private final AuthenticationManager authManager;
    private final TokenService tokenService;

    public AuthenticationController(
            UserRepository userRepositoryInjected,
            @Qualifier("authenticationManager") AuthenticationManager authManagerInjected,
            TokenService tokenServiceInjected
    ) {
        this.userRepository = userRepositoryInjected;
        this.authManager = authManagerInjected;
        this.tokenService = tokenServiceInjected;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public User register(@RequestBody User user) {

        user.setRole(Role.STUDENT);

        PasswordEncoder passwordEncoder
                = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return this.userRepository.save(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody User user) {
        Authentication authentication = this.authManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
        return tokenService.generateToken(authentication);
    }
}