package com.templateproject.api.controller;

import com.templateproject.api.entity.Role;
import com.templateproject.api.entity.User;
import com.templateproject.api.repository.UserRepository;
import com.templateproject.api.service.TokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "localhost:4200")
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

    private boolean checkCookieToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    return true;
                }
            }
        }
        return false;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public User register(@Valid @RequestBody User user, HttpServletRequest request) {
        if (checkCookieToken(request)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User must logout before registering");
        }

        user.setRole(Role.STUDENT);

        PasswordEncoder passwordEncoder
                = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return this.userRepository.save(user);
    }

    @PostMapping("/login")
    public User login(@RequestBody User user, HttpServletResponse response, HttpServletRequest request) {
        if (checkCookieToken(request)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User must logout before registering");
        }
        Authentication authentication = this.authManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
        var jwt = tokenService.generateToken(authentication);
        if (jwt == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username/password supplied");
        }
        Cookie cookie = new Cookie("token", jwt);
        cookie.setSecure(true);
        cookie.setHttpOnly(false);
        cookie.getValue();
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60);
        response.addCookie(cookie);
        return (User) authentication.getPrincipal();
    }
}