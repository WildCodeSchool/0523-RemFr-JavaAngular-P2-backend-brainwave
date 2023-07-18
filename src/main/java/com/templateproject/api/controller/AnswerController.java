package com.templateproject.api.controller;

import com.templateproject.api.DTO.AnswerDTO;
import com.templateproject.api.DtoMapper.AnswerDTOMapper;
import com.templateproject.api.entity.*;

import com.templateproject.api.repository.AnswerRepository;

import com.templateproject.api.repository.TopicRepository;
import com.templateproject.api.repository.UserRepository;

import com.templateproject.api.service.AnswerService;
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
    private final AnswerDTOMapper answerDTOMapper;

    public AnswerController(AnswerRepository answerRepository,
                            UserRepository userRepository,
                            TopicRepository topicRepository,
                            AnswerDTOMapper answerDTOMapper) {
        this.answerRepository = answerRepository;
        this.userRepository = userRepository;
        this.topicRepository = topicRepository;
        this.answerDTOMapper = answerDTOMapper;
    }

    @GetMapping("/users/{userId}")
    public List<AnswerDTO> getAllAnswers(@PathVariable String userId) {
        AnswerService answerService = new AnswerService(
                answerRepository, answerDTOMapper, userRepository
        );
        return answerService.findAllAnswers();
    }

    @GetMapping("/{id}")
    public AnswerDTO getAnswersId(@PathVariable UUID id) {
        Optional<AnswerDTO> optionalAnswerDTO = AnswerService.findById(id);
        if (optionalAnswerDTO.isPresent()) {
            return optionalAnswerDTO.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Answer not found : " + id);
        }
    }

    @GetMapping("/{id}/users/{userId}")
    public AnswerDTO getAnswersByUser(@PathVariable UUID id, @PathVariable String userId) {
        Optional<AnswerDTO> optionalAnswerDTO = AnswerService.findById(id);
        if (optionalAnswerDTO.isPresent()) {
            return optionalAnswerDTO.get();
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Answer not found : " + id + "for user : " + userId);
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
    public ResponseEntity<AnswerDTO> updateAnswer(@PathVariable UUID topicId,
                                                  @PathVariable UUID id,
                                                  @RequestBody @Validated Answer answerDTO) {
        Answer updatedAnswer = answerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Answer not found : " +id));
        BeanUtils.copyNonNullProperties(answerDTO, updatedAnswer);
        Answer savedAnswer = answerRepository.save(updatedAnswer);
        AnswerDTO updatedAnswerDTO = answerDTOMapper.convertToDTO(savedAnswer);
        return ResponseEntity.ok(updatedAnswerDTO);
    }

    @PutMapping("/{id}/users/{userId}/add-authors")
    public ResponseEntity<AnswerDTO> addAuthor(@PathVariable UUID id,
                                               @PathVariable UUID userId,
                                               @RequestBody @Validated Answer addAuthor) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found: " + userId));

        Answer answer = answerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Answer not found: " + id));

        answer.setUser(user);
        BeanUtils.copyNonNullProperties(addAuthor, answer);
        Answer updatedAnswer = answerRepository.save(answer);
        AnswerDTO addedAuthorDTO = answerDTOMapper.convertToDTO(updatedAnswer);
        return ResponseEntity.ok(addedAuthorDTO);
    }

    @PutMapping("/{id}/topics/{topicId}/add-topics")
    public ResponseEntity<AnswerDTO> addTopic(@PathVariable UUID id,
                                              @PathVariable UUID topicId,
                                              @RequestBody @Validated Answer addPromotion) {

        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Topic not found: " + topicId));

        Answer answer = answerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Answer not found: " + id));

        answer.setTopic(topic);
        BeanUtils.copyNonNullProperties(addPromotion, answer);
        Answer updatedAnswer = answerRepository.save(answer);
        AnswerDTO addedAuthorDTO = answerDTOMapper.convertToDTO(updatedAnswer);
        return ResponseEntity.ok(addedAuthorDTO);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        this.answerRepository.deleteById(id);
    }

}