package com.templateproject.api.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.templateproject.api.entity.Resource;
import com.templateproject.api.entity.Topic;
import com.templateproject.api.entity.User;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public record PromotionDTO(UUID id,
                           String name,
                           String tag,
                           UUID authorId,
                           String description,
                           String picture,
                           LocalDateTime creationDate,
                           @JsonIgnore List<Topic> topics,
                           @JsonIgnore List<Resource> resources,
                           @JsonIgnore Set<User> participants

) {
    public LocalDateTime setCreationDate(LocalDateTime localDateTimeNow) {
        return localDateTimeNow;
    }

    public List<UUID> getResourceIds() {
        return resources.stream()
                .map(Resource::getId)
                .collect(Collectors.toList());
    }

    public Set<UUID> getParticipantsIds() {
        return participants.stream()
                .map(User::getId)
                .collect(Collectors.toSet());
    }

    public List<UUID> getTopicIds() {
        return topics.stream()
                .map(Topic::getId)
                .collect(Collectors.toList());
    }

}
