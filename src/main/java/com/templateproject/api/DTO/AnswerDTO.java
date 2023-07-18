package com.templateproject.api.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.templateproject.api.entity.Topic;

import java.time.LocalDateTime;
import java.util.UUID;

public record AnswerDTO(

        UUID id,
        Float upvote,
        LocalDateTime creationDate,
        @JsonIgnore Topic content,
        String topic,
        UUID authorId

) {
    public void setAuthor(UUID authorId) {
        UUID user;
    }

}
