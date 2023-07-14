package com.templateproject.api.controller;

import com.templateproject.api.DTO.UserDTO;
import com.templateproject.api.DtoMapper.UserDTOMapper;
import com.templateproject.api.entity.User;
import com.templateproject.api.repository.UserRepository;
import com.templateproject.api.service.BeanUtils;
import com.templateproject.api.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;
    private final UserDTOMapper userDTOMapper;

    public UserController(UserRepository userRepository, UserDTOMapper userDTOMapper) {
        this.userRepository = userRepository;
        this.userDTOMapper = userDTOMapper;
    }

   /* @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }*/

    /*@GetMapping("/{id}")
     public ResponseEntity<User> getUserById(@PathVariable UUID id) {
         return userRepository.findById(id)
                 .map(ResponseEntity::ok)
                 .orElseGet(() -> ResponseEntity.notFound().build());
     }*/
    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable UUID id) {
        Optional<UserDTO> optionalUserDTO = UserService.findById(id);
        if (optionalUserDTO.isPresent()) {
            return optionalUserDTO.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found " + id);
        }
    }

    @GetMapping("")
    public List<UserDTO> getAllUsers() {
        UserService userService = new UserService(userRepository, userDTOMapper);
        List<UserDTO> userDTOs = userService.findAllUsers();
        return userDTOs;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody @Validated User user) {
        User createdUser = userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    /*@PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable UUID id, @RequestBody @Validated User updatedUser) {
        return userRepository.findById(id).map(user -> {
            BeanUtils.copyNonNullProperties(updatedUser, user);
            return ResponseEntity.ok(userRepository.save(user));
        }).orElse(ResponseEntity.notFound().build());
    }*/
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable UUID id,
                                              @RequestBody @Validated User userDTO) {
        User updatedUser = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Promotion not found: " + id));

        BeanUtils.copyNonNullProperties(userDTO, updatedUser);
        User savedUser = userRepository.save(updatedUser);

        UserDTO updatedPromotionDTO = userDTOMapper.convertToDTO(savedUser);
        return ResponseEntity.ok(updatedPromotionDTO);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable UUID id) {
        return userRepository.findById(id).map(user -> {
            userRepository.delete(user);
            return ResponseEntity.noContent().build();
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
