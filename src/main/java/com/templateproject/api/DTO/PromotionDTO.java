package com.templateproject.api.DTO;

import com.templateproject.api.entity.Resource;
import com.templateproject.api.entity.Topic;
import com.templateproject.api.entity.User;

import java.time.LocalDateTime;
import java.util.*;


public record PromotionDTO(UUID id,
                           String name,
                           String tag,
                           UUID author,
                           List<Topic> topics,
                           List<Resource> resources,
                           Set<User> participants

) {
    public LocalDateTime setCreationDate(LocalDateTime localDateTimeNow) {
        return localDateTimeNow;
    }

    public User setAuthor(User user) {
        return user;
    }
}
