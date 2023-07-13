package com.templateproject.api.service;

import com.templateproject.api.DTO.PromotionDTO;
import com.templateproject.api.DTO.PromotionDTOMapper;
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



    public PromotionService(PromotionRepository promotionRepository, PromotionDTOMapper promotionDTOMapper) {
        PromotionService.promotionRepository = promotionRepository;
        PromotionService.promotionDTOMapper = promotionDTOMapper;

    }

    public static Optional<PromotionDTO> findPromotionById(UUID id) {
        return promotionRepository.findById(id)
                .map(promotionDTOMapper::convertToDTO);

    }

   /* public PromotionDTO createPromotion(UUID userId, PromotionDTO newPromotionDTO) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: " + userId));


        PromotionDTO newPromotion = promotionDTOMapper.convertToEntity(newPromotionDTO);


        LocalDateTime localDateTimeNow = LocalDateTime.now();
        newPromotion.setCreationDate(localDateTimeNow);
        newPromotion.setAuthor(user);

        // Enregistrer la promotion créée dans la base de données
        Promotion createdPromotion = promotionRepository.save(newPromotion);

        // Convertir la promotion créée en PromotionDTO
        PromotionDTO createdPromotionDTO = promotionDTOMapper.convertToDTO(createdPromotion);

        return createdPromotionDTO;
    }
*/

    public List<PromotionDTO> findAllPromotions() {
        List<Promotion> promotions = promotionRepository.findAll();

        return promotions.stream()
                .map(promotionDTOMapper::convertToDTO)
                .collect(Collectors.toList());
    }


    public void setPromotionRepository(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    public void setPromotionDTOMapper(PromotionDTOMapper promotionDTOMapper) {
        this.promotionDTOMapper = promotionDTOMapper;
    }
}
