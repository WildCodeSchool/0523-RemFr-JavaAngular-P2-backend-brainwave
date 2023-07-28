package com.templateproject.api.controller;

import com.templateproject.api.DTO.PromotionDTO;
import com.templateproject.api.DTO.UserDTO;
import com.templateproject.api.DtoMapper.UserDTOMapper;
import com.templateproject.api.entity.Promotion;
import com.templateproject.api.entity.User;
import com.templateproject.api.repository.UserRepository;
import com.templateproject.api.service.BeanUtils;
import com.templateproject.api.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;
    private final UserDTOMapper userDTOMapper;
    private static final String AVATAR_FOLDER = "src/main/resources/static/avatars/";


    public UserController(UserRepository userRepository, UserDTOMapper userDTOMapper) {
        this.userRepository = userRepository;
        this.userDTOMapper = userDTOMapper;
    }

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

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable UUID id,
                                              @RequestBody @Validated User userDTO) {
        User updatedUser = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Promotion not found: " + id));

        User savedUser = userRepository.save(updatedUser);

        UserDTO updatedPromotionDTO = userDTOMapper.convertToDTO(savedUser);
        return ResponseEntity.ok(updatedPromotionDTO);
    }

    @PutMapping("/{userId}/avatar")
    public ResponseEntity<Map<String, String>> uploadAvatar(@PathVariable UUID userId, @RequestParam("avatar") MultipartFile avatar) {
        try {
            if (!Objects.requireNonNull(avatar.getContentType()).startsWith("image/")) {
                throw new IllegalArgumentException("File must be an image");
            }

            if (avatar.getSize() > 1_000_000) {
                throw new MaxUploadSizeExceededException(1_000_000);
            }

            String extension = avatar.getContentType().split("/")[1];

            byte[] bytes = avatar.getBytes();
            Path path = Paths.get(AVATAR_FOLDER + userId.toString() + "." + extension);
            Files.write(path, bytes, StandardOpenOption.CREATE);

            Map<String, String> response = new HashMap<>();
            response.put("avatarUrl", "/avatars/" + userId.toString() + "." + extension);
            this.userRepository.findById(userId).ifPresent(user -> {
                user.setAvatar(userId.toString() + "." + extension);
                this.userRepository.save(user);
            });
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            throw new RuntimeException("Error occurred while storing avatar image", e);
        }
    }

    @GetMapping("/avatar/{filename}")
    public ResponseEntity<byte[]> getAvatar(@PathVariable String filename) throws IOException {
        String avatarPath = AVATAR_FOLDER + filename;
        byte[] imageBytes = Files.readAllBytes(Paths.get(avatarPath));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);

        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable UUID id) {
        return userRepository.findById(id).map(user -> {
            userRepository.delete(user);
            return ResponseEntity.noContent().build();
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @PostMapping("/search")
    public List<UserDTO> search(@RequestBody Map<String, String> body) {
        String searchTerm = body.get("content");
        List<User> users = userRepository
                .findUserByFirstnameIsContainingOrLastnameIsContainingIgnoreCase(searchTerm, searchTerm);
        return users.stream()
                .map(userDTOMapper::convertToDTO)
                .collect(Collectors.toList());
    }

}
