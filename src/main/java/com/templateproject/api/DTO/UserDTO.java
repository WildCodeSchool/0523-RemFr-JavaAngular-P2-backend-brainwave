package com.templateproject.api.DTO;

import com.templateproject.api.entity.Resource;
import com.templateproject.api.entity.Topic;

import java.util.*;

public record UserDTO(
        UUID id,
        String lastname,
        String firstname,
        String email,
        Enum role,

        List<Topic> topics,
        List<Resource> resources) {
}
