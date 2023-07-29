package com.templateproject.api.DtoMapper;

import com.templateproject.api.DTO.UserDTO;
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
                user.getAvatar(),
                user.getDescription(),
                user.getTopics(),
                user.getResources(),
                user.getPromotions(),
                user.getAnswers(),
                user.getPromotionsParticipants(),
                user.getEventsParticipated(),
                user.getEventsCreated()
        );

    }

    public UserDTO convertToDTO(User user) {
        return apply(user);
    }
}
