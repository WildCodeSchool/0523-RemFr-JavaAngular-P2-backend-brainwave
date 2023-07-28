package com.templateproject.api.service;

import com.templateproject.api.DTO.PromotionDTO;
import com.templateproject.api.DtoMapper.PromotionDTOMapper;
import com.templateproject.api.entity.Promotion;
import com.templateproject.api.repository.PromotionRepository;
import com.templateproject.api.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PromotionService {
    private static PromotionRepository promotionRepository;
    private static PromotionDTOMapper promotionDTOMapper;


    public PromotionService(PromotionRepository promotionRepository,
                            PromotionDTOMapper promotionDTOMapper,
                            UserRepository userRepository) {
        PromotionService.promotionRepository = promotionRepository;
        PromotionService.promotionDTOMapper = promotionDTOMapper;


    }

    public static Optional<PromotionDTO> findPromotionById(UUID id) {
        return promotionRepository.findById(id)
                .map(promotionDTOMapper::convertToDTO);

    }


    public List<PromotionDTO> findAllPromotions() {
        List<Promotion> promotions = promotionRepository.findAll();

        return promotions.stream()
                .map(promotionDTOMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    public void setPromotionRepository(PromotionRepository promotionRepository) {
        PromotionService.promotionRepository = promotionRepository;
    }

    public void setPromotionDTOMapper(PromotionDTOMapper promotionDTOMapper) {
        PromotionService.promotionDTOMapper = promotionDTOMapper;
    }
}