package com.templateproject.api.service;

import com.templateproject.api.DTO.AnswerDTO;
import com.templateproject.api.DtoMapper.AnswerDTOMapper;
import com.templateproject.api.entity.Answer;
import com.templateproject.api.repository.AnswerRepository;
import com.templateproject.api.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AnswerService {
    private static AnswerRepository answerRepository;
    private static AnswerDTOMapper answerDTOMapper;
    private final UserRepository userRepository;


    public AnswerService(AnswerRepository answerRepository,
                         AnswerDTOMapper answerDTOMapper,
                         UserRepository userRepository) {
        this.answerRepository = answerRepository;
        this.answerDTOMapper = answerDTOMapper;
        this.userRepository = userRepository;
    }

    public static Optional<AnswerDTO> findById(UUID id) {
        return answerRepository.findById(id)
                .map(answerDTOMapper::convertToDTO);
    }

    public List<AnswerDTO> findAllAnswers() {
        List<Answer> answers = answerRepository.findAll();
        return answers.stream()
                .map(answerDTOMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AnswerDTO> findByTopicId(UUID topicId) {
        List<Answer> answers = answerRepository.findByTopicId(topicId);
        return answers.stream()
                .map(answerDTOMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AnswerDTO> findByAuthor(UUID userId) {
        List<Answer> answers = answerRepository.findByAuthorId(userId);
        return answers.stream()
                .map(answerDTOMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    public void setAnswerRepository(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    public void setAnswerDTOMapper(AnswerDTOMapper answerDTOMapper) {
        this.answerDTOMapper = answerDTOMapper;
    }
}
