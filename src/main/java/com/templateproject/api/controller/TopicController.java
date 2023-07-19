package com.templateproject.api.controller;


import com.templateproject.api.DTO.TopicDTO;
import com.templateproject.api.DtoMapper.TopicDTOMapper;
import com.templateproject.api.entity.Promotion;
import com.templateproject.api.entity.Topic;
import com.templateproject.api.entity.User;
import com.templateproject.api.repository.PromotionRepository;
import com.templateproject.api.repository.TopicRepository;
import com.templateproject.api.repository.UserRepository;
import com.templateproject.api.service.BeanUtils;

import com.templateproject.api.service.TopicService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/topics")
public class TopicController {
    private final TopicRepository topicRepository;
    private final UserRepository userRepository;
    private final PromotionRepository promotionRepository;
    private final TopicDTOMapper topicDTOMapper;

    public TopicController(TopicRepository topicRepository, PromotionRepository promotionRepository,
                           UserRepository userRepository, TopicDTOMapper topicDTOMapper) {

        this.topicRepository = topicRepository;
        this.promotionRepository = promotionRepository;
        this.userRepository = userRepository;
        this.topicDTOMapper = topicDTOMapper;
    }

    @GetMapping("/promotions/{id}")
    public List<TopicDTO> findAllTopics(@PathVariable("id") UUID promotionId) {
        TopicService topicService = new TopicService(topicRepository, topicDTOMapper);
        List<TopicDTO> topicDTOs = topicService.findAllTopics();
        return topicDTOs;
    }

    @GetMapping("/{id}")
    public TopicDTO show(@PathVariable UUID id) {
        Optional<TopicDTO> optionalTopicDTO = TopicService.findById(id);

        if (optionalTopicDTO.isPresent()) {
            return optionalTopicDTO.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Promotion not found " + id);
        }

    }

    @GetMapping("/{id}/promotions/{promoId}")
    public List<TopicDTO> getTopicsByPromotionId(@PathVariable UUID promoId,
                                                 @PathVariable UUID id) {
        TopicService topicService = new TopicService(topicRepository, topicDTOMapper);
        List<TopicDTO> topicDTOs = topicService.findByPromotionId(promoId);
        return topicDTOs;
    }

    @GetMapping("/{id}/users/{userId}")
    public List<TopicDTO> getTopicsByUser(@PathVariable UUID userId,
                                          @PathVariable String id) {
        TopicService topicService = new TopicService(topicRepository, topicDTOMapper);
        List<TopicDTO> topicDTOs = topicService.findByAuthorId(userId);


        return topicDTOs;
    }

    @PostMapping("/promotions/{promoId}/user/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Topic createTopic(
            @PathVariable UUID userId,
            @PathVariable UUID promoId,
            @RequestBody Topic newTopic) {
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Not Found" + userId));
        Promotion promotion = promotionRepository
                .findById(promoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Not Found" + promoId));
        LocalDateTime localDateTimeNow = LocalDateTime.now();
        newTopic.setCreationDate(localDateTimeNow);
        newTopic.setAuthor(user);
        newTopic.setPromotion(promotion);

        return this.topicRepository.save(newTopic);
    }

    @PutMapping("/{id}/promotions/{promoId}")
    public ResponseEntity<TopicDTO> updateTopic(
            @PathVariable UUID promoId,
            @PathVariable UUID id,
            @RequestBody @Validated Topic topicDTO) {
        Topic updatedTopic = topicRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Topic not found: " + id));
        BeanUtils.copyNonNullProperties(topicDTO, updatedTopic);
        Topic savedTopic = topicRepository.save(updatedTopic);

        TopicDTO updatedTopicDTO = topicDTOMapper.convertToDTO(savedTopic);
        return ResponseEntity.ok(updatedTopicDTO);
    }

    @PutMapping("/{id}/users/{userId}/add-authors")
    public ResponseEntity<TopicDTO> addAuthor(@PathVariable UUID id,
                                              @PathVariable UUID userId,
                                              @RequestBody @Validated Topic addAuthor) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found: " + userId));

        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Topic not found: " + id));

        topic.setAuthor(user);

        BeanUtils.copyNonNullProperties(addAuthor, topic);
        Topic updatedTopic = topicRepository.save(topic);
        TopicDTO addedAuthorDTO = topicDTOMapper.convertToDTO(updatedTopic);
        return ResponseEntity.ok(addedAuthorDTO);
    }

    @PutMapping("/{id}/promotions/{promoId}/add-promotions")
    public ResponseEntity<TopicDTO> addPromotion(@PathVariable UUID id,
                                                 @PathVariable UUID promoId,
                                                 @RequestBody @Validated Topic addPromotion) {

        Promotion promotion = promotionRepository.findById(promoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Promotion not found: " + promoId));

        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Topic not found: " + id));

        topic.setPromotion(promotion);

        BeanUtils.copyNonNullProperties(addPromotion, topic);
        Topic updatedTopic = topicRepository.save(topic);
        TopicDTO addedAuthorDTO = topicDTOMapper.convertToDTO(updatedTopic);
        return ResponseEntity.ok(addedAuthorDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
    public void delete(@PathVariable UUID id) {
        this.topicRepository.deleteById(id);

    }

}
