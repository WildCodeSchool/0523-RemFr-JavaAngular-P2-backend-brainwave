package com.templateproject.api.controller;

import com.templateproject.api.entity.Answer;
import com.templateproject.api.entity.Promotion;
import com.templateproject.api.entity.Topic;
import com.templateproject.api.entity.User;
import com.templateproject.api.repository.PromotionRepository;
import com.templateproject.api.repository.TopicRepository;
import com.templateproject.api.repository.UserRepository;
import com.templateproject.api.service.BeanUtils;

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

    public TopicController(TopicRepository topicRepository, PromotionRepository promotionRepository,
            UserRepository userRepository) {

        this.topicRepository = topicRepository;
        this.promotionRepository = promotionRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/promotions/{id}")
    public List<Topic> index() {
        return topicRepository.findAll();
    }

    @GetMapping("/{id}")
    public Topic show(@PathVariable UUID id) {
        Optional<Topic> optionalTopic = this.topicRepository.findById(id);

        if (optionalTopic.isPresent()) {
            return optionalTopic.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Promotion not found " + id);
        }

    }

    @GetMapping("/{id}/promotions/{promoId}")
    public List<Topic> getTopicsByPromotionId(@PathVariable UUID promoId, @PathVariable UUID id) {
        return topicRepository.findByPromotionId(promoId);
    }

    @GetMapping("/{id}/users/{userId}")
    public List<Topic> getTopicsByUser(@PathVariable UUID userId, @PathVariable String id) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: " + userId));

        return topicRepository.findByAuthor(user);
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
    public ResponseEntity<Topic> updateTopic(@PathVariable UUID promoId, @PathVariable UUID id,
            @RequestBody @Validated Topic updatedTopic) {
        return this.topicRepository.findById(id).map(topic -> {
            BeanUtils.copyNonNullProperties(updatedTopic, topic);
            return ResponseEntity.ok(topicRepository.save(topic));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        this.topicRepository.deleteById(id);
    }

}
