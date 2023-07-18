package com.templateproject.api.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.templateproject.api.DTO.UserDTO;
import com.templateproject.api.DtoMapper.UserDTOMapper;
import com.templateproject.api.entity.User;
import com.templateproject.api.repository.UserRepository;

public class UserService {
    private static UserRepository userRepository;
    private static UserDTOMapper userDTOMapper;

    public UserService(UserRepository userRepository, UserDTOMapper userDTOMapper) {
        this.userRepository = userRepository;
        this.userDTOMapper = userDTOMapper;
    }

    public static Optional<UserDTO> findById(UUID id) {
        return userRepository.findById(id)
                .map(userDTOMapper::convertToDTO);
    }

    public List<UserDTO> findAllUsers() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(userDTOMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    public static Optional<UserDTO> findUserById(UUID id) {
        return userRepository.findById(id)
                .map(userDTOMapper::convertToDTO);

    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void setUserDTOMapper(UserDTOMapper userDTOMapper) {
        this.userDTOMapper = userDTOMapper;
    }
}
