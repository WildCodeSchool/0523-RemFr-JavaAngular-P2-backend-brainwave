package com.templateproject.api.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.templateproject.api.entity.Answer;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public record TopicDTO(
        UUID id,
        String title,
        Float upvote,
        UUID promotion_id,
        LocalDateTime creation_date,
        UUID authorId,
        String content,
        @JsonIgnore List<Answer> answers) {

    public List<UUID> getAnswerIds() {
        return answers.stream()
                .map(Answer::getId)
                .collect(Collectors.toList());
    }

    public void setCreationDate(LocalDateTime localDateTimeNow) {
        LocalDateTime creationDate;
    }

    public void setAuthor(UUID authorId) {
        UUID user;
    }

    public void setPromotion(UUID promotion_id) {
        UUID promotion;
    }
}
