package com.templateproject.api.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.templateproject.api.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public record EventDTO(
        UUID id,
        String title,
        LocalDateTime duration,
        UUID promotionId,
        LocalDateTime creationDate,
        UUID author,

        @JsonIgnore
        List<User> participants) {

    public List<UUID> getParticipantIds() {
        return participants.stream()
                .map(User::getId)
                .collect(Collectors.toList());
    }

}
