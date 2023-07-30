package com.templateproject.api.DtoMapper;

import com.templateproject.api.DTO.PromotionDTO;
import com.templateproject.api.entity.Promotion;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class PromotionDTOMapper implements Function<Promotion, PromotionDTO> {

    @Override
    public PromotionDTO apply(Promotion promotion) {
        return new PromotionDTO(
                promotion.getId(),
                promotion.getName(),
                promotion.getTag(),
                promotion.getAuthor().getId(),
                promotion.getDescription(),
                promotion.getBanner(),
                promotion.getPicture(),
                promotion.getRating(),
                promotion.getCreationDate(),
                promotion.getTopics(),
                promotion.getResources(),
                promotion.getParticipants()

        );
    }

    public PromotionDTO convertToDTO(Promotion promotion) {
        return apply(promotion);
    }

    public PromotionDTO convertToEntity(PromotionDTO newPromotionDTO) {
        return newPromotionDTO;
    }
}