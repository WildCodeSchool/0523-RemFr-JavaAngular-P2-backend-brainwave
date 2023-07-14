package com.templateproject.api.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.templateproject.api.entity.*;

import java.util.*;
import java.util.stream.Collectors;

public record UserDTO(
                UUID id,
                String lastname,
                String firstname,
                String email,
                Enum role,
                @JsonIgnore List<Topic> topics,
                @JsonIgnore List<Resource> resources,
                @JsonIgnore List<Promotion> promotions,
                @JsonIgnore List<Answer> answers,
                @JsonIgnore Set<Promotion> promotionsParticipants,
                @JsonIgnore List<Event> eventsParticipated,
                @JsonIgnore List<Event> eventsCreated) {

        public List<UUID> getResourceIds() {
                return resources.stream()
                                .map(Resource::getId)
                                .collect(Collectors.toList());
        }

        public List<UUID> getPromotionIds() {
                return promotions.stream()
                                .map(Promotion::getId)
                                .collect(Collectors.toList());
        }

        public Set<UUID> getPromotionParticipantsIds() {
                return promotions.stream()
                                .map(Promotion::getId)
                                .collect(Collectors.toSet());
        }

        public List<UUID> getTopicIds() {
                return topics.stream()
                                .map(Topic::getId)
                                .collect(Collectors.toList());
        }

        public List<UUID> getAnswerIds() {
                return answers.stream()
                                .map(Answer::getId)
                                .collect(Collectors.toList());
        }

        public List<UUID> getEventIds() {
                return eventsCreated.stream()
                                .map(Event::getId)
                                .collect(Collectors.toList());
        }

        public List<UUID> getEventParticipatedIds() {
                return eventsParticipated.stream()
                                .map(Event::getId)
                                .collect(Collectors.toList());
        }
}
