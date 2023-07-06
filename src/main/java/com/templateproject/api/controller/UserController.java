package com.templateproject.api.controller;


import com.templateproject.api.entity.User;
import com.templateproject.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }
}
