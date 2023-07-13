package com.templateproject.api.service;

import java.util.List;
import java.util.stream.Collectors;

import com.templateproject.api.DTO.UserDTO;
import com.templateproject.api.DTO.UserDTOMapper;
import com.templateproject.api.entity.User;
import com.templateproject.api.repository.UserRepository;

public class UserService {
    private UserRepository userRepository;
    private UserDTOMapper userDTOMapper;

    public UserService(UserRepository userRepository, UserDTOMapper userDTOMapper) {
        this.userRepository = userRepository;
        this.userDTOMapper = userDTOMapper;
    }

    public List<UserDTO> findAllUsers() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(userDTOMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    // Ajouter les méthodes pour injecter les dépendances
    public void setuserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void setuserDTOMapper(UserDTOMapper userDTOMapper) {
        this.userDTOMapper = userDTOMapper;
    }
}
