package com.templateproject.api.DtoMapper;


import com.templateproject.api.DTO.TopicDTO;
import org.springframework.stereotype.Service;
import com.templateproject.api.entity.Topic;

import java.util.function.Function;

@Service
public class TopicDTOMapper implements Function<Topic, TopicDTO> {
    @Override
    public TopicDTO apply(Topic topic) {
        return new TopicDTO(
                topic.getId(),
                topic.getTitle(),
                topic.getUpvote(),
                topic.getPromotion().getId(),
                topic.getCreationDate(),
                topic.getAuthor().getId(),
                topic.getContent(),
                topic.getAnswers()
        );
    }

    public TopicDTO convertToDTO(Topic topic) {
        return apply(topic);
    }

    public Topic convertToEntity(TopicDTO topicDTO) {

        return new Topic();
    }

}
