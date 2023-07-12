package com.templateproject.api.controller;

import com.templateproject.api.entity.Answer;

import com.templateproject.api.entity.Topic;
import com.templateproject.api.entity.User;
import com.templateproject.api.repository.AnswerRepository;

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
@RequestMapping("answers")
public class AnswerController {
    private final AnswerRepository answerRepository;
    private final UserRepository userRepository;
    private final TopicRepository topicRepository;

    public AnswerController(AnswerRepository answerRepository,
            UserRepository userRepository,
            TopicRepository topicRepository) {
        this.answerRepository = answerRepository;
        this.userRepository = userRepository;
        this.topicRepository = topicRepository;
    }

    @GetMapping("")
    public List<Answer> index() {
        return this.answerRepository.findAll();
    }

    @GetMapping("/{id}")
    public Answer show(@PathVariable UUID id) {
        Optional<Answer> optionalAnswer = this.answerRepository.findById(id);
        if (optionalAnswer.isPresent()) {
            return optionalAnswer.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Promotion not found" + id);
        }
    }

    @PostMapping("/topics/{topicId}/users/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Answer createAnswer(
            @PathVariable UUID userId,
            @PathVariable UUID topicId,
            @RequestBody Answer newAnswer) {

        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found: " + userId));

        Topic topic = topicRepository
                .findById(topicId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Topic not found: " + topicId));

        LocalDateTime localDateTimeNow = LocalDateTime.now();
        newAnswer.setCreationDate(localDateTimeNow);
        newAnswer.setUser(user);
        newAnswer.setTopic(topic);
        return this.answerRepository.save(newAnswer);
    }


    @PutMapping("/{id}/topics/{topicId}")
    public ResponseEntity<Answer> updateAnswer(@PathVariable UUID topicId, @PathVariable UUID id,
            @RequestBody @Validated Answer updatedAnswer) {
        return this.answerRepository.findById(id).map(answer -> {
            BeanUtils.copyNonNullProperties(updatedAnswer, answer);
            return ResponseEntity.ok(answerRepository.save(answer));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        this.answerRepository.deleteById(id);
    }

}
