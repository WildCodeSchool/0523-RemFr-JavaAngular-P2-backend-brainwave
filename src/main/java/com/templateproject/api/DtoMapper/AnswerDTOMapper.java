package com.templateproject.api.DtoMapper;

import com.templateproject.api.DTO.AnswerDTO;
import com.templateproject.api.entity.Answer;
import org.springframework.stereotype.Service;


import java.util.function.Function;

@Service
public class AnswerDTOMapper implements Function<Answer, AnswerDTO> {
    @Override
    public AnswerDTO apply(Answer answer) {
        return new AnswerDTO(
                answer.getId(),
                answer.getUpvote(),
                answer.getCreationDate(),
                answer.getTopic(),
                answer.getContent(),
                answer.getUser() == null ? null : answer.getUser().getId()

        );
    }

    public AnswerDTO convertToDTO(Answer answer) {
        return apply(answer);
    }
}
