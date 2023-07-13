package com.templateproject.api.DTO;

import org.springframework.stereotype.Service;
import com.templateproject.api.entity.User;

import java.util.function.Function;
@Service
public class UserDTOMapper implements Function<User, UserDTO> {
    @Override
    public UserDTO apply(User user) {
        return new UserDTO(
                user.getId(),
                user.getLastname(),
                user.getFirstname(),
                user.getEmail(),
                user.getRole(),
                user.getTopics(),
                user.getResources()



        );

    }

    public UserDTO convertToDTO(User user) {
        return apply(user);
    }
}
