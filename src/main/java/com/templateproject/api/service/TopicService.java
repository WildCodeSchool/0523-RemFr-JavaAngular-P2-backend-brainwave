package com.templateproject.api.service;

import com.templateproject.api.DTO.TopicDTO;
import com.templateproject.api.DtoMapper.TopicDTOMapper;
import com.templateproject.api.entity.Topic;
import com.templateproject.api.repository.TopicRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
@Service
public class TopicService {
    private static TopicRepository topicRepository;
    private static TopicDTOMapper topicDTOMapper;

    public TopicService(TopicRepository topicRepository, TopicDTOMapper topicDTOMapper) {
        this.topicRepository = topicRepository;
        this.topicDTOMapper = topicDTOMapper;
    }

    public static Optional<TopicDTO> findById(UUID id) {
        return topicRepository.findById(id)
                .map(topicDTOMapper::convertToDTO);

    }

    public List<TopicDTO> findAllTopics() {
        List<Topic> topics = topicRepository.findAll();

        return topics.stream()
                .map(topicDTOMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    public static Optional<TopicDTO> findTopicById(UUID id) {
        return topicRepository.findById(id)
                .map(topicDTOMapper::convertToDTO);

    }
    public List<TopicDTO> findByPromotionId(UUID promoId) {
        List<Topic> topics = topicRepository.findByPromotionId(promoId);
        return topics.stream()
                .map(topicDTOMapper::convertToDTO)
                .collect(Collectors.toList());
    }
    public List<TopicDTO> findByAuthorId(UUID userId) {
        List<Topic> topics = topicRepository.findByAuthorId(userId);
        return topics.stream()
                .map(topicDTOMapper::convertToDTO)
                .collect(Collectors.toList());
    }
    public void setTopicRepository(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    public void setTopicDTOMapper(TopicDTOMapper topicDTOMapper) {
        this.topicDTOMapper = topicDTOMapper;
    }



}
